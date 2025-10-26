package com.unpsjb.poo.util.Exporter_pdf;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.unpsjb.poo.model.ItemCarrito;
import com.unpsjb.poo.model.Venta;

/**
 * Genera un PDF de ticket para una venta (sin datos del cliente).
 */
public class PDFTicket extends PDFExporter {

    // Formato de fecha para el PDF
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    // Fuentes para el documento
    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static final Font FONT_BOLD = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    
    // Venta a incluir en el ticket
    private final Venta venta;

    // Constructor
    public PDFTicket(Venta venta) {
        this.venta = venta;
    }

    // Método para exportar el PDF
    @Override
    public boolean export(String filePath) {
        if (venta == null || venta.getCarrito() == null || venta.getCarrito().getItems().isEmpty()) {
            return false;
        }

        Document document = new Document(PageSize.A6, 20, 20, 40, 20);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            addHeader(document);
            addProductTable(document);
            addTotals(document);
            addFooter(document);

            document.close();
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    // Esto es para agregar el encabezado del documento
    private void addHeader(Document document) throws DocumentException {
        // Título del documento
        Paragraph titulo = new Paragraph();
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.add(new Phrase("TICKET DE VENTA\n\n", FONT_TITLE));
        document.add(titulo);
        
        // Información del negocio
        Paragraph infoBusiness = new Paragraph();
        infoBusiness.setAlignment(Element.ALIGN_CENTER);
        infoBusiness.add(new Phrase("Sistema de Gestión de Computación\n", FONT_BOLD));
        infoBusiness.add(new Phrase("Tel: (0280) 123-4567\n\n", FONT_NORMAL));
        document.add(infoBusiness);
        
        // Fecha y número de venta
        Paragraph infoVenta = new Paragraph();
        infoVenta.add(new Phrase("Fecha: " + DATE_FORMAT.format(new Date()) + "\n", FONT_NORMAL));
        if (venta.getIdVenta() > 0) {
            infoVenta.add(new Phrase("Nº de Venta: " + venta.getIdVenta() + "\n", FONT_NORMAL));
        }
        infoVenta.add(new Phrase("Método de Pago: " + venta.getEstrategiaPago().getNombreMetodoPago() + "\n\n", FONT_NORMAL));
        document.add(infoVenta);
    }
    
    // Esto es para agregar la tabla de productos
    private void addProductTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4); // Tabla con 4 columnas
        table.setWidthPercentage(100); // Ancho al 100%
        float[] widths = {2f, 1f, 1f, 1.5f}; // Anchos relativos de las columnas
        table.setWidths(widths); // Establecer los anchos de las columnas
        
        addProductTableHeader(table);
        
        // Agregar filas de datos
        for (ItemCarrito item : venta.getCarrito().getItems()) {
            addProductRow(table, item);
        }
        
        document.add(table);
        document.add(new Paragraph("\n"));
    }
    
    // Esto es para agregar el encabezado de la tabla de productos
    private void addProductTableHeader(PdfPTable table) {
        String[] headers = {"Producto", "Precio", "Cant.", "Subtotal"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FONT_BOLD));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
    
    // Esto es para agregar una fila con los datos de un producto
    private void addProductRow(PdfPTable table, ItemCarrito item) {
        table.addCell(new Phrase(item.getProducto().getNombreProducto(), FONT_NORMAL));
        
        PdfPCell precioCell = new PdfPCell(new Phrase("$" + String.format("%.2f", item.getPrecioUnitario()), FONT_NORMAL));
        precioCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(precioCell);
        
        PdfPCell cantCell = new PdfPCell(new Phrase(String.valueOf(item.getCantidad()), FONT_NORMAL));
        cantCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cantCell);
        
        PdfPCell subtotalCell = new PdfPCell(new Phrase("$" + String.format("%.2f", item.getSubtotal()), FONT_NORMAL));
        subtotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(subtotalCell);
    }
    
    // Esto es para agregar los totales del ticket
    private void addTotals(Document document) throws DocumentException {
        double totalCarrito = venta.getCarrito().getTotal().doubleValue();
        double comision = venta.getEstrategiaPago().getComision();
        double totalConComision = totalCarrito * (1 + comision);
        
        // Calcular IVA (21%)
        double subtotalSinIva = totalConComision / 1.21;
        double iva = totalConComision - subtotalSinIva;
        
        // Solo mostrar totales esenciales en el ticket
        Paragraph totales = new Paragraph();
        totales.setAlignment(Element.ALIGN_RIGHT);
        totales.add(new Phrase("Subtotal: $" + String.format("%.2f", subtotalSinIva) + "\n", FONT_NORMAL));
        totales.add(new Phrase("IVA (21%): $" + String.format("%.2f", iva) + "\n", FONT_NORMAL));
        
        if (comision > 0) {
            double montoComision = totalCarrito * comision;
            totales.add(new Phrase("Comisión: $" + String.format("%.2f", montoComision) + "\n", FONT_NORMAL));
        }
        
        totales.add(new Phrase("TOTAL: $" + String.format("%.2f", totalConComision) + "\n", FONT_BOLD));
        document.add(totales);
    }
    
    // Esto es para agregar el pie de página
    private void addFooter(Document document) throws DocumentException {
        Paragraph pie = new Paragraph("\n");
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.add(new Phrase("¡Gracias por comprar en MUNDO PC!\n", FONT_BOLD));
        pie.add(new Phrase("Consumidor Final", FONT_NORMAL));
        document.add(pie);
    }
}
