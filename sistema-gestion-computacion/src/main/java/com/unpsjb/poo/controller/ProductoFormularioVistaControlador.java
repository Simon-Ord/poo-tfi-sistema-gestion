package com.unpsjb.poo.controller;

import java.math.BigDecimal;

import com.unpsjb.poo.model.Producto;
import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.Sesion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductoFormularioVistaControlador {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private ChoiceBox<String> cbCategoria;
    @FXML private ChoiceBox<String> cbFabricante;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private CheckBox chkActivo;

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private final ReportesDAO reportesDAO = new ReportesDAO(); // Nuevo: para registrar eventos de auditoría
    private Producto editing; // null = alta

    @FXML
    private void initialize() {
        cbCategoria.getItems().addAll("Periféricos", "Monitores", "Almacenamiento", "Componentes", "Otros");
        cbFabricante.getItems().addAll("Logitech", "Redragon", "Kingston", "Samsung", "Otros");
        chkActivo.setSelected(true); // Por defecto, el nuevo producto está activo
    }

    public void setProducto(Producto p) {
        this.editing = p;
        if (p != null) {
            txtCodigo.setText(String.valueOf(p.getCodigoProducto()));
            txtNombre.setText(p.getNombreProducto());
            txtDescripcion.setText(p.getDescripcionProducto());
            cbCategoria.setValue(p.getCategoriaProducto());
            cbFabricante.setValue(p.getFabricanteProducto());
            txtPrecio.setText(p.getPrecioProducto() != null ? p.getPrecioProducto().toPlainString() : "");
            txtStock.setText(String.valueOf(p.getStockProducto()));
        }
    }

    @FXML
    private void guardarProducto() {
        try {
            if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty()
                    || txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty()
                    || cbCategoria.getValue() == null || cbFabricante.getValue() == null) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            Producto nuevo = new Producto();
            nuevo.setCodigoProducto(Integer.parseInt(txtCodigo.getText().trim()));
            nuevo.setNombreProducto(txtNombre.getText().trim());
            nuevo.setDescripcionProducto(txtDescripcion.getText() == null ? null : txtDescripcion.getText().trim());
            nuevo.setStockProducto(Integer.parseInt(txtStock.getText().trim()));
            nuevo.setPrecioProducto(new BigDecimal(txtPrecio.getText().trim().replace(',', '.')));
            nuevo.setCategoriaProducto(cbCategoria.getValue());
            nuevo.setFabricanteProducto(cbFabricante.getValue());
            nuevo.setEstado(true);
            nuevo.setActivo(chkActivo.isSelected());

            boolean ok = productoDAO.create(nuevo);

            if (ok) {
                mostrarAlerta("Producto agregado correctamente.");
                registrarEventoAuditoria(nuevo); //  Nuevo: registra en la tabla auditoria quién lo hizo
                cerrarVentana();
            } else {
                mostrarAlerta("Error al guardar el producto. Revisa la consola para más detalles.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     *  Método nuevo:
     * Registra en la tabla de auditoría quién creó el producto.
     */
    private void registrarEventoAuditoria(Producto producto) {
        try {
            String usuarioActual = (Sesion.getUsuarioActual() != null)
                    ? Sesion.getUsuarioActual().getNombre()
                    : "Desconocido";

            EventoAuditoria evento = new EventoAuditoria();
            evento.setUsuario(usuarioActual);
            evento.setAccion("CREAR PRODUCTO");
            evento.setEntidad("Producto");
            evento.setIdEntidad(String.valueOf(producto.getCodigoProducto()));
            evento.setDetalles("El usuario " + usuarioActual + " creó el producto: " + producto.getNombreProducto());

            reportesDAO.registrarEvento(evento);

        } catch (Exception e) {
            System.err.println(" Error al registrar evento de producto: " + e.getMessage());
        }
    }
}
