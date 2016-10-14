package com.salidasnow.salidasnow;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCercaDeMi extends Fragment implements LocationListener {

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    TextView latitud;
    TextView longitud;
    Context thisContext;
    public LocationManager handle;
    private String provider;

    public FragmentCercaDeMi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_cerca_de_mi, container, false);

        thisContext = container.getContext();


        if (ContextCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        } else {
            IniciarServicio();

        }

        latitud = (TextView) vista.findViewById(R.id.latitud);
        longitud = (TextView) vista.findViewById(R.id.longitud);


        return vista;
    }

    void setEstadoServicio(boolean x) {
        if (x) {
            IniciarServicio();
            muestraPosicionActual();
        } else {
            pararServicio();
        }
    }

    public void IniciarServicio() {
        Toast.makeText(thisContext, "Busqueda de ubicación activada", Toast.LENGTH_SHORT).show();
        handle = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        provider = handle.getBestProvider(c, true);
        Toast.makeText(thisContext, "Proveedor: " + provider, Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (provider =="passive")
        {
            Toast.makeText(thisContext, "Activa el gps", Toast.LENGTH_SHORT).show();
        }
        else if (provider == null)
        {
            Toast.makeText(thisContext, "Permite el acceso a tu ubicación", Toast.LENGTH_SHORT).show();
        }
        else {
            handle.requestLocationUpdates(provider, 10000, 1, this);
        }
    }

    public void muestraPosicionActual() {
        if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = getLastKnownLocation();
        if (location == null) {
            latitud.setText("Latitud: desconocida");
            longitud.setText("Longitud: desconocida");
        } else {
            latitud.setText("Latitud: " + String.valueOf(location.getLatitude()));
            longitud.setText("Longitud: " + String.valueOf(location.getLongitude()));
        }

    }

    public void pararServicio() {
        if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        handle.removeUpdates(this);
        Toast.makeText(thisContext, "Busqueda de ubicación desactivada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (location == null) {
            latitud.setText("Latitud: desconocida");
            longitud.setText("Longitud: desconocida");
        } else {
            latitud.setText("Latitud: " + String.valueOf(location.getLatitude()));
            longitud.setText("Longitud: " + String.valueOf(location.getLongitude()));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                    setEstadoServicio(true);
                } else {
                    Toast.makeText(thisContext, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

     Location getLastKnownLocation() {
        List<String> providers = handle.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
            }
            Location l = handle.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
