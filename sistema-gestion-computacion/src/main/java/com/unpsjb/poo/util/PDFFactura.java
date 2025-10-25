package com.unpsjb.poo.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.unpsjb.poo.model.ItemCarrito;
import com.unpsjb.poo.model.Venta;
import java.io.FileOutputStream;

public class PDFFactura extends PDFExporter {

    private final Venta venta;

    public PDFFactura(Venta venta) {
        this.venta = venta;
    }

    @Override
    public boolean export(String filePath) {
        try {
            Document doc = new Document(PageSize.A4, 40, 40, 60, 40);
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();

            // Encabezado
            Paragraph titulo = new Paragraph("FACTURA", new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD));
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Fecha: " + java.time.LocalDate.now()));
            doc.add(new Paragraph("Cliente: " + (venta.getClienteFactura() != null ? venta.getClienteFactura().getNombre() : "Consumidor Final")));
            doc.add(new Paragraph("CUIT/DNI: " + (venta.getClienteFactura() != null ? venta.getClienteFactura().getCuit() : "-")));
            doc.add(new Paragraph("Método de Pago: " + (venta.getEstrategiaPago() != null ? venta.getEstrategiaPago().getNombreMetodoPago() : "-")));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("---------------------------------------------------------"));

            // Tabla de productos
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Producto");
            table.addCell("Cantidad");
            table.addCell("Precio Unit.");
            table.addCell("Subtotal");

            for (ItemCarrito item : venta.getCarrito().getItems()) {
                table.addCell(item.getProducto().getNombreProducto());
                table.addCell(String.valueOf(item.getCantidad()));
                table.addCell("$ " + item.getProducto().getPrecioProducto());
                table.addCell("$ " + item.getSubtotal());
            }

            doc.add(table);
            doc.add(new Paragraph(" "));

            double total = venta.getCarrito().getTotal().doubleValue();
            double iva = total * 0.21;

            doc.add(new Paragraph("Subtotal: $" + String.format("%.2f", total - iva)));
            doc.add(new Paragraph("IVA (21%): $" + String.format("%.2f", iva)));
            doc.add(new Paragraph("TOTAL FINAL: $" + String.format("%.2f", total)));

            doc.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
