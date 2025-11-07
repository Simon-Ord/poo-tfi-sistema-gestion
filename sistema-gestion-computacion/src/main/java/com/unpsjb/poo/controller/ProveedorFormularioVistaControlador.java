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

    private Proveedor proveedorAEditar;

    @FXML
    private void guardarProveedor() {
        try {
            if (txtNombre.getText().trim().isEmpty()) {
                mostrarAlerta("El nombre es obligatorio.");
                return;
            }

            Proveedor proveedor = proveedorAEditar != null ? proveedorAEditar : new Proveedor();
            proveedor.setNombre(txtNombre.getText().trim());
            proveedor.setTelefono(txtTelefono.getText().trim());
            proveedor.setEmail(txtEmail.getText().trim());
            proveedor.setDireccion(txtDireccion.getText().trim());

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
            txtNombre.setText(proveedor.getNombre());
            txtTelefono.setText(proveedor.getTelefono());
            txtEmail.setText(proveedor.getEmail());
            txtDireccion.setText(proveedor.getDireccion());
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