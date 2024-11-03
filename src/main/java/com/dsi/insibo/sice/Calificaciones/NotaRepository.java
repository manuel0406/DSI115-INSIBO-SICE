package com.dsi.insibo.sice.Calificaciones;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Nota;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {

        @Query("SELECT n FROM Nota n WHERE  n.actividad.idActividad=:idActividad ORDER BY n.alumno.apellidoAlumno ASC")
        List<Nota> notasActividad(@Param("idActividad") int idActividad);

        @Modifying
        @Query("DELETE FROM Nota n WHERE n.alumno.nie = :nie ")
        void deleteByAlumnoNie(int nie);

        @Query("SELECT n FROM Nota n WHERE n.actividad.asignacion.docente.duiDocente=:dui AND n.actividad.asignacion.bachillerato.codigoBachillerato=:codigoBachillerato ORDER BY n.actividad.periodo.numeroPeriodo ASC")
        List<Nota> findAll(@Param("dui") String dui, @Param("codigoBachillerato") int codigoBachillerato);

        @Query("SELECT n FROM Nota n WHERE n.actividad.asignacion.docente.duiDocente=:dui AND n.actividad.asignacion.bachillerato.codigoBachillerato=:codigoBachillerato AND n.actividad.periodo.numeroPeriodo=:periodo AND n.actividad.asignacion.materia.idMateria=:idMateria ORDER BY n.actividad.tipoActividad ASC")
        List<Nota> notasPeriodo(@Param("dui") String dui, @Param("codigoBachillerato") int codigoBachillerato,
                        @Param("periodo") String periodo, @Param("idMateria") int idMateria);

        @Modifying
        @Query("DELETE FROM Nota n WHERE n.actividad.idActividad = :idActividad ")
        void deleteByActividad(int idActividad);

        List<Nota> findByAlumno(Alumno alumno);

        @Query("SELECT n FROM Nota n " +
                        "JOIN n.actividad a " +
                        "JOIN a.asignacion asg " +
                        "JOIN asg.materia m " +
                        "JOIN a.periodo p " +
                        "WHERE n.alumno = :alumno " +
                        "ORDER BY p.numeroPeriodo, m.nomMateria, a.nombreActividad")
        List<Nota> findNotasByAlumnoGroupedByMateriaAndPeriodo(@Param("alumno") Alumno alumno);

        @Query("SELECT n FROM Nota n WHERE n.actividad.asignacion.docente.duiDocente=:dui AND n.actividad.asignacion.bachillerato.codigoBachillerato=:codigoBachillerato AND n.actividad.periodo.numeroPeriodo=:periodo AND n.actividad.asignacion.materia.idMateria=:idMateria AND n.alumno.idAlumno=:idAlumno ORDER BY n.actividad.tipoActividad ASC")
        List<Nota> notasPeriodoAlumno(@Param("dui") String dui, @Param("codigoBachillerato") int codigoBachillerato,
                        @Param("periodo") String periodo, @Param("idMateria") int idMateria, @Param("idAlumno") int idAlumno);

}
