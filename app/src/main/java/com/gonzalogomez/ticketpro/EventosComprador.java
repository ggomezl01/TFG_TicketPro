package com.gonzalogomez.ticketpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;
import com.gonzalogomez.ticketpro.BaseDatos.EventoDAO;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Evento;
import com.gonzalogomez.ticketpro.Eventos.ListadoEventosAdaptador;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventosComprador extends AppCompatActivity {

    List<Evento> eventos = new ArrayList<>();
    RecyclerView listado;
    Cuenta cuenta;
    ConexionBD bd = new ConexionBD();
    EventoDAO ed;
    Button btnVerCuenta, btnVerCesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.eventos);
            btnVerCesta = findViewById(R.id.btnVerCesta);
            btnVerCesta.setVisibility(View.VISIBLE);
            btnVerCuenta = findViewById(R.id.btnVerCuenta);
            bd.conexionBD();
            ed = new EventoDAO(bd.getConexion());
            cuenta = (Cuenta)getIntent().getExtras().getSerializable("cuenta");
            cargarEventos();
            rellenarListado();
            cargarNombreUsuario();
            bd.getConexion().close();
            btnVerCuenta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lanzarCuenta(v);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void onBackPressed() {}

    private void cargarEventos() throws SQLException {
        eventos = ed.obtenerEventosActivos();
        for(Evento e: eventos){ if(e.getNumEntradas() == e.getEntradasVendidas()){ ed.cambiarEstadoEvento(e, false); } }
        eventos = ed.obtenerEventosActivos();
    }

    public void lanzarCuenta(View v){
        Intent i = new Intent(this, DetalleCuenta.class);
        i.putExtra("cuenta", cuenta);
        startActivity(i);
    }

    public void lanzarCesta(View v){
        Intent i = new Intent(this, CestaEventos.class);
        i.putExtra("cuenta", cuenta);
        startActivity(i);
    }

    private void rellenarListado(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listado = findViewById(R.id.listadoEventos);
        listado.setLayoutManager(manager);
        ListadoEventosAdaptador adaptador = new ListadoEventosAdaptador(eventos, this, cuenta);
        listado.setAdapter(adaptador);
    }

    private void cargarNombreUsuario(){
        TextView texto = findViewById(R.id.bienvenidaUsuario);
        texto.setText("¡Hola, "+cuenta.getUsuario()+"!");
    }
}