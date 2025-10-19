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
    public boolean create(Producto producto) {
        String sql = """
            INSERT INTO productos 
            (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto, fabricante_producto, codigo_producto, estado, activo)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        // Nota: La columna 'id' (código_producto) es SERIAL, se genera sola. No se debe incluir aquí.
        // Si tienes una columna 'codigo' separada de 'id', debes incluirla. Asumo que usas 'id' como código.
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setString(2, producto.getDescripcionProducto());
            pstmt.setInt(3, producto.getStockProducto());
            pstmt.setBigDecimal(4, producto.getPrecioProducto());
            pstmt.setString(5, producto.getCategoriaProducto());
            pstmt.setString(6, producto.getFabricanteProducto());
            pstmt.setInt(7, producto.getCodigoProducto());
            pstmt.setBoolean(8, producto.isEstado());
            pstmt.setBoolean(9, producto.isActivo()); 
            int filas = pstmt.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
            return false;
        }
    }

    // ================================
    //  Leer producto por ID
    // ================================
    @Override
    public Optional<Producto> read(int id) {
        // COLUMNA CORREGIDA: Usando 'id' en lugar de 'id_producto'
        String sql = "SELECT * FROM productos WHERE id = ?"; 
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
    //  Actualizar producto
    // ================================
    @Override
    public boolean update (Producto producto) {
        String sql = """
            UPDATE productos 
            SET nombre_producto = ?, descripcion_producto = ?, stock_producto = ?, precio_producto = ?, 
                categoria_producto = ?, fabricante_producto = ?, codigo_producto = ?, estado = ?, activo = ?
            WHERE id_producto = ?
            """;
        // Nota: Si 'codigo_producto' es el mismo que 'id', no tiene sentido actualizarlo
        // SET nombre = ?, ..., id = ? (solo si el id no es la PK). Asumo que actualizas por id.
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setString(2, producto.getDescripcionProducto());
            pstmt.setInt(3, producto.getStockProducto());
            pstmt.setBigDecimal(4, producto.getPrecioProducto());
            pstmt.setString(5, producto.getCategoriaProducto());
            pstmt.setString(6, producto.getFabricanteProducto());
            pstmt.setInt(7, producto.getCodigoProducto());
            pstmt.setBoolean(8, producto.isEstado());
            pstmt.setBoolean(9, producto.isActivo());
            pstmt.setInt(10, producto.getIdProducto());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
        return false;
    }

    // ===================================
    //  Eliminar producto (estado = false)
    // ===================================
@Override
public boolean delete(int id) {
    // Para "eliminar" del sistema sin borrar la fila, marcamos estado = FALSE
    String sql = "UPDATE productos SET estado = FALSE WHERE id_producto = ?";
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
    // ============================
    //  Listar todos los productos 
    // ============================
    @Override
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE estado = TRUE ORDER BY nombre_producto";
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
    // ---------------------------
    // Método auxiliar de mapeo - ¡CORREGIDO!
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
        try {
            p.setEstado(rs.getBoolean("estado"));
        } catch (SQLException e) {
            p.setEstado(true);
        }
        try {
            p.setActivo(rs.getBoolean("activo"));
        } catch (SQLException e) {
            p.setActivo(true);
        }
        return p;
    }
}