package com.unpsjb.poo.util;

import com.unpsjb.poo.persistence.GestorDeConexion;
import com.unpsjb.poo.util.Sesion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase utilitaria para registrar eventos de auditoría
 * (como inicio/cierre de sesión) en la base de datos.
 */
public class AuditoriaUtil {

    /**
     * Registra un evento en la tabla auditoria.
     * @param accion Acción realizada (ej: INICIAR SESIÓN, CERRAR SESIÓN, CREAR USUARIO)
     * @param entidad Entidad afectada (ej: sesion, usuario, producto)
     * @param descripcion Texto opcional
     */
    public static void registrarEvento(String accion, String entidad, String descripcion) {
        String usuario = "DESCONOCIDO";

        if (Sesion.getUsuarioActual() != null) {
            usuario = Sesion.getUsuarioActual().getUsuario();
        }

        String sql = "INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada) VALUES (?, ?, ?, ?)";

        try (Connection conn = GestorDeConexion.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, accion);
            ps.setString(3, descripcion);
            ps.setString(4, entidad);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar auditoría: " + e.getMessage());
        }
    }
}
