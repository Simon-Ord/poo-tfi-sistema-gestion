package com.unpsjb.poo.util;

import com.unpsjb.poo.model.EventoAuditoria;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Clase utilitaria para exportar eventos de auditoría (reportes)
 * a un archivo PDF usando la librería iText.
 */
public class PDFExporter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Exporta una lista de eventos de auditoría a un archivo PDF.
     *
     * @param eventos Lista de eventos (EventoAuditoria)
     * @param filePath Ruta del archivo destino (.pdf)
     * @return true si se exportó correctamente, false si ocurrió un error
     */
    public static boolean exportAuditEventsToPDF(List<EventoAuditoria> eventos, String filePath) {
        if (eventos == null || eventos.isEmpty()) {
            return false;
        }

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Título del documento
            document.add(new Paragraph("Reporte de Auditoría del Sistema\n\n"));
            
            // Crear la tabla con 5 columnas
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            
            // Definir anchos relativos de las columnas (ajuste visual)
            float[] widths = {2f, 1.5f, 1.5f, 1f, 4f};
            table.setWidths(widths);

            // Agregar encabezados
            addTableHeader(table);

            // Agregar datos
            for (EventoAuditoria e : eventos) {
                addDataRow(table, e);
            }

            document.add(table);
            document.close();
            return true;

        } catch (Exception ex) {
            System.err.println("Error al exportar PDF: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    // Método auxiliar para añadir los encabezados de la tabla
    private static void addTableHeader(PdfPTable table) {
        String[] headers = {"Fecha y Hora", "Usuario", "Acción", "Entidad", "Detalles"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            // Opcional: añadir estilos básicos a la celda del encabezado
            // cell.setBackgroundColor(BaseColor.LIGHT_GRAY); 
            table.addCell(cell);
        }
    }

    // Método auxiliar para añadir una fila de datos
    private static void addDataRow(PdfPTable table, EventoAuditoria e) {
        // Formatear la fecha
        String fecha = e.getFechaHora() != null ? DATE_FORMAT.format(e.getFechaHora()) : "";

        table.addCell(new Phrase(fecha));
        table.addCell(new Phrase(e.getUsuario()));
        table.addCell(new Phrase(e.getAccion()));
        table.addCell(new Phrase(e.getEntidad()));
        table.addCell(new Phrase(e.getDetalles()));
    }
}