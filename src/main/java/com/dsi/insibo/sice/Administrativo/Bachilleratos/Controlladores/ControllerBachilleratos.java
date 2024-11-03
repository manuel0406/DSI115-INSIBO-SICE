package com.dsi.insibo.sice.Administrativo.Bachilleratos.Controlladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.AnioService;
import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Calificaciones.NotaMateriaService;
import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.AnioAcademico;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.NotaMateria;

@Controller
@RequestMapping("/Bachillerato")
@PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR','SUBDIRECTORA')")
public class ControllerBachilleratos {

    @Autowired
    AnioService anioService;

    @Autowired
    BachilleratoService bachilleratoService;
    @Autowired
    private NotaMateriaService notaMateriaService;
    @Autowired
    private AlumnoService alumnoService;

    @GetMapping("/anio")
    public String prueba(Model model) {

        AnioAcademico anio = new AnioAcademico();

        // Obtiene una lista de todos los años académicos a través del servicio
        // anioService.
        List<AnioAcademico> listaAnio = anioService.listaAnio();

        // Inicializa una lista para almacenar bachilleratos nulos asociados a cada año
        // académico.
        List<Bachillerato> listaNullos = new ArrayList<>();

        // Inicializa un mapa para asociar el ID de cada año académico con un booleano
        // que indica si tiene o no bachilleratos.
        Map<Integer, Boolean> anioBachilleratoMap = new HashMap<>();

        // Itera sobre cada año académico en la lista de años académicos.
        for (AnioAcademico anioAcademico : listaAnio) {
            // Agrega a listaNullos los bachilleratos nulos asociados al año académico
            // actual.
            listaNullos.addAll(anioService.listaNullos(Collections.singletonList(anioAcademico)));

            // Verifica si el año académico actual tiene bachilleratos asociados y almacena
            // el resultado en el mapa.
            boolean tieneBachilleratos = anioService.tieneBachilleratos(anioAcademico.getIdAnioAcademico());
            anioBachilleratoMap.put(anioAcademico.getIdAnioAcademico(), tieneBachilleratos);
        }

        // Agrega varios atributos al modelo para que estén disponibles en la vista.
        model.addAttribute("titulo", "Año"); // Título que se mostrará en la vista.
        model.addAttribute("lista", listaAnio); // Lista de años académicos.
        model.addAttribute("anioBachilleratoMap", anioBachilleratoMap); // Mapa de ID de años académicos a booleanos
                                                                        // indicando si tienen bachilleratos.
        model.addAttribute("anio", anio); // Nueva instancia de AnioAcademico.

        // Retorna el nombre de la vista que se debe renderizar, en este caso, anio.html
        // ubicada en Administrativo/GestionBachilleratos/.
        return "Administrativo/GestionBachilleratos/anio";
    }

    @PostMapping("/guardarAnio")
    public String guardarAnio(@ModelAttribute AnioAcademico anio, RedirectAttributes attributes) {

        AnioAcademico anioExistente = anioService.buscandoAnio(anio.getAnio());
        if (anioExistente != null) {
            attributes.addFlashAttribute("error", "Error: El año ya existe.");
            return "redirect:/Bachillerato/anio";
        } else {
            anioService.guardarAnio(anio);
            attributes.addFlashAttribute("success", "¡Registro guardado con exito!");
        }

        return "redirect:/Bachillerato/anio";
    }

    @PostMapping("/editarAnio")
    public String editarAnio(@ModelAttribute AnioAcademico anio, RedirectAttributes attributes) {

        AnioAcademico activoMatricula = anioService.activoMatricula();
        AnioAcademico activoAnio = anioService.activoAnio();
        System.out.println("cerrado: "+ anio.isCerrado());

        if (activoMatricula != null && (anio.isActivoMatricula() == true) && anio.getIdAnioAcademico() !=activoMatricula.getIdAnioAcademico() ) {
            attributes.addFlashAttribute("error", "¡Solo un año puede tener matricula activa!");
        } else if (activoAnio != null && (anio.isActivoAnio() == true) && anio.getIdAnioAcademico() !=activoAnio.getIdAnioAcademico()) {
            attributes.addFlashAttribute("error", "¡Solo un año puede estar activo activo!");
        } else {
            anioService.guardarAnio(anio);
            attributes.addFlashAttribute("success", "¡Registro editado con exito!");
            return "redirect:/Bachillerato/anio";
        }

        return "redirect:/Bachillerato/anio";
    }

    @GetMapping("/Oferta/{idAnio}")
    public String oferta(Model model, @PathVariable("idAnio") int idAnio, RedirectAttributes attributes) {

        AnioAcademico anioAcademico = new AnioAcademico();

        if (idAnio > 0) {
            anioAcademico = anioService.buscarPoridAnioAcademico(idAnio);
            if (anioAcademico == null) {
                attributes.addFlashAttribute("error", "Error: Ese año no existe.");
                return "redirect:/Bachillerato/anio";
            }
        } else {
            attributes.addFlashAttribute("error", "Error: El año ingresado no es valido.");
            return "redirect:/Bachillerato/anio";
        }

        // AnioAcademico anioAcademico = anioService.buscarPoridAnioAcademico(idAnio);
        model.addAttribute("titulo", "Crear Oferta");
        model.addAttribute("anioAcademico", anioAcademico);
        model.addAttribute("bachillerato", new Bachillerato());
        model.addAttribute("carreras", List.of("Atención Primaria en Salud", "Administrativo Contable",
                "Desarrollo de Software", "Electrónica", "Sistemas Eléctricos")); // Lista de carreras
        model.addAttribute("secciones", List.of("A", "B", "C", "D")); // Lista de secciones disponibles
        return "Administrativo/GestionBachilleratos/crearOferta";
    }

    @PostMapping("/Oferta/guardar/{idAnio}")
    public String crearOferta(@PathVariable("idAnio") int idAnio,
            @RequestParam("secciones") List<String> secciones,
            Model model) {

        AnioAcademico anioAcademico = anioService.buscarPoridAnioAcademico(idAnio);

        for (String seccion : secciones) {
            String[] parts = seccion.split("\\|"); // Separar por el símbolo "|"
            String carrera = parts[0];
            int grado = Integer.parseInt(parts[1]);
            String seccionName = parts[2];

            Bachillerato bachillerato = new Bachillerato();
            bachillerato.setNombreCarrera(carrera);
            bachillerato.setGrado(grado);
            bachillerato.setSeccion(seccionName);
            bachillerato.setAnioAcademico(anioAcademico);

            bachilleratoService.guardarBachillerato(bachillerato);
        }

        return "redirect:/Bachillerato/anio";
    }

    @GetMapping("/VerOferta/{idAnio}")
    public String verOferta(Model model, @PathVariable("idAnio") int idAnio, RedirectAttributes attributes) {

        AnioAcademico anioAcademico = new AnioAcademico();
        if (idAnio > 0) {
            anioAcademico = anioService.buscarPoridAnioAcademico(idAnio);
            if (anioAcademico == null) {
                attributes.addFlashAttribute("error", "Error: La oferta de ese año no existe");
                return "redirect:/Bachillerato/anio";
            }
        } else {
            attributes.addFlashAttribute("error", "Error: El año ingresado no es valido.");
            return "redirect:/Bachillerato/anio";
        }
        Bachillerato bachillerato = new Bachillerato();
        List<Bachillerato> listadoOfertas = bachilleratoService.listadOfertaPorAnio(idAnio);
        List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(false);
        String grado = "";

        model.addAttribute("titulo", "Oferta");
        model.addAttribute("bachilleratos", listadoOfertas);
        model.addAttribute("bachillerato", bachillerato);
        model.addAttribute("listaCarrera", listaCarreras);
        model.addAttribute("nivel", grado);
        model.addAttribute("anioAcademico", anioAcademico);

        return "Administrativo/GestionBachilleratos/ofertaBachillerato";
    }

    @PostMapping("/guardarBachillerato/{idAnio}")
    public String guardarBachillerato(@ModelAttribute Bachillerato bachillerato, @RequestParam("nivel") String nivel,
            @PathVariable("idAnio") int idAnio, Model model, RedirectAttributes attributes) {
        // Convertir el valor de nivel a entero y asignarlo a grado
        AnioAcademico anioAcademico = anioService.buscarPoridAnioAcademico(idAnio);
        int grado = Integer.parseInt(nivel);
        bachillerato.setGrado(grado);
        bachillerato.setAnioAcademico(anioAcademico);

        Bachillerato existe = bachilleratoService.existeBachillerato(bachillerato.getNombreCarrera(),
                bachillerato.getGrado(), bachillerato.getSeccion(), idAnio);
        if (existe != null) {
            System.out.println("si existe");
            attributes.addFlashAttribute("error", "Error: Este bachillerato ya está registrado.");
            return "redirect:/Bachillerato/VerOferta/" + idAnio;
        } else {

            bachilleratoService.guardarBachillerato(bachillerato);

        }

        // Redirigir o devolver a la vista adecuada
        return "redirect:/Bachillerato/VerOferta/" + idAnio;
    }

    @GetMapping("/deleteBachillerato/{codigoBachillerato}")
    public String eliminarBachillerato(@PathVariable("codigoBachillerato") int codigoBachillerato,
            RedirectAttributes attributes) {

        Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(codigoBachillerato);

        try {
            bachilleratoService.deleteBachillerato(codigoBachillerato);
            attributes.addFlashAttribute("warning", "¡Registro eliminado con exito!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error",
                    "¡Registro no se puede eliminar, porque contiene datos relacionados!");
        }
        return "redirect:/Bachillerato/VerOferta/" + bachillerato.getAnioAcademico().getIdAnioAcademico();

    }

    @GetMapping("/cierreAcademico/{idAnioAcademico}")
    public String cierreAcademnico(Model model, @PathVariable("idAnioAcademico") int idAnioAcademico) {

        List<Bachillerato> listadoBachilleratos = bachilleratoService.listadOfertaPorAnio(idAnioAcademico);
        boolean aprobado = true;

        for (Bachillerato bachillerato : listadoBachilleratos) {
            List<Alumno> listadoAlumnos = alumnoService.alumnosPorBachilerato(bachillerato.getCodigoBachillerato());

            for (Alumno alumno : listadoAlumnos) {
                // Filtrar listaNotas para obtener solo notas del alumno actual
                List<NotaMateria> listaNotas = notaMateriaService.listadoNotaMateriaAlumno(alumno.getIdAlumno())
                        .stream()
                        .filter(nota -> nota.getAlumno().getIdAlumno() == alumno.getIdAlumno())
                        .toList();

                if (listaNotas.isEmpty()) {
                    System.out.println("El alumno " + alumno.getNie() + " no tiene notas registradas.");
                    continue; // Salta al siguiente alumno si no tiene notas
                }
                for (NotaMateria notaMateria : listaNotas) {
                    if (notaMateria.getNotaMateria() < 6) {
                        aprobado = false;
                        System.out.println("El alumno " + alumno.getNie() + " está reprobado en una materia.");
                        break; // Salir del bucle de notas al encontrar una nota reprobada
                    }
                }
                if (aprobado) {
                    System.out
                            .println("Aprobado: " + aprobado + ". Se puede guardar el alumno " + alumno.getNie());

                    alumno.setEstadoAlumno(true);
                    alumnoService.guardarAlumno(alumno);
                }
            }
        }

        AnioAcademico anioAcademico = anioService.buscarPoridAnioAcademico(idAnioAcademico);
        anioAcademico.setActivoAnio(false);
        anioAcademico.setCerrado(true);
        anioAcademico.setActivoMatricula(false);
        anioService.guardarAnio(anioAcademico);
        System.out.println("Año cerrado");
        return "redirect:/Bachillerato/anio";
    }

}
