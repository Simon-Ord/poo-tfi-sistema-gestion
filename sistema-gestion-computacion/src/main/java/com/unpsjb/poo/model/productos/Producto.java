package com.unpsjb.poo.model.productos;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Producto {
    protected int idProducto;
    protected String nombreProducto;
    protected String descripcionProducto;
    protected int stockProducto;
    protected BigDecimal precioProducto;
    private Categoria categoria;
    protected int codigoProducto;
    protected boolean activo;
    protected Timestamp fechaCreacion;
    
    public Producto() {
        // Constructor por defecto, necesario para el método Read en el DAO
        this.activo = true; // Por defecto los nuevos productos están activos
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


    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getCategoriaProducto() {
        return categoria != null ? categoria.getNombre() : null;
    }
    public void setCategoriaProducto(String categoriaProducto) {
        if (this.categoria == null) {
            this.categoria = new Categoria();
        }
        this.categoria.setNombre(categoriaProducto);
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

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}