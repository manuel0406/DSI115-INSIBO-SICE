package com.dsi.insibo.sice.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.entity.Usuario;

@Controller
public class recuperarContraController {
    @GetMapping("/recuperarContra")
	public String verRecuperarContra(Model model, @ModelAttribute("mensaje") String mensaje){
		
		 Usuario usuario= new Usuario();
        model.addAttribute("usuario", usuario);
		model.addAttribute("mensaje", mensaje);
		return "Seguridad/recuperarContra";
	}

	@Autowired
    private UsuarioService usuarioService;
    @PostMapping("/correoDeRecuperacion")
	public String validarCorreo(@ModelAttribute Usuario user, RedirectAttributes redirectAttributes) {

        // Obtenemos la informacion de los campos
        String correo =  user.getCorreoUsuario();

		//Buscamos en la base el correo y la contraseña
		Usuario usuario = usuarioService.buscarPorCorreo(correo);

		// Validamos la existencia del usuario
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensaje", "<b>¡Acceso inválido!</b> Sus crendenciales no existen.");
            return "redirect:/recuperarContra";
        } 

		//Validamos el estado del usuario
        if (!usuario.getEstadoUsuario().equals("Activo")){
            redirectAttributes.addFlashAttribute("mensaje", "<b>¡Sin Acceso!</b> Su usuario esta " + usuario.getEstadoUsuario()+".");
            return "redirect:/recuperarContra";
        } 
        else {
			redirectAttributes.addFlashAttribute("Usuario", usuario);
			return "redirect:/enviarCorreo";
        }
	}

	@GetMapping("/enviarCorreo")
	public String enviarCorreo(@ModelAttribute("Usuario") Usuario usuario){
		
		System.out.println(usuario.getContrasenaUsuario());
		


		return "redirect:/iniciarSesion";
	}	

}
