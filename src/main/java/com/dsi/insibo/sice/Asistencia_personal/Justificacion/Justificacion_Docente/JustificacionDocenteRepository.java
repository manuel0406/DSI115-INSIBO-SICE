package com.dsi.insibo.sice.Asistencia_personal.Justificacion.Justificacion_Docente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dsi.insibo.sice.entity.JustificacionDocente;

public interface JustificacionDocenteRepository extends JpaRepository<JustificacionDocente, Integer> {
    //verificar existencia
    boolean existsByAsistenciaDocente_IdAsistencia(int idAsistencia);
    Optional<JustificacionDocente> findByAsistenciaDocente_IdAsistencia(int idAsistencia);
}
