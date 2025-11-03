package com.unpsjb.poo.model;

public interface EstrategiaPago {

    boolean pagar(double monto);
    String getNombreMetodoPago();
    double getComision();
    String getDescripcion();
}
