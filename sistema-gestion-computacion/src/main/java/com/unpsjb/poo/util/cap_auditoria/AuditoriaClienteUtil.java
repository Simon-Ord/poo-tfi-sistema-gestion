package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.Cliente;

/**
 * Auditoría específica para operaciones sobre CLIENTES.
 */
public class AuditoriaClienteUtil extends AuditoriaBase {

    @Override
    public void registrarCreacion(Object nuevo) {
        if (!(nuevo instanceof Cliente cliente)) return;
        registrarEvento("CREAR CLIENTE", "cliente", "Agregó un nuevo cliente: '" + cliente.getNombre() + "'.");
    }

    /** Registra el cambio de estado activo/inactivo */
    public void registrarCambioEstado(Cliente cliente, boolean nuevoEstado) {
        String estado = nuevoEstado ? "ACTIVO" : "INACTIVO";
        registrarEvento("CAMBIAR ESTADO CLIENTE", "cliente",
                "Cambió el estado del cliente '" + cliente.getNombre() + "' a " + estado + ".");
    }

    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        if (!(original instanceof Cliente cOriginal) || !(modificado instanceof Cliente cModificado)) return;
        String cambios = generarResumenCambios(cOriginal, cModificado);
        if (!cambios.isEmpty()) {
            registrarEvento("MODIFICAR CLIENTE", "cliente",
                    "Modificó el cliente '" + cOriginal.getNombre() + "'." + cambios);
        }
    }

    private String generarResumenCambios(Cliente original, Cliente modificado) {
        StringBuilder sb = new StringBuilder();
        comparar(sb, "Nombre", original.getNombre(), modificado.getNombre());
        comparar(sb, "CUIT", original.getCuit(), modificado.getCuit());
        comparar(sb, "Teléfono", original.getTelefono(), modificado.getTelefono());
        comparar(sb, "Dirección", original.getDireccion(), modificado.getDireccion());
        comparar(sb, "Email", original.getEmail(), modificado.getEmail());
        comparar(sb, "Tipo", original.getTipo(), modificado.getTipo());
        return sb.toString();
    }

    private void comparar(StringBuilder sb, String campo, Object o, Object n) {
        if (o == null && n == null) return;
        if (o == null || n == null || !o.equals(n))
            sb.append("\n• ").append(campo).append(": '")
              .append(o != null ? o : "null").append("' --> '")
              .append(n != null ? n : "null").append("'");
    }
}
