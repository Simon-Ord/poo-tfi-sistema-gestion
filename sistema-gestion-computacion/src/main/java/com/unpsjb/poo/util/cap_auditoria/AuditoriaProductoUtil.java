package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.productos.Producto;

public class AuditoriaProductoUtil extends AuditoriaBase {

    public static String generarResumenCambios(Producto original, Producto modificado) {
        StringBuilder sb = new StringBuilder();

        if (!original.getNombreProducto().equals(modificado.getNombreProducto())) {
            sb.append("\n• Nombre: '").append(original.getNombreProducto())
              .append("' → '").append(modificado.getNombreProducto()).append("'");
        }

        if (original.getPrecioProducto() != modificado.getPrecioProducto()) {
            sb.append("\n• Precio: ").append(original.getPrecioProducto())
              .append(" → ").append(modificado.getPrecioProducto());
        }

        if (original.getStockProducto() != modificado.getStockProducto()) {
            sb.append("\n• Stock: ").append(original.getStockProducto())
              .append(" → ").append(modificado.getStockProducto());
        }

        if (original.getCategoria() != null && modificado.getCategoria() != null &&
            !original.getCategoria().getNombre().equals(modificado.getCategoria().getNombre())) {
            sb.append("\n• Categoría: ").append(original.getCategoria().getNombre())
              .append(" → ").append(modificado.getCategoria().getNombre());
        }

        // Cada subclase genera sus cambios específicos
        sb.append(modificado.generarCambiosEspecificos(original));

        return sb.toString();
    }

    // la accion que hace cada hija

    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        if (!(original instanceof Producto) || !(modificado instanceof Producto)) return;

        Producto pOriginal = (Producto) original;
        Producto pModificado = (Producto) modificado;
        String cambios = generarResumenCambios(pOriginal, pModificado);

        if (!cambios.isEmpty()) {
            registrarEvento(
                "MODIFICAR PRODUCTO",
                "producto",
                " modificó el producto '" + pOriginal.getNombreProducto() +
                "'." + cambios
            );
        }
    }
    
}
