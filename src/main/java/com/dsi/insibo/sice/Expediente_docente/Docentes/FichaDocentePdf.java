package com.dsi.insibo.sice.Expediente_docente.Docentes;

import java.awt.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.dsi.insibo.sice.entity.Docente;
import com.lowagie.text.Document;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("Expediente_docente/Docentes/fichaDocenteConsult")
public class FichaDocentePdf extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Docente docente = (Docente) model.get("profesor");
        // attachment fuerza la descarga, inline lo abre en el navegador
        String nombreArchivo = "ADMINISTRATIVO_" + docente.getDuiDocente() + "_"
                + docente.getNombreDocente();
        response.setHeader("Content-Disposition", "inline; filename=" + nombreArchivo + ".pdf");

        document.setPageSize(PageSize.LETTER);
        document.setMargins(85, 85, 36, 72); // Margen de 3 cm a los lados y 2.5 cm abajo
        document.open();

        // Fuentes
        Font titleFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY);

        // Crear una tabla para alinear la imagen y los títulos
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[] { 0.55f, 3 }); // Anchos relativos de las columnas (1: imagen, 3: títulos)

        // Añadir logo
        URL logoUrl = getClass().getResource("/static/Imagenes/LogoINSIBO.jpg");
        if (logoUrl != null) {
            try {
                Image img = Image.getInstance(logoUrl);
                float aspectRatio = img.getWidth() / (float) img.getHeight();
                float newWidth = 60;
                float newHeight = newWidth / aspectRatio;
                img.scaleToFit(newWidth, newHeight);
                img.setAlignment(Image.ALIGN_LEFT);

                PdfPCell logoCell = new PdfPCell(img);
                logoCell.setBorder(PdfPCell.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);

                // Ajustar la posición de la imagen para moverla hacia abajo
                logoCell.setPaddingTop(4); // Mover la imagen 10 puntos hacia abajo

                headerTable.addCell(logoCell);
            } catch (Exception e) {
                System.err.println("Error loading or resizing image: " + e.getMessage());
                PdfPCell emptyCell = new PdfPCell();
                emptyCell.setBorder(PdfPCell.NO_BORDER);
                headerTable.addCell(emptyCell);
            }
        } else {
            System.err.println("Logo image not found at path: /static/Imagenes/LogoINSIBO.jpg");
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(emptyCell);
        }

        // Añadir títulos
        Paragraph title = new Paragraph("INSTITUTO NACIONAL SIMON BOLIVAR\nCODIGO 11694 SANTO TOMAS", titleFont);
        Paragraph title2 = new Paragraph("FICHA DOCENTE: DATOS PERSONALES\n\n", titleFont2);
        title.setAlignment(Element.ALIGN_LEFT);
        title2.setAlignment(Element.ALIGN_LEFT);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.addElement(title);
        titleCell.addElement(title2);
        headerTable.addCell(titleCell);

        document.add(headerTable);

        // Tabla para los datos del docente
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8f);
        table.setSpacingAfter(8f);

        // Configurar celdas
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Datos Personales", headerFont));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.setBackgroundColor(new Color(230, 230, 230));
        table.addCell(cell);

        addTableCell(table, "Nombres:", docente.getNombreDocente(), headerFont, bodyFont);
        addTableCell(table, "Apellidos:", docente.getApellidoDocente(), headerFont, bodyFont);
        addTableCell(table, "Profesión:", docente.getProfesionDocente(), headerFont, bodyFont);
        addTableCell(table, "Fecha de Nacimiento:", docente.getFechaNacimientoD().toString(), headerFont, bodyFont);
        addTableCell(table, "Dirección:", docente.getDireccionDocente(), headerFont, bodyFont);
        addTableCell(table, "Departamento:", docente.getDepartamentoD(), headerFont, bodyFont);
        addTableCell(table, "Municipio:", docente.getMunicipioD(), headerFont, bodyFont);
        addTableCell(table, "Distrito:", docente.getDistritoDocente(), headerFont, bodyFont);
        addTableCell(table, "Zona:", docente.getZonaDocente(), headerFont, bodyFont);
        addTableCell(table, "DUI:", docente.getDuiDocente(), headerFont, bodyFont);
        addTableCell(table, "NIP:", docente.getNip(), headerFont, bodyFont);
        addTableCell(table, "NIT:", docente.getNit(), headerFont, bodyFont);
        addTableCell(table, "NUP:", docente.getNup(), headerFont, bodyFont);
        addTableCell(table, "Correo Electrónico:", docente.getCorreoDocente(), headerFont, bodyFont);
        addTableCell(table, "Teléfono Personal:", docente.getTelefonoDocente(), headerFont, bodyFont);
        addTableCell(table, "Teléfono Fijo:", docente.getTelefonoFijoDocente(), headerFont, bodyFont);
        addTableCell(table, "Especialidad en Estudio:", docente.getEspecialidadEnEstudio(), headerFont, bodyFont);
        addTableCell(table, "Fecha de ingreso al MINEDUCYT:", docente.getFechaMineducyt().toString(), headerFont,
                bodyFont);
        addTableCell(table, "Título que lo acredita como docente:", docente.getTituloDocente(), headerFont, bodyFont);
        addTableCell(table, "¿Presenta curriculum?", docente.isCurriculumDocente() ? "Sí" : "No", headerFont, bodyFont);
        addTableCell(table, "¿Presenta atestados?", docente.isAtestadosDocente() ? "Sí" : "No", headerFont, bodyFont);
        addTableCell(table, "Fecha de Entrega:", docente.getFechaEntrega().toString(), headerFont, bodyFont);

        document.add(table);

        // Firma
        /*
         * Paragraph firma = new
         * Paragraph("\n\nFirma: ________________\n           Subdirección", bodyFont);
         * firma.setAlignment(Element.ALIGN_CENTER);
         * document.add(firma);
         */

        // Pie de página
        String fechaImpresion = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Paragraph footer = new Paragraph("\n\n\nINSIBO - " + fechaImpresion, footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private void addTableCell(PdfPTable table, String header, String body, Font headerFont, Font bodyFont) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(header, headerFont));
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(body, bodyFont));
        cell.setPadding(5);
        table.addCell(cell);
    }
}
