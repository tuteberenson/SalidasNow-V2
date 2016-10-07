package com.salidasnow.salidasnow;

import java.io.Serializable;

/**
 * Created by berenson on 26/09/2016.
 */

public class Restaurantes implements Serializable {

    private int _IdRestaurant,_Precio, _Estrellas,_NumTelefono;
    private String _Nombre, _Direccion;
    private double _Latitud,_Longitud;
    private boolean _Likeado;

    public Restaurantes()
    {

    }

    public int get_IdRestaurant() {
        return _IdRestaurant;
    }

    public void set_IdRestaurant(int _IdRestaurant) {
        this._IdRestaurant = _IdRestaurant;
    }

    public int get_Precio() {
        return _Precio;
    }

    public void set_Precio(int _Precio) {
        this._Precio = _Precio;
    }

    public int get_Estrellas() {
        return _Estrellas;
    }

    public void set_Estrellas(int _Estrellas) {
        this._Estrellas = _Estrellas;
    }

    public int get_NumTelefono() {
        return _NumTelefono;
    }

    public void set_NumTelefono(int _NumTelefono) {
        this._NumTelefono = _NumTelefono;
    }

    public String get_Nombre() {
        return _Nombre;
    }

    public void set_Nombre(String _Nombre) {
        this._Nombre = _Nombre;
    }

    public String get_Direccion() {
        return _Direccion;
    }

    public void set_Direccion(String _Direccion) {
        this._Direccion = _Direccion;
    }

    public double get_Latitud() {
        return _Latitud;
    }

    public void set_Latitud(double _Latitud) {
        this._Latitud = _Latitud;
    }

    public double get_Longitud() {
        return _Longitud;
    }

    public void set_Longitud(double _Longitud) {
        this._Longitud = _Longitud;
    }

    public boolean is_Likeado() {
        return _Likeado;
    }

    public void set_Likeado(boolean _Likeado) {
        this._Likeado = _Likeado;
    }
}
