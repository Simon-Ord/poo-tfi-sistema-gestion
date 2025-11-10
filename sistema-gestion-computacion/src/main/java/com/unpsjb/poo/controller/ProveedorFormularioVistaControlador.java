package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.Proveedor;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ProveedorFormularioVistaControlador {

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCuit;
    @FXML private javafx.scene.control.ChoiceBox<String> cbTipo;

    private Proveedor proveedorAEditar;

    @FXML
    private void guardarProveedor() {
        try {
            if (safeTrim(txtNombre).isEmpty()) {
                mostrarAlerta("El nombre es obligatorio.");
                return;
            }

            Proveedor proveedor = proveedorAEditar != null ? proveedorAEditar : new Proveedor();
            proveedor.setNombre(safeTrim(txtNombre));
            proveedor.setTelefono(safeTrim(txtTelefono));
            proveedor.setEmail(safeTrim(txtEmail));
            proveedor.setDireccion(safeTrim(txtDireccion));
            String cuitValor = safeTrim(txtCuit);
            proveedor.setCuit(cuitValor.isEmpty() ? null : cuitValor);
            proveedor.setTipo(cbTipo.getValue() == null ? "FISICO" : cbTipo.getValue());

            boolean ok = proveedorAEditar != null ? proveedor.actualizar() : proveedor.guardar();

            if (ok) {
                String accion = proveedorAEditar != null ? "MODIFICAR" : "CREAR";
                AuditoriaUtil.registrarAccion(
                    accion + " PROVEEDOR",
                    "proveedor",
                    accion.toLowerCase() + " al proveedor: " + proveedor.getNombre()
                );
                
                mostrarAlerta("Proveedor " + (proveedorAEditar != null ? "modificado" : "agregado") + " correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta("Error al " + (proveedorAEditar != null ? "modificar" : "guardar") + " el proveedor.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado: " + e.getMessage());
        }
    }

    public void setProveedorAEditar(Proveedor proveedor) {
        this.proveedorAEditar = proveedor;
        if (proveedor != null) {
            txtNombre.setText(proveedor.getNombre() == null ? "" : proveedor.getNombre());
            txtTelefono.setText(proveedor.getTelefono() == null ? "" : proveedor.getTelefono());
            txtEmail.setText(proveedor.getEmail() == null ? "" : proveedor.getEmail());
            txtDireccion.setText(proveedor.getDireccion() == null ? "" : proveedor.getDireccion());
            if (txtCuit != null) txtCuit.setText(proveedor.getCuit() == null ? "" : proveedor.getCuit());
            if (cbTipo != null) cbTipo.setValue(proveedor.getTipo() == null ? "FISICO" : proveedor.getTipo());
        }
    }

    @FXML
    private void initialize() {
        if (cbTipo != null) {
            cbTipo.getItems().addAll("DIGITAL", "FISICO");
            cbTipo.setValue("FISICO");
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

    // Utilidad para evitar NullPointerException cuando el TextField tiene texto null.
    private String safeTrim(TextField tf) {
        if (tf == null) return "";
        String v = tf.getText();
        return v == null ? "" : v.trim();
    }
}