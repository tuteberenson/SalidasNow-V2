package com.salidasnow.salidasnow;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView textNombreResto;
    Restaurantes restaurantRecibido;
    TextView txDireccion, txtestrellas;
    TextView prec1,prec2,prec3,prec4,prec5;
    ImageView imagenrestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_mapa);

        restaurantRecibido =(Restaurantes)getIntent().getSerializableExtra("Restaurant");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Log.d("ResturantTraido","Coordenadas - Lat: "+restaurantRecibido.get_Latitud()+" - Lng: "+restaurantRecibido.get_Longitud());


        textNombreResto =(TextView)findViewById(R.id.textNombreRestaurant);


        prec1 = (TextView)findViewById(R.id.Precio1);
        prec2 = (TextView)findViewById(R.id.Precio2);
        prec3 = (TextView)findViewById(R.id.Precio3);
        prec4 = (TextView)findViewById(R.id.Precio4);
        prec5 = (TextView)findViewById(R.id.Precio5);
        imagenrestaurant = (ImageView)findViewById(R.id.imgrestaurant);

        txDireccion = (TextView)findViewById(R.id.direc);

        txtestrellas = (TextView) findViewById(R.id.txtestrellas);


        textNombreResto.setText( restaurantRecibido.get_Nombre());


        if (restaurantRecibido!=null) {


            txDireccion.setText(restaurantRecibido.get_Direccion().trim());
            txtestrellas.setText("Estrellas: " + restaurantRecibido.get_Estrellas()+"");
            Picasso.with(getApplicationContext())
                    .load("http://salidasnow.hol.es/images/"+ restaurantRecibido.get_IdRestaurant()+".jpg")
                    .fit()
                    .into(imagenrestaurant);
            switch (restaurantRecibido.get_Precio())
            {
                case 1:
                    prec1.setTextColor(Color.rgb(0,0,0));
                    prec2.setTextColor(Color.rgb(124,124,124));
                    prec3.setTextColor(Color.rgb(124,124,124));
                    prec4.setTextColor(Color.rgb(124,124,124));
                    prec5.setTextColor(Color.rgb(124,124,124));
                    break;
                case 2:
                    prec1.setTextColor(Color.rgb(0,0,0));
                    prec2.setTextColor(Color.rgb(0,0,0));
                    prec3.setTextColor(Color.rgb(124,124,124));
                    prec4.setTextColor(Color.rgb(124,124,124));
                    prec5.setTextColor(Color.rgb(124,124,124));
                    break;
                case 3:
                    prec1.setTextColor(Color.rgb(0,0,0));
                    prec2.setTextColor(Color.rgb(0,0,0));
                    prec3.setTextColor(Color.rgb(0,0,0));
                    prec4.setTextColor(Color.rgb(124,124,124));
                    prec5.setTextColor(Color.rgb(124,124,124));
                    break;
                case 4:
                    prec1.setTextColor(Color.rgb(0,0,0));
                    prec2.setTextColor(Color.rgb(0,0,0));
                    prec3.setTextColor(Color.rgb(0,0,0));
                    prec4.setTextColor(Color.rgb(0,0,0));
                    prec5.setTextColor(Color.rgb(124,124,124));
                    break;
                case 5:
                    prec1.setTextColor(Color.rgb(0,0,0));
                    prec2.setTextColor(Color.rgb(0,0,0));
                    prec3.setTextColor(Color.rgb(0,0,0));
                    prec4.setTextColor(Color.rgb(0,0,0));
                    prec5.setTextColor(Color.rgb(0,0,0));
                    break;
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setZoomControlsEnabled(false); // Habilita +/- para hacer zoom
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);    // Selecciona tipo de mapa satelital

        if (mMap!=null)
        {
            addMark(restaurantRecibido.get_Latitud(), restaurantRecibido.get_Longitud(), restaurantRecibido.get_Nombre());
        }
    }


   public void addMark(double lat, double lng, String titulo)
    {
        CameraUpdate posRestaurant= CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15);
       // CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(posRestaurant);
        //mMap.animateCamera(zoom);


        MarkerOptions mo = new MarkerOptions()
                .position(new LatLng(lat,lng))
                .title(titulo);
        mMap.addMarker(mo);
    }
}
