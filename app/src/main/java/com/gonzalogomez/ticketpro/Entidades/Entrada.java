package com.gonzalogomez.ticketpro.Entidades;

import java.io.Serializable;

public class Entrada implements Serializable {
    String idEntrada;
    String fichero;
    Evento idEvento;
    Cuenta idUsuario;

    public Entrada(){}

    public Entrada(String idEntrada, String fichero, Evento idEvento, Cuenta idUsuario) {
        this.idEntrada = idEntrada;
        this.fichero = fichero;
        this.idEvento = idEvento;
        this.idUsuario = idUsuario;
    }

    public String getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(String idEntrada) {
        this.idEntrada = idEntrada;
    }

    public String getFichero() {
        return fichero;
    }

    public void setFichero(String fichero) {
        this.fichero = fichero;
    }

    public Evento getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Evento idEvento) {
        this.idEvento = idEvento;
    }

    public Cuenta getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Cuenta idUsuario) {
        this.idUsuario = idUsuario;
    }
}
