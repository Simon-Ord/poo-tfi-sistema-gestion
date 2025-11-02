package com.unpsjb.poo.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
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

    // Constantes de diseño
    private static final double ALTURA_BARRA_TITULO = 34;
    private static final double ANCHO_MINIMO = 360;
    private static final double ALTO_MINIMO = 220;
    private static final double ANCHO_PREDETERMINADO = 640;
    private static final double ALTO_PREDETERMINADO = 420;
    
    // Constantes de estilo para el botón cerrar
    private static final String ESTILO_BOTON_CERRAR = 
        "-fx-text-fill: white;" +
        "-fx-font-weight: bold;" +
        "-fx-font-size: 12px;" +
        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
        "-fx-min-width: 28px;" +
        "-fx-min-height: 22px;" +
        "-fx-max-width: 28px;" +
        "-fx-max-height: 22px;" +
        "-fx-background-radius: 4px;" +
        "-fx-border-radius: 4px;" +
        "-fx-cursor: hand;" +
        "-fx-padding: 0;";
    
    private static final String COLOR_CERRAR_NORMAL = "#e74c3c";
    private static final String COLOR_CERRAR_HOVER = "#c0392b";
    
    // Componentes de la interfaz
    private final BorderPane contenedorPrincipal = new BorderPane();
    private final HBox barraTitulo = new HBox(8);
    private final Label etiquetaTitulo = new Label();
    private final Button botonCerrar = new Button("×");
    private final StackPane contenedorContenido = new StackPane();

    // Variables para funcionalidad de arrastre
    private double desplazamientoArrastreX, desplazamientoArrastreY;

    // Constructor principal
    public VentanaVistaControlador(String titulo, Node contenido) {
        inicializarVentana(titulo, contenido);
    }
    private void inicializarVentana(String titulo, Node contenido) {
        // Configurar título con estilo más elegante
        etiquetaTitulo.setText(titulo);
        etiquetaTitulo.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: 600; -fx-font-size: 13px; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        
        // Configurar botón cerrar
        configurarBotonCerrar();
        
        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);
        
        // Agregar elementos a la barra de título
        agregarElementosBarraTitulo(espaciador);
        
        configurarBarraTitulo();
        configurarContenedorContenido(contenido);
        configurarContenedorPrincipal();
        
        getChildren().add(contenedorPrincipal);
        habilitarArrastre();
        configurarEventosVentana();
        
        setMinSize(ANCHO_MINIMO, ALTO_MINIMO);
        setPrefSize(ANCHO_PREDETERMINADO, ALTO_PREDETERMINADO);
    }
    
    private void configurarBarraTitulo() {
        barraTitulo.setAlignment(Pos.CENTER_LEFT);
        barraTitulo.setPadding(new Insets(0, 8, 0, 12));
        barraTitulo.setMinHeight(ALTURA_BARRA_TITULO);
        barraTitulo.setPrefHeight(ALTURA_BARRA_TITULO);
        barraTitulo.setMaxHeight(ALTURA_BARRA_TITULO);
        
        // Estilo para la barra de titulo con gradiente sutil
        barraTitulo.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #4a4a4a, #3a3a3a);" +
            "-fx-border-color: #2a2a2a;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-radius: 10px 10px 0 0;" +
            "-fx-border-radius: 10px 10px 0 0;");
    }
    
    private void configurarContenedorContenido(Node contenido) {
        contenedorContenido.getChildren().add(contenido);
        contenedorContenido.setPadding(new Insets(20, 16, 16, 16));
        contenedorContenido.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #000000ff, #192858ff);" +
            "-fx-background-radius: 0 0 10px 10px;" +
            "-fx-border-radius: 0 0 10px 10px;");
    }
    
    private void configurarContenedorPrincipal() {
        contenedorPrincipal.setTop(barraTitulo);
        contenedorPrincipal.setCenter(contenedorContenido);
        
        // Frame principal
        contenedorPrincipal.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 10px;" +
            "-fx-border-radius: 10px;" +
            "-fx-border-color: linear-gradient(to bottom, #606060, #404040);" +
            "-fx-border-width: 1px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0.3, 0, 5);");
    }
    
    private void configurarEventosVentana() {
        // Configurar acciones del botón cerrar
        botonCerrar.setOnAction(e -> cerrarVentana());
        
        // Al clickear, traer al frente
        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> toFront());
    }
    
    // Configura el estilo del botón cerrar
    private void configurarBotonCerrar() {
        botonCerrar.setText("×");
        
        // Estilo normal
        String estiloNormal = ESTILO_BOTON_CERRAR +
                             "-fx-background-color: " + COLOR_CERRAR_NORMAL + ";" +
                             "-fx-border-color: rgba(0,0,0,0.1);" +
                             "-fx-border-width: 1px;";
        
        // Estilo hover
        String estiloHover = ESTILO_BOTON_CERRAR +
                            "-fx-background-color: " + COLOR_CERRAR_HOVER + ";" +
                            "-fx-border-color: rgba(255,255,255,0.2);" +
                            "-fx-border-width: 1px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 1);";
        
        botonCerrar.setStyle(estiloNormal);
        
        // Efectos hover
        botonCerrar.setOnMouseEntered(e -> botonCerrar.setStyle(estiloHover));
        botonCerrar.setOnMouseExited(e -> botonCerrar.setStyle(estiloNormal));
    }
    
    // Agrega los elementos a la barra de título
    private void agregarElementosBarraTitulo(Region espaciador) {
        barraTitulo.getChildren().addAll(etiquetaTitulo, espaciador, botonCerrar);
    }
    
    // Cerrar la ventana de forma directa
    private void cerrarVentana() {
        if (getParent() != null) {
            ((Pane) getParent()).getChildren().remove(this);
        }
    }
    // ====================================
    //         ARRASTRE DE VENTANAS
    // ====================================
    
    // Habilita funcionalidad básica de arrastre simple de ventanas 
    private void habilitarArrastre() {
        barraTitulo.setOnMousePressed(e -> {
            desplazamientoArrastreX = e.getSceneX() - getLayoutX();
            desplazamientoArrastreY = e.getSceneY() - getLayoutY();
            setCursor(Cursor.MOVE);
            toFront();
        });
        barraTitulo.setOnMouseDragged(e -> {
            // Arrastre simple con límites para evitar que se escape
            double nuevaPosicionX = e.getSceneX() - desplazamientoArrastreX;
            double nuevaPosicionY = e.getSceneY() - desplazamientoArrastreY;
            
            // Aplicar límites de posición
            if (getParent() != null) {
                // Límite superior: mantener al menos 2px de la barra de título visible
                nuevaPosicionY = Math.max(2, nuevaPosicionY);
                // Límite izquierdo: no permitir que se vaya completamente a la izquierda
                nuevaPosicionX = Math.max(-getPrefWidth() + 50, nuevaPosicionX);
                // Límites derecho e inferior basados en el contenedor padre
                double anchoContenedorPadre = getParent().getBoundsInLocal().getWidth();
                double altoContenedorPadre = getParent().getBoundsInLocal().getHeight();
                nuevaPosicionX = Math.min(anchoContenedorPadre - 50, nuevaPosicionX);
                nuevaPosicionY = Math.min(altoContenedorPadre - ALTURA_BARRA_TITULO, nuevaPosicionY);
            }
            relocate(nuevaPosicionX, nuevaPosicionY);
        });
        barraTitulo.setOnMouseReleased(e -> {
            setCursor(Cursor.DEFAULT);
            // Sin funcionalidad de snap - solo liberar el cursor
        });
    }    
    @Override
    protected void layoutChildren() {
        double ancho = getWidth();
        double alto = getHeight();
        // Siempre usar la altura completa
        contenedorPrincipal.resizeRelocate(0, 0, ancho, alto);
    }

    // ==================================================
    //  MÉTODO DE UTILIDAD ESTÁTICO - CREAR VENTANA
    // ==================================================
    // Crea y abre una ventana interna cargando el FXML 
    public static ResultadoVentana crearVentana(Pane escritorio, String rutaFxml, String titulo, double ancho, double alto) {
        try {
            // Cargar el FXML
            FXMLLoader cargador = new FXMLLoader(VentanaVistaControlador.class.getResource(rutaFxml));
            Node contenido = cargador.load();
            Object controlador = cargador.getController();
            
            // Crear la ventana
            VentanaVistaControlador ventana = new VentanaVistaControlador(titulo, contenido);
            ventana.setPrefSize(ancho, alto);
            
            // Posicionar la ventana con un algoritmo de cascada inteligente
            posicionarVentanaInteligente(escritorio, ventana);
            
            // Agregar al escritorio y traer al frente
            escritorio.getChildren().add(ventana);
            ventana.toFront();

            return new ResultadoVentana(ventana, controlador);
            
        } catch (IOException ex) {
            throw new RuntimeException("Error al crear ventana desde " + rutaFxml, ex);
        }
    }
    
    // Posiciona la ventana de manera inteligente para evitar superposiciones excesivas
    private static void posicionarVentanaInteligente(Pane escritorio, VentanaVistaControlador ventana) {
        int ventanasExistentes = escritorio.getChildren().size();
        
        // Calcular posición en cascada con límites
        double desplazamientoX = 30 + (ventanasExistentes % 8) * 35;
        double desplazamientoY = 30 + (ventanasExistentes % 6) * 25;
        
        // Si hay demasiadas ventanas, reiniciar posiciones con un offset adicional
        if (ventanasExistentes > 15) {
            desplazamientoX += (ventanasExistentes / 16) * 50;
            desplazamientoY += (ventanasExistentes / 16) * 40;
        }
        
        // Asegurar que la ventana esté dentro de los límites del escritorio
        double maximoX = Math.max(50, escritorio.getWidth() - ventana.getPrefWidth() - 20);
        double maximoY = Math.max(50, escritorio.getHeight() - ventana.getPrefHeight() - 20);
        
        desplazamientoX = Math.min(desplazamientoX, maximoX);
        desplazamientoY = Math.min(desplazamientoY, maximoY);
        
        ventana.relocate(desplazamientoX, desplazamientoY);
    }
    
    // ===================================================
    //  MÉTODOS DE CONVENIENCIA
    // ===================================================
    
    // Sobrecarga del método crearVentana con dimensiones por defecto.
    public static ResultadoVentana crearVentana(Pane escritorio, String rutaFxml, String titulo) {
        return crearVentana(escritorio, rutaFxml, titulo, ANCHO_PREDETERMINADO, ALTO_PREDETERMINADO);
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