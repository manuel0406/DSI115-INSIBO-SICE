package com.dsi.insibo.sice.Calificaciones;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Materia;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, String> {
}