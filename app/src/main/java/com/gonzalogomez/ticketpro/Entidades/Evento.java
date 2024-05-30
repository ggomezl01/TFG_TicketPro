package com.gonzalogomez.ticketpro.Entidades;

import java.io.Serializable;

public class Evento implements Serializable {
    private int idEvento;
    private String artista;
    private String lugar;
    private String fecha;
    private int numEntradas;
    private int entradasVendidas;
    private int entradasDisponibles;
    private int entradasCesta;
    private String dirImagen;
    private double precioEntrada;
    private Cuenta creador;
    private boolean estado;

    public Evento(){}
    public Evento(int idEvento, String artista, String lugar, String fecha, String dirImagen, int numEntradas, int entradasVendidas, int entradasDisponibles, int entradasCesta, double precioEntrada, Cuenta creador, boolean estado) {
        this.idEvento = idEvento;
        this.artista = artista;
        this.lugar = lugar;
        this.fecha = fecha;
        this.dirImagen = dirImagen;
        this.numEntradas = numEntradas;
        this.entradasVendidas = entradasVendidas;
        this.entradasDisponibles = entradasDisponibles;
        this.entradasCesta = entradasCesta;
        this.precioEntrada = precioEntrada;
        this.creador = creador;
        this.estado = estado;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDirImagen() {
        return dirImagen;
    }

    public void setDirImagen(String dirImagen) {
        this.dirImagen = dirImagen;
    }

    public int getNumEntradas() {
        return numEntradas;
    }

    public void setNumEntradas(int numEntradas) {
        this.numEntradas = numEntradas;
    }

    public int getEntradasVendidas() {
        return entradasVendidas;
    }

    public void setEntradasVendidas(int entradasVendidas) {
        this.entradasVendidas = entradasVendidas;
    }

    public int getEntradasDisponibles() {
        return entradasDisponibles;
    }

    public void setEntradasDisponibles(int entradasDisponibles) {
        this.entradasDisponibles = entradasDisponibles;
    }

    public int getEntradasCesta() {
        return entradasCesta;
    }

    public void setEntradasCesta(int entradasCesta) {
        this.entradasCesta = entradasCesta;
    }

    public double getPrecioEntrada() {
        return precioEntrada;
    }

    public void setPrecioEntrada(double precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public Cuenta getCreador() {
        return creador;
    }

    public void setCreador(Cuenta creador) {
        this.creador = creador;
    }

    public boolean getEstado() { return estado; }

    public void setEstado(boolean estado) { this.estado = estado; }
}
