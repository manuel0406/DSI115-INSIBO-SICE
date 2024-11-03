package com.dsi.insibo.sice.Asistencia_personal.DTOAparato;

public class DocenteAparatoDTO {
    private int numeroAparatoDocente;
    private String docenteDui;

    public DocenteAparatoDTO(int numeroAparatoDocente, String docenteDui) {
        this.numeroAparatoDocente = numeroAparatoDocente;
        this.docenteDui=docenteDui;
    }

    public int getNumeroAparatoDocente() {
        return numeroAparatoDocente;
    }

    public void setNumeroAparatoDocente(int numeroAparatoDocente) {
        this.numeroAparatoDocente = numeroAparatoDocente;
    }

    public String getDocenteDui() {
        return docenteDui;
    }

    public void setDocenteDui(String docenteDui) {
        this.docenteDui = docenteDui;
    }
}
