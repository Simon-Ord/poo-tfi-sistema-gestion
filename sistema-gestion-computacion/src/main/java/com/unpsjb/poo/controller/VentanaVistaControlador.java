package com.unpsjb.poo.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

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

        // ============================================================
        // === APLICACIÓN DE CSS PROPIO DE LA VENTANA INTERNA ===
        // ============================================================
        /*
        // Clase raíz y carga del archivo de estilos
        getStyleClass().add("internal-window");
        getStylesheets().add(getClass().getResource("/view/view.css/VentanaVista.css").toExternalForm()
        );

        // Clases específicas para los elementos internos
        titleBar.getStyleClass().add("iw-titlebar");
        titleLbl.getStyleClass().add("iw-title");
        contentHolder.getStyleClass().add("iw-content");
        btnMin.getStyleClass().addAll("iw-btn");
        btnClose.getStyleClass().addAll("iw-btn", "danger");
        */
        // ============================================================

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

}

