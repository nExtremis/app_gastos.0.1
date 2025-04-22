package com.example.misgastos;

public class Gasto {
    private String id;
    private double monto;
    private String tipo;
    private int nroCuotas;
    private String descripcion;
    private String mes;
    private String fecha;

    // Constructor
    // Constructor sin argumentos requerido por Firestore
    public Gasto() {
        this.nroCuotas = 0;
    }
    public Gasto(double monto,String tipo,int nroCuotas, String descripcion, String mes, String fecha) {
        this.monto = monto;
        this.tipo = tipo;
        this.nroCuotas = nroCuotas;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNroCuotas() {
        return nroCuotas;
    }

    public void setNroCuotas(int nroCuotas) {
        this.nroCuotas = nroCuotas;
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
        return "Gasto{" +
                "id=" + id +
                ", monto=" + monto +
                ", tipo=" + tipo +
                ", cantidad de cuotas=" + nroCuotas +
                ", descripcion='" + descripcion + '\'' +
                ", mes='" + mes + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}

