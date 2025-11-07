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
        this.contraseña = contraseña; // No hasheamos aquí, ya que podría venir de la BD
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    private String email;

    public String getEmail() { 
    return email; 
    }
    public void setEmail(String email) { 
    this.email = email; 
    }
    public boolean verificarContraseña(String contraseñaIngresada) {
        System.out.println("DEBUG verificar - Contraseña ingresada: " + contraseñaIngresada);
        System.out.println("DEBUG verificar - Contraseña almacenada en objeto: " + this.contraseña);
        
        // Primero intentamos verificar en texto plano (como en el login)
        if (this.contraseña != null && this.contraseña.equals(contraseñaIngresada)) {
            System.out.println("DEBUG verificar - Coincide en texto plano");
            return true;
        }
        
        // Si no coincide, intentamos con el hash
        String hashIngresado = hashearContraseña(contraseñaIngresada);
        System.out.println("DEBUG verificar - Hash de contraseña ingresada: " + hashIngresado);
        
        boolean coincideHash = this.contraseña != null && this.contraseña.equals(hashIngresado);
        System.out.println("DEBUG verificar - Coincide con hash: " + coincideHash);
        return coincideHash;
    }

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
        System.out.println("DEBUG login - Contraseña ingresada: " + contraseñaIngresada);
        String contraseñaHasheada = hashearContraseña(contraseñaIngresada);
        System.out.println("DEBUG login - Contraseña hasheada: " + contraseñaHasheada);
        
        Usuario usuario = dao.verificarLogin(usuarioIngresado, contraseñaHasheada);        
        if (usuario == null) {
            System.out.println("DEBUG login - Intento con texto plano");
            usuario = dao.verificarLogin(usuarioIngresado, contraseñaIngresada);
        }
        
        if (usuario != null) {
            System.out.println("DEBUG login - Contraseña en BD: " + usuario.getContraseña());
        }
        
        return usuario;
    }
}
