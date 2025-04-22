package com.example.misgastos;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class GastoDAO extends BaseDAO{



    private FirebaseFirestore db;
    private CollectionReference gastosRef;

    public GastoDAO(Context context) {
        super(context);
        db = FirebaseFirestore.getInstance();
        gastosRef = db.collection("gastos");
    }

    public interface OnGastoAgregadoListener {
        void onGastoAgregado(String id);
    }

    public void agregarGasto(Gasto gasto, OnGastoAgregadoListener listener) {
        gastosRef.add(gasto)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId(); // Obtener el documentId generado por Firestore
                        gasto.setId(id); // Guardar el ID en el objeto Gasto
                        db.collection("gastos").document(id).set(gasto); // Actualizar el documento con el ID
                        listener.onGastoAgregado(id); // Notificar al listener con el ID generado
                        Log.d("GastoDAO", "Gasto agregado con ID: " + id);
                        showToast("Gasto agregado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GastoDAO", "Error al agregar gasto", e);
                        showToast("Error al agregar el gasto");
                    }
                });
    }

    public void obtenerGastosPorMes(String mes, OnCompleteListener<QuerySnapshot> listener) {
        gastosRef.whereEqualTo("mes", mes)
                .orderBy("fecha", Query.Direction.ASCENDING) // Ordenar por fecha ascendente
                .get()
                .addOnCompleteListener(listener)
                .addOnFailureListener(e -> {
                    Log.e("GastoDAO", "Error al obtener gastos", e);
                    listener.onComplete(null); // Asegura que el callback siempre se ejecute
                });
    }

    public void calcularTotalGastosPorMes(String mes, OnCompleteListener<QuerySnapshot> listener) {
        gastosRef.whereEqualTo("mes", mes).get()
                .addOnCompleteListener(listener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GastoDAO", "Error al calcular total de gastos por mes", e);
                    }
                });
    }

    public void obtenerGastosPorFecha(String fecha, OnCompleteListener<QuerySnapshot> listener) {
        gastosRef.whereEqualTo("fecha", fecha).get()
                .addOnCompleteListener(listener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GastoDAO", "Error al obtener gastos por fecha", e);
                    }
                });
    }

    public void obtenerGastoPorId(String id, OnCompleteListener<DocumentSnapshot> listener) {
        gastosRef.document(id).get()
                .addOnCompleteListener(listener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GastoDAO", "Error al obtener gasto por ID", e);
                    }
                });
    }

    public void actualizarGasto(String id, Gasto gasto) {
        gastosRef.document(id).set(gasto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("GastoDAO", "Gasto actualizado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GastoDAO", "Error al actualizar gasto", e);
                    }
                });
    }

    public void eliminarGasto(String id) {
        gastosRef.document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("GastoDAO", "Gasto eliminado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GastoDAO", "Error al eliminar gasto", e);
                    }
                });
    }
}
