package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CambioDatosController {

    @FXML private TextField txtNuevoEmail;
    @FXML private PasswordField txtConfirmarContrasena;  // Cambiado de txtRepetirContrasena a txtConfirmarContrasena
    @FXML private TextField txtNuevoNombre;
    @FXML private TextField txtNuevoUsuario;
    @FXML private PasswordField txtNuevaContrasena;
    @FXML private PasswordField txtContrasenaActual;


    @FXML
    private void guardarCambios() {
        try {
            Usuario user = com.unpsjb.poo.util.Sesion.getUsuarioActual();
            String contrasenaActual = txtContrasenaActual.getText().trim();
            String nuevaContrasena = txtNuevaContrasena.getText().trim();
            String confirmarContrasena = txtConfirmarContrasena.getText().trim();
            
            System.out.println("DEBUG cambio - Usuario actual: " + user.getUsuario());
            System.out.println("DEBUG cambio - Contraseña actual ingresada: " + contrasenaActual);
            System.out.println("DEBUG cambio - Nueva contraseña: " + nuevaContrasena);
            System.out.println("DEBUG cambio - Confirmar contraseña: " + confirmarContrasena);
           
            if (contrasenaActual.isEmpty()) {
                mostrarAlerta("Debes ingresar tu contraseña actual.");
                return;
            }


            if (!user.verificarContraseña(contrasenaActual)) {
                mostrarAlerta("La contraseña actual es incorrecta.");
                return;
            }
            if (!txtNuevoEmail.getText().trim().isEmpty()) {
                user.setEmail(txtNuevoEmail.getText().trim());
            }


            // Actualizar datos (solo si se ingresaron nuevos)
            if (!txtNuevoNombre.getText().trim().isEmpty()) {
                user.setNombre(txtNuevoNombre.getText().trim());
            }
            if (!txtNuevoUsuario.getText().trim().isEmpty()) {
                user.setUsuario(txtNuevoUsuario.getText().trim());
            }
            // Validar nueva contraseña solo si se ingresó una
            if (!nuevaContrasena.isEmpty() || !confirmarContrasena.isEmpty()) {
                if (nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                    mostrarAlerta("Debes completar tanto la nueva contraseña como su confirmación.");
                    return;
                }
                if (!nuevaContrasena.equals(confirmarContrasena)) {
                    mostrarAlerta("Las nuevas contraseñas no coinciden.");
                    return;
                }
                user.setContraseña(nuevaContrasena); // Guardamos en texto plano como está en la BD
            }

            boolean ok = user.actualizar();

            if (ok) {
                mostrarAlerta("Datos actualizados correctamente.");

                // Registrar evento con AuditoriaManager
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
        BaseControlador.cerrarVentanaInterna(txtNuevoNombre);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}