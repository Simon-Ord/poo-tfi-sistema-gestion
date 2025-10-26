package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;
import com.unpsjb.poo.util.Sesion;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CambioDatosController {

    @FXML private TextField txtUsuarioActual;
    @FXML private PasswordField txtContrasenaActual;
    @FXML private PasswordField txtRepetirContrasena;
    @FXML private TextField txtNuevoNombre;
    @FXML private TextField txtNuevoUsuario;
    @FXML private PasswordField txtNuevaContrasena;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @FXML
    private void guardarCambios() {
        try {
            String usuarioActual = txtUsuarioActual.getText().trim();
            String contrasenaActual = txtContrasenaActual.getText().trim();
            String repetirContrasena = txtRepetirContrasena.getText().trim();

            // Validar campos obligatorios
            if (usuarioActual.isEmpty() || contrasenaActual.isEmpty() || repetirContrasena.isEmpty()) {
                mostrarAlerta("Debes completar los campos de usuario y contraseña actual.");
                return;
            }

            // Verificar que las contraseñas actuales coincidan
            if (!contrasenaActual.equals(repetirContrasena)) {
                mostrarAlerta("Las contraseñas actuales no coinciden.");
                return;
            }

            // Verificar si el usuario existe
            Usuario user = usuarioDAO.verificarLogin(usuarioActual, contrasenaActual);
            if (user == null) {
                mostrarAlerta("Usuario o contraseña incorrectos.");
                return;
            }

            // Actualizar datos (solo si se ingresaron nuevos)
            if (!txtNuevoNombre.getText().trim().isEmpty()) {
                user.setNombre(txtNuevoNombre.getText().trim());
            }
            if (!txtNuevoUsuario.getText().trim().isEmpty()) {
                user.setUsuario(txtNuevoUsuario.getText().trim());
            }
            if (!txtNuevaContrasena.getText().trim().isEmpty()) {
                user.setContraseña(txtNuevaContrasena.getText().trim());
            }

            boolean ok = usuarioDAO.modificar(user);

            if (ok) {
                mostrarAlerta("Datos actualizados correctamente.");

                // ✅ Registrar evento con AuditoriaManager
                AuditoriaUtil.registrarAccion(
                    "MODIFICAR DATOS",
                    "usuario",
                    "El usuario modificó sus datos personales."
                );

                cerrarVentana();

            } else {
                mostrarAlerta("Error al actualizar los datos.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado: " + e.getMessage());
           // AuditoriaManager.registrarError("usuario", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        BaseControlador.cerrarVentanaInterna(txtUsuarioActual);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}