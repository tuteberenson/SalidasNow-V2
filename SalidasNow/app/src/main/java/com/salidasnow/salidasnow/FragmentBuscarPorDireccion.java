package com.salidasnow.salidasnow;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.gms.maps.model.LatLng;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBuscarPorDireccion extends Fragment {

    Button btn_buscar;
    ListView listView;
    EditText edtx_Direccion;
    public static AdaptadorListViewRestaurantes adapter;
    public static ArrayList<Restaurantes> gListaRestaurantes;
    ArrayList<Direcciones> direccionesObtenidas;
    String opcionSeleccionada;
    private TextView totalClassmates;
    private SwipeLayout swipeLayout;
    public static LatLng coordenadasDireccionObtenida;
    public static String direcBuscada;

    Context thisContext;

    private static String TAG = FragmentBuscarPorDireccion.class.getSimpleName();

    public FragmentBuscarPorDireccion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_buscar_por_direccion, container, false);

        coordenadasDireccionObtenida=null;

        btn_buscar = (Button) vista.findViewById(R.id.btn_buscar_por_direccion);
        listView = (ListView) vista.findViewById(R.id.Lv_buscar_por_direccion);
        edtx_Direccion = (EditText) vista.findViewById(R.id.edtx_buscar_por_direccion);

        gListaRestaurantes = new ArrayList<>();

        thisContext = container.getContext();

        direccionesObtenidas = new ArrayList<>();

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) thisContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtx_Direccion.getWindowToken(), 0);

                String url = "https://maps.googleapis.com/maps/api/geocode/json?address="; //url  de API direcciones

                if (!edtx_Direccion.getText().toString().isEmpty()) {

                    if (isNumeric(edtx_Direccion.getText().toString())) {
                        edtx_Direccion.setError("Dirección inválida");

                    } else if (!verSiHayNums(edtx_Direccion.getText().toString())) {
                        edtx_Direccion.setError("Ingrese números en la dirección");
                    } else {
                        url += edtx_Direccion.getText().toString();  // Copio la direccion ingresada al final de la URL
                        url += "&components=country:AR&key=AIzaSyA0T6Xd7zuyregCBfyon2axZWcgs1CUq-A";
                        new GeolocalizacionTask().execute(url);  // Llamo a clase async con url
                        edtx_Direccion.setEnabled(false);

                    }
                } else if (edtx_Direccion.getText().toString().isEmpty()) {

                    edtx_Direccion.setError("Complete el campo");
                }
            }
        });

        return vista;
    }

    private class GeolocalizacionTask extends AsyncTask<String, Void, ArrayList<Direcciones>> {
        private OkHttpClient client = new OkHttpClient();
        private ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Direcciones> resultado) {
            super.onPostExecute(resultado);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (Validacion(resultado.get(0).direccion)) {
                edtx_Direccion.setError("Dirección inválida");
            } else {

                if (resultado != null) {

                    direccionesObtenidas.clear();
                    direccionesObtenidas.addAll(resultado);

                    Dialog dialog = onCreateDialogSingleChoice(direccionesObtenidas);
                    dialog.show();
                    //arrayStrDirecciones.clear();
                    //arrayStrDirecciones.addAll(ArrarDirecAstrYSpn(resultado));
                    //Adaptador.notifyDataSetChanged();


                  /*  SPNListaDeDirecciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int posicion, long arg3) {

                            Log.d("parametrosOITS", "" + arg0 + "" + arg1 + "" + posicion + "" + arg3);
                            new PlacesTask().execute(direcciones.get(posicion).coordenadas);
                            Log.d("OITS", direcciones.get(posicion).coordenadas);
                           // btnMostrarEnMapa.setVisibility(View.VISIBLE);
                       /* String items = SPNListaDeDirecciones.getSelectedItem().toString();
                        Log.i("Selected item : ", items);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }

                    });*/

                }
            }

        }

        @Override
        protected ArrayList<Direcciones> doInBackground(String... params) {
            String url = params[0];

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {

                ArrayList<Direcciones> listaDirecciones;
                Response response = client.newCall(request).execute();  // Llamado al Google API
                listaDirecciones = parsearResultado(response.body().string());      // Convierto el resultado en ArrayList<Direcciones>


                return listaDirecciones;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return null;
            }
        }


        // Convierte un JSON en un ArrayList de Direccion
        ArrayList<Direcciones> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<Direcciones> direcciones = new ArrayList<>();
            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            JSONArray jsonDirecciones = json.getJSONArray("results");  // Array - una busqueda puede retornar varios resultados
            for (int i = 0; i < jsonDirecciones.length(); i++) {
                // Recorro los resultados recibidos
                JSONObject jsonResultado = jsonDirecciones.getJSONObject(i);
                String jsonAddress = jsonResultado.getString("formatted_address");  // Obtiene la direccion formateada

                JSONObject jsonGeometry = jsonResultado.getJSONObject("geometry");
                JSONObject jsonLocation = jsonGeometry.getJSONObject("location");
                double jsonLat = jsonLocation.getDouble("lat");                     // Obtiene latitud
                double jsonLng = jsonLocation.getDouble("lng");                     // Obtiene longitud
                String coord = jsonLat + "," + jsonLng;

                Direcciones d = new Direcciones(jsonAddress, jsonLat,jsonLng);                    // Creo nueva instancia de direccion
                direcciones.add(d);                                                 // Agrego objeto d al array list
                Log.d("Direccion:", d.direccion + " " + coord);
            }
            return direcciones;
        }
    }

    public Dialog onCreateDialogSingleChoice(ArrayList<Direcciones> listaDirecciones) {

//Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext,R.style.MyAlertDialogStyle);
//Source of the data in the DIalog

        String[] array = new String[listaDirecciones.size()];
        int index = 0;
        for (Direcciones value : listaDirecciones) {
            array[index] = value.direccion;
            index++;
        }

        final List<String> optionsList = Arrays.asList(array);
// Set the dialog title
        opcionSeleccionada = optionsList.get(0);
        builder.setTitle("Seleccione: ")
                .setCancelable(false)
// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                        opcionSeleccionada = optionsList.get(which);

                        Log.d("currentItem", opcionSeleccionada);
                        // Notify the current action
                        //Toast.makeText(thisContext,LimiteDeCuadrasOPC, Toast.LENGTH_SHORT).show();

                    }
                })

// Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String url="";
                        for (Direcciones unaDireccion:direccionesObtenidas)
                        {
                         if (unaDireccion.direccion.equals(opcionSeleccionada))
                         {
                             direcBuscada=unaDireccion.direccion;
                             coordenadasDireccionObtenida=new LatLng(unaDireccion.lat,unaDireccion.lng);
                             url = "http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byCercania.php?latitud="+unaDireccion.lat+"&longitud="+unaDireccion.lng+"&distancia="+1;
                             Log.d(TAG,url);
                         }
                        }

                        new TraerRestaurantes().execute(url);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        edtx_Direccion.setEnabled(true);
                    }
                });

        return builder.create();
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

  /*  public ArrayList<String> ArrarDirecAstrYSpn(ArrayList<Direcciones> adresses) {
        ArrayList<String> DireccionesStr = new ArrayList<>();
        for (Direcciones d : adresses) {
            DireccionesStr.add(d.direccion + "" + d.coordenadas);
        }
        return DireccionesStr;
    }
*/
    public boolean Validacion(String direccion) {
        int varSubstring = 0;
        String palabraNueva = "", caracter;
        boolean esNumero, resul = false;
        for (int i = 0; i < direccion.length(); i++) {

            if (varSubstring < direccion.length()) {
                varSubstring = i + 1;
            }
            caracter = direccion.substring(i, varSubstring);

            esNumero = isNumeric(caracter);

            if (caracter.compareTo(" ") != 0 && caracter.compareTo(".") != 0 && !esNumero && caracter.compareTo("-") != 0) {
                palabraNueva += caracter;
            }
            if (esNumero || caracter.compareTo("-") == 0) {
                i = direccion.length();
            }
        }
        if (palabraNueva.compareTo("Argentina") == 0) {
            resul = true;
        }
        return resul;
    }

    public boolean verSiHayNums(String ingresado) {
        int varSubstring = 0;
        String caracter;
        boolean esNumero, resul = false;
        for (int i = 0; i < ingresado.length(); i++) {

            if (varSubstring < ingresado.length()) {
                varSubstring = i + 1;
            }
            caracter = ingresado.substring(i, varSubstring);

            esNumero = isNumeric(caracter);

            if (esNumero) {
                resul = true;
                i = ingresado.length();

            }

        }
        return resul;
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
                url = "http://salidasnow.hol.es/UsuariosRestaurantes/obtener_IdRestaurant_byUsuario.php?idUsuario=" + ActividadPrincipal.usuarioActual.get_idUsuario();
                Log.d("urlLikeados", url);

                MyTaskParams params = new MyTaskParams(url, resultadoRestaurantes);
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
                double jsonDistance = (double) jsonResultado.getDouble("distance");


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
            this.listaRestaurantes = lista;

        }
    }

    private class RestaurantesLikeados extends AsyncTask<MyTaskParams, Void, ArrayList<Restaurantes>> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(final ArrayList<Restaurantes> listaRestos) {


            Log.d("sizeLista2", listaRestos.size() + "");

            gListaRestaurantes.clear();
            gListaRestaurantes.addAll(listaRestos);

            setListViewAdapter(gListaRestaurantes);

            edtx_Direccion.setEnabled(true);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Restaurantes unResto = listaRestos.get(position );
                        Log.d("Test", "00");
                        Log.d("Test", listaRestos.get(position).get_Nombre() + "");
                        Intent mapActivity = new Intent(thisContext, MapsActivity.class);

                        if (unResto.get_Distancia() < 0.3) {
                            Toast.makeText(thisContext, "El restaurant está a menos de tres cuadras de su ubicación", Toast.LENGTH_SHORT).show();

                        } else if (unResto.get_Distancia() > 0.3 && unResto.get_Distancia() < 0.5) {
                            Toast.makeText(thisContext, "El restaurant está entre 3 y 5 cuadras de su ubicación", Toast.LENGTH_SHORT).show();
                        } else if (unResto.get_Distancia() > 0.5 && unResto.get_Distancia() < 0.7) {
                            Toast.makeText(thisContext, "El restaurant está entre 5 y 7 cuadras de su ubicación", Toast.LENGTH_SHORT).show();
                        } else if (unResto.get_Distancia() > 0.7 && unResto.get_Distancia() < 1) {
                            Toast.makeText(thisContext, "El restaurant está entre 7 y 10 cuadras de su ubicación", Toast.LENGTH_SHORT).show();
                        }

                        mapActivity.putExtra("PosicionRestaurantLista", position);
                        mapActivity.putExtra("FragmentLlamador", TAG);
                        mapActivity.putExtra("Restaurant", unResto);
                        startActivity(mapActivity);

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
                arrayRestaurantes = parsearResultado(response.body().string(), params[0].listaRestaurantes);


                return arrayRestaurantes;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return arrayRestaurantes;
            }

        }

        ArrayList<Restaurantes> parsearResultado(String JSONstr, ArrayList<Restaurantes> listaRestos) throws JSONException {
            // ArrayList<Restaurantes> RestaurantArrayList = new ArrayList<>();
            boolean flag = false;

            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            //JSONObject jsonPrecio = new JSONObject("usuario");  // Array - una busqueda puede retornar varios resultados


            if (json.getInt("estado") == 1) {
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
                            PosicionRestaurant = listaRestos.indexOf(unResto);
                            flag = true;
                            listaRestos.get(PosicionRestaurant).set_Likeado(true);
                            listaRestos.set(PosicionRestaurant, listaRestos.get(PosicionRestaurant));
                        }
                    }
                }
            }
            Log.d("sizeLista", listaRestos.size() + "");
            return listaRestos;
        }
    }

    private void setListViewHeader() {
        LayoutInflater inflater = LayoutInflater.from(thisContext);
        View header = inflater.inflate(R.layout.header_listview, listView, false);
        totalClassmates = (TextView) header.findViewById(R.id.total);
        swipeLayout = (SwipeLayout) header.findViewById(R.id.swipe_layout);
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

    private void setListViewAdapter(ArrayList<Restaurantes> lista) {
        adapter = new AdaptadorListViewRestaurantes(thisContext, R.layout.list_item_restaurant, lista, ActividadPrincipal.usuarioActual, 5);
        listView.setAdapter(adapter);

        //totalClassmates.setText("Restaurantes");
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged(); //update adapter
    }

}

