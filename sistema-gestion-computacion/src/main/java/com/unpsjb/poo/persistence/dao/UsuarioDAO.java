package com.unpsjb.poo.persistence.dao;

import com.unpsjb.poo.model.Usuario;

public interface UsuarioDAO {
    Usuario verificarLogin(String usuario, String contraseña);
}
