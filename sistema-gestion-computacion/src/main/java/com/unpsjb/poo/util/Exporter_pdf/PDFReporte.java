package com.unpsjb.poo.util.Exporter_pdf;

import com.unpsjb.poo.model.EventoAuditoria;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;


public class PDFReporte extends PDFExporter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final List<EventoAuditoria> eventos;

    public PDFReporte(List<EventoAuditoria> eventos) {
        this.eventos = eventos;
    }

    @Override
    public boolean export(String filePath) {
        if (eventos == null || eventos.isEmpty()) return false;

        Document document = new Document(PageSize.A4.rotate(), 30, 30, 40, 30); // horizontal para más espacio
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Título del documento
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
            Paragraph title = new Paragraph("Reporte de Auditoría de MUNDO PC", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Fecha de generación
            Font infoFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
            Paragraph fecha = new Paragraph("Generado el: " + DATE_FORMAT.format(new java.util.Date()), infoFont);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            fecha.setSpacingAfter(10);
            document.add(fecha);

            //  Crear tabla principal
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            float[] widths = {2f, 1.5f, 1.5f, 1.2f, 4f};
            table.setWidths(widths);

            addHeader(table);

            // Agregar filas
            for (EventoAuditoria e : eventos) {
                addRow(table, e);
            }

            document.add(table);

            document.close();
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 🔹 Encabezado con estilo
     */
    private void addHeader(PdfPTable table) {
        String[] headers = {"Fecha y Hora", "Usuario", "Acción", "Entidad", "Descripción / Detalles"};
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(60, 120, 200); // azul oscuro

        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            cell.setBorderColor(BaseColor.GRAY);
            table.addCell(cell);
        }
    }

    /**
     * 🔹 Fila con estilo alternado (blanco / gris claro)
     */
    private void addRow(PdfPTable table, EventoAuditoria e) {
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
        BaseColor rowColor = (table.getRows().size() % 2 == 0)
                ? new BaseColor(245, 245, 245) 
                : BaseColor.WHITE;

        PdfPCell c1 = new PdfPCell(new Phrase(DATE_FORMAT.format(e.getFechaHora()), normalFont));
        PdfPCell c2 = new PdfPCell(new Phrase(e.getUsuario(), normalFont));
        PdfPCell c3 = new PdfPCell(new Phrase(e.getAccion(), normalFont));
        PdfPCell c4 = new PdfPCell(new Phrase(e.getEntidad(), normalFont));
        PdfPCell c5 = new PdfPCell(new Phrase(e.getDetalles(), normalFont));

        PdfPCell[] cells = {c1, c2, c3, c4, c5};
        for (PdfPCell c : cells) {
            c.setPadding(6);
            c.setBackgroundColor(rowColor);
            c.setBorderColor(BaseColor.LIGHT_GRAY);
            table.addCell(c);
        }
    }
}
