package com.dsi.insibo.sice.Calificaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Materia;
import com.dsi.insibo.sice.entity.NotaPeriodo;

@Repository
public interface NotaPeriodoRepository extends JpaRepository<NotaPeriodo, Integer> {

        @Query("SELECT n FROM NotaPeriodo n WHERE n.alumno.idAlumno=:idAlumno AND n.periodo.numeroPeriodo=:periodo AND n.asignacion.materia.idMateria=:idMateria AND n.asignacion.bachillerato.codigoBachillerato=:codigoBachillerato AND n.asignacion.docente.duiDocente=:duiDocente")
        Optional<NotaPeriodo> notaPeriodoAlumno(@Param("idAlumno") int idAlumno, @Param("periodo") String periodo,
                        @Param("idMateria") int idMateria, @Param("codigoBachillerato") int codigoBachillerato,
                        @Param("duiDocente") String duiDocente);

        @Query("SELECT n FROM NotaPeriodo n WHERE n.alumno.idAlumno=:idAlumno AND n.asignacion.materia.idMateria=:idMateria AND n.asignacion.bachillerato.codigoBachillerato=:codigoBachillerato ")
        List<NotaPeriodo> notaPeriodoAlumnos(@Param("idAlumno") int idAlumno,
                        @Param("idMateria") int idMateria, @Param("codigoBachillerato") int codigoBachillerato);

        Optional<NotaPeriodo> findByAlumnoAndPeriodoNumeroPeriodoAndAsignacionMateria(Alumno alumno, int numeroPeriodo,
                        Materia materia);

        @Query("SELECT n FROM NotaPeriodo n WHERE n.asignacion.materia.idMateria=:idMateria AND n.asignacion.bachillerato.codigoBachillerato=:codigoBachillerato AND n.asignacion.docente.duiDocente=:duiDocente ")
        List<NotaPeriodo> notaPeriodoMateria(@Param("idMateria") int idMateria,
                        @Param("codigoBachillerato") int codigoBachillerato, @Param("duiDocente") String duiDocente);

}
