package com.unpsjb.poo.model;

import java.util.Map;

import javafx.scene.Node;

public interface EstadoVenta {
    
    // Métodos de navegación (comportamiento básico del estado)
    void siguientePaso(Venta venta);
    void volverPaso(Venta venta);
    
    // Métodos específicos de gestión del estado
    void validarTransicion(Venta venta);
    void limpiarEstado(Venta venta);
    
    // Métodos de información del estado
    String getNombreEstado();
    String getVistaID();
    
    // Inicialización específica del estado (delegada desde el controlador)
    void inicializarVista(Map<String, Node> vistaMap, Venta venta);
}

