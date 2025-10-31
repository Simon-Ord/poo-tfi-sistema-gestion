package com.unpsjb.poo.persistence.dao.impl;

import java.sql.*;
import java.util.*;
import com.unpsjb.poo.model.*;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

/**
 * ✅ Implementación del DAO para gestionar la persistencia de Ventas.
 */
public class VentaDAOImpl implements DAO<Venta> {

    private static final double IVA_RATE = 0.21;

    @Override
    public boolean create(Venta venta) {
        String sqlVenta = """
            INSERT INTO ventas 
            (codigo_venta, cliente_id, tipo_factura, metodo_pago, subtotal, iva, total)
            VALUES (?, ?, ?, ?, ?, ?, ?)
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
            conexion.setAutoCommit(false);

            // Generar código único (ejemplo: FACT-20251026-001)
            String codigoUnico = generarCodigoVenta(venta.getTipoFactura(), conexion);
            venta.setCodigoVenta(codigoUnico);

            pstmtVenta = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);

            pstmtVenta.setString(1, codigoUnico);

            if (venta.getClienteFactura() != null && venta.getClienteFactura().getId() != 0) {
                pstmtVenta.setInt(2, venta.getClienteFactura().getId());
            } else {
                pstmtVenta.setNull(2, Types.INTEGER);
            }

            pstmtVenta.setString(3, venta.getTipoFactura());
            pstmtVenta.setString(4,
                    venta.getEstrategiaPago() != null ? venta.getEstrategiaPago().getNombreMetodoPago() : "SIN MÉTODO");

            CarritoDeCompra carrito = venta.getCarrito();
            double totalCarrito = carrito != null ? carrito.getTotal().doubleValue() : 0;
            double comision = venta.getEstrategiaPago() != null ? venta.getEstrategiaPago().getComision() : 0;
            double totalConComision = totalCarrito * (1 + comision);

            double subtotalSinIva = totalConComision / (1 + IVA_RATE);
            double iva = totalConComision - subtotalSinIva;

            pstmtVenta.setBigDecimal(5, java.math.BigDecimal.valueOf(subtotalSinIva));
            pstmtVenta.setBigDecimal(6, java.math.BigDecimal.valueOf(iva));
            pstmtVenta.setBigDecimal(7, java.math.BigDecimal.valueOf(totalConComision));

            int filasVenta = pstmtVenta.executeUpdate();
            if (filasVenta == 0) {
                conexion.rollback();
                return false;
            }

            generatedKeys = pstmtVenta.getGeneratedKeys();
            int ventaId = 0;
            if (generatedKeys.next()) {
                ventaId = generatedKeys.getInt(1);
                venta.setIdVenta(ventaId);
            } else {
                conexion.rollback();
                return false;
            }

            // Insertar detalle
            if (carrito != null && !carrito.getItems().isEmpty()) {
                pstmtDetalle = conexion.prepareStatement(sqlDetalle);
                for (ItemCarrito item : carrito.getItems()) {
                    pstmtDetalle.setInt(1, ventaId);
                    pstmtDetalle.setInt(2, item.getProducto().getIdProducto());
                    pstmtDetalle.setInt(3, item.getCantidad());
                    pstmtDetalle.setBigDecimal(4, item.getPrecioUnitario());
                    pstmtDetalle.setBigDecimal(5, item.getSubtotal());
                    pstmtDetalle.addBatch();
                }
                pstmtDetalle.executeBatch();

                // Actualizar stock
                String sqlStock = "UPDATE productos SET stock_producto = stock_producto - ? WHERE id_producto = ?";
                try (PreparedStatement pstmtStock = conexion.prepareStatement(sqlStock)) {
                    for (ItemCarrito item : carrito.getItems()) {
                        pstmtStock.setInt(1, item.getCantidad());
                        pstmtStock.setInt(2, item.getProducto().getIdProducto());
                        pstmtStock.addBatch();
                    }
                    pstmtStock.executeBatch();
                }
            }

            conexion.commit();
            System.out.println("Venta creada con código: " + codigoUnico);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar la venta: " + e.getMessage());
            e.printStackTrace();
            if (conexion != null) {
                try { conexion.rollback(); } catch (SQLException ex) { }
            }
            return false;

        } finally {
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

    private String generarCodigoVenta(String tipo, Connection conn) throws SQLException {
        String prefijo = tipo.equalsIgnoreCase("FACTURA") ? "FACT" : "TICK";
        String fecha = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String sql = "SELECT COUNT(*) AS total FROM ventas WHERE tipo_factura = ?";
        int numero = 1;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numero = rs.getInt("total") + 1;
            }
        }
        return String.format("%s-%s-%03d", prefijo, fecha, numero);
    }

    @Override
    public Optional<Venta> read(int id) {
        return Optional.empty();
    }

    @Override
    public boolean update(Venta venta) { return false; }

    @Override
    public boolean delete(int id) { return false; }

    @Override
    public List<Venta> findAll() { return new ArrayList<>(); }

// ✅ NUEVO MÉTODO: Buscar venta por código
public Venta findByCodigo(String codigoVenta) {
    String sql = """
        SELECT 
            v.id AS id_venta,
            v.codigo_venta,
            v.tipo_factura,
            v.metodo_pago,
            v.subtotal,
            v.iva,
            v.total,
            c.id AS cliente_id,
            c.nombre AS cliente_nombre
        FROM ventas v
        LEFT JOIN clientes c ON v.cliente_id = c.id
        WHERE v.codigo_venta = ?
        """;

    try (Connection conn = GestorDeConexion.getInstancia().getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, codigoVenta);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Venta venta = new Venta();
            venta.setIdVenta(rs.getInt("id_venta"));
            venta.setCodigoVenta(rs.getString("codigo_venta"));
            venta.setTipoFactura(rs.getString("tipo_factura"));

            // ✅ Cargar cliente si existe
            int clienteId = rs.getInt("cliente_id");
            if (clienteId != 0) {
                Cliente cliente = new Cliente();
                cliente.setId(clienteId);
                cliente.setNombre(rs.getString("cliente_nombre"));
                venta.setClienteFactura(cliente);
            }

            // ✅ Reconstruir EstrategiaPago desde texto
            
       venta.setEstrategiaPago(Venta.convertirMetodoPago(rs.getString("metodo_pago")));

            venta.setMetodoPagoTexto(rs.getString("metodo_pago"));


            // ✅ Total (BigDecimal)
            venta.setTotal(rs.getBigDecimal("total"));

            return venta;
        }

    } catch (SQLException e) {
        System.err.println("Error al buscar venta por código: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}

}