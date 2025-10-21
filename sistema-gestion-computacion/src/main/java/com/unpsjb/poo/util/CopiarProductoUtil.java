package com.unpsjb.poo.util;

import com.unpsjb.poo.model.productos.Producto;

public class CopiarProductoUtil {
    public static Producto copiarProducto(Producto original) {
        if (original == null) return null;
        Producto copia = new Producto();
        copia.setIdProducto(original.getIdProducto());
        copia.setNombreProducto(original.getNombreProducto());
        copia.setDescripcionProducto(original.getDescripcionProducto());
        copia.setPrecioProducto(original.getPrecioProducto());
        copia.setStockProducto(original.getStockProducto());
        copia.setCodigoProducto(original.getCodigoProducto());
        copia.setCategoria(original.getCategoria());
        copia.setActivo(original.isActivo());
        copia.setFechaCreacion(original.getFechaCreacion());
        return copia;
    }
}
