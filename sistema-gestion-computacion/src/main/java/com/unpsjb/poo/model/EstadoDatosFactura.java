
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
    public void manejarSolicitud(Venta venta, String tipoSolicitud, Object... parametros) {
        switch (tipoSolicitud) {
            case "VALIDAR_TRANSICION":
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
                break;
                
            case "VALIDAR_TIPO_FACTURA":
                // Valida la selección del tipo de factura
                if (parametros.length > 0 && parametros[0] instanceof String) {
                    String tipo = (String) parametros[0];
                    if (!"Factura".equals(tipo) && !"Ticket".equals(tipo)) {
                        throw new IllegalArgumentException("Tipo de factura inválido: " + tipo);
                    }
                }
                break;
                
            case "VALIDAR_CLIENTE_FACTURA":
                // Valida que el cliente esté completo para facturas
                if (parametros.length > 0 && parametros[0] instanceof Cliente) {
                    Cliente cliente = (Cliente) parametros[0];
                    if (cliente.getCuit() == null || cliente.getCuit().isBlank()) {
                        throw new IllegalArgumentException("El cliente debe tener CUIT para generar una factura.");
                    }
                }
                break;
                
            default:
                System.out.println("Solicitud no reconocida en EstadoDatosFactura: " + tipoSolicitud);
        }
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
 