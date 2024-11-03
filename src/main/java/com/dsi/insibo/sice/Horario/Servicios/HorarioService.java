package com.dsi.insibo.sice.Horario.Servicios;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Horario.PDF.HorarioDTO;
import com.dsi.insibo.sice.Horario.PDF.HorarioEditarDTO;
import com.dsi.insibo.sice.Horario.Repositorios.HorarioRepository;
import com.dsi.insibo.sice.entity.AsignacionHorario;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    // Guardar una asignacion
    public void guardarHoraAsignacion(AsignacionHorario asignacionHorario) {
        horarioRepository.save(asignacionHorario);
    }

    // Método para obtener los idHorarioBase dado un codigoBachillerato y año activo
    public List<Integer> obtenerIdHorariosBasePorCodigoBachillerato(int codigoBachillerato) {
        return horarioRepository.findIdHorarioBaseByCodigoBachilleratoAndActivoAnioTrue(codigoBachillerato);
    }

    // Horas asignadas dado un codigoBachillerato y año activo
    public List<AsignacionHorario> obtenerHorasAsignadas(int codigoBachillerato) {
        return (List<AsignacionHorario>) horarioRepository
                .findHorarioByCodigoBachilleratoAndActivoAnioTrue(codigoBachillerato);
    }

    // Horas asignadas dado un duiDocente y año activo
    public List<AsignacionHorario> obtenerHorasAsignadasDocente(String duiDocente) {
        return (List<AsignacionHorario>) horarioRepository
                .findHorarioByDuiDocenteAndActivoAnioTrue(duiDocente);
    }

    // Filtrar lista AsignacionHorario a lista de HorarioDTO
    public List<HorarioDTO> obtenerHorasAsignadasDTO(List<AsignacionHorario> horasDeClase) {
        return horasDeClase.stream()
                .map(hora -> {
                    HorarioDTO dto = new HorarioDTO();
                    dto.setDuiDocente(hora.getAsignacion().getDocente().getDuiDocente());
                    dto.setIdHorarioBase(String.valueOf(hora.getHorarioBase().getIdHorarioBase()));
                    dto.setCodMateria(hora.getAsignacion().getMateria().getCodMateria());
                    dto.setNomMateria(hora.getAsignacion().getMateria().getNomMateria());
                    dto.setNombreDocente(hora.getAsignacion().getDocente().getNombreDocente());
                    dto.setApellidoDocente(hora.getAsignacion().getDocente().getApellidoDocente());
                    dto.setGrado(String.valueOf(hora.getAsignacion().getBachillerato().getGrado()));
                    dto.setSeccion(hora.getAsignacion().getBachillerato().getSeccion());
                    dto.setNombreCarrera(hora.getAsignacion().getBachillerato().getNombreCarrera());

                    // Generar y asignar el código
                    String codigo = generarCodigo(hora.getAsignacion().getBachillerato().getNombreCarrera());
                    dto.setCodigo(codigo);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Filtrar lista AsignacionHorario a lista de HorarioDTO
    public List<HorarioEditarDTO> obtenerHorasAsignadasEditarDTO(List<AsignacionHorario> horasDeClase) {
        return horasDeClase.stream()
                .map(hora -> {
                    HorarioEditarDTO dto = new HorarioEditarDTO();
                    dto.setIdHorarioBase(String.valueOf(hora.getHorarioBase().getIdHorarioBase()));
                    dto.setNomMateria(hora.getAsignacion().getMateria().getNomMateria());
                    dto.setNombreDocente(hora.getAsignacion().getDocente().getNombreDocente());
                    dto.setApellidoDocente(hora.getAsignacion().getDocente().getApellidoDocente());
                    dto.setNombreDia(hora.getHorarioBase().getDia().getNombreDia());
                    dto.setHoraInicio(hora.getHorarioBase().getHora().getHoraInicio());
                    dto.setHoraFinalizacion(hora.getHorarioBase().getHora().getHoraFinalizacion());
                    dto.setIdAsignacion(String.valueOf(hora.getAsignacion().getIdAsignacion()));
                    dto.setIdHora(String.valueOf(hora.getHorarioBase().getHora().getIdHora()));
                    dto.setIdAsignacionHorario(String.valueOf(hora.getIdAsignacionHorario()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Método privado para generar el código basado en el nombre de la carrera
    private String generarCodigo(String nombreCarrera) {
        return Arrays.stream(nombreCarrera.split(" ")) // Divide el nombre en palabras
                .filter(palabra -> palabra.length() > 3) // Filtra palabras con más de 3 letras
                .map(palabra -> palabra.substring(0, 1)) // Toma la primera letra de cada palabra
                .collect(Collectors.joining()) // Une las letras en un solo String
                .toUpperCase(); // Convierte a mayúsculas
    }

    // Obtener una hora asignada por id
    public AsignacionHorario obtenerHoraAsignacionPorId(int idAsignacionHorario) {
        return horarioRepository.findById(idAsignacionHorario).orElse(null);
    }

    // Eliminar una hora asignada
    public void eliminarHoraAsignacion(int idAsignacionHorario) {
        horarioRepository.deleteById(idAsignacionHorario);
    }

    public void eliminarHorasAsignacion(List<Integer> ids) {
        horarioRepository.deleteAllById(ids);
    }
    

    // Verificar si existe un bloque de 2 horas consecutivas
    public boolean existeBloqueDeDosHoras(int codigoBachillerato, String nombreDia, int idHora, int idAsignacion) {
        List<AsignacionHorario> asignaciones = horarioRepository.findBloqueDeDosHoras(codigoBachillerato, nombreDia,
                idHora, idAsignacion);
        return asignaciones.size() >= 2;
    }
}
