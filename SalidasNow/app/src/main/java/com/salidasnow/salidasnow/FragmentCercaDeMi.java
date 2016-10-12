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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCercaDeMi extends Fragment implements LocationListener {

    TextView latitud;
    TextView longitud;
    Switch mySwitch;
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

        latitud = (TextView) vista.findViewById(R.id.latitud);
        longitud = (TextView) vista.findViewById(R.id.longitud);
        mySwitch = (Switch) vista.findViewById(R.id.switch_ubicacion);


        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEstadoSwitch(isChecked);
            }
        });

        return vista;
    }

    void  setEstadoSwitch(boolean x)
    {
        if (x)
        {
            IniciarServicio();
            muestraPosicionActual();
        }
        else
        {
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
        handle.requestLocationUpdates(provider, 10000, 1, this);
    }

    public void muestraPosicionActual() {
        if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = handle.getLastKnownLocation(provider);
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
}
