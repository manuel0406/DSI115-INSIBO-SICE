package com.dsi.insibo.sice.Biblioteca.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dsi.insibo.sice.entity.InventarioLibro;

public interface InventarioLibroRepository extends JpaRepository<InventarioLibro, Integer>{
    // Consulta que verifica si ya existe un libro con el mismo título y autor
    boolean existsByTituloLibroAndAutorLibro(String tituloLibro, String autorLibro);

}
