package com.unpsjb.poo.util;

import com.unpsjb.poo.model.Usuario;

/**
 * Clase Singleton que guarda información del usuario logueado
 * durante toda la ejecución del sistema.
 */
public class Sesion {

    private static Sesion instancia;    // Única instancia
    private Usuario usuarioActual;      // Usuario logueado

    //  Constructor privado para impedir instanciación externa
    private Sesion() {}

    //  Devuelve la única instancia (crea si no existe)
    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    // Inicia la sesión
    public static void iniciarSesion(Usuario usuario) {
        getInstancia().usuarioActual = usuario;
    }

    //  Cierra la sesión
    public static void cerrarSesion() {
        getInstancia().usuarioActual = null;
    }

    //  Obtiene el usuario actual
    public static Usuario getUsuarioActual() {
        return getInstancia().usuarioActual;
    }

    //  Verifica si hay sesión activa
    public static boolean haySesionActiva() {
        return getInstancia().usuarioActual != null;
    }
}
