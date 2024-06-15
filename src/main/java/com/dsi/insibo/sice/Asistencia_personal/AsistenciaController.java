package com.dsi.insibo.sice.Asistencia_personal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/asistenciaPersonal")
public class AsistenciaController {

	@GetMapping("/cargar")
	public String subiendoArchivo(){
		return "Asistencia_personal/cargarArchivo";
	}
    @GetMapping("/asistenciaGeneral")
	public String llegadaGeneral(){
		return "Asistencia_personal/asistenciaGeneral";
	}
    @GetMapping("/asistenciaTardia")
	public String llegadasTardia(){
		return "Asistencia_personal/asistenciaTardia";
	}
	@GetMapping("/justificacion")
	public String mostrarJustificacion(){
		return "Asistencia_personal/mostrarJustificacion";
	}
	@GetMapping("/justificacionAgregar")
	public String agregarJustificacion(){
		return "Asistencia_personal/agregarJustificacion";
	}
}
