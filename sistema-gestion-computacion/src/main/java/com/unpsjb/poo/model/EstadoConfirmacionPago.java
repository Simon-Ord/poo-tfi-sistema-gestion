package com.unpsjb.poo.model;

public class EstadoConfirmacionPago implements EstadoVenta {

    @Override
    public void siguientePaso(Venta venta) {
        if (venta.getEstrategiaPago() == null) {
            throw new IllegalStateException("No se ha seleccionado un método de pago.");
        }
        // Por ahora solo muestra un mensaje
        System.out.println("Confirmando pago...");
    }

    @Override
    public void volverPaso(Venta venta) {
        venta.setEstado(new EstadoDatosFactura());
    }

    @Override
    public String getNombreEstado() {
        return "Confirmación de Pago";
    }

    @Override
    public String getVistaID() {
        return "FacturaConfirmarVenta";
    }
}
