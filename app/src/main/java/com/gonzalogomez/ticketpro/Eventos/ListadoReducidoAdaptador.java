package com.gonzalogomez.ticketpro.Eventos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gonzalogomez.ticketpro.Entidades.Cesta;
import com.gonzalogomez.ticketpro.Entidades.Evento;
import com.gonzalogomez.ticketpro.R;

import java.text.DecimalFormat;
import java.util.List;

public class ListadoReducidoAdaptador extends RecyclerView.Adapter<ListadoReducidoAdaptador.ViewHolder> {
    private List<Cesta> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListadoReducidoAdaptador(List<Cesta> mData, Context context) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ListadoReducidoAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.elemento_evento_reducido, null);
        return new ListadoReducidoAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListadoReducidoAdaptador.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha, precio;
        ImageView imagen;

        ViewHolder(View itemView){
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNomEvento);
            fecha = itemView.findViewById(R.id.txtFechaEvento);
            precio = itemView.findViewById(R.id.txtEntradasTotal);
            imagen = itemView.findViewById(R.id.imagenEventoReduc);
        }

        void bindData(final Cesta cesta){
            nombre.setText(cesta.getEvento().getArtista());
            fecha.setText(cesta.getEvento().getFecha());
            DecimalFormat df = new DecimalFormat("#.00");
            double precioElemento = cesta.getNumEntradas()*cesta.getEvento().getPrecioEntrada();
            precio.setText(cesta.getNumEntradas() + " - " + df.format(precioElemento) +"€");
            Glide.with(context).load(cesta.getEvento().getDirImagen()).into(imagen);
        }
    }
}
