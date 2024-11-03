package com.dsi.insibo.sice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AsistenciaPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAsistenciaPersonal;

    @ManyToOne
    @JoinColumn(name = "id_personal_aparato")
    private PersonalAparato personalAparato;

    private String DepartPersonal;

    private String turnoP;
    
    private LocalDateTime horaEntradaP;

    private LocalDateTime horaSalidaP;

    public int getIdAsistenciaPersonal() {
        return this.idAsistenciaPersonal;
    }

    public void setIdAsistenciaPersonal(int idAsistenciaPersonal) {
        this.idAsistenciaPersonal = idAsistenciaPersonal;
    }
    public PersonalAparato getPersonalAparato() {
        return personalAparato;
    }

    public void setPersonalAparato(PersonalAparato personalAparato) {
        this.personalAparato = personalAparato;
    }
    
    public String getDepartPersonal() {
        return this.DepartPersonal;
    }

    public void setDepartPersonal(String DepartPersonal) {
        this.DepartPersonal = DepartPersonal;
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
