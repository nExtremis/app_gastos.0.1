package com.example.misgastos;


import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Fecha {
    private LocalDate fecha;

    public Fecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public static String obtenerMesComoString(LocalDate fecha) {
        Month mesEnum = fecha.getMonth(); // Obtiene el mes como un objeto Month
        return mesEnum.toString(); // Convierte el objeto Month en un String
    }

    public String obtenerMesEnEspanol() {
        int mesNumero = fecha.getMonthValue();
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return nombresMeses[mesNumero - 1];
    }

    public String formatearFecha() {
        return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}

