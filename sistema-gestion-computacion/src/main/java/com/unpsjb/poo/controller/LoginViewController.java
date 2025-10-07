package com.unpsjb.poo.controller;

import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

 // FXML Controller class
public class LoginViewController implements Initializable {

    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUser;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblError;

    // Datos de conexión a la base de datos
    private final String URL = "jdbc:postgresql://localhost:5432/tienda_computacion";
    private final String USER = "postgres"; // tu usuario de PostgreSQL
    private final String PASSWORD = "alexis1213"; 

    @FXML
    private void eventKey(KeyEvent event) {
        Object evt = event.getSource();

        // Evitar espacios
        if (evt.equals(txtUser) || evt.equals(txtPassword)) {
            if (event.getCharacter().equals(" ")) {
                event.consume();
            }
        }
    }

    @FXML
    private void eventAction(ActionEvent event) {
        Object evt = event.getSource();

        if (evt.equals(btnLogin)) {
            String usuario = txtUser.getText().trim();
            String contrasena = txtPassword.getText().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                mostrarAlerta("Error", "Debe ingresar usuario y contraseña.", Alert.AlertType.ERROR);
                return;
            }

            // Llamamos al método de verificación
            verificarLogin(usuario, contrasena);
        }
    }

    private void verificarLogin(String usuario, String contrasena) {
        String sql = "SELECT rol FROM usuarios WHERE usuario = ? AND contraseña = ? AND estado = TRUE";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                mostrarAlerta("Ingreso correcto", "Bienvenido, tu rol es: " + rol, Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            mostrarAlerta("Error de conexión", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si hace falta
    }
}
