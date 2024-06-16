package com.dsi.insibo.sice.Calificaciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.entity.Actividad;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.Materia;
import com.dsi.insibo.sice.entity.MateriaBachillerato;
import com.dsi.insibo.sice.entity.Nota;
import com.dsi.insibo.sice.entity.Periodo;

import org.springframework.ui.Model;

@Controller
public class calificacionesController {
	@Autowired
	private GradoService gradoService;
	@Autowired
	private PeriodoService periodoService;
	@Autowired
	private MateriaService materiaService;
	@Autowired
	private ActividadService actividadService;
	@Autowired
    private AlumnoService alumnoService;
	@Autowired
    private NotaService notaService;


	@Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private PeriodoRepository periodoRepository;
	@Autowired
    private ActividadRepository actividadRepository;
	@Autowired
    private NotaRepository notaRepository;
	@Autowired
    private MateriaBachilleratoRepository materiaBachilleratoRepository;

    @GetMapping("/calificaciones")
	public String verCalificaciones(Model model){
		
		List<Bachillerato> listaBachilleratos = gradoService.listaBachilleratos();
		model.addAttribute("grados", listaBachilleratos);

		List<Periodo> listaPeriodos = periodoService.listaPeriodos();
		model.addAttribute("periodos", listaPeriodos);

		//List<Alumno> alumnos = alumnoService.obtenerAlumnosPorMateriaYPeriodo(codMateria, idPeriodo);
        
		//List<Materia> listaMaterias = materiaService.listaMaterias();
		//model.addAttribute("materias", listaMaterias);
		//model.addAttribute("materias", new ArrayList<Materia>());
        //model.addAttribute("alumnos", new ArrayList<Alumno>());

		return "Calificaciones/vistaCalificaciones";
	}

	@GetMapping("calificaciones/materiasPorBachillerato")
    @ResponseBody
    public List<Materia> getMateriasPorBachillerato(@RequestParam String codigoBachillerato) {
        List<MateriaBachillerato> materiaBachilleratoList = materiaBachilleratoRepository.findByBachilleratoCodigoBachillerato(codigoBachillerato);
        return materiaBachilleratoList.stream().map(MateriaBachillerato::getMateria).collect(Collectors.toList());
    }

	@GetMapping("calificaciones/alumnosPorBachillerato")
	public String getAlumnosPorBachillerato(@RequestParam String codigoBachillerato, Model model) {
		List<Alumno> alumnos = alumnoService.findAlumnosByBachilleratoCodigoBachillerato(codigoBachillerato);
		model.addAttribute("alumnos", alumnos);

		// Recuperar y agregar nuevamente la lista de Bachilleratos
		List<Bachillerato> listaBachilleratos = gradoService.listaBachilleratos();
		model.addAttribute("grados", listaBachilleratos);
		return "Calificaciones/vistaCalificaciones";
	}

	@GetMapping("/calificaciones/Alumno/{nie}")
	public String informacionAlumno(@PathVariable("nie") int nie, Model model) {
		// Recuperar la información del alumno por NIE
		Alumno alumno = alumnoService.buscarPorIdAlumno(nie);
		
		List<Materia> materias = materiaService.listaMaterias();
        List<Periodo> periodos = periodoService.listaPeriodos();

		model.addAttribute("materias", materias);
        model.addAttribute("periodos", periodos);

		if (alumno != null) {
			model.addAttribute("alumno", alumno);
			
			List<Nota> notas = notaService.findNotasByAlumno(alumno);
			 // Asumiendo que tienes un servicio para buscar notas por alumno
        	model.addAttribute("notas", notas);
			// Puedes agregar más atributos al modelo si es necesario, como las notas del alumno
			// model.addAttribute("notas", notasService.findNotasByAlumnoNie(nie));
		} else {
			// Manejar el caso donde no se encuentra el alumno (opcional)
			model.addAttribute("error", "Alumno no encontrado");
		}
		
		
		return "Calificaciones/AlumnoCalificaciones";
	}
}
