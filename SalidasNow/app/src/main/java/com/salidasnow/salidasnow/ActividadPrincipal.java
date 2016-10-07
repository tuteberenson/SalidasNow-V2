package com.salidasnow.salidasnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ActividadPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txVwNombre,txVwInfo;
    ImageView imgVwIcono;
   public static Usuarios usuarioActual;
    private ListView listView;
    private AdaptadorListViewRestaurantes adapter;
    private TextView totalClassmates;
    private SwipeLayout swipeLayout;
    private boolean isStartup;
    MenuItem itemAzar;
    private ArrayList<Integer> mSelectedItems;

    private final static String TAG = ActividadPrincipal.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        isStartup=true;

        listView=(ListView)findViewById(R.id.listVW_ActPrincipal);

        Bundle paquete;
        paquete=new Bundle();

        usuarioActual=new Usuarios();
        paquete = getIntent().getExtras();
        usuarioActual = (Usuarios)paquete.get("usuario");

        String url="http://salidasnow.hol.es/Restaurantes/obtener_restaurantsRDM.php";
        new TraerRestaurantes().execute(url);


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txVwNombre=(TextView)navigationView.getHeaderView(0).findViewById(R.id.textViewSideBar1);
        txVwInfo=(TextView)navigationView.getHeaderView(0).findViewById(R.id.textViewSideBar2);
        imgVwIcono=(ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageViewSideBar);

        Picasso.with(getApplicationContext())
                .load("http://salidasnow.hol.es/images/"+usuarioActual.get_NombreIMG())
                .fit()
                .into(imgVwIcono);
        txVwNombre.setText(usuarioActual.get_Nombre()+" "+usuarioActual.get_Apellido());
        txVwInfo.setText("Username: " + usuarioActual.get_Username());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actividad_principal, menu);
        itemAzar= menu.findItem(R.id.action_random);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_random)
        {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;



        if(isStartup) {
            ((FrameLayout) findViewById(R.id.FrameContenedor)).removeAllViews();
            isStartup = false;
        }

       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else*/ if (id == R.id.nav_recomendador) {
           fragment = new FragmentRecomendador();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FrameContenedor, fragment).commit();
        } else if (id == R.id.nav_likeados) {
           // fragmentClass = FragmentRestaurantesLikeados.class;
            fragment = new FragmentRestaurantesLikeados();
            itemAzar.setVisible(true);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FrameContenedor, fragment).commit();

        } else if (id == R.id.nav_buscar_restaurantes) {
            fragment = new FragmentBuscarRestaurantes();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FrameContenedor, fragment).commit();
        } /*else if (id == R.id.nav_share) {

        }*/ else if (id == R.id.nav_logout)
        {
            Generics generics=new Generics(ActividadPrincipal.this);
            SQLiteDatabase baseDeDatos;

            baseDeDatos=generics.AbroBaseDatos();

            if (baseDeDatos!=null)
            {
                baseDeDatos.delete("usuarios","",null);

                Intent actividadLogIn = new Intent(ActividadPrincipal.this, ActividadLogIn.class);
                actividadLogIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(actividadLogIn);
                finish();
            }
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class TraerRestaurantes extends AsyncTask<String, Void, ArrayList<Restaurantes>> {
        private ProgressDialog dialog = new ProgressDialog(ActividadPrincipal.this);
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
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
                Toast.makeText(ActividadPrincipal.this, "No hay restaurantes con ese criterio", Toast.LENGTH_SHORT).show();
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

            JSONArray jsonRestaurantes = json.getJSONArray("restaurants");

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
                        if (position != 0) {
                            Restaurantes unResto = listaRestos.get(position - 1);
                            Log.d("Test", "00");
                            Log.d("Test", listaRestos.get(position - 1) + "");
                            Intent mapActivity = new Intent(ActividadPrincipal.this, MapsActivity.class);

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
            LayoutInflater inflater = getLayoutInflater();
            View header = inflater.inflate(R.layout.header_listview, listView, false);
            totalClassmates = (TextView) header.findViewById(R.id.total);
            swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout);
            setSwipeViewFeatures();
            totalClassmates.setText("Restaurantes");
            listView.addHeaderView(header);
        }

        private void setSwipeViewFeatures() {
            //set show mode.
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));

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
            adapter = new AdaptadorListViewRestaurantes(ActividadPrincipal.this,R.layout.list_item_restaurant, lista, usuarioActual,2);
            listView.setAdapter(adapter);

            //totalClassmates.setText("Restaurantes");
        }

        public void updateAdapter() {
            adapter.notifyDataSetChanged(); //update adapter
            totalClassmates.setText(" "); //update total friends in list
        }


}
