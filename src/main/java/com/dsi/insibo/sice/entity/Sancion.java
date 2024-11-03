package com.dsi.insibo.sice.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Sancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSancion;
    @ManyToOne
    private Alumno alumno;
    private String descripcionSancion;
    private String tipoSancion;
    private Date fechaSancion;
    private String accionCorrectiva;

    public Sancion() {
    }

    public Sancion(int idSancion, Alumno alumno, String descripcionSancion, String tipoSancion, Date fechaSancion,
            String accionCorrectiva) {
        this.idSancion = idSancion;
        this.alumno = alumno;
        this.descripcionSancion = descripcionSancion;
        this.tipoSancion = tipoSancion;
        this.fechaSancion = fechaSancion;
        this.accionCorrectiva = accionCorrectiva;
    }

    public int getIdSancion() {
        return idSancion;
    }

    public void setIdSancion(int idSancion) {
        this.idSancion = idSancion;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getDescripcionSancion() {
        return descripcionSancion;
    }

    public void setDescripcionSancion(String descripcionSancion) {
        this.descripcionSancion = descripcionSancion;
    }

    public String getTipoSancion() {
        return tipoSancion;
    }

    public void setTipoSancion(String tipoSancion) {
        this.tipoSancion = tipoSancion;
    }

    public Date getFechaSancion() {
        return fechaSancion;
    }

    public void setFechaSancion(Date fechaSancion) {
        this.fechaSancion = fechaSancion;
    }

    public String getAccionCorrectiva() {
        return accionCorrectiva;
    }

    public void setAccionCorrectiva(String accionCorrectiva) {
        this.accionCorrectiva = accionCorrectiva;
    }

}
