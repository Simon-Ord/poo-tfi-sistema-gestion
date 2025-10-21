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
<<<<<<< HEAD
    private final ReportesDAO reportesDAO = new ReportesDAO(); // 🟢 Nuevo: para registrar eventos de auditoría
    private Producto editing; // null = alta
=======
    private Producto productoAEditar;
>>>>>>> 96994ef3a83f7c6aad22d560e4f2fadd36b1ae55

    @FXML
    private void initialize() {
        // HAY QUE CAMBIAR ESTO, CREANDO NUEVAS TABLAS DE CATEGORIAS Y FABRICANTES
        cbCategoria.getItems().addAll("Periféricos", "Monitores", "Almacenamiento", "Componentes", "Otros");
        cbFabricante.getItems().addAll("Logitech", "Redragon", "Kingston", "Samsung", "Otros");
        chkActivo.setSelected(true); // Por defecto, el nuevo producto está activo
    }
    // Guardar producto (crear o actualizar)
    @FXML
    private void guardarProducto() {
<<<<<<< HEAD
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
                registrarEventoAuditoria(nuevo); // 🟢 Nuevo: registra en la tabla auditoria quién lo hizo
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

=======
    try {
        // Validar campos obligatorios
        if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty()
                || txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty()
                || cbCategoria.getValue() == null || cbFabricante.getValue() == null) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }
        boolean ok;
        if (productoAEditar != null) {
            // ====== EDITAR ======
            setProducto(productoAEditar);           
            ok = productoDAO.update(productoAEditar); 
            if (ok) {
                mostrarAlerta("Producto actualizado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta("Error al actualizar el producto. Revisá la consola.");
            }
        } else {
            // ====== CREAR ======
            Producto nuevo = new Producto();
            setProducto(nuevo);
            nuevo.setEstado(true);
            ok = productoDAO.create(nuevo);
            if (ok) {
                mostrarAlerta("Producto agregado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta("Error al guardar el producto. Revisá la consola.");
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado: " + e.getMessage());
        }
    }
    // Carga los datos de la UI en el objeto Producto
    private void setProducto(Producto p) {
        p.setCodigoProducto(Integer.parseInt(txtCodigo.getText().trim()));
        p.setNombreProducto(txtNombre.getText().trim());
        p.setDescripcionProducto(txtDescripcion.getText() == null ? null : txtDescripcion.getText().trim());
        p.setStockProducto(Integer.parseInt(txtStock.getText().trim()));
        p.setPrecioProducto(new BigDecimal(txtPrecio.getText().trim().replace(',', '.')));
        p.setCategoriaProducto(cbCategoria.getValue());
        p.setFabricanteProducto(cbFabricante.getValue());
        p.setActivo(chkActivo.isSelected());
    }
    // Carga los datos del producto a editar en los campos de la UI (el contrario al anterior digamos)
    private void cargarDatosEnCampos(Producto productoAEditar) {
        if (productoAEditar != null) {
            txtNombre.setText(productoAEditar.getNombreProducto());
            txtDescripcion.setText(productoAEditar.getDescripcionProducto());
            txtStock.setText(String.valueOf(productoAEditar.getStockProducto()));
            txtPrecio.setText(productoAEditar.getPrecioProducto().toString());
            cbCategoria.setValue(productoAEditar.getCategoriaProducto());
            cbFabricante.setValue(productoAEditar.getFabricanteProducto());
            txtCodigo.setText(String.valueOf(productoAEditar.getCodigoProducto()));
        }
    }
    // Setter para el producto a editar
    public void setProductoAEditar(Producto producto) {
        this.productoAEditar = producto;
        cargarDatosEnCampos(producto);
    }
    // Metodo para cancelar y cerrar la ventana
    @FXML private void cancelar() { cerrarVentana(); }
>>>>>>> 96994ef3a83f7c6aad22d560e4f2fadd36b1ae55
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
<<<<<<< HEAD

=======
    // Metodo para mostrar alertas
>>>>>>> 96994ef3a83f7c6aad22d560e4f2fadd36b1ae55
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
<<<<<<< HEAD
=======














>>>>>>> 96994ef3a83f7c6aad22d560e4f2fadd36b1ae55

    /**
     * 🟢 Método nuevo:
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
            System.err.println("⚠️ Error al registrar evento de producto: " + e.getMessage());
        }
    }
}
