package com.dsi.insibo.sice.Expediente_alumno;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Sancion;
@Repository
public interface SancionesRepository extends JpaRepository<Sancion, Integer>{
    
    @Query("SELECT s FROM Sancion s WHERE s.alumno.idAlumno=:nie")
    public List<Sancion> findAll(@Param("nie") int nie);
}
