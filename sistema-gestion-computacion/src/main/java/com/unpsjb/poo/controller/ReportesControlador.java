package com.unpsjb.poo.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.ReportesDAO;
import com.unpsjb.poo.util.PDFExporter; 

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

    private final ReportesDAO dao = new ReportesDAO();
    private List<EventoAuditoria> resultados;
    
    // Formato de fecha
    private static final java.text.SimpleDateFormat DATE_FORMAT = 
        new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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

        // ⚠️ CORRECCIÓN DE CSS: Solo verifica la existencia del archivo, evita el error fatal
        try {
            java.net.URL cssUrl = getClass().getResource("/css/reportes.css");
            if (cssUrl == null) {
                 System.out.println("⚠️ No se encontró el CSS de reportes. Ruta esperada: src/main/resources/css/reportes.css");
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
        resultados = dao.obtenerEventos(txtUsuario.getText(), txtEntidad.getText(), txtAccion.getText());
        
        // Actualiza la tabla
        tablaReportes.setItems(FXCollections.observableArrayList(resultados));
        
        if (resultados.isEmpty()) {
            mostrarAlerta("No se encontraron registros de auditoría con los filtros especificados.");
        }
    }

    @FXML
    // MÉTODO RENOMBRADO, AUNQUE EL FXML DEBERÍA CAMBIARSE TAMBIÉN.
    // Si mantienes el FXML como está, DEBES llamar al método 'exportarCSV' aquí.
    public void exportarPDF() { 
        if (resultados == null || resultados.isEmpty()) {
            mostrarAlerta("No hay datos cargados o filtrados para exportar.");
            return;
        }

        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar Reporte de Auditoría (PDF)");
        // El filtro es para PDF
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf")); 
        
        // Obtiene la Stage actual
        Stage stage = (Stage) tablaReportes.getScene().getWindow();
        File file = fc.showSaveDialog(stage);
        
        if (file != null) {
            // Llamamos a la clase PDFExporter
            boolean ok = PDFExporter.exportAuditEventsToPDF(resultados, file.getAbsolutePath());
            
            mostrarAlerta(ok 
                ? "Exportación a PDF completada correctamente en:\n" + file.getAbsolutePath() 
                : "Error al exportar el PDF. Revisa la consola para más detalles.");
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información del Reporte");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}