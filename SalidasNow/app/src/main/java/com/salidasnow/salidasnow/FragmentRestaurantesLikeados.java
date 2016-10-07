package com.salidasnow.salidasnow;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRestaurantesLikeados extends Fragment {

    Context thisContext;
    ListView listView;
    private AdaptadorListViewRestaurantes adapter;
    private TextView totalClassmates;
    private SwipeLayout swipeLayout;
    public static ArrayList<Restaurantes> gListaRestaurantes;


    private final static String TAG = FragmentRestaurantesLikeados.class.getSimpleName();


    public FragmentRestaurantesLikeados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_restaurantes_likeados, container, false);

        thisContext = container.getContext();
        listView = (ListView) vista.findViewById(R.id.listVW_Likeados);

        gListaRestaurantes = new ArrayList<>();

        String url = "http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byLikes.php?idUsuario="+ActividadPrincipal.usuarioActual.get_idUsuario();
        new TraerRestaurantes().execute(url);

        return vista;

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

                gListaRestaurantes.addAll(resultadoRestaurantes);
                String url;
                url="http://salidasnow.hol.es/UsuariosRestaurantes/obtener_IdRestaurant_byUsuario.php?idUsuario="+ActividadPrincipal.usuarioActual.get_idUsuario();
                Log.d(TAG,url);

                MyTaskParams params = new MyTaskParams(url,resultadoRestaurantes);
                new RestaurantesLikeados().execute(params);

            } else {
                Toast.makeText(thisContext, "No le gusta ningún restaurant", Toast.LENGTH_SHORT).show();
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
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return arrayRestaurantes;
            }
        }


        ArrayList<Restaurantes> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<Restaurantes> RestaurantArrayList = new ArrayList<>();

            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            //JSONObject jsonPrecio = new JSONObject("usuario");  // Array - una busqueda puede retornar varios resultados

            JSONArray jsonRestaurantes = json.getJSONArray("restaurants");

            int condicion;
            if (jsonRestaurantes.length() > 30) {
                condicion = 30;
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

                Log.d("parsearResulRes", "Nombre: " + jsonNombre + " Direccion: " + jsonDireccion);
                Log.d("latLng", "Lat: " + jsonLatitud + " Lng: " + jsonLongitud);

                Restaurantes re = new Restaurantes();
                re.set_Precio(jsonPrecio);
                re.set_Nombre(jsonNombre);
                re.set_NumTelefono(jsonNumeroTel);
                re.set_Latitud(jsonLatitud);
                re.set_Longitud(jsonLongitud);
                re.set_Estrellas(jsonEstrellas);
                re.set_Direccion(jsonDireccion);
                re.set_IdRestaurant(jsonId);


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

            setListViewAdapter(listaRestos);
            setListViewHeader();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position!=0)
                    {
                        Restaurantes unResto = listaRestos.get(position-1);
                        Log.d("Test", "00");
                        Log.d("Test", listaRestos.get(position-1) + "");
                        Intent mapActivity = new Intent(thisContext, MapsActivity.class);

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
            return listaRestos;
        }
    }

    private void setListViewHeader() {
        LayoutInflater inflater = LayoutInflater.from(thisContext);
        View header = inflater.inflate(R.layout.header_listview, listView, false);
        totalClassmates = (TextView) header.findViewById(R.id.total);
        swipeLayout = (SwipeLayout) header.findViewById(R.id.swipe_layout);
        setSwipeViewFeatures(header);
        totalClassmates.setText("Restaurantes likeados");
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

    private void setListViewAdapter(ArrayList<Restaurantes> lista) {

        Log.d("UsuarioEnLikeados", ActividadPrincipal.usuarioActual.get_Nombre());
        adapter = new AdaptadorListViewRestaurantes(thisContext, R.layout.list_item_restaurant, lista, ActividadPrincipal.usuarioActual,1);
        listView.setAdapter(adapter);

        //totalClassmates.setText("Restaurantes");
    }


      /*  public void updateAdapter() {
            adapter.notifyDataSetChanged(); //update adapter
            totalClassmates.setText("(" + friendsList.size() + ")"); //update total friends in list
        }*/






}
