package com.unpsjb.poo.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.unpsjb.poo.persistence.GestorDeConexion;
public class AuditDAO {

    public static boolean logEvent(String username, String action, String entity,
                                   String entityId, String details) {
        String sql = "INSERT INTO audit.events (username, action, entity, entity_id, details) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, action);
            ps.setString(3, entity);
            ps.setString(4, entityId);
            ps.setString(5, details);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al guardar evento de auditoría: " + e.getMessage());
            return false;
        }
    }
}
