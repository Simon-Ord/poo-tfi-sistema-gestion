package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.productos.ProveedorDigital;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Controlador para el formulario de proveedores digitales (ProveedorDigitalForm.fxml)
 */
public class ProveedorDigitalFormularioVistaControlador {

    @FXML private TextField txtNombre;

    private ProveedorDigital proveedorAEditar = null;
    private ProductoFormularioVistaControlador controladorPadre = null;

    @FXML
    public void initialize() {
        // Inicialización si es necesaria
    }

    /**
     * Establece el proveedor digital a editar (para modo modificar)
     */
    public void setProveedorDigitalAEditar(ProveedorDigital proveedor) {
        this.proveedorAEditar = proveedor;
        if (proveedor != null && txtNombre != null) {
            txtNombre.setText(proveedor.getNombre());
        }
    }

    /**
     * Establece el controlador padre para actualizar la lista
     */
    public void setControladorPadre(ProductoFormularioVistaControlador controlador) {
        this.controladorPadre = controlador;
    }

    @FXML
    private void guardarProveedorDigital(ActionEvent event) {
        String nombre = txtNombre != null ? txtNombre.getText() : null;
        
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarAlerta("El nombre es obligatorio.");
            if (txtNombre != null) txtNombre.requestFocus();
            return;
        }

        try {
            boolean exito;
            
            if (proveedorAEditar != null) {
                // Modo editar
                proveedorAEditar.setNombre(nombre.trim());
                exito = proveedorAEditar.guardar();
            } else {
                // Modo crear
                ProveedorDigital nuevo = new ProveedorDigital();
                nuevo.setNombre(nombre.trim());
                exito = nuevo.guardar();
            }

            if (exito) {
                mostrarAlerta("Proveedor digital guardado correctamente.");
                // Refrescar la lista en el controlador padre si existe
                if (controladorPadre != null) {
                    controladorPadre.cargarProveedoresDigitales();
                }
                cerrarVentana(event);
            } else {
                mostrarAlerta("No se pudo guardar el proveedor digital.");
            }
            
        } catch (Exception e) {
            mostrarAlerta("Error: " + e.getMessage());
        }
    }

    @FXML
    private void cancelarProveedorDigital(ActionEvent event) {
        cerrarVentana(event);
    }

    private void cerrarVentana(ActionEvent event) {
        if (event != null && event.getSource() != null) {
            Node node = (Node) event.getSource();
            BaseControlador.cerrarVentanaInterna(node);
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
