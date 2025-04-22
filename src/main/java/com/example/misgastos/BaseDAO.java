package com.example.misgastos;

import android.content.Context;
import android.widget.Toast;

public class BaseDAO {
    protected Context context;

    public BaseDAO(Context context) {
        this.context = context;
    }

    protected void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

