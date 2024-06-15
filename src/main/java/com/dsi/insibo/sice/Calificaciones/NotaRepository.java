package com.dsi.insibo.sice.Calificaciones;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Nota;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {
    @Query("SELECT n FROM Nota n " +
           "WHERE n.alumno.nie = :nie " +
           "AND n.actividad.materia.codMateria = :codMateria " +
           "AND n.actividad.periodo.idPeriodo = :idPeriodo")
    List<Nota> findByAlumnoNieAndActividadMateriaCodMateriaAndActividadPeriodoId(int nie, String codMateria, int idPeriodo);
}
