package com.salidasnow.salidasnow;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecomendador extends Fragment {

/*    CheckBox checkBox1,checkBox2,checkBox3;
    Button btnMasOpciones, btnRecomendame;
    ArrayList<Indicadores> gArrayIndicadores,indicadoresAlAzar;
    ListView listVw;
    RestaurantAdapterCompleto adaptadorRestos;

    public FragmentRecomendador() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_recomendador, container, false);

        checkBox1 = (CheckBox)vista.findViewById(R.id.chbox1);
        checkBox2 = (CheckBox)vista.findViewById(R.id.chbox2);
        checkBox3 = (CheckBox)vista.findViewById(R.id.chbox3);

        btnMasOpciones= (Button)vista.findViewById(R.id.btnMasOpc);
        btnRecomendame = (Button)vista.findViewById(R.id.btnRecomendame);

        listVw=(ListView)vista.findViewById(R.id.listVwRecomendador);

        gArrayIndicadores=new ArrayList<>();

        String url ="http://salidasnow.hol.es/Indicadores/obtener_indicadores.php";

        Log.d("url solicitada",url);

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
                    Toast.makeText(getContext(), "Seleccione al menos una opción", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Hubo un error", Toast.LENGTH_SHORT).show();
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
*/
}
