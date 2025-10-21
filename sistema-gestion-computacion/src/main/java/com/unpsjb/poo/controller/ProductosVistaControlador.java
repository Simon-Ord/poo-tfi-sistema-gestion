package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;
import com.unpsjb.poo.util.AuditoriaUtil;
import com.unpsjb.poo.util.CopiarProductoUtil;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductosVistaControlador {

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colCantidad;

    @FXML private TextField txtBuscar;
    @FXML private CheckBox chBoxInactivos;

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private final ReportesDAO reportesDAO = new ReportesDAO();
    private ObservableList<Producto> backingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar columnas
        colCodigo.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getCodigoProducto()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreProducto()));
        colDescripcion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDescripcionProducto()));
        colCategoria.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getCategoria() != null ? c.getValue().getCategoria().getNombre() : "Sin Categoría"));
        colPrecio.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPrecioProducto()));
        colCantidad.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getStockProducto()));

        cargarProductos();
    }

    /** Cargar productos activos */
    private void cargarProductos() {
        List<Producto> lista = productoDAO.findAll();
        backingList = FXCollections.observableArrayList(lista);
        tablaProductos.setItems(backingList);
        tablaProductos.refresh();
    }

    /** Buscar productos */
    @FXML
    private void buscarProductos() {
        String q = (txtBuscar != null && txtBuscar.getText() != null)
                ? txtBuscar.getText().trim().toLowerCase()
                : "";

        if (q.isEmpty()) {
            tablaProductos.setItems(backingList);
            return;
        }

        List<Producto> resultados = backingList.stream()
                .filter(p -> {
                    String nombre = p.getNombreProducto() != null ? p.getNombreProducto().toLowerCase() : "";
                    String descripcion = p.getDescripcionProducto() != null ? p.getDescripcionProducto().toLowerCase() : "";
                    String categoria = p.getCategoria() != null ? p.getCategoria().getNombre().toLowerCase() : "";
                    String codigo = String.valueOf(p.getCodigoProducto());

                    return nombre.contains(q) || descripcion.contains(q) ||
                           categoria.contains(q) || codigo.contains(q);
                })
                .collect(Collectors.toList());
        tablaProductos.setItems(FXCollections.observableArrayList(resultados));
    }

    /** Limpiar búsqueda */
    @FXML
    private void limpiarBusqueda() {
        cargarProductos();
    }

    /** Agregar producto */
    @FXML
    private void agregarProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/productoForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Nuevo Producto");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarProductos();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }

    /** Modificar producto */
    @FXML
    private void modificarProducto() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un producto para modificarlo.");
            return;
        }

        try {
            // Guardamos una copia del producto original para comparar cambios después
            Producto productoOriginal = CopiarProductoUtil.copiarProducto(productoSeleccionado);

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

            // 🔹 Auditoría: comparar cambios después de cerrar el formulario
            Producto productoModificado = productoDAO.read(productoSeleccionado.getIdProducto()).orElse(null);
            if (productoModificado != null) {
                String usuario = (Sesion.getUsuarioActual() != null)
                        ? Sesion.getUsuarioActual().getNombre()
                        : "Desconocido";
                AuditoriaUtil.registrarCambioProducto(productoOriginal, productoModificado);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de modificación: " + e.getMessage());
        }
    }

    /** Cambiar estado activo/inactivo */
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
            boolean estadoAnterior = seleccionado.isActivo();
            seleccionado.setActivo(!estadoAnterior);
            boolean ok = productoDAO.update(seleccionado);
            if (ok) {
                String usuario = (Sesion.getUsuarioActual() != null)
                        ? Sesion.getUsuarioActual().getNombre()
                        : "Desconocido";
              AuditoriaUtil.registrarCambioEstadoProducto(seleccionado, !estadoAnterior);

                mostrarAlerta("El producto cambió al estado: " + (!estadoAnterior ? "Activo" : "Inactivo"));
                cargarProductos();
            } else {
                mostrarAlerta("Error al cambiar el estado del producto.");
            }
        }
    }

    /** Mostrar productos inactivos */
    @FXML
    private void MostrarProductosInactivos() {
        if (chBoxInactivos.isSelected()) {
            List<Producto> productosInactivos = productoDAO.findAllCompleto();
            tablaProductos.setItems(FXCollections.observableArrayList(productosInactivos));
        } else {
            tablaProductos.setItems(backingList);
        }
    }

    @FXML
    private void detallesProducto() {
        mostrarAlerta("Función de detalles aún no implementada.");
    }

    /** Mostrar alertas */
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
