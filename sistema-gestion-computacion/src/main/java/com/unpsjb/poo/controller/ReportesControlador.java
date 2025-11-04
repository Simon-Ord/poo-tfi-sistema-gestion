package com.unpsjb.poo.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;


import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.util.Exporter_pdf.PDFExportar;
import com.unpsjb.poo.util.Exporter_pdf.PDFReporte;

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
    @FXML private DatePicker dpDesde;
@FXML private DatePicker dpHasta;


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
    java.sql.Date fechaDesde = (dpDesde.getValue() != null)
            ? java.sql.Date.valueOf(dpDesde.getValue()) : null;
    java.sql.Date fechaHasta = (dpHasta.getValue() != null)
            ? java.sql.Date.valueOf(dpHasta.getValue()) : null;

    resultados = EventoAuditoria.obtenerEventos(
            txtUsuario.getText(),
            txtEntidad.getText(),
            txtAccion.getText(),
            fechaDesde,
            fechaHasta
    );

        

        tablaReportes.setItems(FXCollections.observableArrayList(resultados));

        if (resultados.isEmpty()) {
            mostrarAlerta("No se encontraron registros de auditoría con los filtros especificados.");
        }
    }

    @FXML
private void filtrarPorFecha() {
    if (dpDesde.getValue() == null || dpHasta.getValue() == null) {
        mostrarAlerta("Debe seleccionar ambas fechas para filtrar.");
        return;
    }

    List<EventoAuditoria> filtrados = EventoAuditoria.obtenerEventosPorRangoFechas(
            dpDesde.getValue(), dpHasta.getValue()
    );

    tablaReportes.setItems(FXCollections.observableArrayList(filtrados));

    if (filtrados.isEmpty()) {
        mostrarAlerta("No se encontraron registros en el rango de fechas seleccionado.");
    } else {
        resultados = filtrados; // actualizamos la lista actual (para exportar PDF)
    }
}


/** Exporta los resultados a PDF automáticamente en un hilo separado */
@FXML
public void exportarPDF() {
    if (resultados == null || resultados.isEmpty()) {
        mostrarAlerta("No hay datos cargados o filtrados para exportar.");
        return;
    }

    //  Generar nombre automático y ruta 
    String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
    String fileName = "Reporte_MUNDO_PC_" + timestamp + ".pdf";
    String filePath = System.getProperty("user.home") + "/" + fileName;

    //  Crear hilo para exportar sin bloquear la interfaz
    Thread hiloExportar = new Thread(() -> {
        PDFExportar pdf = new PDFReporte(resultados);
        boolean ok = pdf.export(filePath);

        // Volver al hilo de la interfaz para mostrar mensaje
        javafx.application.Platform.runLater(() -> {
            mostrarAlerta(ok
                    ? "Exportación completada correctamente.\nUbicacion: " + filePath
                    : " Error al exportar el PDF.");
        });
    });

    // Marcamos el hilo como daemon (no bloquea el cierre del programa)
    hiloExportar.setDaemon(true);
    hiloExportar.start();
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
