package com.dsi.insibo.sice.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Nota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNota;

    @ManyToOne
    private Alumno alumno;

    @ManyToOne
    private Actividad actividad;
    private float notaObtenida;
    private Date fechaModificacion;    
    
    public Nota() {
    }
    
    public Nota(int idNota, Alumno alumno, Actividad actividad, float notaObtenida, Date fechaModificacion) {
        this.idNota = idNota;
        this.alumno = alumno;
        this.actividad = actividad;
        this.notaObtenida = notaObtenida;
        this.fechaModificacion = fechaModificacion;
    }

    public int getIdNota() {
        return idNota;
    }
    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }
    public Alumno getAlumno() {
        return alumno;
    }
    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
    public Actividad getActividad() {
        return actividad;
    }
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
    public float getNotaObtenida() {
        return notaObtenida;
    }
    public void setNotaObtenida(float notaObtenida) {
        this.notaObtenida = notaObtenida;
    }
    public Date getFechaModificacion() {
        return fechaModificacion;
    }
    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }


}
