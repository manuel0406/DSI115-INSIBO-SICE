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
    private Materia materia;

    private String nombreActividad;
    private float ponderacionActividad;
    private String descripcionActividad;
    private Date fechaActividad;

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
    public Materia getMateria() {
        return materia;
    }
    public void setMateria(Materia materia) {
        this.materia = materia;
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
    public String getDescripcionActividad() {
        return descripcionActividad;
    }
    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }
    public Date getFechaActividad() {
        return fechaActividad;
    }
    public void setFechaActividad(Date fechaActividad) {
        this.fechaActividad = fechaActividad;
    }


}
