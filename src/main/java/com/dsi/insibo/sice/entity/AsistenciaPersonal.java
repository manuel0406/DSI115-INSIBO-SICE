package com.dsi.insibo.sice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class AsistenciaPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAsistenciaP;

    @ManyToOne
    private PersonalAdministrativo personal;

    @ManyToOne
    private Justificacion justificacion;

    private String turnoP;
    private LocalDateTime horaEntradaP;
    private LocalDateTime horaSalidaP;
    public int getIdAsistenciaP() {
        return idAsistenciaP;
    }
    public void setIdAsistenciaP(int idAsistenciaP) {
        this.idAsistenciaP = idAsistenciaP;
    }
    public PersonalAdministrativo getPersonal() {
        return personal;
    }
    public void setPersonal(PersonalAdministrativo personal) {
        this.personal = personal;
    }
    public Justificacion getJustificacion() {
        return justificacion;
    }
    public void setJustificacion(Justificacion justificacion) {
        this.justificacion = justificacion;
    }
    public String getTurnoP() {
        return turnoP;
    }
    public void setTurnoP(String turnoP) {
        this.turnoP = turnoP;
    }
    public LocalDateTime getHoraEntradaP() {
        return horaEntradaP;
    }
    public void setHoraEntradaP(LocalDateTime horaEntradaP) {
        this.horaEntradaP = horaEntradaP;
    }
    public LocalDateTime getHoraSalidaP() {
        return horaSalidaP;
    }
    public void setHoraSalidaP(LocalDateTime horaSalidaP) {
        this.horaSalidaP = horaSalidaP;
    }
    
}
