package com.gonzalogomez.ticketpro.BaseDatos;

import com.gonzalogomez.ticketpro.Entidades.Cuenta;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;

public class CuentaDAO {
    Connection c;

    public CuentaDAO(Connection c){
        this.c = c;
    }

    public void alta(String usuario, String contraseña, String tipoCuenta) throws SQLException {
        try{
            String pass = encriptarContraseña(contraseña);
            Statement s = c.createStatement();
            String consulta = "INSERT INTO ticketpro.cuenta(usuario,contraseña,tipo_cuenta) VALUES('"+usuario+"','"+pass+"','"+tipoCuenta+"');";
            s.executeUpdate(consulta);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean existeUsuario(String usuario) throws SQLException {
        Statement s = c.createStatement();
        String consulta = "SELECT usuario FROM ticketpro.cuenta WHERE usuario LIKE '"+usuario+"';";
        ResultSet resultado = s.executeQuery(consulta);
        if(resultado.next()) return true;
        else return false;
    }

    public boolean iniciarSesion(String usuario, String contraseña) throws SQLException {
        Statement s = c.createStatement();
        String consulta = "SELECT contraseña FROM ticketpro.cuenta WHERE usuario LIKE '"+usuario+"';";
        ResultSet resultado = s.executeQuery(consulta);
        resultado.next();
        String contraseñaServidor = resultado.getString("contraseña");
        contraseña = encriptarContraseña(contraseña);

        if(contraseñaServidor.equals(contraseña)) return true;
        else return false;
    }

    public String encriptarContraseña(String contraseña){ return DigestUtils.md5Hex(contraseña); }

    public Cuenta obtenerCuenta(String usuario) throws SQLException {
        Cuenta cuenta = new Cuenta();
        Statement s = c.createStatement();
        String consulta = "SELECT * FROM ticketpro.cuenta WHERE usuario LIKE '"+usuario+"';";
        ResultSet resultado = s.executeQuery(consulta);
        resultado.next();

        cuenta.setIdUsuario(resultado.getInt("id_usuario"));
        cuenta.setUsuario(usuario);
        cuenta.setTipoCuenta(resultado.getString("tipo_cuenta"));
        cuenta.setDirImagen(resultado.getString("imagen_perfil"));
        cuenta.setFechaNac(resultado.getString("fecha_nacimiento"));
        cuenta.setNomApe(resultado.getString("nombre_apellidos"));

        return cuenta;
    }

    public Cuenta obtenerCuenta(int idUsuario) throws SQLException {
        Cuenta cuenta = new Cuenta();
        Statement s = c.createStatement();
        String consulta = "SELECT * FROM ticketpro.cuenta WHERE id_usuario="+idUsuario+";";
        ResultSet resultado = s.executeQuery(consulta);
        resultado.next();

        cuenta.setIdUsuario(resultado.getInt("id_usuario"));
        cuenta.setUsuario(resultado.getString("usuario"));
        cuenta.setTipoCuenta(resultado.getString("tipo_cuenta"));
        cuenta.setDirImagen(resultado.getString("imagen_perfil"));
        cuenta.setFechaNac(resultado.getString("fecha_nacimiento"));
        cuenta.setNomApe(resultado.getString("nombre_apellidos"));

        return cuenta;
    }

    public boolean actualizarDatos(Cuenta cuenta) throws SQLException {
        try{
            Statement s = c.createStatement();
            String consulta = "UPDATE ticketpro.cuenta SET nombre_apellidos='"+cuenta.getNomApe()+"'" +
                    ", fecha_nacimiento='"+cuenta.getFechaNac()+"', " +
                    "imagen_perfil='"+cuenta.getDirImagen()+
                    "' WHERE id_usuario="+cuenta.getIdUsuario()+";";
            s.executeUpdate(consulta);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
