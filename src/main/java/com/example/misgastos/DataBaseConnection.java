package com.example.misgastos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseConnection extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "misgastos.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DataBaseConnection", "Creando la base de datos...");
        // Crear las tablas si no existen
        String CREATE_TABLE_INGRESOS = "CREATE TABLE ingresos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "monto REAL," +
                "descripcion TEXT," +
                "mes TEXT," +
                "fecha TEXT)";
        db.execSQL(CREATE_TABLE_INGRESOS);
        Log.d("DataBaseConnection", "Tabla ingresos creada.");

        String CREATE_TABLE_GASTOS = "CREATE TABLE gastos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "monto REAL," +
                "descripcion TEXT," +
                "mes TEXT," +
                "fecha TEXT)";
        db.execSQL(CREATE_TABLE_GASTOS);
        Log.d("DataBaseConnection", "Tabla gastos creada.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DataBaseConnection", "Actualizando la base de datos de versión " + oldVersion + " a " + newVersion);
                // Si la base de datos se actualiza, eliminar las tablas antiguas y crear nuevas
        db.execSQL("DROP TABLE IF EXISTS ingresos");
        db.execSQL("DROP TABLE IF EXISTS gastos");
        onCreate(db);
    }

    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }
}
