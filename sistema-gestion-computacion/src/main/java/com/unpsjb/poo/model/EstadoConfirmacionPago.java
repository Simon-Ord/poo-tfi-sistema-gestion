package com.unpsjb.poo.model;

import java.util.Map;

import javafx.scene.Node;

/**
 * Estado concreto: Confirmación de Pago
 * Maneja todas las solicitudes cuando la venta está en fase de confirmación y procesamiento del pago.
 */
public class EstadoConfirmacionPago implements EstadoVenta {

    @Override
    public void siguientePaso(Venta venta) {
        // En el último estado, no hay siguiente paso
        // El usuario debe usar el botón "Registrar Venta" específico de esta vista
        System.out.println("Ya se encuentra en el último paso. Use 'Registrar Venta' para finalizar.");
    }

    @Override
    public void volverPaso(Venta venta) {
        venta.setEstado(new EstadoDatosFactura());
    }

    @Override
    public void validarTransicion(Venta venta) {
        // Valida si se puede procesar la venta
        if (venta.getEstrategiaPago() == null) {
            throw new IllegalStateException("No se ha seleccionado un método de pago.");
        }
    }

    @Override
    public void limpiarEstado(Venta venta) {
        // Limpia los datos específicos de este estado
        venta.setEstrategiaPago(null);
    }

    @Override
    public void inicializarVista(Map<String, Node> vistaMap, Venta venta) {
        System.out.println("Inicializando vista: " + getNombreEstado());
        Node vista = vistaMap.get(getVistaID());
        if (vista == null) {
            System.err.println("Error: Vista no encontrada para " + getVistaID());
            return;
        }
        System.out.println("Vista de confirmación de pago inicializada correctamente.");
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
