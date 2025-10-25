package com.unpsjb.poo.model;

import com.unpsjb.poo.persistence.dao.impl.VentaDAOImpl;

public class Venta {

private int idVenta;
private CarritoDeCompra carrito;
private EstadoVenta estadoActualVenta;
private String tipoFactura;
private EstrategiaPago estrategiaPago;
private Cliente clienteFactura;

// DAO estático compartido por todas las ventas
private static final VentaDAOImpl ventaDAO = new VentaDAOImpl();

public Venta () {
	this.carrito = new CarritoDeCompra();
	this.estadoActualVenta = (EstadoVenta) new EstadoAgregarProductos();
}

public void siguientePaso() {
	this.estadoActualVenta.siguientePaso(this);
}

public void cancelar() {
    // 1. Limpiar los datos de la venta actual (crucial antes de reiniciar)
    if (this.carrito != null) {
        this.carrito.vaciarCarrito(); //vacía la lista de ítems
    }
    this.tipoFactura = null;
    this.estrategiaPago = null;
    this.clienteFactura = null; 

    // 2. Reiniciar el estado (volviendo al inicio del proceso)
    this.estadoActualVenta = new EstadoAgregarProductos();
}

//Getters y setters


public void setEstado(EstadoVenta nuevoEstado) {
	this.estadoActualVenta = nuevoEstado;
}


public CarritoDeCompra getCarrito() {
	return carrito;
}

public String getTipoFactura () {
	return tipoFactura;
}

public void setTipoFactura (String tipoFactura) {
	this.tipoFactura = tipoFactura;
}

public EstadoVenta getEstadoActual() {
	return estadoActualVenta;
}

public EstrategiaPago getEstrategiaPago() {
	return estrategiaPago;
}

public void setEstrategiaPago(EstrategiaPago estrategiaPago) {
	this.estrategiaPago = estrategiaPago;
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
    
    // Validar que si es FACTURA, debe tener cliente
    if ("FACTURA".equals(this.tipoFactura) && this.clienteFactura == null) {
        throw new IllegalStateException("Una FACTURA requiere datos del cliente.");
    }
    
    // Persistir la venta en la base de datos
    System.out.println("Persistiendo la venta en la base de datos...");
    boolean exito = ventaDAO.create(this);
    
    if (!exito) {
        throw new RuntimeException("Error al guardar la venta en la base de datos.");
    }
    
    System.out.println("✓ Venta guardada exitosamente en la BD con ID: " + this.idVenta);
}

public void setClienteFactura(Cliente clienteFactura) {
	this.clienteFactura = clienteFactura;
}

public Cliente getClienteFactura() {
	return clienteFactura;
}

public int getIdVenta() {
	return idVenta;
}

public void setIdVenta(int idVenta) {
	this.idVenta = idVenta;
}

}
