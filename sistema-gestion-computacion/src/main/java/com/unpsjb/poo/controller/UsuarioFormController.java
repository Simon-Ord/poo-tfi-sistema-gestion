package com.unpsjb.poo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;

public class UsuarioFormController {

    @FXML private TextField txtLegajo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private ChoiceBox<String> cbRol;
    @FXML private CheckBox chkActivo;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @FXML
    private void initialize() {
        cbRol.getItems().addAll("ADMIN", "EMPLEADO");
        chkActivo.setSelected(true);
    }

    @FXML
    private void guardarUsuario() {
        try {
            // Validar campos obligatorios
            if (txtLegajo.getText().isEmpty() || txtNombre.getText().isEmpty() ||
                txtUsuario.getText().isEmpty() || txtContrasena.getText().isEmpty() ||
                cbRol.getValue() == null) {

                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            // Crear objeto Usuario
            Usuario nuevo = new Usuario();
            nuevo.setLegajo(txtLegajo.getText());
            nuevo.setNombre(txtNombre.getText());
            nuevo.setUsuario(txtUsuario.getText());
            nuevo.setContraseña(txtContrasena.getText());
            nuevo.setRol(cbRol.getValue());
            nuevo.setEstado(chkActivo.isSelected());

            // Insertar en la base de datos
            boolean ok = usuarioDAO.insertar(nuevo);

            if (ok) {
                mostrarAlerta("✅ Usuario agregado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta("⚠️ Error al guardar el usuario (posible legajo o usuario duplicado).");
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
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setUsuario(Usuario usuario) {
        // Método preparado para futuras ediciones (por ahora vacío)
    }
}
