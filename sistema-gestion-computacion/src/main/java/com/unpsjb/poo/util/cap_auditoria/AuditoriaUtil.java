package com.unpsjb.poo.util.cap_auditoria;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.Sesion;

/**
  Clase utilitaria genérica para registrar auditorías del sistema.
 */
public class AuditoriaUtil extends AuditoriaBase {

    private static final ReportesDAO reportesDAO = new ReportesDAO();

    /**
     *  Registra una acción genérica sobre cualquier entidad.
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
            evento.setDetalles(detalles);

            reportesDAO.registrarEvento(evento);
        } catch (Exception e) {
            System.err.println(" Error al registrar auditoría genérica: " + e.getMessage());
        }
    }

    /*
     *  Implementación de acción específica genérica (heredada de AuditoriaBase).
     * 
     * En este caso, no compara objetos, solo registra acciones generales.
     */
    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        // Esta implementación es genérica: no compara entidades específicas.
        // Se deja vacía o se puede sobreescribir en subclases concretas.
    }
}
