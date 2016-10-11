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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecomendador extends Fragment {

    private TextView totalClassmates;
    private SwipeLayout swipeLayout;
    Context thisContext;
    CheckBox checkBox1,checkBox2,checkBox3;
    Button btnMasOpciones, btnRecomendame;
    ArrayList<Indicadores> gArrayIndicadores,indicadoresAlAzar;
    ListView listView;
    public static AdaptadorListViewRestaurantes adapter;
    boolean setearLVHeader;
    String seleccionCalidad,seleccionPrecio,seleccionAmbientacion;
   public static ArrayList<Restaurantes> listaRestaurantes;

    private final static String TAG = FragmentRecomendador.class.getSimpleName();

    public FragmentRecomendador() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_recomendador, container, false);

        listaRestaurantes=new ArrayList<>();
        setearLVHeader=true;

        thisContext=container.getContext();

        checkBox1 = (CheckBox) vista.findViewById(R.id.chbox1);
        checkBox2 = (CheckBox) vista.findViewById(R.id.chbox2);
        checkBox3 = (CheckBox) vista.findViewById(R.id.chbox3);

        btnMasOpciones = (Button) vista.findViewById(R.id.btnMasOpc);
        btnRecomendame = (Button) vista.findViewById(R.id.btnRecomendame);

        listView = (ListView) vista.findViewById(R.id.listVwRecomendador);

        gArrayIndicadores = new ArrayList<>();

        String url = "http://salidasnow.hol.es/Indicadores/obtener_indicadores.php";

        Log.d("url solicitada", url);



        new AsyncTaskIndicadores().execute(url);


      btnMasOpciones.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                OrdenarRdm();

                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
            }
        });

        btnRecomendame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked())
                {
                    Toast.makeText(thisContext, "Seleccione al menos una opción", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ArrayList<String> listaCatsSeleccionadas=new ArrayList<>();
                    if (checkBox1.isChecked())
                    {
                        listaCatsSeleccionadas.add(indicadoresAlAzar.get(0).getCategoria());
                    }
                    else if (checkBox2.isChecked())
                    {
                        listaCatsSeleccionadas.add(indicadoresAlAzar.get(1).getCategoria());
                    }
                    else if (checkBox3.isChecked())
                    {
                        listaCatsSeleccionadas.add(indicadoresAlAzar.get(2).getCategoria());
                    }

                    for (String unaCat:listaCatsSeleccionadas)
                    {
                        String urlAEjecutar1;

                        switch (unaCat)
                        {
                            case "Precio":
                                if (indicadoresAlAzar.get(0).getValor()==2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=igual&valor=3&parametroW=Precio";
                                }
                                else if (indicadoresAlAzar.get(0).getValor()>2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=mayor&valor=3&parametroW=Precio";
                                }
                                else
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=menor&valor=3&parametroW=Precio";
                                }
                                break;
                            case "Ambientacion":
                                if (indicadoresAlAzar.get(0).getValor()==2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=igual&valor=3&parametroW=Precio";
                                }
                                else if (indicadoresAlAzar.get(0).getValor()>2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=mayor&valor=3&parametroW=Estrellas";
                                }
                                else
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=menor&valor=3&parametroW=Precio";
                                }
                                break;
                            case "Calidad":
                                if (indicadoresAlAzar.get(0).getValor()==2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=igual&valor=3&parametroW=Estrellas";
                                }
                                else if (indicadoresAlAzar.get(0).getValor()>2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=mayor&valor=3&parametroW=Estrellas";
                                }
                                else
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=menor&valor=3&parametroW=Estrellas";
                                }
                                break;
                            default:
                                Toast.makeText(thisContext, "Hubo un error", Toast.LENGTH_SHORT).show();
                                urlAEjecutar1="";
                                break;
                        }
                        Log.d("btnRecomendame","url a ejecutar: "+ urlAEjecutar1);
                        new TraerRestaurantsPorParametro().execute(urlAEjecutar1);
                    }
                }

            }
        });




        return vista;
    }

    private class AsyncTaskIndicadores extends AsyncTask<String, Void,ArrayList<Indicadores>>
    {
        private ProgressDialog dialog = new ProgressDialog(thisContext);
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected ArrayList<Indicadores> doInBackground(String... params) {
            String url = params[0];

            ArrayList<Indicadores> arrayIndicadores=new ArrayList<>();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                String strResponseBody = response.body().string();

                Log.d("response body",strResponseBody);


                arrayIndicadores = parsearResultadoIndicadores(strResponseBody);


                return arrayIndicadores;
            }catch (IOException | JSONException e)
            {
                Log.d("Error", e.getMessage());
                return arrayIndicadores;
            }
        }

        private ArrayList<Indicadores> parsearResultadoIndicadores(String JSONstr) throws JSONException {

            ArrayList<Indicadores> listaIndicadores=new ArrayList<>();

            JSONObject json = new JSONObject(JSONstr);

            JSONArray jsonIndicadores= json.getJSONArray("indicadores");

            if (jsonIndicadores.length()==0)
            {
                Toast.makeText(thisContext, "aaa", Toast.LENGTH_LONG).show();
            }

            for (int i=0; i<jsonIndicadores.length();i++) {

                JSONObject jsonResultado= jsonIndicadores.getJSONObject(i);

                String jsonTextoIndicador =jsonResultado.getString("Texto");
                String jsonCategoriaIndicador= jsonResultado.getString("Categoria");
                int jsonValorIndicador= jsonResultado.getInt("Valor");

                Indicadores unIndicador= new Indicadores();

                unIndicador.setTexto(jsonTextoIndicador);
                unIndicador.setCategoria(jsonCategoriaIndicador);
                unIndicador.setValor(jsonValorIndicador);

                listaIndicadores.add(unIndicador);

            }



            return listaIndicadores;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Espere por favor");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Indicadores> resultadoIndicadores) {
            super.onPostExecute(resultadoIndicadores);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (!resultadoIndicadores.isEmpty())
            {
                gArrayIndicadores.addAll(resultadoIndicadores);
                Log.d("IndicadorArray",gArrayIndicadores.get(0).getTexto());
                OrdenarRdm();
            }
            else
            {
                Toast.makeText(thisContext, "No se pudieron obtener los indicadores", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void OrdenarRdm()
    {


        Random generadorAzar = new Random();

        String texto1="", texto2="", texto3="", cat1,cat2,cat3;
        int comparadoresT12,comparadoresT13,comparadoresT23, comparadoresC12,comparadoresC13, comparadoresC23;
        indicadoresAlAzar = new ArrayList<>();

        int numeroElegido;

        ArrayList<Integer> listaNumerosAzar=new ArrayList<>();

        boolean volverAGenerar= true;

        while (volverAGenerar) {

            for (int i = 0; i <= 3; i++) {

                // if (i == 0) {
                numeroElegido = generadorAzar.nextInt(gArrayIndicadores.size() - 1);
                listaNumerosAzar.add(numeroElegido);
                indicadoresAlAzar.add(gArrayIndicadores.get(numeroElegido));
                //Categorias.add(indicadoresAlAzar.get(i).getCategoria());
               /*} else {
                   numeroElegido = generadorAzar.nextInt(gArrayIndicadores.size() - 1);
                   for (Integer num : listaNumerosAzar) {
                       if (num == numeroElegido) {
                           numeroElegido = generadorAzar.nextInt(gArrayIndicadores.size() - 1);
                       }
                   }
                   listaNumerosAzar.add(numeroElegido);
               }*/
            }



            texto1= indicadoresAlAzar.get(0).getTexto();
            texto2= indicadoresAlAzar.get(1).getTexto();
            texto3= indicadoresAlAzar.get(2).getTexto();

            cat1= indicadoresAlAzar.get(0).getCategoria();
            cat2= indicadoresAlAzar.get(1).getCategoria();
            cat3= indicadoresAlAzar.get(2).getCategoria();

            comparadoresT12=texto1.compareTo(texto2);
            comparadoresT13=texto1.compareTo(texto3);
            comparadoresT23=texto2.compareTo(texto3);

            comparadoresC12=cat1.compareTo(cat2);
            comparadoresC13=cat1.compareTo(cat3);
            comparadoresC23=cat2.compareTo(cat3);

            if (comparadoresC12 != 0 && comparadoresC13 !=0 && comparadoresC23!= 0 &&
                    comparadoresT12!=0 && comparadoresT13!=0 && comparadoresT23!=0)
            {
                volverAGenerar=false;
            }
            else
            {
                listaNumerosAzar.clear();
                indicadoresAlAzar.clear();
            }


        }
        checkBox1.setText(texto1);
        checkBox2.setText(texto2);
        checkBox3.setText(texto3);
    }

    private class TraerRestaurantsPorParametro extends AsyncTask<String, Void, ArrayList<Restaurantes>> {
        private ProgressDialog dialog = new ProgressDialog(thisContext);
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(final ArrayList<Restaurantes> resultadoRestaurants) {
            super.onPostExecute(resultadoRestaurants);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (!resultadoRestaurants.isEmpty()) {

                if (!resultadoRestaurants.isEmpty()) {


                    String url;
                    url = "http://salidasnow.hol.es/UsuariosRestaurantes/obtener_IdRestaurant_byUsuario.php?idUsuario=" + ActividadPrincipal.usuarioActual.get_idUsuario();
                    Log.d(TAG, url);

                    MyTaskParams params = new MyTaskParams(url, resultadoRestaurants);
                    new RestaurantesLikeados().execute(params);

                } else {
                    Toast.makeText(thisContext, "No hay restaurantes con ese criterio", Toast.LENGTH_SHORT).show();
                }
            }
        }
            @Override
            protected ArrayList<Restaurantes> doInBackground (String...params){
                String url = params[0];

                Log.d("url doInB Precio", url);
                ArrayList<Restaurantes> arrayRestaurantes = new ArrayList<>();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    arrayRestaurantes = parsearResultadoRestosPrecio(response.body().string());

                    return arrayRestaurantes;

                } catch (IOException | JSONException e) {
                    Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                    return arrayRestaurantes;
                }
            }


            ArrayList<Restaurantes> parsearResultadoRestosPrecio (String JSONstr)throws
            JSONException {
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


                    RestaurantArrayList.add(re);

                }
                return RestaurantArrayList;
            }
        }


    private void setListViewHeader() {
        LayoutInflater inflater = LayoutInflater.from(thisContext);
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
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left,header.findViewById(R.id.bottom_wrapper));

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
        adapter = new AdaptadorListViewRestaurantes(thisContext,R.layout.list_item_restaurant, lista, ActividadPrincipal.usuarioActual,2);
        listView.setAdapter(adapter);

        //totalClassmates.setText("Restaurantes");
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


            listaRestaurantes.addAll(listaRestos);

            setListViewAdapter(listaRestaurantes);
            //setListViewHeader();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Restaurantes unResto = listaRestaurantes.get(position);
                        Log.d("Test", "00");
                        Log.d("Test", listaRestaurantes.get(position) + "");
                        Intent mapActivity = new Intent(thisContext, MapsActivity.class);

                        mapActivity.putExtra("PosicionRestaurantLista",position);
                        mapActivity.putExtra("FragmentLlamador",TAG);
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
    public Dialog onCreateDialogSingleChoiceCalidad() {

//Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
//Source of the data in the DIalog
        String[] array = {"Baja", "Media", "Alta"};

        final List<String> optionsList = Arrays.asList(array);
// Set the dialog title
        builder.setTitle("Buscar por...")
// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                        seleccionCalidad = optionsList.get(which);

                        Log.d("currentItem",seleccionCalidad);
                        // Notify the current action
                        Toast.makeText(thisContext,
                                seleccionCalidad, Toast.LENGTH_SHORT).show();

                    }
                })

// Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog
                        Dialog dialogo = onCreateDialogSingleChoicePrecio();
                        dialogo.show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
    public Dialog onCreateDialogSingleChoicePrecio() {

//Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
//Source of the data in the DIalog
        String[] array = {"Bajo", "Medio", "Alto"};

        final List<String> optionsList = Arrays.asList(array);
// Set the dialog title
        builder.setTitle("Buscar por...")
// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                        seleccionPrecio = optionsList.get(which);

                        Log.d("currentItem",seleccionPrecio);
                        // Notify the current action
                        Toast.makeText(thisContext,
                                seleccionPrecio, Toast.LENGTH_SHORT).show();

                    }
                })

// Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog
                        Dialog dialogo = onCreateDialogSingleChoiceAmbientacion();
                        dialogo.show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
    public Dialog onCreateDialogSingleChoiceAmbientacion() {

//Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
//Source of the data in the DIalog
        String[] array = {"Baja", "Media", "Alta"};

        final List<String> optionsList = Arrays.asList(array);
// Set the dialog title
        builder.setTitle("Buscar por...")
// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                        seleccionAmbientacion = optionsList.get(which);

                        Log.d("currentItem",seleccionAmbientacion);
                        // Notify the current action
                        Toast.makeText(thisContext,
                                seleccionAmbientacion, Toast.LENGTH_SHORT).show();

                    }
                })

// Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }


}
