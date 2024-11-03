package com.dsi.insibo.sice.Administrativo.Materias.RespositoriosMaterias;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dsi.insibo.sice.entity.Materia;

public interface MateriasRepository extends JpaRepository<Materia, Integer> {
    // Obtener todas las materias.
    @Query("SELECT m FROM Materia m ORDER BY m.codMateria ASC ")
    List<Materia> obtenerMaterias();

    @Query("SELECT m FROM Materia m WHERE m.activoMateria = true ORDER BY m.codMateria ASC")
    Page<Materia> obtenerMateriasConPaginado(Pageable pageable);

    @Query("SELECT m FROM Materia m WHERE m.tipoMateria = :tipo AND m.activoMateria = true ORDER BY m.codMateria ASC")
    List<Materia> findMateriasByType(String tipo);

    @Query("SELECT m FROM Materia m WHERE m.tipoMateria = :tipo AND m.activoMateria = true ORDER BY m.codMateria ASC")
    Page<Materia> findMateriasByType(String tipo, Pageable pageable);
}
