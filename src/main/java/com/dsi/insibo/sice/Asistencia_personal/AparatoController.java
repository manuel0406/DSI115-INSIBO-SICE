package com.dsi.insibo.sice.Asistencia_personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Asistencia_personal.Aparato_Docente.AparatoDocenteService;
import com.dsi.insibo.sice.Asistencia_personal.Aparato_Personal.AparatoPersonalService;
import com.dsi.insibo.sice.Expediente_docente.Administrativos.AdministrativoDTO;
import com.dsi.insibo.sice.Expediente_docente.Administrativos.AdministrativoService;
import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteDTO;
import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteService;
import com.dsi.insibo.sice.entity.Docente;
import com.dsi.insibo.sice.entity.DocenteAparato;
import com.dsi.insibo.sice.entity.PersonalAdministrativo;
import com.dsi.insibo.sice.entity.PersonalAparato;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Page;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/aparato")
public class AparatoController {

	/*
	 * /aparato/editarNumeroDocente/1
	 * SELECT * FROM personal_aparato;
	 * SELECT * FROM docente_aparato;
	 */
	@Autowired
	private AparatoDocenteService aparatoDocenteService;

	@Autowired
	private AparatoPersonalService aparatoPersonalService;

	@Autowired
	private DocenteService docenteService;

	@Autowired
	private AdministrativoService administrativoService;

	// LISTAR LISTA TANTO DOCENTE COMO ADMINISTRATIVOS
	@GetMapping("/listarAparato")
	public String listarApartoDocente(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			Model model) {
		// Configurar paginación
		PageRequest pageable = PageRequest.of(page, size);

		// Obtener la página de aparatos para docentes y administrativos
		Page<DocenteAparato> listadoAparatoDocentePag = aparatoDocenteService.aparatoTodosDoc(pageable);
		Page<PersonalAparato> listadoAparatoAdmiPag = aparatoPersonalService.aparatoTodosAdm(pageable);

		// Asignar paginación al modelo
		model.addAttribute("aparatoDocente", listadoAparatoDocentePag);
		model.addAttribute("aparatoPersonal", listadoAparatoAdmiPag);
		int totalPages = Math.max(listadoAparatoDocentePag.getTotalPages(), listadoAparatoAdmiPag.getTotalPages()); // Total
																													// mayor
		model.addAttribute("currentPage", page); // Pagina actual
		model.addAttribute("size", size); // Tamaño de la pagina
		model.addAttribute("totalPages", totalPages); // Total de paginas

		// Objeto docentes y administrativo aparato para el llenado en formulario
		DocenteAparato docenteAparato = new DocenteAparato(); // Instancia Objeto aparato docente
		model.addAttribute("docenteAparato", docenteAparato);
		PersonalAparato personalAparato = new PersonalAparato(); // Instancia Objeto aparato personal
		model.addAttribute("personalAparato", personalAparato);

		// Query para llenado de listado de docentes y personal
		List<Docente> listadoDocentes = docenteService.docentes(); // Listado de docentes
		model.addAttribute("listaDocentes", listadoDocentes);
		List<PersonalAdministrativo> listadoPersonal = administrativoService.personal(); // Listado de personal
		model.addAttribute("listaAdministrativo", listadoPersonal);

		// Query para validar identificador no repetidos
		List<Integer> JSONDocente = aparatoDocenteService.obtenerAparatoDocente(); // Obtenemos ID Aparato Docente
		List<Integer> JSONPersonal = aparatoPersonalService.obtenerAparatoPersonal(); // Obtenermos ID Aparato Personal
		JSONDocente.addAll(JSONPersonal); // Uno Listas
		List<Integer> JSONID = JSONDocente; // Asigno una lista para mayor legibilidad

		// Convertir objetos a JSON
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String JSONAparato = objectMapper.writeValueAsString(JSONID); // Conversion de aparatoDocente a JSON
			model.addAttribute("JSONAparato", JSONAparato);

		} catch (JsonProcessingException e) {
			System.out.println("Error al convertir a JSON: " + e);
		}

		// Retornar la vista
		return "Aparato_Asistencia/aparatoListar";
	}

	// METODO GUARDAR DOCENTE
	@PostMapping("/guardarNumeroDocente")
	public String guardarDocenteAp(@Valid @ModelAttribute DocenteAparato docenteAparato,
			@RequestParam("docente") String docenteDui, BindingResult result, Model model,
			RedirectAttributes attribute) {
		List<DocenteDTO> listadoDocentes = docenteService.listarDocentes();
		if (result.hasErrors()) {
			model.addAttribute("docenteAparato", docenteAparato);
			model.addAttribute("Docentes", listadoDocentes);
			// attribute.addFlashAttribute("error","El número ya esta asignado.");
			return "Aparato_Asistencia/agregarNumeroAparato";
		}

		Docente docente = docenteService.buscarPorIdDocente(docenteDui);
		docenteAparato.setDocente(docente);
		aparatoDocenteService.guardarDocenteAparato(docenteAparato);
		attribute.addFlashAttribute("error", "Asignado con exito.");
		return "redirect:/aparato/listarAparato";
	}

	// METODO GUARDAR ADMINISTRATIVO
	@PostMapping("/guardarNumeroPersonal")
	public String guardaPersonalAp(@Valid @ModelAttribute PersonalAparato personalAparato,
			@RequestParam("personal") String personalDui, BindingResult result, Model model,
			RedirectAttributes attribute) {
		List<AdministrativoDTO> listadoPersonales = administrativoService.listarAdministrativos();
		if (result.hasErrors()) {
			model.addAttribute("personalAparato", personalAparato);
			model.addAttribute("personal", listadoPersonales);
			attribute.addFlashAttribute("error", "El número ya esta asignado.");
			return "Aparato_Asistencia/agregarNumeroAparatoPersonal";
		}
		PersonalAdministrativo administrativos = administrativoService.buscarPorIdAdministrativo(personalDui);
		personalAparato.setPersonal(administrativos);
		aparatoPersonalService.guardarPersonalAparato(personalAparato);
		attribute.addFlashAttribute("error", "Asignado con exito.");
		return "redirect:/aparato/listarAparato";
	}

	// METODO ACTUALIZAR ADMINISTRATIVO
	@PostMapping("/actualizarNumeroPersonal")
	public String actualizarPersonalAp(@RequestParam("personalAsignado") String duiPersonal,
			@RequestParam("numAparatoPersonal") int numAparato,
			@RequestParam("idAparatoPersonal") int idAparato,
			RedirectAttributes redirectAttributes) {
		PersonalAparato personalAparato = aparatoPersonalService.buscarPorIdAparatoPersonal(idAparato);
		if (personalAparato == null) {
			redirectAttributes.addFlashAttribute("error", "El ID no existe");
			return "redirect:/aparato/listarAparato";
		}

		personalAparato.setNumeroAparatoPersonal(numAparato);
		aparatoPersonalService.guardarPersonalAparato(personalAparato);
		return "redirect:/aparato/listarAparato";
	}

	// METODO ACTUALIZAR DOCENTE
	@PostMapping("/actualizarNumeroDocente")
	public String actualizarDocenteAp(@RequestParam("docenteAsignado") String duiDocente,
			@RequestParam("numAparatoDocente") int numAparato,
			@RequestParam("idAparatoDocente") int idAparato,
			RedirectAttributes redirectAttributes) {
		DocenteAparato docenteAparato = aparatoDocenteService.buscarPorIdAparatoDocente(idAparato);
		if (docenteAparato == null) {
			redirectAttributes.addFlashAttribute("error", "El ID no existe");
			return "redirect:/aparato/listarAparato";
		}

		docenteAparato.setNumeroAparatoDocente(numAparato);
		aparatoDocenteService.guardarDocenteAparato(docenteAparato);
		return "redirect:/aparato/listarAparato";
	}

	// METODO ELIMINAR DOCENTE
	@GetMapping("/eliminarNumeroDocente/{id}")
	public String eliminarNumeroDocente(@PathVariable("id") int idAparato, Model model, RedirectAttributes attribute) {
		DocenteAparato docenteAparato = null;
		if (idAparato > 0) {
			docenteAparato = aparatoDocenteService.buscarPorIdAparatoDocente(idAparato);
			if (docenteAparato == null) {
				// error: el id del aparato docente no existe
				attribute.addFlashAttribute("error", "El ID no existe.");
				return "redirect:/aparato/listarAparato";
			}
		} else {
			// error: el id del aparato docente
			attribute.addFlashAttribute("error", "Error con el ID.");
			return "redirect:/aparato/listarAparato";
		}
		aparatoDocenteService.eliminarDocenteAparato(idAparato);
		attribute.addFlashAttribute("warning", "Registro eliminado con exito.");
		return "redirect:/aparato/listarAparato";
	}

	// METODO ELIMINAR ADMINISTRATIVO
	@GetMapping("/eliminarNumeroAdmnistrativo/{id}")
	public String eliminarNumeroAdministrativo(@PathVariable("id") int idAparato, Model model,
			RedirectAttributes attribute) {
		PersonalAparato personalAparato = null;
		if (idAparato > 0) {
			personalAparato = aparatoPersonalService.buscarPorIdAparatoPersonal(idAparato);
			if (personalAparato == null) {
				// error: el id del aparato docente no existe
				attribute.addFlashAttribute("error", "El ID no existe.");
				return "redirect:/aparato/listarAparato";
			}
		} else {
			// error: el id del aparato docente
			attribute.addFlashAttribute("error", "Error con el ID.");
			return "redirect:/aparato/listarAparato";
		}
		aparatoPersonalService.eliminarPersonalAparato(idAparato);
		attribute.addFlashAttribute("warning", "Registro eliminado con exito.");
		return "redirect:/aparato/listarAparato";
	}

}
