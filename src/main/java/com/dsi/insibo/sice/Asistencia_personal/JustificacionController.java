package com.dsi.insibo.sice.Asistencia_personal;

import java.io.IOException;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dsi.insibo.sice.Asistencia_personal.Asistencia_admnistrativa.AsistenciaAdmService;
import com.dsi.insibo.sice.Asistencia_personal.Asistencia_docente.AsistenciaDocenteService;
import com.dsi.insibo.sice.Asistencia_personal.Justificacion.Justificacion_Docente.JustificacionDocenteService;
import com.dsi.insibo.sice.Asistencia_personal.Justificacion.Justificacion_Personal.JustificacionPersonalService;
import com.dsi.insibo.sice.entity.AsistenciaDocente;
import com.dsi.insibo.sice.entity.AsistenciaPersonal;
import com.dsi.insibo.sice.entity.JustificacionDocente;
import com.dsi.insibo.sice.entity.JustificacionPersonal;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/justificacionD")
public class JustificacionController {
    @Autowired
    private JustificacionDocenteService justificacionDocenteService;

    @Autowired
    private AsistenciaDocenteService asistenciaDocenteService;

    @Autowired
    private JustificacionPersonalService justificacionPersonalService;

    @Autowired
    private AsistenciaAdmService asistenciaAdmService;

    @GetMapping("/ingresar")
    public String selccionInicio(@RequestParam("id") int idAsistencia,
            @RequestParam("nombre") String nombreDocente,
            Model model) {
        // Agregar los datos al modelo
        model.addAttribute("idAsistencia", idAsistencia);
        model.addAttribute("nombreDocente", nombreDocente);
        return "Asistencia_personal/Docente_asistencia/agregarJustificacion";
    }

    @PostMapping("/subirJustificacion")
    public String subirJustificacion(@RequestParam("archivo") MultipartFile archivo,
            @RequestParam("descripcionJustificacion") String descripcion,
            @RequestParam("idAsistencia") int idAsistencia,
            Model model) {

        try {
            JustificacionDocente justificacionDocente = new JustificacionDocente();
            justificacionDocente.setDescripcionJustificacion(descripcion);
            justificacionDocente.setArchivo(archivo.getBytes());


            AsistenciaDocente asistenciaDocente = asistenciaDocenteService.findById(idAsistencia);
            justificacionDocente.setAsistenciaDocente(asistenciaDocente);
            // Llamar al método save del servicio
            justificacionDocenteService.save(justificacionDocente);

            return "redirect:/asistencias/asistenciaTardia"; // Redirige a la ruta deseada
        } catch (IOException e) {
            model.addAttribute("error", "Error al guardar el archivo");
            return "Asistencia_personal/Docente_asistencia/agregarJustificacion"; // error
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "Asistencia_personal/Docente_asistencia/agregarJustificacion"; // error
        }
    }

    @GetMapping("/consultaJustificacion")
    public String consultarIngreso(@RequestParam("id") int idJustificacion,
            @RequestParam("nombre") String nombreDocente, Model model) {
        System.out.println("ID recibido: " + idJustificacion); // Verifica el ID recibido
        try {
            model.addAttribute("nombreDocente", nombreDocente);
            JustificacionDocente justificacionDocente = justificacionDocenteService
                    .findByAsistenciaDocente_IdAsistencia(idJustificacion);
            model.addAttribute("justificacion", justificacionDocente);
        } catch (RuntimeException e) {
            model.addAttribute("error", "Justificación no encontrada con ID: " + idJustificacion);
            return "Asistencia_personal/Docente_asistencia/asistenciaGeneral";
        }
        return "Asistencia_personal/Docente_asistencia/mostrarJustificacion";
    }

    @GetMapping("/descargarFile")
    public ResponseEntity<byte[]> descargarFile(@RequestParam("id") int idJustificacion) {
        JustificacionDocente justificacion = justificacionDocenteService.findById(idJustificacion);

        if (justificacion == null || justificacion.getArchivo() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] archivo = justificacion.getArchivo();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Specify the correct MIME type if necessary
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("justificacion_" + idJustificacion + ".pdf")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(archivo);
    }

    // personal
    @GetMapping("/ingresaradm")
    public String selccionInicioadm(@RequestParam("id") int idAsistencia,
            @RequestParam("nombre") String nombrePersonal,
            Model model) {
        // Agregar los datos al modelo
        model.addAttribute("idAsistencia", idAsistencia);
        model.addAttribute("nombrePersonal", nombrePersonal);
        return "Asistencia_personal/Personal_asistencia/agregarJustificacionAdm";
    }

    @PostMapping("/subirJustificacionadm")
    public String subirJustificacionAdm(@RequestParam("archivo") MultipartFile archivo,
            @RequestParam("descripcionJustificacion") String descripcion,
            @RequestParam("idAsistencia") int idAsistencia,
            Model model) {

        try {
            JustificacionPersonal justificacionPersonal = new JustificacionPersonal();
            justificacionPersonal.setDescripcionJustificacionPersonal(descripcion);
            justificacionPersonal.setArchivoPersonal(archivo.getBytes());

            AsistenciaPersonal asistenciaPersonal = asistenciaAdmService.findById(idAsistencia);
            justificacionPersonal.setAsistenciaPersonal(asistenciaPersonal);
            // Llamar al método save del servicio
            justificacionPersonalService.save(justificacionPersonal);
            
            return "redirect:/asistencias/asistenciaTardiaadm"; 
        } catch (IOException e) {
            model.addAttribute("error", "Error al guardar el archivo");
            return "Asistencia_personal/Personal_asistencia/agregarJustificacionAdm"; //error
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "Asistencia_personal/Personal_asistencia/agregarJustificacionAdm"; //error
        }
    }

    @GetMapping("/consultaJustificacionadm")
    public String consultarIngresoadm(@RequestParam("id") int idJustificacion,
            @RequestParam("nombre") String nombrePersonal, Model model) {
        System.out.println("ID recibido: " + idJustificacion); // Verifica el ID recibido
        try {
            model.addAttribute("nombrePersonal", nombrePersonal);
            JustificacionPersonal justificacionPersonal = justificacionPersonalService.findByAsistenciaPersonal_IdAsistencia(idJustificacion);
            model.addAttribute("justifiacion", justificacionPersonal);
        } catch (RuntimeException e) {
            model.addAttribute("error", "Justificación no encontrada con ID: " + idJustificacion);
            return "Asistencia_personal/Personal_asistencia/asistenciaTardiaAdm";
        }
        return "Asistencia_personal/Personal_asistencia/mostrarJustificacionAdm";
    }

    @GetMapping("/descargarFileadm")
    public ResponseEntity<byte[]> descargarFileadm(@RequestParam("id") int idJustificacion) {
        JustificacionPersonal justificacionPersonal = justificacionPersonalService.findById(idJustificacion);

        if (justificacionPersonal == null || justificacionPersonal.getArchivoPersonal() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] archivo = justificacionPersonal.getArchivoPersonal();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Specify the correct MIME type if necessary
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("justificacion_" + idJustificacion + ".pdf")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(archivo);
    }
}
