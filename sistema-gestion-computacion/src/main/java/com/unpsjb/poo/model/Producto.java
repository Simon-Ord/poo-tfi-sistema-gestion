package com.unpsjb.poo.model;

import java.math.BigDecimal;

public class Producto {
	private int idProducto;
	private String nombreProducto;
	private String descripcionProducto;
	private int stockProducto;
	private BigDecimal precioProducto;
	private String categoriaProducto;
	private String fabricanteProducto;
	private int codigoProducto;
	private boolean estado; // Para borrarlo del sistema sin eliminarlo de la BD
	private boolean activo; 
	
	public Producto() {
	//Constructor por defecto, necesario para el metodo Read en el DAO	
	}
	
	public Producto(int idProducto,String nombreProducto,String descripcionProducto,int stockProducto,BigDecimal precioProducto,String categoriaProducto,String fabricanteProducto,int codigoProducto, boolean estado) {
		this.idProducto = idProducto;
		this.nombreProducto = nombreProducto;
		this.descripcionProducto = descripcionProducto;
		this.stockProducto = stockProducto;
		this.precioProducto = precioProducto;
		this.categoriaProducto = categoriaProducto;
		this.fabricanteProducto = fabricanteProducto;
		this.codigoProducto = codigoProducto;
		this.estado = estado;
		this.activo = activo;
	}

	// Constructor compatible con versiones anteriores (por defecto estado = true)
	public Producto(int idProducto,String nombreProducto,String descripcionProducto,int stockProducto,BigDecimal precioProducto,String categoriaProducto,String fabricanteProducto,int codigoProducto) {
		this(idProducto, nombreProducto, descripcionProducto, stockProducto, precioProducto, categoriaProducto, fabricanteProducto, codigoProducto, true);
	}

		public boolean isEstado() {
				return estado;
		}

		public void setEstado(boolean estado) {
				this.estado = estado;
		}

	
public int getIdProducto() {
	return idProducto;
}

public void setIdProducto(int idProducto) {
	this.idProducto = idProducto;
}


public String getNombreProducto () {
	return nombreProducto;
}

public void setNombreProducto (String nombreProducto) {
	this.nombreProducto = nombreProducto;
}


public String getDescripcionProducto () {
	return descripcionProducto;
}

public void setDescripcionProducto(String descripcionProducto) {
	this.descripcionProducto = descripcionProducto;
}


public int getStockProducto() {
	return stockProducto;
}

public void setStockProducto(int stockProducto) {
	this.stockProducto = stockProducto;
}


public BigDecimal getPrecioProducto() {
	return precioProducto;
}

public void setPrecioProducto(BigDecimal precioProducto) {
	this.precioProducto = precioProducto;
}


public String getCategoriaProducto() {
	return categoriaProducto;
}

public void setCategoriaProducto(String categoriaProducto) {
	this.categoriaProducto = categoriaProducto;
}


public String getFabricanteProducto() {
	return fabricanteProducto;
}

public void setFabricanteProducto(String fabricanteProducto) {
	this.fabricanteProducto = fabricanteProducto;
}

public int getCodigoProducto() {
	return codigoProducto;
}

public void setCodigoProducto (int codigoProducto) {
	this.codigoProducto = codigoProducto;
}

public boolean isActivo() {
	return activo;
}

public void setActivo(boolean activo) {
	this.activo = activo;
}













	
	

}
