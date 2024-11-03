package com.dsi.insibo.sice.Paquetes_escolar;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dsi.insibo.sice.Administrativo.Bachilleratos.Servicios.BachilleratoService;
import com.dsi.insibo.sice.Paquetes_escolar.Donaciones.InventarioDonacionService;
import com.dsi.insibo.sice.Paquetes_escolar.Uniforme.UniformeService;
import com.dsi.insibo.sice.Paquetes_escolar.Utiles.UtilesService;
import com.dsi.insibo.sice.Paquetes_escolar.Zapatos.ZapatosService;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.InventarioDonacion;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DOCENTE', 'SUBDIRECTORA', 'DIRECTOR')")
@RequestMapping("/paquetes") 
public class PaquetesController {
    @Autowired
    private InventarioDonacionService inventarioDonacionService;

    @Autowired
    private BachilleratoService bachilleratoService;

    @Autowired
    private UniformeService uniformeService;
    @Autowired
    private UtilesService utilesEscolaresService;
    @Autowired
    private ZapatosService zapatosService;
    @Autowired
    private EntregasService entregasService;

    @GetMapping("/inicio")
    public String paquetesInicio(Model model) {
        model.addAttribute("titulo", "Inicio paquetes");
        return "Paquetes_escolares/inicioPaquetes";
    }

    // Donaciones
    @GetMapping("/listaDonaciones")
    public String paquetesDonaciones(Model model) {
        model.addAttribute("donaciones", inventarioDonacionService.obtenerTodasLasDonaciones());
        model.addAttribute("titulo", "Paquetes escolares");
        return "Paquetes_escolares/listaDonaciones";
    }

    // Guardar donaciones
    @PostMapping("/guardar")
    public String guardarDonacion(InventarioDonacion donacion, RedirectAttributes attributes) {
        if ("0".equals(donacion.getTipoPrenda())) {
            attributes.addFlashAttribute("error", "¡Debe seleccionar un tipo de prenda válido!");
            return "redirect:/paquetes/listaDonaciones";
        }

        // Convertir talla a mayusculas
        donacion.setTallaPrenda(donacion.getTallaPrenda().toUpperCase());

        // Verificar si ya existe una donación con la combinación de tipoPrenda y tallaPrenda
        if (inventarioDonacionService.existeDonacion(donacion.getTipoPrenda(), donacion.getTallaPrenda())) {
            attributes.addFlashAttribute("error", "¡Ya existe una donación con este tipo y talla de prenda!");
            return "redirect:/paquetes/listaDonaciones";
        }

        try {
            inventarioDonacionService.guardarDonacion(donacion);
            attributes.addFlashAttribute("success", "¡Donación guardada con éxito!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "¡Error al guardar la donación!");
        }

        return "redirect:/paquetes/listaDonaciones";
    }

    // Modificar la cantidad de donación, agregando al inventario de un registro especifico
    @PostMapping("/modificarCantidad")
    public String modificarCantidad(
            @RequestParam("donacionId") int donacionId,
            @RequestParam("nuevaCantidad") int nuevaCantidad,
            RedirectAttributes redirectAttributes) {

        // Obtener la cantidad actual desde el servicio
        int cantidadActual = inventarioDonacionService.obtenerCantidadPorId(donacionId);

        // Calcular la nueva cantidad sumando la cantidad actual y la nueva cantidad ingresada
        int cantidadTotal = cantidadActual + nuevaCantidad;

        // Llamar al servicio para actualizar la cantidad
        boolean success = inventarioDonacionService.actualizarCantidadDonacion(donacionId, cantidadTotal);

        if (success) {
            redirectAttributes.addFlashAttribute("success", "Cantidad actualizada con éxito.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar la cantidad.");
        }

        return "redirect:/paquetes/listaDonaciones";
    }

    // Modificar la cantidad de donación, restando al inventario de un registro especifico
    @PostMapping("/modificarMenosCantidad")
    public String modificarMenosCantidad(
            @RequestParam("donacionId") int donacionId,
            @RequestParam("nuevaCantidad") int nuevaCantidad,
            RedirectAttributes redirectAttributes) {

        // Obtener la cantidad actual desde el servicio
        int cantidadActual = inventarioDonacionService.obtenerCantidadPorId(donacionId);
        if (cantidadActual >= nuevaCantidad) {
            // Calcular la nueva cantidad sumando la cantidad actual y la nueva cantidad ingresada
            int cantidadTotal = cantidadActual - nuevaCantidad;

            // Llamar al servicio para actualizar la cantidad
            boolean success = inventarioDonacionService.actualizarCantidadDonacion(donacionId, cantidadTotal);

            if (success) {
                redirectAttributes.addFlashAttribute("success", "Cantidad actualizada con éxito.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo actualizar la cantidad.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar la cantidad.");
        }

        return "redirect:/paquetes/listaDonaciones";
    }

    // eliminar donacion solo si esta en 0(cero) validación en vista
    @PostMapping("/eliminar/{id}")
    public String eliminarDonacion(@PathVariable int id, RedirectAttributes redirectAttributes) {
        inventarioDonacionService.eliminarDonacion(id);
        redirectAttributes.addFlashAttribute("success", "Donación eliminada con éxito.");
        return "redirect:/paquetes/listaDonaciones";
    }

    // entregas de paquetes en administrador
    @GetMapping("/entregaLista")
    public String paquetesEntregaLista(Model model) {
        // Obtener el año académico activo desde el servicio
        Integer idAnioAcademico = inventarioDonacionService.obtenerAnioAcademicoActivo();

        // Obtener los bachilleratos basados en el año académico activo desde el servicio
        List<Object[]> bachilleratos = inventarioDonacionService.obtenerBachilleratosPorAnioAcademico(idAnioAcademico);
        // Agregar los bachilleratos al modelo
        model.addAttribute("bachilleratos", bachilleratos);
        model.addAttribute("titulo", "Reportes entrega");
        return "Paquetes_escolares/generarEntregas";
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

    /**Obtener listado de entregas según el tipo de entregas existente */
    @GetMapping("/reporteEntregas")
    public String filtrarEntregasAdm(
            @RequestParam("tipoPaquete") String tipoPaquete,
            @RequestParam("fechaPaquete") String fechaPaquete,
            @RequestParam("estadoEntrega") String estadoEntrega,
            @RequestParam("idBachillerato") int idBachillerato,
            Model model) {

        // Obtener el año académico activo desde el servicio
        Integer idAnioAcademico = inventarioDonacionService.obtenerAnioAcademicoActivo();

        // Obtener los bachilleratos basados en el año académico activo desde el servicio
        List<Object[]> bachilleratos = inventarioDonacionService.obtenerBachilleratosPorAnioAcademico(idAnioAcademico);
        // Agregar los bachilleratos al modelo
        model.addAttribute("bachilleratos", bachilleratos);

        List<Object[]> resultados;

        // Validaciones
        if ("0".equals(tipoPaquete) || "0".equals(fechaPaquete) || "0".equals(estadoEntrega)) {
            model.addAttribute("error",
                    "Por favor, selecciona un tipo de paquete, una fecha y un estado de entrega válidos.");
            Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(idBachillerato);
            model.addAttribute("bachillerato", bachillerato);
            model.addAttribute("id", idBachillerato);
            model.addAttribute("titulo", "Paquetes escolares");
            return "Paquetes_escolares/generarEntregas";
        }

        // Convertir el estado de entrega
        Boolean estado = null;
        if ("entregado".equals(estadoEntrega)) {
            estado = true;
        } else if ("no_entregado".equals(estadoEntrega)) {
            estado = false;
        }

        // Consulta segun el tipo de paquete
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
        model.addAttribute("titulo", "Reportes entrega");
        model.addAttribute("fechaPaquete", fechaPaquete);
        model.addAttribute("estadoEntrega", estadoEntrega);
        model.addAttribute("bachillerato", bachillerato);
        model.addAttribute("tipoPaquete", tipoPaquete);
        model.addAttribute("resultados", resultados);
        model.addAttribute("id", idBachillerato);

        return "Paquetes_escolares/generarEntregas";
    }

    @GetMapping("/entregaListaim")
    public String paquetesEntregaListaIm(Model model) {
        // Obtener el año académico activo desde el servicio
        Integer idAnioAcademico = inventarioDonacionService.obtenerAnioAcademicoActivo();

        // Obtener los bachilleratos basados en el año académico activo desde el servicio
        List<Object[]> bachilleratos = inventarioDonacionService.obtenerBachilleratosPorAnioAcademico(idAnioAcademico);
        // Agregar los bachilleratos al modelo
        model.addAttribute("bachilleratos", bachilleratos);

        return "Paquetes_escolares/impresionEntregas";
    }

    /**Metodo de impresión de entregas de paquetes escolares */
    @GetMapping("/imprimirEntregas")
    public void imprimirEntregas(HttpServletResponse response,
            @RequestParam("tipoPaquete") String tipoPaquete,
            @RequestParam("fechaPaquete") String fechaPaquete,
            @RequestParam("estadoEntrega") String estadoEntrega,
            @RequestParam("idBachillerato") int idBachillerato) throws IOException, DocumentException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=Entregas_Paquetes.pdf");

        // Obtener el bachillerato
        Bachillerato bachillerato = bachilleratoService.bachilleratoPorId(idBachillerato);

        // Convertir estadoEntrega a booleano
        Boolean estado = null;
        if ("entregado".equals(estadoEntrega)) {
            estado = true;
        } else if ("no_entregado".equals(estadoEntrega)) {
            estado = false;
        }

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
        // Filtrar resultados según el tipo de paquete
        List<Object[]> resultados;
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

        Document document = new Document(PageSize.LETTER.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        //Encabezado
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
                headerTable.addCell(new PdfPCell());
            }
        }

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font titleFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        Paragraph title = new Paragraph("INSTITUTO NACIONAL SIMON BOLIVAR\nCODIGO 11694 SANTO TOMAS", titleFont);
        Paragraph title2 = new Paragraph("PROGRAMA DE DOTACION DE PAQUETES ESCOLARES\n\n", titleFont2);
        title.setAlignment(Element.ALIGN_LEFT);
        title2.setAlignment(Element.ALIGN_LEFT);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.addElement(title);
        titleCell.addElement(title2);
        headerTable.addCell(titleCell);

        document.add(headerTable);

        // Información del bachillerato
        Font fontSubTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        String info = "Bachillerato:"+ bachillerato.getNombreCarrera() +"\n" + 
                        "Fecha: " + LocalDate.now().toString()  + " | Año: " + bachillerato.getGrado() + "° Sección: " + bachillerato.getSeccion();
        document.add(new Paragraph(info, fontSubTitle));

        document.add(new Paragraph("Tipo: " + paquete, fontSubTitle));
        document.add(new Paragraph(" ")); // Línea vacía

        // Crear tabla de resultados
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new int[] { 1, 2, 2, 2, 2, 3 });

        // Encabezado de la tabla
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        addTableHeader(table, "N°", headerFont);
        addTableHeader(table, "NIE", headerFont);
        addTableHeader(table, "Apellido", headerFont);
        addTableHeader(table, "Nombre", headerFont);
        addTableHeader(table, "Fecha Entrega", headerFont);
        addTableHeader(table, "Estado Entrega", headerFont);

        // Añadir los resultados
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        int index = 1;
        for (Object[] row : resultados) {
            addTableCell(table, String.valueOf(index++), bodyFont);
            addTableCell(table, String.valueOf(row[2]), bodyFont); // NIE
            addTableCell(table, String.valueOf(row[1]), bodyFont); 
            addTableCell(table, String.valueOf(row[3]), bodyFont); 
            addTableCell(table, String.valueOf(row[4]), bodyFont); // Fecha entrega
            addTableCell(table, (Boolean) row[5] ? "Entregado" : "No Entregado", bodyFont); // Estado entrega
        }

        // Añadir la tabla al documento
        document.add(table);

        // Cerrar el documento
        document.close();
    }

    private void addTableHeader(PdfPTable table, String headerTitle, Font font) {
    PdfPCell header = new PdfPCell(new Phrase(headerTitle, font));
    header.setBackgroundColor(new Color(0, 51, 102)); // Azul oscuro
    header.setHorizontalAlignment(Element.ALIGN_CENTER);
    header.setVerticalAlignment(Element.ALIGN_CENTER);
    header.setPadding(5);
    table.addCell(header);
}

private void addTableCell(PdfPTable table, String text, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setPadding(5);
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    table.addCell(cell);
}

}
