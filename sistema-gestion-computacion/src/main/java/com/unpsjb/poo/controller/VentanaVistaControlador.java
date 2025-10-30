package com.unpsjb.poo.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public class VentanaVistaControlador extends Region {

    private static final double HEADER_HEIGHT = 34;
    private static final double MIN_W = 360;
    private static final double MIN_H = 220;
    private final BorderPane frame = new BorderPane();
    private final HBox titleBar = new HBox(8);
    private final Label titleLbl = new Label();
    private final Button btnClose = new Button("⨯");
    private final StackPane contentHolder = new StackPane();

    // Variables para arrastre simple
    private double dragOffsetX, dragOffsetY;

    // Constructor principal
    public VentanaVistaControlador(String title, Node content) {
        inicializarVentana(title, content);
    }
    private void inicializarVentana(String title, Node content) {
        // Configurar título con estilo más elegante
        titleLbl.setText(title);
        titleLbl.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: 600; -fx-font-size: 13px; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        // Configurar botones según el tipo de ventana
        configurarBotonesPorTipo();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        // Agregar botones según el tipo de ventana
        agregarBotonesPorTipo(spacer);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(0, 8, 0, 12));
        titleBar.setMinHeight(HEADER_HEIGHT);
        titleBar.setPrefHeight(HEADER_HEIGHT);
        titleBar.setMaxHeight(HEADER_HEIGHT);
        
        // Estilo para la barra de titulo con gradiente sutil
        titleBar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #4a4a4a, #3a3a3a);" +
            "-fx-border-color: #2a2a2a;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-radius: 10px 10px 0 0;" +
            "-fx-border-radius: 10px 10px 0 0;");
        // Contenido
        contentHolder.getChildren().add(content);
        contentHolder.setPadding(new Insets(20, 16, 16, 16));
        contentHolder.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #000000ff, #192858ff);" +
            "-fx-background-radius: 0 0 10px 10px;" +
            "-fx-border-radius: 0 0 10px 10px;");
        frame.setTop(titleBar);
        frame.setCenter(contentHolder);
        // Frame principal
        frame.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 10px;" +
            "-fx-border-radius: 10px;" +
            "-fx-border-color: linear-gradient(to bottom, #606060, #404040);" +
            "-fx-border-width: 1px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0.3, 0, 5);");
        getChildren().add(frame);
        enableDrag();
        // Configurar acciones de los botones
        btnClose.setOnAction(e -> cerrarSinAnimacion());
        // Al clickear, traer al frente
        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            toFront();
        });
        setMinSize(MIN_W, MIN_H);
        setPrefSize(640, 420);
    }
     // Estilo para los botones de la barra de titulo
    private void configurarBotonModerno(Button boton, String texto, String colorNormal, String colorHover) {
        boton.setText(texto);
        boton.setStyle(
            "-fx-background-color: " + colorNormal + ";" +
            "-fx-text-fill: white;" + "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" + "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
            "-fx-min-width: 28px;" + "-fx-min-height: 22px;" +
            "-fx-max-width: 28px;" + "-fx-max-height: 22px;" +
            "-fx-background-radius: 4px;" + "-fx-border-radius: 4px;" +
            "-fx-cursor: hand;" + "-fx-padding: 0;" +
            "-fx-border-color: rgba(0,0,0,0.1);" + "-fx-border-width: 1px;");
        // Efectos hover
        boton.setOnMouseEntered(e -> {
            boton.setStyle(
                "-fx-background-color: " + colorHover + ";" + "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" + "-fx-font-size: 12px;" +
                "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                "-fx-min-width: 28px;" + "-fx-min-height: 22px;" + "-fx-max-width: 28px;" +
                "-fx-max-height: 22px;" + "-fx-background-radius: 4px;" + "-fx-border-radius: 4px;" +
                "-fx-cursor: hand;" + "-fx-padding: 0;" +
                "-fx-border-color: rgba(255,255,255,0.2);" + "-fx-border-width: 1px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 1);"
            );
        });
        boton.setOnMouseExited(e -> {
            boton.setStyle(
                "-fx-background-color: " + colorNormal + ";" +
                "-fx-text-fill: white;" + "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;" + "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                "-fx-min-width: 28px;" + "-fx-min-height: 22px;" + "-fx-max-width: 28px;" +
                "-fx-max-height: 22px;" + "-fx-background-radius: 4px;" +
                "-fx-border-radius: 4px;" + "-fx-cursor: hand;" +
                "-fx-padding: 0;" + "-fx-border-color: rgba(0,0,0,0.1);" +
                "-fx-border-width: 1px;");
        });
    }
     // Configura los botones
    private void configurarBotonesPorTipo() {
        // Configurar botón cerrar
        configurarBotonModerno(btnClose, "×", "#e74c3c", "#c0392b");
    }
    // Agrega los botones a la barra de título
    private void agregarBotonesPorTipo(Region spacer) {
        titleBar.getChildren().addAll(titleLbl, spacer, btnClose);
    }
     // Cerrar la ventana de forma directa
    private void cerrarSinAnimacion() {
        if (getParent() != null) {
            ((Pane) getParent()).getChildren().remove(this);
        }
    }
    // ====================================
    //         ARRASTRE DE VENTANAS
    // ====================================
    
    // Habilita el arrastre simple de ventanas 
    // Funcionalidad básica de mover ventanas
    private void enableDrag() {
        titleBar.setOnMousePressed(e -> {
            dragOffsetX = e.getSceneX() - getLayoutX();
            dragOffsetY = e.getSceneY() - getLayoutY();
            setCursor(Cursor.MOVE);
            toFront();
        });
        titleBar.setOnMouseDragged(e -> {
            // Arrastre simple con límites para evitar que se escape
            double nx = e.getSceneX() - dragOffsetX;
            double ny = e.getSceneY() - dragOffsetY;
            
            // Aplicar límites de posición
            if (getParent() != null) {
                // Límite superior: mantener al menos 2px de la barra de título visible
                ny = Math.max(2, ny);
                // Límite izquierdo: no permitir que se vaya completamente a la izquierda
                nx = Math.max(-getPrefWidth() + 50, nx);
                // Límites derecho e inferior basados en el contenedor padre
                double parentWidth = getParent().getBoundsInLocal().getWidth();
                double parentHeight = getParent().getBoundsInLocal().getHeight();
                nx = Math.min(parentWidth - 50, nx);
                ny = Math.min(parentHeight - HEADER_HEIGHT, ny);
            }
            relocate(nx, ny);
        });
        titleBar.setOnMouseReleased(e -> {
            setCursor(Cursor.DEFAULT);
            // Sin funcionalidad de snap - solo liberar el cursor
        });
    }    
    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        // Siempre usar la altura completa
        frame.resizeRelocate(0, 0, w, h);
    }

    // ==================================================
    //  MÉTODO DE UTILIDAD ESTÁTICO - CREAR VENTANA
    // ==================================================
    // Crea y abre una ventana interna cargando el FXML 
    public static ResultadoVentana crearVentana(Pane desktop, String fxmlPath, String titulo, double ancho, double alto) {
        try {
            // Cargar el FXML
            FXMLLoader loader = new FXMLLoader(VentanaVistaControlador.class.getResource(fxmlPath));
            Node content = loader.load();
            Object controller = loader.getController();
            // Crear la ventana
            VentanaVistaControlador ventana = new VentanaVistaControlador(titulo, content);
            ventana.setPrefSize(ancho, alto);
            // Posicionar la ventana con un algoritmo de cascada inteligente
            posicionarVentanaInteligente(desktop, ventana);
            // Agregar al escritorio y traer al frente
            desktop.getChildren().add(ventana);
            ventana.toFront();

            return new ResultadoVentana(ventana, controller);
            
        } catch (IOException ex) {
            mostrarErrorConEstilo("Error cargando ventana", 
                "No se pudo cargar la vista " + fxmlPath + "\n\nDetalle: " + ex.getMessage());
            throw new RuntimeException("Error al crear ventana desde " + fxmlPath, ex);
        }
    }
    // Posiciona la ventana de manera inteligente para evitar superposiciones excesivas
    private static void posicionarVentanaInteligente(Pane desktop, VentanaVistaControlador ventana) {
        int ventanasExistentes = desktop.getChildren().size();
        // Calcular posición en cascada con límites
        double offsetX = 30 + (ventanasExistentes % 8) * 35;
        double offsetY = 30 + (ventanasExistentes % 6) * 25;
        // Si hay demasiadas ventanas, reiniciar posiciones con un offset adicional
        if (ventanasExistentes > 15) {
            offsetX += (ventanasExistentes / 16) * 50;
            offsetY += (ventanasExistentes / 16) * 40;
        }
        // Asegurar que la ventana esté dentro de los límites del desktop
        double maxX = Math.max(50, desktop.getWidth() - ventana.getPrefWidth() - 20);
        double maxY = Math.max(50, desktop.getHeight() - ventana.getPrefHeight() - 20);
        offsetX = Math.min(offsetX, maxX);
        offsetY = Math.min(offsetY, maxY);
        ventana.relocate(offsetX, offsetY);
    }
    // Muestra un error con estilo moderno
    private static void mostrarErrorConEstilo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("¡Ups! Algo salió mal");
        alert.setContentText(mensaje);
        // Aplicar estilo moderno al diálogo
        alert.getDialogPane().setStyle(
            "-fx-background-color: #ecf0f1;" +
            "-fx-border-color: #ffffffff;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
        );
        alert.showAndWait();
    }
    // ===================================================
    //  METODOS DE CONVENIENCIA
    // ===================================================
    //Sobrecarga del método crearVentana con dimensiones por defecto.
    public static ResultadoVentana crearVentana(Pane desktop, String fxmlPath, String titulo) {
        return crearVentana(desktop, fxmlPath, titulo, 640, 420);
    }
    // Clase contenedora para el resultado de crear una ventana.
    // Permite acceder tanto a la ventana como al controlador del FXML cargado.
    public static class ResultadoVentana {
        private final VentanaVistaControlador ventana;
        private final Object controlador;
        
        public ResultadoVentana(VentanaVistaControlador ventana, Object controlador) {
            this.ventana = ventana;
            this.controlador = controlador;
        }
        public VentanaVistaControlador getVentana() {return ventana;}
        public Object getControlador() {return controlador;}

        // Método para obtener el controlador con el tipo específico
        @SuppressWarnings("unchecked")
        public <T> T getControlador(Class<T> tipo) {
            return (T) controlador;
        }
    }
}