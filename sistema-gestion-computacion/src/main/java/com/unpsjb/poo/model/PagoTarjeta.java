package com.unpsjb.poo.model;

public class PagoTarjeta implements EstrategiaPago {
    private static final double comision = 0.05;  // 5% de comisión para las tarjetas

    @Override
    public boolean pagar(double monto) {
        double montoConComision = monto * (1 + comision);
        // Lógica para procesar el pago con tarjeta
        System.out.println("Procesando pago con TARJETA por $: " + montoConComision + " (incluye comisión del 5%)");
        return true; // Asumimos que el pago siempre es exitoso en este caso
    }

    @Override
    public String getNombreMetodoPago() {
        return "Pago con Tarjeta";
    }

    @Override
    public double getComision() {
        return comision;
    }
    
}
