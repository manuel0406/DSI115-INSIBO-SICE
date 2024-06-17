package com.dsi.insibo.sice.Calificaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Actividad;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Nota;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {
    List<Nota> findByAlumnoAndActividad(Alumno alumno, Actividad actividad);

    List<Nota> findByAlumno_Bachillerato_CodigoBachillerato(String codigoBachillerato);

    List<Nota> findByAlumnoNieAndActividadMateriaCodMateriaAndActividadPeriodoIdPeriodo(int nie, String codMateria, int idPeriodo);

    List<Nota> findByAlumno(Alumno alumno);
    
    
    @Modifying
    @Query("DELETE FROM Nota n WHERE n.alumno.nie = :nie ")
    void deleteByAlumnoNie(int nie);
}
