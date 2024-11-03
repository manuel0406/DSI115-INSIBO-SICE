package com.dsi.insibo.sice.Asistencia_personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dsi.insibo.sice.Asistencia_personal.Asistencia_admnistrativa.AsistenciaAdmService;
import com.dsi.insibo.sice.Asistencia_personal.Asistencia_docente.AsistenciaDocenteService;
import com.dsi.insibo.sice.Asistencia_personal.DTOAparato.AsistenciaDTO;
import java.util.*;

@PreAuthorize("hasRole('ADMINISTRADOR')")
@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

	@Autowired
	private AsistenciaService asistenciaService;

	@Autowired
	private AsistenciaDocenteService asistenciaDocenteService;

	@Autowired
	private AsistenciaAdmService asistenciaAdmService;

	public AsistenciaController(AsistenciaService asistenciaService) {
		this.asistenciaService = asistenciaService;
	}

	// inicio de asistencia
	@GetMapping("/seleccionInicio")
	public String selccionInicio(Model model) {
		return "Asistencia_personal/indexAsistencia";
	}

	// Metodo para cargar la importacion del excel
	@GetMapping("/cargarAsistencias")
	public String subiendoArchivo() {
		return "Asistencia_personal/cargarArchivo";
	}

	// Procesa las asistencias
	@ResponseBody
	@PostMapping("/procesarAsistencias")
	public ResponseEntity<Map<String, String>> procesarAsistencia(@RequestBody List<AsistenciaDTO> asistencias ) {
		Map<String, String> response = new HashMap<>();
		// Validación de entrada
		if (asistencias == null || asistencias.isEmpty()) {
			response.put("mensaje", "No se han proporcionado asistencias");
			return ResponseEntity.badRequest().body(response);
		}
		// Procesar asistencias del excel en formato JSON
		try {
			asistenciaService.procesarAsistencias(asistencias);
			response.put("mensaje", "Datos procesados correctamente");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Manejo de excepcion
			e.printStackTrace();
			response.put("mensaje", "Error al procesar los datos");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// ASISTENCIA DOCENTE
	// inicio de asistencia docente
	@GetMapping("/listaGeneral")
	public String asistenciaGeneralDoc(Model model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "docenteAparato", required = false) Integer numeroAparato,
			@RequestParam(value = "docenteDepart", required = false) String departamento) {

		PageRequest pageable = PageRequest.of(page - 1, size);
		Page<Map<String, Object>> pageDocentes = asistenciaDocenteService.listarDocentesfiltro(numeroAparato,
				departamento, pageable);
		List<Map<String, Object>> listaDocente = pageDocentes.getContent();

		model.addAttribute("asistenciaDocentes", listaDocente);
		model.addAttribute("currentPage", pageDocentes.getNumber() + 1);
		model.addAttribute("totalPages", pageDocentes.getTotalPages());
		model.addAttribute("size", size);

		// Obtener la lista de docentes con aparatos
		List<Map<String, Object>> docentesConAparato = asistenciaDocenteService.obtenerDocentesConAparato();
		model.addAttribute("docentesConAparato", docentesConAparato);

		// Obtener la lista de departamentos
		List<Map<String, Object>> docenteDepartamento = asistenciaDocenteService.obtenerDocenteDepartamento();
		model.addAttribute("docenteDepartamento", docenteDepartamento);

		return "Asistencia_personal/Docente_asistencia/asistenciaGeneral";
	}

	@GetMapping("/asistenciaTardia")
	public String llegadasTardia(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(value = "docenteAparato", required = false) Integer numeroAparato,
			@RequestParam(value = "docenteAnioAparato", required = false) String fecha,
			Model model) {

		// Parsear fecha
		Integer mes = null;
		Integer anio = null;
		if (fecha != null && !fecha.isEmpty()) {
			String[] partes = fecha.split("-");
			if (partes.length == 2) {
				mes = Integer.parseInt(partes[0]);
				anio = Integer.parseInt(partes[1]);
			}
		}

		// Configurar paginacion
		PageRequest pageable = PageRequest.of(page, size);

		// Obtener la pagina de asistencias con filtros y paginacion
		Page<Map<String, Object>> asistenciaDocentes = asistenciaDocenteService.listarDocentesTardefiltro(numeroAparato,
				mes, anio, pageable);
		model.addAttribute("asistenciaDocentes", asistenciaDocentes.getContent());

		// Informacion de paginacion para la vista
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", asistenciaDocentes.getTotalPages());
		model.addAttribute("size", size);

		// Obtener la lista de docentes con aparatos
		List<Map<String, Object>> docenteslista = asistenciaDocenteService.obtenerDocentesTardia();
		model.addAttribute("docenteLista", docenteslista);

		// Obtener la lista de meses/años sin paginación
		List<Map<String, Object>> docentesMesAnio = asistenciaDocenteService.obtenerMeses();
		model.addAttribute("MesesFiltro", docentesMesAnio);

		return "Asistencia_personal/Docente_asistencia/asistenciaTardia";
	}

	// ASISTENCIA ADMINISTRATIVOS
	// listar general asistencia administrativa
	@GetMapping("/listaGeneralAdm")
	public String asistenciaGeneralAdm(Model model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "admAparato", required = false) Integer numeroAparato,
			@RequestParam(value = "admDepart", required = false) String departamento) {
		PageRequest pageable = PageRequest.of(page - 1, size);
		Page<Map<String, Object>> pageAdm = asistenciaAdmService.listarAdministrativosfiltro(numeroAparato,
				departamento, pageable);
		List<Map<String, Object>> listaAdm = pageAdm.getContent();
		model.addAttribute("asistenciaAdm", listaAdm);
		model.addAttribute("currentPage", pageAdm.getNumber() + 1);
		model.addAttribute("totalPages", pageAdm.getTotalPages());
		model.addAttribute("size", size);
		// Llenar select
		List<Map<String, Object>> admConAparato = asistenciaAdmService.obtenerAdministrativosConAparato();
		model.addAttribute("admConAparato", admConAparato);
		// Llenar selec departamentos
		List<Map<String, Object>> admDepartamentos = asistenciaAdmService.obtenerAdmDepartamento();
		model.addAttribute("admDepartamentos", admDepartamentos);

		return "Asistencia_personal/Personal_asistencia/asistenciaGeneralAdm";

	}

	@GetMapping("/asistenciaTardiaadm")
	public String llegadasTardiaAdm(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(value = "admAparato", required = false) Integer numeroAparato,
			@RequestParam(value = "admAnioAparato", required = false) String fecha,
			Model model) {
		// Parsear fecha
		Integer mes = null;
		Integer anio = null;
		if (fecha != null && !fecha.isEmpty()) {
			String[] partes = fecha.split("-");
			if (partes.length == 2) {
				mes = Integer.parseInt(partes[0]);
				anio = Integer.parseInt(partes[1]);
			}
		}
		// Configurar paginacion
		PageRequest pageable = PageRequest.of(page, size);
		// Obtener la pagina de asistencias con filtros y paginacion
		Page<Map<String, Object>> asistenciaAdm = asistenciaAdmService.listarAdministrativosTardefiltro(numeroAparato, mes, anio, pageable);
		model.addAttribute("asistenciaPersonal", asistenciaAdm.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", asistenciaAdm.getTotalPages());
		model.addAttribute("size", size);
		//lista nombre
		List<Map<String, Object>> admLista = asistenciaAdmService.obtenerAdmTardia();
		model.addAttribute("admLista", admLista);

		List<Map<String, Object>> admMesAnioAdm = asistenciaAdmService.obtenerMesesAdm();
		model.addAttribute("mesesFiltro", admMesAnioAdm);

		return "Asistencia_personal/Personal_asistencia/asistenciaTardiaAdm";
	}

}
