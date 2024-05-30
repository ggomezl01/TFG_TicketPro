package com.gonzalogomez.ticketpro.Entidades;

import java.io.Serializable;
import java.util.Date;

public class Cuenta implements Serializable {
    int idUsuario;
    String usuario;
    String tipoCuenta;
    String dirImagen;
    String fechaNac;
    String nomApe;

    public Cuenta() {
    }

    public Cuenta(int idUsuario, String usuario, String tipoCuenta) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.tipoCuenta = tipoCuenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getDirImagen() {
        return dirImagen;
    }

    public void setDirImagen(String dirImagen) {
        this.dirImagen = dirImagen;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getNomApe() {
        return nomApe;
    }

    public void setNomApe(String nomApe) {
        this.nomApe = nomApe;
    }
}
