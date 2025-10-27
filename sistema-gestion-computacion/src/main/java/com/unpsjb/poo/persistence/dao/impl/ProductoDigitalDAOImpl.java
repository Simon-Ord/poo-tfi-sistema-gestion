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
import com.unpsjb.poo.model.productos.ProductoDigital;
import com.unpsjb.poo.model.productos.ProveedorDigital;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class ProductoDigitalDAOImpl implements DAO<ProductoDigital> {

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();

    // =========================
    //  Crear producto digital
    // =========================
    @Override
    public boolean create(ProductoDigital producto) {
        // Primero crear el producto base
        if (!productoDAO.create(producto)) {
            return false;
        }
        // Luego agregar características digitales
        String sqlDigital = """
            INSERT INTO productos_digitales 
            (id_producto, id_proveedor_digital, tipo_licencia, activaciones_max, duracion_licencia_dias)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sqlDigital)) {

            pstmt.setInt(1, producto.getIdProducto());
            
            if (producto.getProveedorDigital() != null) {
                pstmt.setInt(2, producto.getProveedorDigital().getId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(3, producto.getTipoLicencia() != null ? producto.getTipoLicencia().name() : null);
            
            if (producto.getActivacionesMax() != null) {
                pstmt.setInt(4, producto.getActivacionesMax());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            if (producto.getDuracionLicenciaDias() != null) {
                pstmt.setInt(5, producto.getDuracionLicenciaDias());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear producto digital: " + e.getMessage());
            return false;
        }
    }
    // =======================
    //  Leer producto por ID
    // =======================
    @Override
    public Optional<ProductoDigital> read(int id) {
        String sql = """
            SELECT p.*, c.id_categoria, c.nombre_categoria,
                   pd.id_proveedor_digital, pd.tipo_licencia, pd.activaciones_max, pd.duracion_licencia_dias,
                   prov.nombre as proveedor_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id_categoria
            LEFT JOIN productos_digitales pd ON p.id_producto = pd.id_producto
            LEFT JOIN proveedores_digitales prov ON pd.id_proveedor_digital = prov.id
            WHERE p.id_producto = ?
            """;
        
        ProductoDigital producto = null;
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer producto digital: " + e.getMessage());
        }
        return Optional.ofNullable(producto);
    }
    // ============================
    //  Actualizar producto digital
    // ============================
    @Override
    public boolean update(ProductoDigital producto) {
        // Primero actualizar el producto base
        if (!productoDAO.update(producto)) {
            return false;
        }
        // Luego actualizar/insertar características digitales usando UPSERT
        String sqlDig = """
            INSERT INTO productos_digitales 
            (id_producto, id_proveedor_digital, tipo_licencia, activaciones_max, duracion_licencia_dias)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (id_producto) DO UPDATE 
            SET id_proveedor_digital = EXCLUDED.id_proveedor_digital,
                tipo_licencia = EXCLUDED.tipo_licencia,
                activaciones_max = EXCLUDED.activaciones_max,
                duracion_licencia_dias = EXCLUDED.duracion_licencia_dias
            """;

        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sqlDig)) {

            pstmt.setInt(1, producto.getIdProducto());
            
            if (producto.getProveedorDigital() != null) {
                pstmt.setInt(2, producto.getProveedorDigital().getId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(3, producto.getTipoLicencia() != null ? producto.getTipoLicencia().name() : null);
            
            if (producto.getActivacionesMax() != null) {
                pstmt.setInt(4, producto.getActivacionesMax());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            if (producto.getDuracionLicenciaDias() != null) {
                pstmt.setInt(5, producto.getDuracionLicenciaDias());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto digital: " + e.getMessage());
            return false;
        }
    }
    // =======================================
    //  Eliminar producto digital (soft delete)
    // =======================================
    @Override
    public boolean delete(int id) {
        // Simplemente delegar al DAO base
        return productoDAO.delete(id);
    }
    // ====================================
    //  Listar todos los productos digitales
    // ====================================
    @Override
    public List<ProductoDigital> findAll() {
        List<ProductoDigital> lista = new ArrayList<>();
        String sql = """
            SELECT p.*, c.id_categoria, c.nombre_categoria,
                   pd.id_proveedor_digital, pd.tipo_licencia, pd.activaciones_max, pd.duracion_licencia_dias,
                   prov.nombre as proveedor_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id_categoria
            JOIN productos_digitales pd ON p.id_producto = pd.id_producto
            LEFT JOIN proveedores_digitales prov ON pd.id_proveedor_digital = prov.id
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
            System.err.println("Error al obtener productos digitales: " + e.getMessage());
        }
        return lista;
    }

    // ========================
    // Método auxiliar de mapeo
    // ========================
    private ProductoDigital mapResultSet(ResultSet rs) throws SQLException {
        ProductoDigital p = new ProductoDigital();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setNombreProducto(rs.getString("nombre_producto"));
        p.setDescripcionProducto(rs.getString("descripcion_producto"));
        p.setStockProducto(rs.getInt("stock_producto"));
        p.setPrecioProducto(rs.getBigDecimal("precio_producto"));
        p.setCodigoProducto(rs.getInt("codigo_producto"));
        p.setActivo(rs.getBoolean("activo"));
        p.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        p.setTipoLicencia(ProductoDigital.TipoLicencia.valueOf(rs.getString("tipo_licencia")));
        p.setActivacionesMax(rs.getInt("activaciones_max"));
        p.setDuracionLicenciaDias(rs.getInt("duracion_licencia_dias"));

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
        // Proveedor digital
        int idProveedor = rs.getInt("id_proveedor_digital");
        if (!rs.wasNull()) {
            ProveedorDigital prov = new ProveedorDigital();
            prov.setId(idProveedor);
            prov.setNombre(rs.getString("proveedor_nombre"));
            p.setProveedorDigital(prov);
        }
       return p;
    }
}
