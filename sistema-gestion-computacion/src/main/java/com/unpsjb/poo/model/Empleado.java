package com.unpsjb.poo.model;

public class Empleado extends Usuario {

    public Empleado() {
        super();
        setRol("EMPLEADO");
    }

    public Empleado(Usuario u) {
        super(u.getId(), u.getNombre(), u.getUsuario(), u.getContraseña(), "EMPLEADO", u.isEstado());
    }

    @Override
    public void mostrarOpciones() {
        System.out.println("Opciones EMPLEADO: registrar ventas, ver stock, generar tickets.");
    }

    public boolean puedeCrearUsuarios() {
        return false;
    }
}
