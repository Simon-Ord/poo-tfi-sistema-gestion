package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.productos.Categoria;
import com.unpsjb.poo.model.productos.Fabricante;
import com.unpsjb.poo.model.productos.ProductoFisico;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class ProductoFisicoDAOImpl implements DAO<ProductoFisico> {
    
    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    // ========================
    //  Crear producto físico
    // ========================
    @Override
    public boolean create(ProductoFisico producto) {
        // Primero creo el producto base
        if (!productoDAO.create(producto)) {
            return false;
        }
        // Luego agrego caracteristicas fisicas
        String sqlFisico = """
            INSERT INTO productos_fisicos 
            (id_producto, id_fabricante, garantia_meses, tipo_garantia, estado_fisico)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sqlFisico)) {

            pstmt.setInt(1, producto.getIdProducto());
            
            if (producto.getFabricante() != null) {
                pstmt.setInt(2, producto.getFabricante().getId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            if (producto.getGarantiaMeses() != null) {
                pstmt.setInt(3, producto.getGarantiaMeses());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(4, producto.getTipoGarantia() != null ? producto.getTipoGarantia().name() : null);
            pstmt.setString(5, producto.getEstadoFisico() != null ? producto.getEstadoFisico().name() : null);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear producto físico: " + e.getMessage());
            return false;
        }
    }
    // ======================
    //  Leer producto por ID
    // ======================
    @Override
    public Optional<ProductoFisico> read(int id) {
        String sql = """
            SELECT p.*, c.id_categoria, c.nombre_categoria,
                   pf.id_fabricante, pf.garantia_meses, pf.tipo_garantia, pf.estado_fisico,
                   f.nombre as fabricante_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id_categoria
            LEFT JOIN productos_fisicos pf ON p.id_producto = pf.id_producto
            LEFT JOIN fabricantes f ON pf.id_fabricante = f.id
            WHERE p.id_producto = ?
            """;
        
        ProductoFisico producto = null;
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer producto físico: " + e.getMessage());
        }
        return Optional.ofNullable(producto);
    }
    // ===========================
    //  Actualizar producto físico
    // ===========================
    @Override
    public boolean update(ProductoFisico producto) {
        // Primero actualizar el producto base
        if (!productoDAO.update(producto)) {
            return false;
        }
        
        // Luego actualizar/insertar características físicas usando UPSERT
        String sqlFis = """
            INSERT INTO productos_fisicos 
            (id_producto, id_fabricante, garantia_meses, tipo_garantia, estado_fisico)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (id_producto) DO UPDATE 
            SET id_fabricante = EXCLUDED.id_fabricante,
                garantia_meses = EXCLUDED.garantia_meses,
                tipo_garantia = EXCLUDED.tipo_garantia,
                estado_fisico = EXCLUDED.estado_fisico
            """;

        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sqlFis)) {

            pstmt.setInt(1, producto.getIdProducto());
            
            if (producto.getFabricante() != null) {
                pstmt.setInt(2, producto.getFabricante().getId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            if (producto.getGarantiaMeses() != null) {
                pstmt.setInt(3, producto.getGarantiaMeses());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(4, producto.getTipoGarantia() != null ? producto.getTipoGarantia().name() : null);
            pstmt.setString(5, producto.getEstadoFisico() != null ? producto.getEstadoFisico().name() : null);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto físico: " + e.getMessage());
            return false;
        }
    }
    // ======================================
    //  Eliminar producto físico (soft delete)
    // ======================================
    @Override
    public boolean delete(int id) {
        // Simplemente delegar al DAO base
        return productoDAO.delete(id);
    }
    // ===================================
    //  Listar todos los productos físicos
    // ===================================
    @Override
    public List<ProductoFisico> findAll() {
        List<ProductoFisico> lista = new ArrayList<>();
        String sql = """
            SELECT p.*, c.id_categoria, c.nombre_categoria,
                   pf.id_fabricante, pf.garantia_meses, pf.tipo_garantia, pf.estado_fisico,
                   f.nombre as fabricante_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id_categoria
            JOIN productos_fisicos pf ON p.id_producto = pf.id_producto
            LEFT JOIN fabricantes f ON pf.id_fabricante = f.id
            WHERE p.activo = TRUE
            ORDER BY p.nombre_producto
            """;
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos físicos: " + e.getMessage());
        }
        return lista;
    }
    // ========================
    // Método auxiliar de mapeo
    // ========================
    private ProductoFisico mapResultSet(ResultSet rs) throws SQLException {
        ProductoFisico p = new ProductoFisico();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setNombreProducto(rs.getString("nombre_producto"));
        p.setDescripcionProducto(rs.getString("descripcion_producto"));
        p.setStockProducto(rs.getInt("stock_producto"));
        p.setPrecioProducto(rs.getBigDecimal("precio_producto"));
        p.setCodigoProducto(rs.getInt("codigo_producto"));
        p.setActivo(rs.getBoolean("activo"));
        p.setGarantiaMeses(rs.getInt("garantia_meses"));
        
        // Manejo nullable para enums
        String tipoGarantiaStr = rs.getString("tipo_garantia");
        p.setTipoGarantia(tipoGarantiaStr != null ? ProductoFisico.TipoGarantia.valueOf(tipoGarantiaStr) : null);
        
        String estadoFisicoStr = rs.getString("estado_fisico");
        p.setEstadoFisico(estadoFisicoStr != null ? ProductoFisico.EstadoFisico.valueOf(estadoFisicoStr) : null);

        if (rs.getTimestamp("fecha_creacion") != null) {
            p.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        }
        // Categoría
        int categoriaId = rs.getInt("id_categoria");
        if (!rs.wasNull()) {
            Categoria cat = new Categoria();
            cat.setId(categoriaId);
            cat.setNombre(rs.getString("nombre_categoria"));
            p.setCategoria(cat);
        }
        // Fabricante
        int idFabricante = rs.getInt("id_fabricante");
        if (!rs.wasNull()) {
            Fabricante f = new Fabricante();
            f.setId(idFabricante);
            f.setNombre(rs.getString("fabricante_nombre"));
            p.setFabricante(f);
        }
        return p;
    }
}
