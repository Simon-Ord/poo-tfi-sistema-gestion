package com.unpsjb.poo.model;

import java.sql.Timestamp;

/**
 * Representa un evento de auditoría en el sistema.
 * Guarda información sobre acciones realizadas por los usuarios
 * (por ejemplo: crear, modificar, eliminar datos).
 */
public class EventoAuditoria {

    private long id;                    // Identificador único del evento
    private Timestamp fechaHora;        // Fecha y hora en que ocurrió
    private String usuario;             // Usuario que realizó la acción
    private String accion;              // Acción ejecutada (CREATE, UPDATE, DELETE)
    private String entidad;             // Entidad afectada (usuarios, productos, etc.)
    private String idEntidad;           // ID de la entidad afectada
    private String detalles;            // Descripción del evento


    // --- Getters y Setters ---
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
