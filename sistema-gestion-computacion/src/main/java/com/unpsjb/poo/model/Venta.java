package com.unpsjb.poo.model;

import com.unpsjb.poo.persistence.dao.impl.VentaDAOImpl;
import java.math.BigDecimal;

public class Venta {

    private int idVenta;
    private String codigoVenta; // código único tipo FACT-0001 o TICK-0001
    private CarritoDeCompra carrito;
    private EstadoVenta estadoActualVenta;
    private String tipoFactura;
    private EstrategiaPago estrategiaPago;
    private Cliente clienteFactura;
    private BigDecimal total; 

    // DAO compartido (patrón Singleton implícito)
    private static final VentaDAOImpl ventaDAO = new VentaDAOImpl();

    public Venta() {
        this.carrito = new CarritoDeCompra();
        this.estadoActualVenta = new EstadoAgregarProductos();
    }

    public void siguientePaso() {
        this.estadoActualVenta.siguientePaso(this);
    }

    public void cancelar() {
        // Limpia los datos de la venta actual
        if (this.carrito != null) {
            this.carrito.vaciarCarrito(); // Vacía la lista de ítems
        }
        this.tipoFactura = null;
        this.estrategiaPago = null;
        this.clienteFactura = null;

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

    // 🔹 Getters y Setters

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public CarritoDeCompra getCarrito() {
        return carrito;
    }

    public void setCarrito(CarritoDeCompra carrito) {
        this.carrito = carrito;
    }

    public EstadoVenta getEstadoActual() {
        return estadoActualVenta;
    }

    public void setEstado(EstadoVenta nuevoEstado) {
        this.estadoActualVenta = nuevoEstado;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public EstrategiaPago getEstrategiaPago() {
        return estrategiaPago;
    }

    public void setEstrategiaPago(EstrategiaPago estrategiaPago) {
        this.estrategiaPago = estrategiaPago;
    }

    public Cliente getClienteFactura() {
        return clienteFactura;
    }

    public void setClienteFactura(Cliente clienteFactura) {
        this.clienteFactura = clienteFactura;
    }

    public String getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}