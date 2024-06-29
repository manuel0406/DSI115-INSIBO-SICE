package com.dsi.insibo.sice.Expediente_alumno;

import java.util.Map;
import java.awt.Color;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("Expediente_alumno/verAlumnoPdf")
public class ListaAlumnosPdf extends AbstractPdfView {

   
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        @SuppressWarnings("unchecked")
        List<Alumno> listaAlumnos = (List<Alumno>) model.get("alumnos");
        String carrera = (String) model.get("carrera");
        String grado = (String) model.get("grado");
        String seccion = (String) model.get("seccion");

        document.setPageSize(PageSize.LETTER.rotate());
        document.setMargins(40, 40, 36, 72); // Margen de 3 cm a los lados y 2.5 cm abajo
        document.open();

        // Fuentes
        Font titleFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
        Font titleFont3 = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE,Color.BLACK);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
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
        Paragraph title2 = new Paragraph("LISTADO GENERAL DE ALUMNOS\n\n", titleFont2);
        title.setAlignment(Element.ALIGN_LEFT);
        title2.setAlignment(Element.ALIGN_LEFT);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.addElement(title);
        titleCell.addElement(title2);
        // titleCell.addElement(title3);
        headerTable.addCell(titleCell);

        document.add(headerTable);

        // Titulo segun el filtro
        Paragraph tituloCarrera = new Paragraph("Carrera:", titleFont2);
        Paragraph tituloCarreraF = new Paragraph(carrera, titleFont3);
        Paragraph tituloSeccion = new Paragraph("Sección:", titleFont2);
        Paragraph tituloSeccionF = new Paragraph(seccion, titleFont3);
        Paragraph tituloGrado = new Paragraph("Año:", titleFont2);
        Paragraph tituloGradoF =null;
        if (grado==null) {
            grado=" ";
            tituloGradoF = new Paragraph(grado, titleFont3);
        }else{
            tituloGradoF = new Paragraph(grado+"°\n", titleFont3);  
        }
       

        PdfPTable headerTable2 = new PdfPTable(6); // 6 columnas
        headerTable2.setWidthPercentage(100);
        headerTable2.setWidths(new float[] { 0.1f, 0.1f, 0.15f, 0.5f, 0.19f, 1.2f}); // Anchos relativos de las columnas

        PdfPCell cellCarrera = new PdfPCell(tituloCarrera);
        cellCarrera.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cellCarreraF = new PdfPCell(tituloCarreraF);
        cellCarreraF.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cellSeccion = new PdfPCell(tituloSeccion);
        cellSeccion.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cellSeccionF = new PdfPCell(tituloSeccionF);
        cellSeccionF.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cellGrado = new PdfPCell(tituloGrado);
        cellGrado.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cellGradoF = new PdfPCell(tituloGradoF);
        cellGradoF.setBorder(PdfPCell.NO_BORDER);
       
        headerTable2.addCell(cellGrado);
        headerTable2.addCell(cellGradoF);
        headerTable2.addCell(cellCarrera);
        headerTable2.addCell(cellCarreraF);
        headerTable2.addCell(cellSeccion);
        headerTable2.addCell(cellSeccionF);               
        headerTable2.setSpacingAfter(6f); // Ajusta el valor para el espacio deseado
        document.add(headerTable2);
        // FIN

        // Tabla de Alumnos
        PdfPTable tablaAlumnos = new PdfPTable(7); // Añadir una columna más para el número de registro
        tablaAlumnos.setWidthPercentage(100);
        tablaAlumnos.setWidths(new float[] { 0.5f, 1f, 2f, 2f, 3f, 1f, 0.7f }); // Anchos relativos de las columnas

        // Encabezados de la tabla de Alumnos
        addTableHeader(tablaAlumnos, "N.º", headerFont);
        addTableHeader(tablaAlumnos, "NIE", headerFont);
        addTableHeader(tablaAlumnos, "Apellidos", headerFont);
        addTableHeader(tablaAlumnos, "Nombres", headerFont);
        addTableHeader(tablaAlumnos, "Especialidad", headerFont);
        addTableHeader(tablaAlumnos, "Sección", headerFont);
        addTableHeader(tablaAlumnos, "Año", headerFont);

        // Datos de los docentes
        int registro = 1;

        for (Alumno alumno : listaAlumnos) {
            addTableCell(tablaAlumnos, String.valueOf(registro++), bodyFont, Element.ALIGN_CENTER,
                    Element.ALIGN_MIDDLE); // Número de registro centrado
            addTableCell(tablaAlumnos, String.valueOf(alumno.getNie()), bodyFont, Element.ALIGN_LEFT,
                    Element.ALIGN_MIDDLE); // NIE alineado a la izquierda
            addTableCell(tablaAlumnos, alumno.getApellidoAlumno(), bodyFont, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE); // Apellidos
                                                                                                                        // alineados
                                                                                                                        // a
                                                                                                                        // la
                                                                                                                        // izquierda
            addTableCell(tablaAlumnos, alumno.getNombreAlumno(), bodyFont, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE); // Nombres
                                                                                                                      // alineados
                                                                                                                      // a
                                                                                                                      // la
                                                                                                                      // izquierda
            addTableCell(tablaAlumnos, alumno.getBachillerato().getNombreCarrera(), bodyFont, Element.ALIGN_LEFT,
                    Element.ALIGN_MIDDLE); // Especialidad alineada a la izquierda
            addTableCell(tablaAlumnos, alumno.getBachillerato().getSeccion(), bodyFont, Element.ALIGN_CENTER,
                    Element.ALIGN_MIDDLE); // Sección centrada
            addTableCell(tablaAlumnos, String.valueOf(alumno.getBachillerato().getGrado()), bodyFont,
                    Element.ALIGN_CENTER, Element.ALIGN_MIDDLE); // Año centrado
        }

        document.add(tablaAlumnos);

        // Pie de página
        String fechaImpresion = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Paragraph footer = new Paragraph("\n\n\nINSIBO - " + fechaImpresion, footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private void addTableHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setBackgroundColor(new Color(0, 51, 102));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Alinear a la izquierda
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, Font font, int horizontalAlignment, int verticalAlignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        table.addCell(cell);
    }

}
