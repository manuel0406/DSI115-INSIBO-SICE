package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Orientador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOrientador;
    @ManyToOne
    private Docente docente;
    @ManyToOne
    private Bachillerato bachillerato;

    public Orientador() {
    }

    public Orientador(int idOrientador, Docente docente, Bachillerato bachillerato) {
        this.idOrientador = idOrientador;
        this.docente = docente;
        this.bachillerato = bachillerato;
    }

    public int getIdOrientador() {
        return idOrientador;
    }

    public void setIdOrientador(int idOrientador) {
        this.idOrientador = idOrientador;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Bachillerato getBachillerato() {
        return bachillerato;
    }

    public void setBachillerato(Bachillerato bachillerato) {
        this.bachillerato = bachillerato;
    }

}
