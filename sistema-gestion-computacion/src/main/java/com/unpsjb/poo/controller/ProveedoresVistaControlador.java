package com.unpsjb.poo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.unpsjb.poo.model.Proveedor;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;
import java.util.List;

public class ProveedoresVistaControlador extends BaseControlador {

    @FXML private TableView<Proveedor> tablaProveedores;
    @FXML private TableColumn<Proveedor, String> colNombre;
    @FXML private TableColumn<Proveedor, String> colTelefono;
    @FXML private TableColumn<Proveedor, String> colEmail;
    @FXML private TableColumn<Proveedor, String> colDireccion;
    @FXML private TableColumn<Proveedor, String> colTipo;
    @FXML private TableColumn<Proveedor, String> colEstado;
    @FXML private CheckBox chBoxInactivos;

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarColumnasEstado();
        configurarListeners();
        cargarProveedores();
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colTelefono.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefono()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        colDireccion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDireccion()));
        colTipo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTipo() == null ? "FISICO" : c.getValue().getTipo()));
    }

    private void configurarColumnasEstado() {
        colEstado.setCellValueFactory(c -> {
            boolean activo = c.getValue().isActivo();
            return new javafx.beans.property.SimpleStringProperty(activo ? "Activo" : "Inactivo");
        });
    }

    private void configurarListeners() {
        if (chBoxInactivos != null) {
            chBoxInactivos.selectedProperty().addListener((obs, oldVal, newVal) -> cargarProveedores());
        }
    }

    @FXML
    private void cargarProveedores() {
        try {
            List<Proveedor> proveedores;
            if (chBoxInactivos != null && chBoxInactivos.isSelected()) {
                proveedores = Proveedor.obtenerTodos();
            } else {
                proveedores = Proveedor.obtenerActivos();
            }
            tablaProveedores.setItems(FXCollections.observableArrayList(proveedores));
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar los proveedores: " + e.getMessage());
        }
    }

    @FXML
    private void agregarProveedor() {
        try {
            var resultado = crearVentana("/view/formularios/ProveedorForm.fxml", "Agregar Nuevo Proveedor");
            if (resultado != null && resultado.getVentana() != null) {
                // Si el formulario necesita el controlador padre o inicialización, se haría aquí
                resultado.getVentana().parentProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == null) {
                        cargarProveedores();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void editarProveedor() {
        Proveedor seleccionado = tablaProveedores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un proveedor para editar.");
            return;
        }

        try {
            var resultado = crearVentana("/view/formularios/ProveedorForm.fxml", "Editar Proveedor");
            if (resultado != null) {
                // Pasar el proveedor seleccionado al controlador del formulario
                Object ctrl = resultado.getControlador();
                if (ctrl instanceof com.unpsjb.poo.controller.ProveedorFormularioVistaControlador) {
                    ((com.unpsjb.poo.controller.ProveedorFormularioVistaControlador) ctrl).setProveedorAEditar(seleccionado);
                }
                resultado.getVentana().parentProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == null) {
                        cargarProveedores();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de edición: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarProveedor() {
        Proveedor seleccionado = tablaProveedores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un proveedor para eliminar.");
            return;
        }

        try {
            boolean ok = seleccionado.desactivar();
            if (ok) {
                AuditoriaUtil.registrarAccion(
                    "ELIMINAR PROVEEDOR",
                    "proveedor",
                    "Se eliminó al proveedor: " + seleccionado.getNombre()
                );
                cargarProveedores();
                mostrarAlerta("Proveedor eliminado correctamente.");
            } else {
                mostrarAlerta("Error al eliminar el proveedor.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al eliminar el proveedor: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
