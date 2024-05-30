package com.gonzalogomez.ticketpro.BaseDatos;

import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Entrada;
import com.gonzalogomez.ticketpro.Entidades.Evento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAO {
    Connection c;

    public EntradaDAO(Connection c){
        this.c = c;
    }

    public void crearEntrada(Entrada e) throws SQLException {
        Statement s = c.createStatement();
        String consulta = "INSERT INTO ticketpro.entrada VALUES('"+e.getIdEntrada()+"','"+e.getFichero()+"',"+e.getIdEvento().getIdEvento()+","+e.getIdUsuario().getIdUsuario()+")";
        s.executeUpdate(consulta);
    }

    public List<Evento> obtenerEventosConEntradas(Cuenta cuenta) throws SQLException {
        Statement s = c.createStatement();
        List<Evento> eventos = new ArrayList<>();
        EventoDAO ed = new EventoDAO(c);
        String consulta = "SELECT DISTINCT id_evento FROM ticketpro.entrada WHERE id_usuario="+cuenta.getIdUsuario()+";";
        ResultSet idsEventos = s.executeQuery(consulta);
        while(idsEventos.next()){
            Evento e = ed.obtenerEvento(idsEventos.getInt("id_evento"));
            eventos.add(e);
        }
        return eventos;
    }

    public List<Entrada> obtenerEntradas(Evento evento, Cuenta cuenta) throws SQLException {
        Statement s = c.createStatement();
        List<Entrada> entradas = new ArrayList<>();
        String consulta = "SELECT * FROM ticketpro.entrada WHERE id_usuario="+cuenta.getIdUsuario()+" AND id_evento="+evento.getIdEvento()+";";
        ResultSet resultado = s.executeQuery(consulta);

        while(resultado.next()){
            Entrada e = new Entrada();
            e.setIdEntrada(resultado.getString("id_entrada"));
            e.setFichero(resultado.getString("fichero"));
            e.setIdUsuario(cuenta);
            e.setIdEvento(evento);
            entradas.add(e);
        }

        return entradas;
    }
}
