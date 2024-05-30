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
import com.gonzalogomez.ticketpro.Entidades.Evento;
import com.gonzalogomez.ticketpro.Eventos.ListadoEventosEntradaAdaptador;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class Entradas extends AppCompatActivity {
    EntradaDAO ed;
    ConexionBD bd = new ConexionBD();
    List<Evento> eventos;
    Cuenta cuenta;
    RecyclerView listado;
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.entradas);
            bd.conexionBD();
            ed = new EntradaDAO(bd.getConexion());
            cuenta = (Cuenta)getIntent().getExtras().getSerializable("cuenta");
            cargarEventosConEntrada();
            rellenarListado();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void onBackPressed() {}

    public void lanzarEventos(View v){
        Intent i = new Intent(this, EventosComprador.class);
        i.putExtra("cuenta", (Serializable) cuenta);
        startActivity(i);
    }

    private void cargarEventosConEntrada() throws SQLException {
        eventos = ed.obtenerEventosConEntradas(cuenta);
        bd.getConexion().close();
    }

    private void rellenarListado(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listado = findViewById(R.id.listadoEventosEntradas);
        listado.setLayoutManager(manager);
        ListadoEventosEntradaAdaptador adaptador = new ListadoEventosEntradaAdaptador(eventos, this, cuenta);
        listado.setAdapter(adaptador);
    }
}
