package com.salidasnow.salidasnow;

import java.io.Serializable;

/**
 * Created by berenson on 23/09/2016.
 */

public class Usuarios implements Serializable{

    private int _idUsuario;
   // private boolean _Autorizado;
    private String _Nombre, _Apellido, _Username, _Password, _NombreIMG, _Email;
    public Usuarios()
    {

    }

    public int get_idUsuario() {
        return _idUsuario;
    }

    public void set_idUsuario(int _idUsuario) {
        this._idUsuario = _idUsuario;
    }

    /*public boolean is_Autorizado() {
        return _Autorizado;
    }

    public void set_Autorizado(boolean _Autorizado) {
        this._Autorizado = _Autorizado;
    }*/

    public String get_Nombre() {
        return _Nombre;
    }

    public void set_Nombre(String _Nombre) {
        this._Nombre = _Nombre;
    }

    public String get_Apellido() {
        return _Apellido;
    }

    public void set_Apellido(String _Apellido) {
        this._Apellido = _Apellido;
    }

    public String get_Username() {
        return _Username;
    }

    public void set_Username(String _Username) {
        this._Username = _Username;
    }

    public String get_Password() {
        return _Password;
    }

    public void set_Password(String _Password) {
        this._Password = _Password;
    }

    public String get_NombreIMG() {
        return _NombreIMG;
    }

    public void set_NombreIMG(String _NombreIMG) {
        this._NombreIMG = _NombreIMG;
    }

    public String get_Email() {
        return _Email;
    }

    public void set_Email(String _Email) {
        this._Email = _Email;
    }

}
