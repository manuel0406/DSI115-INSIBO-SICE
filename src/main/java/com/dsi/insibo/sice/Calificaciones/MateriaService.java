package com.dsi.insibo.sice.Calificaciones;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Materia;

@Service
public class MateriaService {
    @Autowired
    private MateriaRepository materiaRepository;

    public List<Materia> listaMaterias(){
        return (List<Materia>) materiaRepository.findAll();
    }
}