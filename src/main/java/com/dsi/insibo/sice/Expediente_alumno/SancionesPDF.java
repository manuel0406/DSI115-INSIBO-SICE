package com.dsi.insibo.sice.Expediente_alumno;

import java.awt.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Sancion;
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

@Component("Expediente_alumno/AlumnoSanciones")
public class SancionesPDF  extends AbstractPdfView{

    @Override
    protected void buildPdfDocument(@NonNull Map<String, Object> model, @NonNull Document document, @NonNull PdfWriter writer,
    @NonNull HttpServletRequest request,@NonNull HttpServletResponse response) throws Exception {

                @SuppressWarnings("unchecked")
                List<Sancion> listaSanciones= (List<Sancion>) model.get("listaSanciones");
                Alumno alumno = (Alumno) model.get("alumno") ;

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
       Paragraph tituloNombre = new Paragraph("Nombre:", titleFont2);
       Paragraph nombreAlumno = new Paragraph(alumno.getNombreAlumno() +" "+alumno.getApellidoAlumno(), titleFont3);
        
       PdfPTable headerTable2 = new PdfPTable(2); // 6 columnas
       headerTable2.setWidthPercentage(100);
       headerTable2.setWidths(new float[] { 0.15f, 1.5f});

       PdfPCell cellnombre = new PdfPCell(tituloNombre);
       cellnombre.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cellnombreB = new PdfPCell(nombreAlumno);
        cellnombreB.setBorder(PdfPCell.NO_BORDER);

        headerTable2.addCell(cellnombre);
        headerTable2.addCell(cellnombreB);
        headerTable2.setSpacingAfter(6f); // Ajusta el valor para el espacio deseado
        document.add(headerTable2);

        // Tabla de Alumnos
        PdfPTable tablaSanciones = new PdfPTable(6); // Añadir una columna más para el número de registro
        tablaSanciones.setWidthPercentage(100);
        tablaSanciones.setWidths(new float[] { 0.3f, 0.8f, 0.8f, 2f, 3f, 1.5f }); // Anchos relativos de las columnas

        // Encabezados de la tabla de Alumnos
        addTableHeader(tablaSanciones, "N.º", headerFont);
        addTableHeader(tablaSanciones, "Fecha", headerFont);
        addTableHeader(tablaSanciones, "Tipo Falta", headerFont);
        addTableHeader(tablaSanciones, "Descripcion", headerFont);
        addTableHeader(tablaSanciones, "Acción Correctiva", headerFont);
        addTableHeader(tablaSanciones, "Firma", headerFont);
       

        // Datos de los docentes
        int registro = 1;

        for (Sancion sancion : listaSanciones) {
            String fechaImpresion = new SimpleDateFormat("dd/MM/yyyy").format(sancion.getFechaSancion());
            addTableCell(tablaSanciones, String.valueOf(registro++), bodyFont, Element.ALIGN_CENTER,
                    Element.ALIGN_JUSTIFIED); // Número de registro centrado
            addTableCell(tablaSanciones, fechaImpresion , bodyFont, Element.ALIGN_LEFT,
                    Element.ALIGN_JUSTIFIED); // fecha
            addTableCell(tablaSanciones, sancion.getTipoSancion(), bodyFont, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED);    //tipo de sancion         
            addTableCell(tablaSanciones, sancion.getDescripcionSancion(), bodyFont, Element.ALIGN_JUSTIFIED,
                    Element.ALIGN_JUSTIFIED); // Descripcion de la sancion
            addTableCell(tablaSanciones, sancion.getAccionCorrectiva(), bodyFont, Element.ALIGN_JUSTIFIED,
                    Element.ALIGN_JUSTIFIED); // accion correctiva
            addTableCell(tablaSanciones, "F._______________", bodyFont,
                    Element.ALIGN_CENTER, Element.ALIGN_MIDDLE); // Año centrado
        }

        document.add(tablaSanciones);

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
