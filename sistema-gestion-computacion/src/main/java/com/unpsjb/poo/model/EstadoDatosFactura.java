

package com.unpsjb.poo.model;

public class EstadoDatosFactura implements EstadoVenta {

    @Override
    public void siguientePaso(Venta venta) {
         String tipoFactura = venta.getTipoFactura();

        // 1. VALIDACIÓN: Asegurar que se haya seleccionado Factura o Ticket
        if (tipoFactura == null || tipoFactura.isEmpty()) {
            System.err.println("ERROR: Debe seleccionar el tipo de factura (Factura/Ticket).");
            // Se puede mostrar una alerta en la UI o simplemente detener el avance.
            return; 
        }

        // 2. VALIDACIÓN CRÍTICA: Si es FACTURA, verificar que el cliente esté cargado.
        /* 
        if (tipoFactura.equals("FACTURA")) {
            Cliente cliente = venta.getClienteFactura();
            
            // Si el cliente es nulo O si no tiene CUIT (lo que indica que no se cargó el formulario)
            if (cliente == null || cliente.getCuit() == null || cliente.getCuit().isBlank()) {
                System.err.println("ERROR: Para FACTURA, debe cargar los datos fiscales del cliente (CUIT).");
                return; 
            }
        }*/

        // Si la validación pasa (es Ticket o es Factura con cliente cargado), avanza.
        venta.setEstado(new EstadoConfirmacionPago());
    }

    @Override
    public void volverPaso(Venta venta) {
        venta.setEstado(new EstadoAgregarProductos());
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
 