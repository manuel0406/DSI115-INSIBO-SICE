package com.dsi.insibo.sice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDonacion;
    @ManyToOne
    private InventarioDonacion inventarioDonacion;
    @ManyToOne
    private Alumno alumno;
    private Date fechaEntregaDonacion;

    public Donacion() {
    }

    public Donacion(int idDonacion, InventarioDonacion inventarioDonacion, Alumno alumno, Date fechaEntregaDonacion) {
        this.idDonacion = idDonacion;
        this.inventarioDonacion = inventarioDonacion;
        this.alumno = alumno;
        this.fechaEntregaDonacion = fechaEntregaDonacion;
    }

    public int getIdDonacion() {
        return idDonacion;
    }

    public void setIdDonacion(int idDonacion) {
        this.idDonacion = idDonacion;
    }

    public InventarioDonacion getInventarioDonacion() {
        return inventarioDonacion;
    }

    public void setInventarioDonacion(InventarioDonacion inventarioDonacion) {
        this.inventarioDonacion = inventarioDonacion;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Date getFechaEntregaDonacion() {
        return fechaEntregaDonacion;
    }

    public void setFechaEntregaDonacion(Date fechaEntregaDonacion) {
        this.fechaEntregaDonacion = fechaEntregaDonacion;
    }

}
