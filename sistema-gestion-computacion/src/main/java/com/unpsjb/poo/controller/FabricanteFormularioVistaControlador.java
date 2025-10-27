package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.productos.Fabricante;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Controlador para el formulario de fabricantes (FabricanteForm.fxml)
 */
public class FabricanteFormularioVistaControlador {

    @FXML private TextField txtNombre;

    private Fabricante fabricanteAEditar = null;
    private ProductoFormularioVistaControlador controladorPadre = null;

    @FXML
    public void initialize() {
        // Inicialización si es necesaria
    }

    /**
     * Establece el fabricante a editar (para modo modificar)
     */
    public void setFabricanteAEditar(Fabricante fabricante) {
        this.fabricanteAEditar = fabricante;
        if (fabricante != null && txtNombre != null) {
            txtNombre.setText(fabricante.getNombre());
        }
    }

    /**
     * Establece el controlador padre para actualizar la lista
     */
    public void setControladorPadre(ProductoFormularioVistaControlador controlador) {
        this.controladorPadre = controlador;
    }

    @FXML
    private void guardarFabricante(ActionEvent event) {
        String nombre = txtNombre != null ? txtNombre.getText() : null;
        
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarAlerta("El nombre es obligatorio.");
            if (txtNombre != null) txtNombre.requestFocus();
            return;
        }

        try {
            boolean exito;
            
            if (fabricanteAEditar != null) {
                // Modo editar
                fabricanteAEditar.setNombre(nombre.trim());
                exito = fabricanteAEditar.guardar();
            } else {
                // Modo crear
                Fabricante nuevo = new Fabricante();
                nuevo.setNombre(nombre.trim());
                exito = nuevo.guardar();
            }

            if (exito) {
                mostrarAlerta("Fabricante guardado correctamente.");
                // Refrescar la lista en el controlador padre si existe
                if (controladorPadre != null) {
                    controladorPadre.cargarFabricantes();
                }
                cerrarVentana(event);
            } else {
                mostrarAlerta("No se pudo guardar el fabricante.");
            }
            
        } catch (Exception e) {
            mostrarAlerta("Error: " + e.getMessage());
        }
    }

    @FXML
    private void cancelarFabricante(ActionEvent event) {
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
