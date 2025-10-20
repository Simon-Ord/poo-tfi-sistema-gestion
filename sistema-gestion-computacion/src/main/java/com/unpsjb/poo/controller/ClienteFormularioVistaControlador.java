package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.persistence.dao.impl.ClienteDAOImpl;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClienteFormularioVistaControlador {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCuit;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDireccion;
    @FXML private ChoiceBox<String> cbTipoCliente;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private final ClienteDAOImpl clienteDAO = new ClienteDAOImpl();

    @FXML
    private void initialize() {
        cbTipoCliente.getItems().addAll(
            "Consumidor Final",
            "Monotributista",
            "Responsable Inscripto"
        );
        cbTipoCliente.setValue("Consumidor Final");
    }

    @FXML
    private void guardarCliente() {
        try {
            Cliente nuevo = new Cliente();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setCuit(txtCuit.getText());
            nuevo.setTelefono(txtTelefono.getText());
            nuevo.setDireccion(txtDireccion.getText());
            nuevo.setEmail(txtEmail.getText());
            nuevo.setTipo(cbTipoCliente.getValue());

            boolean exito = clienteDAO.insertar(nuevo);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Agregar Cliente");
            alert.setHeaderText(null);

            if (exito) {
                alert.setContentText("✅ Cliente guardado correctamente en la base de datos.");
            } else {
                alert.setContentText("⚠️ No se pudo guardar el cliente. Revisá la conexión o los datos.");
            }

            alert.showAndWait();
            cerrarVentana();

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en los datos: " + e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gestión de Clientes");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
