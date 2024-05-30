package com.gonzalogomez.ticketpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzalogomez.ticketpro.BaseDatos.CestaDAO;
import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;
import com.gonzalogomez.ticketpro.BaseDatos.EventoDAO;
import com.gonzalogomez.ticketpro.Entidades.Cesta;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Evento;
import com.gonzalogomez.ticketpro.Eventos.ListadoReducidoAdaptador;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CestaEventos extends AppCompatActivity {

    List<Cesta> eventos;
    RecyclerView cesta;
    Cuenta cuenta;
    ConexionBD bd = new ConexionBD();
    CestaDAO cd;
    TextView textoTotal;
    double total=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.cesta);
            textoTotal = findViewById(R.id.txtTotal);
            cuenta = (Cuenta)getIntent().getExtras().getSerializable("cuenta");
            bd.conexionBD();
            cd = new CestaDAO(bd.getConexion());
            obtenerCesta();
            rellenarCesta();
            calcularTotal();
            bd.getConexion().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void rellenarCesta(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        cesta = findViewById(R.id.cestaEventos);
        cesta.setLayoutManager(manager);
        ListadoReducidoAdaptador adaptador = new ListadoReducidoAdaptador(eventos, this);
        cesta.setAdapter(adaptador);
    }

    private void obtenerCesta() throws SQLException {
        eventos = cd.obtenerEventos(cuenta);
    }

    public void seguirCompra(View v){
        Intent i = new Intent(this, EventosComprador.class);
        i.putExtra("cuenta",cuenta);
        startActivity(i);
    }

    private void calcularTotal(){
        for(Cesta cesta : eventos){ total = total + (cesta.getNumEntradas() * cesta.getEvento().getPrecioEntrada()); }
        DecimalFormat df = new DecimalFormat("#.00");
        total = Double.valueOf(df.format(total));
        textoTotal.setText("Total: "+total+"€");
    }

    public void lanzarPago(View v){
        if(total==0.0){ Toast.makeText(CestaEventos.this, "La cesta está vacía.", Toast.LENGTH_LONG).show(); }
        else {
            Intent i = new Intent(this, PasarelaPago.class);
            i.putExtra("total", total);
            i.putExtra("cuenta", cuenta);
            startActivity(i);
        }
    }
}