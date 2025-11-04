package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.productos.Producto;

/**
 * Auditoría específica para operaciones sobre PRODUCTOS.
 */
public class AuditoriaProductoUtil extends AuditoriaBase {

    /** Registra la creación de un producto nuevo */
    @Override
    public void registrarCreacion(Object nuevo) {
        if (!(nuevo instanceof Producto producto)) return;

        String tipo = producto.getClass().getSimpleName();
        String detalles = String.format(
                "Creó un producto %s llamado '%s', precio $%.2f, stock %d.",
                tipo,
                producto.getNombreProducto(),
                producto.getPrecioProducto(),
                producto.getStockProducto()
        );

        registrarEvento("CREAR PRODUCTO", "producto", detalles);
    }

    /** Registra el cambio de estado (activo/inactivo) del producto */
    public void registrarCambioEstado(Producto producto, boolean nuevoEstado) {
        String estado = nuevoEstado ? "ACTIVO" : "INACTIVO";
        registrarEvento(
                "CAMBIAR ESTADO PRODUCTO",
                "producto",
                "Cambió el estado del producto '" + producto.getNombreProducto() + "' a " + estado + "."
        );
    }

    /** Registra modificaciones detectadas entre dos versiones de un producto */
    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        if (!(original instanceof Producto pOriginal) || !(modificado instanceof Producto pModificado)) return;

        String cambios = generarResumenCambios(pOriginal, pModificado);
        if (!cambios.isEmpty()) {
            registrarEvento(
                    "MODIFICAR PRODUCTO",
                    "producto",
                    "Modificó el producto '" + pOriginal.getNombreProducto() + "'." + cambios
            );
        }
    }

    /** Genera un resumen con los cambios detectados */
    private static String generarResumenCambios(Producto original, Producto modificado) {
        StringBuilder sb = new StringBuilder();

        comparar(sb, "Nombre", original.getNombreProducto(), modificado.getNombreProducto());
        comparar(sb, "Precio", original.getPrecioProducto(), modificado.getPrecioProducto());
        comparar(sb, "Stock", original.getStockProducto(), modificado.getStockProducto());

        if (original.getCategoria() != null && modificado.getCategoria() != null)
            comparar(sb, "Categoría", original.getCategoria().getNombre(), modificado.getCategoria().getNombre());

        // Llamada polimórfica (los subtipos pueden agregar más comparaciones)
        sb.append(original.compararDatosEspecificos(modificado));

        return sb.toString();
    }

    /* Método auxiliar para comparar valores */
    private static void comparar(StringBuilder sb, String campo, Object o, Object n) {
        if (o == null && n == null) return;
        if (o == null || n == null || !o.equals(n))
            sb.append("\n• ").append(campo).append(": '")
              .append(o != null ? o : "null").append("' --> '")
              .append(n != null ? n : "null").append("'");
    }
}
