package com.example.misgastos;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.misgastos.R;
import com.google.firebase.FirebaseApp;

public class  BaseActivity extends AppCompatActivity {

    protected ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new ConstraintLayout(this);
        FirebaseApp.initializeApp(this);
        layout.setId(View.generateViewId());

        setContentView(layout);
    }
}


