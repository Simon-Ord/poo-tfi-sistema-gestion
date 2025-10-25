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

    // Método para buscar productos activos y devuelve lista de productos
    public List<Producto> buscarProductos(String termino) {
        String sql = """
            SELECT p.*, c.id_categoria as categoria_id, c.nombre_categoria as categoria_nombre 
            FROM productos p 
            LEFT JOIN categorias c ON p.categoria_id = c.id_categoria 
            WHERE p.activo = TRUE 
            AND (LOWER(p.nombre_producto) LIKE ? 
                 OR LOWER(p.descripcion_producto) LIKE ? 
                 OR LOWER(c.nombre_categoria) LIKE ? 
                 OR CAST(p.codigo_producto AS TEXT) LIKE ?
                 OR CAST(p.precio_producto AS TEXT) LIKE ?
                 OR CAST(p.stock_producto AS TEXT) LIKE ?)
            ORDER BY p.nombre_producto
            """;
        
        List<Producto> productos = new ArrayList<>();
        String terminoBusqueda = "%" + termino.toLowerCase() + "%";
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            // Setear el mismo término para todos los campos
            pstmt.setString(1, terminoBusqueda);
            pstmt.setString(2, terminoBusqueda);
            pstmt.setString(3, terminoBusqueda);
            pstmt.setString(4, terminoBusqueda);
            pstmt.setString(5, terminoBusqueda);
            pstmt.setString(6, terminoBusqueda);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
        }
        return productos;
    }

    // Método para buscar productos incluyendo inactivos y devuelve lista de productos
    public List<Producto> buscarProductosCompleto(String termino) {
        String sql = """
            SELECT p.*, c.id_categoria as categoria_id, c.nombre_categoria as categoria_nombre 
            FROM productos p 
            LEFT JOIN categorias c ON p.categoria_id = c.id_categoria 
            WHERE (LOWER(p.nombre_producto) LIKE ? 
                   OR LOWER(p.descripcion_producto) LIKE ? 
                   OR LOWER(c.nombre_categoria) LIKE ? 
                   OR CAST(p.codigo_producto AS TEXT) LIKE ?
                   OR CAST(p.precio_producto AS TEXT) LIKE ?
                   OR CAST(p.stock_producto AS TEXT) LIKE ?)
            ORDER BY p.activo DESC, p.nombre_producto
            """;
        
        List<Producto> productos = new ArrayList<>();
        String terminoBusqueda = "%" + termino.toLowerCase() + "%";
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            // Setear el mismo término para todos los campos
            pstmt.setString(1, terminoBusqueda);
            pstmt.setString(2, terminoBusqueda);
            pstmt.setString(3, terminoBusqueda);
            pstmt.setString(4, terminoBusqueda);
            pstmt.setString(5, terminoBusqueda);
            pstmt.setString(6, terminoBusqueda);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos completo: " + e.getMessage());
        }
        return productos;
    }

 public Optional<Producto> findByCodigo(String codigo) {
    String sql = """
        SELECT p.*, c.id_categoria as categoria_id, c.nombre_categoria as categoria_nombre 
        FROM productos p 
        LEFT JOIN categorias c ON p.categoria_id = c.id_categoria 
        WHERE p.codigo_producto = ? AND p.activo = TRUE
        """; 
    Producto producto = null;

    try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {

        // 1. Convertir el String de la interfaz a INT (porque codigoProducto es INT)
        int codigoInt = Integer.parseInt(codigo);
        pstmt.setInt(1, codigoInt);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                producto = mapResultSet(rs);
            }
        }
    } catch (NumberFormatException e) {
        // Captura si el vendedor escribe letras en el campo de código
        System.err.println("Error: El código '" + codigo + "' no es un número válido.");
        return Optional.empty(); 
    } catch (SQLException e) {
        System.err.println("Error al buscar producto por código: " + e.getMessage());
    }

    // Devuelve el producto encontrado (o vacío si no existe)
    return Optional.ofNullable(producto);

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
}