package com.dsi.insibo.sice.Seguridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad.UsuarioConNombre;
import com.dsi.insibo.sice.Seguridad.SeguridadService.UsuarioService;
import com.dsi.insibo.sice.entity.Usuario;
import com.dsi.insibo.sice.entity.UsuarioRoleEnum;
import com.dsi.insibo.sice.entity.UsuarioRoles;

@Controller
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class gestionarCredencialesController {

    @Autowired
    private UsuarioService usuarioService;
// -----------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/gestionarCredenciales")
    public String cargarUsuarioCredenciales(Model model, @RequestParam(required = false, defaultValue = "1") int pagina) {
        
        //Cantidad de páginas
        pagina=(pagina-1);

        // Obtener a usuarios: ACTIVOS
        List<Usuario> listadoUsuarios = usuarioService.listaUsuariosActivosIntervalos(pagina);
        List<UsuarioConNombre> listadoCompleto =new ArrayList<>();

        //Obtenemos los nombres
        for (Usuario usuario : listadoUsuarios) {
            
            Set<UsuarioRoles> rol = usuario.getRolesUsuario();
            // Verificar si hay algún UsuarioRoles con role_name igual a "ADMINISTRADOR"
            boolean isAdmin = rol.stream()
                                 .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.ADMINISTRADOR));
            // Verificar si hay algún UsuarioRoles con role_name igual a "DOCENTE"
            boolean isDocente = rol.stream()
                                 .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.DOCENTE));
            // Verificar si hay algún UsuarioRoles con role_name igual a "PERSONAL_ADMINISTRATIVO"
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
            String nombre="";

            if(isAdmin){
                nombre = "ADMINISTRADOR";
                if (usuario.getDocente() != null) {
                    nombre = usuario.getDocente().getNombreDocente() + " " + usuario.getDocente().getApellidoDocente();
                }
                if (usuario.getPersonalAdministrativo() != null) {
                    nombre = usuario.getPersonalAdministrativo().getNombrePersonal()+ " " + usuario.getPersonalAdministrativo().getApellidoPersonal();
                }
            }
            if(isDocente || isDirector || isSubDirectora){
                nombre = "DOCENTE";
                if (usuario.getDocente() != null) {
                    nombre = usuario.getDocente().getNombreDocente() + " " + usuario.getDocente().getApellidoDocente();
                }
                if (usuario.getPersonalAdministrativo() != null) {
                    nombre = usuario.getPersonalAdministrativo().getNombrePersonal()+ " " + usuario.getPersonalAdministrativo().getApellidoPersonal();
                }
            }
            if(isPersonal || isSecretaria || isBibliotecaria){
                nombre = "PERSONAL";
                if (usuario.getDocente() != null) {
                    nombre = usuario.getDocente().getNombreDocente() + " " + usuario.getDocente().getApellidoDocente();
                }
                if (usuario.getPersonalAdministrativo() != null) {
                    nombre = usuario.getPersonalAdministrativo().getNombrePersonal()+ " " + usuario.getPersonalAdministrativo().getApellidoPersonal();
                }
            }

            //OCULTAMIENTO DE CONTRASEÑA
            Integer tamanyoContra = usuario.getContrasenaUsuario().length();
            String codificacion="";

            for(int i = 0; i < tamanyoContra ;i++){
                codificacion = codificacion + "*";
            }
            usuario.setContrasenaUsuario(codificacion);

            
            listadoCompleto.add(new UsuarioConNombre(usuario, nombre));
        }

        model.addAttribute("Usuarios", listadoCompleto);

        //Obtenemos la cantidad de usuarios que tenemos
        List<Usuario> totalUsuario = usuarioService.listaUsuariosActivos();
        int cantidad = (int) Math.ceil((double) totalUsuario.size() / 7);
        model.addAttribute("Cantidad", cantidad);


        return "Seguridad/gestionarCredenciales";
    }
// -----------------------------------------------------------------------------------------------------------------------------------

// -----------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/bloquearUsuario/{id}")
    public String bloquearUsuario(@PathVariable("id") int idUsuario, RedirectAttributes attribute ) {
        
        Usuario usuario = usuarioService.buscarPorIdUsuario(idUsuario);
        usuario.setAccountLocked(false);
        usuarioService.guardarUsuario(usuario);
        return "redirect:/gestionarCredenciales";
    }
// -----------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/buscarUsuarioCredencial")
    public String buscarUsuario(@RequestParam("correoUsuario") String correoUsuario, RedirectAttributes redirectAttributes, Model model) {
        Usuario usuarioBuscado = usuarioService.buscarPorCorreoActivo(correoUsuario);
        

        if (usuarioBuscado == null) {
            // Usuario no encontrado, añadir mensaje de error
            redirectAttributes.addFlashAttribute("Error", "<b>¡Usuario no encontrado!</b> Verificar si ha escrito correctamente el correo.");
            return "redirect:/gestionarCredenciales"; // Redirigir a la página de gestión de credenciales
        }

        if (usuarioBuscado.isAccountLocked() != true || usuarioBuscado.isEnabled() != true) {
            // Usuario no encontrado, añadir mensaje de error
            redirectAttributes.addFlashAttribute("Error", "<b>¡Advertencia!</b> Su usuario no se encuentra activo.");
            return "redirect:/gestionarCredenciales"; // Redirigir a la página de gestión de credenciales
        }
        
        Set<UsuarioRoles> rol = usuarioBuscado.getRolesUsuario();
        // Verificar si hay algún UsuarioRoles con role_name igual a "ADMINISTRADOR"
        boolean isAdmin = rol.stream()
                            .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.ADMINISTRADOR));
        // Verificar si hay algún UsuarioRoles con role_name igual a "DOCENTE"
        boolean isDocente = rol.stream()
                            .anyMatch(usuarioRoles -> usuarioRoles.getRoleEnum().equals(UsuarioRoleEnum.DOCENTE));
        // Verificar si hay algún UsuarioRoles con role_name igual a "PERSONAL_ADMINISTRATIVO"
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
        String nombre="";

        if(isAdmin){
            nombre = "ADMINISTRADOR";
            if (usuarioBuscado.getDocente() != null) {
                nombre = usuarioBuscado.getDocente().getNombreDocente() + " " + usuarioBuscado.getDocente().getApellidoDocente();
            }
            if (usuarioBuscado.getPersonalAdministrativo() != null) {
                nombre = usuarioBuscado.getPersonalAdministrativo().getNombrePersonal()+ " " + usuarioBuscado.getPersonalAdministrativo().getApellidoPersonal();
            }
        }
        if(isDocente || isDirector || isSubDirectora){
            nombre = "DOCENTE";
            if (usuarioBuscado.getDocente() != null) {
                nombre = usuarioBuscado.getDocente().getNombreDocente() + " " + usuarioBuscado.getDocente().getApellidoDocente();
            }
            if (usuarioBuscado.getPersonalAdministrativo() != null) {
                nombre = usuarioBuscado.getPersonalAdministrativo().getNombrePersonal()+ " " + usuarioBuscado.getPersonalAdministrativo().getApellidoPersonal();
            }
        }
        if(isPersonal || isSecretaria || isBibliotecaria){
            nombre = "PERSONAL";
            if (usuarioBuscado.getDocente() != null) {
                nombre = usuarioBuscado.getDocente().getNombreDocente() + " " + usuarioBuscado.getDocente().getApellidoDocente();
            }
            if (usuarioBuscado.getPersonalAdministrativo() != null) {
                nombre = usuarioBuscado.getPersonalAdministrativo().getNombrePersonal()+ " " + usuarioBuscado.getPersonalAdministrativo().getApellidoPersonal();
            }
        }

        //OCULTAMIENTO DE CONTRASEÑA
        Integer tamanyoContra = usuarioBuscado.getContrasenaUsuario().length();
        String codificacion="";

        for(int i = 0; i < tamanyoContra ;i++){
            codificacion = codificacion + "*";
        }
        usuarioBuscado.setContrasenaUsuario(codificacion);

        UsuarioConNombre usuarioConNombre = new UsuarioConNombre(usuarioBuscado, nombre);
   
        model.addAttribute("Cantidad", 0);
        model.addAttribute("Usuarios", usuarioConNombre);
        return "Seguridad/gestionarCredenciales";
    }
// -----------------------------------------------------------------------------------------------------------------------------------

}
