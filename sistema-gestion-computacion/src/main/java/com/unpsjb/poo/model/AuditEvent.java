package com.unpsjb.poo.model;

import java.sql.Timestamp;

public class AuditEvent {
    private long id;
    private Timestamp occurredAt;
    private String username;
    private String action;
    private String entity;
    private String entityId;
    private String details;
    private String ip;
    private String severity;

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public Timestamp getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Timestamp occurredAt) { this.occurredAt = occurredAt; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntity() { return entity; }
    public void setEntity(String entity) { this.entity = entity; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
}
