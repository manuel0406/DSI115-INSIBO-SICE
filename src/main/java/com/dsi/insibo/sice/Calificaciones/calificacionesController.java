package com.dsi.insibo.sice.Calificaciones;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

		//List<Materia> listaMaterias = materiaService.listaMaterias();
		//model.addAttribute("materias", listaMaterias);

		return "Calificaciones/vistaCalificaciones";
	}

	@GetMapping("calificaciones/materiasPorBachillerato")
    @ResponseBody
    public List<Materia> getMateriasPorBachillerato(@RequestParam String codigoBachillerato) {
        List<MateriaBachillerato> materiaBachilleratoList = materiaBachilleratoRepository.findByBachilleratoCodigoBachillerato(codigoBachillerato);
        return materiaBachilleratoList.stream().map(MateriaBachillerato::getMateria).collect(Collectors.toList());
    }

	@GetMapping("calificaciones/alumnosPorBachillerato")
    @ResponseBody
    public List<Alumno> getAlumnosPorBachillerato(@RequestParam String codigoBachillerato) {
        return alumnoService.findAlumnosByBachilleratoCodigoBachillerato(codigoBachillerato);
    }

	@GetMapping("calificaciones/actividadesPorMateria")
    @ResponseBody
    public List<Actividad> getActividadesPorMateria(@RequestParam String codMateria) {
        return actividadService.findActividadesByMateriaCodMateria(codMateria);
    }
	
	@GetMapping("calificaciones/notasPorAlumno")
    @ResponseBody
    public List<Nota> getNotasPorAlumno(@RequestParam int nie, @RequestParam String codMateria, @RequestParam int idPeriodo) {
        return notaService.findNotasByAlumnoMateriaPeriodo(nie, codMateria, idPeriodo);
    }
}
