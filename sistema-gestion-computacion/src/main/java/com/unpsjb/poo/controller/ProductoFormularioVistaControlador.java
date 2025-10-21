package com.unpsjb.poo.controller;

import java.math.BigDecimal;

import com.unpsjb.poo.model.Producto;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;
import com.unpsjb.poo.util.Sesion;
import com.unpsjb.poo.util.CopiarProductoUtil;
import com.unpsjb.poo.util.AuditoriaUtil;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    private Producto productoAEditar;       // si es null -> alta
    private Producto productoOriginal;      // copia para comparar cambios

    @FXML
    private void initialize() {
        cbCategoria.getItems().addAll("Periféricos", "Monitores", "Almacenamiento", "Componentes", "Otros");
        cbFabricante.getItems().addAll("Logitech", "Redragon", "Kingston", "Samsung", "Otros");
        chkActivo.setSelected(true);
    }

    /** Método usado por algunos controladores */
    public void setProducto(Producto p) {
        this.productoAEditar = p;
        if (p != null) {
            this.productoOriginal = CopiarProductoUtil.copiarProducto(p);
            txtCodigo.setText(String.valueOf(p.getCodigoProducto()));
            txtNombre.setText(p.getNombreProducto());
            txtDescripcion.setText(p.getDescripcionProducto());
            cbCategoria.setValue(p.getCategoriaProducto());
            cbFabricante.setValue(p.getFabricanteProducto());
            txtPrecio.setText(p.getPrecioProducto() != null ? p.getPrecioProducto().toPlainString() : "");
            txtStock.setText(String.valueOf(p.getStockProducto()));
            chkActivo.setSelected(p.isActivo());
        }
    }

    /** Alias para compatibilidad: permite que quien llame setProductoAEditar(...) funcione */
    public void setProductoAEditar(Producto p) {
        setProducto(p);
    }

    @FXML
    private void guardarProducto() {
        try {
            // Validaciones básicas
            if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() ||
                txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty() ||
                cbCategoria.getValue() == null || cbFabricante.getValue() == null) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            Producto nuevo = new Producto();
            nuevo.setCodigoProducto(Integer.parseInt(txtCodigo.getText().trim()));
            nuevo.setNombreProducto(txtNombre.getText().trim());
            nuevo.setDescripcionProducto(txtDescripcion.getText().trim());
            nuevo.setCategoriaProducto(cbCategoria.getValue());
            nuevo.setFabricanteProducto(cbFabricante.getValue());
            nuevo.setPrecioProducto(new BigDecimal(txtPrecio.getText().trim().replace(',', '.')));
            nuevo.setStockProducto(Integer.parseInt(txtStock.getText().trim()));
            nuevo.setActivo(chkActivo.isSelected());
            // <-- eliminado nuevo.setEstado(true);  (no existe en tu modelo)

            boolean ok;
            String usuario = (Sesion.getUsuarioActual() != null)
                    ? Sesion.getUsuarioActual().getNombre()
                    : "Desconocido";

            if (productoAEditar == null) {
                // Crear nuevo producto
                // ATENCIÓN: si tu ProductoDAOImpl tiene método insertar(...) cambia create por insertar
                ok = productoDAO.create(nuevo);
                if (ok) {
                    AuditoriaUtil.registrarAccion(usuario, "CREAR PRODUCTO", "Producto",
                            "El usuario " + usuario + " creó el producto: " + nuevo.getNombreProducto());
                }
            } else {
                // Modificar producto existente
                nuevo.setIdProducto(productoAEditar.getIdProducto());
                ok = productoDAO.update(nuevo);
                if (ok) {
                    AuditoriaUtil.registrarCambioProducto(productoOriginal, nuevo, usuario);
                }
            }

            if (ok) {
                mostrarAlerta("Producto guardado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta("Error al guardar el producto. Revisa la consola.");
            }

        } catch (NumberFormatException nfe) {
            mostrarAlerta("Formato numérico incorrecto (precio o stock).");
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
}
