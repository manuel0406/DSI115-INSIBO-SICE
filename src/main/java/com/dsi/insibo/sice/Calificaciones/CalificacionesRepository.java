package com.dsi.insibo.sice.Calificaciones;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Actividad;
@Repository
public interface CalificacionesRepository extends JpaRepository<Actividad, Integer> {
    
}