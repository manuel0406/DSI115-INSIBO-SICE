package com.dsi.insibo.sice.Calificaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Actividad;

import jakarta.transaction.Transactional;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private NotaRepository notaRepository;

    public void guardarActividad(Actividad actividad) {
        actividadRepository.save(actividad);
    }

    public List<Actividad> listaActividades(String dui, int idMateria, String periodo, int codigoBachillerato) {
        return (List<Actividad>) actividadRepository.actividadesPorEspecialidadPeriodo(dui, idMateria,
                periodo, codigoBachillerato);
    }

    public List<Actividad> listaActividades(String dui, int idMateria) {
        return (List<Actividad>) actividadRepository.actividadesPorEspecialidad(dui, idMateria);
    }

    @Transactional
    public void eliminarActividad(int idActividad) {
        notaRepository.deleteByActividad(idActividad);// se elimina las notas relacionadas a esa actividad
        actividadRepository.deleteById(idActividad);
    }

    public Actividad buscarActividadPorId(int idActividad) {
        return actividadRepository.findById(idActividad).orElse(null);
    }

    // Método para calcular la ponderación total de todas las actividades menos la
    // actual
    public float calcularTotalPonderacion(Actividad actividad) {
        List<Actividad> actividades = listaActividades(
                actividad.getAsignacion().getDocente().getDuiDocente(),
                actividad.getAsignacion().getMateria().getIdMateria(),
                String.valueOf(actividad.getPeriodo().getIdPeriodo()),
                actividad.getAsignacion().getBachillerato().getCodigoBachillerato());

        float total = actividad.getPonderacionActividad(); // incluir la actividad actual
        for (Actividad actividadExistente : actividades) {
            if (actividadExistente.getIdActividad() != actividad.getIdActividad()) {
                total += actividadExistente.getPonderacionActividad();
            }
        }
        return total;
    }

    // Método para calcular la ponderación de un tipo específico de actividad
    public float calcularPonderacionPorTipo(Actividad actividad, String tipo) {
        List<Actividad> actividades = listaActividades(
                actividad.getAsignacion().getDocente().getDuiDocente(),
                actividad.getAsignacion().getMateria().getIdMateria(),
                String.valueOf(actividad.getPeriodo().getIdPeriodo()),
                actividad.getAsignacion().getBachillerato().getCodigoBachillerato());

        float total = 0;
        for (Actividad actividadExistente : actividades) {
            if (actividadExistente.getTipoActividad().equals(tipo)
                    && actividadExistente.getIdActividad() != actividad.getIdActividad()) {
                total += actividadExistente.getPonderacionActividad();
            }
        }

        if (actividad.getTipoActividad().equals(tipo)) {
            total += actividad.getPonderacionActividad();
        }
        return total;
    }
}