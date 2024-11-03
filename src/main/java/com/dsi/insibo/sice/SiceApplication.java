package com.dsi.insibo.sice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.AnioService;
import com.dsi.insibo.sice.Administrativo.Materias.ServiciosMaterias.AsignacionService;
import com.dsi.insibo.sice.Administrativo.Orientadores.OrientadorService;
import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteService;
import com.dsi.insibo.sice.Seguridad.SeguridadService.SessionService;
import com.dsi.insibo.sice.entity.AnioAcademico;
import com.dsi.insibo.sice.entity.Asignacion;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.Docente;


@Controller
@SpringBootApplication
public class SiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiceApplication.class, args);
	}

	@Autowired
	SessionService sesion;
	@Autowired
	OrientadorService orientadorService;
	@Autowired
	DocenteService docenteService;
	@Autowired
	AnioService anioService;
	@Autowired
	AsignacionService asignacionService;

	@PreAuthorize("!hasRole('BIBLIOTECARIO')")
	@GetMapping("/")
	public String holamundo(Model model) {

		String dui = sesion.duiSession();
		boolean secciones = true;

		Docente doocente = docenteService.buscarPorIdDocente(dui);
		List<Bachillerato> listaSecciones = orientadorService.listaSeccionesB(dui);

		List<Asignacion> listaAsignaciones=asignacionService.buscarAsignacionDocente(dui);

		if (listaSecciones.isEmpty()) {
			secciones = false;
		}

		model.addAttribute("titulo", "Inicio");
		model.addAttribute("dui", doocente);
		model.addAttribute("listaSecciones", listaSecciones);
		model.addAttribute("listadoAsignaciones", listaAsignaciones);
		model.addAttribute("activo", secciones);

		return "home";
	}

	@PreAuthorize("!hasRole('BIBLIOTECARIO')")
	@GetMapping("/administracion")
	public String homeAdministracion(Model model) {

		AnioAcademico matriculaActiva = anioService.activoMatricula();
		if (matriculaActiva ==null) {
			// System.out.println("No hay matricula activa");
		}

		model.addAttribute("titulo", "Administración");
		model.addAttribute("activo", matriculaActiva);
		return "Administrativo/homeAdministracion.html";
	}

	@GetMapping("/session")
	public ResponseEntity<?> getDetailResponseEntity() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userObject = null;

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			userObject = (User) authentication.getPrincipal();
		}

		Map<String, Object> response = new HashMap<>();
		response.put("response", "Hello World");
		response.put("sessionUser", userObject);

		return ResponseEntity.ok(response);
	}

}
