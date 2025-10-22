package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.persistence.dao.impl.ClienteDAOImpl;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

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
    private Cliente clienteEditable = null; // ⚡ si es null = modo agregar, si no = editar

    @FXML
    public void initialize() {
        cbTipoCliente.getItems().addAll("Consumidor Final", "Responsable Inscripto", "Monotributista");
    }

    @FXML
    private void guardarCliente() {
        try {
            if (clienteEditable == null) {
                // === MODO AGREGAR ===
                Cliente nuevo = new Cliente();
                nuevo.setNombre(txtNombre.getText());
                nuevo.setCuit(txtCuit.getText());
                nuevo.setTelefono(txtTelefono.getText());
                nuevo.setDireccion(txtDireccion.getText());
                nuevo.setEmail(txtEmail.getText());
                nuevo.setTipo(cbTipoCliente.getValue());

                if (clienteDAO.insertar(nuevo)) {
                    mostrarAlerta("Cliente guardado correctamente.");
                } else {
                    mostrarAlerta("No se pudo guardar el cliente.");
                }
            } else {
                // === MODO EDITAR ===
                clienteEditable.setNombre(txtNombre.getText());
                clienteEditable.setCuit(txtCuit.getText());
                clienteEditable.setTelefono(txtTelefono.getText());
                clienteEditable.setDireccion(txtDireccion.getText());
                clienteEditable.setEmail(txtEmail.getText());
                clienteEditable.setTipo(cbTipoCliente.getValue());

                if (clienteDAO.modificar(clienteEditable)) {
                    mostrarAlerta("Cliente actualizado correctamente.");
                } else {
                    mostrarAlerta("No se pudo actualizar el cliente.");
                }
            }

            cerrarVentana();

        } catch (Exception e) {
            mostrarAlerta("Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        BaseControlador.cerrarVentanaInterna(btnCancelar);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gestión de Clientes");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // 🔹 Método para precargar datos cuando se edita un cliente
    public void setClienteEditable(Cliente cliente) {
        this.clienteEditable = cliente;

        txtNombre.setText(cliente.getNombre());
        txtCuit.setText(cliente.getCuit());
        txtTelefono.setText(cliente.getTelefono());
        txtDireccion.setText(cliente.getDireccion());
        txtEmail.setText(cliente.getEmail());
        cbTipoCliente.setValue(cliente.getTipo());

        btnGuardar.setText("Guardar Cambios");
    }
}
