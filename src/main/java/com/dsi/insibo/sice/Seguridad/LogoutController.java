package com.dsi.insibo.sice.Seguridad;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout-success")
public class LogoutController {

    @GetMapping
    public String logoutSuccess() {
        return "Seguridad/logout"; // Nombre de la vista de la página de logout (logout.html)
    }
}
