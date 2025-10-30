package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.productos.ProveedorDigital;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class ProveedorDigitalDAOImpl implements DAO<ProveedorDigital> {
    
    // =======================
    //  Crear proveedor Digital
    // ========================
    @Override
    public boolean create(ProveedorDigital proveedor) {
        String sql = "INSERT INTO proveedores_digitales (nombre) VALUES (?)";
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setString(1, proveedor.getNombre());
            int filasAfectadas = pstmt.executeUpdate();
            
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear el proveedor digital: " + e.getMessage());
            return false;
        }
    }

    // ==============================
    //  Leer proveedor Digital por ID
    // ==============================
    @Override
    public Optional<ProveedorDigital> read(int id) {
        String sql = "SELECT * FROM proveedores_digitales WHERE id = ?";
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el proveedor digital: " + e.getMessage());
        }
        return Optional.empty();
    }
    // =============================
    //  Actualizar proveedor Digital
    // =============================
    @Override
    public boolean update(ProveedorDigital proveedor) {
        String sql = "UPDATE proveedores_digitales SET nombre = ? WHERE id = ?";
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setString(1, proveedor.getNombre());
            pstmt.setInt(2, proveedor.getId());
            int filasAfectadas = pstmt.executeUpdate();
            
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el proveedor digital: " + e.getMessage());
            return false;
        }
    }

    // ===========================
    //  Eliminar proveedor Digital
    // ===========================
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM proveedores_digitales WHERE id = ?";
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el proveedor digital: " + e.getMessage());
            return false;
        }
    }

    // =============================
    //  Listar todos los proveedores
    // =============================
    @Override
    public List<ProveedorDigital> findAll() {
        List<ProveedorDigital> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedores_digitales ORDER BY nombre";
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                proveedores.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los proveedores digitales: " + e.getMessage());
        }
        return proveedores;
    }
    // ========================
    // Método auxiliar de mapeo
    // ========================
    private ProveedorDigital mapResultSet(ResultSet rs) throws SQLException {
        ProveedorDigital proveedor = new ProveedorDigital();
        proveedor.setId(rs.getInt("id"));
        proveedor.setNombre(rs.getString("nombre"));
        return proveedor;
    }
}
