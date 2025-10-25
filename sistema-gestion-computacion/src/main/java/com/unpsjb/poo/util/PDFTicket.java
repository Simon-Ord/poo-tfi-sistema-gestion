package com.unpsjb.poo.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.unpsjb.poo.model.ItemCarrito;
import com.unpsjb.poo.model.Venta;
import java.io.FileOutputStream;

public class PDFTicket extends PDFExporter {

    private final Venta venta;

    public PDFTicket(Venta venta) {
        this.venta = venta;
    }

    @Override
    public boolean export(String filePath) {
        try {
            Document doc = new Document(PageSize.A6, 20, 20, 40, 20);
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();

            Paragraph titulo = new Paragraph("TICKET DE VENTA", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Fecha: " + java.time.LocalDate.now()));
            doc.add(new Paragraph(" "));
            
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.addCell("Producto");
            table.addCell("Cant.");
            table.addCell("Subtotal");

            for (ItemCarrito item : venta.getCarrito().getItems()) {
                table.addCell(item.getProducto().getNombreProducto());
                table.addCell(String.valueOf(item.getCantidad()));
                table.addCell("$ " + item.getSubtotal());
            }

            doc.add(table);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("TOTAL: $" + String.format("%.2f", venta.getCarrito().getTotal())));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Método de Pago: " + (venta.getEstrategiaPago() != null ? venta.getEstrategiaPago().getNombreMetodoPago() : "-")));
            doc.add(new Paragraph("Gracias por su compra!"));

            doc.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
