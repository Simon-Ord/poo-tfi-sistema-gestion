package com.unpsjb.poo.controller;

import java.util.List;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class UsuariosVistaControlador {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colDni;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Boolean> colActivo;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @FXML
    public void initialize() {
        colDni.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDni()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colRol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRol()));
        colActivo.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().isEstado()));
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        List<Usuario> lista = usuarioDAO.obtenerTodos();
        ObservableList<Usuario> obsList = FXCollections.observableArrayList(lista);
        tablaUsuarios.setItems(obsList);
    }

    @FXML
    private void agregarUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UsuarioForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Nuevo Usuario");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            cargarUsuarios();
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

        boolean ok = usuarioDAO.modificar(seleccionado);

        if (ok) {
            mostrarAlerta(nuevoEstado ? "Usuario activado correctamente." : "Usuario desactivado correctamente.");
            cargarUsuarios();
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
