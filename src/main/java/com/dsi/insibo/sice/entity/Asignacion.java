package com.dsi.insibo.sice.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Asignacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAsignacion;
    @ManyToOne
    private Materia materia;
    @ManyToOne
    private Docente docente;
    @ManyToOne
    private Bachillerato bachillerato;

    public Asignacion() {
    }

    public Asignacion(int idAsignacion, Materia materia, Docente docente, Bachillerato bachillerato) {
        this.idAsignacion = idAsignacion;
        this.materia = materia;
        this.docente = docente;
        this.bachillerato = bachillerato;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
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
