package com.unpsjb.poo.model;

import java.util.Map;

import javafx.scene.Node;

/**
 * Estado concreto: Agregar Productos
 * Maneja las solicitudes de la venta si está en fase de agregar productos al carrito.
 */
public class EstadoAgregarProductos implements EstadoVenta {
    
    @Override
    public void siguientePaso(Venta venta) {
        // Validación específica del estado antes de la transición
        if (venta.getCarrito().getItems().isEmpty()) {
            throw new IllegalStateException("Debe agregar productos al carrito antes de continuar.");
        }
        venta.setEstado(new EstadoDatosFactura());
    }

    @Override
    public void volverPaso(Venta venta) {
        // No hay paso anterior en el primer estado del proceso
        System.out.println("Ya se encuentra en el primer paso del proceso.");
    }

    @Override
    public void validarTransicion(Venta venta) {
        // Valida si se puede avanzar al siguiente paso
        if (venta.getCarrito().getItems().isEmpty()) {
            throw new IllegalStateException("Debe agregar productos al carrito antes de continuar.");
        }
    }

    @Override
    public void limpiarEstado(Venta venta) {
        // Limpia el estado cuando se cancela la venta
        venta.getCarrito().vaciarCarrito();
    }

    @Override
    public void inicializarVista(Map<String, Node> vistaMap, Venta venta) {
        // Lógica de inicialización específica para la vista de agregar productos
        
        Node vista = vistaMap.get(getVistaID());
        if (vista == null) {
            System.err.println("Error: Vista no encontrada para " + getVistaID());
            return;
        }

        System.out.println("Inicializando vista: " + getNombreEstado());
        
        // La vista ya está visible, solo necesitamos asegurarnos de que esté limpia
        // El controlador manejará los detalles específicos de JavaFX
    }

    @Override
    public String getNombreEstado() {
        return "Agregar Productos";
    }

    @Override
    public String getVistaID() {
        return "FacturaAgregarProductos";
    }
}
