package com.dsi.insibo.sice.Calificaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Nota;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    public List<Nota> findNotasByAlumnoMateriaPeriodo(int nie, String codMateria, int idPeriodo) {
        return notaRepository.findByAlumnoNieAndActividadMateriaCodMateriaAndActividadPeriodoId(nie, codMateria, idPeriodo);
    }
}
