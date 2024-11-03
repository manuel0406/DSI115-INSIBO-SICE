package com.dsi.insibo.sice.Seguridad.SeguridadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.Usuario;

import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private UsuarioService usuarioService;

    public String duiSession() {
        String sessionId = "No encontrado";
        String dui = "No encontrado";
        String correoUsuario = "No encontrado";
        List<Object> sessions = sessionRegistry.getAllPrincipals();

        for (Object session : sessions) {
            if (session instanceof User) {
                User userObject = (User) session;
                correoUsuario = userObject.getUsername();
            }
            // Usuario a recuperar y no incluimos las sesiones expiradas
            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);
            for (SessionInformation sessionInformation : sessionInformations) {
                sessionId = sessionInformation.getSessionId();
            }
        }

        Usuario usuario = usuarioService.buscarPorCorreo(correoUsuario);
        if (usuario != null) {
            if (usuario.getDocente() != null) {
                dui = usuario.getDocente().getDuiDocente(); // Asignamos por defecto el dui del docente
            } else if (usuario.getPersonalAdministrativo() != null) {
                dui = usuario.getPersonalAdministrativo().getDuiPersonal(); // Sino es personal
            } else {
                dui = "ADMINISTRADOR";
            }
        }

        return dui;
    }
}