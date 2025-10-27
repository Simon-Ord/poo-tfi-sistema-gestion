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

        StringBuilder sb = new StringBuilder();
        // Información básica del producto
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append("                INFORMACIÓN BÁSICA\n");
        sb.append("═══════════════════════════════════════════════════\n\n");
        sb.append("Código: ").append(producto.getCodigoProducto()).append("\n");
        sb.append("Nombre: ").append(producto.getNombreProducto()).append("\n");
        sb.append("Descripción: ").append(producto.getDescripcionProducto()).append("\n");
        sb.append("Categoría: ").append(producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin categoría").append("\n");
        sb.append("Precio: $").append(producto.getPrecioProducto()).append("\n");
        sb.append("Stock: ").append(producto.getStockProducto()).append(" unidades\n");
        sb.append("Estado: ").append(producto.isActivo() ? "Activo" : "Inactivo").append("\n");
        
        // Determinar el tipo de producto y mostrar información específica
        String tipo = producto.obtenerTipoProducto();
        
        if ("FISICO".equals(tipo)) {
            ProductoFisico pf = ProductoFisico.obtenerPorId(producto.getIdProducto());
            if (pf != null) {
                sb.append("\n═══════════════════════════════════════════════════\n");
                sb.append("           CARACTERÍSTICAS FÍSICAS\n");
                sb.append("═══════════════════════════════════════════════════\n\n");
                sb.append("Fabricante: ").append(pf.getFabricante() != null ? pf.getFabricante().getNombre() : "No especificado").append("\n");
                sb.append("Estado Físico: ").append(pf.getEstadoFisico() != null ? pf.getEstadoFisico() : "No especificado").append("\n");
                
                if (pf.getGarantiaMeses() != null && pf.getGarantiaMeses() > 0) {
                    sb.append("Garantía: ").append(pf.getGarantiaMeses()).append(" meses\n");
                    sb.append("Tipo de Garantía: ").append(pf.getTipoGarantia() != null ? pf.getTipoGarantia() : "No especificado").append("\n");
                } else {
                    sb.append("Garantía: Sin garantía\n");
                }
            }
        } else if ("DIGITAL".equals(tipo)) {
            ProductoDigital pd = ProductoDigital.obtenerPorId(producto.getIdProducto());
            if (pd != null) {
                sb.append("\n═══════════════════════════════════════════════════\n");
                sb.append("           CARACTERÍSTICAS DIGITALES\n");
                sb.append("═══════════════════════════════════════════════════\n\n");
                sb.append("Proveedor Digital: ").append(pd.getProveedorDigital() != null ? pd.getProveedorDigital().getNombre() : "No especificado").append("\n");
                sb.append("Tipo de Licencia: ").append(pd.getTipoLicencia() != null ? pd.getTipoLicencia() : "No especificado").append("\n");
                
                if (pd.getActivacionesMax() != null && pd.getActivacionesMax() > 0) {
                    sb.append("Activaciones Máximas: ").append(pd.getActivacionesMax()).append("\n");
                } else {
                    sb.append("Activaciones Máximas: Ilimitadas\n");
                }
                if (pd.getDuracionLicenciaDias() != null && pd.getDuracionLicenciaDias() > 0) {
                    sb.append("Duración de Licencia: ").append(pd.getDuracionLicenciaDias()).append(" días\n");
                } else {
                    sb.append("Duración de Licencia: Perpetua\n");
                }
            }
        } else {
            sb.append("\n═══════════════════════════════════════════════════\n");
            sb.append("              PRODUCTO GENÉRICO\n");
            sb.append("═══════════════════════════════════════════════════\n\n");
            sb.append("Este producto no tiene características específicas\n");
        }
        sb.append("\n═══════════════════════════════════════════════════\n");
        
        txtDetalles.setText(sb.toString());
    }

    @FXML
    private void cerrar(ActionEvent event) {
        if (event != null && event.getSource() != null) {
            Node node = (Node) event.getSource();
            BaseControlador.cerrarVentanaInterna(node);
        }
    }
}
