package com.dsi.insibo.sice.Asistencia_personal.Justificacion.Justificacion_Personal;
import com.dsi.insibo.sice.entity.JustificacionPersonal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JustificacionPersonalRepository extends JpaRepository<JustificacionPersonal, Integer>{
    //verificar existencia
    boolean existsByAsistenciaPersonal_IdAsistenciaPersonal(int idAsistenciaPersonal);
    Optional<JustificacionPersonal> findByAsistenciaPersonal_IdAsistenciaPersonal(int idAsistenciaPersonal);
}
