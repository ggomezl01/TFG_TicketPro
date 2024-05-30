package com.gonzalogomez.ticketpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;
import com.gonzalogomez.ticketpro.BaseDatos.EntradaDAO;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Entrada;
import com.gonzalogomez.ticketpro.Entidades.Evento;
import com.gonzalogomez.ticketpro.Eventos.ListadoEntradasAdaptador;
import com.gonzalogomez.ticketpro.Eventos.ListadoEventosEntradaAdaptador;

import java.sql.SQLException;
import java.util.List;

public class DetalleEntradas extends AppCompatActivity {
    EntradaDAO ed;
    ConexionBD bd = new ConexionBD();
    List<Entrada> entradas;
    Cuenta cuenta;
    Evento evento;
    RecyclerView listado;
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.detalle_entradas);
            bd.conexionBD();
            ed = new EntradaDAO(bd.getConexion());
            cuenta = (Cuenta)getIntent().getExtras().getSerializable("cuenta");
            evento = (Evento) getIntent().getExtras().getSerializable("evento");
            cargarEntradas();
            rellenarListado();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void volverEventosEntradas(View v){
        Intent i = new Intent(this, Entradas.class);
        i.putExtra("cuenta", cuenta);
        startActivity(i);
    }

    private void cargarEntradas() throws SQLException {
        entradas = ed.obtenerEntradas(evento, cuenta);
        bd.getConexion().close();
    }

    private void rellenarListado(){
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listado = findViewById(R.id.listadoEntradas);
        listado.setLayoutManager(manager);
        ListadoEntradasAdaptador adaptador = new ListadoEntradasAdaptador(entradas, this, cuenta);
        listado.setAdapter(adaptador);
    }
}
