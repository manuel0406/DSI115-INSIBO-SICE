package com.dsi.insibo.sice.Asistencia_personal.Asistencia_admnistrativa;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import com.dsi.insibo.sice.entity.AsistenciaPersonal;

@Service
public class AsistenciaAdmService {
    @Autowired
    private AsistenciaAdmRepository asistenciaAdmRepository;

    public Page<Map<String, Object>> obtenerTodasAsistenciasAdministrativas(Pageable pageable) {
        return asistenciaAdmRepository.findAsistenciaAdministrativaP(pageable);
    }

    // Obtener asistencias tardias
    public Page<Map<String, Object>> obtenerAsistenciasAdmPorHorario(Pageable pageable) {
        return asistenciaAdmRepository.findAsistenciaAdministrativaByHorario(pageable);
    }

    // Obtener lista de docentes
    public List<Map<String, Object>> obtenerAdministrativosConAparato() {
        return asistenciaAdmRepository.findAdministrativosAparato();
    }

    // Obtener id aparato, nombre en asistencia tardias del docente
    public List<Map<String, Object>> obtenerAdmTardia() {
        return asistenciaAdmRepository.findAdministrativosTardia();
    }

    // Obtener meses de las asistencias
    public List<Map<String, Object>> obtenerMesesAdm() {
        return asistenciaAdmRepository.findMesAnioAdministrativo();
    }

    // Obtner lista de departamentos en base de datos ingresados
    public List<Map<String, Object>> obtenerAdmDepartamento() {
        return asistenciaAdmRepository.findAdministrativosDepartamento();
    }

    public Page<Map<String, Object>> listarAdministrativosfiltro(Integer numeroAparato, String departamento,
            Pageable pageable) {
        if (numeroAparato != null && departamento != null) {
            return asistenciaAdmRepository.findAdministrativoPersonaDepartamentoAll(numeroAparato, departamento,
                    pageable);
        } else if (numeroAparato != null) {
            return asistenciaAdmRepository.findAdministrativoPersonaAll(numeroAparato, pageable);
        } else if (departamento != null) {
            return asistenciaAdmRepository.findAdministrativoDepartamentoAll(departamento, pageable);
        }
        return asistenciaAdmRepository.findAsistenciaAdministrativaP(pageable);
    }

    public Page<Map<String, Object>> listarAdministrativosTardefiltro(Integer numeroAparato, Integer mes, Integer anio,
            Pageable pageable) {
        if (numeroAparato != null && mes != null && anio != null) {
            return asistenciaAdmRepository.findAdministrativoMesTardia(mes, anio, pageable);
        } else if (numeroAparato != null) {
            return asistenciaAdmRepository.findAdministrativoTadiaAll(numeroAparato, pageable);
        } else if (mes != null && anio != null) {
            return asistenciaAdmRepository.findAdministrativoMesTardia(mes, anio, pageable);
        }
        return asistenciaAdmRepository.findAsistenciaAdministrativaByHorario(pageable);
    }

    public AsistenciaPersonal findById(int id) {
        return asistenciaAdmRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada con ID: " + id));
    }

}