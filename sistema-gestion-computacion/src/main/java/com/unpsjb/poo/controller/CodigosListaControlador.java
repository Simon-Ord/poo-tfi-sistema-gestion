package com.unpsjb.poo.controller;

import com.unpsjb.poo.model.productos.Producto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CodigosListaControlador implements Initializable {

    @FXML private TableView<Producto> productosTable;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;

    private int codigoSeleccionado; // Variable para almacenar el resultado
    private ObservableList<Producto> productosData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Configurar las Columnas para enlazar con las propiedades de la clase Producto
        // NOTA: "codigo" y "nombre" deben ser los nombres exactos de los getters de tu clase Producto (ej: getCodigo(), getNombre())
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));

        // Habilitar selección de una sola fila
        productosTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
    }
    
    // Método llamado por FacturaVistaControlador para pasar la lista de datos
    public void setProductos(List<Producto> productos) {
        this.productosData = FXCollections.observableArrayList(productos);
        productosTable.setItems(this.productosData);
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
            // 2. Cerrar la ventana
            cerrarVentana();
        } else {
            // Opcional: Mostrar una alerta de que debe seleccionar un producto
            System.out.println("Por favor, seleccione un producto.");
        }
    }

    @FXML
    private void handleCancelar() {
        this.codigoSeleccionado = (Integer) null; // No devuelve nada
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        // Obtener la Stage (ventana) actual y cerrarla
        Stage stage = (Stage) productosTable.getScene().getWindow();
        stage.close();
    }
}

