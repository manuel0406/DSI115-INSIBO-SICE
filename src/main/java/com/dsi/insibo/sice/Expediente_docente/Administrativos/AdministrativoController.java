package com.dsi.insibo.sice.Expediente_docente.Administrativos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.dsi.insibo.sice.Expediente_docente.Administrativos.Anexos.AnexoAdministrativoService;
import com.dsi.insibo.sice.entity.AnexoPersonalAdministrativo;
import com.dsi.insibo.sice.entity.PersonalAdministrativo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/expedienteadministrativo")
public class AdministrativoController {
    @Autowired
    private AdministrativoService administrativoService;
    @Autowired
    private AnexoAdministrativoService anexoadministrativoService;

    // Habilitando formulario de creación
    @GetMapping("/formulario")
    public String ficha(Model model) {
        PersonalAdministrativo administrativo = new PersonalAdministrativo();

        model.addAttribute("administrativo", administrativo);
        return "Expediente_docente/Administrativos/fichaAdministrativo";
    }

    // Creando
    @PostMapping("/guardar")
    public String guardar(@Validated @ModelAttribute PersonalAdministrativo administrativo,
            @RequestParam("administrativoRol") String rolSeleccionado, BindingResult result,
            Model model, RedirectAttributes attribute) {

        if (result.hasErrors()) {
            model.addAttribute("administrativo", administrativo);
            System.out.println("Se tienen errores en el formulario");
            return "expedienteadministrativo/formulario";
        }

        PersonalAdministrativo administrativoExistente = administrativoService
                .buscarPorIdAdministrativo(administrativo.getDuiPersonal());

        // Verifica que el DUI no se haya usado
        if (administrativoExistente != null) {
            attribute.addFlashAttribute("error", "Error: Este DUI ya esta siendo utilizado.");
            return "redirect:plantaadministrativa";
        } else {
            // Imprimiendo el rol seleccionado
            System.out.println(rolSeleccionado);

            // Guardando el administrativo
            administrativoService.guardarAdministrativo(administrativo);
            attribute.addFlashAttribute("success", "Expediente creado con exito!");
            return "redirect:plantaadministrativa";
        }
    }

    // Actualizando
    @PostMapping("/actualizar")
    public String actualizar(@Validated @ModelAttribute PersonalAdministrativo administrativo,
            @RequestParam("administrativoRol") String rolSeleccionado, BindingResult result,
            Model model, RedirectAttributes attribute) {

        // Imprimiendo el rol seleccionado
        System.out.println(rolSeleccionado);
        administrativoService.guardarAdministrativo(administrativo);
        attribute.addFlashAttribute("success", "Expediente actualizado con exito!");
        return "redirect:plantaadministrativa";
    }

    // Otras consultas
    // @Autowired
    // private AdministrativoService administrativoService;

    // Listando toda la planta administrativa
    @GetMapping("/plantaadministrativa")
    public String listarAdministrativos(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // page indica el numero de pagina en el que se encontrara por defecto
        // Size el numero de registros por pagina

        // Hace la conversion de la estructura List a Page
        PageRequest pageRequest = PageRequest.of(page, size);
        List<AdministrativoDTO> listadoAdministrativos = administrativoService.listarAdministrativos();
        Page<AdministrativoDTO> pageAdministrativos = new PageImpl<>(listadoAdministrativos.subList(
                pageRequest.getPageNumber() * pageRequest.getPageSize(),
                Math.min((pageRequest.getPageNumber() + 1) * pageRequest.getPageSize(), listadoAdministrativos.size())),
                pageRequest, listadoAdministrativos.size());

        model.addAttribute("titulo", "Planta Administrativa");
        model.addAttribute("Administrativos", listadoAdministrativos);
        // Hace el envio de la estructura con paginación a la vista
        model.addAttribute("page", pageAdministrativos);

        return "Expediente_docente/Administrativos/listarAdministrativos";
    }

    // Habilitando la consulta
    @GetMapping("/consultarexpediente/{id}")
    public String consultarAdministrativo(@PathVariable("id") String idAdministrativo, Model model,
            RedirectAttributes attribute) {
        PersonalAdministrativo administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);
        // Por si ingresa un id inexistente desde URL
        if (administrativo == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        model.addAttribute("administrativo", administrativo);
        return "Expediente_docente/Administrativos/fichaAdministrativoConsult";
    }

    // Habilitando la edición
    @GetMapping("/editarexpediente/{id}")
    public String editarAdministrativo(@PathVariable("id") String idAdministrativo, Model model,
            RedirectAttributes attribute) {
        PersonalAdministrativo administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);
        // Por si ingresa un id inexistente desde URL
        if (administrativo == null) {
            System.out.println("El docente no existe");
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        model.addAttribute("administrativo", administrativo);
        model.addAttribute("editar", true); // Deshabilita el campo DUI
        return "Expediente_docente/Administrativos/fichaAdministrativoEdit";
    }

    // Eliminando
    @GetMapping("/eliminarexpediente/{id}")
    public String eliminarAdministrativo(@PathVariable("id") String idAdministrativo, RedirectAttributes attribute) {
        PersonalAdministrativo administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);
        // Por si ingresa un id inexistente desde URL
        if (administrativo == null) {
            attribute.addFlashAttribute("error", "El expediente no existe");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        // Primero elimina los anexos
        anexoadministrativoService.eliminarAnexoAdministrativo(idAdministrativo);
        administrativoService.eliminar(idAdministrativo);
        attribute.addFlashAttribute("warning", "El expediente se elimino");
        return "redirect:/expedienteadministrativo/plantaadministrativa";
    }

    // Gestionando anexos
    @GetMapping("Documentos/{id}")
    public String administrativoDocumentos(@PathVariable("id") String idAdministrativo, Model model,
            RedirectAttributes attributes) {

        PersonalAdministrativo administrativo = null;
        if (idAdministrativo != "") {
            // Busca el administrativo a traves del DUI
            administrativo = administrativoService.buscarPorIdAdministrativo(idAdministrativo);

            // Verifica que exista
            if (administrativo == null) {
                attributes.addFlashAttribute("error", "Error: El expediente no existe");
                return "redirect:/expedienteadministrativo/plantaadministrativa";
            }
        } else {
            // Maneja el caso donde el idAdministrativo no es válido
            System.out.println("Error: ¡El idAdministrativo ingresado no es válido!");
            attributes.addFlashAttribute("error", "Error: ¡El idAdministrativo ingresado no es válido!");
            return "redirect:/expedienteadministrativo/plantaadministrativa";
        }

        // Obtener los anexos asociados al administrativo
        AnexoPersonalAdministrativo anexos = anexoadministrativoService.buscarAdministrativo(idAdministrativo);

        // Agregar atributos al modelo para ser utilizados en la vista
        model.addAttribute("administrativo", administrativo);
        model.addAttribute("anexos", anexos);
        model.addAttribute("titulo", "Documentos");

        // return "Expediente_administrativo/administrativoDocumentos";
        return "Expediente_docente/Administrativos/AdministrativoDocumentos";
    }
}
