package com.unpsjb.poo.persistence.dao;

import com.unpsjb.poo.model.AuditEvent;
import com.unpsjb.poo.persistence.GestorDeConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportesDAO {

    public List<AuditEvent> obtenerEventos(String usuario, String entidad, String accion) {
        List<AuditEvent> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM audit.events WHERE 1=1 ");

        if (usuario != null && !usuario.isEmpty()) sql.append("AND username ILIKE ? ");
        if (entidad != null && !entidad.isEmpty()) sql.append("AND entity ILIKE ? ");
        if (accion != null && !accion.isEmpty()) sql.append("AND action ILIKE ? ");
        sql.append("ORDER BY occurred_at DESC LIMIT 200");

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (usuario != null && !usuario.isEmpty()) ps.setString(index++, "%" + usuario + "%");
            if (entidad != null && !entidad.isEmpty()) ps.setString(index++, "%" + entidad + "%");
            if (accion != null && !accion.isEmpty()) ps.setString(index++, "%" + accion + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AuditEvent ev = new AuditEvent();
                ev.setId(rs.getLong("id"));
                ev.setOccurredAt(rs.getTimestamp("occurred_at"));
                ev.setUsername(rs.getString("username"));
                ev.setAction(rs.getString("action"));
                ev.setEntity(rs.getString("entity"));
                ev.setEntityId(rs.getString("entity_id"));
                ev.setDetails(rs.getString("details"));
                ev.setIp(rs.getString("ip"));
                ev.setSeverity(rs.getString("severity"));
                lista.add(ev);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
