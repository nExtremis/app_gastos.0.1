package com.example.misgastos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditViewActivity extends BaseActivity {

    private EditText datePicker;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> registros;
    private Calendar calendar;
    private LocalDate fechaSeleccionada;
    private Button btnFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);

        datePicker = findViewById(R.id.datePicker);
        btnFecha = findViewById(R.id.btnFecha);
        Button btnMostrarRegistros = findViewById(R.id.btnMostrarRegistros);
        listView = findViewById(R.id.listView);
        registros = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, registros);
        listView.setAdapter(adapter);

        calendar = Calendar.getInstance();

        // Configurar el DatePickerDialog para seleccionar la fecha
        btnFecha.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditViewActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        fechaSeleccionada = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                        datePicker.setText(fechaSeleccionada.toString());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Configurar evento para el botón "Mostrar Registros"
        btnMostrarRegistros.setOnClickListener(v -> mostrarRegistros());

        // Configurar evento para la selección de un registro
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String newValue = registros.get(position);
            if (newValue != null) {
                String[] partes = newValue.split(" - ");
                String tipo = partes[0].split(": ")[0]; // "Ingreso" o "Gasto"
                String descripcion = partes[1];

                // Buscar el registro en la lista correspondiente
                if (fechaSeleccionada != null) {
                    String fechaStr = fechaSeleccionada.toString();
                    if (tipo.equals("Ingreso")) {
                        IngresoDAO ingresoDAO = new IngresoDAO(this);
                        ingresoDAO.obtenerIngresosPorFecha(fechaStr, task -> {
                            if (task.isSuccessful()) {
                                List<Ingreso> ingresos = task.getResult().toObjects(Ingreso.class);
                                for (Ingreso ingreso : ingresos) {
                                    if (ingreso.getDescripcion().equals(descripcion)) {
                                        abrirVentanaEdicion(ingreso.getId(), "ingreso");
                                        break;
                                    }
                                }
                            } else {
                                Log.e("EditViewActivity", "Error al obtener ingresos por fecha", task.getException());
                            }
                        });
                    } else if (tipo.equals("Gasto")) {
                        GastoDAO gastoDAO = new GastoDAO(this);
                        gastoDAO.obtenerGastosPorFecha(fechaStr, task -> {
                            if (task.isSuccessful()) {
                                List<Gasto> gastos = task.getResult().toObjects(Gasto.class);
                                for (Gasto gasto : gastos) {
                                    if (gasto.getDescripcion().equals(descripcion)) {
                                        abrirVentanaEdicion(gasto.getId(), "gasto");
                                        break;
                                    }
                                }
                            } else {
                                Log.e("EditViewActivity", "Error al obtener gastos por fecha", task.getException());
                            }
                        });
                    }
                }
            }
        });
    }

    private void mostrarRegistros() {
        if (fechaSeleccionada != null) {
            String fechaStr = fechaSeleccionada.toString();
            registros.clear(); // Limpiar la lista antes de agregar nuevos registros

            // Obtener ingresos de la fecha seleccionada
            IngresoDAO ingresoDAO = new IngresoDAO(this);
            ingresoDAO.obtenerIngresosPorFecha(fechaStr, task -> {
                if (task.isSuccessful()) {
                    List<Ingreso> ingresos = task.getResult().toObjects(Ingreso.class);
                    for (Ingreso ingreso : ingresos) {
                        registros.add("Ingreso: $" + ingreso.getMonto() + " - " + ingreso.getDescripcion());
                    }

                    // Obtener gastos de la fecha seleccionada
                    GastoDAO gastoDAO = new GastoDAO(this);
                    gastoDAO.obtenerGastosPorFecha(fechaStr, gastoTask -> {
                        if (gastoTask.isSuccessful()) {
                            List<Gasto> gastos = gastoTask.getResult().toObjects(Gasto.class);
                            for (Gasto gasto : gastos) {
                                registros.add("Gasto: $" + gasto.getMonto() + " - " + gasto.getDescripcion());
                            }
                            adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                        } else {
                            Log.e("EditViewActivity", "Error al obtener gastos por fecha", gastoTask.getException());
                        }
                    });
                } else {
                    Log.e("EditViewActivity", "Error al obtener ingresos por fecha", task.getException());
                }
            });
        } else {
            registros.add("Por favor, seleccione una fecha.");
            adapter.notifyDataSetChanged();
        }
    }

    private void abrirVentanaEdicion(String id, String tipo) {
        if (id != null && tipo != null) {
            Log.d("EditViewActivity", "Editando registro con ID: " + id + " y tipo: " + tipo);
            Intent intent = new Intent(this, EditarRegistroActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("tipo", tipo);
            startActivity(intent);
        } else {
            Log.e("EditViewActivity", "Error: El id o tipo es nulo.");
        }
    }


}
