package com.example.misgastos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDate;
import java.util.Calendar;

public class AltaIngresosActivity extends AppCompatActivity {

    private Button btnFecha;
    private TextView txtFechaSeleccionada;
    private Calendar calendar;
    private LocalDate fechaSeleccionada;
    private String mesSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_ingresos);

        EditText txtMonto = findViewById(R.id.txtMonto);
        EditText txtDescripcion = findViewById(R.id.txtDescripcion);
        btnFecha = findViewById(R.id.btnFecha);
        txtFechaSeleccionada = findViewById(R.id.txtFechaSeleccionada);
        Button btnAgregarIngreso = findViewById(R.id.btnAgregarIngreso);

        calendar = Calendar.getInstance();

        btnFecha.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AltaIngresosActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        fechaSeleccionada = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                        Fecha fecha = new Fecha(fechaSeleccionada);
                        mesSeleccionado = fecha.obtenerMesEnEspanol();
                        btnFecha.setText(fecha.formatearFecha());
                        txtFechaSeleccionada.setText("Fecha seleccionada: " + fecha.formatearFecha());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        btnAgregarIngreso.setOnClickListener(v -> {
            double monto = Double.parseDouble(txtMonto.getText().toString());
            String descripcion = txtDescripcion.getText().toString();

            if (fechaSeleccionada != null && mesSeleccionado != null) {
                Ingreso ingreso = new Ingreso(monto, descripcion, mesSeleccionado, fechaSeleccionada.toString());
                IngresoDAO ingresoDAO = new IngresoDAO(this);
                ingresoDAO.agregarIngreso(ingreso, new IngresoDAO.OnIngresoAgregadoListener() {
                    @Override
                    public void onIngresoAgregado(String id) {
                        Log.d("AltaIngresosActivity", "Ingreso agregado con ID: " + id);
                        ingreso.setId(id); // Guardar el ID en el objeto Ingreso
                        // Aquí puedes realizar cualquier acción adicional con el ID, si es necesario
                    }
                });
            } else {
                Log.e("AltaIngresosActivity", "Error: No se ha seleccionado una fecha.");
            }
        });
    }
}
