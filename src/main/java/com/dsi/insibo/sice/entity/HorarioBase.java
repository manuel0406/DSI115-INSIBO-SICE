package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class HorarioBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHorarioBase;
    @ManyToOne
    private Dia dia;
    @ManyToOne
    private Hora hora;

    public HorarioBase() {
    }

    public HorarioBase(int idHorarioBase, Dia dia, Hora hora) {
        this.idHorarioBase = idHorarioBase;
        this.dia = dia;
        this.hora = hora;
    }

    public int getIdHorarioBase() {
        return idHorarioBase;
    }

    public void setIdHorarioBase(int idHorarioBase) {
        this.idHorarioBase = idHorarioBase;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public Hora getHora() {
        return hora;
    }

    public void setHora(Hora hora) {
        this.hora = hora;
    }

}
