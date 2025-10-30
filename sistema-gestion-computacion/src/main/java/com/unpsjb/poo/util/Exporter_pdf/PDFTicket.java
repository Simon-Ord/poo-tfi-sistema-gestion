package com.unpsjb.poo.util.Exporter_pdf;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.unpsjb.poo.model.ItemCarrito;
import com.unpsjb.poo.model.Venta;

/**
 *  Ticket de venta mejorado - MUNDO PC
 * Diseño profesional con formato tipo recibo de comercio.
 */
public class PDFTicket extends PDFExporter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static final Font FONT_BOLD = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    private static final Font FONT_SMALL = new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC, BaseColor.GRAY);

    private final Venta venta;

    public PDFTicket(Venta venta) {
        this.venta = venta;
    }

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
            addSeparator(document);
            addProductTable(document);
            addSeparator(document);
            addTotals(document);
            addSeparator(document);
            addFooter(document);

            document.close();
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // ======================== ENCABEZADO ========================
    private void addHeader(Document document) throws DocumentException {
        Paragraph titulo = new Paragraph("MUNDO PC", FONT_TITLE);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph(" Av. San Martín 1234 - Trelew\nTel: (0280) 123-4567", FONT_NORMAL);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(6);
        document.add(subtitulo);

        Paragraph fechaVenta = new Paragraph("Fecha: " + DATE_FORMAT.format(new Date()), FONT_NORMAL);
        fechaVenta.setAlignment(Element.ALIGN_CENTER);
        document.add(fechaVenta);

        if (venta.getIdVenta() > 0) {
            Paragraph idVenta = new Paragraph("N° de Venta: " + venta.getIdVenta(), FONT_NORMAL);
            idVenta.setAlignment(Element.ALIGN_CENTER);
            document.add(idVenta);
        }

        String metodo = venta.getEstrategiaPago() != null ? venta.getEstrategiaPago().getNombreMetodoPago() : "Sin método";
        Paragraph metodoPago = new Paragraph("Método de Pago: " + metodo + "\n\n", FONT_NORMAL);
        metodoPago.setAlignment(Element.ALIGN_CENTER);
        document.add(metodoPago);
    }

    // ======================== LÍNEA SEPARADORA ========================
    private void addSeparator(Document document) throws DocumentException {
        Paragraph linea = new Paragraph("----------------------------------------------", FONT_SMALL);
        linea.setAlignment(Element.ALIGN_CENTER);
        linea.setSpacingAfter(5);
        document.add(linea);
    }

    // ======================== TABLA DE PRODUCTOS ========================
    private void addProductTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2.8f, 0.9f, 0.8f, 1.2f});

        addProductTableHeader(table);

        for (ItemCarrito item : venta.getCarrito().getItems()) {
            addProductRow(table, item);
        }

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    private void addProductTableHeader(PdfPTable table) {
        String[] headers = {"Producto", "P.Unit", "Cant.", "Subtot"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FONT_BOLD));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }

    private void addProductRow(PdfPTable table, ItemCarrito item) {
        PdfPCell nombreCell = new PdfPCell(new Phrase(item.getProducto().getNombreProducto(), FONT_NORMAL));
        nombreCell.setBorder(Rectangle.NO_BORDER);
        nombreCell.setPadding(2f);

        PdfPCell precioCell = new PdfPCell(new Phrase(String.format("$%.2f", item.getPrecioUnitario()), FONT_NORMAL));
        precioCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        precioCell.setBorder(Rectangle.NO_BORDER);
        precioCell.setPadding(2f);

        PdfPCell cantCell = new PdfPCell(new Phrase(String.valueOf(item.getCantidad()), FONT_NORMAL));
        cantCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cantCell.setBorder(Rectangle.NO_BORDER);
        cantCell.setPadding(2f);

        PdfPCell subtotalCell = new PdfPCell(new Phrase(String.format("$%.2f", item.getSubtotal()), FONT_NORMAL));
        subtotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subtotalCell.setBorder(Rectangle.NO_BORDER);
        subtotalCell.setPadding(2f);

        table.addCell(nombreCell);
        table.addCell(precioCell);
        table.addCell(cantCell);
        table.addCell(subtotalCell);
    }

    // ======================== TOTALES ========================
    private void addTotals(Document document) throws DocumentException {
        double totalCarrito = venta.getCarrito().getTotal().doubleValue();
        double comision = venta.getEstrategiaPago() != null ? venta.getEstrategiaPago().getComision() : 0.0;
        double totalConComision = totalCarrito * (1 + comision);

        double subtotalSinIva = totalConComision / 1.21;
        double iva = totalConComision - subtotalSinIva;

        Paragraph totales = new Paragraph();
        totales.setAlignment(Element.ALIGN_RIGHT);
        totales.setLeading(12f);
        totales.add(new Phrase("Subtotal: $" + String.format("%.2f", subtotalSinIva) + "\n", FONT_NORMAL));
        totales.add(new Phrase("IVA (21%): $" + String.format("%.2f", iva) + "\n", FONT_NORMAL));

        if (comision > 0) {
            double montoComision = totalCarrito * comision;
            totales.add(new Phrase("Comisión: $" + String.format("%.2f", montoComision) + "\n", FONT_NORMAL));
        }

        Paragraph total = new Paragraph("TOTAL: $" + String.format("%.2f", totalConComision) + "\n", FONT_BOLD);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(4);
        document.add(totales);
        document.add(total);
    }

    // ======================== PIE DE PÁGINA ========================
    private void addFooter(Document document) throws DocumentException {
        Paragraph pie = new Paragraph("\n¡Gracias por su compra en MUNDO PC!\n", FONT_BOLD);
        pie.setAlignment(Element.ALIGN_CENTER);
        document.add(pie);

        if (venta.getCodigoVenta() != null && !venta.getCodigoVenta().isEmpty()) {
            Paragraph codigo = new Paragraph("Código de Control: " + venta.getCodigoVenta(), FONT_SMALL);
            codigo.setAlignment(Element.ALIGN_CENTER);
            document.add(codigo);
        }

        Paragraph legal = new Paragraph("Documento generado electrónicamente", FONT_SMALL);
        legal.setAlignment(Element.ALIGN_CENTER);
        legal.setSpacingBefore(5);
        document.add(legal);
    }
}
