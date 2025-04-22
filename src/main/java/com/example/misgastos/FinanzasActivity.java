// com.example.misgastos.FinanzasActivity.java
package com.example.misgastos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.misgastos.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class FinanzasActivity extends BaseActivity {

    private FinanzasController controlador;
    private GastoDAO gastoDAO;

    private IngresoDAO ingresoDAO;
    private FirebaseFirestore db; // Firestore
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finanzas_view);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();
        Log.d("FinanzasActivity", "Firestore inicializado.");

        controlador = new FinanzasController(); // Inicializar tu controlador aquí
        gastoDAO = new GastoDAO(this);
        ingresoDAO = new IngresoDAO(this);



        Button btnConsultas = findViewById(R.id.btnConsultas);
        Button btnAltaDatos = findViewById(R.id.btnAltaDatos);
        Button btnSignOut = findViewById(R.id.btnSignOut);

        // Acción para el botón "Consultas"
        btnConsultas.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConsultasActivity.class);
            startActivity(intent);
        });

        // Acción para el botón "Alta de Datos"
        btnAltaDatos.setOnClickListener(v -> {
            Intent intent = new Intent(this, SeleccionAltaDatosActivity.class);
            startActivity(intent);
        });
        // Acción para el botón "Sign Out"
            btnSignOut.setOnClickListener(j -> signOut());

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("FinanzasActivity", "Sign out successful");
                startActivity(new Intent(FinanzasActivity.this, LoginActivity.class));
                finish();
            } else {
                Log.e("FinanzasActivity", "Sign out failed", task.getException());
                Toast.makeText(FinanzasActivity.this, "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
