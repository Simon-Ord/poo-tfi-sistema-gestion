package com.unpsjb.poo.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.util.Exporter_pdf.PDFExporter;
import com.unpsjb.poo.util.Exporter_pdf.PDFReporte;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReportesControlador {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtEntidad;
    @FXML private TextField txtAccion;
    @FXML private TableView<EventoAuditoria> tablaReportes;
    @FXML private TableColumn<EventoAuditoria, String> colFecha;
    @FXML private TableColumn<EventoAuditoria, String> colUsuario;
    @FXML private TableColumn<EventoAuditoria, String> colAccion;
    @FXML private TableColumn<EventoAuditoria, String> colEntidad;
    @FXML private TableColumn<EventoAuditoria, String> colDetalles;

    private List<EventoAuditoria> resultados;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(c -> {
            Timestamp fechaHora = c.getValue().getFechaHora();
            String fechaFormateada = fechaHora != null ? DATE_FORMAT.format(fechaHora) : "";
            return new javafx.beans.property.SimpleStringProperty(fechaFormateada);
        });

        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsuario()));
        colAccion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAccion()));
        colEntidad.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEntidad()));
        colDetalles.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDetalles()));

        // Celdas con texto multilinea
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

        buscar();
    }

    /** Busca registros filtrados */
    @FXML
    private void buscar() {
        resultados = EventoAuditoria.obtenerEventos(
                txtUsuario.getText(),
                txtEntidad.getText(),
                txtAccion.getText()
        );

        tablaReportes.setItems(FXCollections.observableArrayList(resultados));

        if (resultados.isEmpty()) {
            mostrarAlerta("No se encontraron registros de auditoría con los filtros especificados.");
        }
    }

    /** Exporta los resultados a PDF en un hilo separado */
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
            // 🔹 Creamos un hilo para no bloquear la interfaz
            Thread hiloExportar = new Thread(() -> {
                PDFExporter pdf = new PDFReporte(resultados);
                boolean ok = pdf.export(file.getAbsolutePath());

                // 🔹 Volvemos al hilo principal (UI) para mostrar el mensaje
                javafx.application.Platform.runLater(() -> {
                    mostrarAlerta(ok
                            ? " Exportación completada correctamente.\nUbicación: " + file.getAbsolutePath()
                            : " Error al exportar el PDF.");
                });
            });

            //  Marcamos el hilo como secundario (no bloquea cierre del programa)
            hiloExportar.setDaemon(true);
            hiloExportar.start();
        }
    }

    /** Muestra un mensaje en pantalla */
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información del Reporte");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
