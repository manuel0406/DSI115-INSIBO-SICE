package com.dsi.insibo.sice.Expediente_alumno;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Calificaciones.NotaService;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.AnexoAlumno;
import com.dsi.insibo.sice.entity.Bachillerato;

/**
 * Controlador para gestionar las operaciones del expediente de alumnos.
 */
@Controller
@RequestMapping("/ExpedienteAlumno")
@PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
public class AlumnoController {

	@Autowired
	private AlumnoService alumnoService;
	@Autowired
	private BachilleratoService bachilleratoService;
	@Autowired
	private NotaService notaService;
	@Autowired
	private AnexoAlumnoService anexoAlumnoService;

	/**
	 * Controlador para guardar un nuevo alumno en la base de datos.
	 * 
	 * Este método maneja las solicitudes POST a la ruta "/guardar" y guarda un
	 * nuevo alumno.
	 * Si el número de identificación estudiantil (NIE) proporcionado ya existe,
	 * redirige a la vista
	 * de listado de alumnos con un mensaje de error.
	 *
	 * @param alumno     El objeto Alumno que contiene la información del nuevo
	 *                   alumno.
	 * @param attributes Atributos de redirección utilizados para pasar mensajes
	 *                   flash.
	 * @return Una cadena que redirige a la vista de listado de alumnos.
	 */
	@PostMapping("/guardar")
	public String guardarAlumno(@ModelAttribute Alumno alumno, RedirectAttributes attributes,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion) {

		// Busca si el NIE proporcionado ya existe
		Alumno alumnoExistente = alumnoService.buscarPorIdAlumno(alumno.getNie());
		Bachillerato bachillerato = bachilleratoService.debolverBachilleratoMatricula(carrera, seccion, grado);

		// Evalúa si el alumno ya existe y, de ser así, manda un mensaje de error y
		// redirige
		if (alumnoExistente != null) {
			attributes.addFlashAttribute("error", "Error: El NIE ya está registrado.");
			return "redirect:/matriculados";
		}

		if (alumno.getPadecimientos().isEmpty()) {
			alumno.setPadecimientos("No");
		}
		if (alumno.getMedicamento().isEmpty()) {
			alumno.setMedicamento("No");
		}
		if (alumno.getFormaMedicacion().isEmpty()) {
			alumno.setFormaMedicacion("No");
		}

		if (bachillerato != null) {
			System.out.println("bachillerato no nulo");
			alumno.setBachillerato(bachillerato);
		}
		try {
			// Guarda el nuevo alumno
			alumnoService.guardarAlumno(alumno);
			attributes.addFlashAttribute("success", "¡Alumno guardado con éxito!");
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "¡Error no se puede guardar el registro!");
			e.printStackTrace();
		}

		// Redirige a la vista de listado de alumnos
		return "redirect:/matriculados";
	}

	/**
	 * Controlador para desplegar la vista de registro de un nuevo alumno.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/Crear" y muestra un
	 * formulario
	 * para registrar un nuevo alumno. Proporciona una lista de bachilleratos
	 * extraída de
	 * la base de datos y un objeto Alumno para asociarlo a los campos del
	 * formulario.
	 *
	 * @param model El modelo de Spring utilizado para pasar datos a la vista.
	 * @return El nombre de la vista "Expediente_alumno/registro".
	 */
	@GetMapping("/Crear")
	public String crear(Model model) {

		// Crea un nuevo objeto Alumno
		Alumno alumno = new Alumno();

		// Obtener la lista de carreras (bachilleratos)
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(true);

		// Añade atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Crear Alumno");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("matricula", "true");

		// Retorna el nombre de la vista de registro de alumnos
		return "Expediente_alumno/registro";
	}

	@GetMapping("/secciones")
	public ResponseEntity<List<String>> getSecciones(@RequestParam("carrera") String carrera,
			@RequestParam("grado") String grado, @RequestParam("matricula") String matricula) {
		if (carrera == null || grado == null || carrera.isEmpty() || grado.isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		}
		boolean matriculaActiva = false;
		if (matricula.equals("true")) {
			matriculaActiva = true;
		}
		List<String> secciones = bachilleratoService.getSeccionesByCarrera(carrera, grado, matriculaActiva);
		return ResponseEntity.ok(secciones); // Esto asegurará que Spring lo serialice como JSON
	}

	/**
	 * Controlador para editar la información de un alumno específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/editar/{idAlumno}" y permite
	 * editar
	 * la información de un alumno basado en su número de identificación estudiantil
	 * (NIE).
	 * Redirige a la vista de edición con un formulario prellenado si el alumno
	 * existe.
	 *
	 * @param nie        El número de identificación estudiantil del alumno (NIE).
	 * @param model      El modelo de Spring utilizado para pasar datos a la vista.
	 * @param attributes Atributos de redirección utilizados para pasar mensajes
	 *                   flash.
	 * @return El nombre de la vista "Expediente_alumno/editar" si el alumno existe,
	 *         de lo contrario redirige a la vista de listado de alumnos.
	 */
	@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
	@GetMapping("/editar/{idAlumno}")
	public String editar(@PathVariable("idAlumno") int idAlumno, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (idAlumno > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(idAlumno);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/ExpedienteAlumno/ver";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/ExpedienteAlumno/ver";
		}

		String carrera, grado, seccion;
		carrera = alumno.getBachillerato().getNombreCarrera();
		grado = String.valueOf(alumno.getBachillerato().getGrado());
		seccion = alumno.getBachillerato().getSeccion();

		// Si el alumno existe, obtiene la lista de bachilleratos para el formulario
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(false);
		model.addAttribute("titulo", "Editar");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("editar", true); // Indica que se está en modo edición
		model.addAttribute("carrera", carrera);
		model.addAttribute("grado", grado);
		model.addAttribute("seccion", seccion);
		model.addAttribute("url", "/ExpedienteAlumno/actualizar");
		model.addAttribute("btnCancelar", "/ExpedienteAlumno/ver");
		model.addAttribute("matricula", "false");

		// Retorna el nombre de la vista de edición del alumno
		return "Expediente_alumno/editar";
	}

	/**
	 * Controlador para guardar las actualizaciones de un alumno.
	 * 
	 * Este método maneja las solicitudes POST a la ruta "/actualizar" y guarda las
	 * actualizaciones
	 * realizadas a un alumno. Redirige a la vista de listado de alumnos con un
	 * mensaje de éxito.
	 *
	 * @param alumno     El objeto Alumno que contiene la información actualizada
	 *                   del alumno.
	 * @param attributes Atributos de redirección utilizados para pasar mensajes
	 *                   flash.
	 * @return Una cadena que redirige a la vista de listado de alumnos.
	 */
	@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
	@PostMapping("/actualizar")
	public String actualizarAlumno(@ModelAttribute Alumno alumno, RedirectAttributes attributes,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion) {

		try {

			Bachillerato bachillerato = bachilleratoService.debolverBachillerato(carrera, seccion, grado);
			alumno.setBachillerato(bachillerato);
			// Guarda el alumno con la información actualizada
			alumnoService.guardarAlumno(alumno);

			// Añade un mensaje flash indicando que la actualización fue exitosa
			attributes.addFlashAttribute("success", "¡Alumno actualizado con éxito!");
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "¡Error no se pude actualizar el registro!");
			e.printStackTrace();
		}

		// Redirige a la vista de listado de alumnos
		return "redirect:/ExpedienteAlumno/ver";
	}

	/**
	 * Controlador para eliminar un alumno específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/delete/{idAlumno}" y elimina el
	 * registro
	 * de un alumno basado en su número de identificación estudiantil (NIE).
	 * Redirige a la vista
	 * de listado de alumnos con un mensaje de confirmación o error.
	 *
	 * @param nie        El número de identificación estudiantil del alumno (NIE).
	 * @param attributes Atributos de redirección utilizados para pasar mensajes
	 *                   flash.
	 * @return Una cadena que redirige a la vista de listado de alumnos.
	 */
	@GetMapping("/delete/{idAlumno}")
	public String eliminarAlumno(@PathVariable("idAlumno") int idAlumno, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (idAlumno > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(idAlumno);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/ExpedienteAlumno/ver";
			}
		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/ExpedienteAlumno/ver";
		}

		try {
			// Elimino primero los anexos relacionados al alumno encontrado
			anexoAlumnoService.eliminarAnexoAlumno(idAlumno);
			// Se eliminan las notas relacionadas a ese alumno
			notaService.deleteNotasByAlumnoNie(idAlumno);
			// Elimina el registro del alumno y añade un mensaje de confirmación
			alumnoService.eliminar(idAlumno);
			attributes.addFlashAttribute("warning", "¡Registro eliminado con éxito!");
		} catch (Exception e) {
			attributes.addFlashAttribute("error",
					"¡No se puede eliminar este alumno, primero eliminar sus datos relacionados!");
			e.printStackTrace();
		}

		return "redirect:/ExpedienteAlumno/ver"; // Redirige a la vista de listado de alumnos
	}

	/**
	 * Controlador para manejar la vista de los alumnos.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/ver" y permite ver una
	 * lista de alumnos filtrada por los parámetros opcionales de carrera, grado y
	 * sección.
	 * Los parámetros se reciben a través de la URL y, si están vacíos, se
	 * convierten a null
	 * para evitar problemas con las consultas.
	 *
	 * @param model   El modelo de Spring utilizado para pasar datos a la vista.
	 * @param carrera El parámetro opcional de la carrera del alumno. Puede ser
	 *                null.
	 * @param grado   El parámetro opcional del grado del alumno. Puede ser null.
	 * @param seccion El parámetro opcional de la sección del alumno. Puede ser
	 *                null.
	 * @param page    El número de página actual para la paginación, con valor
	 *                predeterminado 1.
	 * @param size    El tamaño de la página para la paginación, con valor
	 *                predeterminado 10.
	 * @return El nombre de la vista "Expediente_alumno/verAlumno".
	 */
	@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA' )")
	@GetMapping("/ver")
	public String verAlumno(Model model,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion,
			@RequestParam(value = "genero", required = false) String genero,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "50") int size) {

		// Convertir cadenas vacías a null para evitar problemas con las consultas
		if (carrera != null && carrera.isEmpty()) {
			carrera = null;
		}
		if (grado != null && grado.isEmpty()) {
			grado = null;
		}
		if (seccion != null && seccion.isEmpty()) {
			seccion = null;
		}
		if (genero != null && genero.isEmpty()) {
			genero = null;
		}

		// Obtener la lista completa de alumnos filtrada por los parámetros
		List<Alumno> listaAlumnos = alumnoService.listarAlumnos(carrera, grado, seccion, genero);
		// Ordenar la lista por "apellidoAlumno"
		listaAlumnos.sort(Comparator.comparing(Alumno::getApellidoAlumno));
		// for (Alumno alumno : listaAlumnos) {
		// System.out.println("Seccion: "+alumno.getBachillerato().getSeccion() + "
		// Genero: "+alumno.getSexoAlumno());
		// }
		// Crear una estructura paginada manualmente
		PageRequest pageRequest = PageRequest.of(page - 1, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), listaAlumnos.size());
		Page<Alumno> pageAlumnos = new PageImpl<>(listaAlumnos.subList(start, end), pageRequest, listaAlumnos.size());

		// Obtener la lista de carreras (bachilleratos)
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(false);

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Alumnos");
		model.addAttribute("alumnos", pageAlumnos.getContent());
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("carrera", carrera);
		model.addAttribute("grado", grado);
		model.addAttribute("seccion", seccion);
		model.addAttribute("genero", genero);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", pageAlumnos.getTotalPages());
		model.addAttribute("totalElements", listaAlumnos.size());
		model.addAttribute("url", "/ExpedienteAlumno/ver");
		model.addAttribute("matricula", false);
		int baseIndex = (page - 1) * size;// sirve para mantener la base de la numeración de lo alumnos cuando cambia de
											// pagina
		model.addAttribute("baseIndex", baseIndex);
		model.addAttribute("urlBtnEditar", "/ExpedienteAlumno/editar/");
		model.addAttribute("urlBtnVer", "/ExpedienteAlumno/Alumno/");
		model.addAttribute("urlBtnEli", "/ExpedienteAlumno/delete/");
		model.addAttribute("navMatriculados", false);

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/verAlumno";
	}

	/**
	 * Controlador para manejar la vista de la información de un alumno específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/Alumno/{idAlumno}" y muestra la
	 * información
	 * detallada de un alumno basado en su número de identificación estudiantil
	 * (NIE).
	 *
	 * @param idAlumno   
	 * @param model El modelo de Spring utilizado para pasar datos a la vista.
	 * @return El nombre de la vista "Expediente_alumno/AlumnoInformacion".
	 */
	@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
	@GetMapping("/Alumno/{idAlumno}")
	public String informacionAlumno(@PathVariable("idAlumno") int idAlumno, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (idAlumno > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(idAlumno);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/ExpedienteAlumno/ver";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/ExpedienteAlumno/ver";
		}

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Información");
		model.addAttribute("alumno", alumno);
		model.addAttribute("urlInfo", "/ExpedienteAlumno/Alumno/");
		model.addAttribute("urlEnf", "/ExpedienteAlumno/Enfermedades/");
		model.addAttribute("urlResp", "/ExpedienteAlumno/Responsable/");
		model.addAttribute("urlDoc", "/ExpedienteAlumno/Documentos/");
		model.addAttribute("sanciones", true);
		model.addAttribute("btnRegresa", "/ExpedienteAlumno/ver");
		// model.addAttribute("bachillerato", bachillerato);

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/AlumnoInformacion";
	}

	/**
	 * Controlador para manejar la vista de las enfermedades de un alumno
	 * específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/Enfermedades/{idAlumno}" y
	 * muestra la información
	 * de los padecimientos de un alumno basado en su número de identificación
	 * estudiantil (NIE).
	 *
	 * @param nie   El número de identificación estudiantil del alumno (NIE).
	 * @param model El modelo de Spring utilizado para pasar datos a la vista.
	 * @return El nombre de la vista "Expediente_alumno/AlumnoEnfermedad".
	 */
	@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
	@GetMapping("/Enfermedades/{idAlumno}")
	public String enfermedadAlumno(@PathVariable("idAlumno") int idAlumno, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (idAlumno > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(idAlumno);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/ExpedienteAlumno/ver";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/ExpedienteAlumno/ver";
		}

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Padecimientos");
		model.addAttribute("alumno", alumno);
		model.addAttribute("urlInfo", "/ExpedienteAlumno/Alumno/");
		model.addAttribute("urlEnf", "/ExpedienteAlumno/Enfermedades/");
		model.addAttribute("urlResp", "/ExpedienteAlumno/Responsable/");
		model.addAttribute("urlDoc", "/ExpedienteAlumno/Documentos/");
		model.addAttribute("sanciones", true);
		model.addAttribute("btnRegresa", "/ExpedienteAlumno/ver");

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/AlumnoEnfermedad";
	}

	/**
	 * Controlador para manejar la vista del responsable de un alumno específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/Responsable/{idAlumno}" y
	 * muestra la información
	 * del encargado de un alumno basado en su número de identificación estudiantil
	 * (NIE).
	 *
	 * @param    El número de identificación estudiantil del alumno (NIE).
	 * @param model El modelo de Spring utilizado para pasar datos a la vista.
	 * @return El nombre de la vista "Expediente_alumno/AlumnoDatosResponsable".
	 */
	@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
	@GetMapping("/Responsable/{idAlumno}")
	public String responsableAlumno(@PathVariable("idAlumno") int idAlumno, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (idAlumno > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(idAlumno);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/ExpedienteAlumno/ver";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/ExpedienteAlumno/ver";
		}

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("alumno", alumno);
		model.addAttribute("titulo", "Encargado");
		model.addAttribute("urlInfo", "/ExpedienteAlumno/Alumno/");
		model.addAttribute("urlEnf", "/ExpedienteAlumno/Enfermedades/");
		model.addAttribute("urlResp", "/ExpedienteAlumno/Responsable/");
		model.addAttribute("urlDoc", "/ExpedienteAlumno/Documentos/");
		model.addAttribute("sanciones", true);
		model.addAttribute("btnRegresa", "/ExpedienteAlumno/ver");

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/AlumnoDatosResponsable";
	}

	@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
	@GetMapping("/Documentos/{idAlumno}")
	public String alumnoDocumentos(@PathVariable("idAlumno") int idAlumno, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (idAlumno > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(idAlumno);

			// Verifica que el alumno exista
			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/ExpedienteAlumno/ver";
			}

		} else {
			// Maneja el caso donde el NIE no es válido
			System.out.println("Error: ¡El NIE ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
			return "redirect:/ExpedienteAlumno/ver";
		}

		// Obtener los anexos asociados al alumno
		AnexoAlumno anexos = anexoAlumnoService.buscarAlumno(idAlumno);

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("alumno", alumno);
		model.addAttribute("anexos", anexos);
		model.addAttribute("titulo", "Documentos");
		model.addAttribute("urlInfo", "/ExpedienteAlumno/Alumno/");
		model.addAttribute("urlEnf", "/ExpedienteAlumno/Enfermedades/");
		model.addAttribute("urlResp", "/ExpedienteAlumno/Responsable/");
		model.addAttribute("urlDoc", "/ExpedienteAlumno/Documentos/");
		model.addAttribute("sanciones", true);
		model.addAttribute("btnRegresa", "/ExpedienteAlumno/ver");

		return "Expediente_alumno/AlumnoDocumentos";
	}

	/**
	 * Controlador para generar un documento PDF con la lista de alumnos.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/ver" y genera un documento
	 * PDF
	 * que contiene una lista de alumnos, filtrada opcionalmente por carrera, grado
	 * y sección.
	 * La respuesta HTTP tiene el tipo de contenido "application/pdf".
	 *
	 * @param model   El modelo de Spring utilizado para pasar datos a la vista.
	 * @param carrera El parámetro opcional de la carrera del alumno. Puede ser
	 *                null.
	 * @param grado   El parámetro opcional del grado del alumno. Puede ser null.
	 * @param seccion El parámetro opcional de la sección del alumno. Puede ser
	 *                null.
	 * @return Un objeto ModelAndView que contiene la vista
	 *         "Expediente_alumno/verAlumnoPdf"
	 *         y los datos necesarios para generar el PDF.
	 */
	@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
	@GetMapping(value = "/ver", produces = "application/pdf")
	public ModelAndView verAlumnosPdf(Model model,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion,
			@RequestParam(value = "genero", required = false) String genero) {
		// Convertir cadenas vacías a null para los parámetros opcionales
		if (carrera != null && carrera.isEmpty()) {
			carrera = null;
		}
		if (grado != null && grado.isEmpty()) {
			grado = null;
		}
		if (seccion != null && seccion.isEmpty()) {
			seccion = null;
		}
		if (genero != null && genero.isEmpty()) {
			genero = null;
		}

		// Obtener la lista de alumnos filtrada por los parámetros
		List<Alumno> listaAlumnos = alumnoService.listarAlumnos(carrera, grado, seccion, genero);
		// Obtener la lista de carreras (bachilleratos)
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(false);

		// Crear un objeto ModelAndView con la vista "Expediente_alumno/verAlumnoPdf"
		ModelAndView modelAndView = new ModelAndView("Expediente_alumno/verAlumnoPdf");

		// Agregar atributos al ModelAndView para ser utilizados en la vista
		model.addAttribute("titulo", "Alumnos");
		modelAndView.addObject("alumnos", listaAlumnos);
		modelAndView.addObject("bachilleratos", listaCarreras);
		modelAndView.addObject("carrera", carrera);
		modelAndView.addObject("grado", grado);
		modelAndView.addObject("seccion", seccion);
		modelAndView.addObject("genero", genero);

		// Retornar el objeto ModelAndView que contiene la vista y los datos
		return modelAndView;
	}

}
