package com.dsi.insibo.sice.Calificaciones;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Actividad;
@Repository
public interface CalificacionesRepository extends JpaRepository<Actividad, Integer> {
    
}