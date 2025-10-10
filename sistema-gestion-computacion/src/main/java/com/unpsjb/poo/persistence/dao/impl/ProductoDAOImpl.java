package com.unpsjb.poo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.persistence.GestorDeConexion;

public class ProductoDAOImpl implements DAO<Producto> {
	
	private Connection conexion = GestorDeConexion.getInstancia().getConexion();

	@Override
	public void create(Producto producto) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO Productos (nombreProducto,descripcionProducto,stockProducto,precioProducto,categoriaProducto,fabricanteProducto) VALUES (?,?,?,?,?,?)";
		try (PreparedStatement pstmt = this.conexion.prepareStatement(sql)) {
			pstmt.setString(1, producto.getNombreProducto());
			pstmt.setString(2, producto.getDescripcionProducto());
			pstmt.setInt(3, producto.getStockProducto());
			pstmt.setBigDecimal(4, producto.getPrecioProducto());
			pstmt.setString(5, producto.getCategoriaProducto());
			pstmt.setString(6, producto.getFabricanteProducto());
			//pstmt.setInt(7, producto.getCodigoProducto());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al insertar el producto : " + e.getMessage());
		}
	}

	@Override
	public Optional<Producto> read(int id) {
		// TODO Auto-generated method stub
		//Realiza la lectura de un producto por su ID
		String sql = "SELECT * FROM productos WHERE idProducto = ?";
		
		Producto producto = null;
		try (PreparedStatement pstmt = this.conexion.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
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
		} catch (SQLException e) {
			System.err.println("Error al ingresar el producto: " + e.getMessage());
		}
		return Optional.ofNullable(producto);
	}
	
	@Override
	public void update(Producto producto) {
		// TODO Auto-generated method stub
		String sql = "UPDATE productos SET nombreProducto = ?, descripcionProducto = ?, stockProducto = ?, precioProducto = ?, categoriaProducto = ?, fabricanteProducto = ? WHERE idProducto = ?";
		try (PreparedStatement pstmt = this.conexion.prepareStatement(sql)) {
			pstmt.setString(1, producto.getNombreProducto());
			pstmt.setString(2, producto.getDescripcionProducto());
			pstmt.setInt(3, producto.getStockProducto());
			pstmt.setBigDecimal(4, producto.getPrecioProducto());
			pstmt.setString(5, producto.getCategoriaProducto());
			pstmt.setString(6, producto.getFabricanteProducto());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al actualizar el producto: " + e.getMessage());
		}
	}
	

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		//Se elimina un producto a traves de su ID
		String sql = "DELETE FROM productos WHERE idProducto = ?";
		try (PreparedStatement pstmt = this.conexion.prepareStatement(sql)) {
			pstmt.setInt(1,id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al eliminar el producto: " + e.getMessage());
		}
	}

	
	@Override
	public List<Producto> findAll() {
		// TODO Auto-generated method stub
		//Realiza la lectura de todos los productos de la base de datos
		List<Producto> productos = new ArrayList<>();
		
		String sql = "SELECT * FROM productos";
		try (Statement stmt = this.conexion.createStatement();
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
				
		return productos;
	}
	
	
}
