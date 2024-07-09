package com.dsi.insibo.sice.Expediente_docente.Docentes.Anexos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.AnexoDocente;
import jakarta.transaction.Transactional;

@Service
public class AnexoDocenteService {

    @Autowired
    private AnexoDocenteRepository anexoRepository;

    public void guardaAnexo(AnexoDocente anexoDocente){
        anexoRepository.save(anexoDocente);
    }

    public AnexoDocente buscarAnexoPorId(int id){
        return anexoRepository.findById(id).orElse(null);
    }
    
    public AnexoDocente buscarDocente(String duiPersonal){
        return  anexoRepository.findByDocenteDui(duiPersonal);
    }

    @Transactional
    public void eliminarAnexoDocente(String duiPersonal) {
        anexoRepository.deleteByDocenteDui(duiPersonal);
    }
}
