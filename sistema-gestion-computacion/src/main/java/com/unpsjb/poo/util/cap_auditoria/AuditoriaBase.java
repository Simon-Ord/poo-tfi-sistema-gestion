package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.Sesion;

public abstract class AuditoriaBase {

    protected static final ReportesDAO reportesDAO = new ReportesDAO();

    // 🧠 Método común para obtener el usuario actual
    protected String getUsuarioActual() {
        return (Sesion.getUsuarioActual() != null)
                ? Sesion.getUsuarioActual().getNombre()
                : "Sistema";
    }

    // 🧠 Método común para registrar evento
    protected void registrarEvento(String accion, String entidad, String detalles) {
        EventoAuditoria evento = new EventoAuditoria();
        evento.setUsuario(getUsuarioActual());
        evento.setAccion(accion);
        evento.setEntidad(entidad);
        evento.setDetalles(detalles);
        reportesDAO.registrarEvento(evento);
    }

    // 💡 Método abstracto: cada tipo de auditoría define cómo registra su evento
    public abstract void registrarAccionEspecifica(Object original, Object modificado);
}
