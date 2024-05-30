package com.gonzalogomez.ticketpro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.gonzalogomez.ticketpro.BaseDatos.ConexionBD;
import com.gonzalogomez.ticketpro.BaseDatos.EventoDAO;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Evento;

import java.util.HashMap;
import java.util.Map;

public class CrearEvento extends AppCompatActivity {
    Evento e;
    String tipo, url;
    EditText artista, lugar, fecha, entradas, precio;
    ImageButton imagen;
    Uri localizacionImagen;
    boolean cambioImagen = false;
    EventoDAO ed;
    ConexionBD bd;
    Cuenta cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_evento);
        artista = findViewById(R.id.textoArtistaEditable);
        lugar = findViewById(R.id.textoLugarEditable);
        fecha = findViewById(R.id.textoFechaEditable);
        entradas = findViewById(R.id.textoEntradasEditable);
        imagen = findViewById(R.id.imagenEvento_cc);
        precio = findViewById(R.id.textoPrecioEditable);
        cuenta = (Cuenta) getIntent().getExtras().getSerializable("cuenta");
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
        cargarEvento();
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario();
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });
    }

    private void cargarImagen() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        resultadoActividad.launch(i);
    }

    ActivityResultLauncher<Intent> resultadoActividad = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult resultado) {
                    if (resultado.getResultCode() == Activity.RESULT_OK) {
                        Intent datos = resultado.getData();
                        localizacionImagen = datos.getData();
                        MediaManager.get()
                                .upload(localizacionImagen)
                                .option("folder", "imagenes_eventos")
                                .callback(new UploadCallback() {
                                    @Override
                                    public void onStart(String requestId) {
                                    }

                                    @Override
                                    public void onProgress(String requestId, long bytes, long totalBytes) {
                                    }

                                    @Override
                                    public void onSuccess(String requestId, Map resultData) {
                                        url = (String) resultData.get("url");
                                        cambioImagen = cargarImagenSeleccionada();
                                    }

                                    @Override
                                    public void onError(String requestId, ErrorInfo error) {
                                    }

                                    @Override
                                    public void onReschedule(String requestId, ErrorInfo error) {
                                    }
                                }).dispatch();
                    }
                }
            });

    private boolean cargarImagenSeleccionada() {
        Glide.with(this).load(localizacionImagen).into(imagen);
        return true;
    }

    private void abrirCalendario() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(fecha.getWindowToken(), 0);
        TimePickerDialog ventanaHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {
                StringBuilder sb = new StringBuilder();
                sb.append(fecha.getText());

                if (hora < 10) {
                    sb.append("0" + hora + ":");
                } else {
                    sb.append(hora + ":");
                }
                if (minuto < 10) {
                    sb.append("0" + minuto);
                } else {
                    sb.append(minuto);
                }

                fecha.setText(sb.toString());
            }
        }, 18, 00, true);
        DatePickerDialog ventanaFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int año, int mes, int dia) {
                StringBuilder sb = new StringBuilder();
                mes = mes + 1;

                if (dia < 10) {
                    sb.append("0" + dia + "/");
                } else {
                    sb.append(dia + "/");
                }
                if (mes < 10) {
                    sb.append("0" + mes + "/");
                } else {
                    sb.append(mes + "/");
                }
                sb.append(año + " ");

                fecha.setText(sb.toString());
                ventanaHora.show();
            }
        }, 2024, 04, 23);
        ventanaFecha.show();
    }

    public void cargarEvento() {
        tipo = getIntent().getStringExtra("tipo");
        if (tipo.equals("Editar")) {
            e = (Evento) getIntent().getSerializableExtra("evento");
            Glide.with(this).load(e.getDirImagen()).into(imagen);
            artista.setText(e.getArtista());
            lugar.setText(e.getLugar());
            fecha.setText(e.getFecha());
            fecha.setSelected(true);
            fecha.setSelected(false);
            entradas.setText(String.valueOf(e.getNumEntradas()));
            entradas.setEnabled(false);
            precio.setText(String.valueOf(e.getPrecioEntrada()));
        } else if (tipo.equals("Crear")) {
            e = new Evento();
        }
    }

    public void guardarEvento(View v) {
        if (tipo.equals("Crear")) crearEvento();
        else if (tipo.equals("Editar")) modificarEvento();
    }

    public void crearEvento() {
        if (artista.getText().toString().isEmpty() || lugar.getText().toString().isEmpty() || fecha.getText().toString().isEmpty()) {
            Toast.makeText(CrearEvento.this, "Comprueba que los datos no estén incompletos.", Toast.LENGTH_LONG).show();
        } else {
            rellenarEvento();
            if (ed.crearEvento(e)) {
                Intent i = new Intent(this, EventosVendedor.class);
                i.putExtra("cuenta", cuenta);
                startActivity(i);
            } else {
                Toast.makeText(CrearEvento.this, "Error al crear el evento.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void modificarEvento() {
        if (artista.getText().toString().isEmpty() || lugar.getText().toString().isEmpty() || fecha.getText().toString().isEmpty()) {
            Toast.makeText(CrearEvento.this, "Comprueba que los datos no estén incompletos.", Toast.LENGTH_LONG).show();
        } else if (artista.getText().toString().equals(e.getArtista()) && lugar.getText().toString().equals(e.getLugar()) && fecha.getText().toString().equals(e.getFecha())) {
            Toast.makeText(CrearEvento.this, "No hay cambios en los datos.", Toast.LENGTH_LONG).show();
        } else {
            rellenarEvento();
            if (ed.modificarEvento(e)) {
                Intent i = new Intent(this, EventosVendedor.class);
                i.putExtra("cuenta", cuenta);
                startActivity(i);
            } else {
                Toast.makeText(CrearEvento.this, "Error al modificar el evento.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void rellenarEvento(){
        bd = new ConexionBD();
        bd.conexionBD();
        ed = new EventoDAO(bd.getConexion());

        e.setArtista(artista.getText().toString());
        e.setLugar(lugar.getText().toString());
        e.setFecha(fecha.getText().toString());
        e.setNumEntradas(Integer.valueOf(entradas.getText().toString()));
        e.setEntradasDisponibles(Integer.valueOf(entradas.getText().toString()));
        e.setPrecioEntrada(Double.valueOf(precio.getText().toString()));
        if (cambioImagen) {
            e.setDirImagen(url);
        }
        e.setCreador(cuenta);
    }
}
