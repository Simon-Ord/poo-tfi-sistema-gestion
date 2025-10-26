package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.productos.Producto;

/**
 * 🧩 Clase concreta de auditoría para la entidad Producto.
 * Hereda de AuditoriaBase, aplicando polimorfismo.
 */
public class AuditoriaProductoUtil extends AuditoriaBase {

    /**
     * Implementa la comparación y registro de cambios específicos
     * entre dos productos (original y modificado).
     */
    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        if (!(original instanceof Producto) || !(modificado instanceof Producto)) return;
        Producto pOriginal = (Producto) original;
        Producto pModificado = (Producto) modificado;

        StringBuilder cambios = new StringBuilder();

        // Comparación de nombre
        if (!equalsNullSafe(pOriginal.getNombreProducto(), pModificado.getNombreProducto()))
            cambios.append("Nombre: '").append(nvl(pOriginal.getNombreProducto()))
                   .append("' → '").append(nvl(pModificado.getNombreProducto())).append("'. ");

        // Comparación de descripción
        if (!equalsNullSafe(pOriginal.getDescripcionProducto(), pModificado.getDescripcionProducto()))
            cambios.append("Descripción: '").append(nvl(pOriginal.getDescripcionProducto()))
                   .append("' → '").append(nvl(pModificado.getDescripcionProducto())).append("'. ");

        // Comparación de categoría
        if (pOriginal.getCategoria() != null && pModificado.getCategoria() != null) {
            if (!equalsNullSafe(pOriginal.getCategoria().getNombre(), pModificado.getCategoria().getNombre()))
                cambios.append("Categoría: '").append(pOriginal.getCategoria().getNombre())
                       .append("' → '").append(pModificado.getCategoria().getNombre()).append("'. ");
        } else if (pOriginal.getCategoria() != null || pModificado.getCategoria() != null) {
            cambios.append("Categoría: '")
                   .append(pOriginal.getCategoria() != null ? pOriginal.getCategoria().getNombre() : "Ninguna")
                   .append("' → '")
                   .append(pModificado.getCategoria() != null ? pModificado.getCategoria().getNombre() : "Ninguna")
                   .append("'. ");
        }

        // Comparación de precio
        if (pOriginal.getPrecioProducto() != null && pModificado.getPrecioProducto() != null
                && pOriginal.getPrecioProducto().compareTo(pModificado.getPrecioProducto()) != 0)
            cambios.append("Precio: ").append(pOriginal.getPrecioProducto())
                   .append(" → ").append(pModificado.getPrecioProducto()).append(". ");

        // Comparación de stock
        if (pOriginal.getStockProducto() != pModificado.getStockProducto())
            cambios.append("Stock: ").append(pOriginal.getStockProducto())
                   .append(" → ").append(pModificado.getStockProducto()).append(". ");

        // Comparación de estado
        if (pOriginal.isActivo() != pModificado.isActivo())
            cambios.append("Estado: ").append(pOriginal.isActivo() ? "Activo" : "Inactivo")
                   .append(" → ").append(pModificado.isActivo() ? "Activo" : "Inactivo").append(". ");

        // Registrar si hubo cambios
        if (cambios.length() > 0) {
            registrarEvento("MODIFICAR PRODUCTO", "producto",
                    "El usuario " + getUsuarioActual() +
                    " modificó el producto '" + pOriginal.getNombreProducto() +
                    "'. Cambios: " + cambios);
        }
    }

    /**
     * 🔹 Auditoría específica para cambio de estado.
     */
    public void registrarCambioEstado(Producto producto, boolean nuevoEstado) {
        registrarEvento("CAMBIO ESTADO PRODUCTO", "producto",
                "El usuario " + getUsuarioActual() +
                " cambió el estado del producto '" + producto.getNombreProducto() +
                "' a " + (nuevoEstado ? "ACTIVO" : "INACTIVO") + ".");
    }

    // =============================================
    // Métodos auxiliares internos (reutilizables)
    // =============================================

    private boolean equalsNullSafe(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private String nvl(String valor) {
        return (valor != null && !valor.isEmpty()) ? valor : "(vacío)";
    }

    // =============================================
    // 🔄 Compatibilidad con el código anterior
    // =============================================

    /**
     * Permite seguir usando la llamada anterior:
     * AuditoriaProductoUtil.registrarCambioProducto(original, modificado);
     */
    public static void registrarCambioProducto(Producto original, Producto modificado) {
        new AuditoriaProductoUtil().registrarAccionEspecifica(original, modificado);
    }
}


