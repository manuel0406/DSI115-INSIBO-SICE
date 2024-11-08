package com.dsi.insibo.sice.Expediente_alumno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.AnexoAlumno;

import jakarta.transaction.Transactional;

@Service
public class AnexoAlumnoService {

    @Autowired
    private AnexoAlumnoRepository anexoRepository;

    public void guardaAnexo(AnexoAlumno anexoAlumno){

        anexoRepository.save(anexoAlumno);

    }

    public AnexoAlumno buscarAnexoPorId(int id){
        return anexoRepository.findById(id).orElse(null);
    }
    
    public AnexoAlumno buscarAlumno(int idAlumno){      
        return  anexoRepository.findByAlumnoNie(idAlumno);
    }

    @Transactional
    public void eliminarAnexoAlumno(int nie) {
        anexoRepository.deleteByAlumnoNie(nie);
    }
}
