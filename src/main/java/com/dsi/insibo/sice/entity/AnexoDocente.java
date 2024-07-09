package com.dsi.insibo.sice.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class AnexoDocente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAnexoDocente;   

    private String nombreCurriculumDocente;
    @Lob
    private byte[] datosCurriculumDocente;
    private Date fechaCurriculumDocente;

    private String nombreDuiDocente;
    @Lob
    private byte[] datosDuiDocente;
    private Date fechaDuiDocente;

    private String nombreNupDocente;
    @Lob
    private byte[] datosNupDocente;
    private Date fechaNupDocente;


    @ManyToOne
    private Docente docente;


    public int getIdAnexoDocente() {
        return idAnexoDocente;
    }


    public void setIdAnexoDocente(int idAnexoDocente) {
        this.idAnexoDocente = idAnexoDocente;
    }


    public String getNombreCurriculumDocente() {
        return nombreCurriculumDocente;
    }


    public void setNombreCurriculumDocente(String nombreCurriculumDocente) {
        this.nombreCurriculumDocente = nombreCurriculumDocente;
    }


    public byte[] getDatosCurriculumDocente() {
        return datosCurriculumDocente;
    }


    public void setDatosCurriculumDocente(byte[] datosCurriculumDocente) {
        this.datosCurriculumDocente = datosCurriculumDocente;
    }


    public Date getFechaCurriculumDocente() {
        return fechaCurriculumDocente;
    }


    public void setFechaCurriculumDocente(Date fechaCurriculumDocente) {
        this.fechaCurriculumDocente = fechaCurriculumDocente;
    }


    public String getNombreDuiDocente() {
        return nombreDuiDocente;
    }


    public void setNombreDuiDocente(String nombreDuiDocente) {
        this.nombreDuiDocente = nombreDuiDocente;
    }


    public byte[] getDatosDuiDocente() {
        return datosDuiDocente;
    }


    public void setDatosDuiDocente(byte[] datosDuiDocente) {
        this.datosDuiDocente = datosDuiDocente;
    }


    public Date getFechaDuiDocente() {
        return fechaDuiDocente;
    }


    public void setFechaDuiDocente(Date fechaDuiDocente) {
        this.fechaDuiDocente = fechaDuiDocente;
    }


    public String getNombreNupDocente() {
        return nombreNupDocente;
    }


    public void setNombreNupDocente(String nombreNupDocente) {
        this.nombreNupDocente = nombreNupDocente;
    }


    public byte[] getDatosNupDocente() {
        return datosNupDocente;
    }


    public void setDatosNupDocente(byte[] datosNupDocente) {
        this.datosNupDocente = datosNupDocente;
    }


    public Date getFechaNupDocente() {
        return fechaNupDocente;
    }


    public void setFechaNupDocente(Date fechaNupDocente) {
        this.fechaNupDocente = fechaNupDocente;
    }


    public Docente getDocente() {
        return docente;
    }


    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    
}
