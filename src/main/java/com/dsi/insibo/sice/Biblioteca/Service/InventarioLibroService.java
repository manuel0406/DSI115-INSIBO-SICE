package com.dsi.insibo.sice.Biblioteca.Service;

import java.util.List;

import com.dsi.insibo.sice.entity.InventarioLibro;

public interface InventarioLibroService {
    public List<InventarioLibro> listarLibros();
    public void guardar(InventarioLibro inventarioLibro);
    public InventarioLibro buscarPorId(Integer idInventarioLibros);
    public void eliminar(Integer idInventarioLibros);
    public boolean existePorTituloYAutor(String tituloLibro, String autorLibro);
}
