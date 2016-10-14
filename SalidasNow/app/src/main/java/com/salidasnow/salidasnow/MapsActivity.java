package com.salidasnow.salidasnow;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.CacheStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView textNombreResto;
    Restaurantes restaurantRecibido;
    TextView txDireccion, txtestrellas;
    TextView prec1,prec2,prec3,prec4,prec5;
    ImageView imagenrestaurant;
    ImageButton btnVolver, btnLike;
    String FragmentQueLlama;
    int PosicionRestaurantEnLista;

    TextView Calidad1,Calidad2,Calidad3,Calidad4,Calidad5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_mapa);

        restaurantRecibido =(Restaurantes)getIntent().getSerializableExtra("Restaurant");

        FragmentQueLlama= getIntent().getStringExtra("FragmentLlamador");
        PosicionRestaurantEnLista = getIntent().getIntExtra("PosicionRestaurantLista",-1);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Log.d("ResturantTraido","Coordenadas - Lat: "+restaurantRecibido.get_Latitud()+" - Lng: "+restaurantRecibido.get_Longitud());


        textNombreResto =(TextView)findViewById(R.id.textNombreRestaurant);

        btnLike=(ImageButton)findViewById(R.id.btn_like_map_activity);
        btnVolver= (ImageButton)findViewById(R.id.btn_volver_map_activity);
        prec1 = (TextView)findViewById(R.id.Precio1);
        prec2 = (TextView)findViewById(R.id.Precio2);
        prec3 = (TextView)findViewById(R.id.Precio3);
        prec4 = (TextView)findViewById(R.id.Precio4);
        prec5 = (TextView)findViewById(R.id.Precio5);
        Calidad1=(TextView)findViewById(R.id.Calidad1);
        Calidad2=(TextView)findViewById(R.id.Calidad2);
        Calidad3=(TextView)findViewById(R.id.Calidad3);
        Calidad4=(TextView)findViewById(R.id.Calidad4);
        Calidad5=(TextView)findViewById(R.id.Calidad5);
        imagenrestaurant = (ImageView)findViewById(R.id.imgrestaurant);

        txDireccion = (TextView)findViewById(R.id.direcMapActivity);

        //txtestrellas = (TextView) findViewById(R.id.txtestrellas);


        textNombreResto.setText( restaurantRecibido.get_Nombre());


        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String FLikeados = FragmentRestaurantesLikeados.class.getSimpleName().toString();
                String FRecomendador = FragmentRecomendador.class.getSimpleName().toString();
                String FBuscarRestaurantes= FragmentBuscarRestaurantes.class.getSimpleName().toString();
                String FAlAzar= FragmentRestaurantesAzar.class.getSimpleName().toString();
                String FCercaDeMi = FragmentCercaDeMi.class.getSimpleName().toString();
                if (FLikeados.equals(FragmentQueLlama))
                {
                    if (PosicionRestaurantEnLista!=-1) {
                        FragmentRestaurantesLikeados.gListaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                        FragmentRestaurantesLikeados.adapter.notifyDataSetChanged();
                    }
                }
                else if (FRecomendador.equals(FragmentQueLlama))
                {
                    if (PosicionRestaurantEnLista!=-1) {
                        FragmentRecomendador.listaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                        FragmentRecomendador.adapter.notifyDataSetChanged();
                    }
                }
                else if(FBuscarRestaurantes.equals(FragmentQueLlama))
                {
                    if (PosicionRestaurantEnLista!=-1) {
                        FragmentBuscarRestaurantes.gListaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                        FragmentBuscarRestaurantes.adapter.notifyDataSetChanged();
                    }
                }
                else if (FAlAzar.equals(FragmentQueLlama))
                {
                    if (PosicionRestaurantEnLista!=-1) {
                        FragmentRestaurantesAzar.gListaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                        FragmentRestaurantesAzar.adapter.notifyDataSetChanged();
                    }
                }
                else if (FCercaDeMi.equals(FragmentQueLlama))
                {
                    if (PosicionRestaurantEnLista!=-1) {
                        FragmentCercaDeMi.gListaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                        FragmentCercaDeMi.adapter.notifyDataSetChanged();
                    }
                }

              finish();
            }
        });


        if (restaurantRecibido!=null) {

        if (restaurantRecibido.is_Likeado())
        {
            btnLike.setBackgroundColor(Color.rgb(164,0,0));
            btnLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
        }

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int idUsuarioActual=ActividadPrincipal.usuarioActual.get_idUsuario();
                    int idRestaurant=restaurantRecibido.get_IdRestaurant();
                    String url = "http://salidasnow.hol.es/UsuariosRestaurantes/setLike.php?idUsuario="+idUsuarioActual+"&idRestaurant="+idRestaurant+"&like=1";
                    new insertLikeAsync().execute(url);
                }
            });
            txDireccion.setText(restaurantRecibido.get_Direccion().trim());
            //txtestrellas.setText("Estrellas: " + restaurantRecibido.get_Estrellas()+"");

            switch (restaurantRecibido.get_Estrellas()) {
                case 1:
                     Calidad1.setText("★");
                     Calidad2.setText("☆");
                     Calidad3.setText("☆");
                     Calidad4.setText("☆");
                     Calidad5.setText("☆");
                    break;
                case 2:
                     Calidad1.setText("★");
                     Calidad2.setText("★");
                     Calidad3.setText("☆");
                     Calidad4.setText("☆");
                     Calidad5.setText("☆");
                    break;
                case 3:
                     Calidad1.setText("★");
                     Calidad2.setText("★");
                     Calidad3.setText("★");
                     Calidad4.setText("☆");
                     Calidad5.setText("☆");
                    break;
                case 4:
                     Calidad1.setText("★");
                     Calidad2.setText("★");
                     Calidad3.setText("★");
                     Calidad4.setText("★");
                     Calidad5.setText("☆");
                    break;
                case 5:
                     Calidad1.setText("★");
                     Calidad2.setText("★");
                     Calidad3.setText("★");
                     Calidad4.setText("★");
                     Calidad5.setText("★");
                    break;
            }

           Picasso.with(getApplicationContext())
                    .load("http://salidasnow.hol.es/images/"+ restaurantRecibido.get_IdRestaurant()+".jpg")
                    .fit()
                    .placeholder(getResources().getDrawable(R.drawable.default_image))
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

    @Override
    public void onBackPressed() {
        final String FLikeados = FragmentRestaurantesLikeados.class.getSimpleName().toString();
        String FRecomendador = FragmentRecomendador.class.getSimpleName().toString();
        String FBuscarRestaurantes= FragmentBuscarRestaurantes.class.getSimpleName().toString();
        String FAlAzar= FragmentRestaurantesAzar.class.getSimpleName().toString();
        String FCercaDeMi = FragmentCercaDeMi.class.getSimpleName().toString();
        if (FLikeados.equals(FragmentQueLlama))
        {
            if (PosicionRestaurantEnLista!=-1) {
                FragmentRestaurantesLikeados.gListaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                FragmentRestaurantesLikeados.adapter.notifyDataSetChanged();
            }
        }
        else if (FRecomendador.equals(FragmentQueLlama))
        {
            if (PosicionRestaurantEnLista!=-1) {
                FragmentRecomendador.listaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                FragmentRecomendador.adapter.notifyDataSetChanged();
            }
        }
        else if(FBuscarRestaurantes.equals(FragmentQueLlama))
        {
            if (PosicionRestaurantEnLista!=-1) {
                FragmentBuscarRestaurantes.gListaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                FragmentBuscarRestaurantes.adapter.notifyDataSetChanged();
            }
        }
        else if (FAlAzar.equals(FragmentQueLlama))
        {
            if (PosicionRestaurantEnLista!=-1) {
                FragmentRestaurantesAzar.gListaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                FragmentRestaurantesAzar.adapter.notifyDataSetChanged();
            }
        }
        else if (FCercaDeMi.equals(FragmentQueLlama))
        {
            if (PosicionRestaurantEnLista!=-1) {
                FragmentCercaDeMi.gListaRestaurantes.set(PosicionRestaurantEnLista,restaurantRecibido);
                FragmentCercaDeMi.adapter.notifyDataSetChanged();
            }
        }

        finish();
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
    private class insertLikeAsync extends AsyncTask<String, Void,String>
    {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String resultado;

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response=client.newCall(request).execute();

                return response.body().string();
            }
            catch (IOException e)
            {
                Log.d("Error",e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String JsonResponse) {

            int estado=0;
            try {
                estado = parsearResultado(JsonResponse);
                if (estado== 1 || estado==3)
                {
                    if (btnLike.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_star_border_black_24dp).getConstantState())
                    {
                        btnLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                        Toast.makeText(MapsActivity.this, "Me gusta!", Toast.LENGTH_SHORT).show();
                        restaurantRecibido.set_Likeado(true);
                        btnLike.setBackgroundColor(Color.rgb(164,0,0));
                    }
                    else
                    {
                        btnLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                        Toast.makeText(MapsActivity.this, "No me gusta :(", Toast.LENGTH_SHORT).show();
                        btnLike.setBackgroundColor(Color.rgb(196, 198, 197));
                        restaurantRecibido.set_Likeado(false);
                    }
                }
            }
            catch (JSONException e)
            {
                Log.d("error",e.getMessage());
            }
            switch (estado)
            {
                case 0:
                    Log.d("errorSwitch","Fue por el catch");
                    break;
                case 1:
                    Log.d("ResultadoLike","Ya existia el like, se actualizo sin problemas");
                    break;
                case 2:
                    Log.d("ResultadoLike","No se actualizo el registro");
                    break;
                case 3:
                    Log.d("ResultadoLike","No existia el like, se creo sin problemas");
                    break;
                case 4:
                    Log.d("ResultadoLike","No se creo el registro");
                    break;
                case 5:
                    Log.d("ResultadoLike","Excepcion sql: "+ JsonResponse);
                    break;
                default:
                    Log.d("errorSwitch","Ups");
                    break;
            }
        }

        private int parsearResultado(String string) throws JSONException
        {
            JSONObject json=new JSONObject(string);

            return json.getInt("estado");
        }

    }


}
