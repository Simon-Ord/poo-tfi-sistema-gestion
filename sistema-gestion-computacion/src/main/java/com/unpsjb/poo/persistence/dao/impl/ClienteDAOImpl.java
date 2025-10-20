package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.ClienteDAO;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE activo = TRUE ORDER BY id";

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setCuit(rs.getString("cuit"));
                c.setTelefono(rs.getString("telefono"));
                c.setDireccion(rs.getString("direccion"));
                c.setEmail(rs.getString("email"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener clientes: " + e.getMessage());
        }

        return lista;
    }

    
    @Override
public boolean insertar(Cliente cliente) {
    String sql = "INSERT INTO clientes (nombre, cuit, telefono, direccion, email, tipo, activo) VALUES (?, ?, ?, ?, ?, ?, TRUE)";
    try (Connection conn = GestorDeConexion.getInstancia().getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, cliente.getNombre());
        stmt.setString(2, cliente.getCuit());
        stmt.setString(3, cliente.getTelefono());
        stmt.setString(4, cliente.getDireccion());
        stmt.setString(5, cliente.getEmail());
        stmt.setString(6, cliente.getTipo()); // 👈 nuevo campo

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error SQL al insertar cliente: " + e.getMessage());
        return false;
    }
}
@Override
public boolean modificar(Cliente cliente) {
    String sql = "UPDATE clientes SET nombre = ?, cuit = ?, telefono = ?, direccion = ?, email = ?, tipo = ? WHERE id = ?";
    try (Connection conn = GestorDeConexion.getInstancia().getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, cliente.getNombre());
        stmt.setString(2, cliente.getCuit());
        stmt.setString(3, cliente.getTelefono());
        stmt.setString(4, cliente.getDireccion());
        stmt.setString(5, cliente.getEmail());
        stmt.setString(6, cliente.getTipo());
        stmt.setInt(7, cliente.getId());

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error al modificar cliente: " + e.getMessage());
        return false;
    }
}

    @Override
    public boolean eliminar(int id) {
        String sql = "UPDATE clientes SET activo = FALSE WHERE id = ?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al desactivar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente obtenerPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setCuit(rs.getString("cuit"));
                c.setTelefono(rs.getString("telefono"));
                c.setDireccion(rs.getString("direccion"));
                c.setEmail(rs.getString("email"));
                return c;
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener cliente por ID: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE LOWER(nombre) LIKE LOWER(?) AND activo = TRUE";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setCuit(rs.getString("cuit"));
                c.setTelefono(rs.getString("telefono"));
                c.setDireccion(rs.getString("direccion"));
                c.setEmail(rs.getString("email"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
        }

        return lista;
    }
}
