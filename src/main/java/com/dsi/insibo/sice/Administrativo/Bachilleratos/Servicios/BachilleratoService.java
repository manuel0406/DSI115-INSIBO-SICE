package com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Repositorys.BachilleratoRepository;
import com.dsi.insibo.sice.entity.Bachillerato;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BachilleratoService {

    @Autowired
    private BachilleratoRepository bachilleratoRepository;

    public List<Bachillerato> listaBachilleratos() {
        return (List<Bachillerato>) bachilleratoRepository.findAllBachilleratos();
    }

    public Bachillerato debolverBachillerato(String carrera, String seccion, String grado){
        return bachilleratoRepository.especialidad(carrera, seccion, grado);
    }

    public List<Bachillerato> listaCarrera(boolean matricula) {

        List<Bachillerato> bachilleratos= new ArrayList<>();
        if (matricula) {
           bachilleratos = bachilleratoRepository.findAll();
        }else{
            bachilleratos=bachilleratoRepository.findAllBachilleratos();
        }
        // tu lógica para obtener los
                                                                             // bachilleratos;
        Set<String> nombresUnicos = new HashSet<>();

        return bachilleratos.stream()
                .filter(bachillerato -> nombresUnicos.add(bachillerato.getNombreCarrera()))
                .collect(Collectors.toList());
    }

    public void guardarBachillerato(Bachillerato bachillerato) {
        bachilleratoRepository.save(bachillerato);
    }

    public List<Bachillerato> listadOfertaPorAnio(int idAnioAcademico) {
        return bachilleratoRepository.ofertaPorAnio(idAnioAcademico);
    }
    public Bachillerato existeBachillerato(String carrera, int grado, String seccion, int idAnio){
        return bachilleratoRepository.existe(carrera, grado, seccion, idAnio);
    }
    public Bachillerato obtenerBachilleratoEspecifico(String carrera, int grado, String seccion) {
        return bachilleratoRepository.findByNombreCarreraAndGradoAndSeccion(carrera, grado, seccion)
                .orElse(null);
    }
    
    public List<String> getSeccionesByCarrera(String carrera, String grado, boolean matricula) {
        if (matricula) {
            return bachilleratoRepository.findSeccionesByCarreraMatricula(carrera, grado);
        }
        return bachilleratoRepository.findSeccionesByCarrera(carrera, grado);
    }

    public Bachillerato bachilleratoPorId(int id){
        return bachilleratoRepository.findById(id).orElse(null);
    }

    public Bachillerato debolverBachilleratoMatricula(String carrera, String seccion, String grado){
        return bachilleratoRepository.especialidadMatricula(carrera, seccion, grado);
    }
    public void deleteBachillerato(int codigoBachillerato){
        bachilleratoRepository.deleteById(codigoBachillerato);
    }


        //OBTENER PRIMEROS AÑOS
    // 1. Usado en nuevas asignaciones.
    public List<Bachillerato> obtenerPrimeros(){
        return (List<Bachillerato>) bachilleratoRepository.findPorSecciones(1);
    } 

    //OBTENER SEGUNDOS AÑOS
    // 1. Usado en nuevas asignaciones.
    public List<Bachillerato> obtenerSegundos(){
        return (List<Bachillerato>) bachilleratoRepository.findPorSecciones(2);
    }  

    //OBTENER TERCEROS AÑOS
    // 1. Usado en nuevas asignaciones.
    public List<Bachillerato> obtenerTerceros(){
        return (List<Bachillerato>) bachilleratoRepository.findPorSecciones(3);
    }
    
    // OBTENER POR ID
    public Bachillerato obtenerBachilleratoPorId(Integer codigoBachillerato){
        return bachilleratoRepository.findById(codigoBachillerato).orElse(null);
    }
}


