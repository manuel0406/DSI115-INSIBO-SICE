package com.dsi.insibo.sice.Horario.PDF;

import java.awt.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

@Component("Horario/generarHorarioDocente")
public class HorarioDocentePdf extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<HorarioDTO> horasDeClase = (List<HorarioDTO>) model.get("horarioDTO");
        Docente docenteSeleccionado = (Docente) model.get("docenteSeleccionado");
        String titulo = docenteSeleccionado.getNombreDocente() + " " + docenteSeleccionado.getApellidoDocente() + ": Horario de clases";
        titulo = titulo.toUpperCase();

        // Configurar el nombre y tipo de documento
        response.setHeader("Content-Disposition", "inline; filename=" + "Horario de Clases - " + docenteSeleccionado.getNombreDocente() + " " + docenteSeleccionado.getApellidoDocente() + ".pdf");

        // Configurar tamaño y márgenes de la página
        document.setPageSize(PageSize.LETTER.rotate());
        document.setMargins(40, 40, 36, 72); // Margen de 3 cm a los lados y 2.5 cm abajo
        document.open();

        // Definir las fuentes que se usarán
        Font titleFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Color.DARK_GRAY);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY);

        // Crear una tabla para alinear la imagen y los títulos
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[] { 4, 1 }); // 80% para el título y 20% para la imagen

        // Añadir títulos
        Paragraph title = new Paragraph("INSTITUTO NACIONAL SIMON BOLIVAR\nCODIGO 11694 SANTO TOMAS", titleFont);
        // Paragraph title2 = new Paragraph("HORARIO DE CLASES DEL DOCENTE\n\n", titleFont2);
        Paragraph title2 = new Paragraph(titulo + "\n\n", titleFont2);

        title.setAlignment(Element.ALIGN_LEFT);
        title2.setAlignment(Element.ALIGN_LEFT);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.addElement(title);
        titleCell.addElement(title2);
        titleCell.setPaddingLeft(3);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerTable.addCell(titleCell);

        // Añadir logo
        URL logoUrl = getClass().getResource("/static/Imagenes/LogoINSIBO.jpg");
        if (logoUrl != null) {
            try {
                // Cargar la imagen desde la URL
                Image img = Image.getInstance(logoUrl);

                // Calcular la relación de aspecto de la imagen
                float aspectRatio = img.getWidth() / (float) img.getHeight();

                // Establecer el nuevo tamaño de la imagen (más pequeño)
                float newWidth = 53; // Tamaño del ancho ¿En pixeles?
                float newHeight = newWidth / aspectRatio; // Calcular altura manteniendo la relación de aspecto

                // Ajustar la imagen al nuevo tamaño
                img.scaleToFit(newWidth, newHeight);

                // Alinear la imagen a la derecha dentro de la celda
                img.setAlignment(Image.ALIGN_RIGHT);

                // Crear una celda de tabla para la imagen
                PdfPCell logoCell = new PdfPCell(img);
                logoCell.setBorder(PdfPCell.NO_BORDER);
                logoCell.setPadding(0);
                logoCell.setPaddingTop(4); // Margen superior para alinear la imagen con los titulos
                logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                headerTable.addCell(logoCell);

            } catch (Exception e) {
                System.err.println("Error loading or resizing image: " + e.getMessage());

                // Crear una celda vacía si la imagen no se puede cargar
                PdfPCell emptyCell = new PdfPCell();
                emptyCell.setBorder(PdfPCell.NO_BORDER);
                headerTable.addCell(emptyCell);
            }
        } else {
            // Manejar el caso donde la URL de la imagen no se encuentra
            System.err.println("Logo image not found at path: /static/Imagenes/LogoINSIBO.jpg");

            // Crear una celda vacía si la imagen no se encuentra
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(emptyCell);
        }

        document.add(headerTable);

        // Crear la tabla principal del horario con 7 columnas
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 2, 2, 2, 2, 2, 2, 2 });

        // Añadir encabezados de la tabla
        addTableHeader(table, "Hora", headerFont);
        addTableHeader(table, "Lunes", headerFont);
        addTableHeader(table, "Martes", headerFont);
        addTableHeader(table, "Miércoles", headerFont);
        addTableHeader(table, "Jueves", headerFont);
        addTableHeader(table, "Viernes", headerFont);
        addTableHeader(table, "Sábado", headerFont);

        // Añadir las filas del horario
        addScheduleRows(table, horasDeClase, bodyFont, subHeaderFont);
        // Añadir la tabla al documento
        document.add(table);

        // Añadir pie de página con la fecha de impresión
        // String fechaImpresion = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String fechaImpresion = new SimpleDateFormat("yyyy").format(new Date());
        Paragraph footer = new Paragraph("\n\n\nINSIBO - " + fechaImpresion, footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    // Método para añadir encabezados a la tabla
    private void addTableHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setBackgroundColor(new Color(0, 51, 102)); // Color azul oscuro
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }

    // Método para añadir sub-encabezados a la tabla
    private void addTableSubHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setBackgroundColor(new Color(227, 232, 243)); // Color gris claro
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }

    // Método para añadir celdas a la tabla
    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    // Método para añadir las filas del horario a la tabla
    private void addScheduleRows(PdfPTable table, List<HorarioDTO> horasDeClase, Font bodyFont, Font subHeaderFont) {
        addTableCell(table, "07:00 - 07:45 AM", bodyFont);
        addTableCell(table, hora(horasDeClase, "1"), bodyFont);
        addTableCell(table, hora(horasDeClase, "13"), bodyFont);
        addTableCell(table, hora(horasDeClase, "25"), bodyFont);
        addTableCell(table, hora(horasDeClase, "37"), bodyFont);
        addTableCell(table, hora(horasDeClase, "49"), bodyFont);
        addTableCell(table, hora(horasDeClase, "61"), bodyFont);

        addTableCell(table, "07:45 - 08:30 AM", bodyFont);
        addTableCell(table, hora(horasDeClase, "2"), bodyFont);
        addTableCell(table, hora(horasDeClase, "14"), bodyFont);
        addTableCell(table, hora(horasDeClase, "26"), bodyFont);
        addTableCell(table, hora(horasDeClase, "38"), bodyFont);
        addTableCell(table, hora(horasDeClase, "50"), bodyFont);
        addTableCell(table, hora(horasDeClase, "62"), bodyFont);

        addTableSubHeader(table, "08:30 - 08:50 AM", bodyFont);
        addTableSubHeader(table, "R", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "C", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "S", subHeaderFont);
        addTableSubHeader(table, "O", subHeaderFont);

        addTableCell(table, "08:50 - 09:35 AM", bodyFont);
        addTableCell(table, hora(horasDeClase, "3"), bodyFont);
        addTableCell(table, hora(horasDeClase, "15"), bodyFont);
        addTableCell(table, hora(horasDeClase, "27"), bodyFont);
        addTableCell(table, hora(horasDeClase, "39"), bodyFont);
        addTableCell(table, hora(horasDeClase, "51"), bodyFont);
        addTableCell(table, hora(horasDeClase, "63"), bodyFont);

        addTableCell(table, "09:35 - 10:20 AM", bodyFont);
        addTableCell(table, hora(horasDeClase, "4"), bodyFont);
        addTableCell(table, hora(horasDeClase, "16"), bodyFont);
        addTableCell(table, hora(horasDeClase, "28"), bodyFont);
        addTableCell(table, hora(horasDeClase, "40"), bodyFont);
        addTableCell(table, hora(horasDeClase, "52"), bodyFont);
        addTableCell(table, hora(horasDeClase, "64"), bodyFont);

        addTableSubHeader(table, "10:20 - 10:30 AM", bodyFont);
        addTableSubHeader(table, "R", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "C", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "S", subHeaderFont);
        addTableSubHeader(table, "O", subHeaderFont);

        addTableCell(table, "10:30 - 11:15 AM", bodyFont);
        addTableCell(table, hora(horasDeClase, "5"), bodyFont);
        addTableCell(table, hora(horasDeClase, "17"), bodyFont);
        addTableCell(table, hora(horasDeClase, "29"), bodyFont);
        addTableCell(table, hora(horasDeClase, "41"), bodyFont);
        addTableCell(table, hora(horasDeClase, "53"), bodyFont);
        addTableCell(table, hora(horasDeClase, "65"), bodyFont);

        addTableCell(table, "11:15 - 12:00 PM", bodyFont);
        addTableCell(table, hora(horasDeClase, "6"), bodyFont);
        addTableCell(table, hora(horasDeClase, "18"), bodyFont);
        addTableCell(table, hora(horasDeClase, "30"), bodyFont);
        addTableCell(table, hora(horasDeClase, "42"), bodyFont);
        addTableCell(table, hora(horasDeClase, "54"), bodyFont);
        addTableCell(table, hora(horasDeClase, "66"), bodyFont);

        addTableSubHeader(table, "12:00 - 01:00 PM", bodyFont);
        addTableSubHeader(table, "R", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "C", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "S", subHeaderFont);
        addTableSubHeader(table, "O", subHeaderFont);

        addTableCell(table, "01:00 - 01:45 PM", bodyFont);
        addTableCell(table, hora(horasDeClase, "7"), bodyFont);
        addTableCell(table, hora(horasDeClase, "19"), bodyFont);
        addTableCell(table, hora(horasDeClase, "31"), bodyFont);
        addTableCell(table, hora(horasDeClase, "43"), bodyFont);
        addTableCell(table, hora(horasDeClase, "55"), bodyFont);
        addTableCell(table, hora(horasDeClase, "67"), bodyFont);

        addTableCell(table, "01:45 - 02:30 PM", bodyFont);
        addTableCell(table, hora(horasDeClase, "8"), bodyFont);
        addTableCell(table, hora(horasDeClase, "20"), bodyFont);
        addTableCell(table, hora(horasDeClase, "32"), bodyFont);
        addTableCell(table, hora(horasDeClase, "44"), bodyFont);
        addTableCell(table, hora(horasDeClase, "56"), bodyFont);
        addTableCell(table, hora(horasDeClase, "68"), bodyFont);

        addTableSubHeader(table, "02:30 - 02:50 PM", bodyFont);
        addTableSubHeader(table, "R", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "C", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "S", subHeaderFont);
        addTableSubHeader(table, "O", subHeaderFont);

        addTableCell(table, "02:50 - 03:35 PM", bodyFont);
        addTableCell(table, hora(horasDeClase, "9"), bodyFont);
        addTableCell(table, hora(horasDeClase, "21"), bodyFont);
        addTableCell(table, hora(horasDeClase, "33"), bodyFont);
        addTableCell(table, hora(horasDeClase, "45"), bodyFont);
        addTableCell(table, hora(horasDeClase, "57"), bodyFont);
        addTableCell(table, hora(horasDeClase, "69"), bodyFont);

        addTableCell(table, "03:35 - 04:20 PM", bodyFont);
        addTableCell(table, hora(horasDeClase, "10"), bodyFont);
        addTableCell(table, hora(horasDeClase, "22"), bodyFont);
        addTableCell(table, hora(horasDeClase, "34"), bodyFont);
        addTableCell(table, hora(horasDeClase, "46"), bodyFont);
        addTableCell(table, hora(horasDeClase, "58"), bodyFont);
        addTableCell(table, hora(horasDeClase, "70"), bodyFont);

        addTableSubHeader(table, "04:20 - 04:30 PM", bodyFont);
        addTableSubHeader(table, "R", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "C", subHeaderFont);
        addTableSubHeader(table, "E", subHeaderFont);
        addTableSubHeader(table, "S", subHeaderFont);
        addTableSubHeader(table, "O", subHeaderFont);

        addTableCell(table, "04:30 - 05:15 PM", bodyFont);
        addTableCell(table, hora(horasDeClase, "11"), bodyFont);
        addTableCell(table, hora(horasDeClase, "23"), bodyFont);
        addTableCell(table, hora(horasDeClase, "35"), bodyFont);
        addTableCell(table, hora(horasDeClase, "47"), bodyFont);
        addTableCell(table, hora(horasDeClase, "59"), bodyFont);
        addTableCell(table, hora(horasDeClase, "71"), bodyFont);

        addTableCell(table, "05:15 - 06:00 PM", bodyFont);
        addTableCell(table, hora(horasDeClase, "12"), bodyFont);
        addTableCell(table, hora(horasDeClase, "24"), bodyFont);
        addTableCell(table, hora(horasDeClase, "36"), bodyFont);
        addTableCell(table, hora(horasDeClase, "48"), bodyFont);
        addTableCell(table, hora(horasDeClase, "60"), bodyFont);
        addTableCell(table, hora(horasDeClase, "72"), bodyFont);
    }

    // Método para extraer la información de la hora según su ID
    private String hora(List<HorarioDTO> hour, String id) {
        for (HorarioDTO horario : hour) {
            if (horario.getIdHorarioBase().equals(id)) {
                return horario.getNomMateria() + " \n" + horario.getCodigo() + "-" + horario.getGrado()
                        + horario.getSeccion();
            }
        }
        return " \n ";
    }

    // Método auxiliar para crear una celda vacía
/*     private PdfPCell createEmptyCell() {
        PdfPCell emptyCell = new PdfPCell();
        emptyCell.setBorder(PdfPCell.NO_BORDER);
        return emptyCell;
    } */
}
