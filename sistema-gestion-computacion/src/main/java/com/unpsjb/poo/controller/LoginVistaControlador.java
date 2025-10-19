package com.unpsjb.poo.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginVistaControlador {

    @FXML private Button btnLogin;
    @FXML private Button btnCambiarDatos;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtUser;

    @FXML void eventKey(ActionEvent event) {}

    // 🔹 BOTÓN LOGIN
    @FXML
    void btnLoginAction(ActionEvent event) {
        try {
            System.out.println("Botón Login presionado");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/principalView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Sistema de Gestión - Menú Principal");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            // Cerrar ventana de login
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 BOTÓN "MODIFICAR MIS DATOS"
    @FXML
    void btnCambiarDatosAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CambioDatosView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Modificar Mis Datos");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana de login hasta cerrar
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
