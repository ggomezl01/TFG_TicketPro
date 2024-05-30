package com.gonzalogomez.ticketpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gonzalogomez.ticketpro.BaseDatos.CestaDAO;
import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;
import com.gonzalogomez.ticketpro.BaseDatos.EventoDAO;
import com.gonzalogomez.ticketpro.Entidades.Cesta;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Evento;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class DetalleEventoComprador extends AppCompatActivity {

    TextView artista, lugar, fecha, entradas, valor;
    ImageView imagen;
    Evento evento;
    Cuenta cuenta;
    ImageButton sumar, restar;
    Button comprar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_evento_comprador);
        cuenta = (Cuenta)getIntent().getExtras().getSerializable("cuenta");
        artista = findViewById(R.id.txtArtista_c);
        lugar = findViewById(R.id.txtLugar_c);
        fecha = findViewById(R.id.txtFecha_c);
        imagen = findViewById(R.id.imagenEvento_c);
        entradas = findViewById(R.id.txtNumEntradas);
        valor = findViewById(R.id.txtPrecio);
        sumar = findViewById(R.id.btnSumar);
        restar = findViewById(R.id.btnRestar);
        comprar = findViewById(R.id.btnCesta);
        rellenarEvento();

        sumar.setOnClickListener(view -> sumar());
        restar.setOnClickListener(view -> restar());
        comprar.setOnClickListener(view -> {
            try {
                guardarEnCesta();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void rellenarEvento(){
        evento = (Evento) getIntent().getExtras().getSerializable("evento");
        artista.setText(evento.getArtista());
        lugar.setText(evento.getLugar());
        fecha.setText(evento.getFecha());
        Glide.with(this).load(evento.getDirImagen()).into(imagen);
    }

    private void guardarEnCesta() throws SQLException {
        int numero = Integer.valueOf(entradas.getText().toString());
        if(numero != 0){
            Cesta cesta = new Cesta();
            cesta.setUsuario(cuenta);
            cesta.setEvento(evento);
            cesta.setNumEntradas(numero);

            ConexionBD bd = new ConexionBD();
            bd.conexionBD();
            CestaDAO cd = new CestaDAO(bd.getConexion());
            EventoDAO ed = new EventoDAO(bd.getConexion());
            if(cd.existeElementoCesta(cesta)){ Toast.makeText(DetalleEventoComprador.this, "Ya tienes entradas de este evento en la cesta.", Toast.LENGTH_LONG).show(); }
            else{
                evento = ed.obtenerEvento(evento.getIdEvento()); //Recargar evento para comprobar disponibilidad
                if(evento.getEntradasDisponibles() < numero){ Toast.makeText(DetalleEventoComprador.this, "Lo siento, no están disponibles el número de entradas que solicitas. Pruebe a comprar menos entradas.", Toast.LENGTH_LONG).show(); }
                else{
                    ed.reservarEntradas(evento, numero); //Reservar entradas
                    cd.añadirEvento(cesta); //Añadir a cesta
                    irACesta();
                }
            }
            bd.getConexion().close();
        }else{ Toast.makeText(DetalleEventoComprador.this, "Elige entradas para poder añadirlas a la cesta.", Toast.LENGTH_LONG).show(); }
    }

    private void sumar(){
        int numero = Integer.valueOf(entradas.getText().toString());
        double precio = evento.getPrecioEntrada();
        DecimalFormat df = new DecimalFormat("#.00");
        if(numero+1 <= 6 && numero+1<=evento.getEntradasDisponibles()){
            entradas.setText(String.valueOf(numero+1));
            precio = Double.valueOf(df.format(precio*(numero+1)));
            valor.setText(String.valueOf(precio)+"€");
        }
    }

    private void restar(){
        int numero = Integer.valueOf(entradas.getText().toString());
        double precio = evento.getPrecioEntrada();
        DecimalFormat df = new DecimalFormat("#.00");
        if(numero-1 >= 0){
            entradas.setText(String.valueOf(numero-1));
            precio = Double.valueOf(df.format(precio*(numero-1)));
            valor.setText(String.valueOf(precio)+"€");
        }
    }

    public void irACesta(){
        Intent i = new Intent(this, CestaEventos.class);
        i.putExtra("cuenta", cuenta);
        startActivity(i);
    }
}
