package com.dsi.insibo.sice.Seguridad;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model, @RequestParam(name = "error", required = false) String error, HttpServletRequest request) {
        if (error != null) {
            String errorMessage = (String) request.getSession().getAttribute("error");
            model.addAttribute("error", errorMessage);
            request.getSession().removeAttribute("error"); // Remove the error attribute from the session
        }
        return "Seguridad/iniciarSesion";
    }
}

