package com.dsi.insibo.sice.Asistencia_personal.Asistencia_docente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.AsistenciaDocente;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AsistenciaDocenteService {
    @Autowired
    private AsistenciaDocenteRepository asistenciaDocenteRepository;

    public Page<Map<String, Object>> obtenerTodasAsistenciasDocente(Pageable pageable) {
        return asistenciaDocenteRepository.findAsistenciaDocenteP(pageable);
    }

    // Obtener asistencias tardias
    public Page<Map<String, Object>> obtenerAsistenciasPorHorario(Pageable pageable) {
        return asistenciaDocenteRepository.findAsistenciaDocenteByHorario(pageable);
    }
    // Obtener lista de docentes
    public List<Map<String, Object>> obtenerDocentesConAparato() {
        return asistenciaDocenteRepository.findDocentesAparato();
    }
    //Obtener id aparato, nombre en asistencia tardias del docente
    public List<Map<String, Object>> obtenerDocentesTardia() {
        return asistenciaDocenteRepository.findDocentesTardia();
    }
    //Obtener meses de las asistencias
    public List<Map<String, Object>> obtenerMeses() {
        return asistenciaDocenteRepository.findMesAnio();
    }

    // Obtner lista de departamentos en base de datos ingresados
    public List<Map<String, Object>> obtenerDocenteDepartamento() {
        return asistenciaDocenteRepository.findDocenteDepartamento();
    }

    public Page<Map<String, Object>> listarDocentesfiltro(Integer numeroAparato, String departamento,
            Pageable pageable) {
        if (numeroAparato != null && departamento != null) {
            return asistenciaDocenteRepository.findDocentePersonaDepartamentoAll(numeroAparato, departamento, pageable);
        } else if (numeroAparato != null) {
            return asistenciaDocenteRepository.findDocentePersonaAll(numeroAparato, pageable);
        } else if (departamento != null) {
            return asistenciaDocenteRepository.findDocenteDepartamentoAll(departamento, pageable);
        }
        return asistenciaDocenteRepository.findAsistenciaDocenteP(pageable);
    }

    public Page<Map<String, Object>> listarDocentesTardefiltro(Integer numeroAparato, Integer mes, Integer anio,
            Pageable pageable) {
        if (numeroAparato != null && mes != null && anio != null) {
            return asistenciaDocenteRepository.findDocenteMesTardiaAll(mes, anio, pageable);
        } else if (numeroAparato != null) {
            return asistenciaDocenteRepository.findDocentePersonaTardiaAll(numeroAparato, pageable);
        } else if (mes != null && anio != null) {
            return asistenciaDocenteRepository.findDocenteMesTardiaAll(mes, anio, pageable);
        }
        return asistenciaDocenteRepository.findAsistenciaDocenteByHorario(pageable);
    }

    public AsistenciaDocente findById(int id) {
        return asistenciaDocenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada con ID: " + id));
    }
}