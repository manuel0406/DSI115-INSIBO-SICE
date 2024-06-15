package com.dsi.insibo.sice.Expediente_alumno;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.Bachillerato;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BachilleratoService  {

    @Autowired
    private BachilleratoRepository bachilleratoRepository;

    public List<Bachillerato> listaBachilleratos(){
        return (List<Bachillerato>) bachilleratoRepository.findAll();
    }

   

    public List<Bachillerato> listaCarrera() {
    List<Bachillerato> bachilleratos =bachilleratoRepository.findAll(); // tu lógica para obtener los bachilleratos;
    Set<String> nombresUnicos    = new HashSet<>();
    
    return bachilleratos.stream()
            .filter(bachillerato -> nombresUnicos.add(bachillerato.getNombreCarrera()))
            .collect(Collectors.toList());
}
}
