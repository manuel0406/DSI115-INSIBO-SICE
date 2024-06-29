package com.dsi.insibo.sice.entity;

import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

public class AnexoPersonalAdministrativo {
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAnexoPersonal;   

    private String nombreCurriculumPersonal;
    @Lob
    private byte[] datosCurriculumPersonal;
    private Date fechaCurriculumPersonal;

    private String nombreDuiPersonal;
    @Lob
    private byte[] datosDuiPersonal;
    private Date fechaDuiPersonal;
    
    private String nombreNupPersonal;
    @Lob
    private byte[] datosNupPersonal;
    private Date fechaNupPersonal;

    @ManyToOne
    private PersonalAdministrativo personalAdministrativo;

    public int getIdAnexoPersonal() {
        return idAnexoPersonal;
    }

    public void setIdAnexoPersonal(int idAnexoPersonal) {
        this.idAnexoPersonal = idAnexoPersonal;
    }

    public String getNombreCurriculumPersonal() {
        return nombreCurriculumPersonal;
    }

    public void setNombreCurriculumPersonal(String nombreCurriculumPersonal) {
        this.nombreCurriculumPersonal = nombreCurriculumPersonal;
    }

    public byte[] getDatosCurriculumPersonal() {
        return datosCurriculumPersonal;
    }

    public void setDatosCurriculumPersonal(byte[] datosCurriculumPersonal) {
        this.datosCurriculumPersonal = datosCurriculumPersonal;
    }

    public Date getFechaCurriculumPersonal() {
        return fechaCurriculumPersonal;
    }

    public void setFechaCurriculumPersonal(Date fechaCurriculumPersonal) {
        this.fechaCurriculumPersonal = fechaCurriculumPersonal;
    }

    public String getNombreDuiPersonal() {
        return nombreDuiPersonal;
    }

    public void setNombreDuiPersonal(String nombreDuiPersonal) {
        this.nombreDuiPersonal = nombreDuiPersonal;
    }

    public byte[] getDatosDuiPersonal() {
        return datosDuiPersonal;
    }

    public void setDatosDuiPersonal(byte[] datosDuiPersonal) {
        this.datosDuiPersonal = datosDuiPersonal;
    }

    public Date getFechaDuiPersonal() {
        return fechaDuiPersonal;
    }

    public void setFechaDuiPersonal(Date fechaDuiPersonal) {
        this.fechaDuiPersonal = fechaDuiPersonal;
    }

    public String getNombreNupPersonal() {
        return nombreNupPersonal;
    }

    public void setNombreNupPersonal(String nombreNupPersonal) {
        this.nombreNupPersonal = nombreNupPersonal;
    }

    public byte[] getDatosNupPersonal() {
        return datosNupPersonal;
    }

    public void setDatosNupPersonal(byte[] datosNupPersonal) {
        this.datosNupPersonal = datosNupPersonal;
    }

    public Date getFechaNupPersonal() {
        return fechaNupPersonal;
    }

    public void setFechaNupPersonal(Date fechaNupPersonal) {
        this.fechaNupPersonal = fechaNupPersonal;
    }

    public PersonalAdministrativo getPersonalAdministrativo() {
        return personalAdministrativo;
    }

    public void setPersonalAdministrativo(PersonalAdministrativo personalAdministrativo) {
        this.personalAdministrativo = personalAdministrativo;
    }


    
}
