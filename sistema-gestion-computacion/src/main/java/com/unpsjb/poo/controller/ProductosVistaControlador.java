package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.util.List;

import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.util.Sesion;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaProductoUtil;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;

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
    @FXML private TableColumn<Producto, String> colEstado; // Nueva columna para estado

    @FXML private TextField txtBuscar;
    @FXML private CheckBox chBoxInactivos;

    private ObservableList<Producto> backingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarColumnasEstado();
        configurarListeners();
        cargarProductos();
    }

    //Configura las columnas básicas de la tabla
    private void configurarColumnas() {
        colCodigo.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getCodigoProducto()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreProducto()));
        colDescripcion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDescripcionProducto()));
        colCategoria.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getCategoria() != null ? c.getValue().getCategoria().getNombre() : "Sin Categoría"));
        colPrecio.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPrecioProducto()));
        colCantidad.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getStockProducto()));
    }

    // Configura la columna de estado solo con colores de fondo translúcidos
    private void configurarColumnasEstado() {
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(""));
        
        colEstado.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<Producto, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setStyle("");
                    } else {
                        Producto producto = getTableView().getItems().get(getIndex());
                        String backgroundColor = producto.isActivo() ? "rgba(40, 167, 69, 0.3)" : "rgba(220, 53, 69, 0.3)";
                        setStyle("-fx-background-color: " + backgroundColor + ";");
                    }
                }
            };
        });
        colEstado.setVisible(false); // Ocultar columna inicialmente
    }

    // Configura los listeners para búsqueda automática y checkbox
    private void configurarListeners() {
        // Listener para búsqueda automática en tiempo real
        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> buscarProductos());
        }
        // Listener para mostrar/ocultar productos inactivos
        if (chBoxInactivos != null) {
            chBoxInactivos.selectedProperty().addListener((observable, oldValue, newValue) -> {
                colEstado.setVisible(newValue);
                buscarProductos();
            });
        }
    }

    /** Carga todos los productos activos en la tabla */
    public void cargarProductos() {
        // Obtener productos a través del modelo, NO del DAO
        List<Producto> lista = Producto.obtenerTodos();
        backingList = FXCollections.observableArrayList(lista);
        tablaProductos.setItems(backingList);
        tablaProductos.refresh();
    }

    // ==================================
    // BOTONES Y ACCIONES DEL CONTROLADOR
    // ==================================
    /** Buscar productos */
    @FXML private void buscarProductos() {
        String q = (txtBuscar != null && txtBuscar.getText() != null)
                ? txtBuscar.getText().trim().toLowerCase()
                : "";
        // Usar búsqueda completa si el checkbox de inactivos está marcado
        List<Producto> resultados;
        if (chBoxInactivos != null && chBoxInactivos.isSelected()) {
            resultados = Producto.buscarProductosCompleto(q);
        } else {
            resultados = Producto.buscarProductos(q);
        }
        tablaProductos.setItems(FXCollections.observableArrayList(resultados));
    }

    /** Limpiar búsqueda */
    @FXML private void limpiarBusqueda() {
        if (txtBuscar != null) {
            txtBuscar.clear(); // Esto activará automáticamente el listener y hará la búsqueda
        }
        if (chBoxInactivos != null) {
            chBoxInactivos.setSelected(false); // También resetear el checkbox
        }
    }

    /** Agregar producto */
    @FXML private void agregarProducto() {
        crearFormulario("/view/productoForm.fxml", "Agregar Nuevo Producto");
        cargarProductos(); // Recargar datos después de cerrar la ventana
    }

    /** Modificar producto */
    @FXML private void modificarProducto() {
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
    @FXML private void cambiarEstadoProducto() {
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
                    AuditoriaProductoUtil auditor = new AuditoriaProductoUtil();
           auditor.registrarAccionEspecifica(seleccionado, auditor);
            }            
            String estadoActual = seleccionado.isActivo() ? "Activo" : "Inactivo";
            mostrarAlerta("El producto cambió al estado: " + estadoActual);
            cargarProductos();
        } else {
            mostrarAlerta("Error al cambiar el estado del producto.");
        }
    }

    /** Mostrar productos inactivos */
    @FXML private void mostrarProductosInactivos() {
        if (chBoxInactivos.isSelected()) {
            List<Producto> productosInactivos = Producto.obtenerTodosCompleto();
            tablaProductos.setItems(FXCollections.observableArrayList(productosInactivos));
        } else {
            tablaProductos.setItems(backingList);
        }
    }

    @FXML
    private void detallesProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un producto para ver sus detalles.");
            return;
        }

        try {
            VentanaVistaControlador.ResultadoVentana resultado = crearFormulario("/view/DetallesProducto.fxml", "Detalles del Producto", 530, 570);
            if (resultado != null && resultado.getControlador() != null) {
                DetallesProductoControlador controlador = (DetallesProductoControlador) resultado.getControlador();
                controlador.setProducto(seleccionado);
            }
        } catch (Exception e) {
            mostrarAlerta("Error al abrir los detalles: " + e.getMessage());
        }
    }

    /** Mostrar alertas */
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
