package com.dsi.insibo.sice.Asistencia_personal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

import com.dsi.insibo.sice.entity.AsistenciaDocente;

@Repository
public interface AsistenciaDocenteRepository extends JpaRepository<AsistenciaDocente, Integer> {
    @Query(value = "SELECT d.nombre_docente, d.apellido_docente, ad.* " +
                   "FROM asistencia_docente ad " +
                   "JOIN docente d ON ad.dui_docente = d.dui_docente", nativeQuery = true)
    List<Object[]> findAsistenciaDocente();

    @Query(value = "SELECT d.nombre_docente, d.apellido_docente, ad.hora_entrada, ad.hora_salida " +
                   "FROM public.asistencia_docente ad " +
                   "JOIN public.docente d ON ad.dui_docente = d.dui_docente " +
                   "WHERE ((extract(hour from ad.hora_entrada) = 7 AND extract(minute from ad.hora_entrada) >= 30) " +
                   "OR (extract(hour from ad.hora_entrada) > 7 AND extract(hour from ad.hora_entrada) < 13) " +
                   "OR (extract(hour from ad.hora_entrada) = 13 AND extract(minute from ad.hora_entrada) >= 30) " +
                   "OR (extract(hour from ad.hora_entrada) > 13))", nativeQuery = true)
    List<Object[]> findAsistenciaDocenteByHorario();

}

