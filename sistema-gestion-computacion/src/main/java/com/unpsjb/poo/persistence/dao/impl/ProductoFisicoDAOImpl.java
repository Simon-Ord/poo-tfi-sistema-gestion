package com.unpsjb.poo.persistence.dao.impl;
/*
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.productos.Fabricante;
import com.unpsjb.poo.model.productos.ProductoFisico;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

/**
 * DAO para productos físicos. Realiza operaciones en las tablas `productos` y `productos_fisicos`.
 * Las operaciones que modifiquen ambas tablas se ejecutan en una sola transacción.
 
public class ProductoFisicoDAOImpl implements DAO<ProductoFisico> {

	// Inserta un producto (productos + productos_fisicos)
	@Override
	public boolean create(ProductoFisico producto) {
		String sqlProducto = "INSERT INTO productos (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto, codigo_producto, activo, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
		String sqlFisico = "INSERT INTO productos_fisicos (id_producto, id_fabricante, garantia_meses, tipo_garantia, estado_fisico) VALUES (?, ?, ?, ?, ?)";

		try (Connection conexion = GestorDeConexion.getInstancia().getConexion()) {
			conexion.setAutoCommit(false);
			try (PreparedStatement psProd = conexion.prepareStatement(sqlProducto, Statement.RETURN_GENERATED_KEYS);
				 PreparedStatement psFis = conexion.prepareStatement(sqlFisico)) {

				psProd.setString(1, producto.getNombreProducto());
				psProd.setString(2, producto.getDescripcionProducto());
				psProd.setInt(3, producto.getStockProducto());
				psProd.setBigDecimal(4, producto.getPrecioProducto());
				psProd.setString(5, producto.getCategoriaProducto());
				psProd.setInt(6, producto.getCodigoProducto());
				psProd.setBoolean(7, producto.isActivo());
				int filas = psProd.executeUpdate();
				if (filas == 0) {
					conexion.rollback();
					return false;
				}
				try (ResultSet keys = psProd.getGeneratedKeys()) {
					if (keys.next()) {
						int idGenerado = keys.getInt(1);
						// Insertar en productos_fisicos
						psFis.setInt(1, idGenerado);
						if (producto.getFabricante() != null) {
							psFis.setInt(2, producto.getFabricante().getId());
						} else {
							psFis.setNull(2, java.sql.Types.INTEGER);
						}
						if (producto.getGarantiaMeses() != null) {
							psFis.setInt(3, producto.getGarantiaMeses());
						} else {
							psFis.setNull(3, java.sql.Types.INTEGER);
						}
						psFis.setString(4, producto.getTipoGarantia() == null ? null : producto.getTipoGarantia().name());
						psFis.setString(5, producto.getEstadoFisico() == null ? null : producto.getEstadoFisico().name());
						int filasFis = psFis.executeUpdate();
						if (filasFis == 0) {
							conexion.rollback();
							return false;
						}
						conexion.commit();
						return true;
					} else {
						conexion.rollback();
						return false;
					}
				}
			} catch (SQLException ex) {
				conexion.rollback();
				System.err.println("Error al crear producto físico: " + ex.getMessage());
				return false;
			} finally {
				conexion.setAutoCommit(true);
			}
		} catch (SQLException e) {
			System.err.println("Error al crear producto físico (conexión): " + e.getMessage());
			return false;
		}
	}

	// Leer producto físico por id (JOIN)
	@Override
	public Optional<ProductoFisico> read(int id) {
		String sql = "SELECT p.*, pf.id_fabricante, pf.garantia_meses, pf.tipo_garantia, pf.estado_fisico FROM productos p LEFT JOIN productos_fisicos pf ON p.id_producto = pf.id_producto WHERE p.id_producto = ?";
		ProductoFisico producto = null;
		try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
			 PreparedStatement pstmt = conexion.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					producto = mapResultSet(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al leer producto físico: " + e.getMessage());
		}
		return Optional.ofNullable(producto);
	}

	// Actualizar producto y su info fisica
	@Override
	public boolean update(ProductoFisico producto) {
		String sqlProd = "UPDATE productos SET nombre_producto = ?, descripcion_producto = ?, stock_producto = ?, precio_producto = ?, categoria_producto = ?, codigo_producto = ?, activo = ? WHERE id_producto = ?";
		String sqlFis = "INSERT INTO productos_fisicos (id_producto, id_fabricante, garantia_meses, tipo_garantia, estado_fisico) VALUES (?, ?, ?, ?, ?) ON CONFLICT (id_producto) DO UPDATE SET id_fabricante = EXCLUDED.id_fabricante, garantia_meses = EXCLUDED.garantia_meses, tipo_garantia = EXCLUDED.tipo_garantia, estado_fisico = EXCLUDED.estado_fisico";

		try (Connection conexion = GestorDeConexion.getInstancia().getConexion()) {
			conexion.setAutoCommit(false);
			try (PreparedStatement psP = conexion.prepareStatement(sqlProd);
				 PreparedStatement psF = conexion.prepareStatement(sqlFis)) {

				psP.setString(1, producto.getNombreProducto());
				psP.setString(2, producto.getDescripcionProducto());
				psP.setInt(3, producto.getStockProducto());
				psP.setBigDecimal(4, producto.getPrecioProducto());
				psP.setString(5, producto.getCategoriaProducto());
				psP.setInt(6, producto.getCodigoProducto());
				psP.setBoolean(7, producto.isActivo());
				psP.setInt(8, producto.getIdProducto());

				int filasP = psP.executeUpdate();

				// preparar insert/update en productos_fisicos
				psF.setInt(1, producto.getIdProducto());
				if (producto.getFabricante() != null) psF.setInt(2, producto.getFabricante().getId()); else psF.setNull(2, java.sql.Types.INTEGER);
				if (producto.getGarantiaMeses() != null) psF.setInt(3, producto.getGarantiaMeses()); else psF.setNull(3, java.sql.Types.INTEGER);
				psF.setString(4, producto.getTipoGarantia() == null ? null : producto.getTipoGarantia().name());
				psF.setString(5, producto.getEstadoFisico() == null ? null : producto.getEstadoFisico().name());

				int filasF = psF.executeUpdate();

				if (filasP > 0) {
					conexion.commit();
					return true;
				} else {
					conexion.rollback();
					return false;
				}

			} catch (SQLException ex) {
				conexion.rollback();
				System.err.println("Error al actualizar producto físico: " + ex.getMessage());
				return false;
			} finally {
				conexion.setAutoCommit(true);
			}
		} catch (SQLException e) {
			System.err.println("Error al actualizar producto físico (conexión): " + e.getMessage());
			return false;
		}
	}

	// Desactivar (logical delete) el producto principal
	@Override
	public boolean delete(int id) {
		String sql = "UPDATE productos SET activo = FALSE WHERE id_producto = ?";
		try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
			 PreparedStatement pstmt = conexion.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			int filas = pstmt.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al desactivar producto físico: " + e.getMessage());
			return false;
		}
	}

	// Listar todos los productos físicos activos (JOIN)
	@Override
	public List<ProductoFisico> findAll() {
		List<ProductoFisico> lista = new ArrayList<>();
		String sql = "SELECT p.*, pf.id_fabricante, pf.garantia_meses, pf.tipo_garantia, pf.estado_fisico FROM productos p JOIN productos_fisicos pf ON p.id_producto = pf.id_producto WHERE p.activo = TRUE ORDER BY p.nombre_producto";
		try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
			 Statement stmt = conexion.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				lista.add(mapResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener productos físicos: " + e.getMessage());
		}
		return lista;
	}

	// Mapeo ResultSet -> ProductoFisico
	private ProductoFisico mapResultSet(ResultSet rs) throws SQLException {
		ProductoFisico p = new ProductoFisico();
		p.setIdProducto(rs.getInt("id_producto"));
		p.setNombreProducto(rs.getString("nombre_producto"));
		p.setDescripcionProducto(rs.getString("descripcion_producto"));
		p.setStockProducto(rs.getInt("stock_producto"));
		p.setPrecioProducto(rs.getBigDecimal("precio_producto"));
		p.setCategoriaProducto(rs.getString("categoria_producto"));
		p.setCodigoProducto(rs.getInt("codigo_producto"));
		p.setActivo(rs.getBoolean("activo"));
		p.setFechaCreacion(rs.getTimestamp("fecha_creacion"));

		int idFabricante = rs.getInt("id_fabricante");
		if (!rs.wasNull()) {
			// Cargar nombre del fabricante si es necesario
			Fabricante f = new Fabricante();
			f.setId(idFabricante);
			// Intentamos obtener el nombre desde la tabla fabricantes
			try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
				 PreparedStatement pstmt = conexion.prepareStatement("SELECT nombre FROM fabricantes WHERE id = ?")) {
				pstmt.setInt(1, idFabricante);
				try (ResultSet r2 = pstmt.executeQuery()) {
					if (r2.next()) f.setNombre(r2.getString(1));
				}
			} catch (SQLException ex) {
				// si falla dejar solo el id
			}
			p.setFabricante(f);
		}

		int garantia = rs.getInt("garantia_meses");
		if (!rs.wasNull()) p.setGarantiaMeses(garantia);

		String tipoGar = rs.getString("tipo_garantia");
		if (tipoGar != null) {
			try {
				p.setTipoGarantia(ProductoFisico.TipoGarantia.valueOf(tipoGar));
			} catch (IllegalArgumentException e) {
				p.setTipoGarantia(null);
			}
		}

		String estado = rs.getString("estado_fisico");
		if (estado != null) {
			try {
				p.setEstadoFisico(ProductoFisico.EstadoFisico.valueOf(estado));
			} catch (IllegalArgumentException e) {
				p.setEstadoFisico(null);
			}
		}

		return p;
	}

}*/