package com.dsi.insibo.sice.Expediente_alumno;

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
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Bachillerato;

/**
 * Controlador para gestionar las operaciones del expediente de alumnos.
 */
@Controller
@RequestMapping("/ExpedienteAlumno")
public class AlumnoController {

	@Autowired
	private AlumnoService alumnoService;
	@Autowired
	private BachilleratoService bachilleratoService;

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
	public String guardarAlumno(@ModelAttribute Alumno alumno, RedirectAttributes attributes) {

		// Busca si el NIE proporcionado ya existe
		Alumno alumnoExistente = alumnoService.buscarPorIdAlumno(alumno.getNie());

		// Evalúa si el alumno ya existe y, de ser así, manda un mensaje de error y
		// redirige
		if (alumnoExistente != null) {
			attributes.addFlashAttribute("error", "Error: El NIE ya está registrado.");
			return "redirect:/ExpedienteAlumno/ver";
		}

		// Guarda el nuevo alumno
		alumnoService.guardarAlumno(alumno);
		attributes.addFlashAttribute("success", "¡Alumno guardado con éxito!");

		// Redirige a la vista de listado de alumnos
		return "redirect:/ExpedienteAlumno/ver";
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

		// Extrae todos los bachilleratos registrados en la base de datos
		List<Bachillerato> listaBachilleratos = bachilleratoService.listaBachilleratos();

		// Añade atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Crear Alumno");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachilleratos", listaBachilleratos);

		// Retorna el nombre de la vista de registro de alumnos
		return "Expediente_alumno/registro";
	}

	/**
	 * Controlador para editar la información de un alumno específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/editar/{nie}" y permite
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
	@GetMapping("/editar/{nie}")
	public String editar(@PathVariable("nie") int nie, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(nie);

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

		// Si el alumno existe, obtiene la lista de bachilleratos para el formulario
		List<Bachillerato> listaBachilleratos = bachilleratoService.listaBachilleratos();
		model.addAttribute("titulo", "Editar");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachilleratos", listaBachilleratos);
		model.addAttribute("editar", true); // Indica que se está en modo edición

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
	@PostMapping("/actualizar")
	public String actualizarAlumno(@ModelAttribute Alumno alumno, RedirectAttributes attributes) {

		// Guarda el alumno con la información actualizada
		alumnoService.guardarAlumno(alumno);

		// Añade un mensaje flash indicando que la actualización fue exitosa
		attributes.addFlashAttribute("success", "¡Alumno actualizado con éxito!");

		// Redirige a la vista de listado de alumnos
		return "redirect:/ExpedienteAlumno/ver";
	}

	/**
	 * Controlador para eliminar un alumno específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/delete/{nie}" y elimina el
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
	@GetMapping("/delete/{nie}")
	public String eliminarAlumno(@PathVariable("nie") int nie, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			// Busca al alumno por su número de identificación estudiantil (NIE)
			alumno = alumnoService.buscarPorIdAlumno(nie);

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

		// Elimina el registro del alumno y añade un mensaje de confirmación
		alumnoService.eliminar(nie);
		attributes.addFlashAttribute("warning", "¡Registro eliminado con éxito!");
		return "redirect:/ExpedienteAlumno/ver"; // Redirige a la vista de listado de alumnos
	}

	/**
	 * Controlador para manejar la vista de los alumnos.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/ver" y permite ver una
	 * lista de alumnos
	 * filtrada por los parámetros opcionales de carrera, grado y sección. Los
	 * parámetros se reciben
	 * a través de la URL y, si están vacíos, se convierten a null para evitar
	 * problemas con las consultas.
	 *
	 * @param model   El modelo de Spring utilizado para pasar datos a la vista.
	 * @param carrera El parámetro opcional de la carrera del alumno. Puede ser
	 *                null.
	 * @param grado   El parámetro opcional del grado del alumno. Puede ser null.
	 * @param seccion El parámetro opcional de la sección del alumno. Puede ser
	 *                null.
	 * @return El nombre de la vista "Expediente_alumno/verAlumno".
	 */
	@GetMapping("/ver")
	public String verAlumno(Model model, @RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion) {

		// Convertir cadenas vacías a null
		if (carrera != null && carrera.isEmpty()) {
			carrera = null;
		}
		if (grado != null && grado.isEmpty()) {
			grado = null;
		}
		if (seccion != null && seccion.isEmpty()) {
			seccion = null;
		}
		// Obtener la lista de alumnos filtrada por los parámetros
		List<Alumno> listaAlumnos = alumnoService.listarAlumnos(carrera, grado, seccion);
		// Obtener la lista de carreras (bachilleratos)
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera();

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Ver");
		model.addAttribute("alumnos", listaAlumnos);
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("carrera", carrera);
		model.addAttribute("grado", grado);
		model.addAttribute("seccion", seccion);

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/verAlumno";
	}

	/**
	 * Controlador para manejar la vista de la información de un alumno específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/Alumno/{nie}" y muestra la
	 * información
	 * detallada de un alumno basado en su número de identificación estudiantil
	 * (NIE).
	 *
	 * @param nie   El número de identificación estudiantil del alumno (NIE).
	 * @param model El modelo de Spring utilizado para pasar datos a la vista.
	 * @return El nombre de la vista "Expediente_alumno/AlumnoInformacion".
	 */
	@GetMapping("/Alumno/{nie}")
	public String informacionAlumno(@PathVariable("nie") int nie, Model model) {

		// Buscar el alumno por su número de identificación estudiantil (NIE)
		Alumno alumno = alumnoService.buscarPorIdAlumno(nie);

		// Obtener el bachillerato asociado al alumno
		Bachillerato bachillerato = alumno.getBachillerato();

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Información");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachillerato", bachillerato);

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/AlumnoInformacion";
	}

	/**
	 * Controlador para manejar la vista de las enfermedades de un alumno
	 * específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/Enfermedades/{nie}" y
	 * muestra la información
	 * de los padecimientos de un alumno basado en su número de identificación
	 * estudiantil (NIE).
	 *
	 * @param nie   El número de identificación estudiantil del alumno (NIE).
	 * @param model El modelo de Spring utilizado para pasar datos a la vista.
	 * @return El nombre de la vista "Expediente_alumno/AlumnoEnfermedad".
	 */
	@GetMapping("/Enfermedades/{nie}")
	public String enfermedadAlumno(@PathVariable("nie") int nie, Model model) {

		// Buscar el alumno por su número de identificación estudiantil (NIE)
		Alumno alumno = alumnoService.buscarPorIdAlumno(nie);

		// Obtener el bachillerato asociado al alumno
		Bachillerato bachillerato = alumno.getBachillerato();

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("titulo", "Padecimientos");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachillerato", bachillerato);

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/AlumnoEnfermedad";
	}

	/**
	 * Controlador para manejar la vista del responsable de un alumno específico.
	 * 
	 * Este método maneja las solicitudes GET a la ruta "/Responsable/{nie}" y
	 * muestra la información
	 * del encargado de un alumno basado en su número de identificación estudiantil
	 * (NIE).
	 *
	 * @param nie   El número de identificación estudiantil del alumno (NIE).
	 * @param model El modelo de Spring utilizado para pasar datos a la vista.
	 * @return El nombre de la vista "Expediente_alumno/AlumnoDatosResponsable".
	 */
	@GetMapping("/Responsable/{nie}")
	public String responsableAlumno(@PathVariable("nie") int nie, Model model) {

		// Buscar el alumno por su número de identificación estudiantil (NIE)
		Alumno alumno = alumnoService.buscarPorIdAlumno(nie);

		// Obtener el bachillerato asociado al alumno
		Bachillerato bachillerato = alumno.getBachillerato();

		// Agregar atributos al modelo para ser utilizados en la vista
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachillerato", bachillerato);
		model.addAttribute("titulo", "Encargado");

		// Retornar el nombre de la vista a ser renderizada
		return "Expediente_alumno/AlumnoDatosResponsable";
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
	@GetMapping(value = "/ver", produces = "application/pdf")
	public ModelAndView verAlumnosPdf(Model model,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion) {
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

		// Obtener la lista de alumnos filtrada por los parámetros
		List<Alumno> listaAlumnos = alumnoService.listarAlumnos(carrera, grado, seccion);
		// Obtener la lista de carreras (bachilleratos)
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera();

		// Crear un objeto ModelAndView con la vista "Expediente_alumno/verAlumnoPdf"
		ModelAndView modelAndView = new ModelAndView("Expediente_alumno/verAlumnoPdf");

		// Agregar atributos al ModelAndView para ser utilizados en la vista
		modelAndView.addObject("alumnos", listaAlumnos);
		modelAndView.addObject("bachilleratos", listaCarreras);
		modelAndView.addObject("carrera", carrera);
		modelAndView.addObject("grado", grado);
		modelAndView.addObject("seccion", seccion);

		// Retornar el objeto ModelAndView que contiene la vista y los datos
		return modelAndView;
	}

}
