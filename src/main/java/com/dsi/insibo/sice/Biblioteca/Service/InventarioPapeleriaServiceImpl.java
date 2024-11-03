package com.dsi.insibo.sice.Biblioteca.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Biblioteca.Repository.InventarioPapeleriaRepository;
import com.dsi.insibo.sice.entity.InventarioPapeleria;

@Service
public class InventarioPapeleriaServiceImpl implements InventarioPapeleriaService {

    @Autowired
    private InventarioPapeleriaRepository inventarioPapeleriaRepository;

    @Override
    public List<InventarioPapeleria> listarProductos(){
        return (List<InventarioPapeleria>) inventarioPapeleriaRepository.findAll();
    }

    @Override
    public void guardar(InventarioPapeleria inventarioPapeleria){
        inventarioPapeleriaRepository.save(inventarioPapeleria);
    }

    @Override
    public InventarioPapeleria buscarPorId(Integer idArticulo){
        return inventarioPapeleriaRepository.findById(idArticulo).orElse(null);
    }

    @Override
    public void eliminar(Integer idArticulo){
        inventarioPapeleriaRepository.deleteById(idArticulo);
    }

    @Override
    public void actualizarProducto(InventarioPapeleria producto) {
        inventarioPapeleriaRepository.save(producto);
    }
}
