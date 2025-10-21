package com.unpsjb.poo.controller;

import java.math.BigDecimal;

import com.unpsjb.poo.model.Producto;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private Producto productoAEditar;

    @FXML
    private void initialize() {
        // HAY QUE CAMBIAR ESTO, CREANDO NUEVAS TABLAS DE CATEGORIAS Y FABRICANTES
        cbCategoria.getItems().addAll("Periféricos", "Monitores", "Almacenamiento", "Componentes", "Otros");
        cbFabricante.getItems().addAll("Logitech", "Redragon", "Kingston", "Samsung", "Otros");

    }
    // Guardar producto (crear o actualizar)
    @FXML
    private void guardarProducto() {
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
            nuevo.setActivo(true);
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
        //p.setCodigoProducto(Integer.parseInt(txtCodigo.getText().trim()));
        p.setNombreProducto(txtNombre.getText().trim());
        p.setDescripcionProducto(txtDescripcion.getText() == null ? null : txtDescripcion.getText().trim());
        p.setStockProducto(Integer.parseInt(txtStock.getText().trim()));
        p.setPrecioProducto(new BigDecimal(txtPrecio.getText().trim().replace(',', '.')));
        p.setCategoriaProducto(cbCategoria.getValue());
        p.setFabricanteProducto(cbFabricante.getValue());
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
    
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
    // Metodo para mostrar alertas
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }















}
