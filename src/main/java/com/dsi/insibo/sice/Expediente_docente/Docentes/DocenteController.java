package com.dsi.insibo.sice.Expediente_docente.Docentes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.dsi.insibo.sice.Expediente_docente.Docentes.Anexos.AnexoDocenteService;
import com.dsi.insibo.sice.Seguridad.SeguridadService.UsuarioService;
import com.dsi.insibo.sice.entity.AnexoDocente;
import com.dsi.insibo.sice.entity.Docente;
import com.dsi.insibo.sice.entity.Usuario;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/expedientedocente")
public class DocenteController {
    @Autowired
    private AnexoDocenteService anexoDocenteService;

    @Autowired
    private UsuarioService usuarioService;
    // Lista docentes usando la DB /expedientedocente/plantadocente
    @Autowired
    private DocenteService docenteService;

    // Direccionadores estaticos
    @GetMapping("/anexosDocente")
    public String docentes() {
        return "Expediente_docente/Docentes/anexosDocente";
    }

    @GetMapping("/barra")
    public String barra() {
        return "Expediente_docente/barra";
    }

    // Direccionadores de acción
    // Ficha general de expediente docente /expedientedocente/formulario
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @GetMapping("/formulario")
    public String abrirformulario(Model model) {
        Docente profesor = new Docente();

        model.addAttribute("profesor", profesor);
        model.addAttribute("titulo", "Nuevo usuario");

        return "Expediente_docente/Docentes/fichaDocente";
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    // guardando formulario
    @PostMapping("/guardar")
    public String guardar(@Validated @ModelAttribute Docente docente,
            @RequestParam("docenteRol") String rolSeleccionado, BindingResult result, Model model,
            RedirectAttributes attribute) {

        // Inicia cambio
        Docente docenteExistente = docenteService.buscarPorIdDocente(docente.getDuiDocente());

        // Verifica si el DUI ya esta asociado a un registro
        if (docenteExistente != null) {
            // Si lo esta
            attribute.addFlashAttribute("error", "Error: El DUI ya está registrado.");
            return "redirect:plantadocente";
        } // Fin de la modificacion
          // Codigo de usuario
        else {

            // CREACIÓN DEL USUARIO
            Usuario usuario = new Usuario();
            usuario.setDocente(docente);                            // DUI-DOCENTE
            usuario.setCorreoUsuario(docente.getCorreoDocente());   // Correo
            usuario.setEnabled(false);                      // Activo
            usuario.setAccountLocked(true);           // Bloqueado  
            usuario.setAccountNoExpired(true);     // Expirado
            usuario.setCredentialNoExpired(true); // Credencial expirada
            usuario.setPrimerIngreso(true);           // Primera vez
            usuario.setContrasenaUsuario(" ");    // Contraseña
            Long idRol = 0L;
            System.out.println(rolSeleccionado);                    // Rol
            switch (rolSeleccionado) {
                case "Docente":
                    idRol = 2L;
                    break;
                case "Subdirector":
                    idRol = 5L;
                    break;
                case "Director":
                    idRol = 6L;
                    break;                                     
                default:
                    idRol = 2L;
                    break;
            }
            
            if (result.hasErrors()) {
                model.addAttribute("profesor", docente);
                System.out.println("Se tienen errores en el formulario");
                return "expedientedocente/formulario";
            }

            // Fin del codigo para usuario
            // guardando el registro
            docenteService.guardarDocente(docente);
            usuarioService.asignarRol(usuario, idRol); //Guardado y asignado de rol
            attribute.addFlashAttribute("success", "Expediente creado con exito!");

            return "redirect:plantadocente";
        }
    }

    // actualizando la informacion de un docente
    @PostMapping("/actualizar")
    public String actualizar(@Validated @ModelAttribute Docente docente,
            @RequestParam("docenteRol") String rolSeleccionado, BindingResult result, Model model,
            RedirectAttributes attribute) {

        // Actualizamos el usuario
        Usuario usuario = usuarioService.buscarPorIdDocente(docente.getDuiDocente());
        usuario.setCorreoUsuario(docente.getCorreoDocente());   // Nuevo Correo
        usuario.getRolesUsuario().clear();
        Long idRol = 0L;
        System.out.println(rolSeleccionado);                    
        switch (rolSeleccionado) {                              // Nuevo Rol
            case "Docente":
                idRol = 2L;
                break;
            case "Subdirector":
                idRol = 5L;
                break;
            case "Director":
                idRol = 6L;
                break;                                     
            default:
                idRol = 2L;
                break;
        }
        usuarioService.asignarRol(usuario, idRol);          // Guardamos la actualización
        docenteService.guardarDocente(docente);
        attribute.addFlashAttribute("success", "Expediente actualizado con éxito!");
        return "redirect:plantadocente";
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'DOCENTE', 'SUBDIRECTORA')")
    @GetMapping("/plantadocente")
    public String listarDocentes(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // page indica el numero de pagina en el que se encontrara por defecto
        // Size el numero de registros por pagina

        // Hace la conversion de la estructura List a Page
        PageRequest pageRequest = PageRequest.of(page, size);
        List<DocenteDTO> listadoDocentes = docenteService.listarDocentes();
        Page<DocenteDTO> pagedocentes = new PageImpl<>(listadoDocentes.subList(
                pageRequest.getPageNumber() * pageRequest.getPageSize(),
                Math.min((pageRequest.getPageNumber() + 1) * pageRequest.getPageSize(), listadoDocentes.size())),
                pageRequest, listadoDocentes.size());

        model.addAttribute("titulo", "Planta Docente");
        model.addAttribute("Docentes", listadoDocentes);
        // Hace el envio de la estructura con paginación a la vista
        model.addAttribute("page", pagedocentes);
        return "Expediente_docente/Docentes/listarDocentes"; // Vista HTML
    }

    // Consultando docente
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @GetMapping("/consultarexpediente/{id}")
    public String consultarDocente(@PathVariable("id") String idDocente, Model model, RedirectAttributes attribute) {

        Docente profesor = docenteService.buscarPorIdDocente(idDocente);
        if (profesor == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedientedocente/plantadocente";
        }

        model.addAttribute("profesor", profesor);
        model.addAttribute("editar", true); // Indica que se está editando un docente
        return "Expediente_docente/Docentes/fichaDocenteConsult";
    }

    // Editando docente
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    // Editando docente como director, subdirector
    @GetMapping("/editarexpediente/{id}")
    public String editarDocente(@PathVariable("id") String idDocente, Model model, RedirectAttributes attribute) {

        Docente profesor = docenteService.buscarPorIdDocente(idDocente);
        if (profesor == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedientedocente/plantadocente";
        }

        model.addAttribute("profesor", profesor);
        model.addAttribute("editar", true); // Indica que se está editando un docente
        return "Expediente_docente/Docentes/fichaDocenteEdit";
    }

    @PreAuthorize("hasRole('DOCENTE')")
    // Editando docente como docente, osea solo datos generales
    @GetMapping("/editarmiexpediente/{id}")
    public String editarComoDocente(@PathVariable("id") String idDocente, Model model, RedirectAttributes attribute) {
        Docente profesor = docenteService.buscarPorIdDocente(idDocente);
        if (profesor == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedientedocente/plantadocente";
        }

        model.addAttribute("profesor", profesor);
        return "Expediente_docente/Docentes/fichaDocenteLimitada";
    }

    // Eliminar ficha
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @GetMapping("/eliminarexpediente/{id}")
    public String eliminarDocente(@PathVariable("id") String idDocente, RedirectAttributes attribute) {
        Docente profesor = docenteService.buscarPorIdDocente(idDocente);

        if (profesor == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedientedocente/plantadocente";
        }

        anexoDocenteService.eliminarAnexoDocente(idDocente);
        usuarioService.eliminarUsuarioPorDocenteId(idDocente);
        docenteService.eliminar(idDocente);
        attribute.addFlashAttribute("warning", "El expediente se elimino");

        return "redirect:/expedientedocente/plantadocente";
    }

    @Controller
    public class OtroController {
        @RequestMapping("/inicioexpedientes")
        public String inicio() {
            return "Expediente_docente/inicioExpedientes";
        }
    }

    // Gestionando anexos
    @GetMapping("Documentos/{id}")
    public String docenteDocumentos(@PathVariable("id") String idDocente, Model model,
            RedirectAttributes attributes) {

        Docente docente = null;
        if (idDocente != "") {
            // Busca el docente a traves del DUI
            docente = docenteService.buscarPorIdDocente(idDocente);

            // Verifica que exista
            if (docente == null) {
                attributes.addFlashAttribute("error", "Error: El expediente no existe");
                return "redirect:/expedientedocente/plantaadministrativa";
            }
        } else {
            // Maneja el caso donde el iddocente no es válido
            System.out.println("Error: ¡El idDocente ingresado no es válido!");
            attributes.addFlashAttribute("error", "Error: ¡El iddocente ingresado no es válido!");
            return "redirect:/expedientedocente/plantadocente";
        }

        // Obtener los anexos asociados al docente
        AnexoDocente anexos = anexoDocenteService.buscarDocente(idDocente);

        // Agregar atributos al modelo para ser utilizados en la vista
        model.addAttribute("docente", docente);
        model.addAttribute("anexos", anexos);
        model.addAttribute("titulo", "Documentos");

        // return "Expediente_docente/docenteDocumentos";
        return "Expediente_docente/docentes/docenteDocumentos";
    }
}
