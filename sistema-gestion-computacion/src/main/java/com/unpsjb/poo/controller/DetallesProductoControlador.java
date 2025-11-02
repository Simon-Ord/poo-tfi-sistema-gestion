package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.model.productos.ProductoDigital;
import com.unpsjb.poo.model.productos.ProductoFisico;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

// Controlador para mostrar los detalles de un producto
public class DetallesProductoControlador {

    @FXML private TextArea txtDetalles;
    // Establece el producto y sus detalles
    public void setProducto(Producto producto) {
        if (producto == null) {
            txtDetalles.setText("No se ha seleccionado ningún producto.");
            return;
        }
        String tipo = producto.obtenerTipoProducto();
        String detalles;
        
        if ("FISICO".equals(tipo)) {
            ProductoFisico pf = ProductoFisico.obtenerPorId(producto.getIdProducto());
            detalles = pf != null ? pf.detallesProductoUI() : producto.detallesProductoUI();
        } else if ("DIGITAL".equals(tipo)) {
            ProductoDigital pd = ProductoDigital.obtenerPorId(producto.getIdProducto());
            detalles = pd != null ? pd.detallesProductoUI() : producto.detallesProductoUI();
        } else {
            detalles = producto.detallesProductoUI();
        }
        txtDetalles.setText(detalles + "\n═══════════════════════════════════════════════════\n");
    }
    @FXML
    private void cerrar(ActionEvent event) {
        if (event != null && event.getSource() != null) {
            Node node = (Node) event.getSource();
            BaseControlador.cerrarVentanaInterna(node);
        }
    }
}
