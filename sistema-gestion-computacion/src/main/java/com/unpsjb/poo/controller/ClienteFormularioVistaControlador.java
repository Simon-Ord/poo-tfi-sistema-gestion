package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.util.Sesion;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaClienteUtil;
import com.unpsjb.poo.util.copias.CopiarClienteUtil;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ClienteFormularioVistaControlador {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCuit;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDireccion;
    @FXML private ChoiceBox<String> cbTipoCliente;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Cliente clienteEditable;

    @FXML
    public void initialize() {
        cbTipoCliente.getItems().addAll("Consumidor Final", "Responsable Inscripto", "Monotributista", "Exento");
    }

    @FXML
    private void guardarCliente() {
        try {
            if (txtNombre.getText().trim().isEmpty()) {
                mostrarAlerta("Debe ingresar un nombre.");
                return;
            }

            if (clienteEditable == null) {
                // Alta nueva
                Cliente nuevo = new Cliente();
                nuevo.setNombre(txtNombre.getText().trim());
                nuevo.setCuit(txtCuit.getText().trim());
                nuevo.setTelefono(txtTelefono.getText().trim());
                nuevo.setDireccion(txtDireccion.getText().trim());
                nuevo.setEmail(txtEmail.getText().trim());
                nuevo.setTipo(cbTipoCliente.getValue());
                nuevo.setActivo(true);

                if (nuevo.guardar()) {
                    new AuditoriaClienteUtil().registrarCreacion(nuevo);
                    mostrarAlerta(" Cliente agregado correctamente.");
                } else {
                    mostrarAlerta("Error al guardar cliente.");
                }
            } else {
                // Modificación
                Cliente original = CopiarClienteUtil.copiarCliente(clienteEditable);
                clienteEditable.setNombre(txtNombre.getText().trim());
                clienteEditable.setCuit(txtCuit.getText().trim());
                clienteEditable.setTelefono(txtTelefono.getText().trim());
                clienteEditable.setDireccion(txtDireccion.getText().trim());
                clienteEditable.setEmail(txtEmail.getText().trim());
                clienteEditable.setTipo(cbTipoCliente.getValue());

                if (clienteEditable.guardar()) {
                    // auditoria usando el accion de cliente (polimorfismo)
                    new AuditoriaClienteUtil().registrarAccionEspecifica(original, clienteEditable);
                    mostrarAlerta(" Cliente actualizado correctamente.");
                } else {
                    mostrarAlerta(" Error al actualizar cliente.");
                }
            }

            cerrarVentana();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        BaseControlador.cerrarVentanaInterna(btnCancelar);
    }

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

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gestión de Clientes");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
