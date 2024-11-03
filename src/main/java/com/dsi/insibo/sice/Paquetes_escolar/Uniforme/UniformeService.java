package com.dsi.insibo.sice.Paquetes_escolar.Uniforme;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Uniforme;

@Service
public class UniformeService {

    @Autowired
    private UniformeRepository uniformeRepository;

    public void saveUniforme(Alumno alumno, String tallaUniforme, boolean fueEntregado) {
        Uniforme uniforme = new Uniforme();
        uniforme.setAlumno(alumno);
        uniforme.setTallaUniforme(tallaUniforme);
        uniforme.setUniformeEntegado(fueEntregado);

        // Obtener la fecha actual de tipo 
        Date fechaActual = new Date();

        java.sql.Date fechaEntrega = new java.sql.Date(fechaActual.getTime());

        uniforme.setFechaEntregaUniforme(fechaEntrega);
        uniformeRepository.save(uniforme);

    }

    public List<String> obtenerFechasPorBachillerato(int idBachillerato) {
        return uniformeRepository.findDistinctFechasByBachillerato(idBachillerato);
    }

    public Uniforme findById(Integer id) {
        return uniformeRepository.findById(id).orElse(null);
    }

    public void save(Uniforme uniforme) {
        uniformeRepository.save(uniforme);
    }

    public void actualizarEstadoEntrega(Integer id, boolean entregado) {
        Uniforme uniforme = uniformeRepository.findById(id).orElse(null);

        if(uniforme != null){
            uniforme.setUniformeEntegado(entregado);
            uniformeRepository.save(uniforme);
        }
    }
}
