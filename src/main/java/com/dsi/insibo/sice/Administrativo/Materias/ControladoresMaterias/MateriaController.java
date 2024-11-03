package com.dsi.insibo.sice.Administrativo.Materias.ControladoresMaterias;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Administrativo.Materias.ServiciosMaterias.MateriasService;
import com.dsi.insibo.sice.entity.Materia;
import org.springframework.data.domain.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR','SUBDIRECTORA','SECRETARIA')")
@Controller
public class MateriaController {
    
    @Autowired
    private MateriasService materiasService;

    @GetMapping("/GestionMaterias")
    public String gestionarMaterias(
            @RequestParam(value = "tipo", defaultValue = "") String tipo,
            @RequestParam(required = false, defaultValue = "1") int pagina,
            Model model) {

        int tamanyo = 10; // Tamaño de la página
        pagina = pagina - 1; // Convertir a base 0 para PageRequest

        String titulo;
        String seleccion = "";
        Page<Materia> materias;
        List<Materia> materiasTotal;

        if (!tipo.isEmpty()) {
            // Filtrar según el tipo
            materias = materiasService.obtenerMateriasPorTipoConPaginado(tipo, pagina, tamanyo);
            materiasTotal = materiasService.obtenerMateriaPorTipo(tipo);
            seleccion = tipo;
            titulo = "Materias en INSIBO: " + tipo;
        } else {
            // Obtener todas las materias
            materias = materiasService.obtenerMateriasConPaginado(pagina, tamanyo);
            materiasTotal = materiasService.obtenerMaterias();
            titulo = "Materias impartidas en INSIBO";
        }

        // Convertir la lista de materias a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String materiasJson = objectMapper.writeValueAsString(materiasTotal);
            model.addAttribute("materiasJson", materiasJson); // Para JS
        } catch (JsonProcessingException e) {
            System.out.println("El error es: " + e);
        }

        // Calcular la cantidad de páginas
        int cantidad = (int) Math.ceil((double) materiasTotal.size() / tamanyo);
        model.addAttribute("cantidad", cantidad);

        // Añadir atributos al modelo
        model.addAttribute("materias", materias);
        model.addAttribute("titulo", titulo);
        model.addAttribute("seleccion", seleccion);
        return "Administrativo/GestionMaterias/GestionarMaterias";
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/guardarMateria")
    public String guardarMateria(@RequestParam("nomMateria") String nombre, 
                                 @RequestParam("codMateria") String codigo,
                                 @RequestParam("tipoMateria") String tipo,
                                 RedirectAttributes redirectAttributes) {
        try {
            Materia materia = new Materia();
            materia.setCodMateria(codigo);
            materia.setNomMateria(nombre);
            materia.setTipoMateria(tipo);
            materia.setActivoMateria(true);
            materiasService.guardarMateria(materia);
            redirectAttributes.addFlashAttribute("success", "La materia \"" + nombre + "\" se ha guardado con éxito.");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "La materia \"" + nombre + "\" ya existe, ingresar otro nombre o código.");
        }            
        return "redirect:/GestionMaterias";
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/actualizarMateria")
    public String actualizarMateria(@RequestParam("idMateria") int id,
                                    @RequestParam("codMateria") String codigo,
                                    @RequestParam("nomMateria") String nombre,
                                    @RequestParam("tipoMateria") String tipo,
                                    RedirectAttributes redirectAttributes) {
        Materia materia = materiasService.obtenerMateriaPorId(id);
        if (materia == null) {
            redirectAttributes.addFlashAttribute("error", "La materia no existe.");
            return "redirect:/GestionMaterias";
        }
        
        try {
            materia.setCodMateria(codigo);
            materia.setNomMateria(nombre);
            materia.setTipoMateria(tipo);
            materiasService.guardarMateria(materia);
            redirectAttributes.addFlashAttribute("success", "La materia \"" + materia.getNomMateria() + "\" se ha actualizado con éxito.");  
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "La materia \"" + nombre + "\" ya existe, ingresar otro nombre o código.");
        } 
        return "redirect:/GestionMaterias";
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/EliminarMateria/{id}")
    public String eliminarMateria(@PathVariable("id") int idMateria, RedirectAttributes redirectAttributes){
        Materia materia = materiasService.obtenerMateriaPorId(idMateria);
        if (materia == null) {
            redirectAttributes.addFlashAttribute("error", "La materia no existe.");
            return "redirect:/GestionMaterias";
        }

        try {
            materiasService.eliminarMateria(materia); // Eliminado si no tiene datos ligados.
        } catch (Exception e) {
            materia.setActivoMateria(false); // Eliminado con desactivados dado que tiene datos ligados.
            materiasService.guardarMateria(materia); 
        }

        redirectAttributes.addFlashAttribute("success", "La materia \"" + materia.getNomMateria() + "\" se ha eliminado con éxito.");
        return "redirect:/GestionMaterias";
    }
}
