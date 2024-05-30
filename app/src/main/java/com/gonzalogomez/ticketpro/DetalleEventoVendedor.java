package com.gonzalogomez.ticketpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;
import com.gonzalogomez.ticketpro.BaseDatos.EventoDAO;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Evento;

import java.sql.SQLException;

public class DetalleEventoVendedor extends AppCompatActivity {

    TextView artista, lugar, fecha;
    ImageView imagen;
    Evento evento;
    Cuenta cuenta;
    Switch estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_evento_vendedor);
        artista = findViewById(R.id.txtArtista_v);
        lugar = findViewById(R.id.txtLugar_v);
        fecha = findViewById(R.id.txtFecha_v);
        imagen = findViewById(R.id.imagenEvento_v);
        estado = findViewById(R.id.checkActivo);
        cuenta = (Cuenta)getIntent().getExtras().getSerializable("cuenta");
        rellenarEvento();
        rellenarEntradas();

        estado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    cambiarEstadoEvento(isChecked);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void lanzarEditarEvento(View v){
        Intent i = new Intent(this, CrearEvento.class);
        i.putExtra("evento", evento);
        i.putExtra("cuenta", cuenta);
        i.putExtra("tipo","Editar");
        startActivity(i);
    }

    public void lanzarVolverAtras(View v){
        Intent i = new Intent(this, EventosVendedor.class);
        i.putExtra("cuenta", cuenta);
        startActivity(i);
    }

    public void cambiarEstadoEvento(boolean isChecked) throws SQLException {
        ConexionBD bd = new ConexionBD();
        bd.conexionBD();
        EventoDAO ed = new EventoDAO(bd.getConexion());
        ed.cambiarEstadoEvento(evento, isChecked);
        bd.getConexion().close();
    }

    public void rellenarEvento(){
        evento = (Evento) getIntent().getExtras().getSerializable("evento");
        artista.setText(evento.getArtista());
        lugar.setText(evento.getLugar());
        fecha.setText(evento.getFecha());
        Glide.with(this).load(evento.getDirImagen()).into(imagen);
        estado.setChecked(evento.getEstado());
    }

    public void rellenarEntradas() {
        TextView vendidas = findViewById(R.id.entradas_vendidas);
        TextView disponibles = findViewById(R.id.entradas_disponibles);
        TextView totales = findViewById(R.id.entradas_totales);

        vendidas.setText(vendidas.getText() + Integer.toString(evento.getEntradasVendidas()));
        disponibles.setText(disponibles.getText() + Integer.toString(evento.getEntradasDisponibles()));
        totales.setText(totales.getText() + Integer.toString(evento.getNumEntradas()));
    }
}
