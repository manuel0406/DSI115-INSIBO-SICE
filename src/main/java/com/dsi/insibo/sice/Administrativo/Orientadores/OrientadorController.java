package com.dsi.insibo.sice.Administrativo.Orientadores;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteService;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.Orientador;

/**
 * OrientadorController
 */
@Controller
@RequestMapping("/AsignacionOrientador")
@PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR','SUBDIRECTORA')")
public class OrientadorController {

    @Autowired
    BachilleratoService bachilleratoService;
    @Autowired
    DocenteService docenteService;
    @Autowired
    OrientadorService orientadorService;
    @Autowired
    AlumnoService alumnoService;

    @GetMapping("/Asignar")
    public String asignacion(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        Orientador orientador = new Orientador();
        List<Bachillerato> listaBachilleratos = bachilleratoService.listaBachilleratos();
        List<Orientador> listaOrientadores = orientadorService.listaOrientador();

        // Crear una estructura de paginado manualmente
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), listaOrientadores.size());
        Page<Orientador> pageOrientadores = new PageImpl<>(listaOrientadores.subList(start, end), pageRequest,
                listaOrientadores.size());

        model.addAttribute("titulo", "Asignar");
        model.addAttribute("orientador", orientador);
        model.addAttribute("bachilleratos", listaBachilleratos);
        model.addAttribute("Docentes", docenteService.listarDocenteAsignacion());
        model.addAttribute("listaOrientadores", pageOrientadores.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", pageOrientadores.getTotalPages());
        model.addAttribute("totalElements", listaOrientadores.size());
        int baseIndex = (page - 1) * size;// sirve para mantener la base de la numeración de lo alumnos cuando cambia de
                                          // pagina
        model.addAttribute("baseIndex", baseIndex);

        return "Administrativo/GestionOrientador/asignacionOrientador";
    }

    @PostMapping("/guardarOrientacion")
    public String guardar(@ModelAttribute Orientador orientador, RedirectAttributes attributes) {

        Orientador existe = null;

        existe = orientadorService.existe(orientador.getBachillerato().getCodigoBachillerato());
        if (existe != null) {
            attributes.addFlashAttribute("error", "¡Está sección ya fue asignada!");
            return "redirect:/AsignacionOrientador/Asignar";
        }
        orientadorService.guardarOrientador(orientador);
        attributes.addFlashAttribute("success", "¡Registro guardado con exito!");

        return "redirect:/AsignacionOrientador/Asignar";
    }

    @PostMapping("/actualizarOrientacion")
    public String actualizarorientador(@ModelAttribute Orientador orientador, RedirectAttributes attributes) {

        orientadorService.guardarOrientador(orientador);
        attributes.addFlashAttribute("success", "¡Registro guardado con exito!");

        return "redirect:/AsignacionOrientador/Asignar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarOrientador(@PathVariable("id") int id, RedirectAttributes attributes) {

        orientadorService.eliminarOrientador(id);
        attributes.addFlashAttribute("warning", "¡Registro eliminado con éxito!");
        return "redirect:/AsignacionOrientador/Asignar";
    }

    @GetMapping("/seccionAsigada/{id}")
    public String verAlumno(Model model, @PathVariable("id") int id,
            @RequestParam(value = "carrera", required = false) String carrera,
            @RequestParam(value = "grado", required = false) String grado,
            @RequestParam(value = "seccion", required = false) String seccion,
            @RequestParam(value = "genero", required = false) String genero,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {

        Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(id);
        // Convertir cadenas vacías a null para evitar problemas con las consultas

        if (genero != null && genero.isEmpty()) {
            genero = null;
        }

        // Obtener la lista completa de alumnos filtrada por los parámetros
        List<Alumno> listaAlumnos = alumnoService.listarAlumnos(bachillerato.getNombreCarrera(),
                String.valueOf(bachillerato.getGrado()), bachillerato.getSeccion(), genero);
        // Ordenar la lista por "apellidoAlumno"
        listaAlumnos.sort(Comparator.comparing(Alumno::getApellidoAlumno));

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
        model.addAttribute("url", "/AsignacionOrientador/seccionAsigada/" + id);
        model.addAttribute("id", id);
        model.addAttribute("totalElements", listaAlumnos.size());
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

    @GetMapping(value = "/seccionAsigada/{id}", produces = "application/pdf")
    public ModelAndView verAlumnosPdf(Model model, @PathVariable("id") int id,
            @RequestParam(value = "carrera", required = false) String carrera,
            @RequestParam(value = "grado", required = false) String grado,
            @RequestParam(value = "seccion", required = false) String seccion,
            @RequestParam(value = "genero", required = false) String genero) {
        Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(id);
        // Convertir cadenas vacías a null para evitar problemas con las consultas

        if (genero != null && genero.isEmpty()) {
            genero = null;
        }

        // Obtener la lista completa de alumnos filtrada por los parámetros
        List<Alumno> listaAlumnos = alumnoService.listarAlumnos(bachillerato.getNombreCarrera(),
                String.valueOf(bachillerato.getGrado()), bachillerato.getSeccion(), genero);
        // Obtener la lista de carreras (bachilleratos)
        List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(false);

        carrera = bachillerato.getNombreCarrera();
        grado = String.valueOf(bachillerato.getGrado());
        seccion = bachillerato.getSeccion();

        // Crear un objeto ModelAndView con la vista "Expediente_alumno/verAlumnoPdf"
        ModelAndView modelAndView = new ModelAndView("Expediente_alumno/verAlumnoPdf");

        // Agregar atributos al ModelAndView para ser utilizados en la vista
        model.addAttribute("titulo", "Alumnos");
        modelAndView.addObject("alumnos", listaAlumnos);
        modelAndView.addObject("bachilleratos", listaCarreras);
        modelAndView.addObject("carrera", carrera);
        modelAndView.addObject("grado", grado);
        modelAndView.addObject("seccion", seccion);

        // Retornar el objeto ModelAndView que contiene la vista y los datos
        return modelAndView;
    }

    @GetMapping("/editar/{id}/{nie}")
    public String editar(@PathVariable("nie") int nie, @PathVariable("id") int id, Model model,
            RedirectAttributes attributes) {

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

        String carrera, grado, seccion;
        carrera = alumno.getBachillerato().getNombreCarrera();
        grado = String.valueOf(alumno.getBachillerato().getGrado());
        seccion = alumno.getBachillerato().getSeccion();

        // Si el alumno existe, obtiene la lista de bachilleratos para el formulario
        List<Bachillerato> listaCarreras = bachilleratoService.listaCarrera(false);
        model.addAttribute("titulo", "Editar");
        model.addAttribute("alumno", alumno);
        model.addAttribute("bachilleratos", listaCarreras);
        model.addAttribute("editar", true); // Indica que se está en modo edición+
        model.addAttribute("editarO", true); // Indica que se está en modo edición
        model.addAttribute("carrera", carrera);
        model.addAttribute("grado", grado);
        model.addAttribute("seccion", seccion);
        model.addAttribute("id", id);
        model.addAttribute("url", "/AsignacionOrientador/actualizar/" + id);

        // Retorna el nombre de la vista de edición del alumno
        return "Expediente_alumno/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarAlumno(@ModelAttribute Alumno alumno, RedirectAttributes attributes,
            @PathVariable("id") int id) {

        Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(id);
        alumno.setBachillerato(bachillerato);
        // Guarda el alumno con la información actualizada
        alumnoService.guardarAlumno(alumno);

        // Añade un mensaje flash indicando que la actualización fue exitosa
        attributes.addFlashAttribute("success", "¡Alumno actualizado con éxito!");

        // Redirige a la vista de listado de alumnos
        return "redirect:/AsignacionOrientador/seccionAsigada/" + id;
    }

}