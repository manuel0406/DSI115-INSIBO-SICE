package com.dsi.insibo.sice.Asistencia_personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.dsi.insibo.sice.entity.AsistenciaDocente;

@Service
public class AsistenciaDocenteService {

     @Autowired
     private AsistenciaDocenteRepository asistenciaDocenteRepository;

     public List<AsistenciaDocente> listarAsistencia() {
         return asistenciaDocenteRepository.findAll();
     }
     public void guardarAsistencia(AsistenciaDocente asistenciaDocente){
        asistenciaDocenteRepository.save(asistenciaDocente);
     }
     
     public List<Object[]> getAsistenciaDocente() {
      return asistenciaDocenteRepository.findAsistenciaDocente();
    }

    public List<Object[]> getAsistenciaDocenteByHorario() {
        return asistenciaDocenteRepository.findAsistenciaDocenteByHorario();
    }
 }