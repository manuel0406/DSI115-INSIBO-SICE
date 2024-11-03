package com.dsi.insibo.sice.Expediente_alumno;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Sancion;

@Controller
@RequestMapping("/ExpedienteAlumno")
@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA')") // SOLO PARA DOCENTES
public class SancionesController {
    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private SancionesService sancionesService;

    @GetMapping("/Sanciones/{idAlumno}")
    public String sancionesAlumno(@PathVariable("idAlumno") int idAlumno, Model model, RedirectAttributes attributes) {
        Alumno alumno = null;
        if (idAlumno > 0) {
            // Busca al alumno por su número de identificación estudiantil (NIE)
            alumno = alumnoService.buscarPorIdAlumno(idAlumno);

            // Verifica que el alumno exista
            if (alumno == null) {
                System.out.println("Error: ¡El Id ingresado no existe!");
                attributes.addFlashAttribute("error", "Error: ¡El id ingresado no existe!");
                return "redirect:/ExpedienteAlumno/ver";
            }

        } else {
            // Maneja el caso donde el NIE no es válido
            System.out.println("Error: ¡El NIE ingresado no es válido!");
            attributes.addFlashAttribute("error", "Error: ¡El NIE ingresado no es válido!");
            return "redirect:/ExpedienteAlumno/ver";
        }

        List<Sancion> listaSanciones= sancionesService.buscarSancionAlumno(idAlumno);
        Sancion sancion= new Sancion();

        // Agregar atributos al modelo para ser utilizados en la vista
        model.addAttribute("titulo", "Sanciones");
        model.addAttribute("alumno", alumno);
        model.addAttribute("sancion", sancion);
        model.addAttribute("listaSanciones", listaSanciones);
        model.addAttribute("urlInfo", "/ExpedienteAlumno/Alumno/");
		model.addAttribute("urlEnf", "/ExpedienteAlumno/Enfermedades/");
		model.addAttribute("urlResp", "/ExpedienteAlumno/Responsable/");
		model.addAttribute("urlDoc", "/ExpedienteAlumno/Documentos/");
		model.addAttribute("sanciones", true);
		model.addAttribute("btnRegresa", "/ExpedienteAlumno/ver");
        return "Expediente_alumno/AlumnoSanciones";
    }

    @PostMapping("/GuardarSancion/{idAlumno}")
    public String guardarSancion(@PathVariable("idAlumno") int idAlumno,@ModelAttribute Sancion sancion, RedirectAttributes attributes){

        //Buscar el alumno por su id
        Alumno alumno =alumnoService.buscarPorIdAlumno(idAlumno);
        sancion.setAlumno(alumno);
        sancion.setFechaSancion(new Date());
        sancionesService.guardarSancion(sancion);
        attributes.addFlashAttribute("success", "¡Sanción guardada o actualizada con éxito!");
        return "redirect:/ExpedienteAlumno/Sanciones/"+idAlumno;
    }

    @GetMapping("/eliminarSancion/{nie}/{id}")
    public String eliminarSancion(@PathVariable("id") int id,@PathVariable("id") int nie, RedirectAttributes attributes)
    {
        sancionesService.EliminarSancion(id);
        attributes.addFlashAttribute("warning", "¡Registro eliminado con éxito!");
        return "redirect:/ExpedienteAlumno/Sanciones/{nie}";
    }
}
