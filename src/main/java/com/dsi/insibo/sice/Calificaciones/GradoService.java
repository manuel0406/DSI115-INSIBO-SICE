package com.dsi.insibo.sice.Calificaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Bachillerato;

@Service
public class GradoService {
    @Autowired
    private GradoRepository gradoRepository;

    public List<Bachillerato> listaBachilleratos(){
        return (List<Bachillerato>) gradoRepository.findAll();
    }
}