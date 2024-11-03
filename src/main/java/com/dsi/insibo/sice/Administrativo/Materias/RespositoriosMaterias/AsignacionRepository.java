package com.dsi.insibo.sice.Administrativo.Materias.RespositoriosMaterias;

import com.dsi.insibo.sice.entity.Asignacion;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AsignacionRepository extends JpaRepository<Asignacion, Integer> {

    @Query("SELECT a FROM Asignacion a WHERE a.materia.idMateria = :idMateria AND a.materia.activoMateria = true ORDER BY a.docente.nombreDocente ASC, a.docente.apellidoDocente ASC")
    List<Asignacion> obtenerAsignacionExistente(int idMateria);

    @Query("SELECT a.docente.duiDocente FROM Asignacion a "
            + "WHERE a.materia.idMateria != :idMateria AND a.materia.activoMateria = true "
            + "GROUP BY a.docente.duiDocente "
            + "HAVING COUNT(DISTINCT a.materia.idMateria) = 3")
    List<String> findDocentesWithThreeDistinctMaterias(int idMateria);

    @Query("SELECT a FROM Asignacion a WHERE a.docente.duiDocente = :duiDocente AND a.materia.idMateria = :idMateria AND a.materia.activoMateria = true")
    List<Asignacion> findByDocenteAndMateria(String duiDocente, int idMateria);

    @Query("SELECT a FROM Asignacion a WHERE a.materia.idMateria = :idMateria AND a.materia.activoMateria = true " 
            + "ORDER BY a.docente.nombreDocente ASC, "
            + "a.docente.apellidoDocente ASC, a.bachillerato.grado ASC, "
            + "a.bachillerato.seccion ASC")
    List<Asignacion> findByMateria(int idMateria);

    @Query("SELECT a FROM Asignacion a WHERE a.materia.idMateria = :idMateria AND a.materia.activoMateria = true "
            + "ORDER BY a.docente.nombreDocente ASC, "
            + "a.docente.apellidoDocente ASC, a.bachillerato.grado ASC, "
            + "a.bachillerato.seccion ASC")
    Page<Asignacion> findByMateria(int idMateria, Pageable pageable);

    // OBTENER TODAS LAS ASIGNACIONES
    @Query("SELECT a FROM Asignacion a WHERE a.materia.activoMateria = true "
            + "ORDER BY a.docente.nombreDocente ASC, a.materia.nomMateria ASC, "
            + "a.docente.apellidoDocente ASC, a.bachillerato.grado ASC, "
            + "a.bachillerato.seccion ASC")
    List<Asignacion> findAllAsignaciones();

    // OBTENER TODAS LAS ASIGNACIONES DADA UNA SECCIÓN
    @Query("SELECT a FROM Asignacion a WHERE a.bachillerato.codigoBachillerato = :codigoBachillerato")
    List<Asignacion> findByCodigoBachillerato(Integer codigoBachillerato);

    // OBTENER TODAS LAS ASIGNACIONES
    @Query("SELECT a FROM Asignacion a WHERE a.materia.activoMateria = true "
            + "ORDER BY a.docente.nombreDocente ASC, a.materia.nomMateria ASC, "
            + "a.docente.apellidoDocente ASC, a.bachillerato.grado ASC, "
            + "a.bachillerato.seccion ASC")
    Page<Asignacion> findAllAsignaciones(Pageable pageable);

    // Obtener asignación filtrado
    @Query("SELECT a FROM Asignacion a WHERE a.materia.idMateria = :idMateria AND a.materia.activoMateria = true AND a.docente.duiDocente=:dui AND a.bachillerato.codigoBachillerato=:codigoBachillerato")
    Asignacion asignaciónDocenteBachillerato(@Param("dui") String dui, @Param("idMateria") int idMateria,
            @Param("codigoBachillerato") int codigoBachillerato);

    // Obtener todas las asignaciones dado un docente
    @Query("SELECT a FROM Asignacion a WHERE  a.docente.duiDocente = :duiDocente AND a.materia.activoMateria = true")
    List<Asignacion> findByDocente(String duiDocente);

    // Obtener asignación filtrado
    @Query("SELECT a FROM Asignacion a WHERE a.materia.idMateria = :idMateria  AND a.docente.duiDocente=:dui AND a.bachillerato.codigoBachillerato=:codigoBachillerato AND a.materia.activoMateria = true")
    Asignacion asignaciónMateriaBachillerato(@Param("dui") String dui, @Param("idMateria") String idMateria, @Param("codigoBachillerato") int codigoBachillerato);
}
