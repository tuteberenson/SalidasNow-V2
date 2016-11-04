package com.salidasnow.salidasnow;

import android.app.FragmentManager;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuienesSomos extends AppCompatActivity {

    TextView texto,texto2;
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quienes_somos);

        imageView=(CircleImageView) findViewById(R.id.imgnosotros);

        texto = (TextView) findViewById(R.id.txtnosotros);
        texto2 = (TextView) findViewById(R.id.txtnosotros2);
        texto.setText("\n" +
                " Salidas Now es una aplicación móvil, ayudas a las personas a decidir donde pueden ir a comer. Ya esta disponible en el Store de Google Play.\n" +
                " Con esta simple aplicación vas poder conseguir infinidad restaurants cerca de ti con solo un click.");

        texto2.setText(" La aplicación fue creada en el año 2016, por Matias Berenson, Axel Brant, Ilan Pustilnikoff y Ariel Pisterman, un grupo de estudiantes de la Secundaria ORT Yatay.\n");;

        Picasso.with(getApplicationContext())
                .load(R.drawable.cuatro)
                .fit()
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
       /* ActividadPrincipal actividadPrincipal=new ActividadPrincipal();
        Intent activity=new Intent(QuienesSomos.this,ActividadPrincipal.class);
        startActivity(activity);
        actividadPrincipal.set_FragmentAlAzar();*/
        finish();
    }
}
