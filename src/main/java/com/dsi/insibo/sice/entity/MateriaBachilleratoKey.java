package com.dsi.insibo.sice.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;



@Embeddable
public class MateriaBachilleratoKey implements Serializable {

    private String codigoBachillerato;
    private String codMateria;

    public String getCodigoBachillerato() {
        return codigoBachillerato;
    }
    public void setCodigoBachillerato(String codigoBachillerato) {
        this.codigoBachillerato = codigoBachillerato;
    }
    public String getCodMateria() {
        return codMateria;
    }
    public void setCodMateria(String codMateria) {
        this.codMateria = codMateria;
    }

    // Getters, Setters, hashCode y equals
}

