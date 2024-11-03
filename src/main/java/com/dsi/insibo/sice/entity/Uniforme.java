package com.dsi.insibo.sice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Uniforme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUniforme;
    @ManyToOne
    private Alumno alumno;
    private String tallaUniforme;
    private boolean uniformeEntegado;
    private Date fechaEntregaUniforme;

    public Uniforme() {
    }

    public Uniforme(int idUniforme, Alumno alumno, String tallaUniforme, boolean uniformeEntegado,
            Date fechaEntregaUniforme) {
        this.idUniforme = idUniforme;
        this.alumno = alumno;
        this.tallaUniforme = tallaUniforme;
        this.uniformeEntegado = uniformeEntegado;
        this.fechaEntregaUniforme = fechaEntregaUniforme;
    }

    public int getIdUniforme() {
        return idUniforme;
    }

    public void setIdUniforme(int idUniforme) {
        this.idUniforme = idUniforme;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getTallaUniforme() {
        return tallaUniforme;
    }

    public void setTallaUniforme(String tallaUniforme) {
        this.tallaUniforme = tallaUniforme;
    }

    public boolean isUniformeEntegado() {
        return uniformeEntegado;
    }

    public void setUniformeEntegado(boolean uniformeEntegado) {
        this.uniformeEntegado = uniformeEntegado;
    }

    public Date getFechaEntregaUniforme() {
        return fechaEntregaUniforme;
    }

    public void setFechaEntregaUniforme(Date fechaEntregaUniforme) {
        this.fechaEntregaUniforme = fechaEntregaUniforme;
    }

}
