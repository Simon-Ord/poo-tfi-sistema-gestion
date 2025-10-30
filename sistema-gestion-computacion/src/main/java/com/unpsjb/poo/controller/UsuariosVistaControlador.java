package com.unpsjb.poo.controller;

import java.util.List;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UsuariosVistaControlador extends BaseControlador {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colDni;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Boolean> colActivo;

    @FXML
    public void initialize() {
        colDni.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDni()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colRol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRol()));
        colActivo.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().isEstado()));
        cargarUsuarios();
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
            var resultado = crearFormulario("/view/UsuarioForm.fxml", "Agregar Nuevo Usuario");

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
