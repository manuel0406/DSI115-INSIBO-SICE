package com.dsi.insibo.sice.Biblioteca;

import com.dsi.insibo.sice.entity.InventarioPapeleria;
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
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("Biblioteca/verPapeleriaPdf")
public class ListarPapeleriaPdf extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(@NonNull Map<String, Object> model, @NonNull Document document, @NonNull PdfWriter writer,
                                    @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<InventarioPapeleria> listadoProductos = (List<InventarioPapeleria>) model.get("productos");

        document.setPageSize(PageSize.LETTER.rotate());
        document.setMargins(40, 40, 36, 72); // Margen de 3 cm a los lados y 2.5 cm abajo
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
        Font titleFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY);

        // Crear una tabla para alinear la imagen y los títulos
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(50);
        headerTable.setWidths(new float[] { 0.55f, 1.75f }); // Anchos relativos de las columnas (1: imagen, 3: títulos)

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
        Paragraph title2 = new Paragraph("INVENTARIO DE PAPALERÍA INSTITUCIONAL\n\n", titleFont2);
        title.setAlignment(Element.ALIGN_LEFT);
        title2.setAlignment(Element.ALIGN_LEFT);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.addElement(title);
        titleCell.addElement(title2);
        headerTable.addCell(titleCell);

        document.add(headerTable);


    // Crear tabla para el inventario de papelería (5 columnas)
    PdfPTable tablaPapeleria = new PdfPTable(4); // 5 columnas para los datos del inventario
    tablaPapeleria.setWidthPercentage(100);
    tablaPapeleria.setWidths(new float[] { 0.5f, 3f, 4f, 1.5f});

    // Encabezados de la tabla
    addTableHeader(tablaPapeleria, "Nº", headerFont);
    addTableHeader(tablaPapeleria, "Nombre", headerFont);
    addTableHeader(tablaPapeleria, "Descripción", headerFont);
    addTableHeader(tablaPapeleria, "Existencia", headerFont);

    // Datos del inventario
    int correlativo = 1; // Contador para el número correlativo
    for (InventarioPapeleria producto : listadoProductos) {
        addTableCell(tablaPapeleria, String.valueOf(correlativo), bodyFont);
        addTableCell(tablaPapeleria, producto.getNombreArticulo(), bodyFont);
        addTableCell(tablaPapeleria, producto.getDescripcionArticulo(), bodyFont);
        addTableCell(tablaPapeleria, String.valueOf(producto.getExistenciaArticulo()), bodyFont, Element.ALIGN_CENTER);
        correlativo++;
    }

    document.add(tablaPapeleria);

        // Añadir pie de página
        String fechaImpresion = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Paragraph footer = new Paragraph("\nINSIBO - " + fechaImpresion, footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private void addTableHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setBackgroundColor(new Color(0, 51, 102));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        table.addCell(cell);
    }
}