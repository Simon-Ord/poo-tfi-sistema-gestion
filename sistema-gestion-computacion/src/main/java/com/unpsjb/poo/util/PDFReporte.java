package com.unpsjb.poo.util;

import com.unpsjb.poo.model.EventoAuditoria;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Genera un PDF con los registros de auditoría del sistema.
 */
public class PDFReporte extends PDFExporter {

// Formato de fecha para el PDF
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// Formato de fecha para el PDF
    private final List<EventoAuditoria> eventos;// Lista de eventos a incluir en el reporte
// Constructor
    public PDFReporte(List<EventoAuditoria> eventos) {
        this.eventos = eventos;// Lista de eventos a incluir en el reporte
    }
// Método para exportar el PDF
    @Override
    public boolean export(String filePath) {
        if (eventos == null || eventos.isEmpty()) return false;

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph(" Reporte de Auditoría del Sistema\n\n"));

            PdfPTable table = new PdfPTable(5);// Tabla con 5 columnas
            table.setWidthPercentage(100);// Ancho al 100%
            float[] widths = {2f, 1.5f, 1.5f, 1.2f, 3f};// Anchos relativos de las columnas
            table.setWidths(widths);// Establecer los anchos de las columnas

            addHeader(table);
// Agregar filas de datos
            for (EventoAuditoria e : eventos) {
                addRow(table, e);
            }
// Agregar tabla al documento
            document.add(table);
            document.close();
            return true;
// Manejo de excepciones
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
// esto es para agregar el encabezado de la tabla
    private void addHeader(PdfPTable table) {
        String[] headers = {"Fecha y Hora", "Usuario", "Acción", "Entidad", "Detalles"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h));
            table.addCell(cell);
        }
    }
// esto es para agregar una fila con los datos de un evento
    private void addRow(PdfPTable table, EventoAuditoria e) {
        table.addCell(DATE_FORMAT.format(e.getFechaHora()));
        table.addCell(e.getUsuario());
        table.addCell(e.getAccion());
        table.addCell(e.getEntidad());
        table.addCell(e.getDetalles());
    }
}
