package com.dsi.insibo.sice.Calificaciones;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.Periodo;

@Service
public class PeriodoService {
    @Autowired
    private PeriodoRepository periodoRepository;

    public List<Periodo> listaPeriodos() {
        return (List<Periodo>) periodoRepository.findAll();
    }

    public Periodo periodoPorId(int idPeriodo){
        return periodoRepository.findById(idPeriodo).orElse(null);
    }
}
