package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.persistence.dao.DAO;

public class ClienteDAOImpl implements DAO<Cliente> {

    @Override
    public List<Cliente> findAll() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY id";
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
                c.setTipo(rs.getString("tipo"));
                c.setActivo(rs.getBoolean("activo"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean create(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, cuit, telefono, direccion, email, tipo, activo) VALUES (?, ?, ?, ?, ?, ?, TRUE)";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getCuit());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getEmail());
            stmt.setString(6, cliente.getTipo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre=?, cuit=?, telefono=?, direccion=?, email=?, tipo=?, activo=? WHERE id=?";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getCuit());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getEmail());
            stmt.setString(6, cliente.getTipo());
            stmt.setBoolean(7, cliente.isActivo());
            stmt.setInt(8, cliente.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar cliente: " + e.getMessage());
            return false;
        }
    }

@Override
public boolean delete(int id) {
    String sql = "UPDATE clientes SET activo = NOT activo WHERE id = ?";
    try (Connection conn = GestorDeConexion.getInstancia().getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al cambiar estado del cliente: " + e.getMessage());
        return false;
    }
}


    @Override
    public Optional<Cliente> read(int id) {
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
                c.setTipo(rs.getString("tipo"));
                c.setActivo(rs.getBoolean("activo"));
                return Optional.of(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }
        return Optional.empty();
    }


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
                c.setTipo(rs.getString("tipo"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
        }
        return lista;
    }
    // Método para buscar clientes activos por cualquier campo
    public List<Cliente> buscarClientes(String termino) {
        String sql = """
            SELECT * FROM clientes 
            WHERE activo = TRUE 
            AND (LOWER(nombre) LIKE ? 
                 OR LOWER(cuit) LIKE ? 
                 OR LOWER(telefono) LIKE ? 
                 OR LOWER(email) LIKE ? 
                 OR LOWER(tipo) LIKE ?)
            ORDER BY nombre
            """;
        
        List<Cliente> clientes = new ArrayList<>();
        String terminoBusqueda = "%" + termino.toLowerCase() + "%";
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            // Setear el mismo término para todos los campos
            pstmt.setString(1, terminoBusqueda);
            pstmt.setString(2, terminoBusqueda);
            pstmt.setString(3, terminoBusqueda);
            pstmt.setString(4, terminoBusqueda);
            pstmt.setString(5, terminoBusqueda);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setNombre(rs.getString("nombre"));
                    c.setCuit(rs.getString("cuit"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setEmail(rs.getString("email"));
                    c.setTipo(rs.getString("tipo"));
                    c.setActivo(rs.getBoolean("activo"));
                    clientes.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
        }
        return clientes;
    }
    // Método para buscar clientes incluyendo inactivos por cualquier campo
    public List<Cliente> buscarClientesCompleto(String termino) {
        String sql = """
            SELECT * FROM clientes 
            WHERE (LOWER(nombre) LIKE ? 
                   OR LOWER(cuit) LIKE ? 
                   OR LOWER(telefono) LIKE ? 
                   OR LOWER(email) LIKE ? 
                   OR LOWER(tipo) LIKE ?)
            ORDER BY activo DESC, nombre
            """;
        List<Cliente> clientes = new ArrayList<>();
        String terminoBusqueda = "%" + termino.toLowerCase() + "%";
        
        try (Connection conexion = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            // Setear el mismo término para todos los campos
            pstmt.setString(1, terminoBusqueda);
            pstmt.setString(2, terminoBusqueda);
            pstmt.setString(3, terminoBusqueda);
            pstmt.setString(4, terminoBusqueda);
            pstmt.setString(5, terminoBusqueda);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setNombre(rs.getString("nombre"));
                    c.setCuit(rs.getString("cuit"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setEmail(rs.getString("email"));
                    c.setTipo(rs.getString("tipo"));
                    c.setActivo(rs.getBoolean("activo"));
                    clientes.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes completo: " + e.getMessage());
        }
        return clientes;
    }
}
