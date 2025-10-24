

package com.unpsjb.poo.model;

public interface EstadoVenta {
    void siguientePaso (Venta venta);
	void volverPaso (Venta venta);
	String getNombreEstado();
	String getVistaID();  //Agregado para vincular con la vista correspondiente
}

