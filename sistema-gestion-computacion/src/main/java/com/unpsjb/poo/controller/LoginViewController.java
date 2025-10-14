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
import javafx.stage.Stage;

public class LoginViewController {

    @FXML private Button btnLogin;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtUser;
    
    @FXML void eventKey(ActionEvent event) {}
    
    
    // BOTON LOGIN //
    @FXML void btnLoginAction(ActionEvent event) {
    try {
        // Validacion: no dejar campos vacios
        if (txtUser.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            // Mostrar alerta si alguno está vacio
            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.WARNING);
            alerta.setTitle("Campos vacíos");
            alerta.setHeaderText(null);
            alerta.setContentText("ERROR: complete usuario y contraseña.");
            alerta.showAndWait();
            return; // detiene la ejecucion
        }
        // Si los campos no estan vacios sigue
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/principalView.fxml"));
        Parent root = loader.load();
        // Crear la nueva ventana
        Stage stage = new Stage();
        stage.setTitle("Sistema de Gestión - Menú Principal");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
        // Cerrar la ventana de login
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}

}



