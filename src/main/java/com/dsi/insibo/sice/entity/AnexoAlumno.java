package com.dsi.insibo.sice.entity;

import java.util.Date;

import jakarta.persistence.*;


@Entity
public class AnexoAlumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAnexoAlumno;
    private String nombrePartidaNacimiento;
    @Lob
    private byte[] datosPartidaNacimiento;
    private Date fechaPartidaNacimiento;

    private String nombreDui;
    @Lob
    private byte[] datosDui;
    private Date fechaDui;    

    @ManyToOne
    private Alumno alumno;


    public int getIdAnexoAlumno() {
        return idAnexoAlumno;
    }

    public void setIdAnexoAlumno(int idAnexoAlumno) {
        this.idAnexoAlumno = idAnexoAlumno;
    }

    public String getNombrePartidaNacimiento() {
        return nombrePartidaNacimiento;
    }

    public void setNombrePartidaNacimiento(String nombrePartidaNacimiento) {
        this.nombrePartidaNacimiento = nombrePartidaNacimiento;
    }

    public byte[] getDatosPartidaNacimiento() {
        return datosPartidaNacimiento;
    }

    public void setDatosPartidaNacimiento(byte[] datosPartidaNacimiento) {
        this.datosPartidaNacimiento = datosPartidaNacimiento;
    }

    public Date getFechaPartidaNacimiento() {
        return fechaPartidaNacimiento;
    }

    public void setFechaPartidaNacimiento(Date fechaPartidaNacimiento) {
        this.fechaPartidaNacimiento = fechaPartidaNacimiento;
    }

    public String getNombreDui() {
        return nombreDui;
    }

    public void setNombreDui(String nombreDui) {
        this.nombreDui = nombreDui;
    }

    public byte[] getDatosDui() {
        return datosDui;
    }

    public void setDatosDui(byte[] datosDui) {
        this.datosDui = datosDui;
    }

    public Date getFechaDui() {
        return fechaDui;
    }

    public void setFechaDui(Date fechaDui) {
        this.fechaDui = fechaDui;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }


    
    
    
        
}
