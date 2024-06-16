package com.dsi.insibo.sice.Calificaciones;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Actividad;

@Service
public class ActividadService {
    @Autowired
    private ActividadRepository actividadRepository;

    public List<Actividad> listaActividades(){
        return (List<Actividad>) actividadRepository.findAll();
    }

    public List<Actividad> findActividadesByMateriaCodMateria(String codMateria) {
        return actividadRepository.findByMateriaCodMateria(codMateria);
    }

    public List<Actividad> buscarActividadesPorMateriaYPeriodo(String codMateria, int idPeriodo) {
        return actividadRepository.findByMateriaCodMateriaAndPeriodoIdPeriodo(codMateria, idPeriodo);
    }
}