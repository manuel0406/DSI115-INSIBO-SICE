package com.dsi.insibo.sice.Expediente_docente.Administrativos.Anexos;

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

import com.dsi.insibo.sice.Expediente_docente.Administrativos.AdministrativoService;
import com.dsi.insibo.sice.entity.AnexoPersonalAdministrativo;
import com.dsi.insibo.sice.entity.PersonalAdministrativo;

@Controller
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
public class AnexoAdministrativoController {

    @Autowired
    private AnexoAdministrativoService anexoService;
    @Autowired
    private AnexoAdministrativoRepository anexoRepository;
    @Autowired
    private AdministrativoService administrativoService;

    // Controlador para subir archivos
    @GetMapping("/administrativo/Subir/{clave}/{duiPersonal}")
    public String subirAdministrativo(@PathVariable("clave") String clave,
            @PathVariable("duiPersonal") String duiPersonal, Model model) {
        if (clave.equals("dui")) {
            model.addAttribute("tituloB", "Seleccione su DUI");
        } else if (clave.equals("curriculum")) {
            model.addAttribute("tituloB", "Seleccione su Curriculum VITAE");
        } else if (clave.equals("nup")) {
            model.addAttribute("tituloB", "Seleccione su NUP");
        }

        // Agrega los atributos "clave" y "duiPersonal" al modelo
        model.addAttribute("clave", clave);
        model.addAttribute("duiPersonal", duiPersonal);
        model.addAttribute("titulo", "Documentos");
        model.addAttribute("enlace", "/expedienteadministrativo/Documentos/" + duiPersonal);
        return "Expediente_docente/Administrativos/upload";
        //return "upload";

    }

    // Controlador para mostrar el estado de la carga de archivos.
    @GetMapping("/administrativo/uploadStatus")
    public String uploadStatus(Model model) {
        return "Expediente_docente/Administrativos/uploadStatushtml";
    }

    // Controlador para cargar archivos
    @PostMapping("/administrativo/upload/{clave}/{duiPersonal}")
    public String cargandoArchivo(@PathVariable("clave") String clave, @PathVariable("duiPersonal") String duiPersonal,
            @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        // Busca al administrativo por su duiPersonal y obtiene sus anexos
        PersonalAdministrativo administrativo = administrativoService.buscarPorIdAdministrativo(duiPersonal);
        AnexoPersonalAdministrativo archivo = anexoService.buscarAdministrativo(duiPersonal);

        // Si el anexo es nulo, crea uno nuevo
        if (archivo == null) {
            archivo = new AnexoPersonalAdministrativo();
        }

        if (!file.getContentType().equals("application/pdf")) {
            redirectAttributes.addFlashAttribute("message", "Solo se permiten archivos PDF.");
            redirectAttributes.addFlashAttribute("warning", "Error, el archivo a subir debe ser PDF.");
            return "redirect:/expedienteadministrativo/Documentos/" + administrativo.getDuiPersonal();
        }

        // Cuando la clave es DUI
        if (clave.equals("dui")) {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("success", "Por favor, seleccione un archivo para subir.");
                return "redirect:/expedienteadministrativo/Documentos/" + administrativo.getDuiPersonal();
            }
            try {
                byte[] bytes = file.getBytes(); // Lee los bytes del archivo

                // Guarda la referencia en la base de datos
                archivo.setNombreDuiPersonal(file.getOriginalFilename());
                archivo.setDatosDuiPersonal(bytes);
                archivo.setFechaDuiPersonal(new Date());
                archivo.setPersonalAdministrativo(administrativo);
                anexoService.guardaAnexo(archivo);

                redirectAttributes.addFlashAttribute("success",
                        "Has subido correctamente su DUI con nombre: '" + file.getOriginalFilename() + "'");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/expedienteadministrativo/Documentos/" + duiPersonal;
        }

        // Cuando la clave es Curriculum
        else if (clave.equals("curriculum")) {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("success", "Por favor, seleccione un archivo para subir.");
                return "redirect:/expedienteadministrativo/Documentos/" + administrativo.getDuiPersonal();
            }
            try {
                byte[] bytes = file.getBytes();

                // Guarda la referencia en la base de datos
                archivo.setNombreCurriculumPersonal(file.getOriginalFilename());
                archivo.setDatosCurriculumPersonal(bytes);
                archivo.setFechaCurriculumPersonal(new Date());
                archivo.setPersonalAdministrativo(administrativo);
                anexoService.guardaAnexo(archivo);

                redirectAttributes.addFlashAttribute("success",
                        "Has subido correctamente su curriculum con nombre: '" + file.getOriginalFilename() + "'");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/expedienteadministrativo/Documentos/" + duiPersonal;
        }

        // Cuando la clave es NUP
        else if (clave.equals("nup")) {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("success", "Por favor, seleccione un archivo para subir.");
                return "redirect:/expedienteadministrativo/Documentos/" + administrativo.getDuiPersonal();
            }
            try {
                byte[] bytes = file.getBytes();

                // Guarda la referencia en la base de datos
                archivo.setNombreNupPersonal(file.getOriginalFilename());
                archivo.setDatosNupPersonal(bytes);
                archivo.setFechaNupPersonal(new Date());
                archivo.setPersonalAdministrativo(administrativo);
                anexoService.guardaAnexo(archivo);

                redirectAttributes.addFlashAttribute("success",
                        "Has subido correctamente su NUP con nombre: '" + file.getOriginalFilename() + "'");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/expedienteadministrativo/Documentos/" + duiPersonal;
        }
        return "redirect:/expedienteadministrativo/plantaadministrativa";
    }

    // Controlador para eliminar archivos
    @GetMapping("/administrativo/delete/{clave}/{id}/{duiPersonal}")
    public String deleteAnexo(@PathVariable("clave") String clave, @PathVariable("id") int id, Model model,
            @PathVariable("duiPersonal") String duiPersonal, RedirectAttributes redirectAttributes) {

        // Obtiene los anexos del administrativo
        AnexoPersonalAdministrativo archivo = anexoService.buscarAnexoPorId(id);

        // Cuando la clave es DUI
        if (clave.equals("dui")) {
            // Elimina los datos relacionados con el DUI
            archivo.setNombreDuiPersonal(null);
            archivo.setDatosDuiPersonal(null);
            archivo.setFechaDuiPersonal(null);
            anexoService.guardaAnexo(archivo);
            redirectAttributes.addFlashAttribute("warning", "Se ha eliminado correctamente el archivo ");

            return "redirect:/expedienteadministrativo/Documentos/" + duiPersonal;
        }

        // Cuando la clave es curriculum
        else if (clave.equals("curriculum")) {
            // Elimina los datos relacionados con el curriculum
            archivo.setNombreCurriculumPersonal(null);
            archivo.setDatosCurriculumPersonal(null);
            archivo.setFechaCurriculumPersonal(null);
            anexoService.guardaAnexo(archivo);
            redirectAttributes.addFlashAttribute("warning", "Se ha eliminado correctamente el archivo ");

            return "redirect:/expedienteadministrativo/Documentos/" + duiPersonal;
        }

        // Cuando la clave es NUP
        else if (clave.equals("nup")) {
            // Elimina los datos relacionados con el curriculum
            archivo.setNombreNupPersonal(null);
            archivo.setDatosNupPersonal(null);
            archivo.setFechaNupPersonal(null);
            anexoService.guardaAnexo(archivo);
            redirectAttributes.addFlashAttribute("warning", "Se ha eliminado correctamente el archivo ");

            return "redirect:/expedienteadministrativo/Documentos/" + duiPersonal;
        }
        
        return "Expediente_administrativo/upload";
    }

    // Controlador para mostrar archivos
    @GetMapping("/administrativo/expedienteadministrativo/files/{clave}/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> mostrarAnexo(@PathVariable int id, @PathVariable("clave") String clave) {
        // Busca el archivo en el repositorio por su ID
        AnexoPersonalAdministrativo fileEntity = anexoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Cuando la clave es DUI
        if (clave.equals("dui")) {
            // Crea un recurso de bytes a partir de los datos del archivo
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getDatosDuiPersonal());
            // Devuelve la respuesta con el contenido del archivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + fileEntity.getNombreDuiPersonal() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }

        // Cuando la clave es curriculum
        else if (clave.equals("curriculum")) {
            // Crea un recurso de bytes a partir de los datos del archivo
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getDatosCurriculumPersonal());
            // Devuelve la respuesta con el contenido del archivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + fileEntity.getNombreCurriculumPersonal() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }

        // Cuando la clave es nup
        else if (clave.equals("nup")) {
            // Crea un recurso de bytes a partir de los datos del archivo
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getDatosNupPersonal());
            // Devuelve la respuesta con el contenido del archivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + fileEntity.getDatosNupPersonal() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }
        return null;
    }
}
