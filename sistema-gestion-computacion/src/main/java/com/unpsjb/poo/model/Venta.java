/*package com.unpsjb.poo.model;

public class Venta {

private CarritoDeCompra carrito;
private EstadoVenta estadoActualVenta;
private String tipoFactura;
private EstrategiaPago estrategiaPago;

public Venta () {
	this.carrito = new CarritoDeCompra();
	this.estadoActualVenta = (EstadoVenta) new EstadoAgregarProductos();
}

public void siguientePaso() {
	this.estadoActualVenta.siguientePaso(this);
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

}
*/