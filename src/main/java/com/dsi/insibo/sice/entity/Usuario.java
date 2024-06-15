package com.dsi.insibo.sice.entity;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    private String correoUsuario;
    private String contrasenaUsuario;
    private String rolUsuario;
    private boolean primerIngreso;
    private String estadoUsuario;
    @OneToOne
    private Docente docente;
    @OneToOne
    private PersonalAdministrativo personalAdministrativo;
    
    public Docente getDocente() {
        return docente;
    }
    public void setDocente(Docente docente) {
        this.docente = docente;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getCorreoUsuario() {
        return correoUsuario;
    }
    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }
    public String getContrasenaUsuario() {
        return contrasenaUsuario;
    }
    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenaUsuario = contrasenaUsuario;
    }
    public String getRolUsuario() {
        return rolUsuario;
    }
    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
    public boolean isPrimerIngreso() {
        return primerIngreso;
    }
    public void setPrimerIngreso(boolean primerIngreso) {
        this.primerIngreso = primerIngreso;
    }
    public String getEstadoUsuario() {
        return estadoUsuario;
    }
    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }
    public PersonalAdministrativo getPersonalAdministrativo() {
        return personalAdministrativo;
    }
    public void setPersonalAdministrativo(PersonalAdministrativo personalAdministrativo) {
        this.personalAdministrativo = personalAdministrativo;
    }

    
}
