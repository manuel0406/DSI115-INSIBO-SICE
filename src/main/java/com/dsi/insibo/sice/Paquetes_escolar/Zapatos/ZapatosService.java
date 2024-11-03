package com.dsi.insibo.sice.Paquetes_escolar.Zapatos;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Zapatos;

@Service
public class ZapatosService {
    @Autowired
    private ZapatosRepository zapatosRepository;

    public void saveZapatos(Alumno alumno, int tallaZapato, boolean fueEntregado) {
        Zapatos zapatos = new Zapatos();
        zapatos.setAlumno(alumno);
        zapatos.setTallaZapato(tallaZapato);
        zapatos.setZapataloEntregado(fueEntregado);
        // Obtener la fecha actual
        Date fechaActual = new Date();
        java.sql.Date fechaEntrega = new java.sql.Date(fechaActual.getTime());

        zapatos.setFechaEntegaZapatos(fechaEntrega);
        zapatosRepository.save(zapatos);
    }

    public List<String> obtenerFechasPorBachillerato(int idBachillerato) {
        return zapatosRepository.findDistinctFechasByBachillerato(idBachillerato);
    }

    public Zapatos findById(Integer id) {
        return zapatosRepository.findById(id).orElse(null);
    }

     // Método existente para guardar un zapato
     public void save(Zapatos zapatos) {
        zapatosRepository.save(zapatos);
    }

    // Nuevo método para actualizar solo el estado de entrega
    public void actualizarEstadoEntrega(Integer id, boolean entregado) {
        Zapatos zapatos = zapatosRepository.findById(id).orElse(null);
        if (zapatos != null) {
            zapatos.setZapataloEntregado(entregado);
            zapatosRepository.save(zapatos);
        }
    }
}
