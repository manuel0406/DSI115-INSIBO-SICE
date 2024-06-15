package com.dsi.insibo.sice.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.entity.Usuario;
import com.dsi.insibo.sice.Seguridad.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/iniciarSesion")
    public String verIniciarSesion(Model model, @ModelAttribute("mensaje") String mensaje) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);
        return "/seguridad/iniciarSesion";
    }

    @PostMapping("/validarCorreo")
    public String validarUsuario(@ModelAttribute Usuario user, RedirectAttributes redirectAttributes) {
        // Obtenemos la informacion de los campos
        String correo = user.getCorreoUsuario();
        String contra = user.getContrasenaUsuario();

        // Buscamos en la base el correo y la contraseña
        Usuario usuario = usuarioService.buscarPorCorreoYContrasena(correo, contra);

        // Validamos la existencia del usuario
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensaje", "<b>¡Acceso inválido!</b> Sus crendenciales no existen.");
            return "redirect:/iniciarSesion";
        } 

        //Validamos el estado del usuario
        if (!usuario.getEstadoUsuario().equals("Activo")){
            redirectAttributes.addFlashAttribute("mensaje", "<b>¡Sin Acceso!</b> Su usuario esta " + usuario.getEstadoUsuario()+".");
            return "redirect:/iniciarSesion";
        } 
        else {
            if ("Administrador".equals(usuario.getRolUsuario())) {
                return "redirect:/gestionarCredenciales?pagina=1";
            } else {
                return "redirect:/recuperarContra";
            }
        }
    }
}
