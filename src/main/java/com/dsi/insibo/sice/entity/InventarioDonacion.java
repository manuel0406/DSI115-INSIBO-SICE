package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InventarioDonacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idInventarioDonacion;
    private String tipoPrenda;
    private String tallaPrenda;
    private int cantidadPrenda;

    public InventarioDonacion() {
    }

    public InventarioDonacion(int idInventarioDonacion, String tipoPrenda, String tallaPrenda, int cantidadPrenda) {
        this.idInventarioDonacion = idInventarioDonacion;
        this.tipoPrenda = tipoPrenda;
        this.tallaPrenda = tallaPrenda;
        this.cantidadPrenda = cantidadPrenda;
    }

    public int getIdInventarioDonacion() {
        return idInventarioDonacion;
    }

    public void setIdInventarioDonacion(int idInventarioDonacion) {
        this.idInventarioDonacion = idInventarioDonacion;
    }

    public String getTipoPrenda() {
        return tipoPrenda;
    }

    public void setTipoPrenda(String tipoPrenda) {
        this.tipoPrenda = tipoPrenda;
    }

    public String getTallaPrenda() {
        return tallaPrenda;
    }

    public void setTallaPrenda(String tallaPrenda) {
        this.tallaPrenda = tallaPrenda;
    }

    public int getCantidadPrenda() {
        return cantidadPrenda;
    }

    public void setCantidadPrenda(int cantidadPrenda) {
        this.cantidadPrenda = cantidadPrenda;
    }

}
