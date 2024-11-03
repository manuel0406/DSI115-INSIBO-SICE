package com.dsi.insibo.sice.Horario.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.dsi.insibo.sice.entity.AsignacionHorario;

public interface HorarioRepository extends CrudRepository<AsignacionHorario, Integer> {
    @Query("SELECT ah.horarioBase.idHorarioBase FROM AsignacionHorario ah WHERE ah.asignacion.bachillerato.codigoBachillerato = :codigoBachillerato AND ah.asignacion.bachillerato.anioAcademico.activoAnio = true")
    List<Integer> findIdHorarioBaseByCodigoBachilleratoAndActivoAnioTrue(int codigoBachillerato);

    @Query("SELECT ah FROM AsignacionHorario ah WHERE ah.asignacion.bachillerato.codigoBachillerato = :codigoBachillerato AND ah.asignacion.bachillerato.anioAcademico.activoAnio = true ORDER BY ah.horarioBase.idHorarioBase ASC")
    List<AsignacionHorario> findHorarioByCodigoBachilleratoAndActivoAnioTrue(int codigoBachillerato);

    @Query("SELECT ah FROM AsignacionHorario ah WHERE ah.asignacion.docente.duiDocente = :duiDocente AND ah.asignacion.bachillerato.anioAcademico.activoAnio = true ORDER BY ah.horarioBase.idHorarioBase ASC")
    List<AsignacionHorario> findHorarioByDuiDocenteAndActivoAnioTrue(String duiDocente);

    @Query("SELECT ah FROM AsignacionHorario ah " +
            "WHERE ah.asignacion.bachillerato.codigoBachillerato = :codigoBachillerato " +
            "AND ah.horarioBase.dia.nombreDia = :nombreDia " +
            "AND ah.horarioBase.hora.idHora BETWEEN :idHora - 2 AND :idHora + 2 " +
            "AND ah.asignacion.idAsignacion = :idAsignacion " +
            "AND ah.asignacion.materia.tipoMateria = 'Básica' " +
            "AND ah.asignacion.bachillerato.anioAcademico.activoAnio = true " +
            "ORDER BY ah.horarioBase.idHorarioBase ASC")
    List<AsignacionHorario> findBloqueDeDosHoras(int codigoBachillerato, String nombreDia, int idHora,
            int idAsignacion);

}