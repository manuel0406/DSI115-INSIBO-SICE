package com.dsi.insibo.sice.Biblioteca.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Biblioteca.Repository.InventarioLibroRepository;
import com.dsi.insibo.sice.entity.InventarioLibro;

@Service
public class InventarioLibroServiceImpl implements InventarioLibroService{
    @Autowired
    private InventarioLibroRepository inventarioLibroRepository;
    
    @Override
    public List<InventarioLibro> listarLibros(){
        return(List<InventarioLibro>) inventarioLibroRepository.findAll();
    }

    @Override
    public void guardar(InventarioLibro inventarioLibro){
        inventarioLibroRepository.save(inventarioLibro);
    }

    @Override
    public InventarioLibro buscarPorId(Integer idInventarioLibros){
        return inventarioLibroRepository.findById(idInventarioLibros).orElse(null);
    }

    @Override
    public void eliminar(Integer idInventarioLibros){
        inventarioLibroRepository.deleteById(idInventarioLibros);
    }
    @Override
    public boolean existePorTituloYAutor(String tituloLibro, String autorLibro) {
        return inventarioLibroRepository.existsByTituloLibroAndAutorLibro(tituloLibro, autorLibro);
    }
}
