package com.unpsjb.poo.persistence.dao;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.GestorDeConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportesDAO {

    public List<EventoAuditoria> obtenerEventos(String usuario, String entidad, String accion) {
        List<EventoAuditoria> lista = new ArrayList<>();
        
        // La consulta SQL usa el nombre de tabla correcto: 'auditoria'
        StringBuilder sql = new StringBuilder("SELECT * FROM auditoria WHERE 1=1 ");

        // Lógica de filtrado
        if (usuario != null && !usuario.isEmpty()) sql.append("AND usuario ILIKE ? ");
        if (entidad != null && !entidad.isEmpty()) sql.append("AND entidad_afectada ILIKE ? ");
        if (accion != null && !accion.isEmpty()) sql.append("AND accion ILIKE ? ");
        sql.append("ORDER BY fecha_hora DESC LIMIT 200");

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            // Asignación de parámetros
            if (usuario != null && !usuario.isEmpty()) ps.setString(index++, "%" + usuario + "%");
            if (entidad != null && !entidad.isEmpty()) ps.setString(index++, "%" + entidad + "%");
            if (accion != null && !accion.isEmpty()) ps.setString(index++, "%" + accion + "%");

            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                EventoAuditoria ev = new EventoAuditoria();
                
                // Mapeo CORREGIDO a los nombres de columna de la tabla 'auditoria'
                ev.setId(rs.getLong("id"));
                ev.setFechaHora(rs.getTimestamp("fecha_hora")); 
                ev.setUsuario(rs.getString("usuario"));
                ev.setAccion(rs.getString("accion"));
                ev.setEntidad(rs.getString("entidad_afectada")); // Mapea a 'entidad_afectada'
                
                // Mapeo de la descripción y referencia
                // Asumo que 'detalles' en tu modelo es 'descripcion' en la DB
                ev.setDetalles(rs.getString("descripcion")); 

                
                lista.add(ev);
            }

        } catch (SQLException e) {
            System.err.println("Error de SQL en ReportesDAO: " + e.getMessage());
            e.printStackTrace(); 
        }

        return lista;
    }
}