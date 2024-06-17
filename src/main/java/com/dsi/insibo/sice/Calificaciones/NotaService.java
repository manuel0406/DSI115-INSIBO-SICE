package com.dsi.insibo.sice.Calificaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Actividad;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Nota;

import jakarta.transaction.Transactional;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    public List<Nota> findByNieAndCodMateriaAndIdPeriodo(int nie, String codMateria, int idPeriodo) {
        return notaRepository.findByAlumnoNieAndActividadMateriaCodMateriaAndActividadPeriodoIdPeriodo(nie, codMateria, idPeriodo);
    }

    @Autowired
    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public List<Nota> findNotasByAlumno(Alumno alumno) {
        return notaRepository.findByAlumno(alumno);
    }

    @Transactional
    public void deleteNotasByAlumnoNie(int nie) {
        notaRepository.deleteByAlumnoNie(nie);
    }
}
