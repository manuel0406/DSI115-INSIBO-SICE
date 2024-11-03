package com.dsi.insibo.sice.Asistencia_personal.DTOAparato;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AsistenciaDTO {

    @JsonProperty("ID")
    private int id;

    @JsonProperty("Depart")
    private String depart;

    @JsonProperty("Turno")
    private String turno;

    @JsonProperty("Inicio")
    private LocalDateTime inicio;

    @JsonProperty("Fin")
    private LocalDateTime fin;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    @Override
    public String toString() {
        return "AsistenciaDTO [id=" + id + ", depart=" + depart + ", turno=" + turno + ", inicio=" + inicio + ", fin="+ fin + "]";
    }
}
