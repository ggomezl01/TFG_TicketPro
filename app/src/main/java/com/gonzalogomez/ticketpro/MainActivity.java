package com.gonzalogomez.ticketpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cloudinary.android.MediaManager;
import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Map configuracion = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configuracionCloudinary();
    }

    private void configuracionCloudinary(){
        configuracion.put("cloud_name", "");
        configuracion.put("api_key", "");
        configuracion.put("api_secret", "");
        MediaManager.init(this, configuracion);
    }

    public void lanzarLogin(View v){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void lanzarCrearCuenta(View v){
        Intent i = new Intent(this, CrearCuenta.class);
        startActivity(i);
    }
}