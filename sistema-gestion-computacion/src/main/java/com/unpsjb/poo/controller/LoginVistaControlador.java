package com.unpsjb.poo.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.Sesion;

/**
 * Controlador de la vista de Login.
 * Gestiona el inicio de sesión y acceso al cambio de datos.
 */
public class LoginVistaControlador {

    @FXML private Button btnLogin;
    @FXML private Button btnCambiarDatos;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtUser;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
    private final ReportesDAO reportesDAO = new ReportesDAO();

    @FXML
    void eventKey(ActionEvent event) {}

    // 🔹 BOTÓN LOGIN
    @FXML
    void btnLoginAction(ActionEvent event) {
        try {
            String usuario = txtUser.getText().trim();
            String contraseña = txtPassword.getText().trim();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos.");
                return;
            }

            Usuario user = usuarioDAO.verificarLogin(usuario, contraseña);

            if (user != null) {
                // Iniciar sesión en memoria
                Sesion.iniciarSesion(user);

                // Registrar evento con NOMBRE COMPLETO
                EventoAuditoria evento = new EventoAuditoria();
                evento.setUsuario(user.getNombre()); // <-- ahora guarda el nombre y apellido
                evento.setAccion("INICIAR SESIÓN");
                evento.setEntidad("sesion");
                evento.setDetalles("El usuario " + user.getNombre() + " inició sesión correctamente.");

                // Registrar en BD
                reportesDAO.registrarEvento(evento);

                // Cargar la vista principal
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/principalView.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Sistema de Gestión - Menú Principal");
                stage.setScene(new Scene(root));
                stage.setMaximized(true);
                stage.show();

                // Cerrar login
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();

            } else {
                mostrarAlerta("Usuario o contraseña incorrectos o usuario inactivo.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar la ventana principal.");
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
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de cambio de datos.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Inicio de Sesión");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
