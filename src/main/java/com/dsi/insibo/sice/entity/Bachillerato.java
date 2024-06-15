package com.dsi.insibo.sice.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Bachillerato implements Serializable{
    @Id
    private String codigoBachillerato;
    private String nombreCarrera;
    private String seccion;
    private int grado;

    public String getCodigoBachillerato() {
        return codigoBachillerato;
    }
    public void setCodigoBachillerato(String codigoBachillerato) {
        this.codigoBachillerato = codigoBachillerato;
    }
    public String getNombreCarrera() {
        return nombreCarrera;
    }
    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }
    public String getSeccion() {
        return seccion;
    }
    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
    public int getGrado() {
        return grado;
    }
    public void setGrado(int grado) {
        this.grado = grado;
    }
    @Override
    public String toString() {
        return "Bachillerato [codigoBachillerato=" + codigoBachillerato + ", nombreCarrera=" + nombreCarrera
                + ", seccion=" + seccion + ", grado=" + grado + "]";
    }
    
}
