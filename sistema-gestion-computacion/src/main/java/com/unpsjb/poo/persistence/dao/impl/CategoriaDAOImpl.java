package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.productos.Categoria;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class CategoriaDAOImpl implements DAO<Categoria> {
    
    // ===============
    // CREAR CATEGORIA
    // ===============
    @Override
    public boolean create(Categoria categoria) {
        String sql = """
            INSERT INTO categorias 
            (nombre_categoria, activo, fecha_creacion)
            VALUES (?, ?, ?)
            """;
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, categoria.getNombre());
            pstmt.setBoolean(2, categoria.isActivo());
            pstmt.setTimestamp(3, Timestamp.valueOf(categoria.getFechaCreacion()));
            
            int filas = pstmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear categoría: " + e.getMessage());
            return false;
        }
    }
    // ==============
    // LEER CATEGORIA
    // ==============
    @Override
    public Optional<Categoria> read(int id) {
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCategoria(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al leer categoría: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    // ===================
    // ACTUALIZAR CATEGORIA
    // ===================
    @Override
    public boolean update(Categoria categoria) {
        String sql = """
            UPDATE categorias 
            SET nombre_categoria = ?, activo = ?
            WHERE id_categoria = ?
            """;
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, categoria.getNombre());
            pstmt.setBoolean(2, categoria.isActivo());
            pstmt.setInt(3, categoria.getId());
            
            int filas = pstmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
            return false;
        }
    }

    // ==================================
    // ELIMINAR CATEGORIA (activo = falso)
    // ==================================
    @Override
    public boolean delete(int id) {
        String sql = "UPDATE categorias SET activo = false WHERE id_categoria = ?";
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }

    // ==================================
    // LISTAR TODAS ACTIVAS (por defecto)
    // ==================================
    @Override
    public List<Categoria> findAll() {
        return findAllActivas(); // Por defecto solo activas
    }

    // =====================================
    // BUSCAR CATEGORIAS POR NOMBRE PARCIAL
    // =====================================
    public List<Categoria> findByNombre(String nombre) {
        String sql = """
            SELECT * FROM categorias 
            WHERE LOWER(nombre_categoria) LIKE LOWER(?) AND activo = true
            ORDER BY nombre_categoria
            """;
        
        List<Categoria> categorias = new ArrayList<>();
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                categorias.add(mapResultSetToCategoria(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar categorías por nombre: " + e.getMessage());
        }
        
        return categorias;
    }
    // ========================
    // OBTENER TODAS LAS ACTIVAS
    // ========================
    public List<Categoria> findAllActivas() {
        String sql = """
            SELECT * FROM categorias 
            WHERE activo = true 
            ORDER BY nombre_categoria
            """;
        
        return ejecutarConsultaLista(sql);
    }

    // ==========================
    // OBTENER TODAS LAS INACTIVAS
    // ==========================
    public List<Categoria> findAllInactivas() {
        String sql = """
            SELECT * FROM categorias 
            WHERE activo = false 
            ORDER BY nombre_categoria
            """;
        return ejecutarConsultaLista(sql);
    }
    // ====================
    // CAMBIAR ESTADO
    // ====================
    public boolean cambiarEstado(int id, boolean activo) {
        String sql = "UPDATE categorias SET activo = ? WHERE id_categoria = ?";
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setBoolean(1, activo);
            pstmt.setInt(2, id);
            
            int filas = pstmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de categoría: " + e.getMessage());
            return false;
        }
    }
    // ===================
    // MÉTODOS AUXILIARES
    // ===================
    
    /**
     * Mapea un ResultSet a un objeto Categoria
     */
    private Categoria mapResultSetToCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("nombre_categoria"));
        categoria.setActivo(rs.getBoolean("activo"));
        
        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        if (timestamp != null) {
            categoria.setFechaCreacion(timestamp.toLocalDateTime());
        }
        
        return categoria;
    }

    /**
     * Ejecuta una consulta SQL y devuelve una lista de categorías
     */
    private List<Categoria> ejecutarConsultaLista(String sql) {
        List<Categoria> categorias = new ArrayList<>();
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(mapResultSetToCategoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar consulta de categorías: " + e.getMessage());
        }
        
        return categorias;
    }
}
