package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.Cliente;
import com.unpsjb.poo.util.AuditoriaUtil;
import com.unpsjb.poo.util.Sesion;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controlador del formulario de clientes.
 * 
 * 📘 Conceptos aplicados:
 * - Controlador se comunica solo con el Modelo (no con el DAO directamente).
 * - Auditoría automática para altas y modificaciones.
 * - Reutilización de formulario para agregar / editar.
 */
public class ClienteFormularioVistaControlador {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCuit;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDireccion;
    @FXML private ChoiceBox<String> cbTipoCliente;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Cliente clienteEditable; // null = modo agregar, != null = modo editar

    @FXML
    public void initialize() {
        cbTipoCliente.getItems().addAll(
                "Consumidor Final",
                "Responsable Inscripto",
                "Monotributista",
                "Exento"
        );
    }

    /** Botón Guardar */
    @FXML
    private void guardarCliente() {
        try {
            // Validar campos obligatorios
            if (txtNombre.getText().trim().isEmpty()) {
                mostrarAlerta("Debe ingresar un nombre para el cliente.");
                return;
            }

            if (clienteEditable == null) {
                // 🟢 === MODO AGREGAR ===
                Cliente nuevo = new Cliente();
                nuevo.setNombre(txtNombre.getText().trim());
                nuevo.setCuit(txtCuit.getText().trim());
                nuevo.setTelefono(txtTelefono.getText().trim());
                nuevo.setDireccion(txtDireccion.getText().trim());
                nuevo.setEmail(txtEmail.getText().trim());
                nuevo.setTipo(cbTipoCliente.getValue());

                boolean ok = nuevo.guardar();
                if (ok) {
                    mostrarAlerta("✅ Cliente agregado correctamente.");

                    // 🔍 Auditoría
                    AuditoriaUtil.registrarAccion("CREAR CLIENTE", "cliente",
                            "agregó un nuevo cliente: " + nuevo.getNombre());
                } else {
                    mostrarAlerta("❌ No se pudo guardar el cliente.");
                }

            } else {
                // 🟡 === MODO EDITAR ===
                Cliente copiaOriginal = copiarCliente(clienteEditable);

                clienteEditable.setNombre(txtNombre.getText().trim());
                clienteEditable.setCuit(txtCuit.getText().trim());
                clienteEditable.setTelefono(txtTelefono.getText().trim());
                clienteEditable.setDireccion(txtDireccion.getText().trim());
                clienteEditable.setEmail(txtEmail.getText().trim());
                clienteEditable.setTipo(cbTipoCliente.getValue());

                boolean ok = clienteEditable.guardar();
                if (ok) {
                    mostrarAlerta("✅ Cliente actualizado correctamente.");

                    AuditoriaUtil.registrarAccion("MODIFICAR CLIENTE", "cliente",
                            "modificó los datos del cliente '" + copiaOriginal.getNombre() + "'.");
                } else {
                    mostrarAlerta("❌ No se pudo actualizar el cliente.");
                }
            }

            cerrarVentana();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al guardar: " + e.getMessage());
        }
    }

    /** Botón Cancelar */
    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    /** Cierra la ventana del formulario */
    private void cerrarVentana() {
        BaseControlador.cerrarVentanaInterna(btnCancelar);
    }

    /** Muestra alertas informativas */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gestión de Clientes");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /** 🔹 Precargar datos para modo edición */
    public void setClienteEditable(Cliente cliente) {
        this.clienteEditable = cliente;

        txtNombre.setText(cliente.getNombre());
        txtCuit.setText(cliente.getCuit());
        txtTelefono.setText(cliente.getTelefono());
        txtDireccion.setText(cliente.getDireccion());
        txtEmail.setText(cliente.getEmail());
        cbTipoCliente.setValue(cliente.getTipo());

        btnGuardar.setText("Guardar Cambios");
    }

    /** 🔹 Copia segura para detectar cambios y registrar auditoría */
    private Cliente copiarCliente(Cliente original) {
        Cliente copia = new Cliente();
        copia.setId(original.getId());
        copia.setNombre(original.getNombre());
        copia.setCuit(original.getCuit());
        copia.setTelefono(original.getTelefono());
        copia.setDireccion(original.getDireccion());
        copia.setEmail(original.getEmail());
        copia.setTipo(original.getTipo());
        copia.setActivo(original.isActivo());
        return copia;
    }
}
