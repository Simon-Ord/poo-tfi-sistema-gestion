package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.Cliente;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ClientesVistaControlador {

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colCuit;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colTipo;

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Vincular columnas con atributos del modelo Cliente
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colCuit.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCuit()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().esConsumidorFinal() ? "Consumidor Final" : "Responsable Inscripto"));

        // Cargar datos de prueba o desde DAO
        cargarClientesDemo();
        tablaClientes.setItems(listaClientes);
    }

    private void cargarClientesDemo() {
        listaClientes.addAll(
            new Cliente(1, "Juan Pérez", "20123456789", "2975001111", "juan@mail.com", "Responsable Inscripto"),
            new Cliente(2, "María López", null, "2974223344", "maria@mail.com", "Consumidor Final"),
            new Cliente(3, "Carlos Gómez", "27333444555", "2974332211", "carlos@mail.com", "Monotributista")
        );
    }

    // Botón: Agregar cliente
    @FXML
    private void agregarCliente() {
        System.out.println("Abrir ventana Agregar Cliente (próximo paso)");
        // Acá se abrirá el formulario AgregarCliente.fxml
    }

    // Botón: Editar cliente
    @FXML
    private void editarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Debe seleccionar un cliente para editar.");
            return;
        }
        System.out.println("Editar cliente: " + seleccionado.getNombre());
    }

    // Botón: Eliminar cliente
    @FXML
    private void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Debe seleccionar un cliente para eliminar.");
            return;
        }
        listaClientes.remove(seleccionado);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gestión de Clientes");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
