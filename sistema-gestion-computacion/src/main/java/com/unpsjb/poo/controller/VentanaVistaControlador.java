package com.unpsjb.poo.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class VentanaVistaControlador implements Initializable {

    // Root es el AnchorPane del FXML (la “ventana interna”)
    @FXML private AnchorPane root;
    @FXML private StackPane contentArea;
    @FXML private Label titleLabel;
    @FXML private Region gripE, gripW, gripS, gripN, gripSE, gripNE, gripSW, gripNW;
    @FXML private Node titleBar;
    @FXML private Button btnMin, btnMax, btnClose;

    // Drag
    private double dragOffsetX, dragOffsetY;

    // Estado maximizado/restaurado
    private boolean maximized = false;
    private double prevX, prevY, prevW, prevH; // bounds previos

    // Medidas mínimas
    private static final double MIN_W = 260;
    private static final double MIN_H = 140;
    private static final double TITLE_H = 40;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupMove();
        setupResize();
        setupButtons();
        // Traer al frente al hacer click
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> root.toFront());
    }

    /* ========= API pública para usar desde afuera ========= */

    /** Cambia el título en la barra. */
    public void setTitle(String title) { titleLabel.setText(title); }

    /** Inyecta un nodo como contenido de la ventana. */
    public void setContent(Node node) {
        contentArea.getChildren().setAll(node);
    }

    /** Devuelve el nodo raíz para agregar al “escritorio”. */
    public Parent getRoot() { return root; }

    /* ================== Movimiento ================== */
    private void setupMove() {
        titleBar.setOnMousePressed(e -> {
            if (maximized) return; // no mover estando maximizada
            dragOffsetX = e.getSceneX() - root.getLayoutX();
            dragOffsetY = e.getSceneY() - root.getLayoutY();
            root.setCursor(Cursor.MOVE);
        });
        titleBar.setOnMouseDragged(e -> {
            if (maximized) return;
            Parent parent = root.getParent();
            if (parent == null) return;

            double newX = e.getSceneX() - dragOffsetX;
            double newY = e.getSceneY() - dragOffsetY;

            // Limites dentro del contenedor
            double maxX = ((Region) parent).getWidth()  - root.getWidth();
            double maxY = ((Region) parent).getHeight() - root.getHeight();

            root.setLayoutX(clamp(newX, 0, Math.max(0, maxX)));
            root.setLayoutY(clamp(newY, 0, Math.max(0, maxY)));
        });
        titleBar.setOnMouseReleased(e -> root.setCursor(Cursor.DEFAULT));
    }

    /* ================== Redimensionado ================== */
    private void setupResize() {
        // Lados
        gripE.setOnMouseDragged(e -> resizeFromLeft(e.getSceneX() - (root.getLayoutX() + root.getWidth())));
        gripW.setOnMouseDragged(e -> resizeFromRight((root.getLayoutX()) - e.getSceneX()));
        gripS.setOnMouseDragged(e -> resizeFromTop(e.getSceneY() - (root.getLayoutY() + root.getHeight())));
        gripN.setOnMouseDragged(e -> resizeFromBottom((root.getLayoutY()) - e.getSceneY()));

        // Esquinas (combinaciones)
        gripSE.setOnMouseDragged(e -> { resizeFromTop(e.getSceneY() - (root.getLayoutY() + root.getHeight())); resizeFromLeft(e.getSceneX() - (root.getLayoutX() + root.getWidth())); });
        gripNE.setOnMouseDragged(e -> { resizeFromBottom((root.getLayoutY()) - e.getSceneY());             resizeFromLeft(e.getSceneX() - (root.getLayoutX() + root.getWidth())); });
        gripSW.setOnMouseDragged(e -> { resizeFromTop(e.getSceneY() - (root.getLayoutY() + root.getHeight())); resizeFromRight((root.getLayoutX()) - e.getSceneX()); });
        gripNW.setOnMouseDragged(e -> { resizeFromBottom((root.getLayoutY()) - e.getSceneY());             resizeFromRight((root.getLayoutX()) - e.getSceneX()); });

        // Cambiar cursor en enter/exit ya lo hace el CSS, aquí basta con límites mínimos
    }

    private void resizeFromLeft(double delta) {
        if (maximized) return;
        double newW = clamp(root.getWidth() + delta, MIN_W, 10000);
        root.setPrefWidth(newW);
    }
    private void resizeFromRight(double delta) {
        if (maximized) return;
        double newW = clamp(root.getWidth() + delta, MIN_W, 10000);
        // mover X hacia la izquierda cuando “tira” desde el borde izquierdo
        root.setLayoutX(root.getLayoutX() - delta);
        root.setPrefWidth(newW);
    }
    private void resizeFromTop(double delta) {
        if (maximized) return;
        double newH = clamp(root.getHeight() + delta, MIN_H, 10000);
        root.setPrefHeight(newH);
    }
    private void resizeFromBottom(double delta) {
        if (maximized) return;
        double newH = clamp(root.getHeight() + delta, MIN_H, 10000);
        root.setLayoutY(root.getLayoutY() - delta);
        root.setPrefHeight(newH);
    }

    /* ================== Botones ================== */
    private void setupButtons() {
        btnClose.setOnAction(e -> {
            if (root.getParent() instanceof AnchorPane pane) {
                pane.getChildren().remove(root);
            } else if (root.getParent() instanceof Region r) {
                ((Region) r).getChildrenUnmodifiable().remove(root); // fallback
            }
        });

        btnMin.setOnAction(e -> toggleMinimize());
        btnMax.setOnAction(e -> toggleMaximize());
    }

    private void toggleMinimize() {
        boolean minimized = contentArea.isManaged();
        if (minimized) {
            // Minimizar: oculto contenido y dejo solo barra
            contentArea.setManaged(false);
            contentArea.setVisible(false);
            root.setPrefHeight(TITLE_H);
        } else {
            // Restaurar altura mínima
            contentArea.setManaged(true);
            contentArea.setVisible(true);
            root.setPrefHeight(Math.max(root.getPrefHeight(), 220));
        }
    }

    private void toggleMaximize() {
        Parent parent = root.getParent();
        if (!(parent instanceof Region desktop)) return;

        if (!maximized) {
            // Guardar bounds actuales
            prevX = root.getLayoutX();
            prevY = root.getLayoutY();
            prevW = root.getWidth();
            prevH = root.getHeight();

            // Expandir a todo el contenedor
            root.setLayoutX(0);
            root.setLayoutY(0);
            root.setPrefWidth(desktop.getWidth());
            root.setPrefHeight(desktop.getHeight());
            maximized = true;
        } else {
            // Restaurar
            root.setLayoutX(prevX);
            root.setLayoutY(prevY);
            root.setPrefWidth(prevW);
            root.setPrefHeight(prevH);
            maximized = false;
        }
    }

    /* ================== Utils ================== */
    private double clamp(double v, double min, double max) { return Math.max(min, Math.min(max, v)); }
}
