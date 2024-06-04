package com.gonzalogomez.ticketpro.BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private String url = "jdbc:postgresql://bbrp2hfpsa9ifrk7enjx-postgresql.services.clever-cloud.com:50013/bbrp2hfpsa9ifrk7enjx?currentSchema=ticketpro";
    private String usuario = "uwiovcxrvpvkkgg3f2fx";
    private String contraseña = "1N7VyVhLosyU1TOGSOPzYjxZNH57ff";
    private Connection c;

    public void conexionBD(){
        try {
            c = DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConexion(){
        return c;
    }
}
