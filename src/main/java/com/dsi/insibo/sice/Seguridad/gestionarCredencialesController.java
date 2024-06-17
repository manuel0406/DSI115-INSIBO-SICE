package com.dsi.insibo.sice.Seguridad;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.entity.Usuario;

@Controller
public class gestionarCredencialesController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/gestionarCredenciales")
    public String cargarUsuarioCredenciales(Model model, @RequestParam(required = false, defaultValue = "1") int pagina) {
        
        pagina=(pagina-1);
        List<Usuario> listadoUsuarios = usuarioService.listaUsuariosActivosIntervalos(pagina);

        List<UsuarioConNombre> listadoCompleto =new ArrayList<>();
        //Obtenemos los nombres
        for (Usuario usuario : listadoUsuarios) {
            String rol = usuario.getRolUsuario();
            String nombre="";

            if(rol.equals("Administrador")){
                nombre = "Administrador";
            }
            if(rol.equals("Docente")){

                nombre = usuario.getDocente().getNombreDocente() + " " + usuario.getDocente().getApellidoDocente() ;
            }
            if(rol.equals("Personal Administrativo")){
                nombre = usuario.getPersonalAdministrativo().getNombrePersonal()+ " " + usuario.getDocente().getApellidoDocente();
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


        return "/seguridad/gestionarCredenciales";
    }


    @GetMapping("/bloquearUsuario/{id}")
    public String bloquearUsuario(@PathVariable("id") int idUsuario, RedirectAttributes attribute ) {
        
        Usuario usuario = usuarioService.buscarPorIdUsuario(idUsuario);
        usuario.setEstadoUsuario("Bloqueado");
        usuarioService.guardarUsuario(usuario);
        return "redirect:/gestionarCredenciales";
    }

    @GetMapping("/buscarUsuarioCredencial")
    public String buscarUsuario(@RequestParam("correoUsuario") String correoUsuario, RedirectAttributes redirectAttributes, Model model) {
        Usuario usuarioBuscado = usuarioService.buscarPorCorreo(correoUsuario);
        

        if (usuarioBuscado == null) {
            // Usuario no encontrado, añadir mensaje de error
            redirectAttributes.addFlashAttribute("Error", "<b>¡Usuario no encontrado!</b> Verificar si ha escrito correctamente el correo.");
            return "redirect:/gestionarCredenciales"; // Redirigir a la página de gestión de credenciales
        }

        if (!usuarioBuscado.getEstadoUsuario().equals("Activo")) {
            // Usuario no encontrado, añadir mensaje de error
            redirectAttributes.addFlashAttribute("Error", "<b>¡Advertencia!</b> Su usuario no se encuentra activo.");
            return "redirect:/gestionarCredenciales"; // Redirigir a la página de gestión de credenciales
        }
        
        String nombre="";
        if(usuarioBuscado.getRolUsuario().equals("Administrador")){
            nombre = "Administrador";
        }
        if(usuarioBuscado.getRolUsuario().equals("Docente")){

            nombre = usuarioBuscado.getDocente().getNombreDocente() + " " + usuarioBuscado.getDocente().getApellidoDocente() ;
        }
        else{
            nombre = usuarioBuscado.getPersonalAdministrativo().getNombrePersonal()+ " " + usuarioBuscado.getDocente().getApellidoDocente();
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
        return "/seguridad/gestionarCredenciales";
    }

}
