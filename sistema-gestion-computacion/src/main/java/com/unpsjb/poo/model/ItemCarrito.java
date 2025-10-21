package com.unpsjb.poo.model;

import java.math.BigDecimal;

import com.unpsjb.poo.model.productos.Producto;

//Contienen el producto, la cantidad y el precio unitario al momento de agregarlo al carrito
public class ItemCarrito {
    private Producto producto;
    private int cantidad;
    private BigDecimal precioUnitario;

    public ItemCarrito(Producto producto, int cantidad, BigDecimal precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {                      //Devuelve el subtotal de manera precisa usando BigDecimal
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    //Getters/setters
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }   

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    @Override
    public String toString() {
        return "ItemCarrito [Producto: " + producto + ", Cantidad: " + cantidad + ", PrecioUnitario: " + precioUnitario
                + "]";
    }


}
