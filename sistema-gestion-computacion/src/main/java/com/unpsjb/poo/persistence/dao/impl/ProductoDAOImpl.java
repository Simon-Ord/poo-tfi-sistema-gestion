package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.Producto;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class ProductoDAOImpl implements DAO<Producto> {

    @Override
    public void create(Producto producto) {
        String sql = """
            INSERT INTO productos 
            (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto, fabricante_producto, codigo_producto)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setString(2, producto.getDescripcionProducto());
            pstmt.setInt(3, producto.getStockProducto());
            pstmt.setBigDecimal(4, producto.getPrecioProducto());
            pstmt.setString(5, producto.getCategoriaProducto());
            pstmt.setString(6, producto.getFabricanteProducto());
            pstmt.setInt(7, producto.getCodigoProducto());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
        }
    }

    // ================================
    //  Leer producto por ID
    // ================================
    @Override
    public Optional<Producto> read(int id) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
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

    // ================================
    //  Actualizar producto
    // ================================
    @Override
    public void update(Producto producto) {
        String sql = """
            UPDATE productos 
            SET nombre_producto = ?, descripcion_producto = ?, stock_producto = ?, precio_producto = ?, 
                categoria_producto = ?, fabricante_producto = ?, codigo_producto = ?
            WHERE id_producto = ?
            """;
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setString(2, producto.getDescripcionProducto());
            pstmt.setInt(3, producto.getStockProducto());
            pstmt.setBigDecimal(4, producto.getPrecioProducto());
            pstmt.setString(5, producto.getCategoriaProducto());
            pstmt.setString(6, producto.getFabricanteProducto());
            pstmt.setInt(7, producto.getCodigoProducto());
            pstmt.setInt(8, producto.getIdProducto());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    // ================================
    //  Eliminar producto
    // ================================
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
        }
    }
    // ================================
    //  Listar todos los productos
    // ================================
    @Override
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY id_producto";
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
    // ---------------------------
    // Método auxiliar de mapeo 
    // ---------------------------
    private Producto mapResultSet(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setNombreProducto(rs.getString("nombre_producto"));
        p.setDescripcionProducto(rs.getString("descripcion_producto"));
        p.setStockProducto(rs.getInt("stock_producto"));
        p.setPrecioProducto(rs.getBigDecimal("precio_producto"));
        p.setCategoriaProducto(rs.getString("categoria_producto"));
        p.setFabricanteProducto(rs.getString("fabricante_producto"));
        p.setCodigoProducto(rs.getInt("codigo_producto"));
        return p;
    }
}
