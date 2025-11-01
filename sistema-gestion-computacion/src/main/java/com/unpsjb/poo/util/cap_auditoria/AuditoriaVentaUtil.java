package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.Venta;
import com.unpsjb.poo.persistence.dao.impl.VentaDAOImpl;

public class AuditoriaVentaUtil extends AuditoriaBase {

    private static final VentaDAOImpl ventaDAO = new VentaDAOImpl();

    @Override
    public void registrarCreacion(Object nuevo) {
        if (!(nuevo instanceof Venta)) return;
        Venta ventaOriginal = (Venta) nuevo;

        //  Recuperar desde BD los datos persistidos reales
        Venta venta = ventaDAO.findByCodigo(ventaOriginal.getCodigoVenta());
        if (venta == null) venta = ventaOriginal;

        //  Datos descriptivos
        String cliente = (venta.getClienteFactura() != null && venta.getClienteFactura().getNombre() != null)
                ? venta.getClienteFactura().getNombre()
                : "Consumidor Final";

        String tipo = (venta.getTipoFactura() != null && !venta.getTipoFactura().isBlank())
                ? venta.getTipoFactura()
                : "SIN TIPO";

            String metodo = (venta.getMetodoPagoTexto() != null && !venta.getMetodoPagoTexto().isBlank())
           ? venta.getMetodoPagoTexto()
           : "Sin método";


        String codigo = (venta.getCodigoVenta() != null && !venta.getCodigoVenta().isBlank())
                ? venta.getCodigoVenta()
                : "N/A";

        String total = (venta.getTotal() != null)
                ? String.format("$%.2f", venta.getTotal().doubleValue())
                : "$0.00";

        // Formato por filas, estilo más legible
        StringBuilder detalles = new StringBuilder();
        detalles.append("Registró una VENTA:\n")
                .append(" - Tipo: ").append(tipo).append("\n")
                .append(" - Método de Pago: ").append(metodo).append("\n")
                .append(" - Cliente: ").append(cliente).append("\n")
                .append(" - Código: ").append(codigo).append("\n")
                .append(" - Total: ").append(total);

        // Guardar el evento de auditoría
        registrarEvento("REGISTRAR VENTA", "venta", detalles.toString());
    }

    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        // No aplica para ventas
    }
}