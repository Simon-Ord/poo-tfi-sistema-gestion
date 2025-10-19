/*

package com.unpsjb.poo.model;

public class EstadoDatosFactura implements EstadoVenta {

    @Override
    public void siguientePaso(Venta venta) {
        String tipoFactura = venta.getTipoFactura();

        if (tipoFactura == null || tipoFactura.isEmpty()) {
            System.out.println("ERROR: Debe seleccionar un tipo de factura antes de continuar.");
            return;  // Esto bloquea el avance
        }

        //venta.setEstado(new EstadoConfirmacionPago());
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
        return "DatosFacturaVista";
    }

   

    
}
 */