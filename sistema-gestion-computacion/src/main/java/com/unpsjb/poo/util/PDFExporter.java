package com.unpsjb.poo.util;

/**
 * Clase base abstracta para exportar información a PDF.
 * Las subclases concretas (como PDFReporte o PDFTicket)
 * definen la estructura específica de su documento.
 */
public abstract class PDFExporter {

    /**
     * Genera un archivo PDF en la ruta indicada.
     * @param filePath Ruta destino (con extensión .pdf)
     * @return true si el PDF se generó correctamente.
     */
    public abstract boolean export(String filePath);
}
