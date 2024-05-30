package com.gonzalogomez.ticketpro.BaseDatos;

import com.gonzalogomez.ticketpro.Entidades.Cesta;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CestaDAO {
    Connection c;

    public CestaDAO(Connection c){
        this.c = c;
    }

    public boolean existeElementoCesta(Cesta cesta) throws SQLException {
        Statement s = c.createStatement();
        String consulta = "SELECT * FROM ticketpro.cesta WHERE id_usuario="+cesta.getUsuario().getIdUsuario()+" AND id_evento="+cesta.getEvento().getIdEvento()+";";
        ResultSet resultado = s.executeQuery(consulta);
        if(resultado.next()) return true;
        else return false;
    }

    public void añadirEvento(Cesta cesta){
        try{
            Statement s = c.createStatement();
            String consulta = "INSERT INTO ticketpro.cesta VALUES("+cesta.getUsuario().getIdUsuario()+","+ cesta.getEvento().getIdEvento()+","+cesta.getNumEntradas()+");";
            s.executeUpdate(consulta);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void eliminarElementoCesta(Cesta cesta){
        try{
            Statement s = c.createStatement();
            String consulta = "DELETE FROM ticketpro.cesta WHERE id_usuario="+cesta.getUsuario().getIdUsuario()+" AND id_evento="+cesta.getEvento().getIdEvento()+";";
            s.executeUpdate(consulta);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<Cesta> obtenerEventos(Cuenta cuenta) throws SQLException {
        List<Cesta> eventos = new ArrayList<>();
        Statement s = c.createStatement();
        String consulta = "SELECT * FROM ticketpro.cesta WHERE id_usuario="+cuenta.getIdUsuario()+";";
        ResultSet resultado = s.executeQuery(consulta);
        while(resultado.next()){
            Cesta cesta = new Cesta();
            EventoDAO ed = new EventoDAO(c);

            cesta.setUsuario(cuenta);
            cesta.setEvento(ed.obtenerEvento(resultado.getInt("id_evento")));
            cesta.setNumEntradas(resultado.getInt("num_entradas"));

            eventos.add(cesta);
        }
        return eventos;
    }

    public void vaciarCesta(Cuenta cuenta) throws SQLException {
        Statement s = c.createStatement();
        String consulta = "DELETE FROM ticketpro.cesta WHERE id_usuario="+cuenta.getIdUsuario()+";";
        s.executeUpdate(consulta);
    }
}
