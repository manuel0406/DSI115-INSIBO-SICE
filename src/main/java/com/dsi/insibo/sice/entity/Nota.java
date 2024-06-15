package com.dsi.insibo.sice.entity;

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


}
