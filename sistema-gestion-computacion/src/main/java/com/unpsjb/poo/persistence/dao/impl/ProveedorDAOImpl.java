package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.Proveedor;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class ProveedorDAOImpl implements DAO<Proveedor> {

    @Override
    public Optional<Proveedor> read(int id) {
        String sql = "SELECT * FROM proveedores WHERE id = ?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setEmail(rs.getString("email"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setTipo(rs.getString("tipo"));
                proveedor.setActivo(rs.getBoolean("activo"));
                return Optional.of(proveedor);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer proveedor. SQL: " + sql + " - " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Proveedor> findAll() {
        List<Proveedor> proveedores = new ArrayList<>();
    String sql = "SELECT * FROM proveedores";

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setEmail(rs.getString("email"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setTipo(rs.getString("tipo"));
                proveedor.setActivo(rs.getBoolean("activo"));
                proveedores.add(proveedor);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener proveedores. SQL: " + sql + " - " + e.getMessage());
            e.printStackTrace();
        }

        return proveedores;
    }

    public List<Proveedor> findAllActivos() {
        List<Proveedor> proveedores = new ArrayList<>();
    String sql = "SELECT * FROM proveedores WHERE activo = true";

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setEmail(rs.getString("email"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setTipo(rs.getString("tipo"));
                proveedor.setActivo(rs.getBoolean("activo"));
                proveedores.add(proveedor);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener proveedores activos. SQL: " + sql + " - " + e.getMessage());
            e.printStackTrace();
        }

        return proveedores;
    }

    @Override
    public boolean create(Proveedor proveedor) {
    String sql = "INSERT INTO proveedores (nombre, telefono, email, direccion, cuit, tipo, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getTelefono());
            stmt.setString(3, proveedor.getEmail());
            stmt.setString(4, proveedor.getDireccion());
            stmt.setString(5, proveedor.getCuit());
            stmt.setString(6, proveedor.getTipo());
            stmt.setBoolean(7, proveedor.isActivo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear proveedor. SQL: " + sql + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Proveedor proveedor) {
    String sql = "UPDATE proveedores SET nombre = ?, telefono = ?, email = ?, direccion = ?, cuit = ?, tipo = ?, activo = ? WHERE id = ?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getTelefono());
            stmt.setString(3, proveedor.getEmail());
            stmt.setString(4, proveedor.getDireccion());
            stmt.setString(5, proveedor.getCuit());
            stmt.setString(6, proveedor.getTipo());
            stmt.setBoolean(7, proveedor.isActivo());
            stmt.setInt(8, proveedor.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar proveedor. SQL: " + sql + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        // No implementamos el borrado físico
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "UPDATE proveedores SET activo = false WHERE id = ?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar proveedor. SQL: " + sql + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}