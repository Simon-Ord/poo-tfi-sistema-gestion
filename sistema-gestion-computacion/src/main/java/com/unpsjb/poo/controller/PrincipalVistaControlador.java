package com.unpsjb.poo.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.unpsjb.poo.util.AuditoriaUtil;
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

    // ================================================== //
    //             BOTONES DE MENU PRINCIPAL              //
    // ================================================== //

    // =====================
    //  BOTON DE USUARIOS
    // =====================
    @FXML
    private void usuariosAction(ActionEvent event) {
        try {
            VentanaVistaControlador.crearVentana(desktop, "/view/usuariosView.fxml", "Gestión de Usuarios", 800, 500);
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error al abrir la gestión de usuarios: " + e.getMessage()).showAndWait();
        }
    }
    // =====================
    //  BOTON DE PRODUCTOS
    // =====================

    // Ventana para gestionar productos
    @FXML private void productosAction(ActionEvent event) {
        try {
            VentanaVistaControlador.crearVentana(desktop, "/view/productosVista.fxml", "Gestión de Productos", 1000, 700);
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error al abrir la gestión de productos: " + e.getMessage()).showAndWait();
        }
    }
    // Ventana para agregar productos
    @FXML private void agregarProducto() {
        VentanaVistaControlador.crearFormulario(desktop, "/view/productoForm.fxml", "Agregar Producto", 400, 300);
    }

    // =====================
    //  BOTON DE CLIENTES
    // =====================
    @FXML private void clientesAction(ActionEvent event) {
        try {
            VentanaVistaControlador.crearVentana(desktop, "/view/ClientesView.fxml", "Gestión de Clientes", 800, 500);
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error al abrir la gestión de clientes: " + e.getMessage()).showAndWait();
        }
    }
    // =====================
    //  BOTON DE FACTURAR
    // =====================
    @FXML private void facturarAction(ActionEvent event) {}

    // =====================
    //  BOTON DE REPORTES
    // =====================
    @FXML
    private void reportesAction(ActionEvent event) {
        try {
            VentanaVistaControlador.crearVentana(desktop, "/view/reportesView.fxml", "Gestión de Reportes", 900, 600);
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error al abrir la gestión de reportes: " + e.getMessage()).showAndWait();
        }
    }

    // ==================================================
    // 🔹 CERRAR SESIÓN
    // ==================================================
    @FXML
    private void cerrarSesionAction(ActionEvent event) {
        try {
            // 🔹 Registrar el evento de cierre usando AuditoriaManager
            AuditoriaUtil.registrarAccion("CERRAR SESIÓN", "sesion", "cerró sesión.");

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
