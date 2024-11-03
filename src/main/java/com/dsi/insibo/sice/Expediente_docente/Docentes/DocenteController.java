package com.dsi.insibo.sice.Expediente_docente.Docentes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.dsi.insibo.sice.Expediente_docente.Administrativos.AdministrativoService;
import com.dsi.insibo.sice.Expediente_docente.Docentes.Anexos.AnexoDocenteService;
import com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad.PasswordGenerator;
import com.dsi.insibo.sice.Seguridad.SeguridadService.SessionService;
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
    @Autowired
    private DocenteService docenteService;
    @Autowired
    private AdministrativoService administrativoService;
    @Autowired
    SessionService sesion;

    // CONTROLADORES CRUD
    // Guardar un docente (guardar rol)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @PostMapping("/guardar")
    public String guardar(@Validated @ModelAttribute Docente docente,
            @RequestParam("docenteRol") String rolSeleccionado, BindingResult result, Model model,
            RedirectAttributes attribute) {

        // Verifica si el correoDocente ya está en uso
        if (docenteService.correoYaRegistrado(docente.getCorreoDocente()) == true
                || administrativoService.correoYaRegistrado(docente.getCorreoDocente())) {
            model.addAttribute("profesor", docente);
            model.addAttribute("ROL", rolSeleccionado);
            model.addAttribute("titulo", "Nuevo usuario");
            model.addAttribute("warning", "Alerta: Este correo ya esta siendo utilizado.");
            return "Expediente_docente/Docentes/fichaDocente";
        }

        // Verifica si el DUI ya está en uso y si el registro está activo
        Docente docenteExistente = docenteService.buscarPorIdDocente(docente.getDuiDocente());
        if (docenteExistente != null) {
            if (docenteExistente.isActivoDocente()) {
                attribute.addFlashAttribute("error", "Error: Este DUI ya esta siendo utilizado.");
                return "redirect:plantadocente";
            } else {
                attribute.addFlashAttribute("error",
                        "Error: Este registro ya existe en el sistema pero esta desactivo, contacta con el administrador del sistema.");
                return "redirect:plantadocente";
            }
        }

        // Verifica si el NIP, NUP & NIT ya están en uso
        if (docenteService.nipYaRegistrado(docente.getNip()) == true) {
            model.addAttribute("profesor", docente);
            model.addAttribute("ROL", rolSeleccionado);
            model.addAttribute("titulo", "Nuevo usuario");
            model.addAttribute("warning", "Alerta: El número de NIP ya esta siendo utilizado.");
            return "Expediente_docente/Docentes/fichaDocente";
        } else if (docenteService.nupYaRegistrado(docente.getNup()) == true) {
            model.addAttribute("profesor", docente);
            model.addAttribute("ROL", rolSeleccionado);
            model.addAttribute("titulo", "Nuevo usuario");
            model.addAttribute("warning", "Alerta: El número de NUP ya esta siendo utilizado.");
            return "Expediente_docente/Docentes/fichaDocente";
        } else if (docenteService.nitYaRegistrado(docente.getNit()) == true) {
            model.addAttribute("profesor", docente);
            model.addAttribute("ROL", rolSeleccionado);
            model.addAttribute("titulo", "Nuevo usuario");
            model.addAttribute("warning", "Alerta: El número de NIT ya esta siendo utilizado.");
            return "Expediente_docente/Docentes/fichaDocente";
        }

        // CREACIÓN DEL USUARIO
        Usuario usuario = new Usuario();
        usuario.setDocente(docente); // DUI-DOCENTE
        usuario.setCorreoUsuario(docente.getCorreoDocente()); // Correo
        usuario.setEnabled(false); // Activo
        usuario.setAccountLocked(true); // Bloqueado
        usuario.setAccountNoExpired(true); // Expirado
        usuario.setCredentialNoExpired(true); // Credencial expirada
        usuario.setPrimerIngreso(true); // Primera vez
        usuario.setContrasenaUsuario(" "); // Contraseña
        Long idRol = 0L;
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
            return "expedientedocente/formulario";
        }

        docente.setActivoDocente(true);
        docenteService.guardarDocente(docente);
        usuarioService.asignarRol(usuario, idRol); // Guardado y asignado de rol docente
        attribute.addFlashAttribute("success", "Expediente creado con exito!");

        return "redirect:plantadocente";
    }

    // Actualizar un docente (Si actualiza rol)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @PostMapping("/actualizar")
    public String actualizar(@Validated @ModelAttribute Docente docente,
            @RequestParam("docenteRol") String rolSeleccionado, BindingResult result, Model model,
            RedirectAttributes attribute) {

        Docente docenteExistente = docenteService.buscarPorIdDocente(docente.getDuiDocente());
        // Verifica que exista un registro asociado al DUI
        if (docenteExistente != null) {
            // Verifica que el correo del registro asociado al DUI sea diferente al correo al que se quiere actualizar
            if (!docente.getCorreoDocente().equals(docenteExistente.getCorreoDocente())) {
                // Verifica si el correo al que se quiere actualizar ya este registrado
                if (docenteService.correoYaRegistrado(docente.getCorreoDocente()) == true
                        || administrativoService.correoYaRegistrado(docente.getCorreoDocente())) {
                    model.addAttribute("warning", "Alerta: El correo ya esta siendo utilizado.");
                    model.addAttribute("editar", true); // Deshabilita el campo DUI
                    model.addAttribute("profesor", docente);
                    model.addAttribute("ROL", rolSeleccionado);
                    model.addAttribute("editar", true); // Indica que se está editando un docente
                    model.addAttribute("titulo", "Editar docente");
                    return "Expediente_docente/Docentes/fichaDocente";
                }
            }
            // Verifica que el NIP del registro asociado al DUI sea diferente al NUP al que se quiere actualizar
            if (!docenteExistente.getNip().equals(docente.getNip())) {
                // Verifica si el NIP al que se quiere actualizar ya este registrado
                if (docenteService.nipYaRegistrado(docente.getNip()) == true) {
                    model.addAttribute("warning", "Alerta: El número de NIP ya esta siendo utilizado.");
                    model.addAttribute("editar", true); // Deshabilita el campo DUI
                    model.addAttribute("profesor", docente);
                    model.addAttribute("ROL", rolSeleccionado);
                    model.addAttribute("editar", true); // Indica que se está editando un docente
                    model.addAttribute("titulo", "Editar docente");
                    return "Expediente_docente/Docentes/fichaDocente";
                }
            }
            // Verifica que el NUP del registro asociado al DUI sea diferente al NUP al que se quiere actualizar
            if (!docenteExistente.getNup().equals(docente.getNup())) {
                // Verifica si el NUP al que se quiere actualizar ya este registrado
                if (docenteService.nupYaRegistrado(docente.getNup()) == true) {
                    model.addAttribute("warning", "Alerta: El número de NUP ya esta siendo utilizado.");
                    model.addAttribute("editar", true); // Deshabilita el campo DUI
                    model.addAttribute("profesor", docente);
                    model.addAttribute("ROL", rolSeleccionado);
                    model.addAttribute("editar", true); // Indica que se está editando un docente
                    model.addAttribute("titulo", "Editar docente");
                    return "Expediente_docente/Docentes/fichaDocente";
                }
            }
            // Verifica que el NIT del registro asociado al DUI sea diferente al NIT al que se quiere actualizar
            if (!docenteExistente.getNit().equals(docente.getNit())) {
                // Verifica si el NIT al que se quiere actualizar ya este registrado
                if (docenteService.nitYaRegistrado(docente.getNit()) == true) {
                    model.addAttribute("warning", "Alerta: El número de NIT ya esta siendo utilizado.");
                    model.addAttribute("editar", true); // Deshabilita el campo DUI
                    model.addAttribute("profesor", docente);
                    model.addAttribute("ROL", rolSeleccionado);
                    model.addAttribute("editar", true); // Indica que se está editando un docente
                    model.addAttribute("titulo", "Editar docente");
                    return "Expediente_docente/Docentes/fichaDocente";
                }
            }
        }

        // Actualizamos el usuario
        Usuario usuario = usuarioService.buscarPorIdDocente(docente.getDuiDocente());
        usuario.setCorreoUsuario(docente.getCorreoDocente()); // Nuevo Correo
        usuario.getRolesUsuario().clear();
        Long idRol = 0L;
        switch (rolSeleccionado) { // Nuevo Rol
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
        usuarioService.asignarRol(usuario, idRol); // Guardamos la actualización
        docente.setActivoDocente(true);
        docenteService.guardarDocente(docente);
        attribute.addFlashAttribute("success", "Expediente actualizado con éxito!");
        return "redirect:plantadocente";
    }

    // Actualizar ficha del docente que ha iniciado sesión (No actualiza rol)
    @PreAuthorize("hasRole('DOCENTE')")
    @PostMapping("/actualizarGen")
    public String actualizarGen(@Validated @ModelAttribute Docente docente,
            @RequestParam("docenteRol") String rolSeleccionado, BindingResult result, Model model,
            RedirectAttributes attribute) {

        docente.setActivoDocente(true);
        docenteService.guardarDocente(docente);
        attribute.addFlashAttribute("success", "Expediente actualizado con éxito!");
        return "redirect:miexpediente";
    }

    // Consultar un docente
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @GetMapping("/consultarexpediente/{id}")
    public String consultarDocente(@PathVariable("id") String idDocente, Model model, RedirectAttributes attribute) {

        Docente profesor = docenteService.buscarPorIdDocente(idDocente);
        if (profesor == null) {
            attribute.addFlashAttribute("error", "El expediente no existe dentro del sistema");
            return "redirect:/expedientedocente/plantadocente";
        }

        model.addAttribute("profesor", profesor);
        model.addAttribute("editar", true);
        model.addAttribute("titulo", "Expediente");
        return "Expediente_docente/Docentes/fichaDocenteConsult";
    }

    // Consultar el docente que ha iniciado sesión
    @PreAuthorize("hasRole('DOCENTE')")
    @GetMapping("/miexpediente")
    public String consultarMiExpediente(Model model, RedirectAttributes attribute) {

        String idDocente = sesion.duiSession();
        Docente profesor = docenteService.buscarPorIdDocente(idDocente);
        if (profesor == null) {
            attribute.addFlashAttribute("error", "El expediente no existe dentro del sistema");
            return "redirect:/";
        }

        model.addAttribute("profesor", profesor);
        model.addAttribute("editar", true);
        model.addAttribute("titulo", "Expediente");
        return "Expediente_docente/Docentes/fichaDocenteConsult";
    }

    // Listar todos los docentes
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @GetMapping("/plantadocente")
    public String listarDocentes(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // page indica el numero de pagina en el que se encontrara por defecto Size el numero de registros por pagina

        // Hace la conversion de la estructura List a Page
        PageRequest pageRequest = PageRequest.of(page, size);
        List<DocenteDTO> listadoDocentes = docenteService.listarDocentes();

        Page<DocenteDTO> pagedocentes = new PageImpl<>(
                listadoDocentes.subList(pageRequest.getPageNumber() * pageRequest.getPageSize(),
                        Math.min((pageRequest.getPageNumber() + 1) * pageRequest.getPageSize(),
                                listadoDocentes.size())),
                pageRequest, listadoDocentes.size());

        model.addAttribute("titulo", "Planta Docente");
        model.addAttribute("Docentes", listadoDocentes);
        // model.addAttribute("DocentesInactivos", listadoDocentesInactivos);
        model.addAttribute("totalPages", pagedocentes.getTotalPages());
        // Hace el envio de la estructura con paginación a la vista
        model.addAttribute("page", pagedocentes);
        return "Expediente_docente/Docentes/listarDocentes"; // Vista HTML
    }

    // Eliminar un docente (Pasa a estado inactivo)
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @GetMapping("/eliminarexpediente/{id}")
    public String eliminarDocente(@PathVariable("id") String idDocente, RedirectAttributes attribute) {
        Docente profesor = docenteService.buscarPorIdDocente(idDocente);

        // Controlando el ingreso de un id inexistente desde URL
        if (profesor == null) {
            attribute.addFlashAttribute("error", "El expediente no existe dentro del sistema");
            return "redirect:/expedientedocente/plantadocente";
        }

        // Modifiaciones de Usuario
        Usuario usuario = usuarioService.buscarPorCorreo(profesor.getCorreoDocente()); //Obtengo el correo
        usuario.setAccountLocked(false); // Bloqueo al usuario
        usuario.setEnabled(true); // Activo para que no vaya a rechazados
        usuario.setContrasenaUsuario(passwordEncoder.encode(PasswordGenerator.generateRandomPassword(8))); //Contraseña encriptada
        usuarioService.guardarUsuario(usuario); // Guardo la actualización de estado

        // cuando un docente es "eliminado" este no se borra del sistema sino que pasa a un estado 'inactivo'
        profesor.setActivoDocente(false);
        docenteService.guardarDocente(profesor);
        attribute.addFlashAttribute("warning", "El docente " + profesor.getNombreDocente() + " "
                + profesor.getApellidoDocente() + " ha sido eliminado de la planta docente");
        return "redirect:/expedientedocente/plantadocente";
    }

    // OTROS CONTROLADORES
    // Habilitar el formulario en modo 'agregar'
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @GetMapping("/formulario")
    public String abrirformulario(Model model) {
        Docente profesor = new Docente();

        model.addAttribute("profesor", profesor);
        model.addAttribute("tipoNIT", false);
        model.addAttribute("titulo", "Nuevo usuario");

        return "Expediente_docente/Docentes/fichaDocente";
    }

    // Habilitar el formulario en modo 'editar'
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
    @GetMapping("/editarexpediente/{id}")
    public String editarDocente(@PathVariable("id") String idDocente, Model model, RedirectAttributes attribute) {
        Docente profesor = docenteService.buscarPorIdDocente(idDocente);
        // Por si ingresa un id inexistente desde URL
        if (profesor == null) {
            attribute.addFlashAttribute("error", "El expediente no existe dentro del sistema");
            return "redirect:/expedientedocente/plantadocente";
        }

        Usuario usuario = usuarioService.buscarPorIdDocente(idDocente);
        // Por si ha ingresado el registro desde la bd y no tiene un usuario creado
        if (usuario == null) {
            attribute.addFlashAttribute("error", "El usuario asociado al expediente no existe dentro del sistema");
            return "redirect:/expedientedocente/plantadocente";
        }
        String ROL;
        switch (usuario.getRolesUsuarioNombres()) {
            case "DOCENTE":
                ROL = "Docente";
                break;
            case "SUBDIRECTORA":
                ROL = "Subdirector";
                break;
            case "DIRECTOR":
                ROL = "Director";
                break;
            default:
                ROL = "";
                break;
        }

        boolean homologado;
        if (profesor.getDuiDocente().equals(profesor.getNit())) {
            homologado = true;
        } else {
            homologado = false;
        }

        model.addAttribute("profesor", profesor);
        model.addAttribute("ROL", ROL);
        model.addAttribute("tipoNIT", homologado);
        model.addAttribute("editar", true); // Indica que se está editando un docente
        model.addAttribute("titulo", "Editar docente");
        return "Expediente_docente/Docentes/fichaDocente";
    }

    // habilitar el formulario en modo 'editar' con ficha limitada
    @PreAuthorize("hasRole('DOCENTE')")
    @GetMapping("/editarmiexpediente")
    public String editarComoDocente(Model model, RedirectAttributes attribute) {
        String idDocente = sesion.duiSession();
        Docente profesor = docenteService.buscarPorIdDocente(idDocente);
        if (profesor == null) {
            attribute.addFlashAttribute("error", "El expediente no existe dentro del sistema");
            return "redirect:/";
        }

        model.addAttribute("profesor", profesor);
        model.addAttribute("titulo", "Editar mi expediente");
        return "Expediente_docente/Docentes/fichaDocenteLimitada";
    }

    // Habilitar la sección de anexos
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SECRETARIA', 'DOCENTE', 'SUBDIRECTORA', 'DIRECTOR')")
    @GetMapping("Documentos/{id}")
    public String docenteDocumentos(@PathVariable("id") String idDocente, Model model,
            RedirectAttributes attributes) {

        Docente docente = null;
        if (idDocente != "") {
            // Busca el docente a traves del DUI
            docente = docenteService.buscarPorIdDocente(idDocente);

            // Verifica que exista
            if (docente == null) {
                attributes.addFlashAttribute("error", "El expediente no existe dentro del sistema");
                return "redirect:/expedientedocente/plantaadministrativa";
            }
        } else {
            // Maneja el caso donde el iddocente no es válido
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
        return "Expediente_docente/Docentes/docenteDocumentos";
    }

}