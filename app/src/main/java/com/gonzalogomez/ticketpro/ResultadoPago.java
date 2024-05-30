package com.gonzalogomez.ticketpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gonzalogomez.ticketpro.Entidades.Cuenta;

public class ResultadoPago extends AppCompatActivity {
    boolean pagoRealizado;
    ConstraintLayout correcto, incorrecto;
    Cuenta cuenta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultado_pago);
        correcto = findViewById(R.id.pagoCorrecto);
        incorrecto = findViewById(R.id.pagoIncorrecto);
        pagoRealizado = getIntent().getExtras().getBoolean("pagoRealizado");
        cuenta = (Cuenta) getIntent().getExtras().getSerializable("cuenta");
        if(pagoRealizado){ correcto.setVisibility(View.VISIBLE); }
        else{ incorrecto.setVisibility(View.VISIBLE); }
    }

    public void irAEntradas(View v){
        Intent i = new Intent(this, Entradas.class);
        i.putExtra("cuenta", cuenta);
        startActivity(i);
    }

    public void irAEventos(View v){
        Intent i = new Intent(this, EventosComprador.class);
        i.putExtra("cuenta", cuenta);
        startActivity(i);
    }
}
