/****

package com.unpsjb.poo.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.unpsjb.poo.model.EstadoAgregarProductos;
import com.unpsjb.poo.model.Venta;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class FacturaVistaControlador implements Initializable {

    // Vistas inyectadas desde el FXML principal (BorderPane + StackPane)
    @FXML private StackPane contentStackPane;
    @FXML private Node FacturaAgregarProductos; // ASUME fx:id="FacturaAgregarProductos"
    @FXML private Node FacturaDatosVenta;       // ASUME fx:id="FacturaDatosVenta"
    @FXML private Node FacturaConfirmarVenta;   // ASUME fx:id="FacturaConfirmarVenta"
    
    // Referencia al Modelo (Contexto del Patrón State)
    private Venta miVenta;
    
    // Mapa para asociar el ID de la vista con el nodo FXML (sin usar 'instanceof')
    private Map<String, Node> vistaMap;

    // --- Constructor / Inicialización ---

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Inicializar el motor de venta
        miVenta = new Venta(); // Llama al constructor que inicializa Carrito y Estado

        // 2. Inicializar el mapa de vistas
        vistaMap = new HashMap<>();
        // Los valores de las claves deben coincidir con lo que devuelve estado.getVistaID()
        vistaMap.put("FacturaAgregarProductos.fxml", FacturaAgregarProductos);
        vistaMap.put("FacturaDatosVenta.fxml", FacturaDatosVenta);
        vistaMap.put("FacturaConfirmarVenta.fxml", FacturaConfirmarVenta);

        // 3. Establecer la vista inicial (El estado inicial de Venta)
        actualizarVisibilidadVistas(miVenta.getEstadoActual().getVistaID());
    }
    
 /*
    // --- Lógica de la Vista ---

    /*
     * Muestra la vista FXML que corresponde al ID devuelto por el Estado.
     * @param nuevaVistaID El ID de la vista (ej: "FacturaAgregarProductos.fxml")
     */
   /* 
     public void actualizarVisibilidadVistas(String nuevaVistaID) {
        // 1. Ocultar todas las vistas
        for (Node view : vistaMap.values()) {
            view.setVisible(false);
        }
        
        // 2. Mostrar solo la vista que corresponde al ID
        Node vistaAMostrar = vistaMap.get(nuevaVistaID);
        if (vistaAMostrar != null) {
            vistaAMostrar.setVisible(true);
        } else {
            System.err.println("Error: No se encontró el FXML para el ID: " + nuevaVistaID);
        }
    }

    // --- Manejo de Eventos (Delegación al Modelo) ---

    /**
     * Llamado por el botón "Siguiente" en el BorderPane.
     */
    
    
    /* 
    
    
    
     @FXML
    public void handleSiguientePaso() {
        // 1. El Modelo (Venta) avanza de estado y ejecuta la lógica de validación
        miVenta.siguientePaso(); 
        
        // 2. El Controlador actualiza la UI al nuevo estado
        String nuevaVistaID = miVenta.getEstadoActual().getVistaID();
        actualizarVisibilidadVistas(nuevaVistaID);
    }
    
    /**
     * Llamado por el botón "Atrás" en el BorderPane.
     *//* 
    @FXML
    public void handleVolverPaso() {
        // En este caso, asumiremos que Venta tiene un método para retroceder
        // (Tu clase EstadoVenta ya tiene el método volverPaso())
        miVenta.getEstadoActual().volverPaso(miVenta); // El estado actual le dice a Venta que retroceda
        
        // El Controlador actualiza la UI
        String nuevaVistaID = miVenta.getEstadoActual().getVistaID();
        actualizarVisibilidadVistas(nuevaVistaID);
    }

    /**
     * Llamado por el botón "Cancelar Venta" en el BorderPane.
     *
    @FXML
    public void handleCancelarVenta() {
        // Aquí puedes reiniciar el proceso o simplemente cerrar la vista de facturación
        System.out.println("Venta cancelada. Reiniciando proceso.");
        
        // Reiniciar el proceso: establecer el estado inicial y limpiar el carrito
        miVenta.getCarrito().vaciarCarrito();
        miVenta.setEstado(new EstadoAgregarProductos()); 
        
        // Actualizar la UI
        actualizarVisibilidadVistas(miVenta.getEstadoActual().getVistaID());
    }
    
    /* -----------------------------------------------------
       Manejo de Lógica Específica del Formulario (Ejemplo)
       ----------------------------------------------------- */
    
    // NOTA: Los métodos para AGREGAR PRODUCTOS van en el código del archivo 
    // FacturaAgregarProductosController, si decides separarlo, o aquí mismo.
