package com.dsi.insibo.sice.Administrativo.Materias.ServiciosMaterias;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dsi.insibo.sice.Administrativo.Materias.RespositoriosMaterias.MateriasRepository;
import com.dsi.insibo.sice.entity.Materia;

@Service
public class MateriasService {
    
    @Autowired
    private MateriasRepository materiasRepository;
    
    // MATERIAS DISPONIBLES
    public List<Materia> obtenerMaterias(){
        return (List<Materia>) materiasRepository.obtenerMaterias();
    }

    // MATERIAS PAGINA
    public Page<Materia> obtenerMateriasConPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return materiasRepository.obtenerMateriasConPaginado(pageable);
    }

    // GUARDAR MATERIAS
    public void guardarMateria (Materia materia){
        materiasRepository.save(materia);
    }

    // OBTENER MATERIAS POR ID
    public Materia obtenerMateriaPorId(int idMateria) {
        return materiasRepository.findById(idMateria).orElse(null);
    }

    // ELIMINAR MATERIA
    public void eliminarMateria(Materia materia) {
        materiasRepository.delete(materia);
    }

    // OBTENER MATERIA POR TIPO
    public List<Materia> obtenerMateriaPorTipo(String tipo) {
       return materiasRepository.findMateriasByType(tipo);
    }

    public Page<Materia> obtenerMateriasPorTipoConPaginado(String tipo, int pagina, int tamanyo) {
        Pageable pageable = PageRequest.of(pagina, tamanyo);
        return materiasRepository.findMateriasByType(tipo, pageable);
    }
    
}
