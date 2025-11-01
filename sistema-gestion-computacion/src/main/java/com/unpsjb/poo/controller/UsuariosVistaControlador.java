package com.unpsjb.poo.controller;

import java.util.List;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UsuariosVistaControlador extends BaseControlador {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colDni;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Boolean> colActivo;
    @FXML private TableColumn<Usuario, String> colEstado;
    
    @FXML private CheckBox chBoxInactivos;

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarColumnasEstado();
        configurarListeners();
        cargarUsuarios();
    }
    
    private void configurarColumnas() {
        colDni.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDni()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colRol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRol()));
    }
    
    private void configurarColumnasEstado() {
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(""));
        
        colEstado.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<Usuario, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setStyle("");
                    } else {
                        Usuario usuario = getTableView().getItems().get(getIndex());
                        String backgroundColor = usuario.isEstado() ? "rgba(40, 167, 69, 0.3)" : "rgba(220, 53, 69, 0.3)";
                        setStyle("-fx-background-color: " + backgroundColor + ";");
                    }
                }
            };
        });
        colEstado.setVisible(false);
    }
    
    private void configurarListeners() {
        if (chBoxInactivos != null) {
            chBoxInactivos.selectedProperty().addListener((observable, oldValue, newValue) -> {
                colEstado.setVisible(newValue);
                cargarUsuarios();
            });
        }
    }
    @FXML
    private void cargarUsuarios() {
        List<Usuario> lista = Usuario.obtenerTodos();
        ObservableList<Usuario> obsList = FXCollections.observableArrayList(lista);
        tablaUsuarios.setItems(obsList);
    }
    
    @FXML
    private void agregarUsuario() {
        try {
            var resultado = crearVentanaPequena("/view/UsuarioForm.fxml", "Agregar Nuevo Usuario");

            if (resultado != null && resultado.getVentana() != null) {
                resultado.getVentana().parentProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal ==null) {
                        cargarUsuarios();
                    }
                });
        }

    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
    }
}


    @FXML
    private void cambiarEstadoUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un usuario para cambiar su estado.");
            return;
        }

        boolean nuevoEstado = !seleccionado.isEstado();
        seleccionado.setEstado(nuevoEstado);

        boolean ok = seleccionado.actualizar();

        if (ok) {
            mostrarAlerta(nuevoEstado ? "Usuario activado correctamente." : "Usuario desactivado correctamente.");
            cargarUsuarios();

            AuditoriaUtil.registrarAccion(
                nuevoEstado ? "ACTIVAR USUARIO" : "DESACTIVAR USUARIO",
                "usuario",
                "cambió el estado de " + seleccionado.getNombre() + " a " + (nuevoEstado ? "ACTIVO" : "INACTIVO")
            );
        } else {
            mostrarAlerta("Error al cambiar el estado del usuario.");
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
