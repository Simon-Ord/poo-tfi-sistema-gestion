package com.unpsjb.poo.model;

import java.util.Map;

import javafx.scene.Node;

/**
 * Estado concreto: Agregar Productos
 * Maneja las solicitudes de la venta si está en fase de agregar productos al carrito.
 * Encapsula todo el comportamiento específico de este estado.
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
        // El estado controla que el botón "Volver" no esté disponible
        venta.manejarSolicitud("OCULTAR_BOTON_VOLVER");
        System.out.println("Ya se encuentra en el primer paso del proceso.");
    }

    @Override
    public void manejarSolicitud(Venta venta, String tipoSolicitud, Object... parametros) {
        switch (tipoSolicitud) {
            case "VALIDAR_TRANSICION":
                // Valida si se puede avanzar al siguiente paso
                if (venta.getCarrito().getItems().isEmpty()) {
                    throw new IllegalStateException("Debe agregar productos al carrito antes de continuar.");
                }
                break;
            case "LIMPIAR_ESTADO":
                // Limpia el estado cuando se cancela la venta
                venta.getCarrito().vaciarCarrito();
                break;
            case "OCULTAR_BOTON_VOLVER":
                // En el primer estado, el botón volver debe estar oculto/deshabilitado
                // Esta solicitud será manejada por el controlador que tiene acceso a la UI
                System.out.println("Solicitud para ocultar botón 'Volver' - Estado inicial");
                break;
            default:
                System.out.println("Solicitud no reconocida en EstadoAgregarProductos: " + tipoSolicitud);
        }
    }

    @Override
    public void inicializarVista(Map<String, Node> vistaMap, Venta venta) {
        // Lógica de inicialización específica para la vista de agregar productos
        // Esta lógica antes estaba en el controlador
        
        Node vista = vistaMap.get(getVistaID());
        if (vista == null) {
            System.err.println("Error: Vista no encontrada para " + getVistaID());
            return;
        }

        // Buscar y limpiar componentes específicos de esta vista
        System.out.println("Inicializando vista: " + getNombreEstado());
        
        // En el estado inicial, el botón "Volver" debe estar oculto
        venta.manejarSolicitud("OCULTAR_BOTON_VOLVER");
        
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
