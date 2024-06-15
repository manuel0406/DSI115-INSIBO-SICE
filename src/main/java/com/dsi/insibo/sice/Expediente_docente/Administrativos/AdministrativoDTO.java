package com.dsi.insibo.sice.Expediente_docente.Administrativos;

import java.time.LocalDate;
import java.time.Period;
import java.sql.Date; // Importar java.sql.Date

import com.dsi.insibo.sice.entity.PersonalAdministrativo;

public class AdministrativoDTO {
    private PersonalAdministrativo administrativo;
    private int edad;

    public AdministrativoDTO(PersonalAdministrativo administrativo) {
        this.administrativo = administrativo;
        Date fechaNacimiento = administrativo.getFechaNacimientoP();
        
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

    public PersonalAdministrativo getAdministrativo() {
        return administrativo;
    }

    public int getEdad() {
        return edad;
    }
}
