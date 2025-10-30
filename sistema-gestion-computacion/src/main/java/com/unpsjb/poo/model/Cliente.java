package com.unpsjb.poo.model;

import java.sql.Timestamp;
import java.util.List;

import com.unpsjb.poo.persistence.dao.impl.ClienteDAOImpl;

public class Cliente {

    private int id;
    private String nombre;
    private String cuit;
    private String telefono;
    private String direccion;
    private String email;
    private String tipo;
    private boolean activo;
    private Timestamp fechaCreacion;

    // DAO estático compartido
    private static final ClienteDAOImpl clienteDAO = new ClienteDAOImpl();

    public Cliente() {
        this.activo = true;
    }

    // ========================
    // Getters y Setters
    // ========================
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // ========================
    // Lógica de Negocio
    // ========================
    public void cambiarEstado() {
        this.activo = !this.activo;
    }

    // ========================
    // Acceso a Persistencia
    // ========================
    public boolean guardar() {
        if (this.id == 0) {
            return clienteDAO.create(this);
        } else {
            return clienteDAO.update(this);
        }
    }

    public boolean eliminar() {
        this.activo = false;
        return clienteDAO.update(this);
    }

    public boolean actualizarEstado() {
        return clienteDAO.update(this);
    }

    public static List<Cliente> obtenerTodos() {
        return clienteDAO.findAll();
    }

    public static Cliente obtenerPorId(int id) {
        return clienteDAO.read(id).orElse(null);
    }

    public static List<Cliente> buscarPorNombre(String nombre) {
        return clienteDAO.buscarPorNombre(nombre);
    }

    public Object getApellido() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getApellido'");
    }

    public Object getIdCliente() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getIdCliente'");
    }

    public void setIdCliente(Object idCliente) {
     
        throw new UnsupportedOperationException("Unimplemented method 'setIdCliente'");
    }
}
