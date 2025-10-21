package com.unpsjb.poo.controller;

import java.io.IOException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductosVistaControlador {

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, String> colFabricante;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colCantidad;
    @FXML private TableColumn<Producto, Boolean> colActivo;

    @FXML private TextField txtBuscar;

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private final ReportesDAO reportesDAO = new ReportesDAO(); // 🟢 NUEVO: para registrar auditorías
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
        List<Producto> lista = productoDAO.findAll();
        backingList = FXCollections.observableArrayList(lista);
        tablaProductos.setItems(backingList);
    }

    // ==========================
    // 🔍 BÚSQUEDA
    // ==========================
    @FXML
    private void buscarProductos() {
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

            // 🟢 REGISTRO DE AUDITORÍA
            registrarEvento("AGREGAR PRODUCTO", "Producto", "Se agregó un nuevo producto al sistema");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }

    // ==========================
    // ❌ ELIMINAR PRODUCTO (desactivar)
    // ==========================
    @FXML
    private void eliminarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un producto para eliminar.");
            return;
        }

        seleccionado.setEstado(false);
        boolean ok = productoDAO.update(seleccionado);

        if (ok) {
            mostrarAlerta("Producto eliminado correctamente.");
            cargarProductos();

            // 🟢 REGISTRO DE AUDITORÍA
            registrarEvento("ELIMINAR PRODUCTO", "Producto",
                "El usuario eliminó el producto: " + seleccionado.getNombreProducto());

        } else {
            mostrarAlerta("Error al eliminar el producto.");
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

    // ==========================
    // 🔄 CAMBIAR ESTADO (activar/desactivar)
    // ==========================
    @FXML
    private void cambiarEstadoProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un producto para cambiar su estado.");
            return;
<<<<<<< HEAD
        }

        boolean nuevoEstado = !seleccionado.isActivo();
        seleccionado.setActivo(nuevoEstado);

        boolean ok = productoDAO.update(seleccionado);

        if (ok) {
            mostrarAlerta(nuevoEstado ? "Producto activado correctamente." : "Producto desactivado correctamente.");
            cargarProductos();

            // 🟢 REGISTRO DE AUDITORÍA
            registrarEvento("CAMBIAR ESTADO PRODUCTO", "Producto",
                "El usuario cambió el estado del producto: " + seleccionado.getNombreProducto() +
                " a " + (nuevoEstado ? "Activo" : "Inactivo"));
=======
            }
        seleccionado.setActivo(!seleccionado.isActivo());
        boolean ok = productoDAO.update(seleccionado);
        if (ok) {
            mostrarAlerta("El producto cambio al estado: " + (seleccionado.isActivo() ? "Activo" : "Inactivo"));
            cargarProductos();
>>>>>>> 96994ef3a83f7c6aad22d560e4f2fadd36b1ae55
        } else {
            mostrarAlerta("Error al cambiar el estado del producto.");
        }
    }
<<<<<<< HEAD

    // ==========================
    // 🧾 REGISTRAR EVENTOS (Método nuevo)
    // ==========================
    private void registrarEvento(String accion, String entidad, String descripcion) {
        try {
            String usuarioActual = (Sesion.getUsuarioActual() != null)
                    ? Sesion.getUsuarioActual().getNombre()
                    : "Desconocido";

            EventoAuditoria evento = new EventoAuditoria();
            evento.setUsuario(usuarioActual);
            evento.setAccion(accion);
            evento.setEntidad(entidad);
            evento.setDetalles(descripcion);

            reportesDAO.registrarEvento(evento);
        } catch (Exception e) {
            System.err.println("⚠️ Error al registrar evento: " + e.getMessage());
        }
    }

    // ==========================
    // ⚠️ UTILIDADES
    // ==========================
=======
>>>>>>> 96994ef3a83f7c6aad22d560e4f2fadd36b1ae55
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
