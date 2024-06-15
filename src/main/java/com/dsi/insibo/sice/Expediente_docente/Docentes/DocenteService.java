package com.dsi.insibo.sice.Expediente_docente.Docentes;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.Docente;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    public List<DocenteDTO> listarDocentes() {
        List<Docente> docentes = (List<Docente>) docenteRepository.findAll();
        return docentes.stream()
                .map(DocenteDTO::new)
                .collect(Collectors.toList());
    }

    public boolean guardarDocente(Docente docente) {
        // Verificar si el docente ya existe en la base de datos
        boolean existe = docenteRepository.existsById(docente.getDuiDocente());
    
        // Guardar el docente
        docenteRepository.save(docente);
    
        // Devolver true si se creó un nuevo expediente, false si se actualizó uno existente
        return !existe;
    }
    

    public Docente buscarPorIdDocente(String duiDocente) {
        return docenteRepository.findById(duiDocente).orElse(null);
    }

    public void eliminar(String duiDocente) {
        docenteRepository.deleteById(duiDocente);
    }
}
