package com.dsi.insibo.sice.Seguridad;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.entity.Usuario;

@Controller
public class gestionarBloqueados {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/gestionarBloqueados")
    public String cargarBloqueados(Model model, @RequestParam(required = false, defaultValue = "1") int pagina) {
        pagina=(pagina-1);
        List<Usuario> listadoUsuarios = usuarioService.listaUsuariosBloqueadosIntervalos(pagina);

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

            listadoCompleto.add(new UsuarioConNombre(usuario, nombre));
        }

        model.addAttribute("Usuarios", listadoCompleto);

       // Obtenemos la cantidad de usuarios que tenemos
        List<Usuario> totalUsuario = usuarioService.listaUsuariosBloqueados();
        int cantidad = (int) Math.ceil((double) totalUsuario.size() / 7);
        model.addAttribute("Cantidad", cantidad);
    
        return "/seguridad/gestionarBloqueados";
    }


    @GetMapping("/desbloquearUsuario/{id}")
    public String desbloquearUsuario(@PathVariable("id") int idUsuario, RedirectAttributes attribute ) {
        
        Usuario usuario = usuarioService.buscarPorIdUsuario(idUsuario);
        usuario.setEstadoUsuario("Activo");
        usuarioService.guardarUsuario(usuario);
        return "redirect:/gestionarBloqueados";
    }
    
}