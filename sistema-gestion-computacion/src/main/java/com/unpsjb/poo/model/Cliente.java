package com.unpsjb.poo.model;

public class Cliente {

    private Integer id;           
    private String nombre;     // Obligatorio, 100 caracteres
    private String cuit;       // Opcional
    private String telefono;   // Opcional
    private String direccion;  // Opcional
    private String email;      // Opcional

    // ========= Constructor ========= //
    public Cliente (Integer id, String nombre, String cuit, String telefono, String direccion, String email) {
        this.id = id;
        this.nombre = nombre;
        this.cuit = cuit;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }
    // ========= Getters / Setters con validacion ========= //
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) // Validacion doble, nulo o ""
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (nombre.trim().length() < 3 || nombre.trim().length() > 100)
            throw new IllegalArgumentException("El nombre debe tener entre 3 y 100 caracteres");
        this.nombre = nombre;
    }
      // Acepta null/"" (consumidor final)//
    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { 
        if (cuit == null || cuit.isBlank()) {
            this.cuit = null;
            return;
        }
        if (cuit.length() != 11)
            throw new IllegalArgumentException("CUIT inválido");
        this.cuit = cuit;
    }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            this.telefono = null;
            return;
        }
        if (telefono.trim().length() > 40) throw new IllegalArgumentException("Teléfono demasiado largo");
        this.telefono = telefono;
    }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            this.direccion = null;
            return;
        }
        if (direccion.trim().length() > 200) throw new IllegalArgumentException("Dirección demasiado larga");
        this.direccion = direccion;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            this.email = null;
            return;
        }
        if (email.trim().length() > 100) throw new IllegalArgumentException("Email demasiado largo");
        this.email = email.toLowerCase();
    }
    // Es consumidor final si no tiene CUIT cargado //
    public boolean esConsumidorFinal() {
        return this.cuit == null || this.cuit.isBlank();
    }
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cuit='" + (cuit == null ? "-" : cuit) + '\'' +
                ", telefono='" + (telefono == null ? "-" : telefono) + '\'' +
                ", email='" + (email == null ? "-" : email) + '\'' +
                '}';
    }
    
}
