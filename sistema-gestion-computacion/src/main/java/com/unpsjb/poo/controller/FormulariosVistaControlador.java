package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.productos.Categoria;
import com.unpsjb.poo.persistence.dao.impl.CategoriaDAOImpl;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class FormulariosVistaControlador {

	// ======================
    // CategoriaForm.fxml ===
    // ======================
	@javafx.fxml.FXML private javafx.scene.control.TextField txtNombre;
	@javafx.fxml.FXML private javafx.scene.control.Button btnGuardar;
	@javafx.fxml.FXML private javafx.scene.control.Button btnCancelar;

	private final CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();

	@javafx.fxml.FXML
	private void guardarCategoria(javafx.event.ActionEvent event) {
		String nombre = txtNombre != null ? txtNombre.getText() : null;
		if (nombre == null || nombre.trim().isEmpty()) {
			mostrarAlerta(AlertType.WARNING, "Validación", "El nombre es obligatorio.");
			if (txtNombre != null) txtNombre.requestFocus();
			return;
		}

		// Validar duplicado
		try {
			java.util.Optional<Categoria> existente = categoriaDAO.findByNombre(nombre.trim());
			if (existente.isPresent()) {
				mostrarAlerta(AlertType.WARNING, "Duplicado", "Ya existe una categoría con ese nombre.");
				return;
			}

			Categoria categoria = new Categoria();
			categoria.setNombre(nombre.trim());

			boolean creado = categoriaDAO.create(categoria);
			if (creado) {
				mostrarAlerta(AlertType.INFORMATION, "Éxito", "Categoría creada correctamente.");
				cerrarVentanaDesde(event);
			} else {
				mostrarAlerta(AlertType.ERROR, "Error", "No se pudo crear la categoría.");
			}
		} catch (Exception e) {
			mostrarAlerta(AlertType.ERROR, "Error", "Ocurrió un error: " + e.getMessage());
		}
	}

	@javafx.fxml.FXML
	private void cancelarCategoria(ActionEvent event) {
		cerrarVentanaDesde(event);
	}

	// ===== Utilidades =====
	private void cerrarVentanaDesde(ActionEvent event) {
		if (event != null && event.getSource() != null) {
			Node node = (Node) event.getSource();
			BaseControlador.cerrarVentanaInterna(node);
		}
	}

	private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}




}
