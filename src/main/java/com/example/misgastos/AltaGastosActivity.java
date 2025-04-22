package com.example.misgastos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.misgastos.databinding.ActivityAltaGastosBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AltaGastosActivity extends AppCompatActivity {

    private Button btnFecha;
    private TextView txtFechaSeleccionada;
    private Calendar calendar;
    private LocalDate fechaSeleccionada;
    private String mesSeleccionado;
    private ActivityAltaGastosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_alta_gastos);

        binding = ActivityAltaGastosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        setupSpinners();
        setupDatePicker();
        setupButtonListeners();

        /*
        btnAgregarGasto.setOnClickListener(v -> {
            try {
                String tipo = spinnerTipo.getSelectedItem().toString();
                int nroCuotas = 0;
                if (tipo.equals("Cuotas")) {
                    try {
                        nroCuotas = Integer.parseInt(txtNroDeCuotas.getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Número de cuotas no válido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                double monto = Double.parseDouble(txtMonto.getText().toString());

                String descripcion = txtDescripcion.getText().toString();

                if (fechaSeleccionada != null && mesSeleccionado != null) {
                    Gasto gasto = new Gasto(monto, tipo, nroCuotas, descripcion, mesSeleccionado, fechaSeleccionada.toString());
                    GastoDAO gastoDAO = new GastoDAO(this);
                    gastoDAO.agregarGasto(gasto, new GastoDAO.OnGastoAgregadoListener() {
                        @Override
                        public void onGastoAgregado(String id) {
                            Log.d("AltaGastosActivity", "Gasto agregado con ID: " + id);
                            gasto.setId(id); // Guardar el ID en el objeto Gasto si lo deseas
                            // Aquí puedes realizar cualquier acción adicional con el ID, si es necesario
                        }
                    });
                    Log.d("AltaGastosActivity", "Gasto agregado: " + gasto.toString());
                } else {
                    Log.e("AltaGastosActivity", "Error: No se ha seleccionado una fecha.");
                }
            }catch (NumberFormatException e) {
                Toast.makeText(this, "Monto no válido", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("AltaGastosActivity", "Error al agregar gasto", e);
                Toast.makeText(this, "Error al agregar gasto", Toast.LENGTH_SHORT).show();
            }
            });

         */
    }
    private void setupSpinners(){
        binding.spinnerTipos.setupSpinner("Seleccione un Tipo",R.array.tipos_array_edit, this::handleTipoSelection);

        
    }
    private void handleTipoSelection(String selectedItem){
        binding.txtNroDeCuotas.setEnabled(selectedItem.equals("Cuotas"));
    }

    private void initViews(){
        calendar = Calendar.getInstance();
    }
    private void setupDatePicker() {
        binding.btnFecha.setOnClickListener(v -> showDatePickerDialog());
    }
    //metodos de setupDatePicker
    private void showDatePickerDialog() {
        new DatePickerDialog(
            this,
            (view, year, month, day) -> handleDateSelection(year, month, day),
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
    private void handleDateSelection(int year, int month, int day) {
        calendar.set(year, month, day);
        fechaSeleccionada = LocalDate.of(year, month + 1, day);
        Fecha fecha = new Fecha(fechaSeleccionada);
        mesSeleccionado = fecha.obtenerMesEnEspanol();
        updateDateViews(fecha);
    }
    private void updateDateViews(Fecha fecha) {
        binding.btnFecha.setText(fecha.formatearFecha());
        binding.txtFechaSeleccionada.setText("Fecha seleccionada: " + fecha.formatearFecha());
    }
    private Gasto crearGastoDesdeFormulario(){
        String tipo = binding.spinnerTipos.getSelectedItem().toString();
        int nroCuotas  = tipo.equals("Cuotas") ? Integer.parseInt(binding.txtNroDeCuotas.getText().toString()) : 0;

        return new Gasto(
                Double.parseDouble(binding.txtMonto.getText().toString()),
                tipo,
                nroCuotas,
                binding.txtDescripcion.getText().toString(),
                mesSeleccionado,
                fechaSeleccionada.toString()
        );
    }
    private void setupButtonListeners(){
        binding.btnAgregarGasto.setOnClickListener(v -> {

            if (validarFormulario()) {
                agregarGasto();
            }
        });

    }
    private void agregarGasto(){
        try {
            Gasto gasto = crearGastoDesdeFormulario();

            new GastoDAO(this).agregarGasto(gasto, id -> {
                // Guardar el ID en el objeto Gasto si lo deseass
                gasto.setId(id);
                Log.d("AltaGastosActivity", "Gasto agregado con ID: " + id);
                showToast("Gasto registrado exitosamente");

                limpiarFormulario();

                // Aquí puedes realizar cualquier acción adicional con el ID, si es necesario
            });
        }catch(NumberFormatException e){
            showToast("Error en el formato de monto o cuotas");
        }
    }

    private void showToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
    private boolean validarFormulario(){
        if (binding.spinnerTipos.getSelectedItemPosition() == 0) {
            showToast("Seleccione un tipo de gasto");
            return false;
        }
        
        if (fechaSeleccionada == null) {
            showToast("Seleccione una fecha");
            return false;
        }
        
        if (binding.txtMonto.getText().toString().trim().isEmpty()) {
            showToast("Ingrese un monto");
            return false;
        }
        String tipo = binding.spinnerTipos.getSelectedItem().toString();

        if(tipo.equals("Cuotas") && binding.txtNroDeCuotas.getText().toString().trim().isEmpty()){
           
            showToast("Ingrese el número de cuotas");
            return false;
        }
        return true;
    }
    private void limpiarFormulario() {
        binding.spinnerTipos.setSelection(0);
        binding.txtNroDeCuotas.setText("");
        binding.txtMonto.setText("");
        binding.txtDescripcion.setText("");
        binding.btnFecha.setText("Seleccionar fecha");
        binding.txtFechaSeleccionada.setText("");
        fechaSeleccionada = null;
        mesSeleccionado = null;
    }
}
