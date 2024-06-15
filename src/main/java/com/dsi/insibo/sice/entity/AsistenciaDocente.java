package com.dsi.insibo.sice.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class AsistenciaDocente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAsistencia;


    @ManyToOne
    private Justificacion justificacion;

    private String duiDocente;

    private String turno;

    private LocalDateTime horaEntrada;

    private LocalDateTime horaSalida;

    public int getIdAsistencia() {
        return idAsistencia;
    }

    public void setIdAsistencia(int idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public Justificacion getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(Justificacion justificacion) {
        this.justificacion = justificacion;
    }

    public String getDuiDocente() {
        return duiDocente;
    }

    public void setDuiDocente(String duiDocente) {
        this.duiDocente = duiDocente;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    
}
