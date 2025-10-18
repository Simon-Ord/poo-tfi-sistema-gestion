package com.unpsjb.poo.model;

public class Usuario {
    private String dni;
    private String nombre;
    private String usuario;
    private String contraseña;
    private String rol;
    private boolean estado;

    public Usuario() {}

    public Usuario(String dni, String nombre, String usuario, String contraseña, String rol, boolean estado) {
        this.dni = dni;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.estado = estado;
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

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
}
