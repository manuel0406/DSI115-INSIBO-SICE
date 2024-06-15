package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Justificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idJustificacion;

    private String descripcionJustificacion;
    private String archivo;
    
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
    public String getArchivo() {
        return archivo;
    }
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    
}
