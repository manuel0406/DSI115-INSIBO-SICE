package com.dsi.insibo.sice.Paquetes_escolar.Utiles;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.UtilesEscolares;

@Repository
public interface UtilesRepository extends JpaRepository<UtilesEscolares, Integer> {
     UtilesEscolares findByAlumno(Alumno alumno);

     @Query(value = "SELECT DISTINCT e.fecha_entrega_utiles FROM public.utiles_escolares e JOIN public.alumno a ON e.alumno_id_alumno = a.id_alumno WHERE a.bachillerato_codigo_bachillerato = :idBachillerato", nativeQuery = true)
     List<String> findDistinctFechasByBachillerato(@Param("idBachillerato") int idBachillerato);

     @Query(value = "SELECT a.id_alumno, a.apellido_alumno, a.nie, a.nombre_alumno, u.fecha_entrega_utiles, u.entregado, u.id_utiles "
               + "FROM alumno a JOIN utiles_escolares u ON a.id_alumno = u.alumno_id_alumno "
               + "WHERE a.bachillerato_codigo_bachillerato = :idBachillerato "
               + "AND u.fecha_entrega_utiles = CAST(:fecha AS DATE)"
               + "AND u.entregado = :estado ORDER BY a.apellido_alumno ASC", nativeQuery = true)
     List<Object[]> filtrarPorUtiles(@Param("idBachillerato") int idBachillerato,
               @Param("fecha") String fecha,
               @Param("estado") Boolean estado);

     @Query(value = "SELECT a.id_alumno, a.apellido_alumno, a.nie, a.nombre_alumno, u.fecha_entrega_utiles, u.entregado, u.id_utiles "
               + "FROM alumno a JOIN utiles_escolares u ON a.id_alumno = u.alumno_id_alumno "
               + "WHERE a.bachillerato_codigo_bachillerato = :idBachillerato "
               + "AND u.fecha_entrega_utiles = CAST(:fecha AS DATE) ORDER BY a.apellido_alumno ASC", nativeQuery = true)
     List<Object[]> filtrarPorUtilessinEstado(@Param("idBachillerato") int idBachillerato,
               @Param("fecha") String fecha);

}
