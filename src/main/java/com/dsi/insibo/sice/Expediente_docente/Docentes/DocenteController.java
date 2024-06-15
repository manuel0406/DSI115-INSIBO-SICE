package com.dsi.insibo.sice.Expediente_docente.Docentes;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.dsi.insibo.sice.Seguridad.UsuarioService;
import com.dsi.insibo.sice.entity.Docente;
import com.dsi.insibo.sice.entity.Usuario;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/expedientedocente")
public class DocenteController {

    @Autowired
    private UsuarioService usuarioService;

    // Direccionadores estaticos
    @GetMapping("/anexosDocente")
    public String docentes() {
        return "Expediente_docente/Docentes/anexosDocente";
    }

    @GetMapping("/barra")
    public String barra() {
        return "Expediente_docente/barra";
    }

    // Direccionadores de acción
    // Ficha general de expediente docente /expedientedocente/formulario
    @GetMapping("/formulario")
    public String abrirformulario(Model model) {
        Docente profesor = new Docente();

        model.addAttribute("profesor", profesor);
        return "Expediente_docente/Docentes/fichaDocente";
    }

    // guardando formulario
    @PostMapping("/guardar")
    public String guardar(@Validated @ModelAttribute Docente docente, BindingResult result, Model model,
            RedirectAttributes attribute) {

        //Inicia cambio
        Docente docenteExistente = docenteService.buscarPorIdDocente(docente.getDuiDocente());

        // Verifica si el DUI ya esta asociado a un registro
        if (docenteExistente != null) {
            //Si lo esta
            attribute.addFlashAttribute("error", "Error: El DUI ya está registrado.");
            return "redirect:plantadocente";
        }// Fin de la modificacion
        //Codigo de usuario
        else {

            Usuario usuario = new Usuario();
            // Obtenemos informacion relevante del usuario
            String correo = docente.getCorreoDocente();
            String rol = "Docente";
            String estado = "Desactivado";
            boolean inicio = true;
            String contrasena = "";

            // Asignaciones al nuevo usuario
            usuario.setDocente(docente);
            usuario.setCorreoUsuario(correo);
            usuario.setRolUsuario(rol);
            usuario.setEstadoUsuario(estado);
            usuario.setPrimerIngreso(inicio);
            usuario.setContrasenaUsuario(contrasena);

            if (result.hasErrors()) {
                model.addAttribute("profesor", docente);
                System.out.println("Se tienen errores en el formulario");
                return "expedientedocente/formulario";
            }

            // Fin del codigo para usuario
            // guardando el registro
            docenteService.guardarDocente(docente);
            usuarioService.guardarUsuario(usuario);
            attribute.addFlashAttribute("success", "Expediente creado con exito!");

            return "redirect:plantadocente";
        }
    }

    // editando formulario
    @PostMapping("/actualizar")
    public String actualizar(@Validated @ModelAttribute Docente docente, BindingResult result, Model model,
            RedirectAttributes attribute) {
        // Verificamos existencia del usuario
        Usuario usuario = usuarioService.buscarPorIdDocente(docente.getDuiDocente());
        usuario.setCorreoUsuario(docente.getCorreoDocente());
        usuarioService.guardarUsuario(usuario);
        docenteService.guardarDocente(docente);
        attribute.addFlashAttribute("success", "Expediente actualizado con éxito!");
        return "redirect:plantadocente";
    }

    // Lista docentes usando la DB /expedientedocente/plantadocente
    @Autowired
    private DocenteService docenteService;

    @GetMapping("/plantadocente")
    public String listarDocentes(Model model) {
        List<DocenteDTO> listadoDocentes = docenteService.listarDocentes();
        model.addAttribute("titulo", "Planta Docente");
        model.addAttribute("Docentes", listadoDocentes);
        return "Expediente_docente/Docentes/listarDocentes"; // Vista HTML
    }

    // Editando docente
    @GetMapping("/editarexpediente/{id}")
    public String editarDocente(@PathVariable("id") String idDocente, Model model, RedirectAttributes attribute) {

        Docente profesor = docenteService.buscarPorIdDocente(idDocente);
        if (profesor == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedientedocente/plantadocente";
        }

        model.addAttribute("profesor", profesor);
        model.addAttribute("editar", true); // Indica que se está editando un docente
        return "Expediente_docente/Docentes/fichaDocenteEdit";
    }

    @GetMapping("/editarmiexpediente/{id}")
    public String editarComoDocente(@PathVariable("id") String idDocente, Model model, RedirectAttributes attribute) {
        Docente profesor = docenteService.buscarPorIdDocente(idDocente);
        if (profesor == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedientedocente/plantadocente";
        }

        model.addAttribute("profesor", profesor);
        return "Expediente_docente/Docentes/fichaDocenteLimitada";
    }

    // Eliminar ficha
    @GetMapping("/eliminarexpediente/{id}")
    public String eliminarDocente(@PathVariable("id") String idDocente, RedirectAttributes attribute) {
        usuarioService.eliminarUsuarioPorDocenteId(idDocente);
        Docente profesor = docenteService.buscarPorIdDocente(idDocente);

        if (profesor == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedientedocente/plantadocente";
        }

        docenteService.eliminar(idDocente);
        attribute.addFlashAttribute("warning", "El expediente se elimino");

        return "redirect:/expedientedocente/plantadocente";
    }

    @Controller
    public class OtroController {
        @RequestMapping("/inicioexpedientes")
        public String inicio() {
            return "Expediente_docente/inicioExpedientes";
        }
    }
}
