package com.example.misgastos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {


    private List<Gasto> gastos;
    private Context context;

    public void actualizarLista(List<Gasto> nuevaLista) {
        this.gastos = nuevaLista;
        notifyDataSetChanged();
    }


    public GastoAdapter(Context context, List<Gasto> gastos) {
        this.context = context;
        this.gastos = gastos;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = gastos.get(position);
        holder.montoTextView.setText(String.valueOf(gasto.getMonto()));
        holder.tipoTextView.setText(String.valueOf(gasto.getTipo()));
        if(gasto.getNroCuotas() !=0){
            holder.nroCuotasTextView.setVisibility(View.VISIBLE);
            holder.nroCuotasTextView.setText(String.valueOf(gasto.getNroCuotas()));

        }else{
            holder.nroCuotasTextView.setVisibility(View.GONE);
        }
        holder.descripcionTextView.setText(gasto.getDescripcion());
        holder.fechaTextView.setText(gasto.getFecha().toString());
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    public static class GastoViewHolder extends RecyclerView.ViewHolder {


        TextView montoTextView;
        TextView tipoTextView;
        TextView nroCuotasTextView;
        TextView descripcionTextView;
        TextView fechaTextView;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            montoTextView = itemView.findViewById(R.id.tvMonto);
            tipoTextView = itemView.findViewById(R.id.tvTipo);
            nroCuotasTextView = itemView.findViewById(R.id.tvNroCuotas);
            descripcionTextView = itemView.findViewById(R.id.tvDescripcion);
            fechaTextView = itemView.findViewById(R.id.tvFecha);
        }
    }
}

