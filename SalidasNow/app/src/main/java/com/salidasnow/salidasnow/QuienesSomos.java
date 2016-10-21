package com.salidasnow.salidasnow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class QuienesSomos extends AppCompatActivity {

    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quienes_somos);

        texto = (TextView) findViewById(R.id.txtnosotros);
        texto.setText("\n" +
                "Salidas Now es una aplicación móvil, ayudas a las personas a decidir donde pueden ir a comer. Ya esta disponible en el Store de Google Play.\n" +
                "Con esta simple aplicación vas poder conseguir infinidad restaurants cerca de ti con solo un click.\n" +
                "La aplicación fue creada en el año 2016, Matias Berenson, Axel Brant, Ilan Pustilnikoff y Ariel Pisterman, por un grupo de estudiantes de la Secundaria ORT Yatay.\n");
    }
}
