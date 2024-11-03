package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class NotaPeriodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNotaPeriodo;
    private float notaPeriodo;

    @ManyToOne
    private Asignacion asignacion;

    @ManyToOne
    private Alumno alumno;

    @ManyToOne
    private Periodo periodo;

    public NotaPeriodo() {
    }

    public NotaPeriodo(float notaPeriodo, Asignacion asignacion, Alumno alumno, Periodo periodo) {
        this.notaPeriodo = notaPeriodo;
        this.asignacion = asignacion;
        this.alumno = alumno;
        this.periodo = periodo;
    }

    public NotaPeriodo(int idNotaPeriodo, float notaPeriodo, Asignacion asignacion, Alumno alumno, Periodo periodo) {
        this.idNotaPeriodo = idNotaPeriodo;
        this.notaPeriodo = notaPeriodo;
        this.asignacion = asignacion;
        this.alumno = alumno;
        this.periodo = periodo;
    }

    public int getIdNotaPeriodo() {
        return idNotaPeriodo;
    }

    public void setIdNotaPeriodo(int idNotaPeriodo) {
        this.idNotaPeriodo = idNotaPeriodo;
    }

    public float getNotaPeriodo() {
        return notaPeriodo;
    }

    public void setNotaPeriodo(float notaPeriodo) {
        this.notaPeriodo = notaPeriodo;
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

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

}
