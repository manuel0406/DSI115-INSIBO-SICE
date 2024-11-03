package com.dsi.insibo.sice.Calificaciones;

public class ActividadDTO {

    private String nombreActividad;
    private int cantidadActividades;

    public ActividadDTO() {
    }

    public ActividadDTO(String nombreActividad, int cantidadActividades) {
        this.nombreActividad = nombreActividad;
        this.cantidadActividades = cantidadActividades;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public int getCantidadActividades() {
        return cantidadActividades;
    }

    public void setCantidadActividades(int cantidadActividades) {
        this.cantidadActividades = cantidadActividades;
    }

}
