package com.unpsjb.poo.model;

public interface EstrategiaPago {
    
/*
*Retorna true si el pago se realizó con éxito, false en caso contrario
*/    
boolean pagar(double monto);

String getNombreMetodoPago();
}
