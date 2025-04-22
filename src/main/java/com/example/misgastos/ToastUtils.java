package com.example.misgastos;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showShort(Context context, String mensaje){
        if (context != null && mensaje != null){
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showError(Context context, String error){
        if (context != null){
            String mensajeError = "Error: " + (error !=null ? error : "Ocurrio un error");
            Toast.makeText(context, mensajeError, Toast.LENGTH_SHORT).show();
        }
    }
    public static void showFormatted(Context context, String format, Object... args) {
        if (context != null) {
            showShort(context, String.format(format, args));
        }
    }

}
