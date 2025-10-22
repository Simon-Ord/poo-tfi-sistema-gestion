package com.unpsjb.poo.controller;

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
    private static final double RESIZE_MARGIN = 8;

    private final BorderPane frame = new BorderPane();
    private final HBox titleBar = new HBox(8);
    private final Label titleLbl = new Label();
    private final Button btnMin = new Button("—");
    private final Button btnClose = new Button("✕");
    private final StackPane contentHolder = new StackPane();

    private boolean minimized = false;

    // para arrastre
    private double dragOffsetX, dragOffsetY;

    public VentanaVistaControlador(String title, Node content) {
        // Título + botones
        titleLbl.setText(title);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBar.getChildren().addAll(titleLbl, spacer, btnMin, btnClose);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(0, 8, 0, 12));
        titleBar.setMinHeight(HEADER_HEIGHT);
        titleBar.setPrefHeight(HEADER_HEIGHT);
        titleBar.setMaxHeight(HEADER_HEIGHT);

        // Contenido
        contentHolder.getChildren().add(content);
        contentHolder.setPadding(new Insets(12));

        frame.setTop(titleBar);
        frame.setCenter(contentHolder);

        getChildren().add(frame);
        enableDrag();
        enableResize();

        btnClose.setOnAction(e -> ((Pane) getParent()).getChildren().remove(this));
        btnMin.setOnAction(e -> toggleMinimize());

        // Al clickear, traer al frente
        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> toFront());

        setMinSize(MIN_W, MIN_H);
        setPrefSize(640, 420);
    }

    private void toggleMinimize() {
        minimized = !minimized;
        contentHolder.setVisible(!minimized);
        contentHolder.setManaged(!minimized);
        // ajustar altura cuando se minimiza
        requestLayout();
    }

    private void enableDrag() {
        titleBar.setOnMousePressed(e -> {
            dragOffsetX = e.getSceneX() - getLayoutX();
            dragOffsetY = e.getSceneY() - getLayoutY();
            setCursor(Cursor.MOVE);
            toFront();
        });
        titleBar.setOnMouseDragged(e -> {
            double nx = e.getSceneX() - dragOffsetX;
            double ny = e.getSceneY() - dragOffsetY;
            relocate(nx, ny);
        });
        titleBar.setOnMouseReleased(e -> setCursor(Cursor.DEFAULT));
    }

    private void enableResize() {
        // Solo borde inferior-derecho para simplificar
        setOnMouseMoved(e -> {
            if (isInResizeZone(e.getX(), e.getY())) setCursor(Cursor.SE_RESIZE);
            else setCursor(Cursor.DEFAULT);
        });

        final Delta resizeDelta = new Delta();
        setOnMousePressed(e -> {
            if (isInResizeZone(e.getX(), e.getY())) {
                resizeDelta.x = getWidth() - e.getX();
                resizeDelta.y = getHeight() - e.getY();
                e.consume();
            }
        });
        setOnMouseDragged(e -> {
            if (getCursor() == Cursor.SE_RESIZE) {
                double nw = Math.max(MIN_W, e.getX() + resizeDelta.x);
                double nh = Math.max(minimized ? HEADER_HEIGHT : MIN_H, e.getY() + resizeDelta.y);
                setPrefSize(nw, nh);
                e.consume();
            }
        });
    }

    private boolean isInResizeZone(double x, double y) {
        return x > getWidth() - RESIZE_MARGIN && y > getHeight() - RESIZE_MARGIN;
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();

        // altura cuando está minimizado
        double wantedH = minimized ? HEADER_HEIGHT : h;

        frame.resizeRelocate(0, 0, w, wantedH);
    }

    private static class Delta { double x, y; }

    // ==================================================
    // 🔹 MÉTODO DE UTILIDAD ESTÁTICO - CREAR VENTANA
    // ==================================================
    
    /**
     * Crea y abre una ventana interna en el escritorio cargando una vista FXML.
     * Este método simplifica el proceso de abrir vistas como subventanas.
     * 
     * @param desktop El panel contenedor donde se agregará la ventana (típicamente PrincipalVistaControlador.desktop)
     * @param fxmlPath Ruta del archivo FXML a cargar (ej: "/view/productosVista.fxml")
     * @param titulo Título que se mostrará en la barra de la ventana
     * @param ancho Ancho preferido de la ventana en píxeles
     * @param alto Alto preferido de la ventana en píxeles
     * @return ResultadoVentana conteniendo la ventana creada y el controlador FXML cargado
     * @throws RuntimeException si hay error al cargar el FXML
     */
    public static ResultadoVentana crearVentana(Pane desktop, String fxmlPath, String titulo, double ancho, double alto) {
        try {
            // Cargar el FXML
            FXMLLoader loader = new FXMLLoader(VentanaVistaControlador.class.getResource(fxmlPath));
            Node content = loader.load();
            Object controller = loader.getController();
            
            // Crear la ventana con el contenido cargado
            VentanaVistaControlador ventana = new VentanaVistaControlador(titulo, content);
            ventana.setPrefSize(ancho, alto);
            
            // Posicionar la ventana en cascada
            int count = desktop.getChildren().size();
            ventana.relocate(30 + 24 * count, 30 + 18 * count);
            
            // Agregar al escritorio y traer al frente
            desktop.getChildren().add(ventana);
            ventana.toFront();
            
            return new ResultadoVentana(ventana, controller);
            
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, 
                "Error cargando " + fxmlPath + ": " + ex.getMessage()).showAndWait();
            throw new RuntimeException("Error al crear ventana desde " + fxmlPath, ex);
        }
    }
    
    /**
     * Sobrecarga del método crearVentana con dimensiones por defecto.
     * 
     * @param desktop El panel contenedor donde se agregará la ventana
     * @param fxmlPath Ruta del archivo FXML a cargar
     * @param titulo Título que se mostrará en la barra de la ventana
     * @return ResultadoVentana conteniendo la ventana creada y el controlador FXML cargado
     */
    public static ResultadoVentana crearVentana(Pane desktop, String fxmlPath, String titulo) {
        return crearVentana(desktop, fxmlPath, titulo, 640, 420);
    }
    
    /**
     * Clase contenedora para el resultado de crear una ventana.
     * Permite acceder tanto a la ventana como al controlador del FXML cargado.
     */
    public static class ResultadoVentana {
        private final VentanaVistaControlador ventana;
        private final Object controlador;
        
        public ResultadoVentana(VentanaVistaControlador ventana, Object controlador) {
            this.ventana = ventana;
            this.controlador = controlador;
        }
        
        public VentanaVistaControlador getVentana() {
            return ventana;
        }
        
        public Object getControlador() {
            return controlador;
        }
        
        /**
         * Obtiene el controlador con el tipo específico.
         * @param <T> Tipo del controlador
         * @return El controlador casteado al tipo solicitado
         */
        @SuppressWarnings("unchecked")
        public <T> T getControlador(Class<T> tipo) {
            return (T) controlador;
        }
    }

}

