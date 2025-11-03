package com.unpsjb.poo.model;

import java.util.Map;

import javafx.scene.Node;

public interface EstadoVenta {
    
    // Métodos de navegación (comportamiento básico del estado)
    void siguientePaso(Venta venta);
    void volverPaso(Venta venta);
    
    // Método principal: maneja TODAS las solicitudes dependientes del estado
    void manejarSolicitud(Venta venta, String tipoSolicitud, Object... parametros);
    
    // Métodos de información del estado
    String getNombreEstado();
    String getVistaID();
    
    // Inicialización específica del estado (delegada desde el controlador)
    void inicializarVista(Map<String, Node> vistaMap, Venta venta);
}

