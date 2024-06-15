package com.dsi.insibo.sice.Expediente_docente.Docentes;

import java.time.LocalDate;
import java.time.Period;
import java.sql.Date;

import com.dsi.insibo.sice.entity.Docente;

public class DocenteDTO {
    private Docente docente;
    private int edad;

    public DocenteDTO(Docente docente) {
        this.docente = docente;
        Date fechaNacimiento = docente.getFechaNacimientoD();
        
        // Verificar si la fecha de nacimiento es nula o incorrecta
        if (fechaNacimiento == null) {
            this.edad = 0;
        } else {
            // Convertir java.sql.Date a LocalDate
            LocalDate fechaNacimientoLocal = fechaNacimiento.toLocalDate();
            if (fechaNacimientoLocal.isAfter(LocalDate.now())) {
                this.edad = 0;
            } else {
                // Calcular la edad normalmente si la fecha de nacimiento es válida
                this.edad = Period.between(fechaNacimientoLocal, LocalDate.now()).getYears();
            }
        }
    }

    public Docente getDocente() {
        return docente;
    }

    public int getEdad() {
        return edad;
    }
}

