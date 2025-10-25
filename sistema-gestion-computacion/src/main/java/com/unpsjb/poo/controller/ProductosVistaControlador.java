package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.util.AuditoriaUtil;
import com.unpsjb.poo.util.Sesion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


// Controlador para la vista de productos.

public class ProductosVistaControlador extends BaseControlador {

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, String> colFabricante;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colCantidad;

    @FXML private TextField txtBuscar;
    @FXML private CheckBox chBoxInactivos;

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

    /** Carga todos los productos activos en la tabla */
    public void cargarProductos() {
        // Obtener productos a través del modelo, NO del DAO
        List<Producto> lista = Producto.obtenerTodos();
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
        crearFormulario("/view/productoForm.fxml", "Agregar Nuevo Producto");
        cargarProductos(); // Recargar datos después de cerrar la ventana
    }

    /** Modificar producto */
    @FXML
    private void modificarProducto() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un producto para modificarlo.");
            return;
        }
        
        // Abrir ventana y configurar el controlador
        VentanaVistaControlador.ResultadoVentana resultado = 
            crearFormulario("/view/productoForm.fxml", "Modificar Producto");
        
        if (resultado != null) {
            ProductoFormularioVistaControlador controlador = 
                (ProductoFormularioVistaControlador) resultado.getControlador(); 
            if (controlador != null) {
                controlador.setProductoAEditar(productoSeleccionado);
                controlador.setControladorPadre(this); // ← ¡Pasar referencia de este controlador!
            }
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
        // Guardar el estado anterior para auditoría y mensaje
        boolean estadoAnterior = seleccionado.isActivo();
        // Cambiar el estado y persistir
        seleccionado.cambiarEstado(); 
        boolean ok = seleccionado.actualizar();
        if (ok) {
            // Registrar auditoría si hay usuario en sesión
            if (Sesion.getUsuarioActual() != null) {
                AuditoriaUtil.registrarCambioEstadoProducto(seleccionado, !estadoAnterior);
            }            
            String estadoActual = seleccionado.isActivo() ? "Activo" : "Inactivo";
            mostrarAlerta("El producto cambió al estado: " + estadoActual);
            cargarProductos();
        } else {
            mostrarAlerta("Error al cambiar el estado del producto.");
        }
    }

    /** Mostrar productos inactivos */
    @FXML
    private void mostrarProductosInactivos() {
        if (chBoxInactivos.isSelected()) {
            List<Producto> productosInactivos = Producto.obtenerTodosCompleto();
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
