package com.unpsjb.poo.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private static final java.text.SimpleDateFormat DATE_FORMAT = 
        new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


        // ========== MÉTODOS DE INICIALIZACIÓN Y CONFIGURACIÓN ==========
        // Configura las columnas de la tabla y carga datos iniciales
        // Llamado automáticamente tras cargar el FXML
    @FXML
    public void initialize() {
        // Formatear la fecha
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

        // Cargar datos iniciales
        buscar();
        try {
            java.net.URL cssUrl = getClass().getResource("/css/reportes.css");
            if (cssUrl == null) {
                 System.out.println(" No se encontró el CSS de reportes. Ruta esperada: src/main/resources/css/reportes.css");
            } else {
                 // Si la Scene está disponible, intenta aplicar el CSS
                 if (tablaReportes.getScene() != null) {
                     tablaReportes.getScene().getStylesheets().add(cssUrl.toExternalForm());
                 }
            }
        } catch (Exception e) {
            // No hacemos nada si falla la Scene en initialize
        }
    }


    @FXML
    private void buscar() {
        // Llama al DAO con los filtros de los TextField
        // Si un campo está vacío, se pasa una cadena vacía para no filtrar por ese campo
        resultados = dao.obtenerEventos(txtUsuario.getText(), txtEntidad.getText(), txtAccion.getText());
        
        // Actualiza la tabla
        tablaReportes.setItems(FXCollections.observableArrayList(resultados));
        
        if (resultados.isEmpty()) {
            mostrarAlerta("No se encontraron registros de auditoría con los filtros especificados.");
        }
    }

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
        // Usamos la clase polimórfica asi que puede ser PDFReporte u otra futura implementación de PDFExporter
        PDFExporter pdf = new PDFReporte(resultados);
        boolean ok = pdf.export(file.getAbsolutePath());// Genera el PDF en la ruta indicada 

        mostrarAlerta(ok 
            ? "Exportación completada correctamente \nUbicación: " + file.getAbsolutePath()
            : "Error al exportar el PDF ");
    }
}

// Muestra una alerta informativa con el mensaje dado
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información del Reporte");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}