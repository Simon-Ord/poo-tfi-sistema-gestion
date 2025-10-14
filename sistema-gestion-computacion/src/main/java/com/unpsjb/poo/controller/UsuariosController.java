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

public class UsuariosController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colLegajo;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colContraseña;
    @FXML private TableColumn<Usuario, String> colRol;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    // ============ Inicialización =============
    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));
        colLegajo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLegajo()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsuario()));
        colContraseña.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getContraseña()));
        colRol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRol()));

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        List<Usuario> lista = usuarioDAO.obtenerTodos();
        ObservableList<Usuario> obsList = FXCollections.observableArrayList(lista);
        tablaUsuarios.setItems(obsList);
    }


    // ============ BOTÓN "Agregar Usuario" =============
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

            // Recargar tabla después de agregar
            cargarUsuarios();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de usuario: " + e.getMessage());
        }
    }

    // ============ BOTÓN "Modificar" ============
    @FXML
    private void modificarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un usuario para modificar.");
            return;
        }
        mostrarAlerta("Función de modificar usuario en desarrollo.");
    }

    // ============ BOTÓN "Eliminar" ============
    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un usuario para eliminar.");
            return;
        }

        boolean ok = usuarioDAO.eliminar(seleccionado.getId());
        if (ok) {
            mostrarAlerta("Usuario eliminado correctamente.");
            cargarUsuarios();
        } else {
            mostrarAlerta("Error al eliminar el usuario.");
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
