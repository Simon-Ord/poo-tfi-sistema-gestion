package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.Venta;
import com.unpsjb.poo.persistence.dao.impl.VentaDAOImpl;

/**
 * Auditoría específica para registrar ventas en el sistema.
 */
public class AuditoriaVentaUtil extends AuditoriaBase {

    private static final VentaDAOImpl ventaDAO = new VentaDAOImpl();

    @Override
    public void registrarCreacion(Object nuevo) {
        if (nuevo == null) return;
        if (!(nuevo instanceof Venta ventaOriginal)) return;

        //  Intentamos recuperar los datos persistidos desde la BD (si ya se guardó)
        Venta ventaPersistida = ventaDAO.findByCodigo(ventaOriginal.getCodigoVenta());
        Venta venta = (ventaPersistida != null) ? ventaPersistida : ventaOriginal;

        //  Obtener información de la venta
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

        //  Crear un texto bien formateado para guardar en la auditoría
        StringBuilder detalles = new StringBuilder();
        detalles.append("Registró una nueva VENTA:\n")
                .append(" - Tipo: ").append(tipo).append("\n")
                .append(" - Método de Pago: ").append(metodo).append("\n")
                .append(" - Cliente: ").append(cliente).append("\n")
                .append(" - Código: ").append(codigo).append("\n")
                .append(" - Total: ").append(total);



        //  Registrar evento genérico usando el método heredado
        registrarEvento("REGISTRAR VENTA", "venta", detalles.toString());
    }

    /**
     *  No se implementa comparación de modificaciones en ventas.
     * 
     * Se deja vacía porque una venta solo se registra, no se edita por ahora
     */
    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        // No aplica para ventas (solo se registran nuevas)
    }
}
