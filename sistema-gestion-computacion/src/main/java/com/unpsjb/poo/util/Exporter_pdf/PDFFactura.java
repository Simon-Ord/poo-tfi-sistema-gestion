package com.unpsjb.poo.util.Exporter_pdf;

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
 * Genera un PDF de factura para una venta.
 */
public class PDFFactura extends PDFExportar {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font FONT_BOLD = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    private final Venta venta;

    public PDFFactura(Venta venta) {
        this.venta = venta;
    }

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

    // ============================= ENCABEZADO =============================
    private void addHeader(Document document) throws DocumentException {
        // Título principal con nombre de la empresa
        Paragraph titulo = new Paragraph("MUNDO PC", FONT_TITLE);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        // Información de la empresa
        Paragraph infoBusiness = new Paragraph();
        infoBusiness.setAlignment(Element.ALIGN_CENTER);
        infoBusiness.add(new Phrase("Av. San Martín 1234 - Comodoro Rivadavia\n", FONT_NORMAL));
        infoBusiness.add(new Phrase("Tel: (0280) 123-4567\n\n", FONT_NORMAL));
        document.add(infoBusiness);

        // Tipo de documento
        String tipoDocumento = "FACTURA".equals(venta.getTipoFactura()) ? "FACTURA" : "TICKET DE VENTA";
        Paragraph tipoDoc = new Paragraph(tipoDocumento + "\n", FONT_BOLD);
        tipoDoc.setAlignment(Element.ALIGN_CENTER);
        document.add(tipoDoc);

        // Información de la venta
        Paragraph infoVenta = new Paragraph();
        infoVenta.add(new Phrase("Fecha: " + DATE_FORMAT.format(new Date()) + "\n", FONT_NORMAL));

        if (venta.getCodigoVenta() != null && !venta.getCodigoVenta().isEmpty()) {
            infoVenta.add(new Phrase("Código de Venta: " + venta.getCodigoVenta() + "\n", FONT_NORMAL));
        }

        infoVenta.add(new Phrase("Método de Pago: " + venta.getEstrategiaPago().getNombreMetodoPago() + "\n\n", FONT_NORMAL));
        document.add(infoVenta);
    }

    // ============================= DATOS CLIENTE =============================
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

    // ============================= TABLA PRODUCTOS =============================
    private void addProductTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        float[] widths = {1f, 3f, 1.5f, 1f, 1.5f};
        table.setWidths(widths);

        addProductTableHeader(table);

        for (ItemCarrito item : venta.getCarrito().getItems()) {
            addProductRow(table, item);
        }

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    private void addProductTableHeader(PdfPTable table) {
        String[] headers = {"Código", "Producto", "Precio Unit.", "Cant.", "Subtotal"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FONT_BOLD));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5f);
            cell.setBackgroundColor(new com.itextpdf.text.BaseColor(240, 240, 240));
            table.addCell(cell);
        }
    }

    private void addProductRow(PdfPTable table, ItemCarrito item) {
        PdfPCell codigoCell = new PdfPCell(new Phrase(String.valueOf(item.getProducto().getCodigoProducto()), FONT_NORMAL));
        codigoCell.setPadding(4f);
        codigoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(codigoCell);

        PdfPCell nombreCell = new PdfPCell(new Phrase(item.getProducto().getNombreProducto(), FONT_NORMAL));
        nombreCell.setPadding(4f);
        table.addCell(nombreCell);

        PdfPCell precioCell = new PdfPCell(new Phrase("$ " + String.format("%.2f", item.getPrecioUnitario()), FONT_NORMAL));
        precioCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        precioCell.setPadding(4f);
        table.addCell(precioCell);

        PdfPCell cantCell = new PdfPCell(new Phrase(String.valueOf(item.getCantidad()), FONT_NORMAL));
        cantCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cantCell.setPadding(4f);
        table.addCell(cantCell);

        PdfPCell subtotalCell = new PdfPCell(new Phrase("$ " + String.format("%.2f", item.getSubtotal()), FONT_NORMAL));
        subtotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subtotalCell.setPadding(4f);
        table.addCell(subtotalCell);
    }

    // ============================= TOTALES =============================
    private void addTotals(Document document) throws DocumentException {
        double totalCarrito = venta.getCarrito().getTotal().doubleValue();
        double comision = venta.getEstrategiaPago().getComision();
        double totalConComision = totalCarrito * (1 + comision);

        double subtotalSinIva = totalConComision / 1.21;
        double iva = totalConComision - subtotalSinIva;

        PdfPTable tableTotales = new PdfPTable(2);
        tableTotales.setWidthPercentage(50);
        tableTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
        float[] widthsTotales = {3f, 2f};
        tableTotales.setWidths(widthsTotales);

        // Subtotal sin IVA
        PdfPCell labelSubtotal = new PdfPCell(new Phrase("Subtotal sin IVA:", FONT_NORMAL));
        labelSubtotal.setBorder(PdfPCell.NO_BORDER);
        labelSubtotal.setPadding(3f);
        tableTotales.addCell(labelSubtotal);

        PdfPCell cellSubtotal = new PdfPCell(new Phrase("$ " + String.format("%.2f", subtotalSinIva), FONT_NORMAL));
        cellSubtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSubtotal.setBorder(PdfPCell.NO_BORDER);
        cellSubtotal.setPadding(3f);
        tableTotales.addCell(cellSubtotal);

        // IVA
        PdfPCell labelIva = new PdfPCell(new Phrase("IVA (21%):", FONT_NORMAL));
        labelIva.setBorder(PdfPCell.NO_BORDER);
        labelIva.setPadding(3f);
        tableTotales.addCell(labelIva);

        PdfPCell cellIva = new PdfPCell(new Phrase("$ " + String.format("%.2f", iva), FONT_NORMAL));
        cellIva.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellIva.setBorder(PdfPCell.NO_BORDER);
        cellIva.setPadding(3f);
        tableTotales.addCell(cellIva);

        // Comisión si existe
        if (comision > 0) {
            PdfPCell labelComision = new PdfPCell(new Phrase("Comisión (" + String.format("%.1f", comision * 100) + "%):", FONT_NORMAL));
            labelComision.setBorder(PdfPCell.NO_BORDER);
            labelComision.setPadding(3f);
            tableTotales.addCell(labelComision);

            double montoComision = totalCarrito * comision;
            PdfPCell cellComision = new PdfPCell(new Phrase("$ " + String.format("%.2f", montoComision), FONT_NORMAL));
            cellComision.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellComision.setBorder(PdfPCell.NO_BORDER);
            cellComision.setPadding(3f);
            tableTotales.addCell(cellComision);
        }

        // Total
        PdfPCell cellTotalLabel = new PdfPCell(new Phrase("TOTAL:", FONT_BOLD));
        cellTotalLabel.setBorder(PdfPCell.TOP);
        cellTotalLabel.setPadding(5f);
        tableTotales.addCell(cellTotalLabel);

        PdfPCell cellTotal = new PdfPCell(new Phrase("$ " + String.format("%.2f", totalConComision), FONT_BOLD));
        cellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTotal.setBorder(PdfPCell.TOP);
        cellTotal.setPadding(5f);
        tableTotales.addCell(cellTotal);

        document.add(tableTotales);
    }

    // ============================= PIE DE PÁGINA =============================
    private void addFooter(Document document) throws DocumentException {
        Paragraph pie = new Paragraph("\n\n¡Gracias por su compra en MUNDO PC!\n", FONT_BOLD);
        pie.setAlignment(Element.ALIGN_CENTER);
        document.add(pie);

        // Mostrar también el código único de venta en el pie del documento
        if (venta.getCodigoVenta() != null && !venta.getCodigoVenta().isEmpty()) {
            Paragraph codigo = new Paragraph("Código de Control: " + venta.getCodigoVenta() + "\n", FONT_NORMAL);
            codigo.setAlignment(Element.ALIGN_CENTER);
            document.add(codigo);
        }

        Paragraph legal = new Paragraph("Documento generado electrónicamente", FONT_NORMAL);
        legal.setAlignment(Element.ALIGN_CENTER);
        document.add(legal);
    }
}
