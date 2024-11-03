package com.dsi.insibo.sice.Paquetes_escolar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Paquetes_escolar.Uniforme.UniformeRepository;
import com.dsi.insibo.sice.Paquetes_escolar.Utiles.UtilesRepository;
import com.dsi.insibo.sice.Paquetes_escolar.Zapatos.ZapatosRepository;

@Service
public class EntregasService {
    @Autowired
    private ZapatosRepository zapatosRepository;

    @Autowired
    private UniformeRepository uniformeRepository;

    @Autowired
    private UtilesRepository utilesRepository;

    public List<Object[]> filtrarPorUniforme(int idBachillerato, String fecha, boolean estado) {
        return uniformeRepository.filtrarPorUniforme(idBachillerato, fecha, estado);
    }
    public List<Object[]> filtrarPorUniformeSinEstado(int idBachillerato, String fecha) {
        return uniformeRepository.filtrarPorUniformesinEstado(idBachillerato, fecha);
    }

    public List<Object[]> filtrarPorZapatos(int idBachillerato, String fecha, boolean estado) {
        return zapatosRepository.filtrarPorZapatos(idBachillerato, fecha, estado);
    }
    public List<Object[]> filtrarPorZapatosSinEstado(int idBachillerato, String fecha) {
        return zapatosRepository.filtrarPorZapatosSinEstado(idBachillerato, fecha);
    }

    public List<Object[]> filtrarPorUtilesSinEstado(int idBachillerato, String fecha) {
        return utilesRepository.filtrarPorUtilessinEstado(idBachillerato, fecha);
    }

    public List<Object[]> filtrarPorUtiles(int idBachillerato, String fecha, boolean estado) {
        return utilesRepository.filtrarPorUtiles(idBachillerato, fecha, estado);
    }
}
