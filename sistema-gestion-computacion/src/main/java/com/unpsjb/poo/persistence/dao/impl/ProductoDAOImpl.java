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
        // COLUMNAS CORREGIDAS: Usando los nombres de la tabla 'productos' (id, nombre, stock, etc.)
        String sql = """
            INSERT INTO productos 
            (nombre, descripcion, stock, precio, categoria, fabricante) 
            VALUES (?, ?, ?, ?, ?, ?)
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
            
            // Si el campo 'codigo_producto' de tu modelo es el 'id' de la DB, NO debe setearse aquí.
            // Si tienes un campo 'codigo' separado de 'id', entonces ajusta el SQL y el índice.
            // pstmt.setInt(7, producto.getCodigoProducto()); // Línea eliminada/comentada por ser serial.
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
            e.printStackTrace(); // Añade esto para ver el detalle exacto del error.
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
    public void update(Producto producto) {
        // COLUMNAS CORREGIDAS: Usando nombres de la DB y 'id' en el WHERE
        String sql = """
            UPDATE productos 
            SET nombre = ?, descripcion = ?, stock = ?, precio = ?, 
                categoria = ?, fabricante = ?
            WHERE id = ?
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
            pstmt.setInt(7, producto.getIdProducto()); // Se usa para el WHERE id = ?
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    // ================================
    //  Eliminar producto
    // ================================
    @Override
    public void delete(int id) {
        // COLUMNA CORREGIDA: Usando 'id' en lugar de 'id_producto'
        String sql = "DELETE FROM productos WHERE id = ?"; 
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
        }
    }
    // ================================
    //  Listar todos los productos
    // ================================
    @Override
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        // COLUMNA CORREGIDA: Usando 'id' en lugar de 'id_producto'
        String sql = "SELECT * FROM productos ORDER BY id";
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
    // Método auxiliar de mapeo - ¡CORREGIDO!
    // ---------------------------
    private Producto mapResultSet(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        // Mapeo corregido a nombres de columna de la DB (id, nombre, stock, etc.)
        p.setIdProducto(rs.getInt("id")); 
        p.setNombreProducto(rs.getString("nombre"));
        p.setDescripcionProducto(rs.getString("descripcion"));
        p.setStockProducto(rs.getInt("stock"));
        p.setPrecioProducto(rs.getBigDecimal("precio"));
        p.setCategoriaProducto(rs.getString("categoria"));
        p.setFabricanteProducto(rs.getString("fabricante"));
        // Si tu modelo tiene getCodigoProducto, asumo que mapea al mismo 'id' (serial PK)
        p.setCodigoProducto(rs.getInt("id")); 
        return p;
    }
}