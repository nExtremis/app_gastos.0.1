package com.example.misgastos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IngresoAdapter extends RecyclerView.Adapter<IngresoAdapter.IngresoViewHolder> {


    private List<Ingreso> ingresos;
    private Context context;

    public void actualizarLista(List<Ingreso> nuevaLista) {
        this.ingresos = nuevaLista;
        notifyDataSetChanged();
    }


    public IngresoAdapter(Context context, List<Ingreso> ingresos) {
        this.context = context;
        this.ingresos = ingresos != null ? ingresos : new ArrayList<>();
    }

    @NonNull
    @Override
    public IngresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingreso, parent, false);
        return new IngresoAdapter.IngresoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngresoAdapter.IngresoViewHolder holder, int position) {
        Ingreso ingreso = ingresos.get(position);
        holder.montoTextView.setText(String.valueOf(ingreso.getMonto()));
        holder.descripcionTextView.setText(ingreso.getDescripcion());
        holder.fechaTextView.setText(ingreso.getFecha().toString());
    }

    @Override
    public int getItemCount() {
        return ingresos.size();
    }

    public static class IngresoViewHolder extends RecyclerView.ViewHolder {


        TextView montoTextView;
        TextView descripcionTextView;
        TextView fechaTextView;

        public IngresoViewHolder(@NonNull View itemView) {
            super(itemView);
            montoTextView = itemView.findViewById(R.id.tvMonto);
            descripcionTextView = itemView.findViewById(R.id.tvDescripcion);
            fechaTextView = itemView.findViewById(R.id.tvFecha);
        }
    }
}

