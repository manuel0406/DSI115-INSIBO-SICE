package com.dsi.insibo.sice.Biblioteca.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Biblioteca.Repository.PrestamoLibroRepository;
import com.dsi.insibo.sice.entity.PrestamoLibro;

@Service
public class PrestamoLibroServiceImpl implements PrestamoLibroService{
    @Autowired
    private PrestamoLibroRepository prestamoLibroRepository;

    @Override
    public List<PrestamoLibro> listarPrestamos(){
        return(List<PrestamoLibro>) prestamoLibroRepository.findAll();
    }

    @Override
    public void guardar(PrestamoLibro prestamoLibro){
        prestamoLibroRepository.save(prestamoLibro);
    }

    @Override
    public PrestamoLibro buscarPorId(Integer idPrestamoLibro){
        return prestamoLibroRepository.findById(idPrestamoLibro).orElse(null);
    }

    @Override
    public void eliminar(Integer idPrestamoLibro){
        prestamoLibroRepository.deleteById(idPrestamoLibro);
    }
}
