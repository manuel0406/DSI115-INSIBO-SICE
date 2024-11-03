package com.dsi.insibo.sice.Seguridad.SeguridadService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.Usuario;

@Service
public class SessionService {

    @Autowired
    private UsuarioService usuarioService;

    public String duiSession() {
        String dui = "No encontrado";
        String correoUsuario = "No encontrado";

        // Obtenemos la autenticación del usuario actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userObject = (User) authentication.getPrincipal();
            correoUsuario = userObject.getUsername();
        }

        // Buscamos el usuario por correo y obtenemos su información de DUI
        Usuario usuario = usuarioService.buscarPorCorreo(correoUsuario);
        if (usuario != null) {
            if (usuario.getDocente() != null) {
                dui = usuario.getDocente().getDuiDocente(); // Asignamos el DUI del docente
            } else if (usuario.getPersonalAdministrativo() != null) {
                dui = usuario.getPersonalAdministrativo().getDuiPersonal(); // Sino es personal
            } else {
                dui = "ADMINISTRADOR";
            }
        }

        return dui;
    }
}
