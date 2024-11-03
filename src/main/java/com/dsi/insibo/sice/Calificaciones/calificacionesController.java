package com.dsi.insibo.sice.Calificaciones;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Administrativo.Materias.ServiciosMaterias.AsignacionService;
import com.dsi.insibo.sice.Administrativo.Materias.ServiciosMaterias.MateriasService;
import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteService;
import com.dsi.insibo.sice.Seguridad.SeguridadService.SessionService;
import com.dsi.insibo.sice.entity.Actividad;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Asignacion;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.Materia;
import com.dsi.insibo.sice.entity.Nota;
import com.dsi.insibo.sice.entity.NotaMateria;
import com.dsi.insibo.sice.entity.NotaPeriodo;
import com.dsi.insibo.sice.entity.Periodo;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Calificaciones")
public class calificacionesController {
	@Autowired
	private BachilleratoService bachilleratoService;
	@Autowired
	private MateriasService materiaService;
	@Autowired
	private AlumnoService alumnoService;
	@Autowired
	private NotaService notaService;
	@Autowired
	private ActividadService actividadService;
	@Autowired
	private AsignacionService asignacionService;
	@Autowired
	private SessionService sesion;
	@Autowired
	private PeriodoService periodoService;
	@Autowired
	private DocenteService docenteService;
	@Autowired
	private NotaPeriodoService notaPeriodoService;
	@Autowired
	private NotaMateriaService notaMateriaService;

	// @Autowired
	// private MateriaBachilleratoRepository materiaBachilleratoRepository;

	@GetMapping("/{idMateria}/{codigoBachillerato}")
	public String verCalificaciones(Model model, @PathVariable("idMateria") int idMateria,
			@PathVariable("codigoBachillerato") int codigoBachillerato,
			@RequestParam(value = "pe", required = false) String pe) {
		String dui = sesion.duiSession();
		// System.out.println(dui + " " + codigoBachillerato);

		if (pe != null && pe.isEmpty()) {
			pe = null;
		}

		NotaPeriodo notaPeriodo = null;

		Asignacion asignacion = asignacionService.asignacionParaActividad(dui, idMateria, codigoBachillerato);
		if (asignacion.getMateria().getTipoMateria().equals("Módulo")) {
			pe = "1";
			System.out.println("entro");
		}
		List<Actividad> listadoActividades = actividadService.listaActividades(dui,
				asignacion.getMateria().getIdMateria(), pe, codigoBachillerato);
		List<Nota> listaNotas = notaService.listaNotaActividadBachillerato(asignacion.getDocente().getDuiDocente(),
				asignacion.getBachillerato().getCodigoBachillerato(), pe, asignacion.getMateria().getIdMateria());
		List<Alumno> listaAlumnos = alumnoService
				.alumnosPorBachilerato(asignacion.getBachillerato().getCodigoBachillerato());

		// Crear un mapa para almacenar las notas por alumno y actividad
		Map<Integer, Map<Integer, Nota>> notasPorAlumno = new HashMap<>();
		// Crear un mapa para almacenar el total de notas por alumno
		Map<Integer, Double> totalNotasPorAlumno = new HashMap<>();

		// Organizar las notas por alumno y actividad
		for (Nota nota : listaNotas) {
			int idAlumno = nota.getAlumno().getIdAlumno();
			int idActividad = nota.getActividad().getIdActividad();

			notasPorAlumno
					.computeIfAbsent(idAlumno, k -> new HashMap<>())
					.put(idActividad, nota);

			// Calcular el total ponderado de notas por alumno
			BigDecimal notaObtenida = BigDecimal.valueOf(nota.getNotaObtenida());
			BigDecimal ponderacion = BigDecimal.valueOf(nota.getActividad().getPonderacionActividad())
					.divide(BigDecimal.valueOf(100));
			BigDecimal notaPonderada = notaObtenida.multiply(ponderacion).setScale(2, RoundingMode.HALF_UP);

			// Sumar al total de notas por alumno
			totalNotasPorAlumno.merge(idAlumno, notaPonderada.doubleValue(), Double::sum);

			notaPeriodo = notaPeriodoService.notaPeriodoAlumno(idAlumno, pe, idMateria, codigoBachillerato, dui);
			if (notaPeriodo != null) {
				notaPeriodo.setNotaPeriodo(totalNotasPorAlumno.get(idAlumno).floatValue());
				notaPeriodoService.guardarNotaPeriodo(notaPeriodo);
			} else {
				notaPeriodo = new NotaPeriodo();
				Alumno alumno = alumnoService.buscarPorIdAlumno(idAlumno);
				Periodo periodo = periodoService.periodoPorId(nota.getActividad().getPeriodo().getIdPeriodo());

				notaPeriodo.setAlumno(alumno);
				notaPeriodo.setAsignacion(asignacion);
				notaPeriodo.setPeriodo(periodo);
				notaPeriodo.setNotaPeriodo(totalNotasPorAlumno.get(idAlumno).floatValue());
				notaPeriodoService.guardarNotaPeriodo(notaPeriodo);

			}
		}
		// notaPeriodoService.procesarNotaPeriodo(asignacion, dui, pe,
		// codigoBachillerato);

		Map<String, Long> conteoPorTipo = listadoActividades.stream()
				.collect(Collectors.groupingBy(Actividad::getTipoActividad, Collectors.counting()));

		// Convertir el mapa a una lista de ActividadDTO
		List<ActividadDTO> actividadDTOList = conteoPorTipo.entrySet().stream()
				.map(entry -> new ActividadDTO(entry.getKey(), entry.getValue().intValue()))
				.sorted(Comparator.comparing(ActividadDTO::getNombreActividad)) // Ordenar por nombre
				.collect(Collectors.toList());

		// Agregar la lista ordenada al modelo
		model.addAttribute("actividadDTOList", actividadDTOList);

		List<Periodo> periodos = periodoService.listaPeriodos();
		model.addAttribute("titulo", "Cuadro de nota");
		model.addAttribute("periodos", periodos);
		model.addAttribute("listadoActividades", listadoActividades);
		model.addAttribute("listaAlumnos", listaAlumnos);
		model.addAttribute("notasPorAlumno", notasPorAlumno);
		model.addAttribute("totalNotasPorAlumno", totalNotasPorAlumno);
		model.addAttribute("asignacion", asignacion);
		model.addAttribute("pe", pe);

		return "Calificaciones/vistaCalificaciones";
	}

	@PreAuthorize("hasAnyRole('DOCENTE')")
	@GetMapping("/registro/{idActividad}")
	public String registro(Model model, @PathVariable("idActividad") int idActividad) {

		Actividad actividad = actividadService.buscarActividadPorId(idActividad);
		List<Alumno> listaAlumno = alumnoService
				.alumnosPorBachilerato(actividad.getAsignacion().getBachillerato().getCodigoBachillerato());
		List<Nota> listaNotas = notaService.notasPorBachilleratoActivdad(actividad.getIdActividad());
		Asignacion asignacion = asignacionService.asignacionParaActividad(
				actividad.getAsignacion().getDocente().getDuiDocente(),
				actividad.getAsignacion().getMateria().getIdMateria(),
				actividad.getAsignacion().getBachillerato().getCodigoBachillerato());

		// se crea un Set para almacenar los IDs de alumnos que ya tienen notas
		Set<Integer> idsAlumnosConNotas = new HashSet<>();

		// Llenar el Set con los IDs de los alumnos que ya tienen una nota
		for (Nota nota : listaNotas) {
			idsAlumnosConNotas.add(nota.getAlumno().getIdAlumno());
		}
		// Iterar sobre la lista de alumnos
		for (Alumno alumno : listaAlumno) {
			// Verificar si el alumno ya tiene una nota
			if (!idsAlumnosConNotas.contains(alumno.getIdAlumno())) {
				// System.out.println("Se puede guardar: " + alumno.getNombreAlumno());
				// Al no ecnontrar registro se crea una nueva nota
				Nota nota = new Nota();
				nota.setNotaObtenida(0);
				nota.setActividad(actividad);
				nota.setAlumno(alumno);
				notaService.guardarNota(nota);
			}
		}
		Nota nota = new Nota();
		listaNotas = notaService.notasPorBachilleratoActivdad(actividad.getIdActividad());

		model.addAttribute("titulo", "Registro notas");
		model.addAttribute("asignacion", asignacion);
		model.addAttribute("actividad", actividad);
		model.addAttribute("listadoNotas", listaNotas);
		model.addAttribute("nota", nota);
		return "Calificaciones/registroCalificaciones";
	}

	@PostMapping("/registro/add")
	public String guardarCalificacion(@ModelAttribute Nota nota, RedirectAttributes redirectAttributes) {

		nota.setFechaModificacion(new Date());

		notaService.guardarNota(nota);
		try {
			notaPeriodoService.procesarNotaPeriodo(nota);
			System.out.println("Entro al try");
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

		try {
			notaMateriaService.procesarNotaMateria(nota.getActividad().getAsignacion(),
					nota.getActividad().getAsignacion().getDocente().getDuiDocente(),
					String.valueOf(nota.getActividad().getPeriodo().getNumeroPeriodo()),
					nota.getActividad().getAsignacion().getBachillerato().getCodigoBachillerato());
			System.out.println("Entro al try de materia");
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

		redirectAttributes.addFlashAttribute("success", "Calificación actualizada exitosamente.");
		return "redirect:/Calificaciones/registro/" + nota.getActividad().getIdActividad();
	}

	// verAlumno(Model model,
	// @RequestParam(value = "carrera", required = false) String carrera,
	// @RequestParam(value = "grado", required = false) String grado,
	// @RequestParam(value = "seccion", required = false) String seccion,

	@GetMapping("/General")
	public String verCalificacioneGeneral(Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "idMateria", required = false) String idMateria,
			@RequestParam(value = "docente", required = false) String docente,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion,
			@RequestParam(value = "pe", required = false) String pe) {

		// Validaciones para los parámetros vacíos
		if (carrera != null && carrera.isEmpty()) {
			carrera = null;
		}
		if (grado != null && grado.isEmpty()) {
			grado = null;
		}
		if (seccion != null && seccion.isEmpty()) {
			seccion = null;
		}
		if (pe != null && pe.isEmpty()) {
			pe = null;
		}
		if (idMateria != null && idMateria.isEmpty()) {
			idMateria = null;
		}
		if (docente != null && docente.isEmpty()) {
			docente = null;
		}

		String dui = docente;

		// Obtener el bachillerato
		Bachillerato bachillerato = bachilleratoService.debolverBachillerato(carrera, seccion, grado);
		int codigoBachillerato = 0;

		if (bachillerato != null) {
			codigoBachillerato = bachillerato.getCodigoBachillerato();
			Asignacion asignacion = asignacionService.asignacionParaActividad(dui, idMateria, codigoBachillerato);

			// Validación si asignación es nula
			if (asignacion != null) {
				if ("Módulo".equals(asignacion.getMateria().getTipoMateria())) {
					pe = "1";
					System.out.println("entro");
				}

				List<Actividad> listadoActividades = actividadService.listaActividades(dui,
						asignacion.getMateria().getIdMateria(), pe, codigoBachillerato);
				List<Nota> listaNotas = notaService.listaNotaActividadBachillerato(
						asignacion.getDocente().getDuiDocente(),
						asignacion.getBachillerato().getCodigoBachillerato(),
						pe, asignacion.getMateria().getIdMateria());
				List<Alumno> listaAlumnos = alumnoService.alumnosPorBachilerato(
						asignacion.getBachillerato().getCodigoBachillerato());

				// Crear un mapa para almacenar las notas por alumno y actividad
				Map<Integer, Map<Integer, Nota>> notasPorAlumno = new HashMap<>();
				// Crear un mapa para almacenar el total de notas por alumno
				Map<Integer, Double> totalNotasPorAlumno = new HashMap<>();

				for (Nota nota : listaNotas) {
					int idAlumno = nota.getAlumno().getIdAlumno();
					int idActividad = nota.getActividad().getIdActividad();

					notasPorAlumno.computeIfAbsent(idAlumno, k -> new HashMap<>()).put(idActividad, nota);

					BigDecimal notafinal = new BigDecimal(
							nota.getNotaObtenida() * (nota.getActividad().getPonderacionActividad() / 100.0));
					notafinal = notafinal.setScale(2, RoundingMode.HALF_UP);
					totalNotasPorAlumno.merge(idAlumno, notafinal.doubleValue(), Double::sum);
				}

				Map<String, Long> conteoPorTipo = listadoActividades.stream()
						.collect(Collectors.groupingBy(Actividad::getTipoActividad, Collectors.counting()));
				List<ActividadDTO> actividadDTOList = conteoPorTipo.entrySet().stream()
						.map(entry -> new ActividadDTO(entry.getKey(), entry.getValue().intValue()))
						.sorted(Comparator.comparing(ActividadDTO::getNombreActividad)) // Ordenar por nombre
						.collect(Collectors.toList());

				model.addAttribute("actividadDTOList", actividadDTOList);
				model.addAttribute("listadoActividades", listadoActividades);
				model.addAttribute("listaAlumnos", listaAlumnos);
				model.addAttribute("notasPorAlumno", notasPorAlumno);
				model.addAttribute("totalNotasPorAlumno", totalNotasPorAlumno);
				model.addAttribute("asignacion", asignacion);
			} else {
				System.out.println("La asignación solicitada no existe");
				redirectAttributes.addFlashAttribute("info", "No existe esa asignación");
				return "redirect:/Calificaciones/General";
			}
		}

		// Cargar listas de periodos, carreras y materias
		List<Periodo> periodos = periodoService.listaPeriodos();
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(false);
		List<Materia> listaMaterias = materiaService.obtenerMaterias();

		model.addAttribute("titulo", "Gestion de calificaciones");
		model.addAttribute("docentes", docenteService.listarDocenteAsignacion());
		model.addAttribute("periodos", periodos);
		model.addAttribute("pe", pe);
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("listaMaterias", listaMaterias);
		model.addAttribute("carrera", carrera);
		model.addAttribute("grado", grado);
		model.addAttribute("seccion", seccion);
		model.addAttribute("idMateria", idMateria);
		model.addAttribute("docente", docente);

		return "Calificaciones/vistaCalificaciones";
	}

	@GetMapping("/alumnos")
	public String verAlumnosCalificaciones(Model model,
			@RequestParam(value = "carrera", required = false) String carrera,
			@RequestParam(value = "grado", required = false) String grado,
			@RequestParam(value = "seccion", required = false) String seccion,
			@RequestParam(value = "genero", required = false) String genero) {

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
		List<Alumno> listaAlumnos = new ArrayList<>();

		if (carrera != null || grado != null || seccion != null || genero != null) {
			// Obtener la lista completa de alumnos filtrada por los parámetros
			listaAlumnos = alumnoService.listarAlumnos(carrera, grado, seccion, genero);
			// Ordenar la lista por "apellidoAlumno"
			listaAlumnos.sort(Comparator.comparing(Alumno::getApellidoAlumno));
		}

		// Obtener la lista de carreras (bachilleratos)
		List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(false);

		model.addAttribute("titulo", "Notas por alumno");
		model.addAttribute("bachilleratos", listaCarreras);
		model.addAttribute("carrera", carrera);
		model.addAttribute("grado", grado);
		model.addAttribute("seccion", seccion);
		model.addAttribute("alumnos", listaAlumnos);

		return "Calificaciones/listaAlumnoCalificaciones";
	}

	@GetMapping("/alumno/{idAlumno}")
	public String calificacionePorAlumno(Model model, @PathVariable("idAlumno") int idAlumno) {
		Alumno alumno = alumnoService.buscarPorIdAlumno(idAlumno);
		Map<Materia, Map<Integer, Map<String, List<Nota>>>> notasAgrupadas = notaService.notasAgrupadas(alumno);

		// Mapa para almacenar las notas globales por materia y período
		Map<Materia, Map<Integer, Float>> notasGlobalesPorPeriodo = new HashMap<>();

		for (Map.Entry<Materia, Map<Integer, Map<String, List<Nota>>>> materiaEntry : notasAgrupadas.entrySet()) {
			Materia materia = materiaEntry.getKey();
			Map<Integer, Map<String, List<Nota>>> periodos = materiaEntry.getValue();

			for (Integer numeroPeriodo : periodos.keySet()) {
				Optional<NotaPeriodo> notaPeriodoOpt = notaPeriodoService.obtenerNotaPeriodo(alumno, numeroPeriodo,
						materia);
				if (notaPeriodoOpt.isPresent()) {
					Float notaGlobal = notaPeriodoOpt.get().getNotaPeriodo();
					notasGlobalesPorPeriodo.computeIfAbsent(materia, k -> new HashMap<>()).put(numeroPeriodo,
							notaGlobal);
				}
			}
		}

		model.addAttribute("titulo", "Calicaciones " + alumno.getNie());
		model.addAttribute("alumno", alumno);
		model.addAttribute("notasAgrupadas", notasAgrupadas);
		model.addAttribute("notasGlobalesPorPeriodo", notasGlobalesPorPeriodo);
		return "Calificaciones/CalificacionAlumno";
	}

	@GetMapping("/CalificacionPeriodos/{idMateria}/{codigoBachillerato}")
	public String calificacionPorMateria(Model model, @PathVariable("idMateria") int idMateria,
			@PathVariable("codigoBachillerato") int codigoBachillerato) {

		String dui = sesion.duiSession();
		NotaMateria notaMateria = null;
		Asignacion asignacion = asignacionService.asignacionParaActividad(dui, idMateria, codigoBachillerato);
		List<NotaPeriodo> notaPeriodos = notaPeriodoService.listadoNotasPeriodoMateria(idMateria, codigoBachillerato,
				dui);

		// Obtener lista de alumnos y ordenar por apellido en orden ascendente
		List<Alumno> listaAlumnos = alumnoService
				.alumnosPorBachilerato(asignacion.getBachillerato().getCodigoBachillerato());
		listaAlumnos.sort(Comparator.comparing(Alumno::getApellidoAlumno));

		// Usar LinkedHashMap para preservar el orden en listaAlumnos
		Map<Alumno, Map<Integer, Float>> notasPorAlumnoYPeriodo = new LinkedHashMap<>();
		Map<Alumno, Float> notaGlobalPorAlumno = new HashMap<>();

		boolean esModulo = "Módulo".equals(asignacion.getMateria().getTipoMateria());

		// Organizar las notas por alumno y periodo en función de listaAlumnos
		for (Alumno alumno : listaAlumnos) {
			Map<Integer, Float> notasPeriodo = new HashMap<>();
			for (NotaPeriodo nota : notaPeriodos) {
				if (nota.getAlumno().equals(alumno)) {
					int periodo = nota.getPeriodo().getIdPeriodo();
					notasPeriodo.put(periodo, nota.getNotaPeriodo());
				}
			}
			notasPorAlumnoYPeriodo.put(alumno, notasPeriodo);
		}
		// Calcular la nota global para cada alumno
		for (Alumno alumno : listaAlumnos) {
			Map<Integer, Float> notasPeriodo = notasPorAlumnoYPeriodo.getOrDefault(alumno, new HashMap<>());

			float notaGlobal = 0;
			if (esModulo) {
				// Si es "Módulo", solo considerar la nota del periodo 1
				notaGlobal = notasPeriodo.getOrDefault(1, 0.0f);
			} else {
				// Si no es "Módulo", calcular la nota global considerando todos los periodos
				for (int i = 1; i <= 4; i++) {
					float nota = notasPeriodo.getOrDefault(i, 0.0f);
					notaGlobal += nota * 0.25;
				}
			}
			notaGlobalPorAlumno.put(alumno, notaGlobal);

			// Guardar la nota en NotaMateria
			notaMateria = notaMateriaService.existeNotaMateria(alumno.getIdAlumno(), idMateria, dui,
					codigoBachillerato);
			if (notaMateria != null) {
				// System.out.println("si existe");
				// System.out.println("nota: "+ notaGlobal);
				notaMateria.setNotaMateria(notaGlobal);
				notaMateriaService.guardarNotaMateria(notaMateria);
			} else {
				notaMateria = new NotaMateria();
				notaMateria.setAlumno(alumno);
				notaMateria.setAsignacion(asignacion);
				notaMateria.setNotaMateria(notaGlobal);
				// System.out.println("nota: "+ notaGlobal);
				// System.out.println("neles");
				notaMateriaService.guardarNotaMateria(notaMateria);
			}
		}

		model.addAttribute("titulo", "Calificación general");
		model.addAttribute("asignacion", asignacion);
		model.addAttribute("notasPorAlumnoYPeriodo", notasPorAlumnoYPeriodo);
		model.addAttribute("notaGlobalPorAlumno", notaGlobalPorAlumno);
		model.addAttribute("listaAlumnos", listaAlumnos);

		return "Calificaciones/calificacionesPorPeriodo";
	}

	/*
	 * @GetMapping("calificaciones/materiasPorBachillerato")
	 * 
	 * @ResponseBody
	 * public List<Materia> getMateriasPorBachillerato(@RequestParam String
	 * codigoBachillerato) {
	 * List<MateriaBachillerato> materiaBachilleratoList =
	 * materiaBachilleratoRepository.findByBachilleratoCodigoBachillerato(
	 * codigoBachillerato);
	 * return
	 * materiaBachilleratoList.stream().map(MateriaBachillerato::getMateria).collect
	 * (Collectors.toList());
	 * }
	 * 
	 * @GetMapping("calificaciones/alumnosPorBachillerato")
	 * public String getAlumnosPorBachillerato(@RequestParam String
	 * codigoBachillerato, Model model) {
	 * List<Alumno> alumnos =
	 * alumnoService.findAlumnosByBachilleratoCodigoBachillerato(codigoBachillerato)
	 * ;
	 * model.addAttribute("alumnos", alumnos);
	 * 
	 * // Recuperar y agregar nuevamente la lista de Bachilleratos
	 * List<Bachillerato> listaBachilleratos =
	 * bachilleratoService.listaBachilleratos();
	 * model.addAttribute("grados", listaBachilleratos);
	 * return "Calificaciones/vistaCalificaciones";
	 * }
	 * 
	 * @GetMapping("/calificaciones/Alumno/{nie}")
	 * public String informacionAlumno(@PathVariable("nie") int nie, Model model) {
	 * // Recuperar la información del alumno por NIE
	 * Alumno alumno = alumnoService.buscarPorIdAlumno(nie);
	 * 
	 * List<Materia> materias = materiaService.listaMaterias();
	 * List<Periodo> periodos = periodoService.listaPeriodos();
	 * 
	 * model.addAttribute("materias", materias);
	 * model.addAttribute("periodos", periodos);
	 * 
	 * if (alumno != null) {
	 * model.addAttribute("alumno", alumno);
	 * 
	 * List<Nota> notas = notaService.findNotasByAlumno(alumno);
	 * // Asumiendo que tienes un servicio para buscar notas por alumno
	 * model.addAttribute("notas", notas);
	 * // Puedes agregar más atributos al modelo si es necesario, como las notas del
	 * alumno
	 * // model.addAttribute("notas", notasService.findNotasByAlumnoNie(nie));
	 * } else {
	 * // Manejar el caso donde no se encuentra el alumno (opcional)
	 * model.addAttribute("error", "Alumno no encontrado");
	 * }
	 * 
	 * 
	 * return "Calificaciones/AlumnoCalificaciones";
	 * }
	 */
}
