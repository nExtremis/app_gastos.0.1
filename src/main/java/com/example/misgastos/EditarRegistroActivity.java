package com.example.misgastos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.misgastos.databinding.ActivityEditarRegistroBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditarRegistroActivity extends BaseActivity {

    private String id;
    private String tipo; // "ingreso" o "gasto"
    private SpinnerView spinnerTipos;
    private EditText txtNroDeCuotas;
    private EditText txtMonto;
    private EditText txtDescripcion;
    private EditText datePicker;
    private FirebaseFirestore db;
    private ActivityEditarRegistroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditarRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Inicializar FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        obtenerDatosIntent();
        setupViews();
        // Cargar los datos actuales del registro
        cargarDatos();

    }

    private void obtenerDatosIntent() {
        id = getIntent().getStringExtra("id");
        tipo = getIntent().getStringExtra("tipo");
        Log.d("EditarRegistro", "ID: " + id + ", Tipo: " + tipo);
        if (id == null || tipo == null) {
            Log.e("EditarRegistroActivity", "Error: El id o tipo es nulo.");
            finish(); // Cierra la actividad si el id o tipo es nulo
            return;
        }
    }

    private void setupViews() {
        binding.spinnerTipos.setupSpinner("Seleccione tipo", R.array.tipos_array_edit, this::handleTipoSelection
        );
        //evento para guardar cambios
        binding.btnGuardar.setOnClickListener(v -> guardarCambios());
        //evento para eliminar el registro
        binding.btnEliminar.setOnClickListener(v -> eliminarRegistro());
    }
    private void handleTipoSelection(String selectedItem){
        binding.txtNroDeCuotas.setEnabled(selectedItem.equals("Cuotas"));
    }
    private void cargarDatos() {
        DocumentReference docRef = db.collection(tipo + "s").document(id);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if (tipo.equals("ingreso")) {
                    cargarIngreso(documentSnapshot.toObject(Ingreso.class));
                } else {
                    cargarGastos(documentSnapshot.toObject(Gasto.class));
                }
            } else {
                Log.e("EditarRegistro", "Documento inexistente");
                finish();
            }
        }).addOnFailureListener(e -> {
            Log.e("EditarRegistro", "Error al cargar", e);
            finish();
        });
    }

    private void cargarIngreso(Ingreso ingreso) {
        binding.txtMonto.setText(String.valueOf(ingreso.getMonto()));
        binding.txtDescripcion.setText(ingreso.getDescripcion());
        binding.datePicker.setText(ingreso.getFecha());
    }

    private void cargarGastos(Gasto gasto) {
        binding.txtMonto.setText(String.valueOf(gasto.getMonto()));
        binding.spinnerTipos.setupSpinner((gasto.getTipo()), R.array.tipos_array_edit, this::handleTipoSelection);
        binding.txtNroDeCuotas.setText(String.valueOf(gasto.getNroCuotas()));
        binding.txtDescripcion.setText(gasto.getDescripcion());
        binding.datePicker.setText(gasto.getFecha());
        binding.txtNroDeCuotas.setEnabled(gasto.getTipo().equals("Cuotas"));
    }

    private void guardarCambios() {


        if (!validarFormulario()) return;
        try {
            if (tipo.equals("ingreso")) {
                agregarIngreso();
            } else {
                agregarGasto();
            }

        } catch (NumberFormatException e) {
            ToastUtils.showShort(this, "Formato númerico inválido");
        }

    }

    private Double obtenerMontoSeguro(Context context, boolean showToast) {
        try {
            String textoMonto = binding.txtMonto.getText().toString().trim();
            if (textoMonto.isEmpty()) {
                if (showToast) {
                    ToastUtils.showShort(context, "El monto no puede estar vacío");
                }
                return null;
            }

            double monto = Double.parseDouble(textoMonto);
            if (monto <= 0) {
                if (showToast) {
                    ToastUtils.showShort(context, "El monto no puede ser negativo o cero");
                }
                return null;
            }
            return monto;
        } catch (NumberFormatException e) {
            if (showToast) ToastUtils.showShort(context, "Formato de monto inválido");
            return null;
        }
    }
    private Ingreso crearIngresoDesdeFormulario(){
        String fechaStr = binding.datePicker.getText().toString();
        String descripcion = binding.txtDescripcion.getText().toString();
        double monto = obtenerMontoSeguro(this, true);
        LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String mesEnEspanol = new Fecha(fecha).obtenerMesEnEspanol();
        return new Ingreso(monto,descripcion,mesEnEspanol,fechaStr);
    }
    private void agregarIngreso() {
        try{
            Ingreso ingreso = crearIngresoDesdeFormulario();
            ingreso.setId(id);
            db.collection("ingresos").document(id).set(ingreso)
                    .addOnSuccessListener(aVoid -> handleExitoGuardado())
                    .addOnFailureListener(e -> handleErrorGuardado(e));
        } catch (Exception e) {
            handleErrorGuardado(e);
        }
    }
    private void agregarGasto() {
        try {
            Gasto gasto = crearGastoDesdeFormulario();
            gasto.setId(id);
            db.collection("gastos").document(id).set(gasto).addOnSuccessListener(aVoid ->
                            handleExitoGuardado())
                    .addOnFailureListener(e -> handleErrorGuardado(e));
        } catch (Exception e) {
            handleErrorGuardado(e);
        }
    }

    private void handleExitoGuardado() {
        Log.d("EditarRegistro", "Gasto actualizado");
        ToastUtils.showShort(this, "Gasto actualizado");
        finish();
    }

    private void handleErrorGuardado(Exception e) {
        Log.e("EditarRegistro", "Error al guardar", e);
        ToastUtils.showError(this, e.getMessage());
    }

    private Gasto crearGastoDesdeFormulario() {
        String fechaStr = binding.datePicker.getText().toString();
        double monto = obtenerMontoSeguro(this, true);
        String tipo = binding.spinnerTipos.getSelectedItem().toString();
        String descripcion = binding.txtDescripcion.getText().toString();
        int nroCuotas = tipo.equals("Cuotas") ? Integer.parseInt(binding.txtNroDeCuotas.getText().toString()) : 0;
        LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String mesEnEspanol = new Fecha(fecha).obtenerMesEnEspanol();
        return new Gasto(
                Double.parseDouble(binding.txtMonto.getText().toString()),
                tipo,
                nroCuotas,
                descripcion,
                mesEnEspanol,
                fecha.toString()
        );
    }
    private boolean validarFormulario() {

        Double monto = obtenerMontoSeguro(this, true);
        String descripcion = binding.txtDescripcion.getText().toString();
        String fechaSeleccionada = binding.datePicker.getText().toString();
        if (fechaSeleccionada == null) {
            ToastUtils.showShort(this, "Seleccione una fecha");
            return false;
        }
        if (monto == null) {

            return false;
        }
        if (descripcion.trim().isEmpty()) {
            ToastUtils.showShort(this, "Ingrese una descripción");
            return false;
        }
        if (tipo.equals("Cuotas") && binding.txtNroDeCuotas.getText().toString().trim().isEmpty()) {
            ToastUtils.showShort(this, "Ingrese número de cuotas");
            return false;
        }
        return true;
    }

    private void eliminarRegistro() {
        DocumentReference docRef;
        if (tipo.equals("ingreso")) {
            docRef = db.collection("ingresos").document(id);
        } else {
            docRef = db.collection("gastos").document(id);
        }

        docRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("EditarRegistroActivity", "Registro eliminado correctamente.");
                    finish(); // Cerrar la actividad después de eliminar
                })
                .addOnFailureListener(e -> Log.e("EditarRegistroActivity", "Error al eliminar registro: ", e));
    }



}