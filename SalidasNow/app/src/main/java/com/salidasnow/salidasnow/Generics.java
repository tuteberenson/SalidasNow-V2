package com.salidasnow.salidasnow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by berenson on 25/09/2016.
 */

public class Generics {

    private Context mContext;

    public Generics(Context context)
    {
        mContext=context;
    }

    public SQLiteDatabase AbroBaseDatos() {
        // boolean huboError = false;
        String motivoError = "";
        try {
            ManejadorDeBaseDeDatos accesoBD = new ManejadorDeBaseDeDatos(mContext, "baseDeDatos", null, 1);
            return accesoBD.getWritableDatabase();
        } catch (Exception error) {

            //huboError = true;
            motivoError = error.getMessage();
            Toast.makeText(mContext, motivoError, Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    private static ConnectivityManager manager;

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
