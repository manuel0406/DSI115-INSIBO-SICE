package com.dsi.insibo.sice.Biblioteca.Service;

import java.util.List;

import com.dsi.insibo.sice.entity.PrestamoLibro;

public interface PrestamoLibroService {
public List<PrestamoLibro> listarPrestamos();
    public void guardar(PrestamoLibro prestamoLibro);
    public PrestamoLibro buscarPorId(Integer idPrestamoLibro);
    public void eliminar(Integer idPrestamoLibro);
}
