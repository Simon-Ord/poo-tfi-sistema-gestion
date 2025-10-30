package com.unpsjb.poo.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.unpsjb.poo.model.productos.Categoria;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Controlador sencillo para gestión de categorías
 * Maneja tanto la vista como el formulario en el mismo lugar
 */
public class CategoriasVistaControlador extends BaseControlador {

    @FXML private TableView<Categoria> tablaCategorias;
    @FXML private TableColumn<Categoria, String> colNombre;
    @FXML private TableColumn<Categoria, String> colEstado;
    @FXML private TableColumn<Categoria, String> colFechaCreacion;

    @FXML private TextField txtBuscar;
    @FXML private CheckBox chBoxInactivas;

    private ObservableList<Categoria> backingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEstadoTexto()));
        colFechaCreacion.setCellValueFactory(c -> {
            String fecha = c.getValue().getFechaCreacion() != null 
                ? c.getValue().getFechaCreacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
            return new javafx.beans.property.SimpleStringProperty(fecha);
        });

        cargarCategorias();
    }

    /** Cargar categorías en la tabla */
    public void cargarCategorias() {
        List<Categoria> lista = chBoxInactivas.isSelected() 
            ? Categoria.obtenerTodasCompleto() 
            : Categoria.obtenerTodas();
        
        backingList = FXCollections.observableArrayList(lista);
        tablaCategorias.setItems(backingList);
    }

    /** Alternar mostrar inactivas */
    @FXML
    private void mostrarCategoriasInactivas() {
        cargarCategorias();
    }

    /** Buscar categorías */
    @FXML
    private void buscarCategorias() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            tablaCategorias.setItems(backingList);
            return;
        }

        List<Categoria> filtradas = backingList.stream()
            .filter(c -> c.getNombre().toLowerCase().contains(busqueda))
            .toList();
        
        tablaCategorias.setItems(FXCollections.observableArrayList(filtradas));
    }

    /** Limpiar búsqueda */
    @FXML
    private void limpiarBusqueda() {
        txtBuscar.clear();
        tablaCategorias.setItems(backingList);
    }

    /** Agregar nueva categoría */
    @FXML
    private void agregarCategoria() {
        crearVentanaPequena("/view/formularios/CategoriaForm.fxml", "Agregar Categoría");
        cargarCategorias(); // Recargar datos después de cerrar la ventana
    }

    /** Modificar categoría seleccionada */
    @FXML
    private void modificarCategoria() {
        Categoria seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una categoría para modificar");
            return;
        }

        // TODO: Implementar paso de datos al formulario para edición
        crearVentanaPequena("/view/formularios/CategoriaForm.fxml", "Modificar Categoría");
        cargarCategorias(); // Recargar datos después de cerrar la ventana
    }

    /** Cambiar estado activo/inactivo */
    @FXML
    private void cambiarEstadoCategoria() {
        Categoria seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una categoría para cambiar su estado.");
            return;
        }

        String nuevoEstado = seleccionada.isActivo() ? "inactiva" : "activa";
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar cambio de estado");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Desea cambiar el estado de la categoría a " + nuevoEstado + "?");

        if (confirm.showAndWait().get().getButtonData().isDefaultButton()) {
            boolean ok = seleccionada.cambiarEstado(!seleccionada.isActivo());
            
            if (ok) {
                mostrarAlerta("La categoría cambió al estado: " + nuevoEstado);
                cargarCategorias();
            } else {
                mostrarAlerta("Error al cambiar el estado de la categoría.");
            }
        }
    }

    /** Mostrar alertas */
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}