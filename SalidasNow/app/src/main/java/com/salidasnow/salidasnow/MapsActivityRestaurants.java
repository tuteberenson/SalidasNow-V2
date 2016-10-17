package com.salidasnow.salidasnow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.midi.MidiManager;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.salidasnow.salidasnow.ActividadPrincipal.usuarioActual;

public class MapsActivityRestaurants extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Restaurantes> listRestaurantesRecibidos;
    Location locationActual;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    boolean cargoImagen =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_restaurants);

        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView = (ImageView) mCustomMarkerView.findViewById(R.id.profile_image);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_general);
        mapFragment.getMapAsync(this);



        listRestaurantesRecibidos =(ArrayList<Restaurantes>) getIntent().getSerializableExtra("Restaurantes");

        locationActual = getIntent().getParcelableExtra("Location");
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
            for (Restaurantes unRestaurant:listRestaurantesRecibidos)
            {
                addMark(unRestaurant.get_Latitud(), unRestaurant.get_Longitud(), unRestaurant.get_Nombre(),1, unRestaurant.get_IdRestaurant());
            }

            addMark(locationActual.getLatitude(),locationActual.getLongitude(),"Aquí estoy",0,0);

            CameraUpdate posRestaurant= CameraUpdateFactory.newLatLngZoom(new LatLng(locationActual.getLatitude(),locationActual.getLongitude()),15);
            // CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

            mMap.moveCamera(posRestaurant);
            //mMap.animateCamera(zoom);
        }
    }


    public void addMark(final double lat, final double lng, final String titulo, int tipo, int IdRestaurant) //Tipo para ver si es restaurant o usuario, 1=resto,0 = usuario
    {

        if (tipo==1) {
            setCargoImagen(false);
            Glide.with(getApplicationContext())

                    .load("http://salidasnow.hol.es/images/"+IdRestaurant+".jpg")
                    .asBitmap()
                    .error(R.drawable.default_image)
                    .placeholder(R.drawable.default_image)
                    .override(48,48)
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>(){
                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }

                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                            setCargoImagen(true);
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .title(titulo)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap))));

                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                            {
                                @Override
                                public boolean onMarkerClick(Marker arg0) {

                                        Toast.makeText(MapsActivityRestaurants.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                                    return true;
                                }

                            });

                        }
                    });

            if (!cargoImagen) {

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(titulo)
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, R.drawable.default_image))));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {

                    @Override
                    public boolean onMarkerClick(Marker arg0)
                    {
                        if (!arg0.getTitle().equals("Aquí estoy"))
                        {
                            Restaurantes unResto=null;
                            int contador=0,position=0;

                            for (Restaurantes UnRestaurant:listRestaurantesRecibidos)
                            {
                                contador++;
                                if (arg0.getTitle().equals(UnRestaurant.get_Nombre()))
                                {
                                    position=contador;
                                    unResto=UnRestaurant;
                                }
                            }
                            Intent mapActivity = new Intent(MapsActivityRestaurants.this, MapsActivity.class);

                            mapActivity.putExtra("PosicionRestaurantLista",position-1);
                            mapActivity.putExtra("FragmentLlamador",FragmentCercaDeMi.class.getSimpleName());
                            mapActivity.putExtra("Restaurant", unResto);
                            startActivity(mapActivity);
                        }
                            Toast.makeText(MapsActivityRestaurants.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                        return true;
                    }

                });
            }
           /* MarkerOptions mo = new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(titulo)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(mo);*/
        }
        else
        {
            Glide.with(getApplicationContext())
                    .load("http://salidasnow.hol.es/images/"+usuarioActual.get_NombreIMG())
                    .asBitmap()
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .title(titulo)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap))));

                        }
                    });
          /*  MarkerOptions mo = new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(titulo)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.))
            mMap.addMarker(mo);*/
        }
    }



    private Bitmap getMarkerBitmapFromView(View view, @DrawableRes int resId) {

        mMarkerImageView.setImageResource(resId);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    public void setCargoImagen(boolean cargoImagen)
    {
        this.cargoImagen = cargoImagen;
    }

}
