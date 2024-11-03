package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AnioAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAnioAcademico;
    private int anio;
    private boolean activoAnio;
    private boolean activoMatricula;
    private boolean cerrado;

    public AnioAcademico() {
    }

    public AnioAcademico(int idAnioAcademico, int anio, boolean activoAnio, boolean activoMatricula, boolean cerrado) {
        this.idAnioAcademico = idAnioAcademico;
        this.anio = anio;
        this.activoAnio = activoAnio;
        this.activoMatricula = activoMatricula;
        this.cerrado = cerrado;
    }

    public AnioAcademico(int anio, boolean activoAnio, boolean activoMatricula, boolean cerrado) {
        this.anio = anio;
        this.activoAnio = activoAnio;
        this.activoMatricula = activoMatricula;
        this.cerrado = cerrado;
    }

    public int getIdAnioAcademico() {
        return idAnioAcademico;
    }

    public void setIdAnioAcademico(int idAnioAcademico) {
        this.idAnioAcademico = idAnioAcademico;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public boolean isActivoAnio() {
        return activoAnio;
    }

    public void setActivoAnio(boolean activoAnio) {
        this.activoAnio = activoAnio;
    }

    public boolean isActivoMatricula() {
        return activoMatricula;
    }

    public void setActivoMatricula(boolean activoMatricula) {
        this.activoMatricula = activoMatricula;
    }

    public boolean isCerrado() {
        return cerrado;
    }

    public void setCerrado(boolean cerrado) {
        this.cerrado = cerrado;
    }

}
