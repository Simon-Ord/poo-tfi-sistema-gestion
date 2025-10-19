package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.unpsjb.poo.model.Producto;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductosVistaControlador {

    // ====== VISTA (IDs deben coincidir con el FXML) ======
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer>    colCodigo;
    @FXML private TableColumn<Producto, String>     colNombre;
    @FXML private TableColumn<Producto, String>     colDescripcion;
    @FXML private TableColumn<Producto, String>     colCategoria;
    @FXML private TableColumn<Producto, String>     colFabricante;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer>    colCantidad;
    @FXML private TableColumn<Producto, Boolean>    colActivo;

    // barra de búsqueda del FXML nuevo
    @FXML private TextField txtBuscar;

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private ObservableList<Producto> backingList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colCodigo.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getCodigoProducto()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreProducto()));
        colDescripcion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDescripcionProducto()));
        colCategoria.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategoriaProducto()));
        colFabricante.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFabricanteProducto()));
        colPrecio.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPrecioProducto()));
        colCantidad.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getStockProducto()));
        colActivo.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().isActivo()));

        cargarProductos();
    }

    private void cargarProductos() {
    List<Producto> lista = productoDAO.findAll(); // filtra estado=TRUE si corresponde
    ObservableList<Producto> obsList = FXCollections.observableArrayList(lista);
    tablaProductos.setItems(obsList);
    }

    // ====== HANDLERS que el FXML referencia con onAction ======

    @FXML private void buscarProductos() {
        String q = (txtBuscar == null || txtBuscar.getText() == null) ? "" : txtBuscar.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            tablaProductos.setItems(backingList);
        } else {
            List<Producto> filtrados = backingList.stream()
                .filter(p ->
                        (p.getNombreProducto() != null && p.getNombreProducto().toLowerCase().contains(q)) ||
                        String.valueOf(p.getCodigoProducto()).contains(q)
                )
                .collect(Collectors.toList());
            tablaProductos.setItems(FXCollections.observableArrayList(filtrados));
        }
        tablaProductos.refresh();
    }

    @FXML
    private void limpiarBusqueda() {
        if (txtBuscar != null) txtBuscar.clear();
    tablaProductos.setItems(backingList);
        tablaProductos.refresh();
    }

    @FXML
    private void agregarProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/productoForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Nuevo Producto");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            cargarProductos();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }
    @FXML void eliminarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un producto para eliminar.");
            return;
        }
        seleccionado.setEstado(false);
        boolean ok = productoDAO.update(seleccionado);

        if (ok) {
            mostrarAlerta("producto eliminado correctamente.");
            cargarProductos();
        } else {
            mostrarAlerta("Error al eliminar el producto.");
        }
    }

    @FXML
    private void cambiarEstadoProducto() {

    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}



