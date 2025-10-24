package com.unpsjb.poo.model;

public class EstadoConfirmacionPago implements EstadoVenta {

    @Override
    public void siguientePaso(Venta venta) {
        // Procesar el pago
        if (venta.getEstrategiaPago() == null) {
            System.out.println("ERROR: Debe seleccionar un método de pago.");
            return;
        }
        
        double totalFinal = venta.getCarrito().getTotal().doubleValue() * 
                           (1 + venta.getEstrategiaPago().getComision());
        
        boolean pagoExitoso = venta.getEstrategiaPago().pagar(totalFinal);
        
        if (pagoExitoso) {
            // Guardar la venta en la base de datos
            boolean guardado = venta.guardarVentaBD();
            
            if (guardado) {
                System.out.println("Venta confirmada y guardada exitosamente.");
                // Reiniciar la venta para una nueva operación
                venta.cancelar();
            } else {
                System.out.println("ERROR: No se pudo guardar la venta en la base de datos.");
            }
        } else {
            System.out.println("ERROR: El pago no se pudo procesar.");
        }
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
