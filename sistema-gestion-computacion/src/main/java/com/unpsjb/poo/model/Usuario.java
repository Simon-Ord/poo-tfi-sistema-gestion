package com.unpsjb.poo.model;


public abstract class Usuario {
    private int id;
    private String nombre;
    private String usuario;
    private String contraseña;
    private String rol;
    private boolean estado; // true = activo, false = inactivo

    // --- Constructores ---
    public Usuario() {}

    public Usuario(int id, String nombre, String usuario, String contraseña, String rol, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.estado = estado;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // --- Método polimórfico ---
    public abstract void mostrarOpciones();

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", usuario='" + usuario + '\'' +
                ", rol='" + rol + '\'' +
                ", estado=" + estado +
                '}';
    }
}
