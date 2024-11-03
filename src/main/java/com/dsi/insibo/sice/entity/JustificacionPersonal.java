package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;

@Entity
public class JustificacionPersonal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idJustificacionPersonal;
    private String descripcionJustificacionPersonal;
    @Lob
    private byte[] archivoPersonal;

    @OneToOne
    @JoinColumn(name = "idAsistenciaPersonal", referencedColumnName = "idAsistenciaPersonal", nullable = true)
    private AsistenciaPersonal asistenciaPersonal;

    public int getIdJustificacionPersonal() {
        return this.idJustificacionPersonal;
    }

    public void setIdJustificacionPersonal(int idJustificacionPersonal) {
        this.idJustificacionPersonal = idJustificacionPersonal;
    }

    public String getDescripcionJustificacionPersonal() {
        return this.descripcionJustificacionPersonal;
    }

    public void setDescripcionJustificacionPersonal(String descripcionJustificacionPersonal) {
        this.descripcionJustificacionPersonal = descripcionJustificacionPersonal;
    }

    public AsistenciaPersonal getAsistenciaPersonal() {
        return this.asistenciaPersonal;
    }

    public void setAsistenciaPersonal(AsistenciaPersonal asistenciaPersonal) {
        this.asistenciaPersonal = asistenciaPersonal;
    }

    public byte[] getArchivoPersonal() {
        return this.archivoPersonal;
    }

    public void setArchivoPersonal(byte[] archivoPersonal) {
        this.archivoPersonal = archivoPersonal;
    }
}
