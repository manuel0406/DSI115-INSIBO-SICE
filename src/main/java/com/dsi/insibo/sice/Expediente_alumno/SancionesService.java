package com.dsi.insibo.sice.Expediente_alumno;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Sancion;

@Service
public class SancionesService {

    @Autowired
    private SancionesRepository sancionesRepository;

    public void guardarSancion(Sancion sancion){
        sancionesRepository.save(sancion);
    }
    public Sancion buscarSancionPorId(int id){
        return sancionesRepository.findById(id).orElse(null);
    }

    public List<Sancion> buscarSancionAlumno(int nie){

        return sancionesRepository.findAll(nie);
    }

    public void EliminarSancion(int id){
        sancionesRepository.deleteById(id);
    }
}
