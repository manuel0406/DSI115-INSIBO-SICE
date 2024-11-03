package com.dsi.insibo.sice.Paquetes_escolar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Administrativo.Orientadores.OrientadorService;
import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.Expediente_docente.Docentes.DocenteService;
import com.dsi.insibo.sice.Paquetes_escolar.Uniforme.UniformeService;
import com.dsi.insibo.sice.Paquetes_escolar.Utiles.UtilesService;
import com.dsi.insibo.sice.Paquetes_escolar.Zapatos.ZapatosService;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.Uniforme;
import com.dsi.insibo.sice.entity.UtilesEscolares;
import com.dsi.insibo.sice.entity.Zapatos;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DOCENTE', 'SUBDIRECTORA', 'DIRECTOR')")
@RequestMapping("/entregasPaquetes")
public class EntregasController {
    @Autowired
    private EntregasService entregasService;
    @Autowired
    BachilleratoService bachilleratoService;
    @Autowired
    DocenteService docenteService;
    @Autowired
    OrientadorService orientadorService;
    @Autowired
    AlumnoService alumnoService;

    @Autowired
    private UniformeService uniformeService;
    @Autowired
    private UtilesService utilesEscolaresService;
    @Autowired
    private ZapatosService zapatosService;

    @GetMapping("/seccionAsigada/{id}")
    public String verAlumno(Model model, @PathVariable("id") int id,
            @RequestParam(value = "tipoPaquete", required = false) String tipoPaquete) {

        // Obtener la información del Bachillerato
        Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(id);

        // Añadir atributos al modelo
        model.addAttribute("bachillerato", bachillerato);
        model.addAttribute("id", id); // Pasar el ID del bachillerato
        model.addAttribute("tipoPaquete", tipoPaquete); // Paquete seleccionado
        model.addAttribute("info", "Selecciona un tipo de paquete para comenzar");

        return "Paquetes_escolares/generarListado";
    }

    @GetMapping("/seccion/{id}")
    public String miSeccion(Model model, @PathVariable("id") int id,
            @RequestParam(value = "tipoPaquete", required = false) String tipoPaquete,
            @RequestParam(value = "genero", required = false) String genero) {

        // Obtener la información del Bachillerato por ID
        Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(id);

        // Si el filtro de género está vacío, asignarlo a null
        if (genero != null && genero.isEmpty()) {
            genero = null;
        }

        // Obtener la lista de alumnos según los parámetros
        List<Alumno> listaAlumnos = alumnoService.listarAlumnos(bachillerato.getNombreCarrera(),
                String.valueOf(bachillerato.getGrado()), bachillerato.getSeccion(), genero);

        // Ordenar la lista de alumnos por apellido
        listaAlumnos.sort(Comparator.comparing(Alumno::getApellidoAlumno));

        // Agregar atributos al modelo para la vista
        model.addAttribute("bachillerato", bachillerato);
        model.addAttribute("alumnos", listaAlumnos); // Lista de alumnos
        model.addAttribute("tipoPaquete", tipoPaquete); // Tipo de paquete seleccionado
        model.addAttribute("id", id); // ID del bachillerato

        return "Paquetes_escolares/generarListado";
    }

    /**Metodo que cuenta las fechas de entrega existentes para limitar el número de entregas por paquete */
    @GetMapping("/fechasPaquetesContar/{idBachillerato}")
    public ResponseEntity<Map<String, Integer>> obtenerConteoPaquetes(@PathVariable int idBachillerato) {
        int countZapatos = zapatosService.obtenerFechasPorBachillerato(idBachillerato).size();
        int countUtiles = utilesEscolaresService.obtenerFechasPorBachillerato(idBachillerato).size();
        int countUniformes = uniformeService.obtenerFechasPorBachillerato(idBachillerato).size();

        Map<String, Integer> conteos = new HashMap<>();
        conteos.put("zapatos", countZapatos);
        conteos.put("utiles", countUtiles);
        conteos.put("uniformes", countUniformes);

        return ResponseEntity.ok(conteos);
    }

    /**Metodo para guardar el estado de entrega de paquetes  */
    @PostMapping("/entregarPaquete")
    public String entregarPaquete(@RequestParam("tipoPaquete") String tipoPaquete,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            // Obtener los IDs de los alumnos desde el formulario
            String[] alumnoIds = request.getParameterValues("alumnoId");

            for (String idAlumno : alumnoIds) {
                Alumno alumno = alumnoService.buscarPorIdAlumno(Integer.parseInt(idAlumno));
                // Verificar el tipo de paquete y realizar la entrega correspondiente
                if (tipoPaquete.equals("paqueteUniforme")) {
                    // Obtener la talla del uniforme
                    String tallaUniforme = request.getParameter("talla_" + idAlumno);
                    // Verificar si se ha proporcionado una talla
                    if (tallaUniforme == null || tallaUniforme.trim().isEmpty()) {
                        redirectAttributes.addFlashAttribute("error", "La talla del uniforme es obligatoria.");
                        return "redirect:/entregasPaquetes/seccion/" + request.getParameter("id");
                    }
                    // Llamar al servicio para guardar el uniforme
                    boolean fueEntregado = request.getParameter("entregado_" + idAlumno) != null;
                    uniformeService.saveUniforme(alumno, tallaUniforme, fueEntregado);
                } else if (tipoPaquete.equals("paqueteUtiles")) {
                    boolean fueEntregado = request.getParameter("entregado_" + idAlumno) != null;
                    utilesEscolaresService.saveUtilesEscolares(alumno, fueEntregado);
                } else if (tipoPaquete.equals("paqueteZapatos")) {
                    String tallaZapatosStr = request.getParameter("talla_" + idAlumno);
                    if (tallaZapatosStr == null || tallaZapatosStr.trim().isEmpty()) {
                        redirectAttributes.addFlashAttribute("error", "La talla de zapatos es obligatoria.");
                        return "redirect:/entregasPaquetes/seccion/" + request.getParameter("id");
                    }
                    int tallaZapato;
                    try {
                        tallaZapato = Integer.parseInt(tallaZapatosStr);
                    } catch (NumberFormatException e) {
                        redirectAttributes.addFlashAttribute("error", "La talla de zapatos debe ser un número válido.");
                        return "redirect:/entregasPaquetes/seccion/" + request.getParameter("id");
                    }
                    boolean fueEntregado = request.getParameter("entregado_" + idAlumno) != null;
                    zapatosService.saveZapatos(alumno, tallaZapato, fueEntregado);
                }
            }
            // Mensaje de exito
            redirectAttributes.addFlashAttribute("success", "Paquetes entregados con éxito.");
        } catch (Exception e) {
            // Mensaje de error
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al entregar los paquetes.");
        }
        return "redirect:/entregasPaquetes/seccion/" + request.getParameter("id");
    }

    /**Imprimir listado de entrega de seccion para el docente orientador, para la entrega fisica, genera ficha de entrega donde se debe firmar de recibido el paquete por parte del alumno/encargado */
    @GetMapping("/imprimirListado/{id}")
    public void imprimirListado(HttpServletResponse response,
            @PathVariable("id") int id,
            @RequestParam("tipoPaquete") String tipoPaquete) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=Listado_Paquetes.pdf");

        // Obtener datos: bachillerato, alumnos, etc.
        Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(id);
        List<Alumno> alumnos = alumnoService.listarAlumnos(bachillerato.getNombreCarrera(),
                String.valueOf(bachillerato.getGrado()),
                bachillerato.getSeccion(), null);

        String paquete = "";

        // Verificar el tipo de paquete
        if (tipoPaquete.equals("paqueteUniforme")) {
            paquete = "Paquetes de Uniforme Escolar";
        } else if (tipoPaquete.equals("paqueteUtiles")) {
            paquete = "Paquetes de Útiles Escolares";
        } else if (tipoPaquete.equals("paqueteZapatos")) {
            paquete = "Paquetes de Zapatos";
        } else {
            paquete = "";
        }

        Document document = new Document(PageSize.LETTER.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Crear la tabla de encabezado con 2 columnas
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[] { 0.55f, 3 });

        // Añadir el logo
        URL logoUrl = getClass().getResource("/static/Imagenes/LogoINSIBO.jpg");
        if (logoUrl != null) {
            try {
                Image img = Image.getInstance(logoUrl);
                float aspectRatio = img.getWidth() / (float) img.getHeight();
                float newWidth = 60;
                float newHeight = newWidth / aspectRatio;
                img.scaleToFit(newWidth, newHeight);
                img.setAlignment(Image.ALIGN_RIGHT);

                PdfPCell logoCell = new PdfPCell(img);
                logoCell.setBorder(PdfPCell.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                logoCell.setPaddingTop(4);

                headerTable.addCell(logoCell);
            } catch (Exception e) {
                System.err.println("Error al cargar o redimensionar la imagen: " + e.getMessage());
                PdfPCell emptyCell = new PdfPCell();
                emptyCell.setBorder(PdfPCell.NO_BORDER);
                headerTable.addCell(emptyCell);
            }
        } else {
            System.err.println("Logo no encontrado en la ruta: /static/Imagenes/LogoINSIBO.jpg");
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(emptyCell);
        }

        // Definir fuentes para los titulos
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font titleFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE); // Texto Blanco
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK); // Texto Negro

        // Crear los parrafos para los titulos
        Paragraph title = new Paragraph("INSTITUTO NACIONAL SIMON BOLIVAR\nCODIGO 11694 SANTO TOMAS", titleFont);
        Paragraph title2 = new Paragraph("PROGRAMA DE DOTACION DE PAQUETES ESCOLARES\n\n", titleFont2);
        title.setAlignment(Element.ALIGN_LEFT);
        title2.setAlignment(Element.ALIGN_LEFT);

        // Crear una celda para los titulos y añadir los párrafos
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.addElement(title);
        titleCell.addElement(title2);
        headerTable.addCell(titleCell);

        // Añadir la tabla de encabezado al documento
        document.add(headerTable);

        // Definir la fuente para el subtítulo
        Font fontSubTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        // En una sola línea
        String info = "Fecha: " + LocalDate.now().toString() + " | " +
                "Bachillerato: " + bachillerato.getNombreCarrera() + " | " +
                "Año: " + bachillerato.getGrado() + "° Sección: " + bachillerato.getSeccion();

        // Añadir al documento
        document.add(new Paragraph(info, fontSubTitle));

        // Tipo de paquete
        document.add(new Paragraph("Tipo: " + paquete, fontSubTitle));
        document.add(new Paragraph(" ")); // Línea vacia

        // Crear la tabla para los alumnos con 7 columnas
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new int[] { 1, 2, 2, 2, 1, 3, 3, 3 });
        // Definir la fuente para la tabla de alumnos

        // Añadir encabezado a la tabla utilizando el método auxiliar
        addTableHeader(table, "N°", headerFont);
        addTableHeader(table, "NIE", headerFont);
        addTableHeader(table, "Apellido", headerFont);
        addTableHeader(table, "Nombre", headerFont);
        addTableHeader(table, "Género", headerFont);
        addTableHeader(table, "Nombre de quien recibe", headerFont);
        addTableHeader(table, "DUI o NIE de quien recibe", headerFont);
        addTableHeader(table, "Firma", headerFont);
        // Añadir los datos de los alumnos utilizando el método auxiliar
        int index = 1;
        for (Alumno alumno : alumnos) {
            addTableCell(table, String.valueOf(index++), bodyFont, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE); // Número
                                                                                                                // de
                                                                                                                // registro

            addTableCell(table, String.valueOf(alumno.getNie()), bodyFont, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE); // NIE
            addTableCell(table, alumno.getApellidoAlumno(), bodyFont,
                    Element.ALIGN_LEFT, Element.ALIGN_MIDDLE); // Nombre completo alineado a la izquierda
            addTableCell(table, alumno.getNombreAlumno(), bodyFont,
                    Element.ALIGN_LEFT, Element.ALIGN_MIDDLE); // Nombre completo alineado a la izquierda
            addTableCell(table, alumno.getSexoAlumno(), bodyFont, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE); // Género
            addTableCell(table, "", bodyFont, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE); // Nombre de quien recibe
                                                                                         // (celda vacia)
            addTableCell(table, "", bodyFont, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE); // DUI o NIE de quien recibe
                                                                                         // (celda vacia)
            addTableCell(table, "", bodyFont, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE); // Firma (celda vacia)
        }
        // Añadir la tabla al documento
        document.add(table);

        // Cerrar el documento
        document.close();

    }

    /**
     * Método auxiliar para añadir encabezados a la tabla de alumnos.
     *
     * @param table       La tabla a la que se añadirá el encabezado.
     * @param headerTitle El texto del encabezado.
     * @param font        La fuente a aplicar (debe ser texto blanco).
     */
    private void addTableHeader(PdfPTable table, String headerTitle, Font font) {
        PdfPCell header = new PdfPCell(new Phrase(headerTitle, font));
        header.setBackgroundColor(new Color(0, 51, 102)); // Azul oscuro
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_CENTER);
        header.setPadding(5);
        table.addCell(header);
    }

    /**
     * Método auxiliar para añadir celdas de datos a la tabla de alumnos.
     *
     * @param table               La tabla a la que se añadirá la celda.
     * @param text                El texto de la celda.
     * @param font                La fuente a aplicar (debe ser texto negro).
     * @param horizontalAlignment La alineación horizontal del texto en la celda.
     * @param verticalAlignment   La alineación vertical del texto en la celda.
     */
    private void addTableCell(PdfPTable table, String text, Font font, int horizontalAlignment, int verticalAlignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        table.addCell(cell);
    }

    @GetMapping("/reportesEntrega/{id}")
    public String generarReporte(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "Paquetes_escolares/reporteEntrega";
    }

    /**obtener las fechas de entregas de paquetes escolares según el tipo de paquete seleccionado, para llenar un select */
    @GetMapping("/fechasPaquete")
    @ResponseBody
    public List<String> obtenerFechasPaquete(@RequestParam("tipoPaquete") String tipoPaquete,
            @RequestParam("idBachillerato") int idBachillerato) {
        List<String> fechas = new ArrayList<>();
        switch (tipoPaquete) {
            case "paqueteZapatos":
                fechas = zapatosService.obtenerFechasPorBachillerato(idBachillerato);
                break;
            case "paqueteUniforme":
                fechas = uniformeService.obtenerFechasPorBachillerato(idBachillerato);
                break;
            case "paqueteUtiles":
                fechas = utilesEscolaresService.obtenerFechasPorBachillerato(idBachillerato);
                break;
            default:
                break;
        }
        return fechas;
    }

    /**Metodo para obtener el reporte de entregas para el docente orientador, segun el idBachillerato, el tipo de paquete, fecha de entrega y el estado de entrega */
    @GetMapping("/reporteEntrega/{id}")
    public String filtrarEntregas(
            @PathVariable("id") int idBachillerato,
            @RequestParam(value = "tipoPaquete", required = false) String tipoPaquete,
            @RequestParam(value = "fechaPaquete", required = false) String fechaPaquete,
            @RequestParam(value = "estadoEntrega", required = false) String estadoEntrega,
            Model model) {

        List<Object[]> resultados;

        // Validaciones
        if (tipoPaquete == null || fechaPaquete == null || estadoEntrega == null) {
            model.addAttribute("error", "Por favor, selecciona un tipo de paquete, una fecha y un estado de entrega válidos.");
            Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(idBachillerato);
            model.addAttribute("bachillerato", bachillerato);
            model.addAttribute("id", idBachillerato);
            return "Paquetes_escolares/reporteEntrega";
        }

        // Convertir el estado de entrega
        Boolean estado = null;
        if ("entregado".equals(estadoEntrega)) {
            estado = true;
        } else if ("no_entregado".equals(estadoEntrega)) {
            estado = false;
        }

        // Consulta según el tipo de paquete
        if ("paqueteUniforme".equals(tipoPaquete)) {
            if (estado == null) {
                resultados = entregasService.filtrarPorUniformeSinEstado(idBachillerato, fechaPaquete);
            } else {
                resultados = entregasService.filtrarPorUniforme(idBachillerato, fechaPaquete, estado);
            }
        } else if ("paqueteUtiles".equals(tipoPaquete)) {
            if (estado == null) {
                resultados = entregasService.filtrarPorUtilesSinEstado(idBachillerato, fechaPaquete);
            } else {
                resultados = entregasService.filtrarPorUtiles(idBachillerato, fechaPaquete, estado);
            }
        } else if ("paqueteZapatos".equals(tipoPaquete)) {
            if (estado == null) {
                resultados = entregasService.filtrarPorZapatosSinEstado(idBachillerato, fechaPaquete);
            } else {
                resultados = entregasService.filtrarPorZapatos(idBachillerato, fechaPaquete, estado);
            }
        } else {
            resultados = new ArrayList<>();
        }

        // Verificar si la lista de resultados está vacía
        if (resultados.isEmpty()) {
            model.addAttribute("warning", "No hay datos a mostrar para la selección realizada.");
        }

        // Obtener la información del Bachillerato
        Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(idBachillerato);

        // Añadir atributos al modelo
        model.addAttribute("bachillerato", bachillerato);
        model.addAttribute("tipoPaquete", tipoPaquete);
        model.addAttribute("resultados", resultados);
        model.addAttribute("id", idBachillerato);
        model.addAttribute("tipo", tipoPaquete);
        model.addAttribute("fecha", fechaPaquete);
        model.addAttribute("estado", estadoEntrega);

        return "Paquetes_escolares/reporteEntrega";
    }

    /**Metodo para guardar el editar estado de entrega de paquete escolar, se realiza para cada uno de los registros */
    @PostMapping("/editarEntrega")
    public String editarEntrega(@RequestParam("id_entrega") Integer idEntrega,
            @RequestParam("entregado") boolean entregado,
            @RequestParam("tipoPaquete") String tipoPaquete,
            @RequestParam("id") Integer id,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "fecha", required = false) String fecha,
            @RequestParam(value = "estado", required = false) String estado,
            RedirectAttributes redirectAttributes) {

        boolean success = false;
        String message = "";

        // Buscar y actualizar en función del tipo de paquete
        if ("paqueteZapatos".equalsIgnoreCase(tipoPaquete)) {
            Zapatos zapatos = zapatosService.findById(idEntrega);
            if (zapatos != null) {
                zapatos.setZapataloEntregado(entregado);
                zapatosService.save(zapatos);
                success = true;
                message = "Estado de entrega de zapatos actualizado con éxito.";
            }
        } else if ("paqueteUniforme".equalsIgnoreCase(tipoPaquete)) {
            Uniforme uniforme = uniformeService.findById(idEntrega);
            if (uniforme != null) {
                uniforme.setUniformeEntegado(entregado);
                uniformeService.save(uniforme);
                success = true;
                message = "Estado de entrega de uniforme actualizado con éxito.";
            }
        } else if ("paqueteUtiles".equalsIgnoreCase(tipoPaquete)) {
            UtilesEscolares utiles = utilesEscolaresService.findById(idEntrega);
            if (utiles != null) {
                utiles.setEntregado(entregado);
                utilesEscolaresService.save(utiles);
                success = true;
                message = "Estado de entrega de útiles actualizado con éxito.";
            }
        }

        if (success) {
            redirectAttributes.addFlashAttribute("success", message);
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el estado de entrega.");
        }

        // Redireccionar a la misma página con los parámetros necesarios
        return "redirect:/entregasPaquetes/reporteEntrega/" + id +
                "?tipoPaquete=" + tipo +
                "&fechaPaquete=" + fecha +
                "&estadoEntrega=" + estado;
    }

}
