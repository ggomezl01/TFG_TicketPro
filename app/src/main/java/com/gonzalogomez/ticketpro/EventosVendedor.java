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
import com.gonzalogomez.ticketpro.BaseDatos.CuentaDAO;
import com.gonzalogomez.ticketpro.BaseDatos.EventoDAO;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Evento;
import com.gonzalogomez.ticketpro.Eventos.ListadoEventosAdaptador;

import java.sql.SQLException;
import java.util.List;

public class EventosVendedor extends AppCompatActivity {

    List<Evento> eventos;
    RecyclerView listado;
    Button btnCrearEventos, btnVerCuenta;
    ConexionBD bd = new ConexionBD();
    EventoDAO ed;
    CuentaDAO cd;
    Cuenta cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.eventos);
            btnCrearEventos = findViewById(R.id.btnCrearEventos);
            btnCrearEventos.setVisibility(View.VISIBLE);
            btnVerCuenta = findViewById(R.id.btnVerCuenta);
            bd.conexionBD();
            ed = new EventoDAO(bd.getConexion());
            cd = new CuentaDAO(bd.getConexion());
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

    public void lanzarCuenta(View v){
        Intent i = new Intent(this, DetalleCuenta.class);
        i.putExtra("cuenta", cuenta);
        startActivity(i);
    }

    public void lanzarCrearEvento(View v){
        Intent i = new Intent(this, CrearEvento.class);
        i.putExtra("tipo","Crear");
        i.putExtra("cuenta", cuenta);
        startActivity(i);
    }

    private void cargarEventos() throws SQLException {
        eventos = ed.obtenerEventosCreador(cuenta.getIdUsuario());
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
