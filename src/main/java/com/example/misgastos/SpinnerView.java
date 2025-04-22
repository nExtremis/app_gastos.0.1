package com.example.misgastos;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.Arrays;

public class SpinnerView extends AppCompatSpinner {
    public interface OnSpinnerItemSelectedListener {
        void onItemSelected(String selectedItem);
    }

    private ArrayList<String> items;
    private OnSpinnerItemSelectedListener itemSelectedListener;
    private String stringInit;
    private Spinner nombreSpinner;

    // Constructores
    public SpinnerView(Context context) {
        super(context);
        init(null);
    }
    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Configuración inicial del Spinner
        setBackgroundResource(R.drawable.spinner_background); // Estilo personalizado

        // Inicializar la lista de elementos
        items = new ArrayList<>();

        super.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                String selectedItem = items.get(position);
                if (itemSelectedListener != null) {
                    itemSelectedListener.onItemSelected(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
                System.out.println("Elemento seleccionado: ");
            }
        });
    }



    public void setupSpinner(String initialText, int arrayResId, OnSpinnerItemSelectedListener listener){
        this.stringInit = initialText;
        this.itemSelectedListener = listener;
        // Crear un ArrayList y agregar  un string como primer elemento
        items.clear();
        items.add(stringInit);
        items.addAll(Arrays.asList(getResources().getStringArray(arrayResId)));
        // Crear el ArrayAdapter a partir del ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Configurar el adaptador con el Spinner
        setAdapter(adapter);
        setSelection(0, false);
    }


}
