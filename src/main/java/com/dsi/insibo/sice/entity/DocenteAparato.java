package com.dsi.insibo.sice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DocenteAparato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDocenteAparato;

    @OneToOne
    @NotEmpty
    private Docente docente;

    @NotNull
    @Column(unique = true)
    private int numeroAparatoDocente;

    @OneToMany(mappedBy = "aparatoDocente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsistenciaDocente> asistencias = new ArrayList<>();

    public List<AsistenciaDocente> getAsistencias() {
        return this.asistencias;
    }

    public void setAsistencias(List<AsistenciaDocente> asistencias) {
        this.asistencias = asistencias;
    }

    public int getIdDocenteAparato() {
        return this.idDocenteAparato;
    }

    public void setIdDocenteAparato(int idDocenteAparato) {
        this.idDocenteAparato = idDocenteAparato;
    }

    public Docente getDocente() {
        return this.docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public int getNumeroAparatoDocente() {
        return this.numeroAparatoDocente;
    }

    public void setNumeroAparatoDocente(int numeroAparatoDocente) {
        this.numeroAparatoDocente = numeroAparatoDocente;
    }
}