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
import javafx.scene.control.TextField;

public class ClientesVistaControlador extends BaseControlador {

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colCuit;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colDireccion;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colTipo;
    @FXML private TableColumn<Cliente, String> colActivo; 
    @FXML private TableColumn<Cliente, String> colEstado;
    
    @FXML private CheckBox chBoxInactivos;
    @FXML private TextField txtBuscar;

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
    
        System.out.println(" Inicializando pantalla de clientes...");
        configurarColumnas();
        configurarColumnasEstado();
        configurarListeners();
        buscarClientes(); // Carga inicial con búsqueda
    }
    
    private void configurarColumnas() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colCuit.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCuit()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colDireccion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        
        // Vincular la tabla con el ObservableList
        tablaClientes.setItems(listaClientes);
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
        // Listener para el checkbox de inactivos
        if (chBoxInactivos != null) {
            chBoxInactivos.selectedProperty().addListener((observable, oldValue, newValue) -> {
                colEstado.setVisible(newValue);
                buscarClientes();
            });
        }
        
        // Listener para el campo de búsqueda
        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> buscarClientes());
        }
    }
    // ==================================
    // BOTONES Y ACCIONES DEL CONTROLADOR
    // ==================================
    /** Buscar clientes */
    @FXML
    public void buscarClientes() {
        String q = (txtBuscar != null && txtBuscar.getText() != null)
                ? txtBuscar.getText().trim().toLowerCase()
                : "";
        // Usar búsqueda completa si el checkbox de inactivos está marcado
        List<Cliente> resultados;
        if (chBoxInactivos != null && chBoxInactivos.isSelected()) {
            resultados = Cliente.buscarClientesCompleto(q);
        } else {
            resultados = Cliente.buscarClientes(q);
        }
        // Actualizar el ObservableList para que se reflejen los cambios en tiempo real
        listaClientes.clear();
        listaClientes.addAll(resultados);
    }
    /** Limpiar búsqueda */
    @FXML
    private void limpiarBusqueda() {
        if (txtBuscar != null) {
            txtBuscar.clear(); 
        }
    }

    // Botón: Agregar cliente
    @FXML
    private void agregarCliente() {
        try {
            VentanaVistaControlador.ResultadoVentana resultado = 
                crearVentana("/view/ClienteForm.fxml", "Agregar Cliente");
            
            if (resultado != null) {
                ClienteFormularioVistaControlador controlador = 
                    resultado.getControlador(ClienteFormularioVistaControlador.class);
                if (controlador != null) {
                    controlador.setControladorPadre(this); // Pasar referencia
                }
            }
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
                crearVentana("/view/ClienteForm.fxml", "Editar Cliente");

            if (resultado != null) {
                ClienteFormularioVistaControlador controlador = 
                    resultado.getControlador(ClienteFormularioVistaControlador.class);
                if (controlador != null) {
                    controlador.setClienteEditable(seleccionado);
                    controlador.setControladorPadre(this); // Pasar referencia
                }
            }
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
        //  Registrar auditoría del cambio de estado
        AuditoriaClienteUtil auditor = new AuditoriaClienteUtil();
        auditor.registrarCambioEstado(seleccionado, !estadoAnterior);

        // Mensaje al usuario
        String msg = estadoAnterior
                ? "Cliente desactivado correctamente."
                : "Cliente reactivado correctamente.";

        mostrarAlerta(msg);
        buscarClientes(); // Recargar con búsqueda
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
    // ===== MÉTODOS PARA MODO SELECCIÓN EN FACTURACIÓN =====
    private boolean modoSeleccion = false;
    private FacturaVistaControlador facturaControlador;
    
    // Activa el modo selección para facturación
    public void setModoSeleccion(boolean modoSeleccion) {
        this.modoSeleccion = modoSeleccion;
        if (modoSeleccion) {
            // Agregar listener para doble clic cuando está en modo selección
            tablaClientes.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && facturaControlador != null) {
                    Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
                    if (seleccionado != null && seleccionado.isActivo()) {
                        facturaControlador.setClienteSeleccionado(seleccionado);
                        // Cerrar la ventana
                        BaseControlador.cerrarVentanaInterna(tablaClientes);
                    } else if (seleccionado != null && !seleccionado.isActivo()) {
                        mostrarAlerta("No se puede seleccionar un cliente inactivo.");
                    }
                }
            });
        }
    }
    // Establece la referencia al controlador de facturación
    public void setFacturaControlador(FacturaVistaControlador facturaControlador) {
        this.facturaControlador = facturaControlador;
    }
}
