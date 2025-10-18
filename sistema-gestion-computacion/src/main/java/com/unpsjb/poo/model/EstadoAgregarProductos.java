package com.unpsjb.poo.model;

public class EstadoAgregarProductos implements EstadoVenta {
    
    @Override
    public void siguientePaso(Venta venta) {
        if (venta.getCarrito().getItems().isEmpty()) {
            System.out.println("ERROR: Debe agregar productos al carrito antes de continuar.");
            return;  //Esto bloquea el avanceñ
        }
        venta.setEstado(new EstadoDatosFactura());
    }

    @Override
    public void volverPaso(Venta venta) {
        // No hay paso anterior, no hacer nada
    }

    @Override
    public String getNombreEstado() {
        return "Agregar Productos";
    }

    @Override
    public String getVistaID() {
        return "AgregarProductosVista";
    }
    
}
