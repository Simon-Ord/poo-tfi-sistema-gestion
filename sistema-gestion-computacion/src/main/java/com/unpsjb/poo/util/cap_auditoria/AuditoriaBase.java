package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.Sesion;

/**
 * Clase base abstracta para todas las auditorías del sistema.
 *
 * Define el comportamiento común:
 *  - Obtener usuario actual
 *  - Registrar evento genérico
 *  - Métodos polimórficos para creación y modificación
 *
 * Aplica el PRINCIPIO DE POLIMORFISMO:
 * cada subclase define cómo audita sus propias entidades.
 */
public abstract class AuditoriaBase {

    /** DAO responsable de insertar registros de auditoría */
    protected static final ReportesDAO reportesDAO = new ReportesDAO();

    /** Devuelve el nombre del usuario en sesión o "Sistema" si no hay ninguno */
    protected String getUsuarioActual() {
        return (Sesion.getUsuarioActual() != null)
                ? Sesion.getUsuarioActual().getNombre()
                : "Sistema";
    }

    /** Registra un evento genérico de auditoría en la base de datos */
    protected void registrarEvento(String accion, String entidad, String detalles) {
        EventoAuditoria evento = new EventoAuditoria();
        evento.setUsuario(getUsuarioActual());
        evento.setAccion(accion);
        evento.setEntidad(entidad);
        evento.setDetalles(detalles);
        reportesDAO.registrarEvento(evento);
    }

    // ---------------------------------------------------------------------
    // Métodos polimórficos a redefinir por las subclases
    // ---------------------------------------------------------------------

    /** Cómo registrar una modificación (cada entidad define su versión) */
    public abstract void registrarAccionEspecifica(Object original, Object modificado);

    /** Cómo registrar una creación (opcional, redefinido si aplica) */
    public void registrarCreacion(Object nuevo) {
        // Implementación vacía — las subclases la sobrescriben si la usan
    }
}
