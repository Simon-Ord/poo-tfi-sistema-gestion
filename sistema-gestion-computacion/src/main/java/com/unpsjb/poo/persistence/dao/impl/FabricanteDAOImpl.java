package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.productos.Fabricante;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class FabricanteDAOImpl implements DAO<Fabricante> {
    
    // =================
    //  Crear fabricante
    // =================
    @Override
    public boolean create(Fabricante fabricante) {
        String sql = "INSERT INTO fabricantes (nombre) VALUES (?)";
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setString(1, fabricante.getNombre());
            int filasAfectadas = pstmt.executeUpdate();
            
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear el fabricante: " + e.getMessage());
            return false;
        }
    }

    // =======================
    //  Leer fabricante por ID
    // =======================
    @Override
    public Optional<Fabricante> read(int id) {
        String sql = "SELECT * FROM fabricantes WHERE id = ?";
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el fabricante: " + e.getMessage());
        }
        return Optional.empty();
    }

    // ======================
    //  Actualizar fabricante
    // ======================
    @Override
    public boolean update(Fabricante fabricante) {
        String sql = "UPDATE fabricantes SET nombre = ? WHERE id = ?";
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setString(1, fabricante.getNombre());
            pstmt.setInt(2, fabricante.getId());
            int filasAfectadas = pstmt.executeUpdate();
            
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el fabricante: " + e.getMessage());
            return false;
        }
    }
    // ====================
    //  Eliminar fabricante
    // ====================
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM fabricantes WHERE id = ?";
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el fabricante: " + e.getMessage());
            return false;
        }
    }
    // =============================
    //  Listar todos los fabricantes
    // =============================
    @Override
    public List<Fabricante> findAll() {
        List<Fabricante> fabricantes = new ArrayList<>();
        String sql = "SELECT * FROM fabricantes ORDER BY nombre";
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                fabricantes.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los fabricantes: " + e.getMessage());
        }
        return fabricantes;
    }
    
    // ========================
    // Método auxiliar de mapeo
    // ========================
    private Fabricante mapResultSet(ResultSet rs) throws SQLException {
        Fabricante fabricante = new Fabricante();
        fabricante.setId(rs.getInt("id"));
        fabricante.setNombre(rs.getString("nombre"));
        return fabricante;
    }
}
