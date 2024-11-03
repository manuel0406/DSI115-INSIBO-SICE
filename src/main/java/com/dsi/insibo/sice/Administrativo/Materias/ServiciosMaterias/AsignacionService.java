package com.dsi.insibo.sice.Administrativo.Materias.ServiciosMaterias;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Administrativo.Materias.RespositoriosMaterias.AsignacionRepository;
import com.dsi.insibo.sice.entity.Asignacion;

@Service
public class AsignacionService {
    
    @Autowired
    private AsignacionRepository asignacionRepository;

    public void saveAsignacion(Asignacion asignacion) {
        asignacionRepository.save(asignacion);
    }

    public void saveAsignaciones(List<Asignacion> asignaciones) {
        asignacionRepository.saveAll(asignaciones);
    }

    public List<Asignacion> obtenerAsignacionExistente(int idMateria){
        return asignacionRepository.obtenerAsignacionExistente(idMateria);
    }

    public List<String> listarDocentesMaximo(int idMateria){
        return asignacionRepository.findDocentesWithThreeDistinctMaterias(idMateria);
    }

    public List<Asignacion> buscarDocenteMateria(int idMateria, String duiDocente){
        return asignacionRepository.findByDocenteAndMateria(duiDocente, idMateria);
    }

    public List<Asignacion> listarAsignaciones(int idMateria){
        return asignacionRepository.findByMateria(idMateria);
    }

    public Page<Asignacion> listarAsignaciones(int idMateria, int pagina, int tamanyo){
        Pageable pageable = PageRequest.of(pagina, tamanyo);
        return asignacionRepository.findByMateria(idMateria, pageable);
    }

    public Asignacion buscarAsignacion(int idAsignacion){
        return asignacionRepository.findById(idAsignacion).orElse(null);
    }

    public void eliminarAsignacion(Asignacion asignacion){
        asignacionRepository.delete(asignacion);
    }

    public List<Asignacion> obtenerTodaAsignaciones(){
        return asignacionRepository.findAllAsignaciones();
    }

    public List<Asignacion> listarAsignacionesCodigoBachillerato(Integer codigoBachillerato){
        return asignacionRepository.findByCodigoBachillerato(codigoBachillerato);
    }
    public Page<Asignacion> obtenerTodaAsignaciones(int pagina, int tamanyo){
        Pageable pageable = PageRequest.of(pagina, tamanyo);
        return asignacionRepository.findAllAsignaciones(pageable);
    }

    public Asignacion asignacionParaActividad(String dui, int idMateria, int codigoBachillerato){
        return  asignacionRepository.asignaciónDocenteBachillerato(dui, idMateria,codigoBachillerato);
    }
    public Asignacion asignacionParaActividad(String dui, String idMateria, int codigoBachillerato){
        return  asignacionRepository.asignaciónMateriaBachillerato(dui, idMateria,codigoBachillerato);
    }


    public List<Asignacion> buscarAsignacionDocente(String dui){
        return asignacionRepository.findByDocente(dui);
    }
}
