package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.ItemCarrito;
import com.unpsjb.poo.model.Venta;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

/**
 * Implementación del DAO para gestionar la persistencia de Ventas.
 * Aplica el patrón DAO (Data Access Object) con control transaccional.
 * 
 * Se encarga de:
 *  - Insertar ventas (con detalle)
 *  - Actualizar stock de productos
 *  - Manejar transacciones y rollback en caso de error
 */
public class VentaDAOImpl implements DAO<Venta> {

    // Constante de IVA (21%)
    private static final double IVA_RATE = 0.21;

    // =====================================================
    // 🧾 CREAR UNA VENTA
    // =====================================================
    @Override
    public boolean create(Venta venta) {
        String sqlVenta = """
            INSERT INTO ventas 
            (cliente_id, tipo_factura, metodo_pago, subtotal, iva, total)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        String sqlDetalle = """
            INSERT INTO detalle_venta 
            (venta_id, producto_id, cantidad, precio_unitario, subtotal)
            VALUES (?, ?, ?, ?, ?)
            """;

        Connection conexion = null;
        PreparedStatement pstmtVenta = null;
        PreparedStatement pstmtDetalle = null;
        ResultSet generatedKeys = null;

        try {
            // 1️⃣ Conexión e inicio de transacción
            conexion = GestorDeConexion.getInstancia().getConexion();
            conexion.setAutoCommit(false);

            // 2️⃣ Insertar la cabecera de la venta
            pstmtVenta = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);

            // Cliente puede ser null si es un ticket
            if (venta.getClienteFactura() != null && venta.getClienteFactura().getId() != 0) {
                pstmtVenta.setInt(1, venta.getClienteFactura().getId());
            } else {
                pstmtVenta.setNull(1, java.sql.Types.INTEGER);
            }

            pstmtVenta.setString(2, venta.getTipoFactura());
            pstmtVenta.setString(3, venta.getEstrategiaPago().getNombreMetodoPago());

            // Calcular totales
            double totalCarrito = venta.getCarrito().getTotal().doubleValue();
            double comision = venta.getEstrategiaPago().getComision();
            double totalConComision = totalCarrito * (1 + comision);

            // Calcular IVA sobre el total con comisión
            double subtotalSinIva = totalConComision / (1 + IVA_RATE);
            double iva = totalConComision - subtotalSinIva;

            pstmtVenta.setBigDecimal(4, java.math.BigDecimal.valueOf(subtotalSinIva));
            pstmtVenta.setBigDecimal(5, java.math.BigDecimal.valueOf(iva));
            pstmtVenta.setBigDecimal(6, java.math.BigDecimal.valueOf(totalConComision));

            int filasVenta = pstmtVenta.executeUpdate();
            if (filasVenta == 0) {
                conexion.rollback();
                return false;
            }

            // 3️⃣ Obtener ID generado
            generatedKeys = pstmtVenta.getGeneratedKeys();
            int ventaId = 0;
            if (generatedKeys.next()) {
                ventaId = generatedKeys.getInt(1);
                venta.setIdVenta(ventaId);
            } else {
                conexion.rollback();
                return false;
            }

            // 4️⃣ Insertar detalle de venta
            pstmtDetalle = conexion.prepareStatement(sqlDetalle);
            for (ItemCarrito item : venta.getCarrito().getItems()) {
                pstmtDetalle.setInt(1, ventaId);
                pstmtDetalle.setInt(2, item.getProducto().getIdProducto());
                pstmtDetalle.setInt(3, item.getCantidad());
                pstmtDetalle.setBigDecimal(4, item.getPrecioUnitario());
                pstmtDetalle.setBigDecimal(5, item.getSubtotal());
                pstmtDetalle.addBatch();
            }
            pstmtDetalle.executeBatch();

            // 5️⃣ Actualizar stock de productos
            String sqlUpdateStock = "UPDATE productos SET stock_producto = stock_producto - ? WHERE id_producto = ?";
            try (PreparedStatement pstmtStock = conexion.prepareStatement(sqlUpdateStock)) {
                for (ItemCarrito item : venta.getCarrito().getItems()) {
                    pstmtStock.setInt(1, item.getCantidad());
                    pstmtStock.setInt(2, item.getProducto().getIdProducto());
                    pstmtStock.addBatch();
                }
                pstmtStock.executeBatch();
            }

            // 6️⃣ Confirmar transacción
            conexion.commit();
            System.out.println("✅ Venta creada exitosamente con ID: " + ventaId);
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar la venta: " + e.getMessage());
            e.printStackTrace();

            // Rollback en caso de error
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("⚠️ Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;

        } finally {
            // 7️⃣ Cierre de recursos
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmtDetalle != null) pstmtDetalle.close();
                if (pstmtVenta != null) pstmtVenta.close();
                if (conexion != null) {
                    conexion.setAutoCommit(true);
                    conexion.close();
                }
            } catch (SQLException e) {
                System.err.println("⚠️ Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    // =====================================================
    // 🔍 LEER UNA VENTA POR ID (por ahora no usada)
    // =====================================================
    @Override
    public Optional<Venta> read(int id) {
        // En esta versión no es necesario (solo se crean ventas)
        return Optional.empty();
    }

    // =====================================================
    // ✏️ ACTUALIZAR VENTA (no aplicable)
    // =====================================================
    @Override
    public boolean update(Venta venta) {
        // Las ventas no se actualizan una vez registradas
        return false;
    }

    // =====================================================
    // 🗑 ELIMINAR VENTA (no permitido)
    // =====================================================
    @Override
    public boolean delete(int id) {
        // No se elimina físicamente una venta
        // Si se anula, debe hacerse cambiando su estado en la base
        return false;
    }

    // =====================================================
    // 📋 LISTAR TODAS LAS VENTAS (opcional)
    // =====================================================
    @Override
    public List<Venta> findAll() {
        // Se puede implementar en el futuro para reportes
        return new ArrayList<>();
    }
}
