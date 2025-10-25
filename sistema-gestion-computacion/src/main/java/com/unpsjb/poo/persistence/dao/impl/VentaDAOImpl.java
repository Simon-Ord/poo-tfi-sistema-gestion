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
 * Sigue el patrón DAO y respeta los principios de POO.
 */
public class VentaDAOImpl implements DAO<Venta> {

    // Constantes
    private static final double IVA_RATE = 0.21; // 21% de IVA
    
    // ===============
    //  Crear venta
    // ===============
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
            conexion = GestorDeConexion.getInstancia().getConexion();
            conexion.setAutoCommit(false); // Iniciar transacción
            
            // 1. Insertar la venta
            pstmtVenta = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            
            // Cliente puede ser null para TICKET
            if (venta.getClienteFactura() != null && venta.getClienteFactura().getId() != 0) {
                pstmtVenta.setInt(1, venta.getClienteFactura().getId());
            } else {
                pstmtVenta.setNull(1, java.sql.Types.INTEGER);
            }
            
            pstmtVenta.setString(2, venta.getTipoFactura());
            pstmtVenta.setString(3, venta.getEstrategiaPago().getNombreMetodoPago());
            
            // Calcular montos
            double totalCarrito = venta.getCarrito().getTotal().doubleValue();
            
            // Aplicar comisión si existe (antes de calcular IVA)
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
            
            // Obtener el ID generado
            generatedKeys = pstmtVenta.getGeneratedKeys();
            int ventaId = 0;
            if (generatedKeys.next()) {
                ventaId = generatedKeys.getInt(1);
                venta.setIdVenta(ventaId); // Actualizar el ID en el objeto Venta
            } else {
                conexion.rollback();
                return false;
            }
            
            // 2. Insertar los detalles de la venta
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
            
            // 3. Actualizar stock de productos
            String sqlUpdateStock = "UPDATE productos SET stock_producto = stock_producto - ? WHERE id_producto = ?";
            try (PreparedStatement pstmtStock = conexion.prepareStatement(sqlUpdateStock)) {
                for (ItemCarrito item : venta.getCarrito().getItems()) {
                    pstmtStock.setInt(1, item.getCantidad());
                    pstmtStock.setInt(2, item.getProducto().getIdProducto());
                    pstmtStock.addBatch();
                }
                pstmtStock.executeBatch();
            }
            
            // Confirmar transacción
            conexion.commit();
            System.out.println("Venta creada exitosamente con ID: " + ventaId);
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar la venta: " + e.getMessage());
            e.printStackTrace();
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // Cerrar recursos en orden inverso
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmtDetalle != null) pstmtDetalle.close();
                if (pstmtVenta != null) pstmtVenta.close();
                if (conexion != null) {
                    conexion.setAutoCommit(true);
                    conexion.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    // =====================
    //  Leer venta por ID
    // =====================
    @Override
    public Optional<Venta> read(int id) {
        // Por ahora no es necesario implementar la lectura completa
        // Ya que el sistema solo necesita crear ventas
        // Se puede implementar en el futuro si se necesita
        return Optional.empty();
    }

    // =====================
    //  Actualizar venta
    // =====================
    @Override
    public boolean update(Venta venta) {
        // No se necesita actualizar ventas en este sistema
        // Las ventas son registros inmutables una vez creadas
        return false;
    }

    // ===================================
    //  Eliminar venta (no permitido)
    // ===================================
    @Override
    public boolean delete(int id) {
        // No se permite eliminar ventas
        // Solo se pueden consultar o anular mediante un campo de estado
        return false;
    }

    // ===================================
    //  Listar todas las ventas
    // ===================================
    @Override
    public List<Venta> findAll() {
        // Por ahora retornamos lista vacía
        // Se puede implementar en el futuro si se necesita listar ventas
        return new ArrayList<>();
    }
}
