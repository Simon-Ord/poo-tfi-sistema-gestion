package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.productos.Categoria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Controlador para el formulario de categorías (CategoriaForm.fxml)
 */
public class CategoriaFormularioVistaControlador {

    @FXML private TextField txtNombre;

    private Categoria categoriaAEditar = null;

    @FXML
    public void initialize() {
        // Inicialización si es necesaria
    }

    /**
     * Establece la categoría a editar (para modo modificar)
     */
    public void setCategoriaAEditar(Categoria categoria) {
        this.categoriaAEditar = categoria;
        if (categoria != null && txtNombre != null) {
            txtNombre.setText(categoria.getNombre());
        }
    }

    @FXML
    private void guardarCategoria(ActionEvent event) {
        String nombre = txtNombre != null ? txtNombre.getText() : null;
        
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarAlerta("El nombre es obligatorio.");
            if (txtNombre != null) txtNombre.requestFocus();
            return;
        }

        try {
            boolean exito;
            
            if (categoriaAEditar != null) {
                // Modo editar
                categoriaAEditar.setNombre(nombre.trim());
                exito = categoriaAEditar.actualizar();
            } else {
                // Modo crear
                Categoria nueva = new Categoria(nombre.trim());
                exito = nueva.guardar();
            }

            if (exito) {
                mostrarAlerta("Categoría guardada correctamente.");
                cerrarVentana(event);
            } else {
                mostrarAlerta("No se pudo guardar la categoría.");
            }
            
        } catch (Exception e) {
            mostrarAlerta("Error: " + e.getMessage());
        }
    }

    @FXML
    private void cancelarCategoria(ActionEvent event) {
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