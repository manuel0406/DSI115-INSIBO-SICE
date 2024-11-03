package com.dsi.insibo.sice.Biblioteca.Service;

import java.util.List;

import com.dsi.insibo.sice.entity.EntregaPapeleria;

public interface EntregaPapeleriaService {
    public List<EntregaPapeleria> listarEntregas();
    public void guardar(EntregaPapeleria entregaPapeleria);
    public EntregaPapeleria buscarPorId(Integer idEntregaPapeleria);
    public void eliminar(Integer idEntregaPapeleria);
}
