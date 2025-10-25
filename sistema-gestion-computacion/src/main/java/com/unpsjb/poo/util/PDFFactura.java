package com.unpsjb.poo.util;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.unpsjb.poo.model.ItemCarrito;
import com.unpsjb.poo.model.Venta;

/**
 * Genera un PDF de factura o ticket para una venta.
 */
public class PDFFactura extends PDFExporter {

    // Formato de fecha para el PDF
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    // Fuentes para el documento
    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font FONT_BOLD = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    
    // Venta a incluir en la factura
    private final Venta venta;
    
    // Constructor
    public PDFFactura(Venta venta) {
        this.venta = venta;
    }
    
    // Método para exportar el PDF
    @Override
    public boolean export(String filePath) {
        if (venta == null || venta.getCarrito() == null || venta.getCarrito().getItems().isEmpty()) {
            return false;
        }
        
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            addHeader(document);
            addClientData(document);
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
        
        String tipoDocumento = "FACTURA".equals(venta.getTipoFactura()) ? "FACTURA" : "TICKET DE VENTA";
        titulo.add(new Phrase(tipoDocumento + "\n\n", FONT_TITLE));
        document.add(titulo);
        
        // Información del negocio
        Paragraph infoBusiness = new Paragraph();
        infoBusiness.add(new Phrase("Sistema de Gestión de Computación\n", FONT_BOLD));
        infoBusiness.add(new Phrase("Dirección: Calle Principal 123\n", FONT_NORMAL));
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
    
    // Esto es para agregar los datos del cliente (solo si es factura)
    private void addClientData(Document document) throws DocumentException {
        if ("FACTURA".equals(venta.getTipoFactura()) && venta.getClienteFactura() != null) {
            Paragraph datosCliente = new Paragraph();
            datosCliente.add(new Phrase("DATOS DEL CLIENTE\n", FONT_BOLD));
            datosCliente.add(new Phrase("Nombre/Razón Social: " + venta.getClienteFactura().getNombre() + "\n", FONT_NORMAL));
            datosCliente.add(new Phrase("CUIT: " + venta.getClienteFactura().getCuit() + "\n", FONT_NORMAL));
            datosCliente.add(new Phrase("Tipo: " + venta.getClienteFactura().getTipo() + "\n\n", FONT_NORMAL));
            document.add(datosCliente);
        }
    }
    
    // Esto es para agregar la tabla de productos
    private void addProductTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(5); // Tabla con 5 columnas
        table.setWidthPercentage(100); // Ancho al 100%
        float[] widths = {1f, 3f, 1.5f, 1f, 1.5f}; // Anchos relativos de las columnas
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
        String[] headers = {"Código", "Producto", "Precio Unit.", "Cant.", "Subtotal"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FONT_BOLD));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
    
    // Esto es para agregar una fila con los datos de un producto
    private void addProductRow(PdfPTable table, ItemCarrito item) {
        table.addCell(new Phrase(String.valueOf(item.getProducto().getCodigoProducto()), FONT_NORMAL));
        table.addCell(new Phrase(item.getProducto().getNombreProducto(), FONT_NORMAL));
        
        PdfPCell precioCell = new PdfPCell(new Phrase("$ " + String.format("%.2f", item.getPrecioUnitario()), FONT_NORMAL));
        precioCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(precioCell);
        
        PdfPCell cantCell = new PdfPCell(new Phrase(String.valueOf(item.getCantidad()), FONT_NORMAL));
        cantCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cantCell);
        
        PdfPCell subtotalCell = new PdfPCell(new Phrase("$ " + String.format("%.2f", item.getSubtotal()), FONT_NORMAL));
        subtotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(subtotalCell);
    }
    
    // Esto es para agregar los totales de la factura
    private void addTotals(Document document) throws DocumentException {
        double totalCarrito = venta.getCarrito().getTotal().doubleValue();
        double comision = venta.getEstrategiaPago().getComision();
        double totalConComision = totalCarrito * (1 + comision);
        
        // Calcular IVA (21%)
        double subtotalSinIva = totalConComision / 1.21;
        double iva = totalConComision - subtotalSinIva;
        
        PdfPTable tableTotales = new PdfPTable(2);
        tableTotales.setWidthPercentage(50);
        tableTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
        float[] widthsTotales = {3f, 2f};
        tableTotales.setWidths(widthsTotales);
        
        // Subtotal sin IVA
        tableTotales.addCell(new Phrase("Subtotal sin IVA:", FONT_NORMAL));
        PdfPCell cellSubtotal = new PdfPCell(new Phrase("$ " + String.format("%.2f", subtotalSinIva), FONT_NORMAL));
        cellSubtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tableTotales.addCell(cellSubtotal);
        
        // IVA
        tableTotales.addCell(new Phrase("IVA (21%):", FONT_NORMAL));
        PdfPCell cellIva = new PdfPCell(new Phrase("$ " + String.format("%.2f", iva), FONT_NORMAL));
        cellIva.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tableTotales.addCell(cellIva);
        
        // Comisión si existe
        if (comision > 0) {
            tableTotales.addCell(new Phrase("Comisión (" + String.format("%.1f", comision * 100) + "%):", FONT_NORMAL));
            double montoComision = totalCarrito * comision;
            PdfPCell cellComision = new PdfPCell(new Phrase("$ " + String.format("%.2f", montoComision), FONT_NORMAL));
            cellComision.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableTotales.addCell(cellComision);
        }
        
        // Total final
        PdfPCell cellTotalLabel = new PdfPCell(new Phrase("TOTAL:", FONT_BOLD));
        cellTotalLabel.setBorder(PdfPCell.TOP);
        tableTotales.addCell(cellTotalLabel);
        
        PdfPCell cellTotal = new PdfPCell(new Phrase("$ " + String.format("%.2f", totalConComision), FONT_BOLD));
        cellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTotal.setBorder(PdfPCell.TOP);
        tableTotales.addCell(cellTotal);
        
        document.add(tableTotales);
    }
    
    // Esto es para agregar el pie de página
    private void addFooter(Document document) throws DocumentException {
        Paragraph pie = new Paragraph("\n\n");
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.add(new Phrase("¡Gracias por su compra!\n", FONT_BOLD));
        pie.add(new Phrase("Documento generado electrónicamente", FONT_NORMAL));
        document.add(pie);
    }
}
