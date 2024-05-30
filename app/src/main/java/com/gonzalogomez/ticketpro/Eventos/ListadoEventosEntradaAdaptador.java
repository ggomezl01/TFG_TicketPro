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
import com.gonzalogomez.ticketpro.DetalleEntradas;
import com.gonzalogomez.ticketpro.DetalleEventoComprador;
import com.gonzalogomez.ticketpro.DetalleEventoVendedor;
import com.gonzalogomez.ticketpro.Entidades.Cuenta;
import com.gonzalogomez.ticketpro.Entidades.Entrada;
import com.gonzalogomez.ticketpro.Entidades.Evento;
import com.gonzalogomez.ticketpro.R;

import java.util.List;

public class ListadoEventosEntradaAdaptador extends RecyclerView.Adapter<ListadoEventosEntradaAdaptador.ViewHolder> {
    private List<Evento> mData;
    private LayoutInflater mInflater;
    private Context context;
    private Cuenta cuenta;

    public ListadoEventosEntradaAdaptador(List<Evento> mData, Context context, Cuenta cuenta) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.cuenta = cuenta;
    }

    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ListadoEventosEntradaAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.elemento_evento, null);
        return new ListadoEventosEntradaAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListadoEventosEntradaAdaptador.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(holder.itemView.getContext(), DetalleEntradas.class);
                i.putExtra("evento", mData.get(position));
                i.putExtra("cuenta", cuenta);
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView artista, lugar, fecha;

        ViewHolder(View itemView){
            super(itemView);
            artista = itemView.findViewById(R.id.txtArtista);
            lugar = itemView.findViewById(R.id.txtLugar);
            fecha = itemView.findViewById(R.id.txtFecha);
        }

        void bindData(final Evento evento){
            artista.setText(evento.getArtista());
            lugar.setText(evento.getLugar());
            fecha.setText(evento.getFecha());
        }
    }
}
