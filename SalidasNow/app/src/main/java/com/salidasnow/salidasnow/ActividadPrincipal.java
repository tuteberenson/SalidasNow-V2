package com.salidasnow.salidasnow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.squareup.okhttp.internal.io.FileSystem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.salidasnow.salidasnow.FragmentBuscarRestaurantes.spncalidad;
import static com.salidasnow.salidasnow.FragmentBuscarRestaurantes.spnprecio;
import static com.salidasnow.salidasnow.FragmentBuscarRestaurantes.txtnombre;

public class ActividadPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txVwNombre,txVwInfo;
    ImageView imgVwIcono;
   public static Usuarios usuarioActual;

    private boolean isStartup;
    MenuItem itemAzar, itemSearch;
    private ArrayList<Integer> mSelectedItems;
    String opcionSeleccionadaBuscarRestaurantes;

    private final static String TAG = ActividadPrincipal.class.getSimpleName();

    //TODO fijarse lo de las estrellas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isStartup=true;

        Bundle paquete;
        paquete=new Bundle();

        usuarioActual=new Usuarios();
        paquete = getIntent().getExtras();
        usuarioActual = (Usuarios)paquete.get("usuario");

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

        Fragment fragment;

        fragment = new FragmentRestaurantesAzar();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.FrameContenedor, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_pan_tool_black_24dp)
                    .setTitle("Salir")
                    .setMessage("¿Estás seguro?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
           //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actividad_principal, menu);
        itemAzar= menu.findItem(R.id.action_random);
        itemSearch=menu.findItem(R.id.action_select_search);
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
        else if (id == R.id.action_select_search)
        {
            Dialog dialog = onCreateDialogSingleChoice();
            dialog.show();
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

        if (id == R.id.nav_Azar) {
            fragment = new FragmentRestaurantesAzar();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FrameContenedor, fragment).commit();
            itemSearch.setVisible(false);
            itemAzar.setVisible(false);
        } else if (id == R.id.nav_recomendador) {
           fragment = new FragmentRecomendador();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FrameContenedor, fragment).commit();
            itemSearch.setVisible(false);
            itemAzar.setVisible(false);
        } else if (id == R.id.nav_likeados) {
           // fragmentClass = FragmentRestaurantesLikeados.class;
            fragment = new FragmentRestaurantesLikeados();
            itemSearch.setVisible(false);
            itemAzar.setVisible(true);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FrameContenedor, fragment).commit();

        } else if (id == R.id.nav_buscar_restaurantes) {
            fragment = new FragmentBuscarRestaurantes();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FrameContenedor, fragment).commit();
            itemAzar.setVisible(false);
            itemSearch.setVisible(true);
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



    public Dialog onCreateDialogSingleChoice() {

//Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//Source of the data in the DIalog
        String[] array = {"Nombre", "Calidad", "Precio"};

        final List<String> optionsList = Arrays.asList(array);
// Set the dialog title
        builder.setTitle("Buscar por...")
// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                         opcionSeleccionadaBuscarRestaurantes = optionsList.get(which);

                        Log.d("currentItem",opcionSeleccionadaBuscarRestaurantes);
                        // Notify the current action
                        Toast.makeText(getApplicationContext(),
                                opcionSeleccionadaBuscarRestaurantes, Toast.LENGTH_SHORT).show();

                    }
                })

// Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog
                        switch (opcionSeleccionadaBuscarRestaurantes) {
                            case "Nombre":
                                txtnombre.setVisibility(View.VISIBLE);
                                spnprecio.setVisibility(View.INVISIBLE);
                                spncalidad.setVisibility(View.INVISIBLE);

                                break;
                            case "Calidad":
                                spncalidad.setVisibility(View.VISIBLE);
                                spnprecio.setVisibility(View.INVISIBLE);
                                txtnombre.setVisibility(View.INVISIBLE);
                                break;
                            case "Precio":
                                spnprecio.setVisibility(View.VISIBLE);
                                spncalidad.setVisibility(View.INVISIBLE);
                                txtnombre.setVisibility(View.INVISIBLE);
                                break;
                        }

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
