package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class NotaMateria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNotaMateria;
    private float notaMateria;

    @ManyToOne
    private Asignacion asignacion;
    @ManyToOne
    private Alumno alumno;

    public NotaMateria() {
    }

    public NotaMateria(float notaMateria, Asignacion asignacion, Alumno alumno) {
        this.notaMateria = notaMateria;
        this.asignacion = asignacion;
        this.alumno = alumno;
    }

    public NotaMateria(int idNotaMateria, float notaMateria, Asignacion asignacion, Alumno alumno) {
        this.idNotaMateria = idNotaMateria;
        this.notaMateria = notaMateria;
        this.asignacion = asignacion;
        this.alumno = alumno;
    }

    public int getIdNotaMateria() {
        return idNotaMateria;
    }

    public void setIdNotaMateria(int idNotaMateria) {
        this.idNotaMateria = idNotaMateria;
    }

    public float getNotaMateria() {
        return notaMateria;
    }

    public void setNotaMateria(float notaMateria) {
        this.notaMateria = notaMateria;
    }

    public Asignacion getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(Asignacion asignacion) {
        this.asignacion = asignacion;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

}
