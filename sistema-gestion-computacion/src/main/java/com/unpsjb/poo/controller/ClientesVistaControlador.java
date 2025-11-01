package com.unpsjb.poo.controller;

import java.util.List;

import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaClienteUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ClientesVistaControlador extends BaseControlador {

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colCuit;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colTipo;
    @FXML private TableColumn<Cliente, String> colActivo; 
    @FXML private TableColumn<Cliente, String> colEstado;
    
    @FXML private CheckBox chBoxInactivos;

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
    
        System.out.println(" Inicializando pantalla de clientes...");
        configurarColumnas();
        configurarColumnasEstado();
        configurarListeners();
        cargarClientesDesdeBD();
    }
    
    private void configurarColumnas() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colCuit.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCuit()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
    }
    
    private void configurarColumnasEstado() {
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(""));
        
        colEstado.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<Cliente, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setStyle("");
                    } else {
                        Cliente cliente = getTableView().getItems().get(getIndex());
                        String backgroundColor = cliente.isActivo() ? "rgba(40, 167, 69, 0.3)" : "rgba(220, 53, 69, 0.3)";
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
                cargarClientesDesdeBD();
            });
        }
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
        mostrarAlerta("Debe seleccionar un cliente para activar o desactivar.");
        return;
    }

    boolean estadoAnterior = seleccionado.isActivo();

    // Alterna el estado (baja lógica)
    boolean ok = seleccionado.eliminar(); // este método ya cambia activo <-> inactivo en la BD

    if (ok) {
        // 🔸 Registrar auditoría del cambio de estado
        AuditoriaClienteUtil auditor = new AuditoriaClienteUtil();
        auditor.registrarCambioEstado(seleccionado, !estadoAnterior);

        // Mensaje al usuario
        String msg = estadoAnterior
                ? "Cliente desactivado correctamente."
                : "Cliente reactivado correctamente.";

        mostrarAlerta(msg);
        cargarClientesDesdeBD();
    } else {
        mostrarAlerta("No se pudo cambiar el estado del cliente.");
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
