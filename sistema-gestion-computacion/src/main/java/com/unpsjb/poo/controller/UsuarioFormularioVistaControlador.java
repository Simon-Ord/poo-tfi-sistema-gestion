package com.unpsjb.poo.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;

public class UsuarioFormularioVistaControlador {

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContraseña;
    @FXML private ChoiceBox<String> cbRol;
    @FXML private CheckBox chkActivo;

    @FXML
    private void initialize() {
        cbRol.getItems().addAll("ADMINISTRADOR", "EMPLEADO");
        chkActivo.setSelected(true);
    }

    @FXML
    private void guardarUsuario() {
        try {
            if (txtDni.getText().isEmpty() || txtNombre.getText().isEmpty()
                    || txtUsuario.getText().isEmpty() || txtContraseña.getText().isEmpty()
                    || cbRol.getValue() == null) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            Usuario nuevo = new Usuario();
            nuevo.setDni(txtDni.getText().trim());
            nuevo.setNombre(txtNombre.getText().trim());
            nuevo.setUsuario(txtUsuario.getText().trim());
            nuevo.setContraseña(txtContraseña.getText().trim());
            nuevo.setRol(cbRol.getValue());
            nuevo.setEstado(chkActivo.isSelected());

            boolean ok = nuevo.guardar();

            if (ok) {
                mostrarAlerta("Usuario agregado correctamente.");

                //  Ahora el registro de auditoría se hace con una sola línea:
               AuditoriaUtil.registrarAccion(
                    "CREAR USUARIO",
                    "usuario",
                    "Se creo al usuario: " + nuevo.getNombre());

                cerrarVentana();
            } else {
                mostrarAlerta("Error al guardar el usuario.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        BaseControlador.cerrarVentanaInterna(txtNombre);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}