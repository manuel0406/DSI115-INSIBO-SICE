package com.dsi.insibo.sice.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Materia implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMateria;
    @Column(unique = true)
    private String codMateria;
    @Column(unique = true)
    private String nomMateria;
    private String tipoMateria;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true") // Valor por defecto
    private boolean activoMateria;

    public Materia() {
    }

    public Materia(int idMateria, String codMateria, String nomMateria, String tipoMateria, boolean activoMateria) {
        this.idMateria = idMateria;
        this.codMateria = codMateria;
        this.nomMateria = nomMateria;
        this.tipoMateria = tipoMateria;
        this.activoMateria = activoMateria;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getCodMateria() {
        return codMateria;
    }

    public void setCodMateria(String codMateria) {
        this.codMateria = codMateria;
    }

    public String getNomMateria() {
        return nomMateria;
    }

    public void setNomMateria(String nomMateria) {
        this.nomMateria = nomMateria;
    }

    public String getTipoMateria() {
        return tipoMateria;
    }

    public void setTipoMateria(String tipoMateria) {
        this.tipoMateria = tipoMateria;
    }

    public boolean getActivoMateria() {
        return activoMateria;
    }

    public void setActivoMateria(boolean activoMateria) {
        this.activoMateria = activoMateria;
    }
}
