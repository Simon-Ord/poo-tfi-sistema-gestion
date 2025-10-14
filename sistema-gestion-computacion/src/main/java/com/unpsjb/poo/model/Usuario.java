package com.unpsjb.poo.model;

public class Usuario {
    private int id;
    private String legajo; 
    private String nombre;
    private String usuario;
    private String contraseña;
    private String rol;
    private boolean estado;

    // Constructor vacío
    public Usuario() {}

    // Constructor completo
    public Usuario(int id, String legajo, String nombre, String usuario, String contraseña, String rol, boolean estado) {
        this.id = id;
        this.legajo = legajo;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.estado = estado;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLegajo() { return legajo; }
    public void setLegajo(String legajo) { this.legajo = legajo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", legajo='" + legajo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", usuario='" + usuario + '\'' +
                ", rol='" + rol + '\'' +
                ", estado=" + estado +
                '}';
    }
}
