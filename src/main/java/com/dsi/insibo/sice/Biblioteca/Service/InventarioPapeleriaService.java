package com.dsi.insibo.sice.Biblioteca.Service;

import java.util.List;

import com.dsi.insibo.sice.entity.InventarioPapeleria;

public interface InventarioPapeleriaService {
    public List<InventarioPapeleria> listarProductos();
    public void guardar(InventarioPapeleria inventarioPapeleria);
    public InventarioPapeleria buscarPorId(Integer idArticulo);
    public void eliminar(Integer idArticulo);
    public void actualizarProducto(InventarioPapeleria producto);
    
}
