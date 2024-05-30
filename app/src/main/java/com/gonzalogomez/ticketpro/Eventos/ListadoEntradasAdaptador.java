package com.gonzalogomez.ticketpro.Eventos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gonzalogomez.ticketpro.DetalleEventoVendedor;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Entrada;
import com.gonzalogomez.ticketpro.Entidades.Evento;
import com.gonzalogomez.ticketpro.R;

import java.util.List;

public class ListadoEntradasAdaptador extends RecyclerView.Adapter<ListadoEntradasAdaptador.ViewHolder> {
    private List<Entrada> mData;
    private LayoutInflater mInflater;
    private Context context;
    private Cuenta cuenta;
    private int posicion;

    public ListadoEntradasAdaptador(List<Entrada> mData, Context context, Cuenta cuenta) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.cuenta = cuenta;
    }

    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ListadoEntradasAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.elemento_entrada, null);
        return new ListadoEntradasAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListadoEntradasAdaptador.ViewHolder holder, final int position){
        posicion = position+1;
        holder.bindData(mData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView artista, fecha, numentrada, uuid;
        ImageView imagen, qr;

        ViewHolder(View itemView){
            super(itemView);
            artista = itemView.findViewById(R.id.nomEntrada);
            imagen = itemView.findViewById(R.id.imagenEntrada);
            numentrada = itemView.findViewById(R.id.txtNumEntrada);
            uuid = itemView.findViewById(R.id.idEntrada);
            qr = itemView.findViewById(R.id.codigoQR);
            fecha = itemView.findViewById(R.id.fechaEntrada);
        }

        void bindData(final Entrada entrada){
            artista.setText(entrada.getIdEvento().getArtista());
            fecha.setText(entrada.getIdEvento().getFecha());
            numentrada.setText("Entrada "+posicion+" de "+mData.size());
            uuid.setText(entrada.getIdEntrada());
            Glide.with(context).load(entrada.getIdEvento().getDirImagen()).into(imagen);
            Glide.with(context).load(entrada.getFichero()).into(qr);
        }
    }
}
