package com.dsi.insibo.sice.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AsistenciaDocente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAsistencia;

    @ManyToOne
    @JoinColumn(name = "id_docente_aparato")
    private DocenteAparato aparatoDocente;

    private String DeparDocente;
    private String turno;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;

    public int getIdAsistencia() {
        return this.idAsistencia;
    }

    public void setIdAsistencia(int idAsistencia) {
        this.idAsistencia = idAsistencia;
    }
    
    public String getDeparDocente() {
        return this.DeparDocente;
    }

    public void setDeparDocente(String DeparDocente) {
        this.DeparDocente = DeparDocente;
    }
    
    public String getTurno() {
        return this.turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public LocalDateTime getHoraEntrada() {
        return this.horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return this.horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }
    public DocenteAparato getAparatoDocente() {
        return this.aparatoDocente;
    }

    public void setAparatoDocente(DocenteAparato aparatoDocente) {
        this.aparatoDocente = aparatoDocente;
    }

}
