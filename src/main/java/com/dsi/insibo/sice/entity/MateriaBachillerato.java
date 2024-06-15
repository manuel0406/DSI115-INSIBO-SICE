package com.dsi.insibo.sice.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class MateriaBachillerato {

    @EmbeddedId
    private MateriaBachilleratoKey id;

    @ManyToOne
    @MapsId("codigoBachillerato")
    
    private Bachillerato bachillerato;

    @ManyToOne
    @MapsId("codMateria")
   
    private Materia materia;

    public MateriaBachilleratoKey getId() {
        return id;
    }

    public void setId(MateriaBachilleratoKey id) {
        this.id = id;
    }

    public Bachillerato getBachillerato() {
        return bachillerato;
    }

    public void setBachillerato(Bachillerato bachillerato) {
        this.bachillerato = bachillerato;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    // Getters y Setters
}
