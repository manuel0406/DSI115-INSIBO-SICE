package com.dsi.insibo.sice.Asistencia_personal.Aparato_Personal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.PersonalAparato;

@Service
public class AparatoPersonalService {

    @Autowired
    private AparatoPersonalRepository aparatoPersonalRepository;

    // Obtine el numero del aparato del personal administrativo
    public PersonalAparato obtenerPorNumeroAparatoPersonal(int numeroAparatoPersonal) {
        return aparatoPersonalRepository.findByNumeroAparatoPersonal(numeroAparatoPersonal);
    }

    // Lista todos los numeros asignados en aparato
    public Page<PersonalAparato> aparatoTodosAdm(Pageable pageable) {
        return aparatoPersonalRepository.findAll(pageable);
    }

    public List<Integer> obtenerAparatoPersonal() {
        return aparatoPersonalRepository.obtenerIDAparatoPersonal();
    }

    // Guarda el numero de aparato asignado
    public void guardarPersonalAparato(PersonalAparato aparato) {
        aparatoPersonalRepository.save(aparato);
    }

    // Busca el id del aparato
    public PersonalAparato buscarPorIdAparatoPersonal(int idPersonalAparato) {
        return aparatoPersonalRepository.findById(idPersonalAparato).orElse(null);
    }

    // Metodo de eliminar aparato
    public void eliminarPersonalAparato(int idPersonalAparato) {
        aparatoPersonalRepository.deleteById(idPersonalAparato);
    }
}
