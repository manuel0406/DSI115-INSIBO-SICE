package com.dsi.insibo.sice.Expediente_docente.Administrativos.Anexos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.AnexoPersonalAdministrativo;
import jakarta.transaction.Transactional;

@Service
public class AnexoAdministrativoService {

    @Autowired
    private AnexoAdministrativoRepository anexoRepository;

    public void guardaAnexo(AnexoPersonalAdministrativo anexoAdministrativo){
        anexoRepository.save(anexoAdministrativo);
    }

    public AnexoPersonalAdministrativo buscarAnexoPorId(int id){
        return anexoRepository.findById(id).orElse(null);
    }
    
    public AnexoPersonalAdministrativo buscarAdministrativo(String duiPersonal){
        return  anexoRepository.findByAdministrativoDui(duiPersonal);
    }

    @Transactional
    public void eliminarAnexoAdministrativo(String duiPersonal) {
        anexoRepository.deleteByAdministrativoDui(duiPersonal);
    }
}
