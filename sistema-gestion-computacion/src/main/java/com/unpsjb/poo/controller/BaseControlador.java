package com.unpsjb.poo.controller;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Controlador base para crear ventanas internas
 * Todos los controladores que necesiten abrir ventanas internas deben extender esta clase
 */
public abstract class BaseControlador {
    
    /**
     * Obtiene el desktop principal de la aplicación.
     * Busca en todas las ventanas abiertas el Pane con id="desktop"
     */
    protected static Pane obtenerDesktopPrincipal() {
        try {
            // Buscar en todas las ventanas abiertas
            for (Window window : Stage.getWindows()) {
                Stage stage = (Stage) window;
                if (stage.getScene() != null && stage.getScene().getRoot() != null) {
                    Pane desktop = buscarDesktop(stage.getScene().getRoot());
                    if (desktop != null) {
                        return desktop;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener desktop principal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Busca recursivamente el Pane desktop en la jerarquía de nodos
     */
    private static Pane buscarDesktop(Parent parent) {
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child.getClass().equals(Pane.class) && "desktop".equals(child.getId())) {
                return (Pane) child;
            }
            if (child.getClass().getSuperclass().equals(Parent.class) || Parent.class.isAssignableFrom(child.getClass())) {
                Pane resultado = buscarDesktop((Parent) child);
                if (resultado != null) {
                    return resultado;
                }
            }
        }
        return null;
    }
    
    /**
     * Crea una ventana interna principal (con maximizar y minimizar)
     * @param fxmlPath Ruta del archivo FXML
     * @param titulo Título de la ventana
     * @return El resultado de la ventana creada (ventana + controlador)
     */
    protected VentanaVistaControlador.ResultadoVentana crearVentana(String fxmlPath, String titulo) {
        return crearVentana(fxmlPath, titulo, 640, 420);
    }
    
    /**
     * Crea una ventana interna principal con tamaño personalizado
     * @param fxmlPath Ruta del archivo FXML
     * @param titulo Título de la ventana
     * @param ancho Ancho de la ventana
     * @param alto Alto de la ventana
     * @return El resultado de la ventana creada (ventana + controlador)
     */
    protected VentanaVistaControlador.ResultadoVentana crearVentana(String fxmlPath, String titulo, double ancho, double alto) {
        Pane desktop = obtenerDesktopPrincipal();
        if (desktop != null) {
            return VentanaVistaControlador.crearVentana(desktop, fxmlPath, titulo, ancho, alto);
        } else {
            System.err.println("Error: No se pudo obtener el desktop principal");
            return null;
        }
    }
    
    /**
     * Crea un formulario interno (sin maximizar, solo minimizar)
     * @param fxmlPath Ruta del archivo FXML
     * @param titulo Título del formulario
     * @return El resultado de la ventana creada (ventana + controlador)
     */
    protected VentanaVistaControlador.ResultadoVentana crearFormulario(String fxmlPath, String titulo) {
        return crearFormulario(fxmlPath, titulo, 400, 350);
    }
    
    /**
     * Crea un formulario interno con tamaño personalizado
     * @param fxmlPath Ruta del archivo FXML
     * @param titulo Título del formulario
     * @param ancho Ancho del formulario
     * @param alto Alto del formulario
     * @return El resultado de la ventana creada (ventana + controlador)
     */
    protected VentanaVistaControlador.ResultadoVentana crearFormulario(String fxmlPath, String titulo, double ancho, double alto) {
        Pane desktop = obtenerDesktopPrincipal();
        if (desktop != null) {
            return VentanaVistaControlador.crearFormulario(desktop, fxmlPath, titulo, ancho, alto);
        } else {
            System.err.println("Error: No se pudo obtener el desktop principal");
            return null;
        }
    }
    
    /**
     * Crea un diálogo interno (solo cerrar, sin minimizar ni maximizar)
     * @param fxmlPath Ruta del archivo FXML
     * @param titulo Título del diálogo
     * @return El resultado de la ventana creada (ventana + controlador)
     */
    protected VentanaVistaControlador.ResultadoVentana crearDialogo(String fxmlPath, String titulo) {
        return crearDialogo(fxmlPath, titulo, 350, 250);
    }
    
    /**
     * Crea un diálogo interno con tamaño personalizado
     * @param fxmlPath Ruta del archivo FXML
     * @param titulo Título del diálogo
     * @param ancho Ancho del diálogo
     * @param alto Alto del diálogo
     * @return El resultado de la ventana creada (ventana + controlador)
     */
    protected VentanaVistaControlador.ResultadoVentana crearDialogo(String fxmlPath, String titulo, double ancho, double alto) {
        Pane desktop = obtenerDesktopPrincipal();
        if (desktop != null) {
            return VentanaVistaControlador.crearDialogo(desktop, fxmlPath, titulo, ancho, alto);
        } else {
            System.err.println("Error: No se pudo obtener el desktop principal");
            return null;
        }
    }
    
    /**
     * Cierra una ventana interna buscando el VentanaVistaControlador que contiene el nodo dado.
     * Si no se encuentra, intenta cerrar como Stage (para compatibilidad hacia atrás).
     * 
     * @param nodo Cualquier nodo dentro de la ventana a cerrar
     */
    protected static void cerrarVentanaInterna(Node nodo) {
        try {
            // Buscar el VentanaVistaControlador en la jerarquía de padres
            Node nodoActual = nodo;
            while (nodoActual != null && !nodoActual.getClass().equals(VentanaVistaControlador.class)) {
                nodoActual = nodoActual.getParent();
            }
            
            if (nodoActual != null) {
                // Es una ventana interna, removerla del desktop
                VentanaVistaControlador ventanaInterna = (VentanaVistaControlador) nodoActual;
                if (ventanaInterna.getParent() != null) {
                    ((Pane) ventanaInterna.getParent()).getChildren().remove(ventanaInterna);
                }
            } else {
                // Fallback: si no se encuentra la ventana interna, intentar cerrar como Stage
                Stage stage = (Stage) nodo.getScene().getWindow();
                if (stage != null) {
                    stage.close();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cerrar ventana: " + e.getMessage());
        }
    }
    
    /**
     * Abre una ventana interna y permite configurar su controlador después de crearla
     * NO modal - permite múltiples ventanas abiertas simultáneamente
     */
    @SuppressWarnings("unchecked")
    protected <T> T abrirVentanaInternaConControlador(String fxmlPath, String titulo, ControladorConfigurador<T> configurador) {
        return abrirVentanaInternaConControlador(fxmlPath, titulo, 640, 420, configurador);
    }
    /**
     * Abre una ventana interna con tamaño específico y permite configurar su controlador
     */
    @SuppressWarnings("unchecked")
    protected <T> T abrirVentanaInternaConControlador(String fxmlPath, String titulo, double ancho, double alto, ControladorConfigurador<T> configurador) {
        Pane desktop = obtenerDesktopPrincipal();
        if (desktop != null) {
            try {
                // Crear la ventana
                VentanaVistaControlador.ResultadoVentana resultado = 
                    VentanaVistaControlador.crearVentana(desktop, fxmlPath, titulo, ancho, alto);
                
                if (resultado != null) {
                    // Obtener y configurar el controlador
                    T controlador = (T) resultado.getControlador();
                    if (controlador != null && configurador != null) {
                        configurador.configurar(controlador);
                    }
                    return controlador;
                }
                
            } catch (Exception e) {
                System.err.println("Error al crear ventana interna con controlador: " + e.getMessage());
            }
        } else {
            System.err.println("Error: No se pudo obtener el desktop principal");
        }
        return null;
    }
    /**
     * Interfaz funcional para configurar controladores
     */
    @FunctionalInterface
    protected interface ControladorConfigurador<T> {
        void configurar(T controlador);
    }
}