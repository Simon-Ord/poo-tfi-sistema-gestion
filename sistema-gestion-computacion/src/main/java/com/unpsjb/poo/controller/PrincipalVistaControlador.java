package com.unpsjb.poo.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.unpsjb.poo.util.AuditoriaManager;
import com.unpsjb.poo.util.Sesion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrincipalVistaControlador implements Initializable {

    @FXML private Pane desktop;
    @FXML private Button btnUsuarios;
    @FXML private Button btnClientes;
    @FXML private MenuButton mnBtnProductos;
    @FXML private Button btnFacturar;
    @FXML private Button btnReportes;
    @FXML private Button btnCerrarSesion;
    @FXML private Label lblNombreUsuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Sesion.getUsuarioActual() != null) {
            lblNombreUsuario.setText("Usuario: " + Sesion.getUsuarioActual().getNombre());
        } else {
            lblNombreUsuario.setText("Usuario: Desconocido");
        }
    }

    // ==================================================
    // 🔹 MÉTODOS DE UTILIDAD
    // ==================================================

    private void openInternal(String title, Node content, double w, double h) {
        VentanaVistaControlador win = new VentanaVistaControlador(title, content);
        win.setPrefSize(w, h);
        int count = desktop.getChildren().size();
        win.relocate(30 + 24 * count, 30 + 18 * count);
        desktop.getChildren().add(win);
        win.toFront();
    }

    private Node loadView(String resource) {
        try {
            return FXMLLoader.load(getClass().getResource(resource));
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Error cargando " + resource + ": " + ex.getMessage()).showAndWait();
            throw new RuntimeException(ex);
        }
    }

    // ==================================================
    //  BOTONES DE MENÚ
    // ==================================================

    @FXML
    private void usuariosAction(ActionEvent event) {
        try {
            Node view = loadView("/view/usuariosView.fxml");
            openInternal("Gestión de Usuarios", view, 800, 500);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir la gestión de usuarios: " + e.getMessage()).showAndWait();
        }
    }
    // =====================
    //  BOTON DE PRODUCTOS
    // =====================
    @FXML
    private void productosAction(ActionEvent event) {
        Node view = loadView("/view/productosVista.fxml");
        openInternal("Productos", view, 1000, 500);
    }
    @FXML
    private void agregarProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/productoForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Nuevo Producto");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
           // cargarProductos();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir el formulario de producto: " + e.getMessage()).showAndWait();
        }
    }
    // =====================
    //  BOTON DE PRODUCTOS
    // =====================
    @FXML private void clientesAction(ActionEvent event) {
                try {
            Node view = loadView("/view/ClientesView.fxml");
            openInternal("Gestión de Clientes", view, 800, 500);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir la gestión de usuarios: " + e.getMessage()).showAndWait();
        }
    }
    @FXML private void facturarAction(ActionEvent event) {}

    @FXML
    private void reportesAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reportesView.fxml"));
            Node view = loader.load();

            ReportesControlador controller = loader.getController();
            openInternal("Reportes del Sistema", view, 900, 600);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir reportes: " + e.getMessage()).showAndWait();
        }
    }

    // ==================================================
    // 🔹 CERRAR SESIÓN
    // ==================================================
    @FXML
    private void cerrarSesionAction(ActionEvent event) {
        try {
            // 🔹 Registrar el evento de cierre usando AuditoriaManager
            AuditoriaManager.registrar("CERRAR SESIÓN", "sesion", "cerró sesión.");

            Sesion.cerrarSesion();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
            Parent root = loader.load();
            Scene loginScene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Inicio de Sesión");
            stage.setMaximized(false);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
