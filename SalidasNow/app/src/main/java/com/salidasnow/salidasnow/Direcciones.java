package com.salidasnow.salidasnow;

/**
 * Created by berenson on 16/10/2016.
 */

public class Direcciones
{
    String direccion;
    double lng, lat;

    public Direcciones(String direccion, double lat,double lng) {
        this.direccion = direccion;
        this.lat = lat;
        this.lng = lng;
    }
}
