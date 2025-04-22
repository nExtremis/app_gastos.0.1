// com.example.misgastos.FinanzasController.java
package com.example.misgastos;

import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.Button;
import android.widget.Spinner;
import android.util.Log;

public class FinanzasController {
    private FirebaseFirestore db;

    public FinanzasController(){
        db = FirebaseFirestore.getInstance();// Inicializar Firestore
    }
    public void configurarBotones(Button btnIngresosMes, Button btnGastosMes, Button btnBalanceMensual, Spinner comboMeses) {
        btnIngresosMes.setOnClickListener(v -> {
            String mes = comboMeses.getSelectedItem().toString();
            Log.d("FinanzasController", "Ver ingresos del mes: " + mes);
        });

        btnGastosMes.setOnClickListener(v -> {
            String mes = comboMeses.getSelectedItem().toString();
            Log.d("FinanzasController", "Ver gastos del mes: " + mes);
        });

        btnBalanceMensual.setOnClickListener(v -> {
            String mes = comboMeses.getSelectedItem().toString();
            Log.d("FinanzasController", "Ver balance mensual: " + mes);
        });
    }
}

