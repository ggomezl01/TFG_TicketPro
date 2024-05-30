package com.gonzalogomez.ticketpro.BaseDatos;

import com.gonzalogomez.ticketpro.Entidades.Cesta;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Evento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    Connection c;

    public EventoDAO(Connection c){
        this.c = c;
    }

    public void cambiarEstadoEvento(Evento e, boolean estado) throws SQLException {
        Statement s = c.createStatement();
        String consulta = "UPDATE ticketpro.evento SET esta_activo="+estado+" WHERE id_evento="+e.getIdEvento()+";";
        s.executeUpdate(consulta);
    }

    public List<Evento> obtenerEventosCreador(int creador) throws SQLException {
        List<Evento> eventos = new ArrayList<>();
        CuentaDAO cd = new CuentaDAO(c);
        Statement s = c.createStatement();
        String consulta = "SELECT * FROM ticketpro.evento WHERE creador="+creador+";";
        ResultSet rs = s.executeQuery(consulta);

        while(rs.next()){
            Evento e = new Evento();
            e.setIdEvento(rs.getInt("id_evento"));
            e.setArtista(rs.getString("artista"));
            e.setLugar(rs.getString("lugar"));
            e.setFecha(rs.getString("fecha_hora"));
            e.setNumEntradas(rs.getInt("num_entradas"));
            e.setEntradasVendidas(rs.getInt("entradas_vendidas"));
            e.setEntradasDisponibles(rs.getInt("entradas_disponibles"));
            e.setDirImagen(rs.getString("dir_imagen"));
            e.setPrecioEntrada(rs.getDouble("precio_entrada"));
            e.setCreador(cd.obtenerCuenta(creador));
            e.setEstado(rs.getBoolean("esta_activo"));
            eventos.add(e);
        }

        return eventos;
    }

    public void reservarEntradas(Evento e, int numEntradas) throws SQLException {
        Statement s = c.createStatement();
        String consulta = "UPDATE ticketpro.evento SET entradas_disponibles = entradas_disponibles - " + numEntradas + ", entradas_cesta = "+numEntradas+" WHERE id_evento = " + e.getIdEvento() + ";";
        s.executeUpdate(consulta);
    }

    public Evento obtenerEvento(int idEvento) throws SQLException {
        Evento evento = new Evento();
        CuentaDAO cd = new CuentaDAO(c);
        Statement s = c.createStatement();
        String consulta = "SELECT * FROM ticketpro.evento WHERE id_evento="+idEvento+";";
        ResultSet resultado = s.executeQuery(consulta);
        resultado.next();

        evento.setIdEvento(idEvento);
        evento.setArtista(resultado.getString("artista"));
        evento.setLugar(resultado.getString("lugar"));
        evento.setFecha(resultado.getString("fecha_hora"));
        evento.setNumEntradas(resultado.getInt("num_entradas"));
        evento.setEntradasVendidas(resultado.getInt("entradas_vendidas"));
        evento.setEntradasDisponibles(resultado.getInt("entradas_disponibles"));
        evento.setDirImagen(resultado.getString("dir_imagen"));
        evento.setPrecioEntrada(resultado.getDouble("precio_entrada"));
        evento.setCreador(cd.obtenerCuenta(resultado.getInt("creador")));
        evento.setEstado(resultado.getBoolean("esta_activo"));

        return evento;
    }

    public List<Evento> obtenerEventosActivos() throws SQLException {
        List<Evento> eventos = new ArrayList<>();
        CuentaDAO cd = new CuentaDAO(c);
        Statement s = c.createStatement();
        String consulta = "SELECT * FROM ticketpro.evento WHERE esta_activo=true;";
        ResultSet rs = s.executeQuery(consulta);

        while(rs.next()){
            Evento e = new Evento();
            e.setIdEvento(rs.getInt("id_evento"));
            e.setArtista(rs.getString("artista"));
            e.setLugar(rs.getString("lugar"));
            e.setFecha(rs.getString("fecha_hora"));
            e.setNumEntradas(rs.getInt("num_entradas"));
            e.setEntradasVendidas(rs.getInt("entradas_vendidas"));
            e.setEntradasDisponibles(rs.getInt("entradas_disponibles"));
            e.setDirImagen(rs.getString("dir_imagen"));
            e.setPrecioEntrada(rs.getDouble("precio_entrada"));
            int id = rs.getInt("creador");
            e.setCreador(cd.obtenerCuenta(id));
            e.setEstado(rs.getBoolean("esta_activo"));
            eventos.add(e);
        }

        return eventos;
    }

    public boolean crearEvento(Evento evento){
        try{
            Statement s = c.createStatement();
            String consulta = "INSERT INTO ticketpro.evento(artista,lugar,fecha_hora,num_entradas,entradas_disponibles,dir_imagen,precio_entrada,creador) " +
                    "VALUES('"+evento.getArtista()+"','"+evento.getLugar()+"','"+evento.getFecha()+"',"+evento.getNumEntradas()+","+evento.getEntradasDisponibles()+
                    ",'"+evento.getDirImagen()+"',"+evento.getPrecioEntrada()+","+evento.getCreador().getIdUsuario()+");";
            s.executeUpdate(consulta);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean modificarEvento(Evento evento) {
        try {
            Statement s = c.createStatement();
            String consulta = "UPDATE ticketpro.evento SET artista='" + evento.getArtista() + "', " + "lugar='" + evento.getLugar() + "', " + "fecha_hora='" + evento.getFecha() + "', " +
                    "dir_imagen='" + evento.getDirImagen() + "', " + "precio_entrada=" + evento.getPrecioEntrada() + ", " +
                    "creador=" + evento.getCreador().getIdUsuario() + " " + "WHERE id_evento=" + evento.getIdEvento();
            s.executeUpdate(consulta);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void venderEntradas(Cesta cesta) {
        try {
            Statement s = c.createStatement();
            String consulta = "UPDATE ticketpro.evento SET entradas_cesta = entradas_cesta-"+cesta.getNumEntradas()+", entradas_vendidas = entradas_vendidas+"+cesta.getNumEntradas()+" WHERE id_evento="+cesta.getEvento().getIdEvento();
            s.executeUpdate(consulta);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
