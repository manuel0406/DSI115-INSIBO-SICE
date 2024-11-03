package com.dsi.insibo.sice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InventarioLibro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idInventarioLibros;
    private String tipoLibro;
    private String tituloLibro;
    private String autorLibro;
    private double precioUnitario;
    private int existenciaLibro;
    private Date fechaIngreso;
    private int cantidadMalEstado;

    public InventarioLibro() {
    }

    public InventarioLibro(int idInventarioLibros, String tipoLibro, String tituloLibro, String autorLibro,
            double precioUnitario, int existenciaLibro, Date fechaIngreso, int cantidadMalEstado) {
        this.idInventarioLibros = idInventarioLibros;
        this.tipoLibro = tipoLibro;
        this.tituloLibro = tituloLibro;
        this.autorLibro = autorLibro;
        this.precioUnitario = precioUnitario;
        this.existenciaLibro = existenciaLibro;
        this.fechaIngreso = fechaIngreso;
        this.cantidadMalEstado = cantidadMalEstado;
    }

    public int getIdInventarioLibros() {
        return idInventarioLibros;
    }

    public void setIdInventarioLibros(int idInventarioLibros) {
        this.idInventarioLibros = idInventarioLibros;
    }

    public String getTipoLibro() {
        return tipoLibro;
    }

    public void setTipoLibro(String tipoLibro) {
        this.tipoLibro = tipoLibro;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getAutorLibro() {
        return autorLibro;
    }

    public void setAutorLibro(String autorLibro) {
        this.autorLibro = autorLibro;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getExistenciaLibro() {
        return existenciaLibro;
    }

    public void setExistenciaLibro(int existenciaLibro) {
        this.existenciaLibro = existenciaLibro;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getCantidadMalEstado() {
        return cantidadMalEstado;
    }

    public void setCantidadMalEstado(int cantidadMalEstado) {
        this.cantidadMalEstado = cantidadMalEstado;
    }

}
