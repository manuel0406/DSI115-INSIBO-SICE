package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Hora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHora;
    private String horaInicio;
    private String horaFinalizacion;

    public Hora() {
    }

    public Hora(int idHora, String horaInicio, String horaFinalizacion) {
        this.idHora = idHora;
        this.horaInicio = horaInicio;
        this.horaFinalizacion = horaFinalizacion;
    }

    public int getIdHora() {
        return idHora;
    }

    public void setIdHora(int idHora) {
        this.idHora = idHora;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFinalizacion() {
        return horaFinalizacion;
    }

    public void setHoraFinalizacion(String horaFinalizacion) {
        this.horaFinalizacion = horaFinalizacion;
    }

}
