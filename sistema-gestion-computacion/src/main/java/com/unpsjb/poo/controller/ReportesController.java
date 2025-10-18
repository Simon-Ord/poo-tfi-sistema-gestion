package com.unpsjb.poo.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.unpsjb.poo.model.AuditEvent;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.CSVExporter;  // ✅ <-- ESTA ES LA LÍNEA QUE FALTABA

import java.io.File;
import java.util.List;


public class ReportesController {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtEntidad;
    @FXML private TextField txtAccion;
    @FXML private TableView<AuditEvent> tablaReportes;
    @FXML private TableColumn<AuditEvent, String> colFecha;
    @FXML private TableColumn<AuditEvent, String> colUsuario;
    @FXML private TableColumn<AuditEvent, String> colAccion;
    @FXML private TableColumn<AuditEvent, String> colEntidad;
    @FXML private TableColumn<AuditEvent, String> colDetalles;

    private final ReportesDAO dao = new ReportesDAO();
    private List<AuditEvent> resultados;

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getOccurredAt() != null ? c.getValue().getOccurredAt().toString() : ""));
        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsername()));
        colAccion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAction()));
        colEntidad.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEntity()));
        colDetalles.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDetails()));
    }

    @FXML
    private void buscar() {
        resultados = dao.obtenerEventos(txtUsuario.getText(), txtEntidad.getText(), txtAccion.getText());
        tablaReportes.setItems(FXCollections.observableArrayList(resultados));
    }

 @FXML
public void exportarCSV() {
    if (resultados == null || resultados.isEmpty()) {
        mostrarAlerta("No hay datos para exportar.");
        return;
    }

    FileChooser fc = new FileChooser();
    fc.setTitle("Guardar Reporte");
    fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));
    File file = fc.showSaveDialog(new Stage());
    if (file != null) {
        boolean ok = CSVExporter.exportAuditEventsToCSV(resultados, file.getAbsolutePath());
        mostrarAlerta(ok ? "Exportación completada correctamente." : "Error al exportar el CSV.");
    }
}

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
