package com.dsi.insibo.sice.Asistencia_personal;
import java.util.List;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Asistencia_personal.Aparato_Docente.AparatoDocenteRepository;
import com.dsi.insibo.sice.Asistencia_personal.Aparato_Personal.AparatoPersonalRepository;
import com.dsi.insibo.sice.Asistencia_personal.Asistencia_admnistrativa.AsistenciaAdmRepository;
import com.dsi.insibo.sice.Asistencia_personal.Asistencia_docente.AsistenciaDocenteRepository;
import com.dsi.insibo.sice.Asistencia_personal.DTOAparato.AsistenciaDTO;
import com.dsi.insibo.sice.entity.AsistenciaDocente;
import com.dsi.insibo.sice.entity.AsistenciaPersonal;
import com.dsi.insibo.sice.entity.DocenteAparato;
import com.dsi.insibo.sice.entity.PersonalAparato;

@Service
public class AsistenciaService {

    private final AparatoDocenteRepository aparatoDocenteRepository;
    private final AsistenciaDocenteRepository asistenciaDocenteRepository;
    private final AparatoPersonalRepository aparatoPersonalRepository;
    private final AsistenciaAdmRepository asistenciaAdmRepository;

    public AsistenciaService(AsistenciaDocenteRepository asistenciaDocenteRepository,
            AparatoDocenteRepository aparatoDocenteRepository,
            AsistenciaAdmRepository asistenciaAdmRepository,
            AparatoPersonalRepository aparatoPersonalRepository) {
        this.asistenciaDocenteRepository = asistenciaDocenteRepository;
        this.aparatoDocenteRepository = aparatoDocenteRepository;
        this.asistenciaAdmRepository = asistenciaAdmRepository;
        this.aparatoPersonalRepository = aparatoPersonalRepository;
    }
    public List<AsistenciaDocente> obtenerTodasAsistenciasDocente(){
        return asistenciaDocenteRepository.findAll();
    }

    public List<AsistenciaPersonal> obtenerTodasAsistenciasPersonal(){
        return asistenciaAdmRepository.findAll();
    }
    
    public void procesarAsistencias(List<AsistenciaDTO> asistencias) {
        for (AsistenciaDTO asistenciaDTO : asistencias) {
            System.out.println("Procesando asistencia: " + asistenciaDTO);

            String depart = asistenciaDTO.getDepart();
            System.out.println("Departamento: " + depart);

            // Verifica que Depart no es nulo o vacio
            if (depart != null && !depart.trim().isEmpty()) {
                if (depart.equalsIgnoreCase("docente")) {
                    DocenteAparato docenteAparato = aparatoDocenteRepository
                            .findByNumeroAparatoDocente(asistenciaDTO.getId());
                    if (docenteAparato != null) {
                        AsistenciaDocente asistenciaDocente = new AsistenciaDocente();
                        asistenciaDocente.setAparatoDocente(docenteAparato);
                        asistenciaDocente.setDeparDocente(depart);
                        asistenciaDocente.setTurno(asistenciaDTO.getTurno());
                        asistenciaDocente.setHoraEntrada(asistenciaDTO.getInicio());
                        
                        asistenciaDocente.setHoraSalida(asistenciaDTO.getFin());
                        asistenciaDocenteRepository.save(asistenciaDocente);
                        System.out.println("Asistencia docente guardada: " + asistenciaDocente);
                    } else {
                        System.out.println("DocenteAparato no encontrado para ID: " + asistenciaDTO.getId());
                    }
                } else if (depart.equalsIgnoreCase("personal")) {
                    PersonalAparato personalAparato = aparatoPersonalRepository
                            .findByNumeroAparatoPersonal(asistenciaDTO.getId());
                    if (personalAparato != null) {
                        AsistenciaPersonal asistenciaPersonal = new AsistenciaPersonal();
                        asistenciaPersonal.setPersonalAparato(personalAparato);
                        asistenciaPersonal.setDepartPersonal(depart);
                        asistenciaPersonal.setTurnoP(asistenciaDTO.getTurno());
                        asistenciaPersonal.setHoraEntradaP(asistenciaDTO.getInicio());
                        asistenciaPersonal.setHoraSalidaP(asistenciaDTO.getFin());
                        asistenciaAdmRepository.save(asistenciaPersonal);
                        System.out.println("Asistencia personal guardada: " + asistenciaPersonal);
                    } else {
                        System.out.println("PersonalAparato no encontrado para ID: " + asistenciaDTO.getId());
                    }
                }
            } else {
                // Manejo de departamento vacio
                System.out.println("Asistencia con ID " + asistenciaDTO.getId() + " tiene un campo Depart nulo o vacío.");
            }
        }
    }

}
