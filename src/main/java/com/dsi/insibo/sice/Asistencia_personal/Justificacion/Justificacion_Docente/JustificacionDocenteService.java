package com.dsi.insibo.sice.Asistencia_personal.Justificacion.Justificacion_Docente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.JustificacionDocente;

@Service
public class JustificacionDocenteService {
    @Autowired
    private JustificacionDocenteRepository justificacionDocenteRepository;

    public JustificacionDocente save(JustificacionDocente justificacionDocente) {
        return justificacionDocenteRepository.save(justificacionDocente);
    }

    // Método para verificar si existe una justificación para un ID de asistencia
    // específico
    public boolean existsByAsistenciaDocente(int idAsistencia) {
        return justificacionDocenteRepository.existsByAsistenciaDocente_IdAsistencia(idAsistencia);
    }

    // Método para obtener una justificación por su ID
    public JustificacionDocente findByAsistenciaDocente_IdAsistencia(int idAsistencia) {
        return justificacionDocenteRepository.findByAsistenciaDocente_IdAsistencia(idAsistencia)
                .orElseThrow(() -> new RuntimeException(
                        "Justificación no encontrada para la asistencia con ID: " + idAsistencia));
    }
    public JustificacionDocente findById(int idJustificacion) {
        return justificacionDocenteRepository.findById(idJustificacion)
                .orElseThrow(() -> new RuntimeException("Justificación no encontrada con ID: " + idJustificacion));
    }
}