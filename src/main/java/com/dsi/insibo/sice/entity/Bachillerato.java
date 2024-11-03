package com.dsi.insibo.sice.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Bachillerato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigoBachillerato;
    private String nombreCarrera;
    private String seccion;
    private int grado;
    @ManyToOne
    private AnioAcademico anioAcademico;

    public Bachillerato() {
    }

    public Bachillerato( String nombreCarrera, String seccion, int grado,
            AnioAcademico anioAcademico) {
        
        this.nombreCarrera = nombreCarrera;
        this.seccion = seccion;
        this.grado = grado;
        this.anioAcademico = anioAcademico;
    }

    public int getCodigoBachillerato() { 
        return codigoBachillerato;
    }

    public void setCodigoBachillerato(int codigoBachillerato) {
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

    public AnioAcademico getAnioAcademico() {
        return anioAcademico;
    }

    public void setAnioAcademico(AnioAcademico anioAcademico) {
        this.anioAcademico = anioAcademico;
    }

    @Override
    public String toString() {
        return "Bachillerato [codigoBachillerato=" + codigoBachillerato + ", nombreCarrera=" + nombreCarrera
                + ", seccion=" + seccion + ", grado=" + grado + "]";
    }

}
