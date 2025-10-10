package com.unpsjb.poo.persistence.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.UsuarioDAO;
import com.unpsjb.poo.util.UsuarioFactory; //  Importa correctamente el Factory

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    //  🔹 Método existente para verificar login (no modificado excepto por el Factory)
    public Usuario verificarLogin(String usuario, String contraseña) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ? AND estado = TRUE";

        try (Connection conn = GestorDeConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Obtenemos el rol del usuario
                String rol = rs.getString("rol");

                //  Creamos el usuario correcto con el Factory (NO instanciamos Usuario directamente)
                Usuario u = UsuarioFactory.crearUsuarioPorRol(rol);

                //  Cargamos los datos desde la base
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setContraseña(rs.getString("contraseña"));
                u.setRol(rol);
                u.setEstado(rs.getBoolean("estado"));

                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // -------------------------------------------------------------
    // ✅ Obtener todos los usuarios
    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";

        try (Connection conn = GestorDeConexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String rol = rs.getString("rol");

                // ✅ Usamos el Factory nuevamente
                Usuario u = UsuarioFactory.crearUsuarioPorRol(rol);

                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setContraseña(rs.getString("contraseña"));
                u.setRol(rol);
                u.setEstado(rs.getBoolean("estado"));

                lista.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // -------------------------------------------------------------
    // Insertar usuario nuevo
    @Override
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, usuario, contraseña, rol, estado) VALUES (?, ?, ?, ?, TRUE)";
        try (Connection conn = GestorDeConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getUsuario());
            stmt.setString(3, usuario.getContraseña());
            stmt.setString(4, usuario.getRol());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // -------------------------------------------------------------
    // Modificar usuario existente
    @Override
    public boolean modificar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, usuario = ?, contraseña = ?, rol = ?, estado = ? WHERE id = ?";
        try (Connection conn = GestorDeConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getUsuario());
            stmt.setString(3, usuario.getContraseña());
            stmt.setString(4, usuario.getRol());
            stmt.setBoolean(5, usuario.isEstado());
            stmt.setInt(6, usuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // -------------------------------------------------------------
    //  Eliminar usuario
    // 🔹 Implementa eliminar por ID 
    // que lo tengo que cambiar creo pare aun nose creo que seria melor por lejago pero creo que esta bueno la idua del id
    @Override
    public boolean eliminar(int id) {
        
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = GestorDeConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
