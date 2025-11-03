
package com.unpsjb.poo.model;

import java.util.Map;

import javafx.scene.Node;


// Maneja todas las solicitudes cuando la venta está en fase de captura de datos de facturación.
public class EstadoDatosFactura implements EstadoVenta {

    @Override
    public void siguientePaso(Venta venta) {
        String tipoFactura = venta.getTipoFactura();
        // Validación específica del estado
        if (tipoFactura == null || tipoFactura.isEmpty()) {
            throw new IllegalStateException("Debe seleccionar el tipo de factura (Factura/Ticket).");
        }
        // Validación crítica: Si es FACTURA, verificar que el cliente esté cargado
        if ("FACTURA".equals(tipoFactura)) {
            Cliente cliente = venta.getClienteFactura();
            if (cliente == null || cliente.getCuit() == null || cliente.getCuit().isBlank()) {
                throw new IllegalStateException("Para FACTURA, debe cargar los datos fiscales del cliente (CUIT).");
            }
        }
        // Si todas las validaciones pasan, avanza al siguiente estado
        venta.setEstado(new EstadoConfirmacionPago());
    }

    @Override
    public void volverPaso(Venta venta) {
        venta.setEstado(new EstadoAgregarProductos());
    }

    @Override
    public void validarTransicion(Venta venta) {
        // Valida si se puede avanzar al siguiente paso
        String tipoFactura = venta.getTipoFactura();
        if (tipoFactura == null || tipoFactura.isEmpty()) {
            throw new IllegalStateException("Debe seleccionar el tipo de factura (Factura/Ticket).");
        }
        
        if ("FACTURA".equals(tipoFactura)) {
            Cliente cliente = venta.getClienteFactura();
            if (cliente == null || cliente.getCuit() == null || cliente.getCuit().isBlank()) {
                throw new IllegalStateException("Para FACTURA, debe cargar los datos fiscales del cliente (CUIT).");
            }
        }
    }

    @Override
    public void limpiarEstado(Venta venta) {
        // Limpia los datos específicos de este estado
        venta.setTipoFactura(null);
        venta.setClienteFactura(null);
    }

    @Override
    public void inicializarVista(Map<String, Node> vistaMap, Venta venta) {
        System.out.println("Inicializando vista: " + getNombreEstado());
        // Buscar elementos específicos de la vista y configurarlos
        Node vista = vistaMap.get(getVistaID());
        if (vista == null) {
            System.err.println("Error: Vista no encontrada para " + getVistaID());
            return;
        } 
        System.out.println("Vista de datos de factura inicializada correctamente.");
    }

    @Override
    public String getNombreEstado() {
        return "Datos de Factura";
    }

    @Override
    public String getVistaID() {
        return "FacturaDatosVenta";
    }
}
