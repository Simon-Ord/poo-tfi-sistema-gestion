package com.unpsjb.poo.controller;

import java.util.List;

import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.util.Sesion;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaClienteUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ClientesVistaControlador extends BaseControlador {

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
        // Vincular columnas
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colCuit.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCuit()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));

        cargarClientesDesdeBD();
    }

    private void cargarClientesDesdeBD() {
        listaClientes.clear();
        List<Cliente> clientesBD = Cliente.obtenerTodos();
        listaClientes.addAll(clientesBD);
        tablaClientes.setItems(listaClientes);
    }

    // Botón: Agregar cliente
    @FXML
    private void agregarCliente() {
        try {
            crearVentanaPequena("/view/ClienteForm.fxml", "Agregar Cliente");
            cargarClientesDesdeBD();
        } catch (Exception e) {
            mostrarAlerta("No se pudo abrir el formulario de cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Botón: Editar cliente
    @FXML
    private void editarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Debe seleccionar un cliente para editar.");
            return;
        }

        try {
            VentanaVistaControlador.ResultadoVentana resultado = 
                crearVentanaPequena("/view/ClienteForm.fxml", "Editar Cliente");

            if (resultado != null) {
                ClienteFormularioVistaControlador controlador = 
                    resultado.getControlador(ClienteFormularioVistaControlador.class);
                if (controlador != null) {
                    controlador.setClienteEditable(seleccionado);
                }
            }

            cargarClientesDesdeBD();
        } catch (Exception e) {
            mostrarAlerta("No se pudo abrir el formulario de edición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Botón: Eliminar cliente (baja lógica)
    @FXML
    private void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Debe seleccionar un cliente para eliminar.");
            return;
        }

        boolean eliminado = seleccionado.eliminar();
        if (eliminado) {
            mostrarAlerta("Cliente eliminado correctamente.");

            //Auditoría: cambio de estado
            AuditoriaClienteUtil auditor = new AuditoriaClienteUtil();
            auditor.registrarCambioEstado(seleccionado, false);

            cargarClientesDesdeBD();
        } else {
            mostrarAlerta("No se pudo eliminar el cliente.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gestión de Clientes");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
