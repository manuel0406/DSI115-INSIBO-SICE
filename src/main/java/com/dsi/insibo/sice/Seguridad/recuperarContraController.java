package com.dsi.insibo.sice.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad.EnvioCorreo;
import com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad.PasswordGenerator;
import com.dsi.insibo.sice.Seguridad.SeguridadService.UsuarioService;
import com.dsi.insibo.sice.entity.Usuario;

@Controller
public class recuperarContraController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EnvioCorreo envioCorreo;

    @GetMapping("/recuperarContra")
    public String verRecuperarContra(Model model, @ModelAttribute("mensaje") String mensaje){
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        model.addAttribute("mensaje", mensaje);
        return "Seguridad/recuperarContra";
    }

    @PostMapping("/correoDeRecuperacion")
	public String validarCorreo(@ModelAttribute Usuario user, RedirectAttributes redirectAttributes){
        String correo = user.getCorreoUsuario();
        // Buscamos en la base el correo y la contraseña
        Usuario usuario = usuarioService.buscarPorCorreo(correo);

        // Validamos la existencia del usuario
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensaje", "<b>¡Acceso inválido!</b> Sus credenciales no existen.");
            return "redirect:/recuperarContra";
        }

        // Validamos el estado del usuario
        if (usuario.isEnabled() != true || usuario.isAccountLocked() != true) {
            redirectAttributes.addFlashAttribute("mensaje", "<b>¡Sin Acceso!</b> Su usuario está bloqueado o desactivado.");
            return "redirect:/recuperarContra";
        } else {
            String nuevaContra = PasswordGenerator.generateRandomPassword(8); // Genero una nueva contraseña
            usuario.setContrasenaUsuario(passwordEncoder.encode(nuevaContra)); // Codifico y cambio la nueva contraseña
            usuario.setPrimerIngreso(true); // Restablezco primer inicio de sesión si es necesario
            usuarioService.guardarUsuario(usuario); // Guardo el usuario actualizado con la nueva contraseña
            
            // Envío de correo con la nueva contraseña utilizando la plantilla HTML
            String encabezado = "Restauración de contraseña";
            envioCorreo.sendEmail(encabezado, usuario.getCorreoUsuario(), nuevaContra);

            redirectAttributes.addFlashAttribute("Usuario", usuario);
            return "redirect:/login"; // Redirigir a la página de inicio de sesión
        }
    }
}
