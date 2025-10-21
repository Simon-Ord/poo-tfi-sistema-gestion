package com.unpsjb.poo.util;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.ReportesDAO;

public class AuditoriaManager {

    private static final ReportesDAO reportesDAO = new ReportesDAO();

    public static void registrar(String accion, String entidad, String detalles) {
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
            System.err.println(" Error al registrar evento de auditoría: " + e.getMessage());
        }
    }
}