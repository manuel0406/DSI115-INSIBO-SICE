package com.dsi.insibo.sice.Asistencia_personal.Justificacion.Justificacion_Personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.JustificacionPersonal;

@Service
public class JustificacionPersonalService {
    @Autowired
    private JustificacionPersonalRepository justificacionPersonalRepository;

    public JustificacionPersonal save(JustificacionPersonal justificacionPersonal) {
        return justificacionPersonalRepository.save(justificacionPersonal);
    }

    // Método para verificar si existe una justificación para un ID de asistencia
    // específico
    public boolean existsByAsistenciaPersonal(int idAsistencia) {
        return justificacionPersonalRepository.existsByAsistenciaPersonal_IdAsistenciaPersonal(idAsistencia);
    }

    // Método para obtener una justificación por su ID
    public JustificacionPersonal findByAsistenciaPersonal_IdAsistencia(int idAsistencia) {
        return justificacionPersonalRepository.findByAsistenciaPersonal_IdAsistenciaPersonal(idAsistencia)
                .orElseThrow(() -> new RuntimeException(
                        "Justificación no encontrada para la asistencia con ID: " + idAsistencia));
    }

    public JustificacionPersonal findById(int idJustificacion) {
        return justificacionPersonalRepository.findById(idJustificacion)
                .orElseThrow(() -> new RuntimeException("Justificación no encontrada con ID: " + idJustificacion));
    }
}
