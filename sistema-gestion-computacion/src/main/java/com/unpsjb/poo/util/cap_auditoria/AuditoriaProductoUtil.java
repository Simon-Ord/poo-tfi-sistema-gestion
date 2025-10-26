package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.Sesion;

/**
 * 🧾 Clase específica para registrar auditorías relacionadas con productos.
 * 
 * Separa la lógica de comparación y registro de cambios de productos
 * del resto de AuditoriaUtil para mantener el código más limpio.
 */
public class AuditoriaProductoUtil {

    private static final ReportesDAO reportesDAO = new ReportesDAO();

    /**
     * 🔹 Registrar cambios entre un producto original y su versión modificada.
     * Detecta qué campos fueron cambiados y los detalla.
     */
    public static void registrarCambioProducto(Producto original, Producto modificado) {
        try {
            String usuario = (Sesion.getUsuarioActual() != null)
                    ? Sesion.getUsuarioActual().getNombre()
                    : "Sistema";

            StringBuilder cambios = new StringBuilder();

            if (!equalsNullSafe(original.getNombreProducto(), modificado.getNombreProducto()))
                cambios.append("Nombre: '").append(nvl(original.getNombreProducto()))
                        .append("' → '").append(nvl(modificado.getNombreProducto())).append("'. ");

            if (!equalsNullSafe(original.getDescripcionProducto(), modificado.getDescripcionProducto()))
                cambios.append("Descripción: '").append(nvl(original.getDescripcionProducto()))
                        .append("' → '").append(nvl(modificado.getDescripcionProducto())).append("'. ");

            if (original.getCategoria() != null && modificado.getCategoria() != null) {
                if (!original.getCategoria().getNombre().equals(modificado.getCategoria().getNombre()))
                    cambios.append("Categoría: '").append(original.getCategoria().getNombre())
                            .append("' → '").append(modificado.getCategoria().getNombre()).append("'. ");
            } else if (original.getCategoria() != null || modificado.getCategoria() != null) {
                cambios.append("Categoría: '")
                        .append(original.getCategoria() != null ? original.getCategoria().getNombre() : "Ninguna")
                        .append("' → '")
                        .append(modificado.getCategoria() != null ? modificado.getCategoria().getNombre() : "Ninguna")
                        .append("'. ");
            }

            if (original.getPrecioProducto() != null && modificado.getPrecioProducto() != null
                    && original.getPrecioProducto().compareTo(modificado.getPrecioProducto()) != 0)
                cambios.append("Precio: ").append(original.getPrecioProducto())
                        .append(" → ").append(modificado.getPrecioProducto()).append(". ");

            if (original.getStockProducto() != modificado.getStockProducto())
                cambios.append("Stock: ").append(original.getStockProducto())
                        .append(" → ").append(modificado.getStockProducto()).append(". ");

            if (original.isActivo() != modificado.isActivo())
                cambios.append("Estado: ").append(original.isActivo() ? "Activo" : "Inactivo")
                        .append(" → ").append(modificado.isActivo() ? "Activo" : "Inactivo").append(". ");

            // Registrar solo si hubo algo que cambió
            if (cambios.length() > 0) {
                EventoAuditoria evento = new EventoAuditoria();
                evento.setUsuario(usuario);
                evento.setAccion("MODIFICAR PRODUCTO");
                evento.setEntidad("producto");
                evento.setDetalles("El usuario " + usuario + " modificó el producto '" 
                        + original.getNombreProducto() + "'. Cambios: " + cambios);

                reportesDAO.registrarEvento(evento);
            }

        } catch (Exception e) {
            System.err.println("Error al registrar cambios de producto: " + e.getMessage());
        }
    }

    /**
     * 🔹 Registrar cambio de estado (activo/inactivo) del producto.
     */
    public static void registrarCambioEstadoProducto(Producto producto, boolean nuevoEstado) {
        String usuario = (Sesion.getUsuarioActual() != null)
                ? Sesion.getUsuarioActual().getNombre()
                : "Sistema";

        EventoAuditoria evento = new EventoAuditoria();
        evento.setUsuario(usuario);
        evento.setAccion("CAMBIO ESTADO PRODUCTO");
        evento.setEntidad("producto");
        evento.setDetalles("El usuario " + usuario + " cambió el estado del producto '" 
                + producto.getNombreProducto() + "' a " 
                + (nuevoEstado ? "ACTIVO" : "INACTIVO") + ".");

        reportesDAO.registrarEvento(evento);
    }

    // Métodos auxiliares
    private static boolean equalsNullSafe(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private static String nvl(String valor) {
        return (valor != null && !valor.isEmpty()) ? valor : "(vacío)";
    }
}
