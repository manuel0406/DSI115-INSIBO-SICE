package com.dsi.insibo.sice.Calificaciones;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Administrativo.Materias.ServiciosMaterias.AsignacionService;
import com.dsi.insibo.sice.Seguridad.SeguridadService.SessionService;
import com.dsi.insibo.sice.entity.Actividad;
import com.dsi.insibo.sice.entity.Asignacion;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.Periodo;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/Actividad")
public class actividadesController {

	@Autowired
	private PeriodoService periodoService;
	@Autowired
	private ActividadService actividadService;
	@Autowired
	BachilleratoService bachilleratosService;

	@Autowired
	SessionService sesion;
	@Autowired
	AsignacionService asignacionService;
	@Autowired
	private NotaService notaService;

	@PreAuthorize("hasAnyRole('DOCENTE')")
	@GetMapping("/{idMateria}/{codigoBachillerato}")
	public String verActividades(Model model, @PathVariable("idMateria") int idMateria,
			@PathVariable("codigoBachillerato") int codigoBachillerato,
			RedirectAttributes attributes, @RequestParam(value = "pe", required = false) String pe) {

		// System.out.println("el periodo es: " + pe);
		Bachillerato bachillerato = null;
		if (pe != null && pe.isEmpty()) {
			pe = null;
		}
		if (codigoBachillerato > 0) {
			// Busca al bachillerato por su codigo
			bachillerato = bachilleratosService.bachilleratoPorId(codigoBachillerato);

			// Verifica que el bachillerato exista
			if (bachillerato == null) {
				System.out.println("Error: ¡El código ingresado no existe");
				attributes.addFlashAttribute("error", "Error: ¡El código ingresado no existe");
				return "redirect:/Actividad/" + codigoBachillerato;
			}

		} else {
			// Maneja el caso donde el codigo no es válido
			System.out.println("Error: ¡El código ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El código ingresado no es válido!");
			return "redirect:/Actividad/" + codigoBachillerato;
		}
		// Se extra el listado de periodos existentes
		List<Periodo> periodos = periodoService.listaPeriodos();
		// Objecto activada nuevo
		Actividad actividad = new Actividad();

		// Acá se obtienen el objeto asignación correspondiente al docente y
		// bachillerato
		String dui = sesion.duiSession();
		// System.out.println(dui + " " + codigoBachillerato);
		Asignacion asignacion = asignacionService.asignacionParaActividad(dui, idMateria, codigoBachillerato);

		if (asignacion.getMateria().getTipoMateria().equals("Módulo")) {
			pe = "1";
			System.out.println("entro: "+pe);

		}
		// Listado de las actividaes que ha creado un docente por bachillerato
		List<Actividad> listadoActividades = actividadService.listaActividades(dui, idMateria, pe, codigoBachillerato);

		// System.out.println(asignacion.getMateria().getTipoMateria());
		model.addAttribute("actividad", actividad);
		model.addAttribute("periodos", periodos);
		model.addAttribute("listadoActividades", listadoActividades);
		model.addAttribute("asignacion", asignacion);
		model.addAttribute("titulo", "Actividades");
		// System.out.println("periodo abajo: " + pe);
		model.addAttribute("pe", pe);

		return "Calificaciones/vistaActividades";
	}

	@PreAuthorize("hasAnyRole('DOCENTE')")
	@PostMapping("/add")
	public String guardarActividad(@ModelAttribute Actividad actividad, RedirectAttributes redirectAttributes) {

		int periodoId = actividad.getPeriodo().getIdPeriodo();
		float totalPonderacion = actividadService.calcularTotalPonderacion(actividad);
		float actividadCotidiana = actividadService.calcularPonderacionPorTipo(actividad, "Actividad cotidiana");
		float actividadIntegradora = actividadService.calcularPonderacionPorTipo(actividad, "Actividad integradora");

		// Validar si la ponderación de actividades cotidianas o integradoras supera el
		// límite
		if (actividadCotidiana > 30) {
			redirectAttributes.addFlashAttribute("error",
					"¡Error: la suma de ponderaciones de actividades cotidianas no debe superar el 30%!");
			return generarRedirect(actividad, periodoId);
		}

		if (actividadIntegradora > 30) {
			redirectAttributes.addFlashAttribute("error",
					"¡Error: la suma de ponderaciones de actividades integradoras no debe superar el 30%!");
			return generarRedirect(actividad, periodoId);
		}

		// Validar si la ponderación total supera el 100%
		if (totalPonderacion <= 100) {
			actividadService.guardarActividad(actividad);
			redirectAttributes.addFlashAttribute("success", "Actividad agregada exitosamente.");
		} else {
			redirectAttributes.addFlashAttribute("error",
					"La suma de ponderaciones no debe superar el 100%. Por favor, verifique los porcentajes asignados.");
		}

		return generarRedirect(actividad, periodoId);
	}

	@PreAuthorize("hasAnyRole('DOCENTE')")
	@GetMapping("/{idMateria}/{codigoBachillerato}/delete/{idActividad}")
	public String eliminarActividad(@PathVariable("idMateria") int idMateria,
			@PathVariable("codigoBachillerato") int codigoBachillerato,
			@PathVariable("idActividad") int idActividad, RedirectAttributes attributes) {
		int pe = 0;
		Actividad actividad = null;
		if (idActividad > 0) {
			// Busca al bachillerato por su codigo
			actividad = actividadService.buscarActividadPorId(idActividad);

			// Verifica que el bachillerato exista
			if (actividad == null) {
				// System.out.println("Error: ¡No exite este idActividad no existe");
				attributes.addFlashAttribute("error", "Error: ¡El idActividad ingresado no existe");
				return "redirect:/Actividad/" + idMateria + "/" + codigoBachillerato;
			}
			// Si la actividad existe, obtener el periodo
			pe = actividad.getPeriodo().getIdPeriodo();
		} else {
			// Maneja el caso donde el codigo no es válido
			// System.out.println("Error: ¡El idActividad ingresado no es válido!");
			attributes.addFlashAttribute("error", "Error: ¡El idActividad ingresado no es válido!");
			return "redirect:/Actividad/" + idMateria + "/" + codigoBachillerato;
		}
		// Elimina el registro
		notaService.deleteNotaActividad(idActividad);
		actividadService.eliminarActividad(idActividad);
		attributes.addFlashAttribute("warning", "¡Registro eliminado con éxito!");

		return "redirect:/Actividad/" + idMateria + "/" + codigoBachillerato + "?pe=" + pe;
	}

	// Método para generar la URL de redirección
	private String generarRedirect(Actividad actividad, int periodoId) {
		return "redirect:/Actividad/" + actividad.getAsignacion().getMateria().getIdMateria() + "/"
				+ actividad.getAsignacion().getBachillerato().getCodigoBachillerato() + "?pe=" + periodoId;
	}

}
