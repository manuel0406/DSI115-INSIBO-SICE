package com.dsi.insibo.sice.Administrativo.Materias.ControladoresMaterias;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Administrativo.Materias.Clases.DocenteAsignacionDTO;
import com.dsi.insibo.sice.Administrativo.Materias.ServiciosMaterias.AsignacionService;
import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Administrativo.Materias.ServiciosMaterias.MateriasService;
import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteService;
import com.dsi.insibo.sice.entity.Asignacion;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.Docente;
import com.dsi.insibo.sice.entity.Materia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@PreAuthorize("hasAnyRole('ADMINISTRADOR','SUBDIRECTORA', 'DIRECTOR')")
@Controller
public class AsignacionController {

    // Materias
    @Autowired
    private MateriasService materiasService;
    // Bachilleratos
    @Autowired
    private BachilleratoService bachilleratosService;
    // Profesores
    @Autowired
    private DocenteService docenteService;
    @Autowired
    private AsignacionService asignacionService;

    @GetMapping("/AsignacionMateria")
    public String asignacionMateriasGeneral(Model model,  @RequestParam(required = false, defaultValue = "1") int pagina){

        int tamanyo = 10; // Tamaño de la página
        pagina = pagina - 1; // Convertir a base 0 para PageRequest


        String titulo = "Asignaciones de Materias";                                         // Asignar titulo
        List<Asignacion> totalAsignacions = asignacionService.obtenerTodaAsignaciones(); // Obtener todas las asignaciones
        Page<Asignacion> listadoAsignaciones = asignacionService.obtenerTodaAsignaciones(pagina, tamanyo);
        List<Docente> docentes = docenteService.listarDocenteAsignacion();                  // Obtener maestros

        // Calcular la cantidad de páginas
        int cantidad = (int) Math.ceil((double) totalAsignacions.size() / tamanyo);

        // Construccion de infromacion a front-end
        model.addAttribute("asignaciones", listadoAsignaciones);
        model.addAttribute("cantidad", cantidad);
        model.addAttribute("docentes", docentes);
        model.addAttribute("titulo", titulo);
        return "Administrativo/GestionMaterias/AsignacionMateriaGeneral";
    }

    @GetMapping("/AsignacionMateria/{id}")
    public String asignacionMateria(Model model,  @PathVariable("id") int idMateria, 
                                    @RequestParam(required = false, defaultValue = "1") int pagina){

        int tamanyo = 10; // Tamaño de la página
        pagina = pagina - 1; // Convertir a base 0 para PageRequest
        
        // ID INVALIDA
        if (idMateria == 0) {
            return "redirect:/GestionMaterias";
        }
        
        // Para obtener el titulo
        Materia materia = materiasService.obtenerMateriaPorId(idMateria);                       // Informacion de la materia
        String titulo = "Asignaciones a: " + materia.getNomMateria();

        // Para obtener todas las asignaciones
        List<Asignacion> listadoTotal = asignacionService.listarAsignaciones(idMateria); // Listado de asignaciones de la materia
        Page<Asignacion> listadoAsignaciones = asignacionService.listarAsignaciones(idMateria, pagina, tamanyo);

        // Filtrado de maximo de docentes - obtener docentes sin repetir
        List<String> docentesMax = asignacionService.listarDocentesMaximo(idMateria);
        List<Docente> docentes = docenteService.listarDocenteAsignacion();
        docentes.removeIf(docente -> docentesMax.contains(docente.getDuiDocente())); // Eliminar de la lista de docentes aquellos cuyo duiDocente está en la lista de docentesMax
        

        // Calcular la cantidad de páginas
        int cantidad = (int) Math.ceil((double) listadoTotal.size() / tamanyo);

        model.addAttribute("titulo", titulo);
        model.addAttribute("asignaciones", listadoAsignaciones);
        model.addAttribute("cantidad", cantidad);
        model.addAttribute("idMateria", idMateria);
        model.addAttribute("docentes", docentes);
        return "Administrativo/GestionMaterias/AsignacionMateria";
    }

    @GetMapping("/NuevaAsignacion")
    public String nuevaAsignacionGeneral(Model model){

        // Obtenemos las materias
        List<Materia> materias = materiasService.obtenerMaterias();

        // Obtenemos los bachilleratos
        List<Bachillerato> primeros = bachilleratosService.obtenerPrimeros();
        List<Bachillerato> segundos = bachilleratosService.obtenerSegundos();
        List<Bachillerato> terceros = bachilleratosService.obtenerTerceros();

        // Obtenemos las asignaciones
        List<Asignacion> asignaciones = asignacionService.obtenerTodaAsignaciones();

        // Convertir la listas  a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String materiasJson = objectMapper.writeValueAsString(materias);
            String asignacionesJson = objectMapper.writeValueAsString(asignaciones);
            String primerosJson = objectMapper.writeValueAsString(primeros);
            String segundosJson = objectMapper.writeValueAsString(segundos);
            String tercerosJson = objectMapper.writeValueAsString(terceros);

            model.addAttribute("materiasJson", materiasJson);
            model.addAttribute("asignacionesJson", asignacionesJson);
            model.addAttribute("primerosJson", primerosJson);
            model.addAttribute("segundosJson", segundosJson);
            model.addAttribute("tercerosJson", tercerosJson);
        } catch (JsonProcessingException e) {
            System.out.println("El error es: " + e);
        }

        // Crear la vista
        model.addAttribute("materias", materias);


        return "Administrativo/GestionMaterias/NuevaAsignacionGeneral";
        
    }

    // PETICION AJAX A TIEMPO REAL
    @GetMapping("/DocentesMax")
    @ResponseBody
    public List<DocenteAsignacionDTO> getDocentesByMateria(@RequestParam int idMateria) {
        // Obtener la lista de docentes máximos
        List<String> docentesMax = asignacionService.listarDocentesMaximo(idMateria);
        
        // Obtener todos los docentes y filtrar los que están en la lista de docentesMax
        List<Docente> docentes = docenteService.listarDocenteAsignacion();
        
        // Filtrar los docentes cuyo duiDocente no está en la lista de docentesMax
        List<DocenteAsignacionDTO> docentesDTO = docentes.stream()
            .filter(docente -> !docentesMax.contains(docente.getDuiDocente()))
            .map(docente -> new DocenteAsignacionDTO(docente.getDuiDocente(), docente.getNombreDocente(), docente.getApellidoDocente()))
            .collect(Collectors.toList());
    
        return docentesDTO;
    }    

    @GetMapping("/NuevaAsignacion/{id}")
    public String nuevaAsignacion(Model model, @PathVariable("id") int idMateria, RedirectAttributes attribute) {
    
        // ID INVALIDA
        if (idMateria == 0) {
            return "redirect:/GestionMaterias";
        }
    
        // Obtener la materia correspondiente al ID
        Materia materia = materiasService.obtenerMateriaPorId(idMateria);
    
        // Filtrado de maximo de docentes
        List<String> docentesMax = asignacionService.listarDocentesMaximo(idMateria);
        List<Docente> docentes = docenteService.listarDocenteAsignacion();
        docentes.removeIf(docente -> docentesMax.contains(docente.getDuiDocente())); // Eliminar de la lista de docentes aquellos cuyo duiDocente está en la lista de docentesMax
    
        // Obtener las asignaciones para la materia específica
        List<Asignacion> asignaciones = asignacionService.obtenerAsignacionExistente(idMateria);
        Set<Integer> codigosBachilleratoAsignaciones = asignaciones.stream()
                    .map(asignacion -> asignacion.getBachillerato().getCodigoBachillerato())
                    .collect(Collectors.toSet());                                               // Codigo de las asignaciones existentes
    
        // Obtener listas de bachilleratos filtrados por los códigos no asignados
        List<Bachillerato> primeros = bachilleratosService.obtenerPrimeros().stream()
                .filter(bachillerato -> !codigosBachilleratoAsignaciones.contains(bachillerato.getCodigoBachillerato()))
                .collect(Collectors.toList());
    
        List<Bachillerato> segundos = bachilleratosService.obtenerSegundos().stream()
                .filter(bachillerato -> !codigosBachilleratoAsignaciones.contains(bachillerato.getCodigoBachillerato()))
                .collect(Collectors.toList());
    
        List<Bachillerato> terceros = bachilleratosService.obtenerTerceros().stream()
                .filter(bachillerato -> !codigosBachilleratoAsignaciones.contains(bachillerato.getCodigoBachillerato()))
                .collect(Collectors.toList());
    
        // Añadir los atributos necesarios al modelo para la vista
        model.addAttribute("primeros", primeros);
        model.addAttribute("segundos", segundos);
        model.addAttribute("terceros", terceros);
        model.addAttribute("materia", materia);
        model.addAttribute("docentes", docentes);
    
        // Retornar la vista correspondiente
        return "Administrativo/GestionMaterias/NuevaAsignacion";
    }
    
    @PostMapping("/crearAsignacion")
    public String crearAsignacionMateria(
            @RequestParam("idMateria") int idMateria,
            @RequestParam("duiDocente") String duiDocente,
            @RequestParam("codigoBachillerato") List<Integer> codigosBachillerato,
            RedirectAttributes redirectAttributes) {
        
        Docente docente = docenteService.buscarPorIdDocente(duiDocente);
        Materia materia = materiasService.obtenerMateriaPorId(idMateria);

        List<Asignacion> asignaciones = new ArrayList<>();
        for (int codigo : codigosBachillerato) {
            Bachillerato bachillerato = bachilleratosService.obtenerBachilleratoPorId(codigo);
            
            Asignacion asignacion = new Asignacion();
            asignacion.setMateria(materia);
            asignacion.setDocente(docente);
            asignacion.setBachillerato(bachillerato);
            asignaciones.add(asignacion);
        }

        redirectAttributes.addFlashAttribute("success", "La asignación de la materia \"" + materia.getNomMateria() + "\" se ha guardado con éxito.");

        asignacionService.saveAsignaciones(asignaciones);
        return "redirect:/AsignacionMateria"; // Redireccionar a la página deseada después de guardar
    }

    @PostMapping("/actualizarAsignacion")
    public String actualizarAsignacion(
        @RequestParam("idAsignacion") int idAsignacion,
        @RequestParam("materia") String nomMateria,  // Nombre actualizado para coincidir con el formulario
        @RequestParam("bachillerato") String codigoBachillerato, // Nombre actualizado para coincidir con el formulario
        @RequestParam("duiDocente") String duiDocente,
        @RequestParam("idMateria") int idMateria, // Asegúrate de recibir idMateria
        RedirectAttributes redirectAttributes) 
    {
        // Crear o encontrar la entidad Asignacion
        Asignacion asignacion = asignacionService.buscarAsignacion(idAsignacion);
        if (asignacion != null) {
            // Actualiza los detalles de la asignación
            Docente docenteNuevo = docenteService.buscarPorIdDocente(duiDocente);
            if (docenteNuevo != null) {
                asignacion.setDocente(docenteNuevo);
            } else {
                // Si no se encuentra el docente, redirige con un mensaje de error
                redirectAttributes.addFlashAttribute("error", "Docente no encontrado.");
                return "redirect:/AsignacionMateria";
            }
    
            // Guarda la asignación actualizada
            asignacionService.saveAsignacion(asignacion);
    
            // Redirige con un mensaje de éxito si lo deseas
            redirectAttributes.addFlashAttribute("success", "La asignación de la materia \"" + asignacion.getMateria().getNomMateria() + "\" se ha actualizado con éxito.");
        } else {
            // Si no se encuentra la asignación, redirige con un mensaje de error
            redirectAttributes.addFlashAttribute("error", "Asignación no encontrada.");
        }
    
        // Redirige a la página correcta usando idMateria
        return "redirect:/AsignacionMateria";
    }
    

@GetMapping("/eliminarAsignacion")
public String eliminarAsignacion(
    @RequestParam("idMateria") int idMateria,
    @RequestParam("idAsignacion") int idAsignacion,
    RedirectAttributes redirectAttributes) {

    try {
        // Buscar la asignación por idAsignacion
        Asignacion asignacion = asignacionService.buscarAsignacion(idAsignacion);

        if (asignacion != null) {
            // Verificar si la asignación corresponde a la materia solicitada (opcional, pero más seguro)
            if (asignacion.getMateria().getIdMateria() == idMateria) {
                // Eliminar la asignación
                asignacionService.eliminarAsignacion(asignacion);

                // Redirigir con mensaje de éxito
                redirectAttributes.addFlashAttribute("success", "La asignación de la materia \"" + asignacion.getMateria().getNomMateria() + "\" se ha eliminado con éxito.");
            } else {
                // Redirigir con mensaje de error si la asignación no corresponde a la materia
                redirectAttributes.addFlashAttribute("error", "La asignación no corresponde a la materia proporcionada.");
            }
        } else {
            // Redirigir con mensaje de error si la asignación no se encuentra
            redirectAttributes.addFlashAttribute("error", "Asignación no encontrada.");
        }
    } catch (Exception e) {
        // Manejo de excepciones generales
        redirectAttributes.addFlashAttribute("error", "Ocurrió un error al intentar eliminar la asignación. Tiene información relacionada");
    }

    // Redirigir a la página de asignación de materia, pasando el idMateria
    return "redirect:/AsignacionMateria?idMateria=" + idMateria;
}

    
       
    
}
