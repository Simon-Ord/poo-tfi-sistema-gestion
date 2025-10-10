package com.unpsjb.poo.util;

import com.unpsjb.poo.model.Admin;
import com.unpsjb.poo.model.Empleado;
import com.unpsjb.poo.model.Usuario;

public class UsuarioFactory {

    public static Usuario crearUsuarioPorRol(String rol) {
        if (rol == null) {
            System.out.println("⚠ Rol no especificado, devolviendo Empleado por defecto.");
            return new Empleado();
        }

        switch (rol.trim().toUpperCase()) {
            case "ADMIN":
            case "ADMINISTRADOR":
                return new Admin();
            case "EMPLEADO":
                return new Empleado();
            default:
                System.out.println("⚠ Rol desconocido (" + rol + "), devolviendo Empleado por defecto.");
                return new Empleado();
        }
    }
}
