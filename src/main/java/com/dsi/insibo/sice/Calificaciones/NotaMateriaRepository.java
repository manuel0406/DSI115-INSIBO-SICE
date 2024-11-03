package com.dsi.insibo.sice.Calificaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dsi.insibo.sice.entity.NotaMateria;

@Repository
public interface NotaMateriaRepository extends JpaRepository<NotaMateria, Integer> {

        @Query("SELECT n FROM NotaMateria n WHERE n.alumno.idAlumno=:idAlumno AND n.asignacion.materia.idMateria=:idMateria AND n.asignacion.docente.duiDocente=:duiDocente AND n.asignacion.bachillerato.codigoBachillerato=:codigoBachillerato")
        Optional<NotaMateria> notaMateriaAlumno(@Param("idAlumno") int idAlumno, @Param("idMateria") int idMateria,
                        @Param("duiDocente") String duiDocente, @Param("codigoBachillerato") int codigoBachillerato);

        @Query("SELECT n FROM NotaMateria n WHERE n.alumno.idAlumno=:idAlumno AND n.asignacion.materia.idMateria=:idMateria AND n.asignacion.docente.duiDocente=:duiDocente AND n.asignacion.bachillerato.codigoBachillerato=:codigoBachillerato")
        List<NotaMateria> notaMateriaAlumnos(@Param("idAlumno") int idAlumno, @Param("idMateria") int idMateria,
                        @Param("duiDocente") String duiDocente, @Param("codigoBachillerato") int codigoBachillerato);

        @Query("SELECT n FROM NotaMateria n WHERE n.alumno.idAlumno=:idAlumno")
        List<NotaMateria> notaMateriaPorAlumno(@Param("idAlumno") int idAlumno);
}
