package com.unpsjb.poo.util;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.persistence.dao.ReportesDAO;

/**
 * Clase utilitaria para registrar eventos de auditoría
 * y evitar repetir código en los controladores.
 *
 * 📘 Conceptos POO aplicados:
 * - Abstracción: los controladores no saben cómo se guarda la auditoría.
 * - Encapsulamiento: toda la lógica de registro está protegida aquí.
 * - Reutilización: se usa en todo el sistema.
 */
public class AuditoriaUtil {

    private static final ReportesDAO reportesDAO = new ReportesDAO();

    /** Método genérico para registrar cualquier acción del sistema */
    public static void registrarAccion(String usuario, String accion, String entidad, String detalle) {
        try {
            EventoAuditoria evento = new EventoAuditoria();
            evento.setUsuario(usuario != null ? usuario : "Sistema");
            evento.setAccion(accion);
            evento.setEntidad(entidad);
            evento.setDetalles(detalle);
            reportesDAO.registrarEvento(evento);
        } catch (Exception e) {
            System.err.println("Error al registrar auditoría: " + e.getMessage());
        }
    }

    /** Registrar los cambios entre un producto original y uno modificado */
    public static void registrarCambioProducto(Producto original, Producto modificado, String usuario) {
        try {
            StringBuilder cambios = new StringBuilder();

            if (!original.getNombreProducto().equals(modificado.getNombreProducto()))
                cambios.append("Nombre: '").append(original.getNombreProducto())
                        .append("' → '").append(modificado.getNombreProducto()).append("'. ");

            if (!original.getDescripcionProducto().equals(modificado.getDescripcionProducto()))
                cambios.append("Descripción: '").append(original.getDescripcionProducto())
                        .append("' → '").append(modificado.getDescripcionProducto()).append("'. ");

            if (!original.getCategoriaProducto().equals(modificado.getCategoriaProducto()))
                cambios.append("Categoría: '").append(original.getCategoriaProducto())
                        .append("' → '").append(modificado.getCategoriaProducto()).append("'. ");

            if (original.getPrecioProducto().compareTo(modificado.getPrecioProducto()) != 0)
                cambios.append("Precio: ").append(original.getPrecioProducto())
                        .append(" → ").append(modificado.getPrecioProducto()).append(". ");

            if (original.getStockProducto() != modificado.getStockProducto())
                cambios.append("Stock: ").append(original.getStockProducto())
                        .append(" → ").append(modificado.getStockProducto()).append(". ");

            if (cambios.length() > 0) {
                registrarAccion(
                        usuario,
                        "MODIFICAR PRODUCTO",
                        "Producto",
                        "El usuario " + usuario + " modificó el producto '" +
                                original.getNombreProducto() + "'. Cambios: " + cambios);
            }

        } catch (Exception e) {
            System.err.println("Error al registrar cambios de producto: " + e.getMessage());
        }
    }

    /** Registrar el cambio de estado (activo/inactivo) de un producto */
    public static void registrarCambioEstadoProducto(Producto producto, boolean nuevoEstado, String usuario) {
        String detalle = "El usuario " + usuario + " cambió el estado del producto '" +
                producto.getNombreProducto() + "' a " + (nuevoEstado ? "ACTIVO" : "INACTIVO") + ".";
        registrarAccion(usuario, "CAMBIO ESTADO PRODUCTO", "Producto", detalle);
    }
}
