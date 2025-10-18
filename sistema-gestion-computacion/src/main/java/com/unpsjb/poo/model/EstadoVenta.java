package com.unpsjb.poo.model;

public interface EstadoVenta {
    void siguientePaso (Venta venta);
	void volverPaso (Venta venta);
	String getNombreEstado();

}
