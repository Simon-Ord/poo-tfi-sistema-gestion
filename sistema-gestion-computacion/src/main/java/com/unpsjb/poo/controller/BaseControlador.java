package com.unpsjb.poo.controller;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;


// Controlador base para crear ventanas internas
public class BaseControlador {
    
    // Metodo para obtener el desktop principal, buscando en todas las ventanas abiertas 
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

    // Metodo para buscar el Pane desktop 
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
    // Metodo para crear ventana con tamaño personalizado
    protected VentanaVistaControlador.ResultadoVentana crearVentana(String fxmlPath, String titulo, double ancho, double alto) {
        Pane desktop = obtenerDesktopPrincipal();
        if (desktop != null) {
            return VentanaVistaControlador.crearVentana(desktop, fxmlPath, titulo, ancho, alto);
        } else {
            System.err.println("Error: No se pudo obtener el desktop principal");
            return null;
        }
    }
    // Metodo para crear ventana con tamaño por defecto
    protected VentanaVistaControlador.ResultadoVentana crearVentana(String fxmlPath, String titulo) {
        return crearVentana(fxmlPath, titulo, 640, 420);
    }
    // Metodo para crear ventana pequeña (tamaño típico de formulario)
    protected VentanaVistaControlador.ResultadoVentana crearVentanaPequena(String fxmlPath, String titulo) {
        return crearVentana(fxmlPath, titulo, 400, 350);
    }
    // Metodo para cerrar ventana interna buscando el VentanaVistaControlador que tiene su nodo
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
            } 
        } catch (Exception e) {
            System.err.println("Error al cerrar ventana: " + e.getMessage());
        }
    }
}