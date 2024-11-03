package com.dsi.insibo.sice.Biblioteca;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Biblioteca.Service.InventarioLibroService;
import com.dsi.insibo.sice.Biblioteca.Service.PrestamoLibroService;
import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.InventarioLibro;
import com.dsi.insibo.sice.entity.PrestamoLibro;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/Biblioteca")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SUBDIRECTORA', 'DIRECTOR', 'BIBLIOTECARIO')")
public class BibliotecaController {

    @GetMapping("/")
    public String inicioBiblioteca(Model model){
        model.addAttribute("titulo","Gestión de Biblioteca");

        return "Biblioteca/inicioBiblioteca.html";
    }

    @Autowired
    private InventarioLibroService inventarioLibroService;
    @Autowired
    private PrestamoLibroService prestamoLibroService;
    @Autowired
    private AlumnoService alumnoService;
    
    @Autowired
    BachilleratoService bachilleratoService;

    // @GetMapping("/InventarioLibros")
    // public String inventarioLibros(Model model){
    //     List<InventarioLibro> listadoLibros =inventarioLibroService.listarLibros();
    //     model.addAttribute("titulo","Inventario de Libros");
    //     model.addAttribute("libros", listadoLibros);
    //     model.addAttribute("nuevoLibro", new InventarioLibro());

    //     return "/Biblioteca/inventarioLibros.html";
    // }

    @GetMapping("/InventarioLibros")
    public String inventarioLibros(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {

        List<InventarioLibro> listadoLibros = inventarioLibroService.listarLibros();

        // Filtrar productos si se proporciona un término de búsqueda
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            listadoLibros = listadoLibros.stream()
                    .filter(p -> p.getTituloLibro().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    p.getAutorLibro().toLowerCase().contains(searchTerm.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        int start = page * size;
        int end = Math.min((start + size), listadoLibros.size());

        if (start > listadoLibros.size()) {
            start = listadoLibros.size() - size;
            if (start < 0) {
                start = 0;
            }
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<InventarioLibro> pageLibros = new PageImpl<>(
                listadoLibros.subList(start, end),
                pageRequest, 
                listadoLibros.size()
        );

        model.addAttribute("titulo", "Inventario de Libros");
        model.addAttribute("libros", pageLibros.getContent());
        model.addAttribute("nuevoLibro", new InventarioLibro());
        model.addAttribute("page", pageLibros);

        return "Biblioteca/inventarioLibros.html";
    }

    @PostMapping("/InventarioLibros")
    public String guardarLibro(Model model, @Validated @ModelAttribute("nuevoLibro") InventarioLibro nuevoLibro, BindingResult result, RedirectAttributes attributes) {
        List<InventarioLibro> listadoLibros = inventarioLibroService.listarLibros();
    
        // Validación personalizada: Verificar si existe un libro con el mismo título y autor
        if (inventarioLibroService.existePorTituloYAutor(nuevoLibro.getTituloLibro(), nuevoLibro.getAutorLibro())) {
            attributes.addFlashAttribute("warning", "Ya existe un libro con el mismo título y autor.");
            return "redirect:/Biblioteca/InventarioLibros";  // Redirigir y mostrar el mensaje de error
        }
    
        // Si hay otros errores de validación
        if (result.hasErrors()) {
            attributes.addFlashAttribute("warning", "Hubo un problema con los datos ingresados.");
            model.addAttribute("titulo", "Inventario de Libros");
            model.addAttribute("libros", listadoLibros);
            return "redirect:/Biblioteca/InventarioLibros";  // Redirigir y mostrar el mensaje de error
        }
    
        // Si no hay errores, guardar el libro
        inventarioLibroService.guardar(nuevoLibro);
        attributes.addFlashAttribute("success", "Libro agregado con éxito");
        model.addAttribute("titulo", "Inventario de Libros");
        model.addAttribute("libros", listadoLibros);
        return "redirect:/Biblioteca/InventarioLibros";  // Redirigir y mostrar el mensaje de éxito
    }
    
    @GetMapping("/InventarioLibros/delete/{idInventarioLibros}")
    public String eliminarLibro(@PathVariable("idInventarioLibros") int idInventarioLibros, RedirectAttributes attribute) {
        try {
            // Intenta eliminar el libro
            inventarioLibroService.eliminar(idInventarioLibros);
            // Si la eliminación es exitosa, agrega un mensaje de éxito
            attribute.addFlashAttribute("success", "El registro se ha eliminado exitosamente");
        } catch (DataIntegrityViolationException e) {
            // Si ocurre una excepción, agrega un mensaje de error
            attribute.addFlashAttribute("warning", "No se puede eliminar el registro porque ya hay registros guardados relacionados.");
        } catch (Exception e) {
            // Captura cualquier otra excepción
            attribute.addFlashAttribute("error", "Ocurrió un error al intentar eliminar el registro.");
        }
        return "redirect:/Biblioteca/InventarioLibros";
    }

    @GetMapping("/InventarioLibros/edit/{idInventarioLibros}")
    @ResponseBody
    public InventarioLibro obtenerLibroPorId(@PathVariable("idInventarioLibros") int idInventarioLibros) {
        return inventarioLibroService.buscarPorId(idInventarioLibros);
    }

    @PostMapping("/InventarioLibros/update")
    public String actualizarLibro(@Validated @ModelAttribute("libro") InventarioLibro libro, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "redirect:/Biblioteca/InventarioLibros";
        }

        inventarioLibroService.guardar(libro);
        attributes.addFlashAttribute("success", "Libro actualizado con éxito");
        return "redirect:/Biblioteca/InventarioLibros";
    }

    //PRESTAMOS
    @GetMapping("/Prestamos")
    public String PrestamosBiblioteca(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(value = "nombreAlumno", required = false) String nombreAlumno) {
    
        // Obtener todos los préstamos con estado "Pendiente"
        List<PrestamoLibro> listadoPrestamos = prestamoLibroService.listarPrestamos().stream()
            .filter(prestamo -> "Pendiente".equals(prestamo.getEstadoPrestamo()))
            .collect(Collectors.toList());

        List<Bachillerato> listaBachilleratos = bachilleratoService.listaBachilleratos();
    
        // Filtrar por nombre del alumno si se proporciona un término de búsqueda
        if (nombreAlumno != null && !nombreAlumno.trim().isEmpty()) {
            listadoPrestamos = listadoPrestamos.stream()
                .filter(prestamo -> prestamo.getAlumno().getNombreAlumno().toLowerCase().contains(nombreAlumno.toLowerCase()))
                .collect(Collectors.toList());
        }
    
        // Calcular el rango para la página actual
        int start = page * size;
        int end = Math.min((start + size), listadoPrestamos.size());
    
        if (start > listadoPrestamos.size()) {
            start = listadoPrestamos.size() - size;
            if (start < 0) {
                start = 0;
            }
        }
    
        // Crear una sublista para la página actual
        List<PrestamoLibro> pagedPrestamos = listadoPrestamos.subList(start, end);
    
        // Crear el objeto Page para pasar a la vista
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PrestamoLibro> pagePrestamos = new PageImpl<>(
                pagedPrestamos,
                pageRequest,
                listadoPrestamos.size()
        );
    
        // Cargar otros datos necesarios
        List<InventarioLibro> listadoLibros = inventarioLibroService.listarLibros();
        List<Alumno> listadoAlumnos = alumnoService.listarAlumnos(null, null, null, null);
    
        // Añadir los atributos al modelo
        model.addAttribute("titulo", "Gestión de Préstamos");
        model.addAttribute("prestamos", pagePrestamos.getContent());
        model.addAttribute("libros", listadoLibros);
        model.addAttribute("alumnos", listadoAlumnos);
        model.addAttribute("bachilleratos", listaBachilleratos);
        model.addAttribute("nuevoPrestamo", new PrestamoLibro());
        model.addAttribute("page", pagePrestamos);
    
        return "Biblioteca/prestamos.html";
    }

    @PostMapping("/Prestamos/registrar")
    public String registrarPrestamo(@ModelAttribute("nuevoPrestamo") PrestamoLibro nuevoPrestamo, Model model, RedirectAttributes attribute) {
        // Obtener el libro seleccionado
        InventarioLibro libro = inventarioLibroService.buscarPorId(nuevoPrestamo.getInventarioLibro().getIdInventarioLibros());

        // Verificar si hay suficiente cantidad en el inventario
        if (libro.getExistenciaLibro() < nuevoPrestamo.getCantidadPrestamo()) {
            attribute.addFlashAttribute("error", "Titulo de libro no disponible, verificar inventario");
            return "redirect:/Biblioteca/Prestamos";
        }

        // Restar la cantidad del inventario
        libro.setExistenciaLibro(libro.getExistenciaLibro() - nuevoPrestamo.getCantidadPrestamo());
        inventarioLibroService.guardar(libro);

        // Guardar el nuevo préstamo
        prestamoLibroService.guardar(nuevoPrestamo);

        // Redirigir a la página principal con un mensaje de éxito
        attribute.addFlashAttribute("success", "Préstamo registrado correctamente.");
        return "redirect:/Biblioteca/Prestamos";
    }

    @PostMapping("/Prestamos/devolver/{id}")
    public String devolverPrestamo(@PathVariable("id") int idPrestamoLibro, 
                                @RequestParam("cantidadPrestamo") int cantidadPrestamo, 
                                @RequestParam("idInventario") int idInventarioLibros, 
                                RedirectAttributes attributes) {
        // Buscar el préstamo por su ID
        PrestamoLibro prestamo = prestamoLibroService.buscarPorId(idPrestamoLibro);

        if (prestamo != null && "Pendiente".equals(prestamo.getEstadoPrestamo())) {
            // Actualizar el estado del préstamo a "Devuelto"
            prestamo.setEstadoPrestamo("Devuelto");

            // Buscar el libro en el inventario por su ID
            InventarioLibro libro = inventarioLibroService.buscarPorId(idInventarioLibros);
            if (libro != null) {
                // Sumar la cantidad prestada al inventario
                libro.setExistenciaLibro(libro.getExistenciaLibro() + cantidadPrestamo);
                inventarioLibroService.guardar(libro);

                // Guardar el préstamo actualizado
                prestamoLibroService.guardar(prestamo);

                // Añadir mensaje de éxito
                attributes.addFlashAttribute("success", "Préstamo marcado como Devuelto y el inventario actualizado correctamente.");
            } else {
                // Si no se encuentra el libro
                attributes.addFlashAttribute("error", "Libro no encontrado en el inventario.");
            }
        } else {
            attributes.addFlashAttribute("error", "El préstamo no existe o ya está marcado como Devuelto.");
        }

        // Redirigir a la página principal
        return "redirect:/Biblioteca/Prestamos";
    }

    @GetMapping("/Prestamos/Devueltos")
    public String PrestamosDevueltos(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(value = "nombreAlumno", required = false) String nombreAlumno) {
    
        // Obtener todos los préstamos con estado "Devuelto"
        List<PrestamoLibro> listadoPrestamos = prestamoLibroService.listarPrestamos().stream()
            .filter(prestamo -> "Devuelto".equals(prestamo.getEstadoPrestamo()))
            .collect(Collectors.toList());
    
    // Filtrar por nombre del alumno o NIE si se proporciona un término de búsqueda
    if (nombreAlumno != null && !nombreAlumno.trim().isEmpty()) {
        try {
            // Intentar convertir el término de búsqueda a Integer para comparar con el NIE
            int nie = Integer.parseInt(nombreAlumno);
            listadoPrestamos = listadoPrestamos.stream()
                .filter(prestamo -> prestamo.getAlumno().getNie() == nie)
                .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            // Si no es un número, buscar por el nombre del alumno
            listadoPrestamos = listadoPrestamos.stream()
                .filter(prestamo -> prestamo.getAlumno().getNombreAlumno().toLowerCase().contains(nombreAlumno.toLowerCase()))
                .collect(Collectors.toList());
        }
    }
    
        // Calcular el rango para la página actual
        int start = page * size;
        int end = Math.min((start + size), listadoPrestamos.size());
    
        if (start > listadoPrestamos.size()) {
            start = listadoPrestamos.size() - size;
            if (start < 0) {
                start = 0;
            }
        }
    
        // Crear una sublista para la página actual
        List<PrestamoLibro> pagedPrestamos = listadoPrestamos.subList(start, end);
    
        // Crear el objeto Page para pasar a la vista
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PrestamoLibro> pagePrestamos = new PageImpl<>(
                pagedPrestamos,
                pageRequest,
                listadoPrestamos.size()
        );
    
        // Cargar otros datos necesarios
        List<InventarioLibro> listadoLibros = inventarioLibroService.listarLibros();
        List<Alumno> listadoAlumnos = alumnoService.listarAlumnos(null, null, null, null);
    
        // Añadir los atributos al modelo
        model.addAttribute("titulo", "Préstamos Devueltos");
        model.addAttribute("prestamos", pagePrestamos.getContent());
        model.addAttribute("libros", listadoLibros);
        model.addAttribute("alumnos", listadoAlumnos);
        model.addAttribute("page", pagePrestamos);
        model.addAttribute("nombreAlumno", nombreAlumno); // Para mantener el valor del término de búsqueda
    
        return "Biblioteca/prestamosDevueltos.html";
    }

    @GetMapping(value = "/verPrestamos", produces = "application/pdf")
    public ModelAndView verPrestamosPdf(Model model) {
        List<PrestamoLibro> listaPrestamos = prestamoLibroService.listarPrestamos();
    
        // Filtrar los préstamos que tienen estado "Pendiente"
        List<PrestamoLibro> listaPrestamosPendientes = listaPrestamos.stream()
            .filter(prestamo -> "Pendiente".equals(prestamo.getEstadoPrestamo()))
            .collect(Collectors.toList());

        ModelAndView modelAndView = new ModelAndView("Biblioteca/verPrestamosPdf");
        modelAndView.addObject("prestamos", listaPrestamosPendientes);
        return modelAndView;
    }

    @GetMapping(value = "/verDevueltos", produces = "application/pdf")
    public ModelAndView verDevueltosPdf(Model model) {
        List<PrestamoLibro> listaPrestamos = prestamoLibroService.listarPrestamos();
    
        // Filtrar los préstamos que tienen estado "Pendiente"
        List<PrestamoLibro> listaPrestamosPendientes = listaPrestamos.stream()
            .filter(prestamo -> "Devuelto".equals(prestamo.getEstadoPrestamo()))
            .collect(Collectors.toList());

        ModelAndView modelAndView = new ModelAndView("Biblioteca/verDevueltosPdf");
        modelAndView.addObject("prestamos", listaPrestamosPendientes);
        return modelAndView;
    }

    @GetMapping(value = "/verInventarioPdf", produces = "application/pdf")
    public ModelAndView verInventarioPdf(Model model) {
        
        // Obtener la lista de todos los libros
        List<InventarioLibro> listadoLibros = inventarioLibroService.listarLibros();

        ModelAndView modelAndView = new ModelAndView("Biblioteca/verInventarioPdf");
        modelAndView.addObject("libros", listadoLibros);
        return modelAndView;
    }

    @GetMapping(value = "/verPrestamosPorGrado", produces = "application/pdf")
    public ModelAndView verPrestamosPorGradoPdf(@RequestParam("codigoBachillerato") String codigoBachillerato) {
        List<PrestamoLibro> listaPrestamos = prestamoLibroService.listarPrestamos();
        
        // Filtrar los préstamos que tienen estado "Pendiente" y el bachillerato correcto
        List<PrestamoLibro> listaPrestamosPendientes = listaPrestamos.stream()
        .filter(prestamo -> "Pendiente".equals(prestamo.getEstadoPrestamo())
                         && codigoBachillerato.equals(
                             String.valueOf(prestamo.getAlumno().getBachillerato().getCodigoBachillerato())))
        .collect(Collectors.toList());

        // Obtener el nombre de la carrera del primer préstamovvvvvvvvvv                                                                                                                                                                                                       
        String nombreCarrera = listaPrestamosPendientes.isEmpty() 
        ? "" 
        : listaPrestamosPendientes.get(0).getAlumno().getBachillerato().getGrado() + "° " 
        + listaPrestamosPendientes.get(0).getAlumno().getBachillerato().getNombreCarrera() + " " 
        + listaPrestamosPendientes.get(0).getAlumno().getBachillerato().getSeccion();

        ModelAndView modelAndView = new ModelAndView("Biblioteca/verPrestamosPdf");
        modelAndView.addObject("prestamos", listaPrestamosPendientes);
        modelAndView.addObject("codigoBachillerato", codigoBachillerato);
        modelAndView.addObject("nombreCarrera", nombreCarrera);
        return modelAndView;
    }

    @GetMapping("/buscarAlumnos")
    @ResponseBody
    public List<Alumno> buscarAlumnos(@RequestParam("term") String term) {
        // Aquí debes obtener la lista de alumnos que coincidan con el término de búsqueda
        return alumnoService.buscarPorNombre(term);
    }
}
