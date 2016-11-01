package com.salidasnow.salidasnow;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.squareup.okhttp.Address;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCercaDeMi extends Fragment implements LocationListener {

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    Button btn_Actualizar;
    TextView latitud;
    TextView longitud;
    TextView tv_Direccion;
    Context thisContext;
    public LocationManager handle;
    private String provider;
    String LimiteDeCuadrasOPC;
    double LimiteDeCuadras;
    private ListView listView;
    public static AdaptadorListViewRestaurantes adapter;
    private TextView totalClassmates;
    private SwipeLayout swipeLayout;
    private final static String TAG = FragmentCercaDeMi.class.getSimpleName();
    public static ArrayList<Restaurantes> gListaRestaurantes;
    boolean setearLVHeader;
    public static Location ubicacionActual;
    String direccionActual;

    public FragmentCercaDeMi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_cerca_de_mi, container, false);

        thisContext = container.getContext();

        listView=(ListView)vista.findViewById(R.id.list_view_cerca_de_mi);

        setearLVHeader=true;

        tv_Direccion = (TextView)vista.findViewById(R.id.tv_direccion_cerca_de_mi);

        gListaRestaurantes=new ArrayList<>();

        if (ContextCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        } else {
            IniciarServicio();

        }

        btn_Actualizar=(Button)vista.findViewById(R.id.btn_actualizar);
       // latitud = (TextView) vista.findViewById(R.id.latitud);
       // longitud = (TextView) vista.findViewById(R.id.longitud);


        btn_Actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = onCreateDialogSingleChoice();
                dialog.show();
            }
        });
        return vista;
    }

    void setEstadoServicio(boolean x) {
        if (x) {
            IniciarServicio();
            PosicionActual(false);
        } else {
            pararServicio();
        }
    }

    public void IniciarServicio() {
        //Toast.makeText(thisContext, "Busqueda de ubicación activada", Toast.LENGTH_SHORT).show();
        handle = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        provider = handle.getBestProvider(c, true);
       // Toast.makeText(thisContext, "Proveedor: " + provider, Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (provider.equals("passive"))
        {
            showGPSDisabledAlertToUser();        }
        else if (provider == null)
        {
            Toast.makeText(thisContext, "Permite el acceso a tu ubicación", Toast.LENGTH_SHORT).show();
        }
        else {
            handle.requestLocationUpdates(provider, 10000, 1, this);
            PosicionActual(false);
        }
    }
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisContext);
        alertDialogBuilder
                .setMessage(
                        "Active la ubicación")
                .setCancelable(false)
                .setPositiveButton("Ir a configuración",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });

        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }// ;

    public void PosicionActual(boolean mostrarRestaurantes) {
        if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = getLastKnownLocation();
        if (location == null) {
            //latitud.setText("Latitud: desconocida");
           // longitud.setText("Longitud: desconocida");
            Toast.makeText(thisContext, "No se pudo obtener la ubicacion", Toast.LENGTH_SHORT).show();
        } else {
            ubicacionActual = location;
            if (mostrarRestaurantes) {
                String url = "http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byCercania.php?latitud=" + location.getLatitude() + "&longitud=" + location.getLongitude() + "&distancia=" + LimiteDeCuadras;
                Log.d(TAG, url);
                new TraerRestaurantes().execute(url);
                setDireccion(location);
            }
            else
            {
                setDireccion(location);
            }
        }


    }

    private void setDireccion(Location loc)
    {
        if (loc!=null)
        {
            if (loc.getLongitude()!=0.0 && loc.getLatitude()!=0.0)
            {
                try {
                    Geocoder geocoder = new Geocoder(thisContext, Locale.getDefault());
                    List<android.location.Address> list=geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(),1);
                    if (!list.isEmpty())
                    {
                        android.location.Address direc=list.get(0);
                        tv_Direccion.setText("Dirección aproximada: "+direc.getAddressLine(0));
                        direccionActual = direc.getAddressLine(0);
                    }
                }
                catch (IOException e)
                {
                    tv_Direccion.setText(e+"");
                }
            }
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
            //latitud.setText("Latitud: desconocida");
            //longitud.setText("Longitud: desconocida");
            Toast.makeText(thisContext, "No se pudo obtener la ubicacion", Toast.LENGTH_SHORT).show();
        } else {
           // latitud.setText("Latitud: " + String.valueOf(location.getLatitude()));
           // longitud.setText("Longitud: " + String.valueOf(location.getLongitude()));
           // String url = "http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byCercania.php?latitud="+location.getLatitude()+"&longitud="+location.getLongitude()+"&distancia="+LimiteDeCuadras;
           // new TraerRestaurantes().execute(url);

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
                    Toast.makeText(thisContext, "Para utilizar esta función se necesita su ubicación!", Toast.LENGTH_SHORT).show();
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
    public Dialog onCreateDialogSingleChoice() {

//Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext,R.style.MyAlertDialogStyle);
//Source of the data in the DIalog
        String[] array = {"3", "5", "7","10"};

        LimiteDeCuadrasOPC="3";
        final List<String> optionsList = Arrays.asList(array);
// Set the dialog title
        builder.setTitle("Límite de cuadras: ")
// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                        LimiteDeCuadrasOPC = optionsList.get(which);

                        Log.d("currentItem", LimiteDeCuadrasOPC);
                        // Notify the current action
                      //Toast.makeText(thisContext,LimiteDeCuadrasOPC, Toast.LENGTH_SHORT).show();

                    }
                })

// Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog

                        switch (LimiteDeCuadrasOPC) {
                            case "3":
                                LimiteDeCuadras=0.3;
                                break;
                            case "5":
                                LimiteDeCuadras=0.5;
                                break;
                            case "7":
                                LimiteDeCuadras=0.7;
                                break;
                            case "10":
                                LimiteDeCuadras=1;
                                break;
                        }
                      //  if (provider.equals("gps"))
                      //  {



                           listView.setAdapter(null);
                            PosicionActual(true);

                       // }
                     //   else
                       // {
                            //Toast.makeText(thisContext, "Active el gps", Toast.LENGTH_SHORT).show();
                        //}

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        LimiteDeCuadras=0.0;
                    }
                });

        return builder.create();
    }

    private class TraerRestaurantes extends AsyncTask<String, Void, ArrayList<Restaurantes>> {
        private ProgressDialog dialog = new ProgressDialog(thisContext);
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Espere por favor");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(final ArrayList<Restaurantes> resultadoRestaurantes) {
            super.onPostExecute(resultadoRestaurantes);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (!resultadoRestaurantes.isEmpty()) {


                String url;
                url="http://salidasnow.hol.es/UsuariosRestaurantes/obtener_IdRestaurant_byUsuario.php?idUsuario="+ActividadPrincipal.usuarioActual.get_idUsuario();
                Log.d("urlLikeados",url);

                MyTaskParams params = new MyTaskParams(url,resultadoRestaurantes);
                new RestaurantesLikeados().execute(params);

            } else {
                Toast.makeText(thisContext, "No se encontraron restaurantes cerca", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ArrayList<Restaurantes> doInBackground(String... params) {
            String url = params[0];

            Log.d("url doInB Precio", url);
            ArrayList<Restaurantes> arrayRestaurantes = new ArrayList<>();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                arrayRestaurantes = parsearResultado(response.body().string());

                return arrayRestaurantes;

            } catch (IOException | JSONException e) {
                Log.d("Error1", e.getMessage());                          // Error de Network o al parsear JSON
                return arrayRestaurantes;
            }
        }


        ArrayList<Restaurantes> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<Restaurantes> RestaurantArrayList = new ArrayList<>();

            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            //JSONObject jsonPrecio = new JSONObject("usuario");  // Array - una busqueda puede retornar varios resultados

            JSONArray jsonRestaurantes = json.getJSONArray("restaurantes");

            int condicion;
            if (jsonRestaurantes.length() > 10) {
                condicion = 10;
            } else {
                condicion = jsonRestaurantes.length();
            }

            for (int i = 0; i < condicion; i++) {

                JSONObject jsonResultado = jsonRestaurantes.getJSONObject(i);

                int jsonId = jsonResultado.getInt("idRestaurant");
                String jsonNombre = jsonResultado.getString("Nombre");
                String jsonDireccion = jsonResultado.getString("Direccion");
                int jsonPrecio = jsonResultado.getInt("Precio");
                int jsonEstrellas = jsonResultado.getInt("Estrellas");
                int jsonNumeroTel = jsonResultado.getInt("NumeroTelefono");
                double jsonLatitud = (double) jsonResultado.getDouble("Latitud");
                double jsonLongitud = (double) jsonResultado.getDouble("Longitud");
                double jsonDistance= (double)jsonResultado.getDouble("distance");


                Log.d("parsearResulRes", "Nombre: " + jsonNombre + " Direccion: " + jsonDireccion);
                Log.d("latLng","Lat: "+ jsonLatitud+ " Lng: "+jsonLongitud);

                Restaurantes re = new Restaurantes();
                re.set_Precio(jsonPrecio);
                re.set_Nombre(jsonNombre);
                re.set_NumTelefono(jsonNumeroTel);
                re.set_Latitud(jsonLatitud);
                re.set_Longitud(jsonLongitud);
                re.set_Estrellas(jsonEstrellas);
                re.set_Direccion(jsonDireccion);
                re.set_IdRestaurant(jsonId);
                re.set_Distancia(jsonDistance);

                RestaurantArrayList.add(re);                                                 // Agrego objeto d al array list
            }
            return RestaurantArrayList;
        }
    }
    private static class MyTaskParams {
        String url;
        ArrayList<Restaurantes> listaRestaurantes;

        MyTaskParams(String url, ArrayList<Restaurantes> lista) {
            this.url = url;
            this.listaRestaurantes =lista;

        }
    }
    private class RestaurantesLikeados extends AsyncTask<MyTaskParams, Void, ArrayList<Restaurantes>>
    {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(final ArrayList<Restaurantes> listaRestos) {


            if (setearLVHeader) {
                setearLVHeader=false;
                setListViewHeader();
            }

            Log.d("sizeLista2",listaRestos.size()+"");

            gListaRestaurantes.clear();
                gListaRestaurantes.addAll(listaRestos);

            setListViewAdapter(gListaRestaurantes);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        Restaurantes unResto = listaRestos.get(position - 1);
                        Log.d("Test", "00");
                        Log.d("Test", listaRestos.get(position - 1).get_Nombre() + "");
                        Intent mapActivity = new Intent(thisContext, MapsActivity.class);

                    if (unResto.get_Distancia()<0.3)
                    {
                        Toast.makeText(thisContext, "El restaurant está a menos de tres cuadras de su ubicación", Toast.LENGTH_SHORT).show();

                    }else if (unResto.get_Distancia()>0.3 && unResto.get_Distancia()<0.5 )
                    {
                        Toast.makeText(thisContext, "El restaurant está entre 3 y 5 cuadras de su ubicación", Toast.LENGTH_SHORT).show();
                    }
                    else if (unResto.get_Distancia()>0.5 && unResto.get_Distancia()<0.7 )
                    {
                        Toast.makeText(thisContext, "El restaurant está entre 5 y 7 cuadras de su ubicación", Toast.LENGTH_SHORT).show();
                    }
                    else if (unResto.get_Distancia()>0.7 && unResto.get_Distancia()<1 )
                    {
                        Toast.makeText(thisContext, "El restaurant está entre 7 y 10 cuadras de su ubicación", Toast.LENGTH_SHORT).show();
                    }

                        mapActivity.putExtra("direcActual",direccionActual);
                        mapActivity.putExtra("Location",ubicacionActual);
                        mapActivity.putExtra("PosicionRestaurantLista",position-1);
                        mapActivity.putExtra("FragmentLlamador",TAG);
                        mapActivity.putExtra("Restaurant", unResto);
                        startActivity(mapActivity);
                    }
                }
            });

            super.onPostExecute(listaRestos);

        }

        @Override
        protected ArrayList<Restaurantes> doInBackground(MyTaskParams... params) {
            String url = params[0].url;
            ArrayList<Restaurantes> arrayRestaurantes = new ArrayList<>();

            Log.d("url doInB Precio", url);

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                arrayRestaurantes = parsearResultado(response.body().string(),params[0].listaRestaurantes);


                return arrayRestaurantes;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return arrayRestaurantes;
            }

        }
        ArrayList<Restaurantes> parsearResultado(String JSONstr, ArrayList<Restaurantes> listaRestos) throws JSONException {
            // ArrayList<Restaurantes> RestaurantArrayList = new ArrayList<>();
            boolean flag=false;

            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            //JSONObject jsonPrecio = new JSONObject("usuario");  // Array - una busqueda puede retornar varios resultados


            if (json.getInt("estado")==1) {
                JSONArray jsonRestaurantes = json.getJSONArray("restaurantes");

                int condicion;
                if (jsonRestaurantes.length() > 100) {
                    condicion = 100;
                } else {
                    condicion = jsonRestaurantes.length();
                }

                for (Restaurantes unResta : listaRestos) {

                    unResta.set_Likeado(false);
                }

                for (int i = 0; i < condicion; i++) {

                    flag = false;
                    JSONObject jsonResultado = jsonRestaurantes.getJSONObject(i);

                    int jsonIdRestaurant = jsonResultado.getInt("idRestaurant");
                    int PosicionRestaurant = -1;

                    for (Restaurantes unResto : listaRestos) {
                        if (unResto.get_IdRestaurant() == jsonIdRestaurant) {
                            PosicionRestaurant=listaRestos.indexOf(unResto);
                            flag = true;
                            listaRestos.get(PosicionRestaurant).set_Likeado(true);
                            listaRestos.set(PosicionRestaurant, listaRestos.get(PosicionRestaurant));
                        }
                    }
                }
            }
            Log.d("sizeLista",listaRestos.size()+"");
            return listaRestos;
        }
    }
    private void setListViewHeader() {
        LayoutInflater inflater =LayoutInflater.from(thisContext);
        View header = inflater.inflate(R.layout.header_listview, listView, false);
        totalClassmates = (TextView) header.findViewById(R.id.total);
        swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout);
        setSwipeViewFeatures(header);
        totalClassmates.setText("Restaurantes");
        listView.addHeaderView(header);
    }

    private void setSwipeViewFeatures(View header) {
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, header.findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                Log.i(TAG, "onClose");
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                Log.i(TAG, "on swiping");
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.i(TAG, "on start open");
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally show");
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally close");
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }

    private void setListViewAdapter(ArrayList<Restaurantes>lista) {
        adapter = new AdaptadorListViewRestaurantes(thisContext, R.layout.list_item_restaurant, lista, ActividadPrincipal.usuarioActual,5);
        listView.setAdapter(adapter);

        //totalClassmates.setText("Restaurantes");
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged(); //update adapter
    }

}
