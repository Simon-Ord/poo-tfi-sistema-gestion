 package com.unpsjb.poo.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    // Constructor por defecto
    public Usuario() {
        this.estado = true;
    }

    // ===============================
    // Getters y Setters
    // ===============================

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { 
        this.contraseña = hashearContraseña(contraseña); 
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    // ===============================
    // Métodos de encriptación de contraseñas
    // ===============================
    
    // Hashea una contraseña usando SHA-256
    private static String hashearContraseña(String contraseña) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseña.getBytes());
            
            // Convertir bytes a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error al hashear contraseña: " + e.getMessage());
            return contraseña; // En caso de error, retorna la contraseña original
        }
    }

    // ===============================
    // Métodos de persistencia (DAO)
    // ===============================

    public boolean guardar() {
        return dao.create(this);
    }

    public boolean actualizar() {
        return dao.update(this);
    }

    public boolean desactivar() {
        return dao.eliminar(this.dni);
    }

    public static List<Usuario> obtenerTodos() {
        return dao.findAll();
    }

    public static Usuario verificarLogin(String usuarioIngresado, String contraseñaIngresada) {
        String contraseñaHasheada = hashearContraseña(contraseñaIngresada);
        Usuario usuario = dao.verificarLogin(usuarioIngresado, contraseñaHasheada);        
        // ESTO SE PODRIA SACAR SI NO TENEMOS MAS CONTRASEÑAS EN TEXTO PLANO
        if (usuario == null) {
            usuario = dao.verificarLogin(usuarioIngresado, contraseñaIngresada);
        }
        return usuario;
    }
}
