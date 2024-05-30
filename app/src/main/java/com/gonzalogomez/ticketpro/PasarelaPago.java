package com.gonzalogomez.ticketpro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.gonzalogomez.ticketpro.BaseDatos.CestaDAO;
import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;
import com.gonzalogomez.ticketpro.BaseDatos.EntradaDAO;
import com.gonzalogomez.ticketpro.BaseDatos.EventoDAO;
import com.gonzalogomez.ticketpro.Entidades.Cesta;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Entrada;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class PasarelaPago extends AppCompatActivity {

    ConexionBD bd = new ConexionBD();
    CestaDAO cd;
    EventoDAO evd;
    EntradaDAO end;
    Cuenta cuenta;
    List<Cesta> eventos;
    double total;
    TextView comercio,importe,fecha,numTarjeta,fechaCad,cvc,titular;
    Button pagar;
    int numIntentos=3;
    List<String> tarjetaValida = new ArrayList<>();
    String urlQR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.pasarela_pago);

            comercio = findViewById(R.id.comercioTrans);
            importe = findViewById(R.id.importeTrans);
            fecha = findViewById(R.id.fechaTrans);
            numTarjeta = findViewById(R.id.txtNumTarjeta);
            fechaCad = findViewById(R.id.txtFechaCad);
            cvc = findViewById(R.id.txtCVC);
            titular = findViewById(R.id.txtNomTitular);
            pagar = findViewById(R.id.btnPagarTarjeta);
            cuenta = (Cuenta)getIntent().getExtras().getSerializable("cuenta");
            total = getIntent().getExtras().getDouble("total");

            bd.conexionBD();
            cd = new CestaDAO(bd.getConexion());
            evd = new EventoDAO(bd.getConexion());
            end = new EntradaDAO(bd.getConexion());
            obtenerCesta();
            cargarDatos();

            pagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        pagarTransaccion();
                    } catch (WriterException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void pagarTransaccion() throws WriterException, SQLException {
        String regexNum = "\\d{16}";
        String regexFecha = "\\d{2}/\\d{2}";
        String regexCVC = "\\d{3}";
        if(numIntentos-1==0){
            cd.vaciarCesta(cuenta);
            finalizarPago(false);
        }
        else if(numTarjeta.getText().toString().isEmpty() || fechaCad.getText().toString().isEmpty() || cvc.getText().toString().isEmpty() || titular.getText().toString().isEmpty()) { Toast.makeText(PasarelaPago.this, "Rellena todos los campos.", Toast.LENGTH_LONG).show(); }
        else if(!Pattern.matches(regexNum, numTarjeta.getText().toString()) || !Pattern.matches(regexFecha, fechaCad.getText().toString()) || !Pattern.matches(regexCVC, cvc.getText().toString())){ Toast.makeText(PasarelaPago.this, "Hay algún dato incorrecto.", Toast.LENGTH_LONG).show(); }
        else if(!numTarjeta.getText().toString().equals(tarjetaValida.get(0)) || !fechaCad.getText().toString().equals(tarjetaValida.get(1)) || !cvc.getText().toString().equals(tarjetaValida.get(2)) || !titular.getText().toString().equals(tarjetaValida.get(3))){
            numIntentos = numIntentos-1;
            Toast.makeText(PasarelaPago.this, "Error al realizar el pago. Quedan "+numIntentos+" intentos.", Toast.LENGTH_LONG).show();
        }else{
            transaccionCorrecta();
            finalizarPago(true);
        }
    }

    private void obtenerCesta() throws SQLException {
        eventos = cd.obtenerEventos(cuenta);
    }

    private void finalizarPago(boolean pagoRealizado) throws SQLException {
        Intent i = new Intent(this, ResultadoPago.class);
        i.putExtra("cuenta", cuenta);
        i.putExtra("pagoRealizado", pagoRealizado);
        startActivity(i);
    }

    private void cargarDatos(){
        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        comercio.setText("TicketPro");
        importe.setText(String.valueOf(total)+"€");
        fecha.setText(fechaActual.format(fechaFormateada));
        tarjetaValida.add("1234567890123456");
        tarjetaValida.add("12/34");
        tarjetaValida.add("123");
        tarjetaValida.add("Gonzalo Gomez");
    }

    private void transaccionCorrecta() throws WriterException, SQLException {
        for(Cesta c : eventos){
            for(int i=0;i<c.getNumEntradas();i++){
                Entrada e = new Entrada();
                UUID uuid = UUID.randomUUID();
                MultiFormatWriter mWriter = new MultiFormatWriter();
                BitMatrix qr = mWriter.encode(uuid.toString(), BarcodeFormat.QR_CODE, 400,400);
                BarcodeEncoder enc = new BarcodeEncoder();
                Bitmap imagenQR = enc.createBitmap(qr);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagenQR.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytesQR = stream.toByteArray();

                e.setIdEntrada(uuid.toString());
                e.setIdUsuario(c.getUsuario());
                e.setIdEvento(c.getEvento());
                MediaManager.get()
                        .upload(bytesQR)
                        .option("folder", "entradas")
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {
                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                urlQR = (String)resultData.get("url");
                                e.setFichero(urlQR);
                                try {
                                    end.crearEntrada(e);
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {
                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {
                            }
                        }).dispatch();
            }
            cd.eliminarElementoCesta(c);
            evd.venderEntradas(c);
        }
    }
}
