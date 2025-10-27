package com.unpsjb.poo.model.productos;

import java.util.List;
import com.unpsjb.poo.persistence.dao.impl.FabricanteDAOImpl;

public class Fabricante {
    private int idFabricante;
    private String nombreFabricante;
    
    private static final FabricanteDAOImpl fabricanteDAO = new FabricanteDAOImpl();

    // Constructor
    public Fabricante() {
    }
    // getters y setters
    public int getId() {return idFabricante;}
    public void setId(int idFabricante) {this.idFabricante = idFabricante;}
    public String getNombre() {return nombreFabricante;}
    public void setNombre(String nombreFabricante) {this.nombreFabricante = nombreFabricante;}

    @Override
    public String toString() {return nombreFabricante;}
    
    // Métodos de acceso a persistencia
    public static List<Fabricante> obtenerTodos() {
        return fabricanteDAO.findAll();
    }
    
    public static Fabricante obtenerPorId(int id) {
        return fabricanteDAO.read(id).orElse(null);
    }
    
    public boolean guardar() {
        if (this.idFabricante == 0) {
            return fabricanteDAO.create(this);
        } else {
            return fabricanteDAO.update(this);
        }
    }
    
    public boolean eliminar() {
        return fabricanteDAO.delete(this.idFabricante);
    }
}
