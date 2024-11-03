package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;

@Entity
public class JustificacionDocente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idJustificacion;

    private String descripcionJustificacion;
    @Lob
    private byte[] archivo;

    @OneToOne
    @JoinColumn(name = "idAsistencia", referencedColumnName = "idAsistencia", nullable = true)
    private AsistenciaDocente asistenciaDocente;

    public int getIdJustificacion() {
        return idJustificacion;
    }

    public void setIdJustificacion(int idJustificacion) {
        this.idJustificacion = idJustificacion;
    }

    public String getDescripcionJustificacion() {
        return descripcionJustificacion;
    }

    public void setDescripcionJustificacion(String descripcionJustificacion) {
        this.descripcionJustificacion = descripcionJustificacion;
    }

    public byte[] getArchivo() {
        return this.archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public AsistenciaDocente getAsistenciaDocente() {
        return this.asistenciaDocente;
    }

    public void setAsistenciaDocente(AsistenciaDocente asistenciaDocente) {
        this.asistenciaDocente = asistenciaDocente;
    }

}
