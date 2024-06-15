package com.dsi.insibo.sice.Calificaciones;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Actividad;

@Service
public class CalificacionesService {
    @Autowired
    private CalificacionesRepository calificacionesRepository;
}