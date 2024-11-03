package com.dsi.insibo.sice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PersonalAparato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPersonalAparato;

    @OneToOne
    private PersonalAdministrativo personal;
    
    @NotNull
    @Column(unique = true)
    private int numeroAparatoPersonal;

    @OneToMany(mappedBy = "personalAparato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsistenciaPersonal> asistencias = new ArrayList<>();

    public int getIdPersonalAparato() {
        return idPersonalAparato;
    }

    public void setIdPersonalAparato(int idPersonalAparato) {
        this.idPersonalAparato = idPersonalAparato;
    }

    public PersonalAdministrativo getPersonal() {
        return personal;
    }

    public void setPersonal(PersonalAdministrativo personal) {
        this.personal = personal;
    }

    public int getNumeroAparatoPersonal() {
        return numeroAparatoPersonal;
    }

    public void setNumeroAparatoPersonal(int numeroAparatoPersonal) {
        this.numeroAparatoPersonal = numeroAparatoPersonal;
    }

    public List<AsistenciaPersonal> getAsistencias() {
        return this.asistencias;
    }

    public void setAsistencias(List<AsistenciaPersonal> asistencias) {
        this.asistencias = asistencias;
    }
}