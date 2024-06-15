package com.dsi.insibo.sice.Expediente_docente.Administrativos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.dsi.insibo.sice.entity.PersonalAdministrativo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/expedienteadministrativo")
public class AdministrativoController {

    // Habilitando formulario de creación
    @GetMapping("/formulario")
    public String ficha(Model model) {
        PersonalAdministrativo administrativo = new PersonalAdministrativo();

        model.addAttribute("administrativo", administrativo);
        return "Expediente_docente/Administrativos/fichaAdministrativo";
    }

    // Creando
    @PostMapping("/guardar")
    public String guardar(@Validated @ModelAttribute PersonalAdministrativo administrativo, BindingResult result,
            Model model, RedirectAttributes attribute) {

        if (result.hasErrors()) {
            model.addAttribute("administrativo", administrativo);
            System.out.println("Se tienen errores en el formulario");
            return "expedienteadministrativo/formulario";
        }

        PersonalAdministrativo administrativoExistente = administrativoService
                .buscarPorIdAdministrativo(administrativo.getDuiPersonal());

        // Si el DUI existe
        if (administrativoExistente != null) {
            attribute.addFlashAttribute("error", "Error: Este DUI ya esta siendo utilizado.");
            return "redirect:plantaadministrativa";
        } else {
            // Si no
            administrativoService.guardarAdministrativo(administrativo);
            attribute.addFlashAttribute("success", "Expediente creado con exito!");
            return "redirect:plantaadministrativa";
        }
    }

    // Actualizando
    @PostMapping("/actualizar")
    public String actualizar(@Validated @ModelAttribute PersonalAdministrativo administrativo, BindingResult result,
            Model model, RedirectAttributes attribute) {

        administrativoService.guardarAdministrativo(administrativo);
        attribute.addFlashAttribute("success", "Expediente actualizado con exito!");
        return "redirect:plantaadministrativa";
    }



    // Otras consultas
    @Autowired
    private AdministrativoService administrativoService;

    // Listando toda la planta administrativa
    @GetMapping("/plantaadministrativa")
    public String listarAdministrativos(Model model) {
        List<AdministrativoDTO> listadoAdministrativos = administrativoService.listarAdministrativos();
        model.addAttribute("titulo", "Planta Administrativa");
        model.addAttribute("Administrativos", listadoAdministrativos);
        return "Expediente_docente/Administrativos/listarAdministrativos";
    }

    // Habilitando la edición
    @GetMapping("/editarexpediente/{id}")
    public String editarAdministrativo(@PathVariable("id") String idAdministrativo, Model model,
            RedirectAttributes attribute) {
        PersonalAdministrativo administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);
        //Por si ingresa un id inexistente desde URL
        if (administrativo == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        model.addAttribute("administrativo", administrativo);
        model.addAttribute("editar", true); //Deshabilita el campo DUI
        return "Expediente_docente/Administrativos/fichaAdministrativoEdit";
    }

    // Eliminando
    @GetMapping("/eliminarexpediente/{id}")
    public String eliminarAdministrativo(@PathVariable("id") String idAdministrativo, RedirectAttributes attribute) {
        PersonalAdministrativo administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);
        //Por si ingresa un id inexistente desde URL
        if (administrativo == null) {
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        administrativoService.eliminar(idAdministrativo);
        attribute.addFlashAttribute("warning", "El expediente se elimino");
        return "redirect:/expedienteadministrativo/plantaadministrativa";
    }
}
