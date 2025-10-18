package com.unpsjb.poo.persistence.dao;

import java.util.List;
import com.unpsjb.poo.model.Usuario;

public interface UsuarioDAO {

    // 🔹 Verifica usuario y contraseña (para login)
    Usuario verificarLogin(String usuario, String contraseña);

    // 🔹 Devuelve todos los usuarios
    List<Usuario> obtenerTodos();

    // 🔹 Inserta un nuevo usuario
    boolean insertar(Usuario usuario);

    // 🔹 Modifica un usuario existente
    boolean modificar(Usuario usuario);

    // 🔹 Desactiva un usuario (ya no se elimina)
    boolean eliminar(String dni);
}

