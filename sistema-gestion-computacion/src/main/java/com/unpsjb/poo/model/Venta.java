package com.unpsjb.poo.model;

public class Venta {

private CarritoDeCompra carrito;
private EstadoVenta estadoActualVenta;
private String tipoFactura;
private EstrategiaPago estrategiaPago;
private Cliente clienteFactura;

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
//Lógica aca
	System.out.println("Persistiendo la venta en la base de datos...");
}

public void setClienteFactura(Cliente clienteFactura) {
	this.clienteFactura = clienteFactura;
}

public Cliente getClienteFactura() {
	return clienteFactura;
}

}

