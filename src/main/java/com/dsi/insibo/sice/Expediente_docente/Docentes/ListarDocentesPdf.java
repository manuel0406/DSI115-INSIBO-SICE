package com.dsi.insibo.sice.Expediente_docente.Docentes;

import java.awt.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

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

@Component("Expediente_docente/Docentes/listarDocentes")
public class ListarDocentesPdf extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<DocenteDTO> listadoDocentes = (List<DocenteDTO>) model.get("Docentes");
        response.setHeader("Content-Disposition", "inline; filename=" + "Personal_Docente_INSIBO" + ".pdf");


        document.setPageSize(PageSize.LETTER.rotate());
        document.setMargins(40, 40, 36, 72); // Margen de 3 cm a los lados y 2.5 cm abajo
        document.open();

        // Fuentes
        Font titleFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY);

        // Crear una tabla para alinear la imagen y los títulos
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(50);
        headerTable.setWidths(new float[]{0.55f, 1.75f}); // Anchos relativos de las columnas (1: imagen, 3: títulos)

        // Añadir logo
        URL logoUrl = getClass().getResource("/static/Imagenes/LogoINSIBO.jpg");
        if (logoUrl != null) {
            try {
                Image img = Image.getInstance(logoUrl);
                float aspectRatio = img.getWidth() / (float) img.getHeight();
                float newWidth = 60;
                float newHeight = newWidth / aspectRatio;
                img.scaleToFit(newWidth, newHeight);
                img.setAlignment(Image.ALIGN_CENTER);

                PdfPCell logoCell = new PdfPCell(img);
                logoCell.setBorder(PdfPCell.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                // Ajustar la posición de la imagen para moverla hacia abajo
                logoCell.setPaddingTop(5); // Mover la imagen 10 puntos hacia abajo

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
        Paragraph title2 = new Paragraph("LISTADO GENERAL DE DOCENTES\n\n", titleFont2);
        title.setAlignment(Element.ALIGN_LEFT);
        title2.setAlignment(Element.ALIGN_LEFT);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.addElement(title);
        titleCell.addElement(title2);
        headerTable.addCell(titleCell);

        document.add(headerTable);
        // FIN

        // Tabla de docentes
        PdfPTable tablaDocentes = new PdfPTable(7); // Añadir una columna más para el número de registro
        tablaDocentes.setWidthPercentage(100);
        tablaDocentes.setWidths(new float[]{1, 2, 2, 2, 2, 1, 2}); // Anchos relativos de las columnas

        // Encabezados de la tabla de docentes
        addTableHeader(tablaDocentes, "N.º", headerFont);
        addTableHeader(tablaDocentes, "Nombres", headerFont);
        addTableHeader(tablaDocentes, "Apellidos", headerFont);
        addTableHeader(tablaDocentes, "Profesión", headerFont);
        addTableHeader(tablaDocentes, "Teléfono", headerFont);
        addTableHeader(tablaDocentes, "Edad", headerFont);
        addTableHeader(tablaDocentes, "DUI", headerFont);

        // Datos de los docentes
        int registro = 1;
        for (DocenteDTO docente : listadoDocentes) {
            addTableCell(tablaDocentes, String.valueOf(registro++), bodyFont); // Añadir número de registro
            addTableCell(tablaDocentes, docente.getDocente().getNombreDocente(), bodyFont);
            addTableCell(tablaDocentes, docente.getDocente().getApellidoDocente(), bodyFont);
            addTableCell(tablaDocentes, docente.getDocente().getProfesionDocente(), bodyFont);
            addTableCell(tablaDocentes, docente.getDocente().getTelefonoDocente(), bodyFont);
            addTableCell(tablaDocentes, String.valueOf(docente.getEdad()), bodyFont);
            addTableCell(tablaDocentes, docente.getDocente().getDuiDocente(), bodyFont);
        }

        document.add(tablaDocentes);

        // Pie de página
        String fechaImpresion = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Paragraph footer = new Paragraph("\n\n\nINSIBO - " + fechaImpresion, footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private void addTableHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setBackgroundColor(new Color(0, 51, 102));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT); // Alinear a la izquierda
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        table.addCell(cell);
    }
}
