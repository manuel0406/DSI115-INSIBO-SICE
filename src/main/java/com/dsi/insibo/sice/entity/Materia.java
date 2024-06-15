package com.dsi.insibo.sice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Materia {
    
    @Id
    private String codMateria;

    @ManyToOne
   
    private Docente docente;

    private String nomMateria;
    private String tipoMateria;
    public String getCodMateria() {
        return codMateria;
    }
    public void setCodMateria(String codMateria) {
        this.codMateria = codMateria;
    }
    public Docente getDocente() {
        return docente;
    }
    public void setDocente(Docente docente) {
        this.docente = docente;
    }
    public String getNomMateria() {
        return nomMateria;
    }
    public void setNomMateria(String nomMateria) {
        this.nomMateria = nomMateria;
    }
    public String getTipoMateria() {
        return tipoMateria;
    }
    public void setTipoMateria(String tipoMateria) {
        this.tipoMateria = tipoMateria;
    }

    

}
