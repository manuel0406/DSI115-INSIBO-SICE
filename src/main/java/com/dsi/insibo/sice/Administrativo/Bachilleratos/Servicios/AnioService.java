package com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Repositorys.AnioRepository;
import com.dsi.insibo.sice.entity.AnioAcademico;
import com.dsi.insibo.sice.entity.Bachillerato;

@Service
public class AnioService {

    @Autowired
    AnioRepository anioRepository;

    public List<AnioAcademico> listaAnio() {
        List<AnioAcademico> lista = anioRepository.findAll();
    lista.sort(Comparator.comparingInt(AnioAcademico::getAnio));
    return lista;
    }

    public void guardarAnio(AnioAcademico anioAcademico) {
        anioRepository.save(anioAcademico);
    }

    public AnioAcademico buscarPoridAnioAcademico(int id) {
        return anioRepository.findById(id).orElse(null);
    }

    public AnioAcademico buscandoAnio(int anio) {
        return anioRepository.buscarAnio(anio);
    }

    public List<Bachillerato> listaNullos(List<AnioAcademico> listaAnios) {
        List<Bachillerato> lista = new ArrayList<Bachillerato>();
        for (AnioAcademico anioAcademico : listaAnios) {
            lista = anioRepository.listaBuscarAnio(anioAcademico.getIdAnioAcademico());
        }
        return lista;
    }

    public boolean tieneBachilleratos(int idAnioAcademico) {
        List<Bachillerato> bachilleratos = anioRepository.listaBuscarAnio(idAnioAcademico);
        return bachilleratos != null && !bachilleratos.isEmpty();
    }

    public AnioAcademico activoMatricula(){
        return anioRepository.matriculaActiva();
    }
    public AnioAcademico activoAnio(){
        return anioRepository.anioActivo();
    }
}
