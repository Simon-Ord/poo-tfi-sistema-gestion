package com.unpsjb.poo.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.unpsjb.poo.model.AuditEvent;

/**
 * Clase utilitaria para exportar eventos de auditoría (reportes)
 * a un archivo CSV legible por Excel, LibreOffice o Google Sheets.
 */
public class CSVExporter {

    /**
     * Exporta una lista de eventos de auditoría a un archivo CSV.
     *
     * @param eventos Lista de eventos (AuditEvent)
     * @param filePath Ruta del archivo destino (.csv)
     * @return true si se exportó correctamente, false si ocurrió un error
     */
    public static boolean exportAuditEventsToCSV(List<AuditEvent> eventos, String filePath) {
        if (eventos == null || eventos.isEmpty()) {
            return false;
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            // Escribir encabezado
            writer.append("Fecha,Usuario,Acción,Entidad,Detalles\n");

            // Escribir los registros
            for (AuditEvent e : eventos) {
                writer.append(String.format("%s,%s,%s,%s,%s\n",
                        escape(String.valueOf(e.getOccurredAt())),
                        escape(e.getUsername()),
                        escape(e.getAction()),
                        escape(e.getEntity()),
                        escape(e.getDetails())));
            }

            writer.flush();
            return true;
        } catch (IOException ex) {
            System.err.println("Error al exportar CSV: " + ex.getMessage());
            return false;
        }
    }

    // Evita errores con comas o saltos de línea
    private static String escape(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
