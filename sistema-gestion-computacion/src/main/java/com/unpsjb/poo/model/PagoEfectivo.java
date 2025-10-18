package com.unpsjb.poo.model;

public class PagoEfectivo implements EstrategiaPago {

    @Override
    public boolean pagar(double monto) {
        // Lógica para procesar el pago en efectivo
        System.out.println("Procesando pago en EFECTIVO por $: " + monto);
        return true; // Asumimos que el pago siempre es exitoso en este caso
    }

    @Override
    public String getNombreMetodoPago() {
        return "Pago en Efectivo";
    }
    
}
