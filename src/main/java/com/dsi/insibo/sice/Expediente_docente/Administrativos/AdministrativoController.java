package com.dsi.insibo.sice.Expediente_docente.Administrativos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.dsi.insibo.sice.Expediente_docente.Administrativos.Anexos.AnexoAdministrativoService;
import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteService;
import com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad.PasswordGenerator;
import com.dsi.insibo.sice.Seguridad.SeguridadService.UsuarioService;
import com.dsi.insibo.sice.entity.AnexoPersonalAdministrativo;
import com.dsi.insibo.sice.entity.PersonalAdministrativo;
import com.dsi.insibo.sice.entity.Usuario;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SECRETARIA', 'SUBDIRECTORA', 'DIRECTOR')")
@RequestMapping("/expedienteadministrativo")
public class AdministrativoController {
    @Autowired
    private AdministrativoService administrativoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AnexoAdministrativoService anexoadministrativoService;
    @Autowired
    private DocenteService docenteService;

    // CONTROLADORES CRUD
    // Guardar un empleado administrativo
    @PostMapping("/guardar")
    public String guardar(@Validated @ModelAttribute PersonalAdministrativo administrativo,
            @RequestParam("administrativoRol") String rolSeleccionado, BindingResult result,
            Model model, RedirectAttributes attribute) {

        // Verifica si el correoPersonal ya está en uso
        if (administrativoService.correoYaRegistrado(administrativo.getCorreoPersonal()) == true
            || docenteService.correoYaRegistrado(administrativo.getCorreoPersonal()) == true) {
            model.addAttribute("administrativo", administrativo);
            model.addAttribute("ROL", rolSeleccionado);
            model.addAttribute("warning", "Alerta: Este correo ya esta siendo utilizado.");
            return "Expediente_docente/Administrativos/fichaAdministrativo";
        }

        // Verifica si el DUI ya está en uso y si el registro está activo
        PersonalAdministrativo administrativoExistente = administrativoService
                .buscarPorIdAdministrativo(administrativo.getDuiPersonal());
        if (administrativoExistente != null) {
            if (administrativoExistente.isActivoPersonalAdministrativo()) {
                attribute.addFlashAttribute("error", "Error: Este DUI ya esta siendo utilizado.");
                return "redirect:plantaadministrativa";
            } else {
                attribute.addFlashAttribute("error",
                        "Error: Este registro ya existe en el sistema pero esta desactivo, contacta con el administrador del sistema.");
                return "redirect:plantaadministrativa";
            }
        }

        // Verifica si el NUP o NIT ya están en uso
        if (administrativoService.nitYaRegistrado(administrativo.getNitPersonal()) == true) {
            model.addAttribute("warning", "Alerta: El número de NIT ya esta siendo utilizado.");
            model.addAttribute("ROL", rolSeleccionado);
            model.addAttribute("administrativo", administrativo);
            return "Expediente_docente/Administrativos/fichaAdministrativo";
        } else if (administrativoService.nupYaRegistrado(administrativo.getNupPersonal()) == true) {
            model.addAttribute("warning", "Alerta: El número de NUP ya esta siendo utilizado.");
            model.addAttribute("ROL", rolSeleccionado);
            model.addAttribute("administrativo", administrativo);
            return "Expediente_docente/Administrativos/fichaAdministrativo";
        }

        // Si todas las validaciones pasaron, procede a guardar el administrativo
        // CREACIÓN DEL USUARIO
        Usuario usuario = new Usuario();
        usuario.setPersonalAdministrativo(administrativo); // DUI-DOCENTE
        usuario.setCorreoUsuario(administrativo.getCorreoPersonal());// Correo
        usuario.setEnabled(false); // Activo
        usuario.setAccountLocked(true); // Bloqueado
        usuario.setAccountNoExpired(true); // Expirado
        usuario.setCredentialNoExpired(true); // Credencial expirada
        usuario.setPrimerIngreso(true); // Primera vez
        usuario.setContrasenaUsuario(" "); // Contraseña
        Long idRol = 4L;
        switch (rolSeleccionado) {
            case "Secretaria":
                idRol = 3L;
                break;
            case "Bibliotecaria":
                idRol = 7L;
                break;
            default:
                idRol = 4L;
                break;
        }

        // Guardando el administrativo
        administrativo.setActivoPersonalAdministrativo(true);
        administrativoService.guardarAdministrativo(administrativo);
        usuarioService.asignarRol(usuario, idRol);
        attribute.addFlashAttribute("success", "Expediente creado con exito!");
        return "redirect:plantaadministrativa";

    }

    // Actualizar un empleado administrativo (Si actualiza rol)
    @PostMapping("/actualizar")
    public String actualizar(@Validated @ModelAttribute PersonalAdministrativo administrativo,
            @RequestParam("administrativoRol") String rolSeleccionado, BindingResult result,
            Model model, RedirectAttributes attribute) {

        PersonalAdministrativo administrativoExistente = administrativoService.buscarPorIdAdministrativo(administrativo.getDuiPersonal());
        // Verifica que exista un registro asociado al DUI
        if (administrativoExistente != null) {
            // Verifica que el correo del registro asociado al DUI sea diferente al correo al que se quiere actualizar
            if (!administrativo.getCorreoPersonal().equals(administrativoExistente.getCorreoPersonal())) {
                // Verifica si el correo al que se quiere actualizar ya este registrado
                if (administrativoService.correoYaRegistrado(administrativo.getCorreoPersonal()) == true
                    || docenteService.correoYaRegistrado(administrativo.getCorreoPersonal()) == true) {
                    model.addAttribute("editar", true); // Deshabilita el campo DUI
                    model.addAttribute("ROL", rolSeleccionado);
                    model.addAttribute("administrativo", administrativo);
                    model.addAttribute("warning", "Alerta: Este correo ya esta siendo utilizado.");
                    model.addAttribute("formAction", "/expedienteadministrativo/actualizar"); // Acción de edición
                    return "Expediente_docente/Administrativos/fichaAdministrativo";
                }
            }
            // Verifica que el NIT del registro asociado al DUI sea diferente al NIT al que se quiere actualizar
            if (!administrativoExistente.getNitPersonal().equals(administrativo.getNitPersonal())) {
                // Verifica si el NIT al que se quiere actualizar ya este registrado
                if (administrativoService.nitYaRegistrado(administrativo.getNitPersonal()) == true) {
                    model.addAttribute("editar", true); // Deshabilita el campo DUI
                    model.addAttribute("ROL", rolSeleccionado);
                    model.addAttribute("administrativo", administrativo);
                    model.addAttribute("warning", "Alerta: El número de NIT ya esta siendo utilizado.");
                    model.addAttribute("formAction", "/expedienteadministrativo/actualizar"); // Acción de edición
                    return "Expediente_docente/Administrativos/fichaAdministrativo";
                }
            }
            // Verifica que el NUP del registro asociado al DUI sea diferente al NUP al que se quiere actualizar
            if (!administrativoExistente.getNupPersonal().equals(administrativo.getNupPersonal())) {
                // Verifica si el NUP al que se quiere actualizar ya este registrado
                if (administrativoService.nupYaRegistrado(administrativo.getNupPersonal()) == true) {
                    model.addAttribute("editar", true); // Deshabilita el campo DUI
                    model.addAttribute("ROL", rolSeleccionado);
                    model.addAttribute("administrativo", administrativo);
                    model.addAttribute("warning", "Alerta: El número de NUP ya esta siendo utilizado.");
                    model.addAttribute("formAction", "/expedienteadministrativo/actualizar"); // Acción de edición
                    return "Expediente_docente/Administrativos/fichaAdministrativo";
                }
            }
        }
        
        // Actualizamos el usuario
        Usuario usuario = usuarioService.buscarPorIdPersonal(administrativo.getDuiPersonal());
        usuario.setCorreoUsuario(administrativo.getCorreoPersonal()); // Nuevo Correo
        usuario.getRolesUsuario().clear();
        Long idRol = 4L;
        switch (rolSeleccionado) { // Nuevo Rol
            case "Secretaria":
                idRol = 3L;
                break;
            case "Bibliotecaria":
                idRol = 7L;
                break;
            default:
                idRol = 4L;
                break;
        }
        usuarioService.asignarRol(usuario, idRol); // Guardamos la actualización
        administrativo.setActivoPersonalAdministrativo(true);
        administrativoService.guardarAdministrativo(administrativo);
        attribute.addFlashAttribute("success", "Expediente actualizado con exito!");
        return "redirect:plantaadministrativa";
    }

    // Consultar un empleado administrativo
    @GetMapping("/consultarexpediente/{id}")
    public String consultarAdministrativo(@PathVariable("id") String idAdministrativo, Model model,
            RedirectAttributes attribute) {
        PersonalAdministrativo administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);
        // Por si ingresa un id inexistente desde URL
        if (administrativo == null) {
            attribute.addFlashAttribute("error", "El expediente no existe dentro del sistema");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        model.addAttribute("administrativo", administrativo);
        model.addAttribute("titulo", "Expediente");
        return "Expediente_docente/Administrativos/fichaAdministrativoConsult";
    }

    // Listar todos los empleados administrativos
    @GetMapping("/plantaadministrativa")
    public String listarAdministrativos(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // page indica el numero de pagina en el que se encontrara por defecto Size el numero de registros por pagina
        // Hace la conversion de la estructura List a Page
        PageRequest pageRequest = PageRequest.of(page, size);
        List<AdministrativoDTO> listadoAdministrativos = administrativoService.listarAdministrativos();


        Page<AdministrativoDTO> pageAdministrativos = new PageImpl<>(listadoAdministrativos.subList(
                pageRequest.getPageNumber() * pageRequest.getPageSize(),
                Math.min((pageRequest.getPageNumber() + 1) * pageRequest.getPageSize(), listadoAdministrativos.size())),
                pageRequest, listadoAdministrativos.size());

        model.addAttribute("titulo", "Planta Administrativa");
        model.addAttribute("Administrativos", listadoAdministrativos);

        // Hace el envio de la estructura con paginación a la vista
        model.addAttribute("page", pageAdministrativos);
        model.addAttribute("totalPages", pageAdministrativos.getTotalPages());

        return "Expediente_docente/Administrativos/listarAdministrativos";
    }

    // Eliminar un empleado administrativo (Pasa a estado inactivo)
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/eliminarexpediente/{id}")
    public String eliminarAdministrativo(@PathVariable("id") String idAdministrativo, RedirectAttributes attribute) {
        PersonalAdministrativo administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);

        // Controlando el ingreso de un id inexistente desde URL
        if (administrativo == null) {
            attribute.addFlashAttribute("error", "El expediente no existe dentro del sistema");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        // Modifiaciones de Usuario
        Usuario usuario = usuarioService.buscarPorCorreo(administrativo.getCorreoPersonal());
        usuario.setAccountLocked(false); // Bloqueo al usuario
        usuario.setEnabled(true); // Activo para que no vaya a rechazados
        usuario.setContrasenaUsuario(passwordEncoder.encode(PasswordGenerator.generateRandomPassword(8))); //Contraseña encriptada
        usuarioService.guardarUsuario(usuario); // Guardo la actualización de estado

        // cuando un administrativo es "eliminado" este no se borra del sistema sino que pasa a un estado 'inactivo'
        administrativo.setActivoPersonalAdministrativo(false);
        administrativoService.guardarAdministrativo(administrativo);

        attribute.addFlashAttribute("warning", "El administrativo " + administrativo.getNombrePersonal() + " "
                + administrativo.getApellidoPersonal() + " ha sido eliminado de la planta administrativa");
        return "redirect:/expedienteadministrativo/plantaadministrativa";
    }

    // OTROS CONTROLADORES
    // Habilitar el formulario en modo 'agregar'
    @GetMapping("/formulario")
    public String ficha(Model model) {
        PersonalAdministrativo administrativo = new PersonalAdministrativo();

        model.addAttribute("administrativo", administrativo);
        model.addAttribute("tipoNIT", false);
        model.addAttribute("formAction", "/expedienteadministrativo/guardar"); // Acción de creación
        model.addAttribute("titulo", "Nuevo usuario");
        return "Expediente_docente/Administrativos/fichaAdministrativo";
    }

    // habilitar el formulario en modo 'editar'
    @GetMapping("/editarexpediente/{id}")
    public String editarAdministrativo(@PathVariable("id") String idAdministrativo, Model model,
            RedirectAttributes attribute) {
        PersonalAdministrativo administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);
        // Por si ingresa un id inexistente desde URL
        if (administrativo == null) {
            attribute.addFlashAttribute("error", "El expediente no existe dentro del sistema");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        Usuario usuario = usuarioService.buscarPorIdPersonal(idAdministrativo);
        // Por si ha ingresado el registro desde la bd y no tiene un usuario creado
        if (usuario == null) {
            attribute.addFlashAttribute("error", "El usuario asociado al expediente no existe dentro del sistema");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        String ROL;
        switch (usuario.getRolesUsuarioNombres()) {
            case "SECRETARIA":
                ROL = "Secretaria";
                break;
            case "BIBLIOTECARIO":
            ROL = "Bibliotecaria";
                break;
            case "PERSONAL_ADMINISTRATIVO":
            ROL = "Otro";
                break;
            default:
            ROL = "";
                break;
        }

        boolean homologado;
        if(administrativo.getDuiPersonal().equals(administrativo.getDuiPersonal())){
            homologado = true;
        }else{
            homologado = false;
        }

        model.addAttribute("administrativo", administrativo);
        model.addAttribute("ROL", ROL);
        model.addAttribute("tipoNIT", homologado);
        model.addAttribute("editar", true); // Deshabilita el campo DUI
        model.addAttribute("formAction", "/expedienteadministrativo/actualizar"); // Acción de edición
        return "Expediente_docente/Administrativos/fichaAdministrativo";
    }

    // habilitar la sección de anexos
    @GetMapping("Documentos/{id}")
    public String administrativoDocumentos(@PathVariable("id") String idAdministrativo, Model model,
            RedirectAttributes attributes) {

        PersonalAdministrativo administrativo = null;
        if (idAdministrativo != "") {
            // Busca el administrativo a traves del DUI
            administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);

            // Verifica que exista
            if (administrativo == null) {
                attributes.addFlashAttribute("error", "El expediente no existe dentro del sistema");
                return "redirect:/expedienteadministrativo/plantaadministrativa";
            }
        } else {
            // Maneja el caso donde el idAdministrativo no es válido
            attributes.addFlashAttribute("error", "Error: ¡El idAdministrativo ingresado no es válido!");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        // Obtener los anexos asociados al administrativo
        AnexoPersonalAdministrativo anexos = anexoadministrativoService.buscarAdministrativo(idAdministrativo);

        // Agregar atributos al modelo para ser utilizados en la vista
        model.addAttribute("administrativo", administrativo);
        model.addAttribute("anexos", anexos);
        model.addAttribute("titulo", "Documentos");

        return "Expediente_docente/Administrativos/AdministrativoDocumentos";
    }
}
