package com.unpsjb.poo.persistence.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class UsuarioDAOImpl implements DAO<Usuario> {
    @Override
    public Optional<Usuario> read(int id) {
        // Usuario uses String dni as primary key, not int id
        // This method is not applicable for Usuario
        throw new UnsupportedOperationException("Usuario uses String dni as primary key. Use custom methods instead.");
    }
    
    public Usuario verificarLogin(String usuario, String contraseña) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ? AND estado = TRUE";
        Connection conn = GestorDeConexion.getInstancia().getConexion();
        
        if (conn == null) {
            System.err.println("Error: No se pudo establecer conexión a la base de datos");
            return null;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setDni(rs.getString("dni"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setContraseña(rs.getString("contraseña"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getBoolean("estado"));
                return u;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar login: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre";

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setDni(rs.getString("dni"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setContraseña(rs.getString("contraseña"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getBoolean("estado"));
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean create(Usuario usuario) {
        String sql = "INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getDni());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getUsuario());
            stmt.setString(4, usuario.getContraseña());
            stmt.setString(5, usuario.getRol());
            stmt.setBoolean(6, usuario.isEstado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, usuario = ?, contraseña = ?, rol = ?, estado = ? WHERE dni = ?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getUsuario());
            stmt.setString(3, usuario.getContraseña());
            stmt.setString(4, usuario.getRol());
            stmt.setBoolean(5, usuario.isEstado());
            stmt.setString(6, usuario.getDni());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        // Usuario uses String dni as primary key, not int id
        // This method is not applicable for Usuario
        throw new UnsupportedOperationException("Usuario uses String dni as primary key. Use eliminar(String dni) instead.");
    }
    
    // Custom method for Usuario since it uses String dni as primary key
    public boolean eliminar(String dni) {
        // ahora solo desactiva
        String sql = "UPDATE usuarios SET estado = FALSE WHERE dni = ?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al desactivar usuario: " + e.getMessage());
            return false;
        }
    }
}
