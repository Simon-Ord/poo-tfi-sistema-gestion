package com.unpsjb.poo.persistence.dao;

import java.util.List;

/**
 * Interfaz que define las operaciones CRUD
 * sobre la tabla usuarios.
 */
import com.unpsjb.poo.model.Usuario;

public interface UsuarioDAO {

    // Verifica usuario y contraseña (login)
    Usuario verificarLogin(String usuario, String contraseña);

    // Devuelve todos los usuarios
    List<Usuario> obtenerTodos();

    // Inserta un nuevo usuario
    boolean insertar(Usuario usuario);

    // Modifica un usuario existente
    boolean modificar(Usuario usuario);

    // Elimina un usuario por su ID
    boolean eliminar(int id);
}
