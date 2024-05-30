package com.gonzalogomez.ticketpro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.gonzalogomez.ticketpro.BaseDatos.CuentaDAO;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DetalleCuenta extends AppCompatActivity {

    Cuenta cuenta;
    TextView usuario, nomape, fechanac, tipocuenta;
    ConexionBD bd = new ConexionBD();
    CuentaDAO cd;
    Button verEntradas, guardarCambios;
    ImageButton imagen;
    Uri localizacionImagen;
    boolean cambioImagen = false;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_cuenta);
        usuario = findViewById(R.id.nombre_usuario);
        nomape = findViewById(R.id.textoNomApellidos);
        fechanac = findViewById(R.id.textoFechaNac);
        tipocuenta = findViewById(R.id.textoTipoCuenta);
        imagen = findViewById(R.id.imagenPerfil);
        guardarCambios = findViewById(R.id.guardar_cuenta);
        cuenta = (Cuenta) getIntent().getExtras().getSerializable("cuenta");
        try {
            bd.conexionBD();
            cd = new CuentaDAO(bd.getConexion());
            cuenta = cd.obtenerCuenta(cuenta.getUsuario());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cargarDatos();
        if (cuenta.getTipoCuenta().equals("Vendedor")) {
            verEntradas = findViewById(R.id.ver_entradas);
            verEntradas.setVisibility(View.INVISIBLE);
        }

        fechanac.setOnClickListener(new View.OnClickListener() {
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

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    guardarCambios();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void irAEntradas(View v) throws SQLException {
        Intent i = new Intent(this, Entradas.class);
        i.putExtra("cuenta", cuenta);
        bd.getConexion().close();
        startActivity(i);
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
                                .option("folder", "imagenes_perfil")
                                .callback(new UploadCallback() {
                                    @Override
                                    public void onStart(String requestId) {}

                                    @Override
                                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                                    @Override
                                    public void onSuccess(String requestId, Map resultData) { url = (String) resultData.get("url"); }

                                    @Override
                                    public void onError(String requestId, ErrorInfo error) {}

                                    @Override
                                    public void onReschedule(String requestId, ErrorInfo error) {}
                                }).dispatch();
                        cambioImagen = cargarImagenSeleccionada();
                    }
                }
            });

    private boolean cargarImagenSeleccionada() {
        Glide.with(this).load(localizacionImagen).into(imagen);
        return true;
    }

    public void abrirCalendario() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(fechanac.getWindowToken(), 0);
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
                sb.append(año);

                fechanac.setText(sb.toString());
            }
        }, 2024, 04, 23);
        ventanaFecha.show();
    }

    public void cargarDatos() {
        usuario.setText(cuenta.getUsuario());
        nomape.setText(cuenta.getNomApe());
        fechanac.setText(cuenta.getFechaNac());
        tipocuenta.setText(cuenta.getTipoCuenta());
        if(cuenta.getDirImagen() != null){
            Glide.with(this).load(cuenta.getDirImagen()).into(imagen);
        }
    }

    public void guardarCambios() throws SQLException {
        if (nomape.getText().toString().equals(cuenta.getNomApe()) && fechanac.getText().toString().equals(cuenta.getFechaNac())) {
            Toast.makeText(DetalleCuenta.this, "No hay ningún cambio.", Toast.LENGTH_LONG).show();
        }else if (nomape.getText().toString().isEmpty() || fechanac.toString().isEmpty()) {
            Toast.makeText(DetalleCuenta.this, "No se van a guardar datos al estar incompleto.", Toast.LENGTH_LONG).show();
        }else {
            cuenta.setNomApe(nomape.getText().toString());
            cuenta.setFechaNac(fechanac.getText().toString());
            if(cambioImagen){
                cuenta.setDirImagen(url);
            }
            if (cd.actualizarDatos(cuenta)) {
                Intent i;
                if(cuenta.getTipoCuenta().equals("Vendedor")) { i = new Intent(this, EventosVendedor.class); }
                else { i = new Intent(this, EventosComprador.class); }
                i.putExtra("cuenta", cuenta);
                bd.getConexion().close();
                startActivity(i);
            } else {
                Toast.makeText(DetalleCuenta.this, "No se han podido actualizar los datos.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
