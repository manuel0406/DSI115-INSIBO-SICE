package com.dsi.insibo.sice.Paquetes_escolar.Uniforme;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Uniforme;

@Repository
public interface UniformeRepository extends JpaRepository<Uniforme, Integer> {
    Uniforme findByAlumno(Alumno alumno);

    @Query(value = "SELECT DISTINCT u.fecha_entrega_uniforme FROM public.uniforme u JOIN public.alumno a ON u.alumno_id_alumno = a.id_alumno WHERE a.bachillerato_codigo_bachillerato = :idBachillerato", nativeQuery = true)
    List<String> findDistinctFechasByBachillerato(@Param("idBachillerato") int idBachillerato);

    @Query(value = "SELECT a.id_alumno, a.apellido_alumno, a.nie, a.nombre_alumno, u.fecha_entrega_uniforme , u.uniforme_entegado, u.id_uniforme, u.talla_uniforme "
            + "FROM alumno a JOIN uniforme u ON a.id_alumno = u.alumno_id_alumno "
            + "WHERE a.bachillerato_codigo_bachillerato = :idBachillerato "
            + "AND u.fecha_entrega_uniforme = CAST(:fecha AS DATE)"
            + "AND u.uniforme_entegado = :estado ORDER BY a.apellido_alumno ASC", nativeQuery = true)
    List<Object[]> filtrarPorUniforme(@Param("idBachillerato") int idBachillerato,
            @Param("fecha") String fecha,
            @Param("estado") Boolean estado);

    @Query(value = "SELECT a.id_alumno, a.apellido_alumno, a.nie, a.nombre_alumno, u.fecha_entrega_uniforme , u.uniforme_entegado, u.talla_uniforme, u.id_uniforme "
            + "FROM alumno a JOIN uniforme u ON a.id_alumno = u.alumno_id_alumno "
            + "WHERE a.bachillerato_codigo_bachillerato = :idBachillerato "
            + "AND u.fecha_entrega_uniforme = CAST(:fecha AS DATE) ORDER BY a.apellido_alumno ASC", nativeQuery = true)
    List<Object[]> filtrarPorUniformesinEstado(@Param("idBachillerato") int idBachillerato,
            @Param("fecha") String fecha);

}
