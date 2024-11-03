package com.dsi.insibo.sice.Expediente_alumno;

import java.io.IOException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.AnexoAlumno;

@Controller
@PreAuthorize("hasAnyRole('DOCENTE','ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
public class AnexoAlumnoController {

    @Autowired
    private AnexoAlumnoService anexoService;
    @Autowired
    private AnexoAlumnoRepository anexoRepository;
    @Autowired
    private AlumnoService alumnoService;

    /**
     * Controlador para subir archivos relacionados con un alumno.
     *
     * Este método maneja las solicitudes GET a la ruta "/Subir/{clave}/{nie}" y
     * muestra la vista de carga de archivos.
     *
     * @param clave La clave que indica el tipo de documento (dui,
     *              PartidaNacimiento).
     * @param nie   El número de identificación estudiantil del alumno.
     * @param model El modelo que se pasa a la vista.
     * @return Una cadena que representa la vista de carga de archivos.
     */
    @GetMapping("/Subir/{clave}/{nie}")
    public String subir(@PathVariable("clave") String clave, @PathVariable("nie") int nie, Model model) {
        if (clave.equals("dui")) {
            model.addAttribute("tituloB", "Seleccione su DUI");
        }else if (clave.equals("PartidaNacimiento")) {
            model.addAttribute("tituloB", "Seleccione su Partida de nacimiento");
        }
        // Agrega los atributos "clave" y "nie" al modelo
        model.addAttribute("clave", clave);
        model.addAttribute("nie", nie);
        model.addAttribute("titulo", "Documentos");
        model.addAttribute("enlace", "/ExpedienteAlumno/Documentos/"+nie);
        //System.out.println("NIE: " + nie); // Imprime el NIE en la consola


        // Retorna la vista "upload"
        return "Expediente_alumno/upload";
    }
    
    /**
     * Controlador para mostrar el estado de la carga de archivos.
     *
     * Este método maneja las solicitudes GET a la ruta "/uploadStatus" y muestra
     * la vista de estado de la carga de archivos.
     *
     * @param model El modelo que se pasa a la vista.
     * @return Una cadena que representa la vista de estado de la carga de archivos.
     */
    @GetMapping("/uploadStatus")
    public String uploadStatus(Model model) {
        // Retorna la vista "Expediente_alumno/uploadStatus"
        return "Expediente_alumno/uploadStatus";
    }

    /**
     * Controlador para cargar archivos relacionados con un alumno.
     *
     * Este método maneja las solicitudes POST a la ruta "/upload/{clave}/{nie}"
     * y guarda un archivo específico en el expediente del alumno.
     *
     * @param clave              La clave que indica el tipo de documento (dui,
     *                           PartidaNacimiento).
     * @param nie                El número de identificación estudiantil del alumno.
     * @param file               El archivo que se va a subir.
     * @param redirectAttributes Atributos de redirección utilizados para pasar
     *                           mensajes flash.
     * @return Una cadena que redirige a la vista de documentos del alumno.
     */
    @PostMapping("/upload/{clave}/{nie}")
    public String cargandoArchivo(@PathVariable("clave") String clave, @PathVariable("nie") int nie,
            @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        // Busca al alumno por su NIE
        Alumno alumno = alumnoService.buscarPorIdAlumno(nie);
        // Obtiene el anexo del alumno
        AnexoAlumno archivo = anexoService.buscarAlumno(nie);

        // Si el anexo es nulo, crea uno nuevo
        if (archivo == null) {
            archivo = new AnexoAlumno();
        }

        if (!file.getContentType().equals("application/pdf")) {
            redirectAttributes.addFlashAttribute("message", "Solo se permiten archivos PDF.");
            redirectAttributes.addFlashAttribute("warning", "Error, el archivo a subir debe ser PDF.");
            return "redirect:/ExpedienteAlumno/Documentos/" + alumno.getIdAlumno();
        }

        // Verifica si la clave es "dui"
        if (clave.equals("dui")) {
            // Verifica si el archivo está vacío
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Por favor, seleccione un archivo para subir.");
                return "redirect:/ExpedienteAlumno/ver";
            }
            try {
                // Lee los bytes del archivo
                byte[] bytes = file.getBytes();

                // Guarda la referencia en la base de datos
                archivo.setNombreDui(file.getOriginalFilename());
                archivo.setDatosDui(bytes);
                archivo.setFechaDui(new Date());
                archivo.setAlumno(alumno);
                anexoService.guardaAnexo(archivo);

                redirectAttributes.addFlashAttribute("success",
                        "Has subido correctamente su DUI con nombre: '" + file.getOriginalFilename() + "'");

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Redirige a la página de documentos del alumno
            return "redirect:/ExpedienteAlumno/Documentos/" + nie;
        }
        // Verifica si la clave es "PartidaNacimiento"
        else if (clave.equals("PartidaNacimiento")) {
            // Verifica si el archivo está vacío
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("success", "Por favor, seleccione un archivo para subir.");
                return "redirect:/ExpedienteAlumno/ver";
            }
            try {
                // Lee los bytes del archivo
                byte[] bytes = file.getBytes();

                // Guarda la referencia en la base de datos
                archivo.setNombrePartidaNacimiento(file.getOriginalFilename());
                archivo.setDatosPartidaNacimiento(bytes);
                archivo.setFechaPartidaNacimiento(new Date());
                archivo.setAlumno(alumno);
                anexoService.guardaAnexo(archivo);

                redirectAttributes.addFlashAttribute("message",
                        "Has subido correctamente su partida de nacimiento con nombre: '" + file.getOriginalFilename() + "'");

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Redirige a la página de documentos del alumno
            return "redirect:/ExpedienteAlumno/Documentos/" + nie;
        }
        // Redirige a la vista de ver el expediente del alumno por defecto
        return "redirect:/ExpedienteAlumno/ver";
    }
    /**
     * Controlador para eliminar archivos relacionados con un alumno.
     *
     * Este método maneja las solicitudes GET a la ruta "/delete/{clave}/{id}/{nie}"
     * y elimina un archivo específico del expediente del alumno.
     *
     * @param clave              La clave que indica el tipo de documento (dui,
     *                           PartidaNacimiento).
     * @param id                 El ID del archivo a eliminar.
     * @param nie                El número de identificación estudiantil del alumno.
     * @param model              El modelo que se pasa a la vista.
     * @param redirectAttributes Atributos de redirección utilizados para pasar
     *                           mensajes flash.
     * @return Una cadena que redirige a la vista de documentos del alumno.
     */
    @GetMapping("/delete/{clave}/{id}/{nie}")
    public String deleteAnexo(@PathVariable("clave") String clave, @PathVariable("id") int id, Model model,
            @PathVariable("nie") int nie, RedirectAttributes redirectAttributes) {
        // Busca el anexo por su ID
        AnexoAlumno archivo = anexoService.buscarAnexoPorId(id);

        // Verifica si la clave es "dui"
        if (clave.equals("dui")) {
            // Elimina los datos relacionados con el DUI
            archivo.setNombreDui(null);
            archivo.setDatosDui(null);
            archivo.setFechaDui(null);
            anexoService.guardaAnexo(archivo);
            redirectAttributes.addFlashAttribute("warning", "Se ha eliminado correctamente el archivo ");

            // Redirige a la página de documentos del alumno
            return "redirect:/ExpedienteAlumno/Documentos/" + nie;
        }
        // Verifica si la clave es "PartidaNacimiento"
        else if (clave.equals("PartidaNacimiento")) {
            // Elimina los datos relacionados con la partida de nacimiento
            archivo.setNombrePartidaNacimiento(null);
            archivo.setDatosPartidaNacimiento(null);
            archivo.setFechaPartidaNacimiento(null);
            anexoService.guardaAnexo(archivo);

            redirectAttributes.addFlashAttribute("warning", "Se ha eliminado correctamente el archivo ");

            // Redirige a la página de documentos del alumno
            return "redirect:/ExpedienteAlumno/Documentos/" + nie;
        }
        // Retorna la vista "Expediente_alumno/upload" por defecto
        return "Expediente_alumno/upload";
    }

    /**
     * Controlador para mostrar archivos relacionados con un alumno.
     *
     * Este método maneja las solicitudes GET a la ruta
     * "/ExpedienteAlumno/files/{clave}/{id}"
     * y devuelve el archivo solicitado en formato PDF.
     *
     * @param id    El ID del archivo a mostrar.
     * @param clave La clave que indica el tipo de documento (dui,
     *              PartidaNacimiento).
     * @return Un ResponseEntity que contiene el archivo en formato PDF.
     */
    @GetMapping("/ExpedienteAlumno/files/{clave}/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> mostrarAnexo(@PathVariable int id, @PathVariable("clave") String clave) {
        // Busca el archivo en el repositorio por su ID
        AnexoAlumno fileEntity = anexoRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));

        // Verifica si la clave es "dui"
        if (clave.equals("dui")) {
            // Crea un recurso de bytes a partir de los datos del archivo
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getDatosDui());

            // Devuelve la respuesta con el contenido del archivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileEntity.getNombreDui() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }
        // Verifica si la clave es "PartidaNacimiento"
        else if (clave.equals("PartidaNacimiento")) {
            // Crea un recurso de bytes a partir de los datos del archivo
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getDatosPartidaNacimiento());

            // Devuelve la respuesta con el contenido del archivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + fileEntity.getNombrePartidaNacimiento() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }
        return null;
    }
}
