package com.unpsjb.poo.controller;

import java.util.List;

import com.unpsjb.poo.model.Proveedor;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
        // Mostrar texto "Activo" / "Inactivo" y colorear la celda semejante a clientes/usuarios
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isActivo() ? "Activo" : "Inactivo"));
        colEstado.setCellFactory(column -> new javafx.scene.control.TableCell<Proveedor,String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Proveedor proveedor = getTableView().getItems().get(getIndex());
                    String backgroundColor = proveedor.isActivo() ? "rgba(40, 167, 69, 0.3)" : "rgba(220, 53, 69, 0.3)";
                    setText(item);
                    setStyle("-fx-background-color: " + backgroundColor + "; -fx-alignment: CENTER; -fx-text-fill: black;");
                }
            }
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
    private void cambiarEstadoProveedor() {
        Proveedor seleccionado = tablaProveedores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un proveedor para activar o desactivar.");
            return;
        }

        try {
            boolean estadoAnterior = seleccionado.isActivo();
            boolean ok = seleccionado.cambiarEstado();
            if (ok) {
                String nuevoEstado = seleccionado.isActivo() ? "ACTIVO" : "INACTIVO";
                AuditoriaUtil.registrarAccion(
                    "CAMBIAR ESTADO PROVEEDOR",
                    "proveedor",
                    "Proveedor '" + seleccionado.getNombre() + "' pasó de " + (estadoAnterior?"ACTIVO":"INACTIVO") + " a " + nuevoEstado
                );
                cargarProveedores();
                mostrarAlerta(" El proveedor cambió al estado: " + nuevoEstado);
            } else {
                mostrarAlerta(" Error al cambiar el estado del proveedor.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al cambiar estado del proveedor: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
