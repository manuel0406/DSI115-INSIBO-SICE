package com.dsi.insibo.sice.Calificaciones;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.entity.Actividad;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.Periodo;
import com.dsi.insibo.sice.entity.Materia;
import com.dsi.insibo.sice.entity.Actividad;
import com.dsi.insibo.sice.entity.MateriaBachillerato;

import org.springframework.ui.Model;

@Controller
public class actividadesController {

	@Autowired
	private GradoService gradoService;
	@Autowired
	private PeriodoService periodoService;
	@Autowired
	private MateriaService materiaService;
	@Autowired
	private ActividadService actividadService;


	@Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private PeriodoRepository periodoRepository;
	@Autowired
    private ActividadRepository actividadRepository;
	@Autowired
    private MateriaBachilleratoRepository materiaBachilleratoRepository;

    @GetMapping("/Actividades")
	public String verActividades(Model model){

		List<Bachillerato> listaBachilleratos = gradoService.listaBachilleratos();
		model.addAttribute("grados", listaBachilleratos);

		List<Periodo> listaPeriodos = periodoService.listaPeriodos();
		model.addAttribute("periodos", listaPeriodos);

		List<Materia> listaMaterias = materiaService.listaMaterias();
		model.addAttribute("materias", listaMaterias);

		List<Actividad> listaActividads = actividadService.listaActividades();
		model.addAttribute("actividades", listaActividads);

		model.addAttribute("actividad", new Actividad());

		return "Calificaciones/vistaActividades";
	}

	@PostMapping("/Actividades/add")
    public String addActividadFromModal(@ModelAttribute Actividad actividad, Model model) {
        actividadRepository.save(actividad);

        return "redirect:/Actividades";
    }

	@GetMapping("/Actividades/delete/{idActividad}")
	public String deleteActividad(@PathVariable Integer idActividad) {
		actividadRepository.deleteById(idActividad);

		return "redirect:/Actividades";
	}

	@GetMapping("/Actividades/edit/{idActividad}")
	@ResponseBody
	public Actividad getActividad(@PathVariable Integer idActividad) {
		return actividadRepository.findById(idActividad).orElse(null);
	}

	@PostMapping("/Actividades/edit")
	public String editActividad(@ModelAttribute Actividad actividad) {
		actividadRepository.save(actividad);
		return "redirect:/Actividades";
	}

	@GetMapping("Actividades/materiasPorBachillerato")
    @ResponseBody
    public List<Materia> getMateriasPorBachillerato(@RequestParam String codigoBachillerato) {
        List<MateriaBachillerato> materiaBachilleratoList = materiaBachilleratoRepository.findByBachilleratoCodigoBachillerato(codigoBachillerato);
        return materiaBachilleratoList.stream().map(MateriaBachillerato::getMateria).collect(Collectors.toList());
    }
}
