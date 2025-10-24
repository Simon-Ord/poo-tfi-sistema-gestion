package com.unpsjb.poo.persistence.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.CarritoDeCompra;
import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.model.ItemCarrito;
import com.unpsjb.poo.model.Venta;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.VentaDAO;

/**
 * Implementación del DAO para la entidad Venta.
 * Maneja la persistencia de ventas y sus detalles en la base de datos.
 */
public class VentaDAOImpl implements VentaDAO {

    @Override
    public boolean create(Venta venta) {
        String sqlFactura = "INSERT INTO facturas (fecha, cliente_id, total, tipo_factura, metodo_pago) VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_factura (factura_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmtFactura = null;
        PreparedStatement stmtDetalle = null;
        
        try {
            conn = GestorDeConexion.getInstancia().getConexion();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Insertar factura
            stmtFactura = conn.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);
            stmtFactura.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            
            // Cliente puede ser null (consumidor final)
            if (venta.getClienteFactura() != null) {
                stmtFactura.setInt(2, venta.getClienteFactura().getId());
            } else {
                stmtFactura.setNull(2, java.sql.Types.INTEGER);
            }
            
            stmtFactura.setBigDecimal(3, venta.getCarrito().getTotal());
            stmtFactura.setString(4, venta.getTipoFactura());
            stmtFactura.setString(5, venta.getEstrategiaPago() != null ? 
                venta.getEstrategiaPago().getNombreMetodoPago() : null);
            
            int filasAfectadas = stmtFactura.executeUpdate();
            
            if (filasAfectadas == 0) {
                conn.rollback();
                return false;
            }
            
            // Obtener el ID generado de la factura
            ResultSet generatedKeys = stmtFactura.getGeneratedKeys();
            if (!generatedKeys.next()) {
                conn.rollback();
                return false;
            }
            
            int facturaId = generatedKeys.getInt(1);
            
            // Insertar detalles de la factura
            stmtDetalle = conn.prepareStatement(sqlDetalle);
            for (ItemCarrito item : venta.getCarrito().getItems()) {
                stmtDetalle.setInt(1, facturaId);
                stmtDetalle.setInt(2, item.getProducto().getIdProducto());
                stmtDetalle.setInt(3, item.getCantidad());
                stmtDetalle.setBigDecimal(4, item.getPrecioUnitario());
                stmtDetalle.setBigDecimal(5, item.getSubtotal());
                stmtDetalle.addBatch();
            }
            
            stmtDetalle.executeBatch();
            
            // Actualizar stock de productos
            actualizarStockProductos(venta, conn);
            
            conn.commit(); // Confirmar transacción
            System.out.println("Venta guardada exitosamente con ID: " + facturaId);
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al guardar venta: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            cerrarRecursos(conn, stmtFactura, stmtDetalle);
        }
    }

    private void actualizarStockProductos(Venta venta, Connection conn) throws SQLException {
        String sqlUpdateStock = "UPDATE productos SET stock_producto = stock_producto - ? WHERE id_producto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateStock)) {
            for (ItemCarrito item : venta.getCarrito().getItems()) {
                stmt.setInt(1, item.getCantidad());
                stmt.setInt(2, item.getProducto().getIdProducto());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public Optional<Venta> read(int id) {
        // Este método no es necesario para la funcionalidad básica de ventas
        // pero se implementa para cumplir con la interfaz DAO
        return Optional.empty();
    }

    @Override
    public boolean update(Venta venta) {
        // Las ventas normalmente no se modifican, solo se crean
        return false;
    }

    @Override
    public boolean delete(int id) {
        // Las ventas no se eliminan, solo se anulan si es necesario
        return false;
    }

    @Override
    public List<Venta> findAll() {
        // Este método se puede implementar después si es necesario listar ventas
        return new ArrayList<>();
    }

    private void cerrarRecursos(Connection conn, PreparedStatement... statements) {
        for (PreparedStatement stmt : statements) {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
