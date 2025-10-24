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
import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class ProductoDAOImpl implements DAO<Producto> {
    // ===============
    //  Crear producto
    // ===============
    @Override
    public boolean create(Producto producto) {
        String sql = """
            INSERT INTO productos 
            (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_id, 
            codigo_producto, activo, fecha_creacion)
            VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            """;
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setString(2, producto.getDescripcionProducto());
            pstmt.setInt(3, producto.getStockProducto());
            pstmt.setBigDecimal(4, producto.getPrecioProducto());
            
            // Manejo seguro de la categoría
            if (producto.getCategoria() != null) {
                pstmt.setInt(5, producto.getCategoria().getId());
            } else {
                pstmt.setInt(5, 1); // Categoría por defecto
            }
            
            pstmt.setInt(6, producto.getCodigoProducto());
            pstmt.setBoolean(7, producto.isActivo());
            int filas = pstmt.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
            return false;
        }
    }
    // =====================
    //  Leer producto por ID
    // =====================
    @Override
    public Optional<Producto> read(int id) {
        String sql = """
            SELECT p.*, c.id_categoria as categoria_id, c.nombre_categoria as categoria_nombre 
            FROM productos p 
            LEFT JOIN categorias c ON p.categoria_id = c.id_categoria 
            WHERE p.id_producto = ?
            """; 
        Producto producto = null;
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el producto: " + e.getMessage());
        }

        return Optional.ofNullable(producto);
    }
    // =====================
    //  Actualizar producto
    // =====================
    @Override
    public boolean update(Producto producto) {
        String sql = """
            UPDATE productos 
            SET nombre_producto = ?, descripcion_producto = ?, stock_producto = ?, precio_producto = ?, 
                categoria_id = ?, codigo_producto = ?, activo = ?
            WHERE id_producto = ?
            """;
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setString(2, producto.getDescripcionProducto());
            pstmt.setInt(3, producto.getStockProducto());
            pstmt.setBigDecimal(4, producto.getPrecioProducto());
            
            // Manejo seguro de la categoría
            if (producto.getCategoria() != null) {
                pstmt.setInt(5, producto.getCategoria().getId());
            } else {
                pstmt.setInt(5, 1); // Categoría por defecto
            }
            
            pstmt.setInt(6, producto.getCodigoProducto());
            pstmt.setBoolean(7, producto.isActivo());
            pstmt.setInt(8, producto.getIdProducto());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
        return false;
    }
    // ===================================
    //  Eliminar producto (activo = false)
    // ===================================
@Override
public boolean delete(int id) {
    String sql = "UPDATE productos SET activo = FALSE WHERE id_producto = ?";
    try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        int filas = pstmt.executeUpdate();
        return filas > 0;
    } catch (SQLException e) {
        System.err.println("Error al desactivar el producto: " + e.getMessage());
        return false;
    }
}
    // ===================================
    //  Listar todos los productos activos
    // ===================================
    @Override
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        String sql = """
            SELECT p.*, c.id_categoria as categoria_id, c.nombre_categoria as categoria_nombre 
            FROM productos p 
            LEFT JOIN categorias c ON p.categoria_id = c.id_categoria 
            WHERE p.activo = TRUE 
            ORDER BY p.nombre_producto
            """;
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productos.add(mapResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
        }
        return productos;
    }
    // ===============================================
    //  Listar todos los productos activos e Inactivos
    // ===============================================
    public List<Producto> findAllCompleto() {
        List<Producto> productos = new ArrayList<>();
        String sql = """
            SELECT p.*, c.id_categoria as categoria_id, c.nombre_categoria as categoria_nombre 
            FROM productos p 
            LEFT JOIN categorias c ON p.categoria_id = c.id_categoria 
            ORDER BY p.nombre_producto
            """;
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
        }
        return productos;
    }
    // =====================================================
    //  Comprueba si un producto esta activo (activo = true)
    // =====================================================
public boolean estaActivo(int id) {
    String sql = "SELECT activo FROM productos WHERE id_producto = ?";
    try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean("activo");
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al comprobar si esta activo el producto: " + e.getMessage());
    }
    return false;
}
    // =====================================
    //  Reactiva un producto (activo = true)
    // =====================================
public boolean reactivar(int id) {
    String sql = "UPDATE productos SET activo = TRUE WHERE id_producto = ?";
    try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        int filas = pstmt.executeUpdate();
        return filas > 0;
    } catch (SQLException e) {
        System.err.println("Error al reactivar el producto: " + e.getMessage());
        return false;
    }
}
    // ========================
    // Método auxiliar de mapeo
    // ========================
    private Producto mapResultSet(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto")); 
        p.setNombreProducto(rs.getString("nombre_producto"));
        p.setDescripcionProducto(rs.getString("descripcion_producto"));
        p.setStockProducto(rs.getInt("stock_producto"));
        p.setPrecioProducto(rs.getBigDecimal("precio_producto"));
        p.setCodigoProducto(rs.getInt("codigo_producto"));
        p.setActivo(rs.getBoolean("activo"));
        
        // Obtener la categoría
        int categoriaId = rs.getInt("categoria_id");
        if (!rs.wasNull()) {
            Categoria cat = new Categoria();
            cat.setId(categoriaId);
            cat.setNombre(rs.getString("categoria_nombre"));
            p.setCategoria(cat);
        }
        
        if (rs.getTimestamp("fecha_creacion") != null) {
            p.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        }
        return p;
    }
    public Optional<Producto> findByCodigo(String codigo) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

}