package com.salidasnow.salidasnow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by berenson on 24/09/2016.
 */

public class ManejadorDeBaseDeDatos extends SQLiteOpenHelper
{

    public ManejadorDeBaseDeDatos(Context contexto, String nombre, SQLiteDatabase.CursorFactory fabrica, int version) {
        super(contexto, nombre, fabrica, version);
    }

    @Override
    public void onCreate(SQLiteDatabase baseDeDatos) {

        String sqlCrearTabla;

        sqlCrearTabla="create table usuarios (username text, password text)";
        baseDeDatos.execSQL(sqlCrearTabla);
    }
    @Override
    public void onUpgrade(SQLiteDatabase baseDeDatos, int versionVieja, int versionNueva) {

    }
}
