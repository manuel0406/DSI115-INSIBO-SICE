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

@Controller
@RequestMapping("/ExpedienteAlumno")
public class AlumnoController {

	@Autowired
	private AlumnoService alumnoService;
	@Autowired
	private BachilleratoService bachilleratoService;

	@PostMapping("/guardar")
	public String guardarAlumno(@ModelAttribute Alumno alumno, RedirectAttributes attributes) {
		// Busca el alumno existente en la base de datos utilizando el NIE proporcionado
		Alumno alumnoExistente = alumnoService.buscarPorIdAlumno(alumno.getNie());

		if (alumnoExistente != null) {
			attributes.addFlashAttribute("error", "Error: El NIE ya está registrado.");
			return "redirect:/ExpedienteAlumno/ver";
		}

		// Guarda o actualiza el alumno
		alumnoService.guardarAlumno(alumno);
		attributes.addFlashAttribute("success", "¡Alumno guardado con éxito!");
		return "redirect:/ExpedienteAlumno/ver";
	}

	@GetMapping("/Crear")
	public String crear(Model model) {

		Alumno alumno = new Alumno();

		List<Bachillerato> listaBachilleratos = bachilleratoService.listaBachilleratos();
		model.addAttribute("titulo", "Crear Alumno");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachilleratos", listaBachilleratos);

		return "Expediente_alumno/registro";
	}

	@GetMapping("/editar/{nie}")
	public String editar(@PathVariable("nie") int nie, Model model, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			alumno = alumnoService.buscarPorIdAlumno(nie);

			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/ExpedienteAlumno/ver";
			}

		} else {
			System.out.println("Error: ¡El NIE ingresado no es valido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es valido!");
			return "redirect:/ExpedienteAlumno/ver";

		}

		List<Bachillerato> listaBachilleratos = bachilleratoService.listaBachilleratos();
		model.addAttribute("titulo", "Editar");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachilleratos", listaBachilleratos);
		model.addAttribute("editar", true); // Indica que se está editando un docente
		return "Expediente_alumno/editar";
	}

	@PostMapping("/actualizar")
	public String actualizarAlumno(@ModelAttribute Alumno alumno, RedirectAttributes attributes) {
		// Busca el alumno existente en la base de datos utilizando el NIE proporcionado

		alumnoService.guardarAlumno(alumno);
		attributes.addFlashAttribute("success", "¡Alumno actualizado con éxito!");
		return "redirect:/ExpedienteAlumno/ver";
	}

	@GetMapping("/delete/{nie}")
	public String eliminar(@PathVariable("nie") int nie, RedirectAttributes attributes) {

		Alumno alumno = null;
		if (nie > 0) {
			alumno = alumnoService.buscarPorIdAlumno(nie);

			if (alumno == null) {
				System.out.println("Error: ¡El NIE ingresado no existe!");
				attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no existe!");
				return "redirect:/ExpedienteAlumno/ver";
			}

		} else {
			System.out.println("Error: ¡El NIE ingresado no es valido!");
			attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es valido!");
			return "redirect:/ExpedienteAlumno/ver";

		}

		alumnoService.eliminar(nie);
		attributes.addFlashAttribute("warning", "¡Registro eliminado con exito!");
		return "redirect:/ExpedienteAlumno/ver";
	}

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
		List<Alumno> listaAlumnos = alumnoService.listarAlumnos(carrera, grado, seccion);
		// List<Bachillerato> listaBachilleratos =
		// bachilleratoService.listaBachilleratos();
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera();

		model.addAttribute("titulo", "Ver");
		model.addAttribute("alumnos", listaAlumnos);
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("carrera", carrera);
		model.addAttribute("grado", grado);
		model.addAttribute("seccion", seccion);

		return "Expediente_alumno/verAlumno";
	}

	@GetMapping("/Alumno/{nie}")
	public String infomacionAlumno(@PathVariable("nie") int nie, Model model) {

		Alumno alumno = alumnoService.buscarPorIdAlumno(nie);
		Bachillerato bachillerato = alumno.getBachillerato();
		model.addAttribute("titulo", "Información");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachillerato", bachillerato);
		return "Expediente_alumno/AlumnoInformacion";
	}

	@GetMapping("/Enfermedades/{nie}")
	public String enfermedadAlumno(@PathVariable("nie") int nie, Model model) {

		Alumno alumno = alumnoService.buscarPorIdAlumno(nie);
		Bachillerato bachillerato = alumno.getBachillerato();
		model.addAttribute("titulo", "Padecimientos");
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachillerato", bachillerato);
		return "Expediente_alumno/AlumnoEnfermedad";
	}

	@GetMapping("/Responsable/{nie}")
	public String responsableAlumno(@PathVariable("nie") int nie, Model model) {

		Alumno alumno = alumnoService.buscarPorIdAlumno(nie);
		Bachillerato bachillerato = alumno.getBachillerato();
		model.addAttribute("alumno", alumno);
		model.addAttribute("bachillerato", bachillerato);
		model.addAttribute("titulo", "Encargado");
		return "Expediente_alumno/AlumnoDatosResponsable";  
	}

	@GetMapping(value = "/ver", produces = "application/pdf")
	public ModelAndView verAlumnosPdf(Model model,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion) {
		if (carrera != null && carrera.isEmpty()) {
			carrera = null;
		}
		if (grado != null && grado.isEmpty()) {
			grado = null;
		}
		if (seccion != null && seccion.isEmpty()) {
			seccion = null;
		}
		List<Alumno> listaAlumnos = alumnoService.listarAlumnos(carrera, grado, seccion);
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera();

		ModelAndView modelAndView = new ModelAndView("Expediente_alumno/verAlumnoPdf");
		modelAndView.addObject("alumnos", listaAlumnos);
		modelAndView.addObject("bachilleratos", listaCarreras);
		modelAndView.addObject("carrera", carrera);
		modelAndView.addObject("grado", grado);
		modelAndView.addObject("seccion", seccion);

		return modelAndView;
	}

}
