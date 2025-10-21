package com.unpsjb.poo.model.productos;


public class Categoria {
    private int idCategoria;
    private String nombreCategoria;

    // Constructor
    public Categoria() {}

    // getters y setters
    public int getId() {return idCategoria;}
    public void setId(int idCategoria) {this.idCategoria = idCategoria;}
    public String getNombre() {return nombreCategoria;}
    public void setNombre(String nombreCategoria) {this.nombreCategoria = nombreCategoria;}

    @Override
    public String toString() {
        return nombreCategoria;
    }
}
