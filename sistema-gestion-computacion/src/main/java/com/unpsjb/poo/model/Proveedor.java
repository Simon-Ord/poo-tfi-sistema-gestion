package com.unpsjb.poo.model;

import java.util.List;

import com.unpsjb.poo.persistence.dao.impl.ProveedorDAOImpl;

public class Proveedor {
    private int id;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private String tipo; // DIGITAL or FISICO
    private String cuit;
    private boolean activo;

    private static final ProveedorDAOImpl dao = new ProveedorDAOImpl();

    // Constructor por defecto
    public Proveedor() {
        this.activo = true;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    // Métodos de persistencia
    public boolean guardar() {
        return dao.create(this);
    }

    public boolean actualizar() {
        return dao.update(this);
    }

    /**
     * Alterna el estado activo/inactivo del proveedor en la base de datos.
     * Devuelve true si la operación fue exitosa.
     */
    public boolean cambiarEstado() {
        boolean ok = dao.toggleActivo(this.id);
        if (ok) {
            this.activo = !this.activo; // reflejar en memoria
        }
        return ok;
    }

    /** Método legado para compatibilidad con código previo */
    public boolean desactivar() {
        return cambiarEstado();
    }

    public static List<Proveedor> obtenerTodos() {
        return dao.findAll();
    }

    public static List<Proveedor> obtenerActivos() {
        return dao.findAllActivos();
    }
}