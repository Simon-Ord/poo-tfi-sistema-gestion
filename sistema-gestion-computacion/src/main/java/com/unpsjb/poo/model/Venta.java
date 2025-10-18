package com.unpsjb.poo.model;

public class Venta {

private CarritoDeCompra carrito;
private EstadoVenta estadoActualVenta;
private String tipoFactura;

public Venta () {
	this.carrito = new CarritoDeCompra();
	this.estadoActualVenta = (EstadoVenta) new EstadoAgregarProductos();
}

public void setEstado(EstadoVenta nuevoEstado) {
	this.estadoActualVenta = nuevoEstado;
}

public void siguientePaso() {
	this.estadoActualVenta.siguientePaso(this);
}

//Getters y setters

public CarritoDeCompra getCarrito() {
	return carrito;
}

public String getTipoFactura () {
	return tipoFactura;
}

public void setTipoFactura (String tipoFactura) {
	this.tipoFactura = tipoFactura;
}


}
