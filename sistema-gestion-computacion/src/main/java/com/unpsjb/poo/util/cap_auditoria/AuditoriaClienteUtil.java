package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.util.Sesion;


public class AuditoriaClienteUtil extends AuditoriaBase {

    /**
     *  Genera un resumen de los cambios entre el cliente original y el modificado.
     */
    public static String generarResumenCambios(Cliente original, Cliente modificado) {
        StringBuilder sb = new StringBuilder();

        if (!original.getNombre().equals(modificado.getNombre())) {
            sb.append("\n• Nombre: '").append(original.getNombre())
              .append("' --> '").append(modificado.getNombre()).append("'");
        }

        if (original.getCuit() != null && !original.getCuit().equals(modificado.getCuit())) {
            sb.append("\n• CUIT: '").append(original.getCuit())
              .append("' --> '").append(modificado.getCuit()).append("'");
        }

        if (original.getTelefono() != null && !original.getTelefono().equals(modificado.getTelefono())) {
            sb.append("\n• Teléfono: '").append(original.getTelefono())
              .append("' --> '").append(modificado.getTelefono()).append("'");
        }

        if (original.getDireccion() != null && !original.getDireccion().equals(modificado.getDireccion())) {
            sb.append("\n• Dirección: '").append(original.getDireccion())
              .append("' --> '").append(modificado.getDireccion()).append("'");
        }

        if (original.getEmail() != null && !original.getEmail().equals(modificado.getEmail())) {
            sb.append("\n• Email: '").append(original.getEmail())
              .append("' --> '").append(modificado.getEmail()).append("'");
        }

        if (original.getTipo() != null && !original.getTipo().equals(modificado.getTipo())) {
            sb.append("\n• Tipo: '").append(original.getTipo())
              .append("' --> '").append(modificado.getTipo()).append("'");
        }

        return sb.toString();
    }

    public void registrarCreacion(Cliente cliente) {
        String usuario = (Sesion.getUsuarioActual() != null)
                ? Sesion.getUsuarioActual().getNombre()
                : "Desconocido";

        registrarEvento(
                "CREAR CLIENTE",
                "cliente",
              " agrego un nuevo cliente: " + cliente.getNombre()
        );
    }


    public void registrarCambioEstado(Cliente cliente, boolean nuevoEstado) {
        String usuario = (Sesion.getUsuarioActual() != null)
                ? Sesion.getUsuarioActual().getNombre()
                : "Desconocido";

        String estadoTexto = nuevoEstado ? "ACTIVO" : "INACTIVO";
        registrarEvento(
                "CAMBIAR ESTADO CLIENTE",
                "cliente",
                " cambió el estado del cliente '" +
                cliente.getNombre() + "' a " + estadoTexto + "."
        );
    }

    /**
     * 🧠 Registra las modificaciones entre dos versiones del cliente.
     */
    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        if (!(original instanceof Cliente) || !(modificado instanceof Cliente)) {
            return;
        }

        Cliente cliOriginal = (Cliente) original;
        Cliente cliModificado = (Cliente) modificado;

        String cambios = generarResumenCambios(cliOriginal, cliModificado);
        if (cambios.isEmpty()) return; // No hubo cambios

        String usuario = (Sesion.getUsuarioActual() != null)
                ? Sesion.getUsuarioActual().getNombre()
                : "Desconocido";

        registrarEvento(
                "MODIFICAR CLIENTE",
                "cliente",
                " modificó al cliente '" + cliOriginal.getNombre() +
                "'. Cambios (antes --> despues)" + cambios
        );
    }
}
