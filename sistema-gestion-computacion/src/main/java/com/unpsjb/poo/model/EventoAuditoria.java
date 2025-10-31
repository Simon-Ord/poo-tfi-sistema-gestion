package com.unpsjb.poo.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import com.unpsjb.poo.persistence.dao.ReportesDAO;

/**
 * Representa un evento de auditoría en el sistema.
 * Ahora además contiene la lógica de persistencia, conectándose al DAO.
 */
public class EventoAuditoria {

    private long id;
    private Timestamp fechaHora;
    private String usuario;
    private String accion;
    private String entidad;
    private String idEntidad;
    private String detalles;

    private static final ReportesDAO dao = new ReportesDAO();

    public static List<EventoAuditoria> obtenerEventosPorRangoFechas(LocalDate desde, LocalDate hasta) {
    return new ReportesDAO().obtenerEventosPorRangoFechas(desde, hasta);
}


    // ==============================
    // Métodos de persistencia
    // ==============================

    /** Guarda este evento de auditoría en la base de datos */
    public boolean guardar() {
        try {
            dao.registrarEvento(this);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar evento de auditoría: " + e.getMessage());
            return false;
        }
    }

    /** Obtiene una lista de eventos filtrados */
public static List<EventoAuditoria> obtenerEventos(String usuario, String entidad, String accion,
                                      java.sql.Date desde, java.sql.Date hasta) {
    ReportesDAO dao = new ReportesDAO();
    return dao.obtenerEventos(usuario, entidad, accion, desde, hasta);
}


    // ==============================
    // Getters / Setters
    // ==============================

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Timestamp getFechaHora() { return fechaHora; }
    public void setFechaHora(Timestamp fechaHora) { this.fechaHora = fechaHora; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }

    public String getIdEntidad() { return idEntidad; }
    public void setIdEntidad(String idEntidad) { this.idEntidad = idEntidad; }

    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }

    @Override
    public String toString() {
        return "EventoAuditoria{" +
                "id=" + id +
                ", fechaHora=" + fechaHora +
                ", usuario='" + usuario + '\'' +
                ", accion='" + accion + '\'' +
                ", entidad='" + entidad + '\'' +
                ", idEntidad='" + idEntidad + '\'' +
                ", detalles='" + detalles + '\'' +
                '}';
    }
}
