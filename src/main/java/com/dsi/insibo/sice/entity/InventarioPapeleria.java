package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InventarioPapeleria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idArticulo;
    private String nombreArticulo;
    private String descripcionArticulo;
    private int existenciaArticulo;

    public InventarioPapeleria() {
    }

    public InventarioPapeleria(int idArticulo, String nombreArticulo, String descripcionArticulo,
            int existenciaArticulo) {
        this.idArticulo = idArticulo;
        this.nombreArticulo = nombreArticulo;
        this.descripcionArticulo = descripcionArticulo;
        this.existenciaArticulo = existenciaArticulo;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public String getDescripcionArticulo() {
        return descripcionArticulo;
    }

    public void setDescripcionArticulo(String descripcionArticulo) {
        this.descripcionArticulo = descripcionArticulo;
    }

    public int getExistenciaArticulo() {
        return existenciaArticulo;
    }

    public void setExistenciaArticulo(int existenciaArticulo) {
        this.existenciaArticulo = existenciaArticulo;
    }

}
