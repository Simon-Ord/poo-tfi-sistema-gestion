package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.Sesion;

/**
 * Clase base abstracta para todas las auditorías del sistema.
 * Define el comportamiento común y las operaciones polimórficas.
 */
public abstract class AuditoriaBase {

    protected static final ReportesDAO reportesDAO = new ReportesDAO();

    /**
     * Obtiene el nombre del usuario actual en sesión.
     */
    protected String getUsuarioActual() {
        return (Sesion.getUsuarioActual() != null)
                ? Sesion.getUsuarioActual().getNombre()
                : "Sistema";
    }

    /**
     * Registra un evento genérico en la auditoría.
     */
    protected void registrarEvento(String accion, String entidad, String detalles) {
        EventoAuditoria evento = new EventoAuditoria();
        evento.setUsuario(getUsuarioActual());
        evento.setAccion(accion);
        evento.setEntidad(entidad);
        evento.setDetalles(detalles);
        reportesDAO.registrarEvento(evento);
    }

    /**
     * Método abstracto: cada tipo de auditoría define cómo registrar sus modificaciones.
     */
    public abstract void registrarAccionEspecifica(Object original, Object modificado);

    /**
     * Método opcional: registrar la creación de una nueva entidad.
     * Las hijas lo sobrescriben si lo necesitan.
     */
    public void registrarCreacion(Object nuevo) {
        // Por defecto no hace nada
    }
}
