package com.dsi.insibo.sice.Paquetes_escolar.Donaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.InventarioDonacion;

@Service
public class InventarioDonacionService {
    @Autowired
    private InventarioDonacionRepository inventarioDonacionRepository;

    public List<InventarioDonacion> obtenerTodasLasDonaciones() {
        return inventarioDonacionRepository.findAll();
    }

    // Comprobar existencia
    public boolean existeDonacion(String tipoPrenda, String tallaPrenda) {
        return inventarioDonacionRepository.findByTipoPrendaAndTallaPrenda(tipoPrenda, tallaPrenda).isPresent();
    }

    public void guardarDonacion(InventarioDonacion donacion) {
        inventarioDonacionRepository.save(donacion);
    }

    // Metodo para obtener una donacion por su ID
    public InventarioDonacion obtenerDonacionPorId(int id) {
        Optional<InventarioDonacion> donacion = inventarioDonacionRepository.findById(id);
        return donacion.orElse(null);
    }

    public int obtenerCantidadPorId(int donacionId) {
        return inventarioDonacionRepository.findCantidadById(donacionId);
    }

    public boolean actualizarCantidadDonacion(int donacionId, int nuevaCantidad) {
        Optional<InventarioDonacion> optionalDonacion = inventarioDonacionRepository.findById(donacionId);
        if (optionalDonacion.isPresent()) {
            InventarioDonacion donacion = optionalDonacion.get();
            donacion.setCantidadPrenda(nuevaCantidad);
            inventarioDonacionRepository.save(donacion);
            return true;
        } else {
            return false;
        }
    }

    // Metodo de eliminar donacion
    public void eliminarDonacion(int idInventarioDonacion) {
        inventarioDonacionRepository.deleteById(idInventarioDonacion);
    }

    // Metodo para obtener los detalles del bachillerato por su código
    public Bachillerato obtenerDetallesBachilleratoPorCodigo(Integer codigoBachillerato) {
        return inventarioDonacionRepository.findBachilleratoByCodigo(codigoBachillerato);
    }

    public List<List<Object>> obtenerAlumnosPorCodigoBachillerato(Integer codigoBachillerato) {
        List<Object[]> results = inventarioDonacionRepository.findAlumnosByCodigoBachillerato(codigoBachillerato);
        List<List<Object>> alumnos = new ArrayList<>();
        
        for (Object[] result : results) {
            List<Object> alumno = new ArrayList<>();
            for (Object field : result) {
                alumno.add(field);
            }
            alumnos.add(alumno);
        }
        return alumnos;
    }


     // Metodo para obtener el id del año académico activo
     public Integer obtenerAnioAcademicoActivo() {
        return inventarioDonacionRepository.findActiveAnioAcademicoId();
    }

    // Método para obtener los bachilleratos por año académico
    public List<Object[]> obtenerBachilleratosPorAnioAcademico(Integer idAnioAcademico) {
        return inventarioDonacionRepository.findBachilleratoByAnioAcademicoNoDTO(idAnioAcademico);
    }

}
