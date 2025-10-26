package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.Sesion;

/**
 * ✅ Clase utilitaria para registrar auditorías genéricas del sistema.
 * 
 * Se usa para registrar acciones sobre cualquier entidad
 * (usuarios, clientes, ventas, etc.)
 * 
 * Los cambios de productos fueron movidos a AuditoriaProductoUtil.
 */
public class AuditoriaUtil {

    private static final ReportesDAO reportesDAO = new ReportesDAO();

    /**
     * 🔹 Registrar acción genérica de auditoría (para cualquier entidad)
     */
    public static void registrarAccion(String accion, String entidad, String detalles) {
        try {
            String usuario = (Sesion.getUsuarioActual() != null)
                    ? Sesion.getUsuarioActual().getNombre()
                    : "Sistema";

            EventoAuditoria evento = new EventoAuditoria();
            evento.setUsuario(usuario);
            evento.setAccion(accion.toUpperCase());
            evento.setEntidad(entidad.toLowerCase());
            evento.setDetalles("El usuario " + usuario + " " + detalles);

            reportesDAO.registrarEvento(evento);
        } catch (Exception e) {
            System.err.println("Error al registrar auditoría genérica: " + e.getMessage());
        }
    }
}
