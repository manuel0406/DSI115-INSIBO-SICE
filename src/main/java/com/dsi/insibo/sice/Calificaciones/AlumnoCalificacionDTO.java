package com.dsi.insibo.sice.Calificaciones;

import java.util.ArrayList;
import java.util.List;

public class AlumnoCalificacionDTO {

    private String nombre;
    private String apellido;
    private List<Float> calificaciones;
    private List<Float> ponderaciones;

    // Constructor vacío
    public AlumnoCalificacionDTO() {
        this.calificaciones = new ArrayList<>();
        this.ponderaciones = new ArrayList<>();
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<Float> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<Float> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public List<Float> getPonderaciones() {
        return ponderaciones;
    }

    public void setPonderaciones(List<Float> ponderaciones) {
        this.ponderaciones = ponderaciones;
    }
}