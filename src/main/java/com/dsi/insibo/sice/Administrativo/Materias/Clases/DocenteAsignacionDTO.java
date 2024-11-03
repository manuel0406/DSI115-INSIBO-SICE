package com.dsi.insibo.sice.Administrativo.Materias.Clases;

public class DocenteAsignacionDTO {
    private String duiDocente;
    private String nombreDocente;
    private String apellidoDocente;

    // Constructor
    public DocenteAsignacionDTO(String duiDocente, String nombreDocente, String apellidoDocente) {
        this.duiDocente = duiDocente;
        this.nombreDocente = nombreDocente;
        this.apellidoDocente = apellidoDocente;
    }

    // Getters y setters
    public String getDuiDocente() {
        return duiDocente;
    }

    public void setDuiDocente(String duiDocente) {
        this.duiDocente = duiDocente;
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
}
