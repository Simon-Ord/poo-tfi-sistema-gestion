package com.unpsjb.poo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;
import java.util.List;
import javafx.scene.control.Alert;

public class UsuariosController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colContrasena; 
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Boolean> colEstado;
// aqui llama al dao para usar los metodos
    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsuario()));

        //  COLUMNA DE CONTRASEÑA (MOSTRAR TEXTO)
        colContrasena.setCellValueFactory(c -> 
            new javafx.beans.property.SimpleStringProperty(c.getValue().getContraseña()));

        // Si se quisiera mostrar la contraseña encriptada, usar:
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

    @FXML
    private void agregarUsuario() {
        mostrarAlerta("Función agregar usuario en desarrollo...");
        // Más adelante abriremos una ventana FXML para registrar nuevos usuarios
        // Al cerrar esa ventana, recargar la tabla con cargarUsuarios()
        // Por ahora solo mostramos una alerta
    
    }

    @FXML
    // Función para modificar usuario (en desarrollo)
    private void modificarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un usuario para modificar");
            return;
        }
        // Aquí abriríamos una ventana para modificar el usuario seleccionado
        // Al cerrar esa ventana, recargar la tabla con cargarUsuarios()
        mostrarAlerta("Función modificar usuario en desarrollo por ahora despues lo pongo");
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un usuario para eliminar");
            return;
        }
        // Confirmar eliminación en la base de datos y recargar la tabla si se elimina correctamente
        boolean ok = usuarioDAO.eliminar(seleccionado.getId());
        if (ok) {
            mostrarAlerta("Usuario eliminado correctamente");
            cargarUsuarios(); // Recargar la tabla después de eliminar el usuario
        } else {
            mostrarAlerta("Error al eliminar el usuario"); // Mensaje de error si no se pudo eliminar
        }
    }

    // Mostrar alertas informativas
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
