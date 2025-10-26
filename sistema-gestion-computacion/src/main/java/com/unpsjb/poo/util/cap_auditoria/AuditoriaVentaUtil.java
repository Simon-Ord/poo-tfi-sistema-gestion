package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.Venta;

/**
 * ✅ Clase utilitaria para registrar auditorías de ventas.
 * Registra la creación de una venta, indicando quién la hizo,
 * a qué cliente, el tipo de comprobante, método y código único.
 */
public class AuditoriaVentaUtil extends AuditoriaBase {

    public void registrarVenta(Venta venta) {
        if (venta == null) return;

        String usuario = getUsuarioActual();
        String cliente = (venta.getClienteFactura() != null)
                ? venta.getClienteFactura().getNombre()
                : "Consumidor Final";
        String tipo = (venta.getTipoFactura() != null)
                ? venta.getTipoFactura()
                : "Desconocido";
        String metodo = (venta.getEstrategiaPago() != null)
                ? venta.getEstrategiaPago().getDescripcion()
                : "Sin método";
        String codigo = (venta.getCodigoVenta() != null)
                ? venta.getCodigoVenta()
                : "N/A";
        
        // 🚨 CORRECCIÓN APLICADA AQUÍ: 
        // Se llama a getTotal() y se convierte a double para el formato String.
        double total = (venta.getCarrito() != null)
                ? venta.getCarrito().getTotal().doubleValue()
                : 0.0;

        String detalles = String.format(
            "El usuario %s realizó una %s (%s) al cliente '%s' con código %s. Total: $%.2f.",
            usuario, tipo, metodo, cliente, codigo, total
        );

        registrarEvento("REGISTRAR VENTA", "venta", detalles);
    }

    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarAccionEspecifica'");
    }
}