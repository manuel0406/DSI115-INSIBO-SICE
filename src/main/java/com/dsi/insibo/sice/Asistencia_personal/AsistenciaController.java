package com.dsi.insibo.sice.Asistencia_personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.util.*;
import com.dsi.insibo.sice.entity.AsistenciaDocente;
import com.dsi.insibo.sice.entity.Justificacion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/asistenciaPersonal")
public class AsistenciaController {


	private static String UPLOADED_FOLDER = "justificacionesDocumentos/";
	@Autowired
    private JustificacionRepository justificacionRepository;

    @Autowired
    private AsistenciaDocenteService asistenciaDocenteService;

	// Metodo para cargar la importacion del excel
	@GetMapping("/cargar")
	public String subiendoArchivo(){
		return "Asistencia_personal/cargarArchivo";
	}
	// Metodo para manejar la visualización las asistencias
    @GetMapping("/asistenciaGeneral")
	public String llegadaGeneral(Model model){
		List<Object[]> asistencia = asistenciaDocenteService.getAsistenciaDocente();
        model.addAttribute("asistencia", asistencia);
        model.addAttribute("titulo", "Asistencia");

		return "Asistencia_personal/asistenciaGeneral";
	}
	// Metodo para manejar la visualización de las llegadas tardias
    @GetMapping("/asistenciaTardia")
	public String llegadasTardia(Model model){
		List<Object[]> asistencia = asistenciaDocenteService.getAsistenciaDocenteByHorario();
        model.addAttribute("asistenciaHorario", asistencia);
        model.addAttribute("titulo", "Asistencia tardia");
		return "Asistencia_personal/asistenciaTardia";
	}
	
	@GetMapping("/justificacion")
	public String mostrarJustificacion(){
		return "Asistencia_personal/mostrarJustificacion";
	}
	// Metodo para manejar la visualización de la pagina agregarJustificacion
	@GetMapping("/justificacionAgregar")
	public String agregarJustificacion(){
		return "Asistencia_personal/agregarJustificacion";
	}
	// Metodo para manejar la subida de archivos y guardar descripcion
    @PostMapping("/uploadJustificacion")
    public String uploadJustificacion(
            @RequestParam("archivo") MultipartFile file,
            @RequestParam("String descripcionJustificacion") String descripcionJustificacion, Model model) {

				if (file.isEmpty()) {
					model.addAttribute("message", "Seleccione un archivo");
					return "Asistencia_personal/agregarJustificacion";
				}

				 try {
            // Crear directorio si no existe
            File uploadDir = new File(UPLOADED_FOLDER);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Generar un nombre unico para el archivo
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            Files.write(path, file.getBytes());

            // Guardar la ruta del archivo y la descripcion en la base de datos
            Justificacion justificacion = new Justificacion();
            justificacion.setDescripcionJustificacion(descripcionJustificacion);
            justificacion.setArchivo(path.toString());
            justificacionRepository.save(justificacion);

            model.addAttribute("message", "Archivo subido exitosamente: " + fileName);
            return "redirect:/asistenciaPersonal/justificacionAgregar";

        } catch (IOException e) {
            model.addAttribute("message", "Error al guardar archivo: " + e.getMessage());
            return "Asistencia_personal/agregarJustificacion";
        }
    }

}
