package com.dsi.insibo.sice.entity;

import java.io.Serializable;
import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Alumno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAlumno;
    private int nie;
    private String nombreAlumno;
    private String apellidoAlumno;
    private String sexoAlumno;
    private Date fechaNacimientoAlumno;
    private String duiAlumno;
    private String telefonoAlumno;
    private String direccionAlumno;
    private String municipioAlumno;
    private String departamentoAlumno;
    private String distritoAlumno;
    private String zonaAlumno;
    private String correoAlumno;
    private String viveCon;
    private String medicamento;
    private String nombreEncargado;
    private String apellidoEncargado;
    private String formaMedicacion;
    private String telefonoEncargado;
    private String padecimientos;
    private String parentescoEncargado;
    private String lugarDeTrabajo;
    private String correoEncargado;
    private String duiEncargado;
    private boolean estadoAlumno;

    @ManyToOne
    private Bachillerato bachillerato;

    public Alumno() {
    }

    public Alumno(int nie, String nombreAlumno, String apellidoAlumno, String sexoAlumno, Date fechaNacimientoAlumno,
            String duiAlumno, String telefonoAlumno, String direccionAlumno, String municipioAlumno,
            String departamentoAlumno, String distritoAlumno, String zonaAlumno, String correoAlumno, String viveCon,
            String medicamento, String nombreEncargado, String apellidoEncargado, String formaMedicacion,
            String telefonoEncargado, String padecimientos, String parentescoEncargado, String lugarDeTrabajo,
            String correoEncargado, String duiEncargado, Bachillerato bachillerato) {
        this.nie = nie;
        this.nombreAlumno = nombreAlumno;
        this.apellidoAlumno = apellidoAlumno;
        this.sexoAlumno = sexoAlumno;
        this.fechaNacimientoAlumno = fechaNacimientoAlumno;
        this.duiAlumno = duiAlumno;
        this.telefonoAlumno = telefonoAlumno;
        this.direccionAlumno = direccionAlumno;
        this.municipioAlumno = municipioAlumno;
        this.departamentoAlumno = departamentoAlumno;
        this.distritoAlumno = distritoAlumno;
        this.zonaAlumno = zonaAlumno;
        this.correoAlumno = correoAlumno;
        this.viveCon = viveCon;
        this.medicamento = medicamento;
        this.nombreEncargado = nombreEncargado;
        this.apellidoEncargado = apellidoEncargado;
        this.formaMedicacion = formaMedicacion;
        this.telefonoEncargado = telefonoEncargado;
        this.padecimientos = padecimientos;
        this.parentescoEncargado = parentescoEncargado;
        this.lugarDeTrabajo = lugarDeTrabajo;
        this.correoEncargado = correoEncargado;
        this.duiEncargado = duiEncargado;
        this.bachillerato = bachillerato;
    }

    

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getCorreoEncargado() {
        return correoEncargado;
    }

    public void setCorreoEncargado(String correoEncargado) {
        this.correoEncargado = correoEncargado;
    }

    public String getCorreoAlumno() {
        return correoAlumno;
    }

    public void setCorreoAlumno(String correoAlumno) {
        this.correoAlumno = correoAlumno;
    }

    public void setBachillerato(Bachillerato bachillerato) {
        this.bachillerato = bachillerato;
    }

    public Bachillerato getBachillerato() {
        return bachillerato;
    }

    public int getNie() {
        return nie;
    }

    public void setNie(int nie) {
        this.nie = nie;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getApellidoAlumno() {
        return apellidoAlumno;
    }

    public void setApellidoAlumno(String apellidoAlumno) {
        this.apellidoAlumno = apellidoAlumno;
    }

    public String getDuiAlumno() {
        return duiAlumno;
    }

    public void setDuiAlumno(String duiAlumno) {
        this.duiAlumno = duiAlumno;
    }

    public String getDireccionAlumno() {
        return direccionAlumno;
    }

    public void setDireccionAlumno(String direccionAlumno) {
        this.direccionAlumno = direccionAlumno;
    }

    public String getViveCon() {
        return viveCon;
    }

    public void setViveCon(String viveCon) {
        this.viveCon = viveCon;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getFormaMedicacion() {
        return formaMedicacion;
    }

    public void setFormaMedicacion(String formaMedicacion) {
        this.formaMedicacion = formaMedicacion;
    }

    public String getTelefonoEncargado() {
        return telefonoEncargado;
    }

    public void setTelefonoEncargado(String telefonoEncargado) {
        this.telefonoEncargado = telefonoEncargado;
    }

    public String getPadecimientos() {
        return padecimientos;
    }

    public void setPadecimientos(String padecimientos) {
        this.padecimientos = padecimientos;
    }

    public String getParentescoEncargado() {
        return parentescoEncargado;
    }

    public void setParentescoEncargado(String parentescoEncargado) {
        this.parentescoEncargado = parentescoEncargado;
    }

    public String getLugarDeTrabajo() {
        return lugarDeTrabajo;
    }

    public void setLugarDeTrabajo(String lugarDeTrabajo) {
        this.lugarDeTrabajo = lugarDeTrabajo;
    }

    public String getApellidoEncargado() {
        return apellidoEncargado;
    }

    public void setApellidoEncargado(String apellidoEncargado) {
        this.apellidoEncargado = apellidoEncargado;
    }

    public Date getFechaNacimientoAlumno() {
        return fechaNacimientoAlumno;
    }

    public void setFechaNacimientoAlumno(Date fechaNacimientoAlumno) {
        this.fechaNacimientoAlumno = fechaNacimientoAlumno;
    }

    public String getNombreEncargado() {
        return nombreEncargado;
    }

    public void setNombreEncargado(String nombreEncargado) {
        this.nombreEncargado = nombreEncargado;
    }

    public String getDuiEncargado() {
        return duiEncargado;
    }

    public void setDuiEncargado(String duiEncargado) {
        this.duiEncargado = duiEncargado;
    }

    public String getSexoAlumno() {
        return sexoAlumno;
    }

    public void setSexoAlumno(String sexoAlumno) {
        this.sexoAlumno = sexoAlumno;
    }

    public String getTelefonoAlumno() {
        return telefonoAlumno;
    }

    public void setTelefonoAlumno(String telefonoAlumno) {
        this.telefonoAlumno = telefonoAlumno;
    }

    public String getMunicipioAlumno() {
        return municipioAlumno;
    }

    public void setMunicipioAlumno(String municipioAlumno) {
        this.municipioAlumno = municipioAlumno;
    }

    public String getDepartamentoAlumno() {
        return departamentoAlumno;
    }

    public void setDepartamentoAlumno(String departamentoAlumno) {
        this.departamentoAlumno = departamentoAlumno;
    }

    public String getDistritoAlumno() {
        return distritoAlumno;
    }

    public void setDistritoAlumno(String distritoAlumno) {
        this.distritoAlumno = distritoAlumno;
    }

    public String getZonaAlumno() {
        return zonaAlumno;
    }

    public void setZonaAlumno(String zonaAlumno) {
        this.zonaAlumno = zonaAlumno;
    }

    public boolean isEstadoAlumno() {
        return estadoAlumno;
    }

    public void setEstadoAlumno(boolean estadoAlumno) {
        this.estadoAlumno = estadoAlumno;
    }

}
