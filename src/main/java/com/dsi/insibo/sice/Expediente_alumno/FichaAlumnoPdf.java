package com.dsi.insibo.sice.Expediente_alumno;

import java.awt.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import com.dsi.insibo.sice.entity.Alumno;
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

@Component("Expediente_alumno/editar")
public class FichaAlumnoPdf extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Alumno alumno = (Alumno) model.get("alumno");
        document.setPageSize(PageSize.LETTER);
        document.setMargins(85, 85, 36, 72); // Margen de 3 cm a los lados y 2.5 cm abajo
        document.open();

        // Fuentes
        Font titleFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, Color.GRAY);

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
        Paragraph title2 = new Paragraph("FICHA PERSONAL ALUMNO: DATOS PERSONALES\n\n", titleFont2);
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

        addTableCell(table, "NIE:", String.valueOf(alumno.getNie()), headerFont, bodyFont);
        addTableCell(table, "Año:", String.valueOf(alumno.getBachillerato().getGrado()), headerFont, bodyFont);
        addTableCell(table, "Especialidad en Estudio:", alumno.getBachillerato().getNombreCarrera(), headerFont,
                bodyFont);
        addTableCell(table, "Sección:", String.valueOf(alumno.getBachillerato().getSeccion()), headerFont, bodyFont);
        addTableCell(table, "Nombres:", alumno.getNombreAlumno(), headerFont, bodyFont);
        addTableCell(table, "Apellidos:", alumno.getApellidoAlumno(), headerFont, bodyFont);
        addTableCell(table, "Fecha de Nacimiento:", alumno.getFechaNacimientoAlumno().toString(), headerFont, bodyFont);
        addTableCell(table, "Dirección:", alumno.getDireccionAlumno(), headerFont, bodyFont);
        addTableCell(table, "DUI:", alumno.getDuiAlumno(), headerFont, bodyFont);
        addTableCell(table, "Correo Electrónico:", alumno.getCorreoAlumno(), headerFont, bodyFont);
        addTableCell(table, "Teléfono:", alumno.getTelefonoAlumno(), headerFont, bodyFont);

        cell = new PdfPCell(new Phrase("Padecimientos", headerFont));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.setBackgroundColor(new Color(230, 230, 230));
        table.addCell(cell);        
        
        addTableCell(table, "Enfermedad:", alumno.getPadecimientos(), headerFont, bodyFont);
        addTableCell(table, "Medicamento:", alumno.getMedicamento(), headerFont, bodyFont);
        addTableCell(table, "Forma de medicación:", alumno.getFormaMedicacion(), headerFont, bodyFont);
        
        
        cell = new PdfPCell(new Phrase("Datos del encargado", headerFont));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.setBackgroundColor(new Color(230, 230, 230));
        table.addCell(cell);        
        
        addTableCell(table, "Nombres:", alumno.getNombreEncargado(), headerFont, bodyFont);
        addTableCell(table, "Apellidos:", alumno.getApellidoEncargado(), headerFont, bodyFont);
        addTableCell(table, "DUI:", alumno.getDuiEncargado(), headerFont, bodyFont);
        addTableCell(table, "Teléfono:", alumno.getTelefonoEncargado(), headerFont, bodyFont);
        addTableCell(table, "Correo Electrónico:", alumno.getCorreoEncargado(), headerFont, bodyFont);        
        addTableCell(table, "Lugar de trabajo:", alumno.getLugarDeTrabajo(), headerFont, bodyFont);
        
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
