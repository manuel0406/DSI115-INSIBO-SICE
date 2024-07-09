package com.dsi.insibo.sice.Seguridad.Configuraciones;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "<b>¡Incorrecto!</b> Correo o contraseña incorrectos";

        if (exception.getClass().isAssignableFrom(LockedException.class)) {
            errorMessage = "<b>¡Cuenta Bloqueada!</b> Por favor, contacte al administrador.";
        } else if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            errorMessage = "<b>¡Sin acceso!</b> Correo o contraseña incorrectos";
        }
        request.getSession().setAttribute("error", errorMessage);
        getRedirectStrategy().sendRedirect(request, response, "/login?error");
    }
}
