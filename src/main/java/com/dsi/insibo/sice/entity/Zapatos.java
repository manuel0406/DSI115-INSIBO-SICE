package com.dsi.insibo.sice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Zapatos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idZapatos;
    @ManyToOne
    private Alumno alumno;
    private int tallaZapato;
    private boolean zapataloEntregado;
    private Date fechaEntegaZapatos;

    public Zapatos() {
    }

    public Zapatos(int idZapatos, Alumno alumno, int tallaZapato, boolean zapataloEntregado, Date fechaEntegaZapatos) {
        this.idZapatos = idZapatos;
        this.alumno = alumno;
        this.tallaZapato = tallaZapato;
        this.zapataloEntregado = zapataloEntregado;
        this.fechaEntegaZapatos = fechaEntegaZapatos;
    }

    public int getIdZapatos() {
        return idZapatos;
    }

    public void setIdZapatos(int idZapatos) {
        this.idZapatos = idZapatos;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public int getTallaZapato() {
        return tallaZapato;
    }

    public void setTallaZapato(int tallaZapato) {
        this.tallaZapato = tallaZapato;
    }

    public boolean isZapataloEntregado() {
        return zapataloEntregado;
    }

    public void setZapataloEntregado(boolean zapataloEntregado) {
        this.zapataloEntregado = zapataloEntregado;
    }

    public Date getFechaEntegaZapatos() {
        return fechaEntegaZapatos;
    }

    public void setFechaEntegaZapatos(Date fechaEntegaZapatos) {
        this.fechaEntegaZapatos = fechaEntegaZapatos;
    }

}
