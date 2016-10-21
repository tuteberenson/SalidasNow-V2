package com.salidasnow.salidasnow;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBuscarRestaurantes extends Fragment {


     public static  MaterialSpinner spncalidad, spnprecio;
             Spinner spneleccion;
 public static    View textInput;
    public static EditText txtnombre;
    String nombrebuscado;
    Context thisContext;
    ArrayList<String> arrayspnprecio;
    ArrayList<String> arrayspncalidad;
    ArrayList<String> arrayspneleccion;
    ArrayAdapter<String> adapterprecio;
    ArrayAdapter<String> adaptercalidad;
    ArrayAdapter<String> adaptereleccion;
    Button btnBuscar;
    ListView listView;
    private TextView totalClassmates;
    private SwipeLayout swipeLayout;
     public static    AdaptadorListViewRestaurantes adapter;
    boolean setearLVHeader;
    public static ArrayList<Restaurantes> gListaRestaurantes;

    private final static String TAG = FragmentBuscarRestaurantes.class.getSimpleName();

    public FragmentBuscarRestaurantes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista= inflater.inflate(R.layout.fragment_buscar_restaurantes, container, false);

        gListaRestaurantes=new ArrayList<>();

        thisContext=container.getContext();

        setearLVHeader=true;

        btnBuscar = (Button)vista.findViewById(R.id.btnBuscarRestaurantes);

        listView=(ListView)vista.findViewById(R.id.listVwBuscoRestaurantes);

        textInput=vista.findViewById(R.id.textInput);

        spncalidad = (MaterialSpinner)vista.findViewById(R.id.SpnCalidad);
        spnprecio = (MaterialSpinner)vista.findViewById(R.id.SpnPrecio);
        //spneleccion = (Spinner)vista.findViewById(R.id.Spneleccion);
        txtnombre = (EditText)vista.findViewById(R.id.nombbuscar);
        //  nombrebuscado = txtnombre.getText().toString().trim();
        arrayspncalidad = new ArrayList<String>();
        arrayspnprecio = new ArrayList<String>();
        arrayspneleccion = new ArrayList<String>();

        arrayspnprecio.add("Muy barato");
        arrayspnprecio.add("Barato");
        arrayspnprecio.add("Medio");
        arrayspnprecio.add("Caro");
        arrayspnprecio.add("Muy caro");

        arrayspncalidad.add("1 Estrella");
        arrayspncalidad.add("2 Estrellas");
        arrayspncalidad.add("3 Estrellas");
        arrayspncalidad.add("4 Estrellas");
        arrayspncalidad.add("5 Estrellas");

        /*arrayspneleccion.add("Buscar por nombre");
        arrayspneleccion.add("Buscar por calidad");
        arrayspneleccion.add("Buscar por precio");*/


        String[] array = new String[arrayspncalidad.size()];
        int index = 0;
        for (String value : arrayspncalidad) {
            array[index] = value;
            index++;
        }

        //final List<String> optionsListCalidad = Arrays.asList(array);

        String[] array2 = new String[arrayspnprecio.size()];
        int index2 = 0;
        for (String value : arrayspnprecio) {
            array2[index2] = value;
            index2++;
        }

        //final List<String> optionsListPrecio = Arrays.asList(array2);

        adaptercalidad = new ArrayAdapter<String>(thisContext, R.layout.support_simple_spinner_dropdown_item, array);
        adapterprecio = new ArrayAdapter<String>(thisContext,  R.layout.support_simple_spinner_dropdown_item, array2);
      //  adaptereleccion = new ArrayAdapter<String>(thisContext,  R.layout.spinner_item, arrayspneleccion);


        adaptercalidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterprecio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnprecio.setAdapter(adapterprecio);
        spncalidad.setAdapter(adaptercalidad);


        //spneleccion.setAdapter(adaptereleccion);
        spnprecio.setVisibility(View.INVISIBLE);
        spncalidad.setVisibility(View.INVISIBLE);
        txtnombre.setVisibility(View.VISIBLE);
        textInput.setVisibility(View.VISIBLE);




        /*spneleccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                // int posicion = spneleccion.getSelectedItemPosition();
                switch (pos) {
                    case (0):
                        txtnombre.setVisibility(View.VISIBLE);
                        spnprecio.setVisibility(View.INVISIBLE);
                        spncalidad.setVisibility(View.INVISIBLE);

                        break;
                    case (1):
                        spncalidad.setVisibility(View.VISIBLE);
                        spnprecio.setVisibility(View.INVISIBLE);
                        txtnombre.setVisibility(View.INVISIBLE);
                        break;
                    case (2):
                        spnprecio.setVisibility(View.VISIBLE);
                        spncalidad.setVisibility(View.INVISIBLE);
                        txtnombre.setVisibility(View.INVISIBLE);
                        break;
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        }); // (optional)
        */

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Estrellas, Precio, NombreRestaurant;

                Estrellas= spncalidad.getSelectedItem().toString();
                Precio = spnprecio.getSelectedItem().toString();
                NombreRestaurant= txtnombre.getText().toString().trim();


                String textoABuscar="";
                for (int i=0; i < NombreRestaurant.length(); i++)
                {
                    if (i==0) {
                        String primeraLetra = NombreRestaurant.substring(0, 1);
                        textoABuscar+=primeraLetra.toUpperCase();
                    }
                    else
                    {
                        textoABuscar+= NombreRestaurant.substring(i, i+1);
                    }

                }

                listView.setAdapter(null);

/*                if (nombrebuscado.compareTo("")==0 && spneleccion.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(thisContext, "Ingrese un nombre de restaurant", Toast.LENGTH_SHORT).show();
                }
                else{*/

                if (spncalidad.getVisibility()== View.VISIBLE)
                {

                    int cantEstrellas;
                    switch (Estrellas)
                    {
                        case "1 Estrella":
                            cantEstrellas=1;
                            break;
                        case "2 Estrellas":
                            cantEstrellas=2;
                            break;
                        case "3 Estrellas":
                            cantEstrellas=3;
                            break;
                        case "4 Estrellas":
                            cantEstrellas=4;
                            break;
                        case "5 Estrellas":
                            cantEstrellas=5;
                            break;
                        default:
                            cantEstrellas=0;
                            break;

                    }
                    String url = "http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byEstrellas.php?estrellas="+ cantEstrellas;
                    Log.d("url byEstrellas", url);
                    MyTaskParams parametros=new MyTaskParams(url,"Estrellas");
                    new TraerRestaurantes().execute(parametros);
                }
                else if (spnprecio.getVisibility()== View.VISIBLE)
                {
                    int cantPrecio;
                    switch (Precio)
                    {
                        case "Muy barato":
                            cantPrecio = 1;
                            break;
                        case "Barato":
                            cantPrecio = 2;
                            break;
                        case "Medio":
                            cantPrecio = 3;
                            break;
                        case "Caro":
                            cantPrecio = 4;
                            break;
                        case "Muy caro":
                            cantPrecio = 5;
                            break;
                        default:
                            cantPrecio = 0;
                            break;
                    }
                    String url1 = "http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byPrecio.php?precio="+ cantPrecio;
                    Log.d("url byPrecio", url1);
                    MyTaskParams parametros=new MyTaskParams(url1,"Precio");
                    new TraerRestaurantes().execute(parametros);
                }
                else if (txtnombre.getVisibility()== View.VISIBLE)
                {
                    if(textoABuscar.compareTo("")==0) {
                        txtnombre.setError("Complete el campo");
                    }else
                    {
                        String url2 = "http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byNombre.php?nombre=" + textoABuscar;
                        Log.d("url byNombre", url2);
                        MyTaskParams parametros = new MyTaskParams(url2, "Nombre");
                        new TraerRestaurantes().execute(parametros);

                    }
                }
            }


            //}
        });

        return vista;
    }

    private static class MyTaskParams {
        String url, tipoDeBusqueda;

        ArrayList<Restaurantes> listaRestaurantes;

        MyTaskParams(String url, ArrayList<Restaurantes> lista) {
            this.url = url;
            this.listaRestaurantes =lista;

        }

        MyTaskParams(String url, String tipoDeBusqueda) {
            this.url = url;
            this.tipoDeBusqueda =tipoDeBusqueda;
        }
    }
    private class TraerRestaurantes extends AsyncTask<MyTaskParams, Void, ArrayList<Restaurantes>> {
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
                Log.d(TAG,url);

                MyTaskParams params = new MyTaskParams(url,resultadoRestaurantes);
                new RestaurantesLikeados().execute(params);

            } else {
                Toast.makeText(thisContext, "No hay restaurantes con ese criterio", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ArrayList<Restaurantes> doInBackground(MyTaskParams... params) {
            String url = params[0].url;

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

            JSONArray jsonRestaurantes = json.getJSONArray("restaurantes");

            int condicion;
            if (jsonRestaurantes.length() > 15) {
                condicion = 15;
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


    private class RestaurantesLikeados extends AsyncTask<MyTaskParams, Void, ArrayList<Restaurantes>>
    {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(final ArrayList<Restaurantes> listaRestos) {

            gListaRestaurantes.clear();
            gListaRestaurantes.addAll(listaRestos);
            setListViewAdapter(gListaRestaurantes);


            //setListViewAdapter(gListaRestaurantes);
            if (setearLVHeader) {
             setearLVHeader=false;
                //setListViewHeader();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                      Restaurantes unResto = gListaRestaurantes.get(position);
                        Log.d("Test", "00");
                        Log.d("Test", gListaRestaurantes.get(position) + "");
                        Intent mapActivity = new Intent(thisContext, MapsActivity.class);

                    mapActivity.putExtra("PosicionRestaurantLista",position);
                    mapActivity.putExtra("FragmentLlamador",TAG);
                        mapActivity.putExtra("Restaurant", unResto);
                        startActivity(mapActivity);

                }
            });



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
        totalClassmates.setText("Restaurantes encontrados");
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
        adapter = new AdaptadorListViewRestaurantes(thisContext, R.layout.list_item_restaurant, lista, ActividadPrincipal.usuarioActual,4);
        listView.setAdapter(adapter);

        //totalClassmates.setText("Restaurantes");
    }


      /*  public void updateAdapter() {
            adapter.notifyDataSetChanged(); //update adapter
            totalClassmates.setText("(" + friendsList.size() + ")"); //update total friends in list
        }*/






}
