package com.dsi.insibo.sice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class EntregaPapeleria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEntregaPapeleria;
    @ManyToOne
    private InventarioPapeleria inventarioPapeleria;
    private Date entregaFecha;
    private int entregaCantidad;
    private String entregaPersona;

    public EntregaPapeleria() {
    }

    public EntregaPapeleria(int idEntregaPapeleria, InventarioPapeleria inventarioPapeleria, Date entregaFecha,
            int entregaCantidad, String entregaPersona) {
        this.idEntregaPapeleria = idEntregaPapeleria;
        this.inventarioPapeleria = inventarioPapeleria;
        this.entregaFecha = entregaFecha;
        this.entregaCantidad = entregaCantidad;
        this.entregaPersona = entregaPersona;
    }

    public int getIdEntregaPapeleria() {
        return idEntregaPapeleria;
    }

    public void setIdEntregaPapeleria(int idEntregaPapeleria) {
        this.idEntregaPapeleria = idEntregaPapeleria;
    }

    public InventarioPapeleria getInventarioPapeleria() {
        return inventarioPapeleria;
    }

    public void setInventarioPapeleria(InventarioPapeleria inventarioPapeleria) {
        this.inventarioPapeleria = inventarioPapeleria;
    }

    public Date getEntregaFecha() {
        return entregaFecha;
    }

    public void setEntregaFecha(Date entregaFecha) {
        this.entregaFecha = entregaFecha;
    }

    public int getEntregaCantidad() {
        return entregaCantidad;
    }

    public void setEntregaCantidad(int entregaCantidad) {
        this.entregaCantidad = entregaCantidad;
    }

    public String getEntregaPersona() {
        return entregaPersona;
    }

    public void setEntregaPersona(String entregaPersona) {
        this.entregaPersona = entregaPersona;
    }

}
