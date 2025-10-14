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

/**
 * Implementación del DAO para la entidad Producto.
 * Permite realizar operaciones CRUD sobre la tabla 'productos'.
 */
public class ProductoDAOImpl implements DAO<Producto> {

    /**
     * Inserta un nuevo producto en la base de datos.
     * @param producto El producto a insertar.
     */
    @Override
    public void create(Producto producto) {
        String sql = "INSERT INTO productos (nombreProducto, descripcionProducto, stockProducto, precioProducto, categoriaProducto, fabricanteProducto) VALUES (?, ?, ?, ?, ?, ?)";
        // Usamos try-with-resources para asegurar el cierre de la conexión y el PreparedStatement
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setString(2, producto.getDescripcionProducto());
            pstmt.setInt(3, producto.getStockProducto());
            pstmt.setBigDecimal(4, producto.getPrecioProducto());
            pstmt.setString(5, producto.getCategoriaProducto());
            pstmt.setString(6, producto.getFabricanteProducto());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
        }
    }

    /**
     * Busca un producto por su id.
     * @param id El id del producto.
     * @return Un Optional con el producto si existe, o vacío si no se encuentra.
     */
    @Override
    public Optional<Producto> read(int id) {
        String sql = "SELECT * FROM productos WHERE idProducto = ?";
        Producto producto = null;
        // Usamos try-with-resources para asegurar el cierre de la conexión y el PreparedStatement
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            // El ResultSet también se cierra automáticamente
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto();
                    producto.setId(rs.getInt("idProducto"));
                    producto.setNombreProducto(rs.getString("nombreProducto"));
                    producto.setDescripcionProducto(rs.getString("descripcionProducto"));
                    producto.setStockProducto(rs.getInt("stockProducto"));
                    producto.setPrecioProducto(rs.getBigDecimal("precioProducto"));
                    producto.setCategoriaProducto(rs.getString("categoriaProducto"));
                    producto.setFabricanteProducto(rs.getString("fabricanteProducto"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el producto: " + e.getMessage());
        }
        // Optional evita problemas con valores nulos
        return Optional.ofNullable(producto);
    }

    /**
     * Actualiza los datos de un producto existente.
     * @param producto El producto con los datos actualizados.
     */
    @Override
    public void update(Producto producto) {
        String sql = "UPDATE productos SET nombreProducto = ?, descripcionProducto = ?, stockProducto = ?, precioProducto = ?, categoriaProducto = ?, fabricanteProducto = ? WHERE idProducto = ?";
        // Usamos try-with-resources para asegurar el cierre de la conexión y el PreparedStatement
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setString(2, producto.getDescripcionProducto());
            pstmt.setInt(3, producto.getStockProducto());
            pstmt.setBigDecimal(4, producto.getPrecioProducto());
            pstmt.setString(5, producto.getCategoriaProducto());
            pstmt.setString(6, producto.getFabricanteProducto());
            pstmt.setInt(7, producto.getId()); // Es importante pasar el id para identificar el registro a actualizar
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    /**
     * Elimina un producto por su id.
     * @param id El id del producto a eliminar.
     */
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM productos WHERE idProducto = ?";
        // Usamos try-with-resources para asegurar el cierre de la conexión y el PreparedStatement
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los productos de la base de datos.
     * @return Una lista con todos los productos.
     */
    @Override
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        // Usamos try-with-resources para asegurar el cierre de la conexión, el Statement y el ResultSet
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("idProducto"));
                producto.setNombreProducto(rs.getString("nombreProducto"));
                producto.setDescripcionProducto(rs.getString("descripcionProducto"));
                producto.setStockProducto(rs.getInt("stockProducto"));
                producto.setPrecioProducto(rs.getBigDecimal("precioProducto"));
                producto.setCategoriaProducto(rs.getString("categoriaProducto"));
                producto.setFabricanteProducto(rs.getString("fabricanteProducto"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al encontrar todos los productos: " + e.getMessage());
        }
        return productos;}
}
