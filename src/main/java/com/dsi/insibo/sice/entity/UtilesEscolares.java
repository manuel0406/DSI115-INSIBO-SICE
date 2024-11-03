package com.dsi.insibo.sice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class UtilesEscolares {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUtiles;
    @ManyToOne
    private Alumno alumno;
    private boolean entregado;
    private Date fechaEntregaUtiles;

    public UtilesEscolares() {
    }

    public UtilesEscolares(int idUtiles, Alumno alumno, boolean entregado, Date fechaEntregaUtiles) {
        this.idUtiles = idUtiles;
        this.alumno = alumno;
        this.entregado = entregado;
        this.fechaEntregaUtiles = fechaEntregaUtiles;
    }

    public int getIdUtiles() {
        return idUtiles;
    }

    public void setIdUtiles(int idUtiles) {
        this.idUtiles = idUtiles;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public boolean isEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    public Date getFechaEntregaUtiles() {
        return fechaEntregaUtiles;
    }

    public void setFechaEntregaUtiles(Date fechaEntregaUtiles) {
        this.fechaEntregaUtiles = fechaEntregaUtiles;
    }

}
