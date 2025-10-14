package com.unpsjb.poo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.stage.Stage;

import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;

import java.util.List;

public class UsuariosController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colContrasena;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Boolean> colEstado;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsuario()));
        colContrasena.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getContraseña()));
        colRol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRol()));
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().isEstado()));

        cargarUsuarios();
    }

    @FXML
    private void cargarUsuarios() {
        List<Usuario> lista = usuarioDAO.obtenerTodos();
        ObservableList<Usuario> obsList = FXCollections.observableArrayList(lista);
        tablaUsuarios.setItems(obsList);
    }

    // NUEVO MÉTODO: abrir ventana para agregar usuario
    @FXML
    private void agregarUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/usuarioForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Nuevo Usuario");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Espera a que se cierre la ventana

            cargarUsuarios(); // Recargar tabla después de agregar
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de usuario: " + e.getMessage());
        }
    }












    @FXML
    private void modificarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un usuario para modificar");
            return;
        }
        mostrarAlerta("Función modificar usuario en desarrollo");
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un usuario para eliminar");
            return;
        }

        boolean ok = usuarioDAO.eliminar(seleccionado.getId());
        if (ok) {
            mostrarAlerta("Usuario eliminado correctamente");
            cargarUsuarios();
        } else {
            mostrarAlerta("Error al eliminar el usuario");
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
