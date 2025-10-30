package com.unpsjb.poo.util.copias;

import com.unpsjb.poo.model.Cliente;

/**
 * 🧩 Utilidad para crear una copia profunda (clone) de un cliente.
 * Se usa principalmente para comparar cambios antes y después
 * de una modificación en la auditoría.
 */
public class CopiarClienteUtil {

    public static Cliente copiarCliente(Cliente original) {
        if (original == null) return null;

        Cliente copia = new Cliente();
        copia.setId(original.getId());
        copia.setNombre(original.getNombre());
        copia.setCuit(original.getCuit());
        copia.setTelefono(original.getTelefono());
        copia.setDireccion(original.getDireccion());
        copia.setEmail(original.getEmail());
        copia.setTipo(original.getTipo());
        copia.setActivo(original.isActivo());
        copia.setFechaCreacion(original.getFechaCreacion());

        return copia;
    }
}
