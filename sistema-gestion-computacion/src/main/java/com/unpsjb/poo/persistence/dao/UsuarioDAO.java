package com.unpsjb.poo.persistence.dao;

import com.unpsjb.poo.model.Usuario;
import java.util.List;

public interface UsuarioDAO {

    // 🔹 Método que ya tenías y sigue igual (lo usa el Login)
    Usuario verificarLogin(String usuario, String contraseña);

    // 🔹 Métodos nuevos para el ABM de usuarios
    List<Usuario> obtenerTodos();                // Ver todos los usuarios
    boolean insertar(Usuario usuario);           // Agregar nuevo usuario
    boolean modificar(Usuario usuario);          // Modificar un usuario existente
    boolean eliminar(int id);                    // Eliminar usuario por ID
}
