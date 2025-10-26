package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.util.List;

import com.unpsjb.poo.model.productos.Categoria;
import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.util.CopiarProductoUtil;
import com.unpsjb.poo.util.Sesion;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaProductoUtil;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ProductoFormularioVistaControlador extends BaseControlador {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private ChoiceBox<Categoria> cbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private Producto productoAEditar;       // si es null -> alta
    private Producto productoOriginal;      // copia para comparar cambios
    
    // Referencia a ProductoVistaControlador para actualizar la tabla al cerrar
    private ProductosVistaControlador productosVista;

    @FXML
    private void initialize() {
        try {
            cargarCategorias();
                } catch (Exception e) {
            System.err.println("Error al inicializar el formulario de productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método separado para cargar categorías
    public void cargarCategorias(){
        try {
            List<Categoria> categorias = Categoria.obtenerTodas();
            if (categorias != null && !categorias.isEmpty()) {
                cbCategoria.getItems().clear();
                cbCategoria.getItems().addAll(categorias);
            } else {
                System.out.println("No se encontraron categorías en la base de datos");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar categorías: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Método usado por algunos controladores para configurar el producto a editar */
    public void setProducto(Producto p) {
        this.productoAEditar = p;
        if (p != null) {
            this.productoOriginal = CopiarProductoUtil.copiarProducto(p);
            cargarDatosEnCampos(p);
        }
    }

    // ESTE METODO CREO SE PUEDE BORRAR
        public void setProductoAEditar(Producto p) {
        setProducto(p);
    }

    /** Establecer referencia al controlador padre */
    public void setControladorPadre(ProductosVistaControlador productosVista) {
        this.productosVista = productosVista;
    }

    // Carga los datos del producto a editar en los campos de la UI
    private void cargarDatosEnCampos(Producto producto) {
        if (producto != null) {
            txtCodigo.setText(String.valueOf(producto.getCodigoProducto()));
            txtNombre.setText(producto.getNombreProducto());
            txtDescripcion.setText(producto.getDescripcionProducto());
            cbCategoria.setValue(producto.getCategoria());
            txtPrecio.setText(producto.getPrecioProducto() != null ? producto.getPrecioProducto().toPlainString() : "");
            txtStock.setText(String.valueOf(producto.getStockProducto()));
        }
    }

    @FXML
    private void guardarProducto() {
        try {
            // Validaciones básicas
            if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() ||
                txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty() ||
                cbCategoria.getValue() == null) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }
            boolean ok;
            String usuario = (Sesion.getUsuarioActual() != null)
                    ? Sesion.getUsuarioActual().getNombre()
                    : "Desconocido";
            if (productoAEditar == null) {
                // Crear nuevo producto
                Producto nuevo = new Producto();
                guardarDatosEnProducto(nuevo);
                // Por convención, los productos nuevos se crean activos
                nuevo.setActivo(true);
                ok = nuevo.guardar(); // Usar el método del modelo
                if (ok) {
                    AuditoriaUtil.registrarAccion(usuario, "CREAR PRODUCTO", "Producto");
                }
            } else {
                // Modificar producto existente
                guardarDatosEnProducto(productoAEditar);
                ok = productoAEditar.actualizar(); // Usar el método del modelo
                if (ok) {
          AuditoriaProductoUtil auditor = new AuditoriaProductoUtil();
           auditor.registrarAccionEspecifica(productoOriginal, productoAEditar);

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
            System.err.println("Error inesperado: " + e.getMessage());
            mostrarAlerta("Error inesperado: " + e.getMessage());
        }
    }

    // Guarda los datos del formulario en el objeto Producto
    private void guardarDatosEnProducto(Producto producto) {
        if (producto != null) {
            try {
                producto.setCodigoProducto(Integer.parseInt(txtCodigo.getText().trim()));
                producto.setNombreProducto(txtNombre.getText().trim());
                producto.setDescripcionProducto(txtDescripcion.getText() == null ? "" : txtDescripcion.getText().trim());
                producto.setStockProducto(Integer.parseInt(txtStock.getText().trim()));
                producto.setPrecioProducto(new BigDecimal(txtPrecio.getText().trim().replace(',', '.')));
                producto.setCategoria(cbCategoria.getValue());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Error en el formato de los campos numéricos");
            }
        }
    }

    @FXML
    public void agregarCategoria() {
        try {
            // Crear y mostrar la ventana del formulario de categoría
            crearFormulario("/view/formularios/CategoriaForm.fxml", "Agregar Nueva Categoría");
            // Recargar categorías por si se creó una nueva
            cargarCategorias();
        } catch (Exception e) {
            System.err.println("Error al abrir el formulario: " + e.getMessage());
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML private void cancelar() {cerrarVentana();}

    private void cerrarVentana() {
        if (productosVista != null) {
            productosVista.cargarProductos();
        }
        // Para ventanas internas, usar el método de BaseControlador
        BaseControlador.cerrarVentanaInterna(txtCodigo);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
