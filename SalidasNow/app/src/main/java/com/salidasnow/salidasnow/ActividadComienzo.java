package com.salidasnow.salidasnow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActividadComienzo extends Activity {

    Generics generics;
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generics=new Generics(ActividadComienzo.this);
        if (generics.isOnline()) {

                Intent activity=new Intent(ActividadComienzo.this,ActividadLogIn.class);
                startActivity(activity);
                finish();
        }
        else
        {
            setContentView(R.layout.actividad_comienzo);
            btnOK = (Button)findViewById(R.id.btnOk);

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


}
