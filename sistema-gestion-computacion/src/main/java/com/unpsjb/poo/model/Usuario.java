package com.unpsjb.poo.model;

import java.util.List;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;


public class Usuario {
    private String dni;
    private String nombre;
    private String usuario;
    private String contraseña;
    private String rol;
    private boolean estado;

    private static final UsuarioDAOImpl dao = new UsuarioDAOImpl();

    public Usuario() {}

    public Usuario(String dni, String nombre, String usuario, String contraseña, String rol, boolean estado) {
        this.dni = dni;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.estado = estado;
    }

    // ===============================
    // 🔹 Métodos de persistencia (DAO)
    // ===============================

    public boolean guardar() {
        return dao.insertar(this);
    }

    public boolean actualizar() {
        return dao.modificar(this);
    }

    public boolean desactivar() {
        return dao.eliminar(this.dni);
    }

    public static List<Usuario> obtenerTodos() {
        return dao.obtenerTodos();
    }

    public static Usuario verificarLogin(String usuario, String contraseña) {
        return dao.verificarLogin(usuario, contraseña);
    }

    // ===============================
    // 🔹 Getters y Setters
    // ===============================

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
