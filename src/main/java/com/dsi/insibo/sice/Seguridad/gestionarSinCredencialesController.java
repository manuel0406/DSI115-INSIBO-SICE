package com.dsi.insibo.sice.Seguridad;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad.EnvioCorreo;
import com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad.PasswordGenerator;
import com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad.UsuarioConNombre;
import com.dsi.insibo.sice.Seguridad.SeguridadService.UsuarioService;
import com.dsi.insibo.sice.entity.Usuario;
import com.dsi.insibo.sice.entity.UsuarioRoleEnum;
import com.dsi.insibo.sice.entity.UsuarioRoles;

@Controller
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class gestionarSinCredencialesController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EnvioCorreo envioCorreo;

    // -------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/gestionarSinCredenciales")
    public String cargarSinCredenciales(Model model, @RequestParam(required = false, defaultValue = "1") int pagina) {
        pagina = (pagina - 1);
        List<Usuario> listadoUsuarios = usuarioService.listaUsuariosDesactivadosIntervalos(pagina);

        List<UsuarioConNombre> listadoCompleto = new ArrayList<>();
        // Obtenemos los nombres
        for (Usuario usuario : listadoUsuarios) {

            Set<UsuarioRoles> rol = usuario.getRolesUsuario();
            // Verificar si hay algún UsuarioRoles con role_name igual a "ADMINISTRADOR"
            boolean isAdmin = rol.stream()
                    .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.ADMINISTRADOR));
            // Verificar si hay algún UsuarioRoles con role_name igual a "DOCENTE"
            boolean isDocente = rol.stream()
                    .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.DOCENTE));
            // Verificar si hay algún UsuarioRoles con role_name igual a
            // "PERSONAL_ADMINISTRATIVO"
            boolean isPersonal = rol.stream()
                    .anyMatch(
                            usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.PERSONAL_ADMINISTRATIVO));
            // Verificar si hay algún UsuarioRoles con role_name igual a "DIRECTOR"
            boolean isDirector = rol.stream()
                    .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.DIRECTOR));
            // Verificar si hay algún UsuarioRoles con role_name igual a "SECRETARIA"
            boolean isSecretaria = rol.stream()
                    .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.SECRETARIA));
            // Verificar si hay algún UsuarioRoles con role_name igual a "SUBDIRECTORA"
            boolean isSubDirectora = rol.stream()
                    .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.SUBDIRECTORA));
            // Verificar si hay algún UsuarioRoles con role_name igual a "BIBLIOTECARIA"
            boolean isBibliotecaria = rol.stream()
                    .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.BIBLIOTECARIO));
            String nombre = "";

            if (isAdmin) {
                nombre = "ADMINISTRADOR";
                if (usuario.getDocente() != null) {
                    nombre = usuario.getDocente().getNombreDocente() + " " + usuario.getDocente().getApellidoDocente();
                }
                if (usuario.getPersonalAdministrativo() != null) {
                    nombre = usuario.getPersonalAdministrativo().getNombrePersonal() + " "
                            + usuario.getPersonalAdministrativo().getApellidoPersonal();
                }
            }
            if (isDocente || isDirector || isSubDirectora) {
                nombre = "DOCENTE";
                if (usuario.getDocente() != null) {
                    nombre = usuario.getDocente().getNombreDocente() + " " + usuario.getDocente().getApellidoDocente();
                }
                if (usuario.getPersonalAdministrativo() != null) {
                    nombre = usuario.getPersonalAdministrativo().getNombrePersonal() + " "
                            + usuario.getPersonalAdministrativo().getApellidoPersonal();
                }
            }
            if (isPersonal || isSecretaria || isBibliotecaria) {
                nombre = "PERSONAL";
                if (usuario.getDocente() != null) {
                    nombre = usuario.getDocente().getNombreDocente() + " " + usuario.getDocente().getApellidoDocente();
                }
                if (usuario.getPersonalAdministrativo() != null) {
                    nombre = usuario.getPersonalAdministrativo().getNombrePersonal() + " "
                            + usuario.getPersonalAdministrativo().getApellidoPersonal();
                }
            }

            listadoCompleto.add(new UsuarioConNombre(usuario, nombre));
        }

        model.addAttribute("Usuarios", listadoCompleto);

        // Obtenemos la cantidad de usuarios que tenemos
        List<Usuario> totalUsuario = usuarioService.listaUsuariosDesactivados();
        int cantidad = (int) Math.ceil((double) totalUsuario.size() / 7);
        model.addAttribute("Cantidad", cantidad);

        return "Seguridad/gestionarSinCredenciales";
    }
    // -------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/rechazarUsuario/{id}")
    public String bloquearUsuario(@PathVariable("id") int idUsuario, RedirectAttributes attribute) {

        Usuario usuario = usuarioService.buscarPorIdUsuario(idUsuario);
        usuario.setAccountLocked(false);
        usuarioService.guardarUsuario(usuario);
        return "redirect:/gestionarSinCredenciales";
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // ENCRIPTAMIENTO DE CONTRASEÑA
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/aceptarUsuario/{id}")
    public String aceptarUsuario(@PathVariable("id") int idUsuario, RedirectAttributes attribute) {
        Usuario usuario = usuarioService.buscarPorIdUsuario(idUsuario);
        String contrasena = PasswordGenerator.generateRandomPassword(8);
        usuario.setEnabled(true);
        usuario.setContrasenaUsuario(passwordEncoder.encode(contrasena)); // Contraseña encriptada
        // usuario.setContrasenaUsuario(passwordEncoder.encode("admin123")); //SOLO PARA
        // PRUEBAS
        usuarioService.guardarUsuario(usuario);

        // Envío de correo con la nueva contraseña utilizando la plantilla HTML
        String encabezado = "Creación de credenciales";
        String descripcion = "Por medio de la presente le hacemos llegar sus credenciales para ingresar al Sistema Integral de Control y Progreso Educativo INSIBO (SICE-INSIBO), aconsejamos guardarla de manera privada para evitar contratiempos en el desarrollo de sus actividades.";
        envioCorreo.sendEmail(encabezado, descripcion, usuario.getCorreoUsuario(), contrasena);

        return "redirect:/gestionarSinCredenciales";
    }
    // -------------------------------------------------------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/buscarUsuario")
    public String buscarUsuario(@RequestParam("correoUsuario") String correoUsuario,
            RedirectAttributes redirectAttributes, Model model) {
        Usuario usuarioBuscado = usuarioService.buscarPorCorreo(correoUsuario);

        if (usuarioBuscado == null) {
            // Usuario no encontrado, añadir mensaje de error
            redirectAttributes.addFlashAttribute("Error",
                    "<b>¡Usuario no encontrado!</b> Verificar si ha escrito correctamente el correo.");
            return "redirect:/gestionarSinCredenciales"; // Redirigir a la página de gestión de credenciales
        }

        if (usuarioBuscado.isEnabled() != false || usuarioBuscado.isAccountLocked() != true) {
            // Usuario no encontrado, añadir mensaje de error
            redirectAttributes.addFlashAttribute("Error",
                    "<b>¡Advertencia!</b> Su usuario no se encuentra desactivado.");
            return "redirect:/gestionarSinCredenciales"; // Redirigir a la página de gestión de credenciales
        }

        Set<UsuarioRoles> rol = usuarioBuscado.getRolesUsuario();
        // Verificar si hay algún UsuarioRoles con role_name igual a "ADMINISTRADOR"
        boolean isAdmin = rol.stream()
                .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.ADMINISTRADOR));
        // Verificar si hay algún UsuarioRoles con role_name igual a "DOCENTE"
        boolean isDocente = rol.stream()
                .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.DOCENTE));
        // Verificar si hay algún UsuarioRoles con role_name igual a
        // "PERSONAL_ADMINISTRATIVO"
        boolean isPersonal = rol.stream()
                .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.PERSONAL_ADMINISTRATIVO));
        // Verificar si hay algún UsuarioRoles con role_name igual a "DIRECTOR"
        boolean isDirector = rol.stream()
                .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.DIRECTOR));
        // Verificar si hay algún UsuarioRoles con role_name igual a "SECRETARIA"
        boolean isSecretaria = rol.stream()
                .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.SECRETARIA));
        // Verificar si hay algún UsuarioRoles con role_name igual a "SUBDIRECTORA"
        boolean isSubDirectora = rol.stream()
                .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.SUBDIRECTORA));
        // Verificar si hay algún UsuarioRoles con role_name igual a "BIBLIOTECARIA"
        boolean isBibliotecaria = rol.stream()
                .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.BIBLIOTECARIO));
        String nombre = "";

        if (isAdmin) {
            nombre = "ADMINISTRADOR";
            if (usuarioBuscado.getDocente() != null) {
                nombre = usuarioBuscado.getDocente().getNombreDocente() + " "
                        + usuarioBuscado.getDocente().getApellidoDocente();
            }
            if (usuarioBuscado.getPersonalAdministrativo() != null) {
                nombre = usuarioBuscado.getPersonalAdministrativo().getNombrePersonal() + " "
                        + usuarioBuscado.getPersonalAdministrativo().getApellidoPersonal();
            }
        } else if (isDocente || isDirector || isSubDirectora) {
            nombre = "DOCENTE";
            if (usuarioBuscado.getDocente() != null) {
                nombre = usuarioBuscado.getDocente().getNombreDocente() + " "
                        + usuarioBuscado.getDocente().getApellidoDocente();
            }
            if (usuarioBuscado.getPersonalAdministrativo() != null) {
                nombre = usuarioBuscado.getPersonalAdministrativo().getNombrePersonal() + " "
                        + usuarioBuscado.getPersonalAdministrativo().getApellidoPersonal();
            }
        } else if (isPersonal || isSecretaria || isBibliotecaria) {
            nombre = "PERSONAL";
            if (usuarioBuscado.getDocente() != null) {
                nombre = usuarioBuscado.getDocente().getNombreDocente() + " "
                        + usuarioBuscado.getDocente().getApellidoDocente();
            }
            if (usuarioBuscado.getPersonalAdministrativo() != null) {
                nombre = usuarioBuscado.getPersonalAdministrativo().getNombrePersonal() + " "
                        + usuarioBuscado.getPersonalAdministrativo().getApellidoPersonal();
            }
        }

        // OCULTAMIENTO DE CONTRASEÑA
        Integer tamanyoContra = usuarioBuscado.getContrasenaUsuario().length();
        String codificacion = "";

        for (int i = 0; i < tamanyoContra; i++) {
            codificacion = codificacion + "*";
        }
        usuarioBuscado.setContrasenaUsuario(codificacion);

        UsuarioConNombre usuarioConNombre = new UsuarioConNombre(usuarioBuscado, nombre);

        model.addAttribute("Cantidad", 0);
        model.addAttribute("Usuarios", usuarioConNombre);
        return "Seguridad/gestionarSinCredenciales";
    }
    // -------------------------------------------------------------------------------------------------------------------------------

}
