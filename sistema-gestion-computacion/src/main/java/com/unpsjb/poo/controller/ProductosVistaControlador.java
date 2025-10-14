package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import com.unpsjb.poo.model.Producto;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOMock; // usamos el mock

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controlador para la vista de Productos.
 * Esta versión usa un DAO simulado (sin base de datos) para pruebas visuales.
 */
public class ProductosVistaControlador implements Initializable {

    // 🔹 Campos de texto del formulario
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtFabricante;

    // 🔹 Botones
    @FXML private Button btnRegistrar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnCancelar;
    @FXML private Button btnAgregarCategoria;
    @FXML private Button btnAgregarFabricante;

    // 🔹 DAO (modo simulación, sin BD)
    private final ProductoDAOMock productoDAO = new ProductoDAOMock();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización vacía por ahora
    }

    // ==================================================
    // 👉 REGISTRAR PRODUCTO
    // ==================================================
    @FXML
    private void btnRegistrarAction(ActionEvent event) {
        try {
            // Leer campos del formulario
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            int stock = Integer.parseInt(txtCantidad.getText().trim());
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            String categoria = "General"; // temporal
            String fabricante = txtFabricante.getText().trim();
            int codigo = Integer.parseInt(txtCodigo.getText().trim());

            // Crear el objeto producto
            Producto producto = new Producto(0, nombre, descripcion, stock, precio, categoria, fabricante, codigo);

            // Guardar en el mock (lista en memoria)
            productoDAO.create(producto);

            // Mostrar éxito
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto registrado correctamente (modo simulado).");

            // Limpiar campos
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de formato",
                    "Verifica los campos numéricos (código, precio o cantidad).");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "Ocurrió un error al registrar el producto:\n" + e.getMessage());
        }
    }

    // ==================================================
    // 👉 MÉTODOS AUXILIARES
    // ==================================================
    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtCantidad.clear();
        txtDescripcion.clear();
        txtFabricante.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // --------------------------------------------------
    // Los otros botones quedan vacíos por ahora
    // --------------------------------------------------
    @FXML private void btnModificarAction(ActionEvent event) {}
    @FXML private void btnEliminarAction(ActionEvent event) {}
    @FXML private void btnCancelarAction(ActionEvent event) {}
    @FXML private void AgregarCategoriaAction(ActionEvent event) {}
    @FXML private void AgregarFabricanteAction(ActionEvent event) {}
}
