package com.dsi.insibo.sice.Asistencia_personal.DTOAparato;

public class PersonalAparatoDTO {
    private int numeroAparatoPersonal;
    private String duiPersonal;

    public PersonalAparatoDTO(int numeroAparatoPersonal, String duiPersonal) {
        this.numeroAparatoPersonal = numeroAparatoPersonal;
        this.duiPersonal=duiPersonal;
    }

    public int getNumeroAparatoDocente() {
        return numeroAparatoPersonal;
    }

    public void setNumeroAparatoDocente(int numeroAparatoPersonal) {
        this.numeroAparatoPersonal = numeroAparatoPersonal;
    }

    public String getDocenteDui() {
        return duiPersonal;
    }

    public void setDocenteDui(String duiPersonal) {
        this.duiPersonal = duiPersonal;
    }
}
