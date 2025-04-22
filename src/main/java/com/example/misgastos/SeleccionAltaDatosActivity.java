package com.example.misgastos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.misgastos.R;

public class SeleccionAltaDatosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_alta_datos);

        Button btnAgregarIngreso = findViewById(R.id.btnAgregarIngreso);
        Button btnAgregarGasto = findViewById(R.id.btnAgregarGasto);
        Button btnEditarRegistro = findViewById(R.id.btnEditarRegistro);

        // Acción para el botón "Agregar ingreso"
        btnAgregarIngreso.setOnClickListener(v -> {
            Intent intent = new Intent(this, AltaIngresosActivity.class);
            startActivity(intent);
            finish(); // Cierra la actividad actual
        });

        // Acción para el botón "Agregar gasto"
        btnAgregarGasto.setOnClickListener(v -> {
            Intent intent = new Intent(this, AltaGastosActivity.class);
            startActivity(intent);
            finish(); // Cierra la actividad actual
        });

        // Acción para el botón "Editar registro"
        btnEditarRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditViewActivity.class);
            startActivity(intent);
            finish(); // Cierra la actividad actual
        });
    }
}
