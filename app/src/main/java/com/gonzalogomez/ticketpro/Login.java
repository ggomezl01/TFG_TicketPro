package com.gonzalogomez.ticketpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;
import com.gonzalogomez.ticketpro.BaseDatos.CuentaDAO;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;

import java.io.Serializable;
import java.sql.SQLException;

public class Login extends AppCompatActivity {

    ConexionBD bd = new ConexionBD();
    CuentaDAO cr;
    Cuenta cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
    }

    public void lanzarCrearCuenta(View v){
        Intent i = new Intent(this, CrearCuenta.class);
        startActivity(i);
    }

    public void lanzarEventos(){
        Intent i;
        if(cuenta.getTipoCuenta().equals("Vendedor")) i = new Intent(this, EventosVendedor.class);
        else i = new Intent(this, EventosComprador.class);
        i.putExtra("cuenta", (Serializable) cuenta);
        startActivity(i);
    }

    public void iniciarSesion(View v) throws SQLException {
        String valorUsuario, valorContraseña;
        EditText usuario = findViewById(R.id.textoUsuario);
        EditText contraseña = findViewById(R.id.textoContraseña);
        bd.conexionBD();
        cr = new CuentaDAO(bd.getConexion());

        if(usuario.getText().toString().trim().isEmpty()) Toast.makeText(Login.this, "El usuario está vacío.", Toast.LENGTH_LONG).show();
        else if(contraseña.getText().toString().trim().isEmpty()) Toast.makeText(Login.this, "La contraseña está vacía.", Toast.LENGTH_LONG).show();
        else if(!cr.existeUsuario(usuario.getText().toString())) Toast.makeText(Login.this, "El usuario introducido no existe.", Toast.LENGTH_LONG).show();
        else{
            valorUsuario = usuario.getText().toString();
            valorContraseña = contraseña.getText().toString();
            boolean haIniciado = cr.iniciarSesion(valorUsuario, valorContraseña);

            if(haIniciado){
                cuenta = cr.obtenerCuenta(valorUsuario);
                lanzarEventos();
            }else{ Toast.makeText(Login.this, "Inicio de sesión incorrecto.", Toast.LENGTH_LONG).show(); }
        }
        bd.getConexion().close();
    }
}