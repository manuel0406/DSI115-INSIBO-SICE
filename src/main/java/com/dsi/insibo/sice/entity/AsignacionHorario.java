package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class AsignacionHorario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAsignacionHorario;
    @ManyToOne
    private Asignacion asignacion;
    @ManyToOne
    private HorarioBase horarioBase;

    public AsignacionHorario() {
    }

    public AsignacionHorario(int idAsignacionHorario, Asignacion asignacion, HorarioBase horarioBase) {
        this.idAsignacionHorario = idAsignacionHorario;
        this.asignacion = asignacion;
        this.horarioBase = horarioBase;
    }

    public int getIdAsignacionHorario() {
        return idAsignacionHorario;
    }

    public void setIdAsignacionHorario(int idAsignacionHorario) {
        this.idAsignacionHorario = idAsignacionHorario;
    }

    public Asignacion getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(Asignacion asignacion) {
        this.asignacion = asignacion;
    }

    public HorarioBase getHorarioBase() {
        return horarioBase;
    }

    public void setHorarioBase(HorarioBase horarioBase) {
        this.horarioBase = horarioBase;
    }

}
