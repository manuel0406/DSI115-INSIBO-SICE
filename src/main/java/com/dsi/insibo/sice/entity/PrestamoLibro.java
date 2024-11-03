package com.dsi.insibo.sice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PrestamoLibro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPrestamoLibro;
    @ManyToOne
    private Alumno alumno;
    @ManyToOne
    private InventarioLibro inventarioLibro;
    private String descripcionPrestamo;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private int cantidadPrestamo;
    private String estadoPrestamo;

    public PrestamoLibro() {
    }

    public PrestamoLibro(int idPrestamoLibro, Alumno alumno, InventarioLibro inventarioLibro,
            String descripcionPrestamo, Date fechaPrestamo, Date fechaDevolucion, int cantidadPrestamo,
            String estadoPrestamo) {
        this.idPrestamoLibro = idPrestamoLibro;
        this.alumno = alumno;
        this.inventarioLibro = inventarioLibro;
        this.descripcionPrestamo = descripcionPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.cantidadPrestamo = cantidadPrestamo;
        this.estadoPrestamo = estadoPrestamo;
    }

    public int getIdPrestamoLibro() {
        return idPrestamoLibro;
    }

    public void setIdPrestamoLibro(int idPrestamoLibro) {
        this.idPrestamoLibro = idPrestamoLibro;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public InventarioLibro getInventarioLibro() {
        return inventarioLibro;
    }

    public void setInventarioLibro(InventarioLibro inventarioLibro) {
        this.inventarioLibro = inventarioLibro;
    }

    public String getDescripcionPrestamo() {
        return descripcionPrestamo;
    }

    public void setDescripcionPrestamo(String descripcionPrestamo) {
        this.descripcionPrestamo = descripcionPrestamo;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public int getCantidadPrestamo() {
        return cantidadPrestamo;
    }

    public void setCantidadPrestamo(int cantidadPrestamo) {
        this.cantidadPrestamo = cantidadPrestamo;
    }

    public String getEstadoPrestamo() {
        return estadoPrestamo;
    }

    public void setEstadoPrestamo(String estadoPrestamo) {
        this.estadoPrestamo = estadoPrestamo;
    }

    public String getGradoSeccion() {
        throw new UnsupportedOperationException("Unimplemented method 'getGradoSeccion'");
    }

    public String getNombreLibro() {
        throw new UnsupportedOperationException("Unimplemented method 'getNombreLibro'");
    }

}
