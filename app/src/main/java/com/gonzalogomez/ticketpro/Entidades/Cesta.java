package com.gonzalogomez.ticketpro.Entidades;

public class Cesta {
    private Cuenta usuario;
    private Evento evento;
    private int numEntradas;

    public Cesta() {
    }

    public Cesta(Cuenta usuario, Evento evento, int numEntradas) {
        this.usuario = usuario;
        this.evento = evento;
        this.numEntradas = numEntradas;
    }

    public Cuenta getUsuario() {
        return usuario;
    }

    public void setUsuario(Cuenta usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public int getNumEntradas() {
        return numEntradas;
    }

    public void setNumEntradas(int numEntradas) {
        this.numEntradas = numEntradas;
    }
}
