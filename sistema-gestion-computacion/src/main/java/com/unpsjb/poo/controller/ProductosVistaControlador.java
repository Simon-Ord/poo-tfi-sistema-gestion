package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.Producto;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProductosVistaControlador {

    // === VISTA ===
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colCantidad;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, String> colFabricante;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtFabricante;

    @FXML private ComboBox<String> cbCategoria;
    @FXML private ComboBox<String> cbFabricante;

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();

    // === MÉTODOS DE INICIALIZACIÓN ===
    @FXML
    public void initialize() {
        configurarTabla();
        cargarProductos();

        cbCategoria.getItems().addAll("Periféricos", "Monitores", "Almacenamiento", "Componentes", "Otros");
        cbFabricante.getItems().addAll("Logitech", "Redragon", "Kingston", "Samsung", "Otros");
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getCodigoProducto()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreProducto()));
        colPrecio.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPrecioProducto()));
        colCantidad.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getStockProducto()));
        colDescripcion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDescripcionProducto()));
        colCategoria.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategoriaProducto()));
        colFabricante.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFabricanteProducto()));
    }

    private void cargarProductos() {
        List<Producto> lista = productoDAO.findAll();
        ObservableList<Producto> obsList = FXCollections.observableArrayList(lista);
        tablaProductos.setItems(obsList);
    }

    // === BOTONES ===
    @FXML private void btnRegistrarAction() {
        try {
            if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty() ||
                txtCantidad.getText().isEmpty() || txtCodigo.getText().isEmpty()) {
                mostrarAlerta("⚠️ Todos los campos obligatorios deben completarse (Código, Nombre, Precio, Cantidad).");
                return;
            }

            Producto p = new Producto();
            p.setCodigoProducto(Integer.parseInt(txtCodigo.getText()));
            p.setNombreProducto(txtNombre.getText());
            p.setDescripcionProducto(txtDescripcion.getText());
            p.setStockProducto(Integer.parseInt(txtCantidad.getText()));
            p.setPrecioProducto(new BigDecimal(txtPrecio.getText()));
            p.setCategoriaProducto(cbCategoria.getValue());
            p.setFabricanteProducto(txtFabricante.getText());

            productoDAO.create(p);
            mostrarAlerta("✅ Producto registrado correctamente.");
            limpiarCampos();
            cargarProductos();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("❌ Error al registrar producto: " + e.getMessage());
        }
    }

    @FXML private void btnModificarAction() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un producto para modificar.");
            return;
        }
        try {
            seleccionado.setCodigoProducto(Integer.parseInt(txtCodigo.getText()));
            seleccionado.setNombreProducto(txtNombre.getText());
            seleccionado.setDescripcionProducto(txtDescripcion.getText());
            seleccionado.setStockProducto(Integer.parseInt(txtCantidad.getText()));
            seleccionado.setPrecioProducto(new BigDecimal(txtPrecio.getText()));
            seleccionado.setCategoriaProducto(cbCategoria.getValue());
            seleccionado.setFabricanteProducto(txtFabricante.getText());

            productoDAO.update(seleccionado);
            mostrarAlerta("✅ Producto actualizado correctamente.");
            limpiarCampos();
            cargarProductos();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("❌ Error al modificar producto: " + e.getMessage());
        }
    }

    @FXML private void btnEliminarAction() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un producto para eliminar.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar el producto seleccionado?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            productoDAO.delete(seleccionado.getIdProducto());
            mostrarAlerta("🗑️ Producto eliminado correctamente.");
            cargarProductos();
        }
    }

    @FXML private void btnCancelarAction() {
        limpiarCampos();
        tablaProductos.getSelectionModel().clearSelection();
    }
    // INCOMPLETOS
    @FXML private void AgregarCategoriaAction(){}
    @FXML private void AgregarFabricanteAction(){}

    // === UTILIDADES ===

    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtCantidad.clear();
        txtFabricante.clear();
        cbCategoria.setValue(null);
        cbFabricante.setValue(null);
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
