package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List; //  Importa correctamente el Factory

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.UsuarioDAO;

public class UsuarioDAOImpl implements UsuarioDAO {

    // ================================
    //  Verificar login
    // ================================
    @Override
    public Usuario verificarLogin(String usuario, String contraseña) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ? AND estado = TRUE";

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setLegajo(rs.getString("legajo"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setContraseña(rs.getString("contraseña"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getBoolean("estado"));
                return u;
            }

        } catch (SQLException e) {
            System.err.println(" Error al verificar login: " + e.getMessage());
        }

        return null;
    }

    // ================================
    //  Obtener todos los usuarios
    // ================================
    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setLegajo(rs.getString("legajo"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setContraseña(rs.getString("contraseña"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getBoolean("estado"));
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println(" Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    // ================================
    // Insertar usuario
    // ================================
    @Override
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (legajo, nombre, usuario, contraseña, rol, estado) VALUES (?, ?, ?, ?, ?, TRUE)";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getLegajo());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getUsuario());
            stmt.setString(4, usuario.getContraseña());
            stmt.setString(5, usuario.getRol());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
        }
        return false;
    }

    // ================================
    //  Modificar usuario
    // ================================
    @Override
    public boolean modificar(Usuario usuario) {
        String sql = "UPDATE usuarios SET legajo = ?, nombre = ?, usuario = ?, contraseña = ?, rol = ?, estado = ? WHERE id = ?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getLegajo());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getUsuario());
            stmt.setString(4, usuario.getContraseña());
            stmt.setString(5, usuario.getRol());
            stmt.setBoolean(6, usuario.isEstado());
            stmt.setInt(7, usuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(" Error al modificar usuario: " + e.getMessage());
        }
        return false;
    }

    // ================================
    //  Eliminar usuario
    // ================================
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filas = stmt.executeUpdate();
            if (filas == 0) {
                System.out.println("No se encontró ningún usuario con id=" + id);
            }
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }
}