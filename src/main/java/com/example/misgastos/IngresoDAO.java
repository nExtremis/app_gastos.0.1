package com.example.misgastos;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

public class IngresoDAO extends BaseDAO{

    private FirebaseFirestore db;
    private CollectionReference ingresosRef;

    public IngresoDAO(Context context) {
        super(context);
        db = FirebaseFirestore.getInstance();
        ingresosRef = db.collection("ingresos");
    }

    public interface OnIngresoAgregadoListener {
        void onIngresoAgregado(String id);
    }

    public void agregarIngreso(Ingreso ingreso, OnIngresoAgregadoListener listener) {
        ingresosRef.add(ingreso)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId(); // Obtener el documentId generado por Firestore
                        ingreso.setId(id); // Guardar el ID en el objeto Ingreso
                        db.collection("ingresos").document(id).set(ingreso); // Actualizar el documento con el ID
                        listener.onIngresoAgregado(id); // Notificar al listener con el ID generado
                        Log.d("IngresoDAO", "Ingreso agregado con ID: " + id);
                        showToast("Ingreso agregado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("IngresoDAO", "Error al agregar ingreso", e);
                        showToast("Error al agregar el gasto");
                    }
                });
    }

    public void obtenerIngresosPorMes(String mes, OnCompleteListener<QuerySnapshot> listener) {
        ingresosRef.whereEqualTo("mes", mes).get()
                .addOnCompleteListener(listener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("IngresoDAO", "Error al obtener ingresos por mes", e);
                    }
                });
    }

    public void calcularTotalIngresosPorMes(String mes, OnCompleteListener<QuerySnapshot> listener) {
        ingresosRef.whereEqualTo("mes", mes).get()
                .addOnCompleteListener(listener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("IngresoDAO", "Error al calcular total de ingresos por mes", e);
                    }
                });
    }

    public void obtenerIngresosPorFecha(String fecha, OnCompleteListener<QuerySnapshot> listener) {
        ingresosRef.whereEqualTo("fecha", fecha).get()
                .addOnCompleteListener(listener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("IngresoDAO", "Error al obtener ingresos por fecha", e);
                    }
                });
    }

    public void obtenerIngresoPorId(String id, OnCompleteListener<DocumentSnapshot> listener) {
        ingresosRef.document(id).get()
                .addOnCompleteListener(listener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("IngresoDAO", "Error al obtener ingreso por ID", e);
                    }
                });
    }

    public void actualizarIngreso(String id, Ingreso ingreso) {
        ingresosRef.document(id).set(ingreso)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("IngresoDAO", "Ingreso actualizado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("IngresoDAO", "Error al actualizar ingreso", e);
                    }
                });
    }

    public void eliminarIngreso(String id) {
        ingresosRef.document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("IngresoDAO", "Ingreso eliminado correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("IngresoDAO", "Error al eliminar ingreso", e);
                    }
                });
    }
}
