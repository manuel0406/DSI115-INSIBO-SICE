package com.dsi.insibo.sice.Asistencia_personal.Aparato_Docente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.DocenteAparato;

@Service
public class AparatoDocenteService {
    @Autowired
    private AparatoDocenteRepository aparatoDocenteRepository;

    //Obtine el numero del aparato docente
    public DocenteAparato obtenerPorNumeroAparatoDocente(int numeroAparatoDocente) {
        return aparatoDocenteRepository.findByNumeroAparatoDocente(numeroAparatoDocente);
    }

    //Obtengo un paginado
    public Page<DocenteAparato> aparatoTodosDoc(Pageable pageable) {
        return aparatoDocenteRepository.findAll(pageable);
    }

    //Guarda el numero del aparato docente
    public void guardarDocenteAparato(DocenteAparato aparato){
        aparatoDocenteRepository.save(aparato);
    };

    public List<Integer> obtenerAparatoDocente(){
        return aparatoDocenteRepository.obtenerIDAparatoDocente();
    }

    //Busca el id del aparato docente
    public DocenteAparato buscarPorIdAparatoDocente(int idDocenteAparato){
        return aparatoDocenteRepository.findById(idDocenteAparato).orElse(null);
    };
    //elimina el docente aparato
    public void eliminarDocenteAparato(int idDocenteAparato){
        aparatoDocenteRepository.deleteById(idDocenteAparato);
    };
    
}
