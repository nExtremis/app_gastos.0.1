package com.example.misgastos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.misgastos.databinding.ActivityConsultasBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultasActivity extends AppCompatActivity {


    //Componentes UI
    private SpinnerView spinnerMeses, spinnerFiltros;
    private TextView lblResultados;
    private RecyclerView recyclerViewConsultasGastos;
    private GastoAdapter gastoAdapter;
    private IngresoAdapter ingresoAdapter;

    private ActivityConsultasBinding binding;

    //Datos
    private List<Ingreso> ingresosList = new ArrayList<>();
    private List<Gasto> gastosList = new ArrayList<>();
    private List<Gasto> gastosFiltrados = new ArrayList<>();
    private GastoDAO gastoDAO;
    private IngresoDAO ingresoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        // Inflar el binding
        binding = ActivityConsultasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        setupAdapters();
        setupSpinners();
        setupButtons();

        /*
        comboMeses = findViewById(R.id.comboMeses);
// Crear un ArrayList y agregar "Seleccione un mes" como primer elemento
        ArrayList<String> listaMeses = new ArrayList<>();
        listaMeses.add("Seleccione un mes");
        listaMeses.addAll(Arrays.asList(getResources().getStringArray(R.array.meses_array)));

// Crear el ArrayAdapter a partir del ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, listaMeses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Configurar el adaptador con el Spinner
        comboMeses.setAdapter(adapter);
        comboMeses.setSelection(0, false);

        

        comboFiltros = findViewById(R.id.comboFiltros);
        ArrayList<String> listaFiltros = new ArrayList<>();
        listaFiltros.add("Seleccione un Filtro");
        listaFiltros.addAll(Arrays.asList(getResources().getStringArray(R.array.filtros_array)));
        // Configurar el Spinner con los meses
        ArrayAdapter<String> adapterFiltro = new ArrayAdapter<>(
                this,android.R.layout.simple_spinner_item, listaFiltros);
        adapterFiltro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboFiltros.setAdapter(adapterFiltro);
        comboFiltros.setSelection(0, false);


        // Deshabilitar el Spinner y ocultar el lblResultados al inicio
        

        // Acción para el botón "Ver Ingresos"


        // Acción para el botón "Ver Gastos"
        

        // Acción para el botón "Ver Balance"
        */

    }
    private void initViews(){
        //recyclerViewConsultasGastos = findViewById(R.id.recyclerViewConsultasGastos);
        //lblResultados = findViewById(R.id.lblResultados);
        //spinnerMeses = findViewById(R.id.comboMeses);


        gastoDAO = new GastoDAO(this);
        ingresoDAO = new IngresoDAO(this);
    }
    private void setupAdapters(){

        gastoAdapter = new GastoAdapter(this, gastosFiltrados);
        ingresoAdapter = new IngresoAdapter(this,ingresosList);
        binding.recyclerViewConsultasGastos.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewConsultasGastos.setAdapter(gastoAdapter);
        binding.recyclerViewConsultasIngresos.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewConsultasIngresos.setAdapter(ingresoAdapter);
    }
    private void setupSpinners(){
        binding.comboMeses.setupSpinner("Seleccione un mes", R.array.meses_array,null);

        binding.comboFiltros.setupSpinner("Selecciona un filtro", R.array.tipos_array_consulta,tipoDeGasto -> {
            consultarGastosPorTipo(tipoDeGasto);
        });
        //ESTADO INICIAL DE SPINNERS
        binding.comboFiltros.setVisibility(View.GONE);
        binding.recyclerViewConsultasGastos.setVisibility(View.GONE);
    }
    private void setupButtons(){


        // Acción para el botón "Ver Gastos"
        binding.btnVerGastos.setOnClickListener(v -> cargarGastos());
        binding.btnVerIngresos.setOnClickListener(v -> cargarIngresos());
        binding.btnVerBalance.setOnClickListener(v -> cargarBalance());
    }
    
    private void cargarGastos(){
        String mes = binding.comboMeses.getSelectedItem().toString();
        Log.d("ConsultasActivity", "Mes seleccionado: " + mes);
        if (validarMesSeleccionado(mes)) return;
        gastoDAO.obtenerGastosPorMes(mes, task -> {
            if (task.isSuccessful()) {
                gastosList = task.getResult().toObjects(Gasto.class);
                mostrarResultados(gastosList,formateadorGastos,"Gastos");
            } else {
                mostrarError("Error al cargar gastos", task.getException());
            }
        });
    }
    private void mostrarResultadosGastos() {
        gastosFiltrados.clear();
        gastosFiltrados.addAll(gastosList);
        
        spinnerFiltros.setVisibility(View.VISIBLE);
        recyclerViewConsultasGastos.setVisibility(View.VISIBLE);
        gastoAdapter.notifyDataSetChanged();
        
        lblResultados.setText(gastosList.isEmpty() ? "No hay gastos" : "");
    }
    private void cargarIngresos(){
        String mes = binding.comboMeses.getSelectedItem().toString();
        Log.e("el mes es", mes);
        if (validarMesSeleccionado(mes)) return;
        ingresoDAO.obtenerIngresosPorMes(mes, task ->{
            if(task.isSuccessful()){
                List<Ingreso> ingresos = task.getResult().toObjects(Ingreso.class);
                Log.e("AGREGADO","ingresos cargados");
                mostrarResultados(ingresos,formateadorIngresos,"Ingresos");
            }else{
                mostrarError("Error al cargar ingresos",task.getException());
            }
        });
    }
    private void cargarBalance(){
        String mes = binding.comboMeses.getSelectedItem().toString();
        if(validarMesSeleccionado(mes)) return;
        ingresoDAO.obtenerIngresosPorMes(mes,ingresoTask ->{
            if(ingresoTask.isSuccessful()){
                double totalIngresos = ingresoTask.getResult().toObjects(Ingreso.class)
                    .stream().mapToDouble(Ingreso::getMonto).sum();
                gastoDAO.obtenerGastosPorMes(mes,gastoTask ->{
                    if(gastoTask.isSuccessful()){
                        double totalGastos = gastoTask.getResult().toObjects(Gasto.class)
                            .stream().mapToDouble(Gasto::getMonto).sum();
                        mostrarBalance(totalIngresos,totalGastos);    
                    }else{
                        mostrarError("Error al calcular gastos",gastoTask.getException());
                    }
                    

                });
            }else{
                mostrarError("Error al calcular Ingresos",ingresoTask.getException());
            }   
            
        });

    }

    private void consultarGastosPorTipo(String tipoGasto){
        gastosFiltrados.clear();


        if (tipoGasto.equals("Todos los tipos")) {
            gastosFiltrados.addAll(gastosList);
        } else {

            gastosFiltrados.addAll(
                gastosList.stream()
                    .filter(g -> g.getTipo().equals(tipoGasto))
                    .collect(Collectors.toList())
            );

        }
        actualizarVistaFiltrada();
    }
    private void actualizarVistaFiltrada(){
        gastoAdapter.notifyDataSetChanged();
        binding.lblResultados.setText(
                gastosFiltrados.isEmpty() ? "No hay gastos con este filtro" : ""
        );
    }

    private boolean validarMesSeleccionado(String mes){
        if (mes.equals("Seleccione un mes")) {
            binding.lblResultados.setText("Seleccione  un mes primero");
            return true;
        }
        return false;
    }
    private void mostrarError(String mensaje,Exception e){
        Log.e("Consultas",mensaje,e);
        binding.lblResultados.setText(mensaje);
    }

    private static class BalanceData {
        double totalIngresos;
        double totalGastos;
    }

    // Método para formatear la lista de ingresos
    private String formatIngresos(List<Ingreso> ingresos) {
        StringBuilder sb = new StringBuilder();
        for (Ingreso ingreso : ingresos) {
            sb.append(ingreso.getDescripcion()).append(": $").append(ingreso.getMonto()).append("\n");
        }
        return sb.toString();
    }

    // Método para formatear la lista de gastos
    private String formatGastos(List<Gasto> gastos) {
        StringBuilder sb = new StringBuilder();
        for (Gasto gasto : gastos) {
            sb.append(" - ").append(gasto.getDescripcion()).append(" - $").append(gasto.getMonto()).append(" - ").append(gasto.getFecha()).append(" - ").append("\n");
        }
        return sb.toString();
    }

    private <T> void mostrarResultados(List<T> items,FormateadorTransacciones<T> formateador, String tipo){
        if(items.isEmpty()){
            binding.lblResultados.setText("No hay" + tipo.toLowerCase() + "para " + binding.comboMeses.getSelectedItem().toString());
            return;
        }
        //binding.lblResultados.setText(formateador.formatear(items));
        
        //control de visibilidad
        if(tipo.equals("Gastos")){
            binding.lblResultados.setText("GASTOS\n");
            gastosList = (List<Gasto>) items;
            actualizarReciclerView((List<Gasto>) items);
            binding.comboFiltros.setVisibility(View.VISIBLE);
            binding.recyclerViewConsultasGastos.setVisibility(View.VISIBLE);
        }else if(tipo.equals("Ingresos")){
            binding.lblResultados.setText("INGRESOS\n");
            ingresosList.clear();
            ingresosList.addAll((List<Ingreso>) items); // Asegúrate de declarar `ingresosList` como atributo
            ingresoAdapter.notifyDataSetChanged();
            actualizarReciclerViewIngresos((List<Ingreso>) items); // Necesitarás este método
            binding.comboFiltros.setVisibility(View.GONE);
            binding.recyclerViewConsultasGastos.setVisibility(View.GONE);
            binding.recyclerViewConsultasIngresos.setVisibility(View.VISIBLE); // Asegúrate de tener este RecyclerView en el layout
            Log.d("Consultas", "Mostrando " + ingresosList.size() + " ingresos");
        }    
    }



    private void mostrarBalance(double ingresos, double gastos){
        String texto = String.format(
            "Balance de %s:\nIngresos: $%.2f\nGastos: $%.2f\nTotal: $%.2f",
                binding.comboMeses.getSelectedItem().toString(),
            ingresos,
            gastos,
            (ingresos - gastos)
        );
        binding.lblResultados.setText(texto);
        binding.recyclerViewConsultasGastos.setVisibility(View.GONE);

        
    }
    private interface FormateadorTransacciones<T>{
        String formatear(List<T> items);
    }
    private final FormateadorTransacciones<Ingreso> formateadorIngresos = ingresos -> {
        StringBuilder sb = new StringBuilder();
        for (Ingreso ingreso : ingresos) {
            sb.append(ingreso.getDescripcion())
            .append(": $")
            .append(ingreso.getMonto())
            .append("\n");
        }
        return sb.toString();
    };
    private final FormateadorTransacciones<Gasto> formateadorGastos = gastos -> {
        StringBuilder sb = new StringBuilder();
        for (Gasto gasto : gastos) {
            sb.append(" - ")
            .append(gasto.getDescripcion())
            .append(" - $")
            .append(gasto.getMonto())
            .append(" - ")
            .append(gasto.getFecha())
            .append(" - \n");
        }
        return sb.toString();
    };
    private void actualizarReciclerView(List<Gasto> nuevosGastos){

        if (gastoAdapter == null || nuevosGastos == null) return;
        // 1. Limpiar la lista actual
        gastosFiltrados.clear();
        
        // 2. Agregar los nuevos elementos
        gastosFiltrados.addAll(nuevosGastos);
        
        // 3. Notificar al adaptador que los datos cambiaron
        // Notificación eficiente (evita parpadeos)
        if (gastoAdapter.getItemCount() == 0) {
            gastoAdapter.notifyDataSetChanged();
        } else {
            gastoAdapter.notifyItemRangeChanged(0, nuevosGastos.size());
        }
        
        // 4. Opcional: Scroll al inicio
        binding.recyclerViewConsultasGastos.scrollToPosition(0);
    }
    private void actualizarReciclerViewIngresos(List<Ingreso> nuevosIngresos){

        if (ingresoAdapter == null || nuevosIngresos == null) return;
        // 1. Limpiar la lista actual
        ingresosList.clear();

        // 2. Agregar los nuevos elementos
        ingresosList.addAll(nuevosIngresos);

        // 3. Notificar al adaptador que los datos cambiaron
        // Notificación eficiente (evita parpadeos)
        if (ingresoAdapter.getItemCount() == 0) {
            ingresoAdapter.notifyDataSetChanged();
        } else {
            ingresoAdapter.notifyItemRangeChanged(0, nuevosIngresos.size());
        }

        // 4. Opcional: Scroll al inicio
        binding.recyclerViewConsultasIngresos.scrollToPosition(0);
    }
}
