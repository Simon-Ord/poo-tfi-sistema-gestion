package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.util.List;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.model.productos.Categoria;
import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.persistence.dao.impl.CategoriaDAOImpl;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;
import com.unpsjb.poo.util.Sesion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductoFormularioVistaControlador {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private ChoiceBox<Categoria> cbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private final ReportesDAO reportesDAO = new ReportesDAO(); // Nuevo: para registrar eventos de auditoría
    private Producto productoAEditar; 

    private final CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();

    @FXML
    private void initialize() {
        // Cargar las categorías desde la base de datos
        List<Categoria> categorias = categoriaDAO.findAll();
        if (categorias != null && !categorias.isEmpty()) {
            cbCategoria.getItems().addAll(categorias);
        }
    }
    // Guardar producto (crear o actualizar)
    @FXML
    private void guardarProducto() {
        try {
            // Validación de campos obligatorios
            if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty()
                    || txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty()
                    || cbCategoria.getValue() == null) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            boolean ok;
            if (productoAEditar != null) {
                // Actualizar producto existente
                setProducto(productoAEditar);
                ok = productoDAO.update(productoAEditar);
                if (ok) {
                    mostrarAlerta("Producto actualizado correctamente.");
                    registrarEventoAuditoria(productoAEditar, "ACTUALIZAR PRODUCTO");
                } else {
                    mostrarAlerta("Error al actualizar el producto. Revisa la consola para más detalles.");
                }
            } else {
                // Crear nuevo producto
                Producto nuevo = new Producto();
                setProducto(nuevo);
                nuevo.setActivo(true);
                ok = productoDAO.create(nuevo);
                if (ok) {
                    mostrarAlerta("Producto agregado correctamente.");
                    registrarEventoAuditoria(nuevo, "CREAR PRODUCTO");
                } else {
                    mostrarAlerta("Error al guardar el producto. Revisa la consola para más detalles.");
                }
            }
            
            if (ok) {
                cerrarVentana();
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
        p.setCategoria(cbCategoria.getValue());
    }
    // Carga los datos del producto a editar en los campos de la UI (el contrario al anterior digamos)
    private void cargarDatosEnCampos(Producto productoAEditar) {
        if (productoAEditar != null) {
            txtNombre.setText(productoAEditar.getNombreProducto());
            txtDescripcion.setText(productoAEditar.getDescripcionProducto());
            txtStock.setText(String.valueOf(productoAEditar.getStockProducto()));
            txtPrecio.setText(productoAEditar.getPrecioProducto().toString());
            cbCategoria.setValue(productoAEditar.getCategoria());
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
    private void registrarEventoAuditoria(Producto producto, String accion) {
        try {
            String usuarioActual = (Sesion.getUsuarioActual() != null)
                    ? Sesion.getUsuarioActual().getNombre()
                    : "Desconocido";

            EventoAuditoria evento = new EventoAuditoria();
            evento.setUsuario(usuarioActual);
            evento.setAccion(accion);
            evento.setEntidad("Producto");
            evento.setIdEntidad(String.valueOf(producto.getCodigoProducto()));
            String accionTexto = accion.equals("CREAR PRODUCTO") ? "creó" : "actualizó";
            evento.setDetalles("El usuario " + usuarioActual + " " + accionTexto + " el producto: " + producto.getNombreProducto());

            reportesDAO.registrarEvento(evento);

        } catch (Exception e) {
            System.err.println(" Error al registrar evento de producto: " + e.getMessage());
        }
    }
}
