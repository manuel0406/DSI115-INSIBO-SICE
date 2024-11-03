package com.dsi.insibo.sice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idActividad;
    @ManyToOne
    private Periodo periodo;
    @ManyToOne
    private Asignacion asignacion;
    private String nombreActividad;
    private float ponderacionActividad;
    private String tipoActividad;
    private Date fechaActividad;

    public Actividad() {
    }

    public Actividad(int idActividad, Periodo periodo, Asignacion asignacion, String nombreActividad,
            float ponderacionActividad, String descripcionActividad, Date fechaActividad) {
        this.idActividad = idActividad;
        this.periodo = periodo;
        this.asignacion = asignacion;
        this.nombreActividad = nombreActividad;
        this.ponderacionActividad = ponderacionActividad;
        this.tipoActividad = descripcionActividad;
        this.fechaActividad = fechaActividad;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Asignacion getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(Asignacion asignacion) {
        this.asignacion = asignacion;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public float getPonderacionActividad() {
        return ponderacionActividad;
    }

    public void setPonderacionActividad(float ponderacionActividad) {
        this.ponderacionActividad = ponderacionActividad;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String descripcionActividad) {
        this.tipoActividad = descripcionActividad;
    }

    public Date getFechaActividad() {
        return fechaActividad;
    }

    public void setFechaActividad(Date fechaActividad) {
        this.fechaActividad = fechaActividad;
    }

}
