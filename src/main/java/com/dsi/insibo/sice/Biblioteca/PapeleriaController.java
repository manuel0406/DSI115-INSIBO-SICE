package com.dsi.insibo.sice.Biblioteca;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Biblioteca.Service.EntregaPapeleriaService;
import com.dsi.insibo.sice.Biblioteca.Service.InventarioPapeleriaService;
import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteService;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Docente;
import com.dsi.insibo.sice.entity.EntregaPapeleria;
import com.dsi.insibo.sice.entity.InventarioPapeleria;


@Controller
@RequestMapping("/Biblioteca/Papeleria")
@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
public class PapeleriaController {
    @Autowired
    private InventarioPapeleriaService inventarioPapeleriaService;
    @Autowired
    private EntregaPapeleriaService entregaPapeleriaService;
    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private DocenteService docenteService;

    // @GetMapping("/InventarioPapeleria")
    // public String inventarioPapeleria(Model model){
    //     List<InventarioPapeleria> listadoProductos = inventarioPapeleriaService.listarProductos();
    //     model.addAttribute("titulo","Inventario Papelería");
    //     model.addAttribute("productos",listadoProductos);
    //     model.addAttribute("nuevoProducto", new InventarioPapeleria());

    //     return "/Biblioteca/inventarioPapeleria.html";
    // }

    //INVENTARIO PAPELERIA

    @GetMapping("/InventarioPapeleria")
    public String inventarioPapeleria(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String searchTerm) {

        List<InventarioPapeleria> listadoProductos = inventarioPapeleriaService.listarProductos();
        
        // Filtrar productos si se proporciona un término de búsqueda
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            listadoProductos = listadoProductos.stream()
                    .filter(p -> p.getNombreArticulo().toLowerCase().contains(searchTerm.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int start = page * size;
        int end = Math.min((start + size), listadoProductos.size());

        if (start > listadoProductos.size()) {
            start = listadoProductos.size() - size;
            if (start < 0) {
                start = 0;
            }
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<InventarioPapeleria> pageProductos = new PageImpl<>(
                listadoProductos.subList(start, end),
                pageRequest, 
                listadoProductos.size()
        );

        model.addAttribute("titulo", "Inventario Papelería");
        model.addAttribute("productos", pageProductos.getContent());
        model.addAttribute("nuevoProducto", new InventarioPapeleria());
        model.addAttribute("page", pageProductos);
        model.addAttribute("searchTerm", searchTerm);

        return "/Biblioteca/inventarioPapeleria.html";
    }

    @PostMapping("/InventarioPapeleria")
    public String guardarProducto(Model model, @Validated @ModelAttribute("nuevoProducto") InventarioPapeleria nuevoProducto, BindingResult result, RedirectAttributes attributes) {
        List<InventarioPapeleria> listadoProductos = inventarioPapeleriaService.listarProductos();

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Inventario Papelería");
            model.addAttribute("productos", listadoProductos);
            return "redirect:/Biblioteca/Papeleria/InventarioPapeleria";
        }

        inventarioPapeleriaService.guardar(nuevoProducto);
        attributes.addFlashAttribute("success", "Artículo guardado con éxito");
        return "redirect:/Biblioteca/Papeleria/InventarioPapeleria";
    }


    @GetMapping("/InventarioPapeleria/delete/{idArticulo}")
    public String eliminarProducto(@PathVariable("idArticulo") int idArticulo, RedirectAttributes attribute) {
        try {
            // Intenta eliminar el artículo
            inventarioPapeleriaService.eliminar(idArticulo);
            // Si la eliminación es exitosa, agrega un mensaje de éxito
            attribute.addFlashAttribute("success", "El registro se ha eliminado exitosamente");
        } catch (DataIntegrityViolationException e) {
            // Si ocurre una excepción, agrega un mensaje de error
            attribute.addFlashAttribute("warning", "No se puede eliminar el registro porque ya hay registros guardados relacionados.");
        } catch (Exception e) {
            // Captura cualquier otra excepción
            attribute.addFlashAttribute("error", "Ocurrió un error al intentar eliminar el registro.");
        }
        return "redirect:/Biblioteca/Papeleria/InventarioPapeleria";
    }

    @GetMapping("/InventarioPapeleria/edit/{idArticulo}")
    @ResponseBody
    public InventarioPapeleria obtenerArticuloPorId(@PathVariable("idArticulo") int idArticulo) {
        return inventarioPapeleriaService.buscarPorId(idArticulo);
    }

    @PostMapping("/InventarioPapeleria/update")
    public String actualizarProducto(@Validated @ModelAttribute("producto") InventarioPapeleria producto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "redirect:/Biblioteca/Papeleria/InventarioPapeleria";
        }

        inventarioPapeleriaService.guardar(producto);
        attributes.addFlashAttribute("success", "Artículo actualizado con éxito");
        return "redirect:/Biblioteca/Papeleria/InventarioPapeleria";
    }

    

    // GESTIÓN DE ENTREGA 

    @GetMapping("/Control")
    public String ControlPapeleria(Model model,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "9") int size,
                @RequestParam(value = "query", required = false) String query) {
    
        // Obtener la lista completa de entregas
        List<EntregaPapeleria> listadoEntregas = entregaPapeleriaService.listarEntregas();
        
        // Filtrar las entregas si se proporciona un query
        if (query != null && !query.trim().isEmpty()) {
            listadoEntregas = listadoEntregas.stream()
                .filter(entrega -> entrega.getEntregaPersona().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        }
    
        // Calcular el rango para la página actual
        int start = page * size;
        int end = Math.min((start + size), listadoEntregas.size());
    
        if (start > listadoEntregas.size()) {
            start = listadoEntregas.size() - size;
            if (start < 0) {
                start = 0;
            }
        }
    
        // Crear una sublista para la página actual
        List<EntregaPapeleria> pagedEntregas = listadoEntregas.subList(start, end);
    
        // Crear el objeto Page para pasar a la vista
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EntregaPapeleria> pageEntregas = new PageImpl<>(
                pagedEntregas,
                pageRequest,
                listadoEntregas.size()
        );
    
        // Obtener la lista de productos sin paginación
        List<InventarioPapeleria> listadoProductos = inventarioPapeleriaService.listarProductos();
    
        model.addAttribute("titulo", "Entrega de Papelería");
        model.addAttribute("entregas", pageEntregas.getContent());
        model.addAttribute("productos", listadoProductos);
        model.addAttribute("nuevaEntrega", new EntregaPapeleria());
        model.addAttribute("page", pageEntregas);
    
        return "/Biblioteca/controlPapeleria.html";
    }

    @PostMapping("/Control/entrega")
    public String registrarEntrega(@ModelAttribute("nuevaEntrega") EntregaPapeleria nuevaEntrega, Model model, RedirectAttributes redirectAttributes) {
        // Obtener el producto seleccionado
        InventarioPapeleria producto = inventarioPapeleriaService.buscarPorId(nuevaEntrega.getInventarioPapeleria().getIdArticulo());

        // Restar la cantidad entregada del inventario
        int cantidadRestante = producto.getExistenciaArticulo() - nuevaEntrega.getEntregaCantidad();
        if (cantidadRestante < 0) {
            producto.setExistenciaArticulo(cantidadRestante);
            inventarioPapeleriaService.actualizarProducto(producto);
            entregaPapeleriaService.guardar(nuevaEntrega);
            // Manejo de advertencia: no hay suficiente inventario
            redirectAttributes.addFlashAttribute("warning", "Verifique la disponibilidad del articulo en el inventario.");
            return "redirect:/Biblioteca/Papeleria/Control";
        }

        // Actualizar la cantidad en el inventario
        producto.setExistenciaArticulo(cantidadRestante);
        inventarioPapeleriaService.actualizarProducto(producto);

        // Guardar la nueva entrega
        entregaPapeleriaService.guardar(nuevaEntrega);
        redirectAttributes.addFlashAttribute("success", "Entrega registrada correctamente.");
        // Redirigir a la página principal
        return "redirect:/Biblioteca/Papeleria/Control";
    }

    @PostMapping("/Control/editar")
    public String actualizarEntrega(@ModelAttribute("entrega") EntregaPapeleria entrega, Model model, RedirectAttributes redirectAttributes) {
        // Obtener la entrega original
        EntregaPapeleria entregaOriginal = entregaPapeleriaService.buscarPorId(entrega.getIdEntregaPapeleria());

        // Obtener el producto original
        InventarioPapeleria producto = inventarioPapeleriaService.buscarPorId(entregaOriginal.getInventarioPapeleria().getIdArticulo());

        // Ajustar el inventario
        int cantidadOriginal = entregaOriginal.getEntregaCantidad();
        int cantidadNueva = entrega.getEntregaCantidad();
        int diferencia = cantidadOriginal - cantidadNueva;

        // Actualizar el inventario en función de la diferencia
        producto.setExistenciaArticulo(producto.getExistenciaArticulo() + diferencia);
        inventarioPapeleriaService.actualizarProducto(producto);

        // Actualizar la entrega
        entregaOriginal.setEntregaCantidad(cantidadNueva);
        entregaPapeleriaService.guardar(entregaOriginal);

        redirectAttributes.addFlashAttribute("success", "Cantidad modificada correctamente.");
        return "redirect:/Biblioteca/Papeleria/Control";
    }


    @GetMapping(value = "/verPapeleriaPdf", produces = "application/pdf")
    public ModelAndView verPapeleriaPdf(Model model) {
        List<InventarioPapeleria> listadoProductos = inventarioPapeleriaService.listarProductos();
    
        // Crear la vista para el PDF
        ModelAndView modelAndView = new ModelAndView("Biblioteca/verPapeleriaPdf");
        modelAndView.addObject("productos", listadoProductos);
    
        return modelAndView;
    }

    @GetMapping(value = "/verEntregasPdf", produces = "application/pdf")
    public ModelAndView verEntregasPdf(@RequestParam(value = "query", required = false) String query) {
        List<EntregaPapeleria> listadoEntregas = entregaPapeleriaService.listarEntregas();
    
        // Filtrar las entregas si se proporciona una consulta de búsqueda
        if (query != null && !query.trim().isEmpty()) {
            listadoEntregas = listadoEntregas.stream()
                .filter(entrega -> entrega.getEntregaPersona().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        }
    
        // Crear la vista para el PDF con los resultados filtrados
        ModelAndView modelAndView = new ModelAndView("Biblioteca/verEntregasPdf");
        modelAndView.addObject("entregas", listadoEntregas);
    
        return modelAndView;
    }

    @GetMapping("/buscarPersonas")
    @ResponseBody
    public List<String> buscarPersonas(@RequestParam("term") String term) {
        List<String> resultados = new ArrayList<>();
        
        // Obtener alumnos y docentes que coincidan con el término de búsqueda
        List<Alumno> alumnos = alumnoService.buscarPorNombre(term);
        List<Docente> docentes = docenteService.buscarPorNombre(term);
    
        // Concatenar nombre y apellido para los alumnos
        alumnos.forEach(alumno -> resultados.add(alumno.getNombreAlumno() + " " + alumno.getApellidoAlumno()));
    
        // Concatenar nombre y apellido para los docentes
        docentes.forEach(docente -> resultados.add(docente.getNombreDocente() + " " + docente.getApellidoDocente()));
    
        return resultados;
    }
}
