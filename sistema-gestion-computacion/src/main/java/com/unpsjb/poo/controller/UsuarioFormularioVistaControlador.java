package com.unpsjb.poo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;

public class UsuarioFormularioVistaControlador {

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContraseña;
    @FXML private ChoiceBox<String> cbRol;
    @FXML private CheckBox chkActivo;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @FXML
    private void initialize() {
        cbRol.getItems().addAll("ADMINISTRADOR", "EMPLEADO");
        chkActivo.setSelected(true); // Por defecto, el nuevo usuario está activo
    }

    @FXML
    private void guardarUsuario() {
        try {
            // Validación de campos vacíos
            if (txtDni.getText().isEmpty() || txtNombre.getText().isEmpty()
                    || txtUsuario.getText().isEmpty() || txtContraseña.getText().isEmpty()
                    || cbRol.getValue() == null) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            // Crear el objeto usuario
            Usuario nuevo = new Usuario();
            nuevo.setDni(txtDni.getText().trim());
            nuevo.setNombre(txtNombre.getText().trim());
            nuevo.setUsuario(txtUsuario.getText().trim());
            nuevo.setContraseña(txtContraseña.getText().trim());
            nuevo.setRol(cbRol.getValue());
            nuevo.setEstado(chkActivo.isSelected());

            // Intentar guardar en la base de datos
            boolean ok = usuarioDAO.insertar(nuevo);

            if (ok) {
                mostrarAlerta(" Usuario agregado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta(" Error al guardar el usuario. Revisa la consola para más detalles.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(" Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
