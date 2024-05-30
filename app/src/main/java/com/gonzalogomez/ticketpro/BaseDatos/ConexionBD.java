package com.gonzalogomez.ticketpro.BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private String url = "";
    private String usuario = "";
    private String contraseña = "";
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
