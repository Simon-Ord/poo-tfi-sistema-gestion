package com.unpsjb.poo.model;

public class ItemCarrito {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public ItemCarrito(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    // getters/setters
}
