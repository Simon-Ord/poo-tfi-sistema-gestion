package com.unpsjb.poo.model;

import java.math.BigDecimal;
import java.util.Map;

import com.unpsjb.poo.model.productos.Producto;

import javafx.scene.Node;

/**
 * Estado concreto: Confirmación de Pago
 * Maneja todas las solicitudes cuando la venta está en fase de confirmación y procesamiento del pago.
 */
public class EstadoConfirmacionPago implements EstadoVenta {

    @Override
    public void siguientePaso(Venta venta) {
        // En el último estado, el botón "Siguiente" debe estar oculto/deshabilitado
        // El usuario debe usar el botón "Registrar Venta" específico de esta vista
        venta.manejarSolicitud("OCULTAR_BOTON_SIGUIENTE");
        System.out.println("Ya se encuentra en el último paso. Use 'Registrar Venta' para finalizar.");
    }

    @Override
    public void volverPaso(Venta venta) {
        venta.setEstado(new EstadoDatosFactura());
    }

    @Override
    public void manejarSolicitud(Venta venta, String tipoSolicitud, Object... parametros) {
        switch (tipoSolicitud) {
            case "VALIDAR_TRANSICION":
                // Valida si se puede procesar la venta
                if (venta.getEstrategiaPago() == null) {
                    throw new IllegalStateException("No se ha seleccionado un método de pago.");
                }
                break;
    
            case "CALCULAR_TOTAL_CON_COMISION":
                // Calcula el total incluyendo comisión del método de pago
                if (venta.getEstrategiaPago() != null) {
                    BigDecimal total = venta.getCarrito().getTotal();
                    double comision = venta.getEstrategiaPago().getComision();
                    BigDecimal totalConComision = total.multiply(BigDecimal.valueOf(1 + comision));
                    venta.setTotal(totalConComision);
                } else {
                    venta.setTotal(venta.getCarrito().getTotal());
                }
                break;
                
            case "VALIDAR_STOCK":
                // Valida que hay stock suficiente para todos los productos
                for (ItemCarrito item : venta.getCarrito().getItems()) {
                    Producto producto = item.getProducto();
                    int cantidadRequerida = item.getCantidad();
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
                break;
                
            case "PROCESAR_PAGO":
                // Procesa el pago usando la estrategia seleccionada
                if (venta.getEstrategiaPago() == null) {
                    throw new IllegalStateException("No hay método de pago seleccionado.");
                }
                double montoTotal = venta.getCarrito().getTotal().doubleValue();
                double comision = venta.getEstrategiaPago().getComision();
                double montoConComision = montoTotal * (1 + comision);
                boolean pagoExitoso = venta.getEstrategiaPago().pagar(montoConComision);
                if (!pagoExitoso) {
                    throw new RuntimeException("ERROR: El pago no pudo ser procesado.");
                }
                break;
                
            case "OCULTAR_BOTON_SIGUIENTE":
                // En el último estado, el botón "Siguiente" debe estar oculto
                System.out.println("Solicitud para ocultar botón 'Siguiente' - Estado final");
                break;
                
            default:
                System.out.println("Solicitud no reconocida en EstadoConfirmacionPago: " + tipoSolicitud);
        }
    }

    @Override
    public void inicializarVista(Map<String, Node> vistaMap, Venta venta) {
        System.out.println("Inicializando vista: " + getNombreEstado());
        Node vista = vistaMap.get(getVistaID());
        if (vista == null) {
            System.err.println("Error: Vista no encontrada para " + getVistaID());
            return;
        }
        // En el estado final, el botón "Siguiente" debe estar oculto
        venta.manejarSolicitud("OCULTAR_BOTON_SIGUIENTE");
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
