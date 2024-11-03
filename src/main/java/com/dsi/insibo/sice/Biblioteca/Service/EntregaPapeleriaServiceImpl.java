package com.dsi.insibo.sice.Biblioteca.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Biblioteca.Repository.EntregaPapeleriaRepository;
import com.dsi.insibo.sice.entity.EntregaPapeleria;

@Service
public class EntregaPapeleriaServiceImpl implements EntregaPapeleriaService {
    @Autowired
    private EntregaPapeleriaRepository entregaPapeleriaRepository;

    @Override
    public List<EntregaPapeleria> listarEntregas(){
        return(List<EntregaPapeleria>) entregaPapeleriaRepository.findAll();
    }

    @Override
    public void guardar(EntregaPapeleria entregaPapeleria){
        entregaPapeleriaRepository.save(entregaPapeleria);
    }

    @Override
    public EntregaPapeleria buscarPorId(Integer idEntregaPapeleria){
        return entregaPapeleriaRepository.findById(idEntregaPapeleria).orElse(null);
    }

    @Override
    public void eliminar(Integer idEntregaPapeleria){
        entregaPapeleriaRepository.deleteById(idEntregaPapeleria);
    }
}
