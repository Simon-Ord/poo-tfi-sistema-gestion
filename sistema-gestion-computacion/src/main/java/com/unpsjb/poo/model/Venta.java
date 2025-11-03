package com.unpsjb.poo.model;

import java.math.BigDecimal;
import java.util.Map;

import com.unpsjb.poo.persistence.dao.impl.VentaDAOImpl;

import javafx.scene.Node;

/**
 * Delega TODAS las solicitudes dependientes del estado al objeto estado actual.
 */
public class Venta {

    private int idVenta;
    private String codigoVenta; // código único tipo FACT-0001 o TICK-0001
    private CarritoDeCompra carrito;
    private EstadoVenta estadoActualVenta;
    private String tipoFactura;
    private EstrategiaPago estrategiaPago;
    private Cliente clienteFactura;
    private BigDecimal total; 
    private String metodoPagoTexto; // solo para auditorías o reportes

public String getMetodoPagoTexto() { return (metodoPagoTexto == null || metodoPagoTexto.isBlank()) 
    && estrategiaPago != null ? estrategiaPago.getNombreMetodoPago() : metodoPagoTexto; }
public void setMetodoPagoTexto(String metodoPagoTexto) { this.metodoPagoTexto = metodoPagoTexto; }


    // DAO compartido (patrón Singleton implícito)
    private static final VentaDAOImpl ventaDAO = new VentaDAOImpl();

    // Convierte el texto del método de pago en una EstrategiaPago concreta
public static EstrategiaPago convertirMetodoPago(String metodoTexto) {
    if (metodoTexto == null) return null;

    switch (metodoTexto.toUpperCase()) {
        case "EFECTIVO":
            return new PagoEfectivo();
        case "TARJETA":
            return new PagoTarjeta();
        default:
            return null;
    }
}

    public Venta() {
        this.carrito = new CarritoDeCompra();
        this.estadoActualVenta = new EstadoAgregarProductos();
    }

    // ===============================================================
    // MÉTODOS DE DELEGACIÓN DEL PATRÓN STATE
    // ===============================================================
    // Delega la navegación al siguiente paso al estado actual.
    public void siguientePaso() {
        this.estadoActualVenta.siguientePaso(this);
    }
    // Delega la navegación al paso anterior al estado actual.
    public void volverPaso() {
        this.estadoActualVenta.volverPaso(this);
    }
    // Metodo que delega la solicitud al estado actual
    public void manejarSolicitud(String tipoSolicitud, Object... parametros) {
        this.estadoActualVenta.manejarSolicitud(this, tipoSolicitud, parametros);
    }
    // Delega la inicialización de la vista al estado actual.
    public void inicializarVistaActual(Map<String, Node> vistaMap) {
        this.estadoActualVenta.inicializarVista(vistaMap, this);
    }
    // Valida si se puede avanzar al siguiente paso.
    public void validarTransicion() {
        this.manejarSolicitud("VALIDAR_TRANSICION");
    }

    public void cancelar() {
        // Delegar la limpieza específica del estado antes de resetear
        this.manejarSolicitud("LIMPIAR_ESTADO");
        // Limpia los datos de la venta actual
        if (this.carrito != null) {
            this.carrito.vaciarCarrito(); // Vacía la lista de ítems
        }
        this.tipoFactura = null;
        this.estrategiaPago = null;
        this.clienteFactura = null;
        this.total = null;

        // Reinicia el estado al inicio del proceso
        this.estadoActualVenta = new EstadoAgregarProductos();
    }

    public void guardarVentaBD() {
        // Validaciones antes de guardar
        if (this.carrito == null || this.carrito.getItems().isEmpty()) {
            throw new IllegalStateException("No se puede guardar una venta sin productos.");
        }
        if (this.tipoFactura == null || this.tipoFactura.isEmpty()) {
            throw new IllegalStateException("No se puede guardar una venta sin tipo de factura.");
        }
        if (this.estrategiaPago == null) {
            throw new IllegalStateException("No se puede guardar una venta sin método de pago.");
        }
        // Si es una FACTURA, debe tener cliente asociado
        if ("FACTURA".equalsIgnoreCase(this.tipoFactura) && this.clienteFactura == null) {
            throw new IllegalStateException("Una FACTURA requiere datos del cliente.");
        }
        // Persistir en la BD
        System.out.println("Guardando venta en la base de datos...");
        boolean exito = ventaDAO.create(this);
        if (!exito) {
            throw new RuntimeException("Error al guardar la venta en la base de datos.");
        }
        System.out.println("Venta guardada correctamente en la BD con ID: " + this.idVenta);
    }
    // ====================
    //  Getters y Setters
    // ====================
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public CarritoDeCompra getCarrito() { return carrito; }
    public void setCarrito(CarritoDeCompra carrito) { this.carrito = carrito; }

    public EstadoVenta getEstadoActual() { return estadoActualVenta; }
    public void setEstado(EstadoVenta nuevoEstado) { this.estadoActualVenta = nuevoEstado; }

    public String getTipoFactura() { return tipoFactura; }
    public void setTipoFactura(String tipoFactura) { this.tipoFactura = tipoFactura; }

    public EstrategiaPago getEstrategiaPago() { return estrategiaPago; }
    public void setEstrategiaPago(EstrategiaPago estrategiaPago) { this.estrategiaPago = estrategiaPago; }

    public Cliente getClienteFactura() { return clienteFactura; }
    public void setClienteFactura(Cliente clienteFactura) { this.clienteFactura = clienteFactura; }

    public String getCodigoVenta() { return codigoVenta; }
    public void setCodigoVenta(String codigoVenta) { this.codigoVenta = codigoVenta; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    // ========================================================================
    // MÉTODOS DE CÁLCULO - LÓGICA DE NEGOCIO
    // ========================================================================
    // Calcula el subtotal sin IVA a partir del total del carrito
    public BigDecimal calcularSubtotalSinIVA() {
        if (carrito == null) return BigDecimal.ZERO;
        
        BigDecimal totalCarrito = carrito.getTotal();
        // El IVA está incluido en el precio, dividimos por 1.21 para obtener la base
        return totalCarrito.divide(new BigDecimal("1.21"), 2, java.math.RoundingMode.HALF_UP);
    }
    // Calcula el monto del IVA (21%)
    public BigDecimal calcularMontoIVA() {
        if (carrito == null) return BigDecimal.ZERO;
        
        BigDecimal subtotalSinIVA = calcularSubtotalSinIVA();
        return carrito.getTotal().subtract(subtotalSinIVA);
    }
    // Calcula el total final aplicando la comisión del método de pago
    public BigDecimal calcularTotalConComision() {
        if (carrito == null) return BigDecimal.ZERO;
        
        BigDecimal totalCarrito = carrito.getTotal();
        if (estrategiaPago == null) return totalCarrito;
        
        BigDecimal comision = BigDecimal.valueOf(estrategiaPago.getComision());
        BigDecimal factorComision = BigDecimal.ONE.add(comision);
        return totalCarrito.multiply(factorComision).setScale(2, java.math.RoundingMode.HALF_UP);
    }
    // Obtiene la descripción del cliente para mostrar en resúmenes
    public String obtenerDescripcionCliente() {
        if (clienteFactura != null && clienteFactura.getNombre() != null) {
            return clienteFactura.getNombre();
        }
        return "Consumidor Final";
    }
    // Obtiene el texto descriptivo del tipo de factura
    public String obtenerDescripcionTipoFactura() {
        return tipoFactura != null ? tipoFactura : "Sin Definir";
    }

}