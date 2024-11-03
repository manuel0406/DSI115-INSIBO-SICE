package com.dsi.insibo.sice.Administrativo.Matricula;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.AnioService;
import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.Expediente_alumno.AnexoAlumnoService;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.AnexoAlumno;
import com.dsi.insibo.sice.entity.AnioAcademico;
import com.dsi.insibo.sice.entity.Bachillerato;

@Controller
public class MatriculaController {

	@Autowired
	AlumnoService alumnoService;
	@Autowired
	AnioService anioService;
	@Autowired
	BachilleratoService bachilleratoService;
	@Autowired
	private AnexoAlumnoService anexoAlumnoService;

	@GetMapping("/AntiguoIngreso")
	public String antiguoIngreso(Model model, @RequestParam(value = "nie", required = false) String nie,
			@RequestParam(value = "nombre", required = false) String nombre,
			@RequestParam(value = "apellido", required = false) String apellido) {

		AnioAcademico anioAcademico = anioService.activoMatricula();
		int anioActivo = anioAcademico.getAnio() - 1;

		List<Alumno> busqueda = alumnoService.matricula(nie, nombre, apellido, anioActivo);
		List<Alumno> listadoMatriculado = alumnoService.yaMatriculado();
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(true);

		List<Alumno> listado = new ArrayList<>();

		// Crear un Set para almacenar los NIEs de los alumnos matriculados
		Set<Integer> nieMatriculadosSet = new HashSet<>();

		// Llenar el Set con los NIEs de los alumnos matriculados
		for (Alumno alumnoM : listadoMatriculado) {
			nieMatriculadosSet.add(alumnoM.getNie());
		}

		// Iterar sobre la lista de búsqueda
		for (Alumno alumnoB : busqueda) {
			// Verificar si el NIE del alumnoB no está en el Set
			if (!nieMatriculadosSet.contains(alumnoB.getNie())) {
				listado.add(alumnoB);
				// System.out.println("Alumno añadido: " + alumnoB.getNombreAlumno()); //
				// Opcional, para verificar
			}
		}

		model.addAttribute("alumnos", listado);
		model.addAttribute("titulo", "Antiguo ingreso");
		// model.addAttribute("nie", nie);
		model.addAttribute("nombre", nombre);
		model.addAttribute("apellido", apellido);
		model.addAttribute("matricula", "true");
		model.addAttribute("bachilleratos", listaCarreras);

		return "Administrativo/GestionMatricula/antiguoIngreso";
	}

	@GetMapping("/fichaMatricula/{idAlumno}")
	public String fichaMatricula(@PathVariable("idAlumno") int idAlumno, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (idAlumno > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(idAlumno);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/matriculados";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/matriculados";
		}

		String carrera, grado, seccion;
		carrera = alumno.getBachillerato().getNombreCarrera();
		grado = String.valueOf(alumno.getBachillerato().getGrado());
		seccion = alumno.getBachillerato().getSeccion();

		// Si el alumno existe, obtiene la lista de bachilleratos para el formulario
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(true);
		model.addAttribute("titulo", "Ficha Matricula");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("editar", true); // Indica que se está en modo edición
		model.addAttribute("carrera", carrera);
		model.addAttribute("grado", grado);
		model.addAttribute("seccion", seccion);

		// Retorna el nombre de la vista de edición del alumno
		return "Expediente_alumno/registro";
	}

	@PostMapping("/saveMatricula/")
	public String saveMatricula(RedirectAttributes attributes, @RequestParam(value = "idAlumno", required = false) String idAlumno,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion) {

		Alumno alumno = alumnoService.buscarPorIdAlumno(Integer.parseInt(idAlumno));
		System.out.println("grado: " + grado);
		// System.out.println("idAlumno: " + alumnoa.getIdAlumno());

		// if (!alumno.isEstadoAlumno()) {
		// attributes.addFlashAttribute("error", "¡Este alumno no aprobo el año
		// anterior!");
		// return "redirect:/AntiguoIngreso";
		// }

		Bachillerato bachillerato = bachilleratoService.debolverBachilleratoMatricula(carrera, seccion, grado);

		if (bachillerato == null) {
			System.out.println("Bachillerato nulo");
		}
		Alumno alumnoNuevo = new Alumno(alumno.getNie(), alumno.getNombreAlumno(), alumno.getApellidoAlumno(),
				alumno.getSexoAlumno(), alumno.getFechaNacimientoAlumno(), alumno.getDuiAlumno(),
				alumno.getTelefonoAlumno(),
				alumno.getDireccionAlumno(), alumno.getMunicipioAlumno(), alumno.getDepartamentoAlumno(),
				alumno.getDistritoAlumno(),
				alumno.getZonaAlumno(), alumno.getCorreoAlumno(), alumno.getViveCon(), alumno.getMedicamento(),
				alumno.getNombreEncargado(),
				alumno.getApellidoEncargado(), alumno.getFormaMedicacion(), alumno.getTelefonoEncargado(),
				alumno.getPadecimientos(), alumno.getParentescoEncargado(), alumno.getLugarDeTrabajo(),
				alumno.getCorreoEncargado(),
				alumno.getDuiEncargado(), bachillerato);

		alumnoService.guardarAlumno(alumnoNuevo);
		attributes.addFlashAttribute("success", "¡Alumno guardado con éxito!");
		return "redirect:/matriculados";
	}

	@GetMapping("/matriculados")
	public String verAlumno(Model model,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion,
			@RequestParam(value = "genero", required = false) String genero,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "50") int size) {

		// Convertir cadenas vacías a null para evitar problemas con las consultas
		carrera = (carrera != null && carrera.isEmpty()) ? null : carrera;
		grado = (grado != null && grado.isEmpty()) ? null : grado;
		seccion = (seccion != null && seccion.isEmpty()) ? null : seccion;
		genero = (genero != null && genero.isEmpty()) ? null : genero;

		// Obtener la lista completa de alumnos
		List<Alumno> alumnosMatriculados = alumnoService.yaMatriculado();
		List<Alumno> listaAlumnos = new ArrayList<>();

		for (Alumno alumno : alumnosMatriculados) {
			boolean matches = true;

			if (carrera != null && !alumno.getBachillerato().getNombreCarrera().equals(carrera)) {
				matches = false;
			}
			if (grado != null && alumno.getBachillerato().getGrado() != Integer.parseInt(grado)) {
				matches = false;
			}
			if (seccion != null && !alumno.getBachillerato().getSeccion().equals(seccion)) {
				matches = false;
			}
			if (genero != null && !alumno.getSexoAlumno().equals(genero)) {
				matches = false;
			}

			if (matches) {
				listaAlumnos.add(alumno);
			}
		}

		// Ordenar la lista por "apellidoAlumno"
		listaAlumnos.sort(Comparator.comparing(Alumno::getApellidoAlumno));

		// Crear una estructura paginada manualmente
		PageRequest pageRequest = PageRequest.of(page - 1, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), listaAlumnos.size());
		Page<Alumno> pageAlumnos = new PageImpl<>(listaAlumnos.subList(start, end), pageRequest, listaAlumnos.size());

		// Obtener la lista de carreras (bachilleratos)
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(true);

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Matriculados");
		model.addAttribute("alumnos", pageAlumnos.getContent());
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("carrera", carrera);
		model.addAttribute("grado", grado);
		model.addAttribute("seccion", seccion);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", pageAlumnos.getTotalPages());
		model.addAttribute("totalElements", listaAlumnos.size());
		model.addAttribute("matricula", true);
		model.addAttribute("url", "/matriculados");
		int baseIndex = (page - 1) * size;// sirve para mantener la base de la numeración de lo alumnos cuando cambia de
											// pagina
		model.addAttribute("baseIndex", baseIndex);
		model.addAttribute("urlBtnEditar", "/matriculados/editar/");

		model.addAttribute("urlBtnVer", "/matriculados/Alumno/");
		model.addAttribute("urlBtnEli", "/matriculados/delete/");
		model.addAttribute("navMatriculados", true);
		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/verAlumno";
	}

	@GetMapping(value = "/matriculados", produces = "application/pdf")
	public ModelAndView verAlumnosPdf(Model model,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion,
			@RequestParam(value = "seccion", required = false) String genero) {
		// Convertir cadenas vacías a null para evitar problemas con las consultas
		carrera = (carrera != null && carrera.isEmpty()) ? null : carrera;
		grado = (grado != null && grado.isEmpty()) ? null : grado;
		seccion = (seccion != null && seccion.isEmpty()) ? null : seccion;
		genero = (genero != null && genero.isEmpty()) ? null : genero;

		// Obtener la lista completa de alumnos
		List<Alumno> alumnosMatriculados = alumnoService.yaMatriculado();
		List<Alumno> listaAlumnos = new ArrayList<>();

		for (Alumno alumno : alumnosMatriculados) {
			boolean matches = true;

			if (carrera != null && !alumno.getBachillerato().getNombreCarrera().equals(carrera)) {
				matches = false;
			}
			if (grado != null && alumno.getBachillerato().getGrado() != Integer.parseInt(grado)) {
				matches = false;
			}
			if (seccion != null && !alumno.getBachillerato().getSeccion().equals(seccion)) {
				matches = false;
			}
			if (genero != null && !alumno.getSexoAlumno().equals(genero)) {
				matches = false;
			}

			if (matches) {
				listaAlumnos.add(alumno);
			}
		}

		// Obtener la lista de carreras (bachilleratos)
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(true);

		// Crear un objeto ModelAndView con la vista "Expediente_alumno/verAlumnoPdf"
		ModelAndView modelAndView = new ModelAndView("Expediente_alumno/verAlumnoPdf");

		// Agregar atributos al ModelAndView para ser utilizados en la vista
		model.addAttribute("titulo", "Alumnos");
		modelAndView.addObject("alumnos", listaAlumnos);
		modelAndView.addObject("bachilleratos", listaCarreras);
		modelAndView.addObject("carrera", carrera);
		modelAndView.addObject("grado", grado);
		modelAndView.addObject("seccion", seccion);

		// Retornar el objeto ModelAndView que contiene la vista y los datos
		return modelAndView;
	}

	@GetMapping("/matriculados/editar/{nie}")
	public String editar(@PathVariable("nie") int nie, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(nie);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/matriculados";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/matriculados";
		}

		String carrera, grado, seccion;
		carrera = alumno.getBachillerato().getNombreCarrera();
		grado = String.valueOf(alumno.getBachillerato().getGrado());
		seccion = alumno.getBachillerato().getSeccion();

		// Si el alumno existe, obtiene la lista de bachilleratos para el formulario
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(true);
		model.addAttribute("titulo", "Editar");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("editar", true); // Indica que se está en modo edición
		model.addAttribute("carrera", carrera);
		model.addAttribute("grado", grado);
		model.addAttribute("seccion", seccion);
		model.addAttribute("url", "/matriculados/actualizar");
		model.addAttribute("btnCancelar", "/matriculados");
		model.addAttribute("matricula", "true");

		// Retorna el nombre de la vista de edición del alumno
		return "Expediente_alumno/editar";
	}

	@PostMapping("/matriculados/actualizar")
	public String actualizarAlumno(@ModelAttribute Alumno alumno, RedirectAttributes attributes,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion) {

		Bachillerato bachillerato = bachilleratoService.debolverBachilleratoMatricula(carrera, seccion, grado);
		alumno.setBachillerato(bachillerato);
		// Guarda el alumno con la información actualizada
		alumnoService.guardarAlumno(alumno);

		// Añade un mensaje flash indicando que la actualización fue exitosa
		attributes.addFlashAttribute("success", "¡Alumno actualizado con éxito!");

		// Redirige a la vista de listado de alumnos
		return "redirect:/matriculados";
	}

	@GetMapping("/matriculados/Alumno/{nie}")
	public String informacionAlumno(@PathVariable("nie") int nie, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(nie);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/matriculados";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/matriculados";
		}

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Información");
		model.addAttribute("alumno", alumno);
		model.addAttribute("urlInfo", "/matriculados/Alumno/");
		model.addAttribute("urlEnf", "/matriculados/Enfermedades/");
		model.addAttribute("urlResp", "/matriculados/Responsable/");
		model.addAttribute("urlDoc", "/matriculados/Documentos/");
		model.addAttribute("sanciones", false);
		model.addAttribute("btnRegresa", "/matriculados");

		// model.addAttribute("bachillerato", bachillerato);

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/AlumnoInformacion";
	}

	@GetMapping("/matriculados/Enfermedades/{nie}")
	public String enfermedadAlumno(@PathVariable("nie") int nie, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(nie);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/matriculados";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/matriculados";
		}

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Padecimientos");
		model.addAttribute("alumno", alumno);
		model.addAttribute("urlInfo", "/matriculados/Alumno/");
		model.addAttribute("urlEnf", "/matriculados/Enfermedades/");
		model.addAttribute("urlResp", "/matriculados/Responsable/");
		model.addAttribute("urlDoc", "/matriculados/Documentos/");
		model.addAttribute("sanciones", false);
		model.addAttribute("btnRegresa", "/matriculados");

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/AlumnoEnfermedad";
	}

	@GetMapping("/matriculados/Responsable/{nie}")
	public String responsableAlumno(@PathVariable("nie") int nie, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(nie);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/matriculados";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/matriculados";
		}

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("alumno", alumno);
		model.addAttribute("titulo", "Encargado");
		model.addAttribute("urlInfo", "/matriculados/Alumno/");
		model.addAttribute("urlEnf", "/matriculados/Enfermedades/");
		model.addAttribute("urlResp", "/matriculados/Responsable/");
		model.addAttribute("urlDoc", "/matriculados/Documentos/");
		model.addAttribute("sanciones", false);
		model.addAttribute("btnRegresa", "/matriculados");

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/AlumnoDatosResponsable";
	}

	@GetMapping("/matriculados/Documentos/{nie}")
	public String alumnoDocumentos(@PathVariable("nie") int nie, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(nie);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/matriculados";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/matriculados";
		}

		// Obtener los anexos asociados al alumno
		AnexoAlumno anexos = anexoAlumnoService.buscarAlumno(nie);

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("alumno", alumno);
		model.addAttribute("anexos", anexos);
		model.addAttribute("titulo", "Documentos");
		model.addAttribute("urlInfo", "/matriculados/Alumno/");
		model.addAttribute("urlEnf", "/matriculados/Enfermedades/");
		model.addAttribute("urlResp", "/matriculados/Responsable/");
		model.addAttribute("urlDoc", "/matriculados/Documentos/");
		model.addAttribute("sanciones", false);
		model.addAttribute("btnRegresa", "/matriculados");

		return "Expediente_alumno/AlumnoDocumentos";
	}

	@GetMapping("/matriculados/delete/{nie}")
	public String eliminarAlumno(@PathVariable("nie") int nie, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(nie);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/matriculados";
			}
		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/matriculados";
		}

		// Elimino primero los anexos relacionados al alumno encontrado
		anexoAlumnoService.eliminarAnexoAlumno(nie);
		// Se eliminan las notas relacionadas a ese alumno
		// notaService.deleteNotasByAlumnoNie(nie);
		// Elimina el registro del alumno y añade un mensaje de confirmación
		alumnoService.eliminar(nie);
		attributes.addFlashAttribute("warning", "¡Registro eliminado con éxito!");
		return "redirect:/matriculados"; // Redirige a la vista de listado de alumnos
	}
}
