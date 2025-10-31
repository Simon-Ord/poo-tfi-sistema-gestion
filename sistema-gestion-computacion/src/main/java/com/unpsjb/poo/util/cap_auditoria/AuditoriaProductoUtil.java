package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.model.productos.ProductoDigital;
import com.unpsjb.poo.model.productos.ProductoFisico;
import com.unpsjb.poo.util.Sesion;

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

        if (original instanceof ProductoFisico && modificado instanceof ProductoFisico) {
            ProductoFisico o = (ProductoFisico) original;
            ProductoFisico m = (ProductoFisico) modificado;
            if (!o.getFabricante().equals(m.getFabricante())) {
                sb.append("\n• Fabricante: ").append(o.getFabricante())
                  .append(" → ").append(m.getFabricante());
            }
        } else if (original instanceof ProductoDigital && modificado instanceof ProductoDigital) {
            ProductoDigital o = (ProductoDigital) original;
            ProductoDigital m = (ProductoDigital) modificado;
            if (!o.getProveedorDigital().equals(m.getProveedorDigital())) {
                sb.append("\n• Proveedor: ").append(o.getProveedorDigital())
                  .append(" → ").append(m.getProveedorDigital());
            }
        }

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
