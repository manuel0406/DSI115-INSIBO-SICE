package com.dsi.insibo.sice.Expediente_docente.Docentes.Anexos;

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

import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteService;
import com.dsi.insibo.sice.entity.AnexoDocente;
import com.dsi.insibo.sice.entity.Docente;

@Controller
@PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'DOCENTE', 'SUBDIRECTORA', 'DIRECTOR')")
public class AnexoDocenteController {

    @Autowired
    private AnexoDocenteService anexoService;
    @Autowired
    private AnexoDocenteRepository anexoRepository;
    @Autowired
    private DocenteService docenteService;

    // Controlador para subir archivos
    @GetMapping("/docente/Subir/{clave}/{duiDocente}")
    public String subirAdministrativo(@PathVariable("clave") String clave,
            @PathVariable("duiDocente") String duiDocente, Model model) {
        if (clave.equals("dui")) {
            model.addAttribute("tituloB", "Seleccione su DUI");
        } else if (clave.equals("curriculum")) {
            model.addAttribute("tituloB", "Seleccione su Curriculum VITAE");
        } else if (clave.equals("nup")) {
            model.addAttribute("tituloB", "Seleccione su NUP");
        }

        // Agrega los atributos "clave" y "duiDocente" al modelo
        model.addAttribute("clave", clave);
        model.addAttribute("duiDocente", duiDocente);
        model.addAttribute("titulo", "Documentos");
        model.addAttribute("enlace", "/expedientedocente/Documentos/" + duiDocente); // cambiar por el del controller
        return "Expediente_docente/Docentes/upload";
        // return "upload";

    }

    // Controlador para mostrar el estado de la carga de archivos.
    @GetMapping("/docente/uploadStatus")
    public String uploadStatus(Model model) {
        return "Expediente_docente/Docentes/uploadStatushtml";
    }

    // Controlador para cargar archivos
    @PostMapping("/docente/upload/{clave}/{duiDocente}")
    public String cargandoArchivo(@PathVariable("clave") String clave, @PathVariable("duiDocente") String duiDocente,
            @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        // Busca al administrativo por su duiDocente y obtiene sus anexos
        Docente docente = docenteService.buscarPorIdDocente(duiDocente);
        AnexoDocente archivo = anexoService.buscarDocente(duiDocente);

        // Si el anexo es nulo, crea uno nuevo
        if (archivo == null) {
            archivo = new AnexoDocente();
        }

        if (!file.getContentType().equals("application/pdf")) {
            redirectAttributes.addFlashAttribute("message", "Solo se permiten archivos PDF.");
            redirectAttributes.addFlashAttribute("warning", "Error, el archivo a subir debe ser PDF.");
            return "redirect:/expedientedocente/Documentos/" + docente.getDuiDocente();
        }

        // Cuando la clave es DUI
        if (clave.equals("dui")) {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("success", "Por favor, seleccione un archivo para subir.");
                return "redirect:/expedientedocente/Documentos/" + docente.getDuiDocente();
            }
            try {
                byte[] bytes = file.getBytes(); // Lee los bytes del archivo

                // Guarda la referencia en la base de datos
                archivo.setNombreDuiDocente(file.getOriginalFilename());
                archivo.setDatosDuiDocente(bytes);
                archivo.setFechaDuiDocente(new Date());
                archivo.setDocente(docente);
                anexoService.guardaAnexo(archivo);

                redirectAttributes.addFlashAttribute("success",
                        "Has subido correctamente su DUI con nombre: '" + file.getOriginalFilename() + "'");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/expedientedocente/Documentos/" + duiDocente; // cambiar por el del controller
        }

        // Cuando la clave es Curriculum
        else if (clave.equals("curriculum")) {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("success", "Por favor, seleccione un archivo para subir.");
                return "redirect:/expedientedocente/Documentos/" + docente.getDuiDocente();
            }
            try {
                byte[] bytes = file.getBytes();

                // Guarda la referencia en la base de datos
                archivo.setNombreCurriculumDocente(file.getOriginalFilename());
                archivo.setDatosCurriculumDocente(bytes);
                archivo.setFechaCurriculumDocente(new Date());
                archivo.setDocente(docente);
                anexoService.guardaAnexo(archivo);

                redirectAttributes.addFlashAttribute("success",
                        "Has subido correctamente su curriculum con nombre: '" + file.getOriginalFilename() + "'");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/expedientedocente/Documentos/" + duiDocente; // cambiar por el del controller
        }

        // Cuando la clave es NUP
        else if (clave.equals("nup")) {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("success", "Por favor, seleccione un archivo para subir.");
                return "redirect:/expedientedocente/Documentos/" + docente.getDuiDocente();
            }
            try {
                byte[] bytes = file.getBytes();

                // Guarda la referencia en la base de datos
                archivo.setNombreNupDocente(file.getOriginalFilename());
                archivo.setDatosNupDocente(bytes);
                archivo.setFechaNupDocente(new Date());
                archivo.setDocente(docente);
                anexoService.guardaAnexo(archivo);

                redirectAttributes.addFlashAttribute("success",
                        "Has subido correctamente su NUP con nombre: '" + file.getOriginalFilename() + "'");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/expedientedocente/Documentos/" + duiDocente; // cambiar por el del controller
        }
        return "redirect:/expedientedocente/Documentos/" + docente.getDuiDocente();
    }

    // Controlador para eliminar archivos
    @GetMapping("/docente/delete/{clave}/{id}/{duiDocente}")
    public String deleteAnexo(@PathVariable("clave") String clave, @PathVariable("id") int id, Model model,
            @PathVariable("duiDocente") String duiDocente, RedirectAttributes redirectAttributes) {

        // Obtiene los anexos del administrativo
        AnexoDocente archivo = anexoService.buscarAnexoPorId(id);

        // Cuando la clave es DUI
        if (clave.equals("dui")) {
            // Elimina los datos relacionados con el DUI
            archivo.setNombreDuiDocente(null);
            archivo.setDatosDuiDocente(null);
            archivo.setFechaDuiDocente(null);
            anexoService.guardaAnexo(archivo);
            redirectAttributes.addFlashAttribute("warning", "Se ha eliminado correctamente el archivo ");

            return "redirect:/expedientedocente/Documentos/" + duiDocente; // cambiar por el del controller
        }

        // Cuando la clave es curriculum
        else if (clave.equals("curriculum")) {
            // Elimina los datos relacionados con el curriculum
            archivo.setNombreCurriculumDocente(null);
            archivo.setDatosCurriculumDocente(null);
            archivo.setFechaCurriculumDocente(null);
            anexoService.guardaAnexo(archivo);
            redirectAttributes.addFlashAttribute("warning", "Se ha eliminado correctamente el archivo ");

            return "redirect:/expedientedocente/Documentos/" + duiDocente;
        }

        // Cuando la clave es NUP
        else if (clave.equals("nup")) {
            // Elimina los datos relacionados con el curriculum
            archivo.setNombreNupDocente(null);
            archivo.setDatosNupDocente(null);
            archivo.setFechaNupDocente(null);
            anexoService.guardaAnexo(archivo);
            redirectAttributes.addFlashAttribute("warning", "Se ha eliminado correctamente el archivo ");

            return "redirect:/expedientedocente/Documentos/" + duiDocente;
        }

        return "Expediente_docente/upload";
    }

    // Controlador para mostrar archivos
    @GetMapping("/docente/expedientedocente/files/{clave}/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> mostrarAnexo(@PathVariable int id, @PathVariable("clave") String clave) {
        // Busca el archivo en el repositorio por su ID
        AnexoDocente fileEntity = anexoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Cuando la clave es DUI
        if (clave.equals("dui")) {
            // Crea un recurso de bytes a partir de los datos del archivo
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getDatosDuiDocente());
            // Devuelve la respuesta con el contenido del archivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + fileEntity.getNombreDuiDocente() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }

        // Cuando la clave es curriculum
        else if (clave.equals("curriculum")) {
            // Crea un recurso de bytes a partir de los datos del archivo
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getDatosCurriculumDocente());
            // Devuelve la respuesta con el contenido del archivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + fileEntity.getNombreCurriculumDocente() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }

        // Cuando la clave es nup
        else if (clave.equals("nup")) {
            // Crea un recurso de bytes a partir de los datos del archivo
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getDatosNupDocente());
            // Devuelve la respuesta con el contenido del archivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + fileEntity.getDatosNupDocente() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }
        return null;
    }
}
