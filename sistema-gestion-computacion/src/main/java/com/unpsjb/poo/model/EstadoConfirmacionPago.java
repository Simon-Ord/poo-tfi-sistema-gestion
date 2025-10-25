package com.unpsjb.poo.model;

import com.unpsjb.poo.model.productos.Producto;

public class EstadoConfirmacionPago implements EstadoVenta {

    @Override
    public void siguientePaso(Venta venta) {
        if (venta.getEstrategiaPago() == null) {
            throw new IllegalStateException("No se ha seleccionado un método de pago.");
        }
        
        // Validar stock suficiente antes de procesar la venta
        for (ItemCarrito item : venta.getCarrito().getItems()) {
            Producto producto = item.getProducto();
            int cantidadRequerida = item.getCantidad();
            
            // Obtener el producto actualizado de la base de datos para verificar stock real
            Producto productoActual = Producto.obtenerPorId(producto.getIdProducto());
            
            if (productoActual == null) {
                throw new IllegalStateException("El producto " + producto.getNombreProducto() + " no existe.");
            }
            
            if (!productoActual.tieneStockSuficiente(cantidadRequerida)) {
                throw new IllegalStateException(
                    "Stock insuficiente para el producto: " + producto.getNombreProducto() + 
                    ". Disponible: " + productoActual.getStockProducto() + 
                    ", Requerido: " + cantidadRequerida
                );
            }
        }
        
        // Procesar el pago usando la estrategia seleccionada
        double montoTotal = venta.getCarrito().getTotal().doubleValue();
        double comision = venta.getEstrategiaPago().getComision();
        double montoConComision = montoTotal * (1 + comision);
        
        System.out.println("Procesando pago por $" + montoConComision + "...");
        boolean pagoExitoso = venta.getEstrategiaPago().pagar(montoConComision);
        
        if (pagoExitoso) {
            try {
                // Guardar la venta en la base de datos
                venta.guardarVentaBD();
                
                // Limpiar la venta y volver al estado inicial
                venta.cancelar();
            } catch (Exception e) {
                System.err.println("ERROR al guardar la venta: " + e.getMessage());
                throw new RuntimeException("No se pudo completar la venta: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("ERROR: El pago no pudo ser procesado.");
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
