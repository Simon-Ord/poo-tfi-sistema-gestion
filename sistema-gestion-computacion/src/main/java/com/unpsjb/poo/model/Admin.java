package com.unpsjb.poo.model;

public class Admin extends Usuario {

    public Admin() {
        super();
        setRol("ADMIN");
    }

    public Admin(Usuario u) {
        super(u.getId(), u.getNombre(), u.getUsuario(), u.getContraseña(), "ADMIN", u.isEstado());
    }

    @Override
    public void mostrarOpciones() {
        System.out.println("Opciones ADMIN: crear usuarios, modificar datos, ver reportes, gestionar stock.");
    }

    public boolean puedeCrearUsuarios() {
        return true;
    }
}
