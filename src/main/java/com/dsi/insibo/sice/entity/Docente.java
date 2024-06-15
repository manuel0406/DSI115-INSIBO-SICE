package com.dsi.insibo.sice.entity;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Docente {
    
    @Id
    private String duiDocente;

    private String nit;
    private String nup;
    private String nip;
    private String telefonoFijoDocente;
    private String nombreDocente;
    private String apellidoDocente;
    private Date fechaNacimientoD;
    private String direccionDocente;
    private String municipioD;
    private String departamentoD;
    private String distritoDocente;
    private String correoDocente;
    private String telefonoDocente;
    private String profesionDocente;
    private Date fechaMineducyt;
    private String zonaDocente;
    private String tituloDocente;
    private String especialidadEnEstudio;
    private boolean curriculumDocente;
    private boolean atestadosDocente;
    private Date fechaEntrega;
    
    
    public String getNip() {
        return nip;
    }
    public void setNip(String nip) {
        this.nip = nip;
    }
    public String getTelefonoFijoDocente() {
        return telefonoFijoDocente;
    }
    public void setTelefonoFijoDocente(String telefonoFijoDocente) {
        this.telefonoFijoDocente = telefonoFijoDocente;
    }
    public String getDuiDocente() {
        return duiDocente;
    }
    public void setDuiDocente(String duiDocente) {
        this.duiDocente = duiDocente;
    }
    
    public String getNit() {
        return nit;
    }
    public void setNit(String nit) {
        this.nit = nit;
    }
    public String getNup() {
        return nup;
    }
    public void setNup(String nup) {
        this.nup = nup;
    }
    public String getNombreDocente() {
        return nombreDocente;
    }
    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }
    public String getApellidoDocente() {
        return apellidoDocente;
    }
    public void setApellidoDocente(String apellidoDocente) {
        this.apellidoDocente = apellidoDocente;
    }
    public Date getFechaNacimientoD() {
        return fechaNacimientoD;
    }
    public void setFechaNacimientoD(Date fechaNacimientoD) {
        this.fechaNacimientoD = fechaNacimientoD;
    }
    public String getDireccionDocente() {
        return direccionDocente;
    }
    public void setDireccionDocente(String direccionDocente) {
        this.direccionDocente = direccionDocente;
    }
    public String getMunicipioD() {
        return municipioD;
    }
    public void setMunicipioD(String municipioD) {
        this.municipioD = municipioD;
    }
    public String getDepartamentoD() {
        return departamentoD;
    }
    public void setDepartamentoD(String departamentoD) {
        this.departamentoD = departamentoD;
    }
    public String getDistritoDocente() {
        return distritoDocente;
    }
    public void setDistritoDocente(String distritoDocente) {
        this.distritoDocente = distritoDocente;
    }
    public String getCorreoDocente() {
        return correoDocente;
    }
    public void setCorreoDocente(String correoDocente) {
        this.correoDocente = correoDocente;
    }
    public String getTelefonoDocente() {
        return telefonoDocente;
    }
    public void setTelefonoDocente(String telefonoDocente) {
        this.telefonoDocente = telefonoDocente;
    }
    public String getProfesionDocente() {
        return profesionDocente;
    }
    public void setProfesionDocente(String profesionDocente) {
        this.profesionDocente = profesionDocente;
    }
    public Date getFechaMineducyt() {
        return fechaMineducyt;
    }
    public void setFechaMineducyt(Date fechaMineducyt) {
        this.fechaMineducyt = fechaMineducyt;
    }
    public String getZonaDocente() {
        return zonaDocente;
    }
    public void setZonaDocente(String zonaDocente) {
        this.zonaDocente = zonaDocente;
    }
    public String getTituloDocente() {
        return tituloDocente;
    }
    public void setTituloDocente(String tituloDocente) {
        this.tituloDocente = tituloDocente;
    }
    public String getEspecialidadEnEstudio() {
        return especialidadEnEstudio;
    }
    public void setEspecialidadEnEstudio(String especialidadEnEstudio) {
        this.especialidadEnEstudio = especialidadEnEstudio;
    }
    public boolean isCurriculumDocente() {
        return curriculumDocente;
    }
    public void setCurriculumDocente(boolean curriculumDocente) {
        this.curriculumDocente = curriculumDocente;
    }
    public boolean isAtestadosDocente() {
        return atestadosDocente;
    }
    public void setAtestadosDocente(boolean atestadosDocente) {
        this.atestadosDocente = atestadosDocente;
    }
    public Date getFechaEntrega() {
        return fechaEntrega;
    }
    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    
}
