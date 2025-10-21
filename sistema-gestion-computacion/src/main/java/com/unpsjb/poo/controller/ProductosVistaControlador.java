package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.unpsjb.poo.model.Producto;
import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.Sesion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductosVistaControlador {

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, String> colFabricante;
    @FXML private TableColumn<Producto, Integer> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, String> colFabricante;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colCantidad;

    @FXML private TextField txtBuscar;
    // Checkbox para mostrar productos inactivos
    @FXML private CheckBox chBoxInactivos;

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private final ReportesDAO reportesDAO = new ReportesDAO(); //  NUEVO: para registrar auditorías
    private ObservableList<Producto> backingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar las columnas de la tabla
        colCodigo.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getCodigoProducto()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreProducto()));
        colDescripcion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDescripcionProducto()));
        colCategoria.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategoriaProducto()));
        colFabricante.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFabricanteProducto()));
        colPrecio.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPrecioProducto()));
        colCantidad.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getStockProducto()));

        cargarProductos();
    }

    private void cargarProductos() {
        // Obtener solo productos activos
        List<Producto> lista = productoDAO.findAll();
        backingList = FXCollections.observableArrayList(lista);
        tablaProductos.setItems(backingList);
        tablaProductos.refresh();
    }

    // ====== HANDLERS que el FXML referencia con onAction ======

    @FXML
    private void buscarProductos() {
    String q = (txtBuscar != null && txtBuscar.getText() != null) ? 
               txtBuscar.getText().trim().toLowerCase() : "";

    if (q.isEmpty()) {
        tablaProductos.setItems(backingList);
        return;
    }
    
    List<Producto> resultados = backingList.stream()
        .filter(p -> {
            String nombre = p.getNombreProducto() != null ? p.getNombreProducto().toLowerCase() : "";
            String descripcion = p.getDescripcionProducto() != null ? p.getDescripcionProducto().toLowerCase() : "";
            String categoria = p.getCategoriaProducto() != null ? p.getCategoriaProducto().toLowerCase() : "";
            String fabricante = p.getFabricanteProducto() != null ? p.getFabricanteProducto().toLowerCase() : "";
            String codigo = String.valueOf(p.getCodigoProducto());

            return nombre.contains(q)
                || descripcion.contains(q)
                || categoria.contains(q)
                || fabricante.contains(q)
                || codigo.contains(q);
        })
        .collect(Collectors.toList());
    tablaProductos.setItems(FXCollections.observableArrayList(resultados));
}


    @FXML
    private void limpiarBusqueda() {
        cargarProductos();
    }

    // ==========================
    // ➕ AGREGAR PRODUCTO
    // ==========================
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

            //  REGISTRO DE AUDITORÍA
            registrarEvento("AGREGAR PRODUCTO", "Producto", "Se agregó un nuevo producto al sistema");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void modificarProducto() {
    Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
    if (productoSeleccionado == null) {
        mostrarAlerta("Debe seleccionar un producto para modificarlo.");
        return;
    }
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/productoForm.fxml"));
        Parent root = loader.load();
        ProductoFormularioVistaControlador controlador = loader.getController();
        controlador.setProductoAEditar(productoSeleccionado);

        Stage stage = new Stage();
        stage.setTitle("Modificar Producto");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        cargarProductos();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    // Metodo para cambiar el estado activo/inactivo de un producto
    @FXML
    private void cambiarEstadoProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un producto para cambiar su estado.");
            return;
        }
        String nuevoEstado = seleccionado.isActivo() ? "inactivo" : "activo";
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar cambio de estado");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Desea cambiar el estado del producto a " + nuevoEstado + "?");
        if (confirm.showAndWait().get().getButtonData().isDefaultButton()) {
            seleccionado.setActivo(!seleccionado.isActivo());
            boolean ok = productoDAO.update(seleccionado);
            if (ok) {
                mostrarAlerta("El producto cambió al estado: " + (seleccionado.isActivo() ? "Activo" : "Inactivo"));
                cargarProductos();
            } else {
                mostrarAlerta("Error al cambiar el estado del producto.");
            }
        }
    }
    @FXML private void MostrarProductosInactivos() {
        if (chBoxInactivos.isSelected()) {
            // Pedimos al DAO los productos inactivos para no depender del backingList (que contiene solo activos)
            List<Producto> productosInactivos = productoDAO.findAllCompleto();
            tablaProductos.setItems(FXCollections.observableArrayList(productosInactivos));
        } else {
            // Volver a mostrar activos
            tablaProductos.setItems(backingList);
        }
    }
    // Metodo para mostrar alertas
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
