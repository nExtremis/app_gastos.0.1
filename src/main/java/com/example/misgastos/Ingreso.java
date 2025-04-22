package com.example.misgastos;

public class Ingreso {
    private String id;
    private double monto;
    private String descripcion;
    private String mes;
    private String fecha;

    // Constructor
    // Constructor sin argumentos requerido por Firestore
    public Ingreso() {
    }
    public Ingreso(double monto, String descripcion, String mes, String fecha) {
        this.monto = monto;
        this.descripcion = descripcion;
        this.mes = mes;
        this.fecha = fecha;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Ingreso{" +
                "id=" + id +
                ", monto=" + monto +
                ", descripcion='" + descripcion + '\'' +
                ", mes='" + mes + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
