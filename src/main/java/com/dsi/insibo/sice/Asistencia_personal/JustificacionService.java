package com.dsi.insibo.sice.Asistencia_personal;

import com.dsi.insibo.sice.entity.Justificacion;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JustificacionService {
    
    @Autowired
    private JustificacionRepository justificacionRepository;
    
    public List<Justificacion> listarAlumnos(String carrera, String grado, String seccion){
        return justificacionRepository.findAll();
    }

    public void guardarJustificacion(Justificacion justificacion) {
        justificacionRepository.save(justificacion);
    }

    public Justificacion buscarPorIdJustificacion(int id){

        return justificacionRepository.findById(id).orElse(null);

    }

}
