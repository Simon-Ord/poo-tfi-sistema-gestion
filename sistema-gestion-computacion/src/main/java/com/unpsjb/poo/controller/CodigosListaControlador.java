package com.unpsjb.poo.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.unpsjb.poo.model.productos.Producto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CodigosListaControlador extends BaseControlador implements Initializable {

    @FXML private TableView<Producto> productosTable;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private TextField txtBuscar;

    private int codigoSeleccionado; // Variable para almacenar el resultado
    private ObservableList<Producto> productosData;
    private FacturaVistaControlador controladorPadre; // Para comunicarse de vuelta

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        configurarListeners();
    }

    /**
     * Configura las columnas de la tabla
     */
    private void configurarColumnas() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockProducto"));
        
        // Habilitar selección de una sola fila
        productosTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
    }

    /**
     * Configura los listeners para búsqueda automática
     */
    private void configurarListeners() {
        // Listener para búsqueda automática en tiempo real
        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> buscarProductos());
        }
    }
    
    // Método llamado por FacturaVistaControlador para pasar la lista de datos
    public void setProductos(List<Producto> productos) {
        this.productosData = FXCollections.observableArrayList(productos);
        productosTable.setItems(this.productosData);
    }

    // Método para configurar el controlador padre
    public void setControladorPadre(FacturaVistaControlador controladorPadre) {
        this.controladorPadre = controladorPadre;
    }

    /**
     * Busca productos por cualquier atributo usando la base de datos
     * (código, nombre, descripción, categoría, precio, stock)
     */
    @FXML
    private void buscarProductos() {
        String q = (txtBuscar != null && txtBuscar.getText() != null)
                ? txtBuscar.getText().trim()
                : "";
        
        // Usar la misma búsqueda en BD que ProductosVistaControlador
        List<Producto> resultados = Producto.buscarProductos(q);
        productosTable.setItems(FXCollections.observableArrayList(resultados));
    }
    
    // Devuelve el código seleccionado (se llama desde FacturaVistaControlador)
    public Integer getCodigoSeleccionado() {
        return codigoSeleccionado;
    }

    // --- MANEJO DE BOTONES ---

    @FXML
    private void handleSeleccionar() {
        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();
        
        if (productoSeleccionado != null) {
            // 1. Guardar el código seleccionado para devolverlo
            this.codigoSeleccionado = productoSeleccionado.getCodigoProducto();
            
            // 2. Si hay controlador padre, pasarle el código seleccionado
            if (controladorPadre != null) {
                controladorPadre.setCodigoProductoSeleccionado(this.codigoSeleccionado);
            }
            
            // 3. Cerrar la ventana
            cerrarVentana();
        } else {
            // Opcional: Mostrar una alerta de que debe seleccionar un producto
            System.out.println("Por favor, seleccione un producto.");
        }
    }

    @FXML
    private void handleCancelar() {
        this.codigoSeleccionado = 0; // Valor que indica cancelación
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        // Para ventanas internas, usar el método de BaseControlador
        BaseControlador.cerrarVentanaInterna(productosTable);
    }
}

