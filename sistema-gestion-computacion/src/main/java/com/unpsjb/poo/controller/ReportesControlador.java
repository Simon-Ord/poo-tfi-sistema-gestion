package com.unpsjb.poo.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.PDFExporter;
import com.unpsjb.poo.util.PDFReporte;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReportesControlador {

    // ========== REFERENCIAS FXML ==========
    @FXML private TextField txtUsuario;
    @FXML private TextField txtEntidad;
    @FXML private TextField txtAccion;
    @FXML private TableView<EventoAuditoria> tablaReportes;
    @FXML private TableColumn<EventoAuditoria, String> colFecha;
    @FXML private TableColumn<EventoAuditoria, String> colUsuario;
    @FXML private TableColumn<EventoAuditoria, String> colAccion;
    @FXML private TableColumn<EventoAuditoria, String> colEntidad;
    @FXML private TableColumn<EventoAuditoria, String> colDetalles;

    private final ReportesDAO dao = new ReportesDAO();
    private List<EventoAuditoria> resultados;

    // Formato de fecha para la tabla
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // ========== MÉTODOS DE INICIALIZACIÓN ==========
    @FXML
    public void initialize() {
        // Formatear fecha
        colFecha.setCellValueFactory(c -> {
            Timestamp fechaHora = c.getValue().getFechaHora();
            String fechaFormateada = fechaHora != null
                    ? DATE_FORMAT.format(fechaHora)
                    : "";
            return new javafx.beans.property.SimpleStringProperty(fechaFormateada);
        });

        // Otras columnas
        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsuario()));
        colAccion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAccion()));
        colEntidad.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEntidad()));
        colDetalles.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDetalles()));

        // ✅ Nuevo: celdas de “Detalles” con texto ajustable (multilínea)
        colDetalles.setCellFactory(tc -> {
            TableCell<EventoAuditoria, String> cell = new TableCell<>() {
                private final Text text = new Text();
                {
                    text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                    setGraphic(text);
                    setPrefHeight(Control.USE_COMPUTED_SIZE);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    text.setText(empty ? "" : item);
                }
            };
            return cell;
        });

        // Cargar datos iniciales
        buscar();

        // Aplicar CSS si es posible
        try {
            java.net.URL cssUrl = getClass().getResource("/css/reportes.css");
            if (cssUrl != null && tablaReportes.getScene() != null) {
                tablaReportes.getScene().getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al aplicar CSS: " + e.getMessage());
        }
    }

    // ========== MÉTODOS DE ACCIÓN ==========

    /** Ejecuta la búsqueda con los filtros actuales */
    @FXML
    private void buscar() {
        resultados = dao.obtenerEventos(
                txtUsuario.getText(),
                txtEntidad.getText(),
                txtAccion.getText()
        );

        tablaReportes.setItems(FXCollections.observableArrayList(resultados));

        if (resultados.isEmpty()) {
            mostrarAlerta("No se encontraron registros de auditoría con los filtros especificados.");
        }
    }

    /** Exporta los resultados actuales a un PDF */
    @FXML
    public void exportarPDF() {
        if (resultados == null || resultados.isEmpty()) {
            mostrarAlerta("No hay datos cargados o filtrados para exportar.");
            return;
        }

        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar Reporte de Auditoría (PDF)");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));

        Stage stage = (Stage) tablaReportes.getScene().getWindow();
        File file = fc.showSaveDialog(stage);

        if (file != null) {
            PDFExporter pdf = new PDFReporte(resultados);
            boolean ok = pdf.export(file.getAbsolutePath());

            mostrarAlerta(ok
                    ? "Exportación completada correctamente.\nUbicación: " + file.getAbsolutePath()
                    : " Error al exportar el PDF.");
        }
    }

    /** Muestra un mensaje informativo */
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información del Reporte");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
