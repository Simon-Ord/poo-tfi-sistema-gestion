package com.unpsjb.poo.model.productos;

import java.util.List;
import com.unpsjb.poo.persistence.dao.impl.ProveedorDigitalDAOImpl;

public class ProveedorDigital {
    private int idProveedorDigital;
    private String nombreProveedorDigital;
    
    private static final ProveedorDigitalDAOImpl proveedorDAO = new ProveedorDigitalDAOImpl();

    // Constructor
    public ProveedorDigital() {
    }

    // getters y setters
    public int getId() {return idProveedorDigital;}
    public void setId(int idProveedorDigital) {this.idProveedorDigital = idProveedorDigital;}
    public String getNombre() {return nombreProveedorDigital;}
    public void setNombre(String nombreProveedorDigital) {this.nombreProveedorDigital = nombreProveedorDigital;}

    @Override
    public String toString() {return nombreProveedorDigital;}
    
    // Métodos de acceso a persistencia
    public static List<ProveedorDigital> obtenerTodos() {
        return proveedorDAO.findAll();
    }
    
    public static ProveedorDigital obtenerPorId(int id) {
        return proveedorDAO.read(id).orElse(null);
    }
    
    public boolean guardar() {
        if (this.idProveedorDigital == 0) {
            return proveedorDAO.create(this);
        } else {
            return proveedorDAO.update(this);
        }
    }
    
    public boolean eliminar() {
        return proveedorDAO.delete(this.idProveedorDigital);
    }
}
