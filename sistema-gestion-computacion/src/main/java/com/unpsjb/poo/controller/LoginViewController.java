package com.unpsjb.poo.controller;

import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;

public class LoginViewController implements Initializable {

    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUser;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblError;

    // 🔹 DAO para manejar los usuarios (usa tu conexión GestorDeConexion)
    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

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
        Usuario u = usuarioDAO.verificarLogin(usuario, contrasena);

        if (u != null) {
            mostrarAlerta("Ingreso correcto", "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")", Alert.AlertType.INFORMATION);

            // 🔹 Ejemplo: según rol podrías abrir otra vista
            // if (u instanceof Admin) {
            //     abrirVentanaAdmin();
            // } else if (u instanceof Empleado) {
            //     abrirVentanaEmpleado();
            // }

        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
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
        // Nada especial por ahora
    }
}
