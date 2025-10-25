package com.unpsjb.poo.persistence.dao;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.GestorDeConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de acceder a la tabla de auditoría.
 * No tiene lógica de negocio, solo consultas SQL puras.
 */
public class ReportesDAO {

    /** Inserta un nuevo evento en la base de datos */
    public void registrarEvento(EventoAuditoria evento) {
        String sql = "INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, evento.getUsuario());
            ps.setString(2, evento.getAccion());
            ps.setString(3, evento.getDetalles());
            ps.setString(4, evento.getEntidad());
            ps.setString(5, evento.getIdEntidad());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al registrar evento de auditoría: " + e.getMessage());
        }
    }

    /** Obtiene eventos filtrados según usuario, entidad o acción */
    public List<EventoAuditoria> obtenerEventos(String usuario, String entidad, String accion) {
        List<EventoAuditoria> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM auditoria WHERE 1=1 ");

        if (usuario != null && !usuario.isEmpty()) sql.append("AND usuario ILIKE ? ");
        if (entidad != null && !entidad.isEmpty()) sql.append("AND entidad_afectada ILIKE ? ");
        if (accion != null && !accion.isEmpty()) sql.append("AND accion ILIKE ? ");
        sql.append("ORDER BY fecha_hora DESC LIMIT 200");

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (usuario != null && !usuario.isEmpty()) ps.setString(index++, "%" + usuario + "%");
            if (entidad != null && !entidad.isEmpty()) ps.setString(index++, "%" + entidad + "%");
            if (accion != null && !accion.isEmpty()) ps.setString(index++, "%" + accion + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EventoAuditoria ev = new EventoAuditoria();
                ev.setId(rs.getLong("id"));
                ev.setFechaHora(rs.getTimestamp("fecha_hora"));
                ev.setUsuario(rs.getString("usuario"));
                ev.setAccion(rs.getString("accion"));
                ev.setEntidad(rs.getString("entidad_afectada"));
                ev.setDetalles(rs.getString("descripcion"));
                lista.add(ev);
            }

        } catch (SQLException e) {
            System.err.println("Error de SQL en ReportesDAO: " + e.getMessage());
        }

        return lista;
    }
}
