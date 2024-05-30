package com.gonzalogomez.ticketpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;
import com.gonzalogomez.ticketpro.BaseDatos.CuentaDAO;

import java.sql.SQLException;

public class CrearCuenta extends AppCompatActivity {

    ConexionBD bd = new ConexionBD();
    CuentaDAO cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
    }

    public void lanzarLogin(View v){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void crearCuenta(View v) throws SQLException {
        String valorUsuario, valorContraseña, valorTipoCuenta="";
        EditText usuario = findViewById(R.id.textoUsuario);
        EditText contraseña = findViewById(R.id.textoContraseña);
        RadioButton vendedor = findViewById(R.id.opcionVendedor);
        RadioButton comprador = findViewById(R.id.opcionComprador);
        bd.conexionBD();
        cr = new CuentaDAO(bd.getConexion());

        if(!vendedor.isChecked() && !comprador.isChecked()) Toast.makeText(CrearCuenta.this, "Elige un tipo de cuenta.", Toast.LENGTH_SHORT).show();
        else if(usuario.getText() == null || usuario.getText().toString().trim().isEmpty()) Toast.makeText(CrearCuenta.this, "El usuario está vacío.", Toast.LENGTH_LONG).show();
        else if(contraseña.getText() == null || contraseña.getText().toString().trim().isEmpty()) Toast.makeText(CrearCuenta.this, "La contraseña está vacía.", Toast.LENGTH_LONG).show();
        else if(cr.existeUsuario(usuario.getText().toString())) Toast.makeText(CrearCuenta.this, "El usuario ya existe.", Toast.LENGTH_LONG).show();
        else{
            if(vendedor.isChecked()) valorTipoCuenta = "Vendedor";
            if(comprador.isChecked()) valorTipoCuenta = "Comprador";
            valorUsuario = usuario.getText().toString();
            valorContraseña = contraseña.getText().toString();

            try {
                cr.alta(valorUsuario, valorContraseña, valorTipoCuenta);
                Toast.makeText(CrearCuenta.this, "La cuenta ha sido creada.", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(CrearCuenta.this, "La cuenta no ha podido ser creada.", Toast.LENGTH_LONG).show();
            }
        }
        bd.getConexion().close();
    }
}