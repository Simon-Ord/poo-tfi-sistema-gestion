package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.unpsjb.poo.model.Cliente; 
import com.unpsjb.poo.model.EstrategiaPago;
import com.unpsjb.poo.model.ItemCarrito; 
import com.unpsjb.poo.model.PagoEfectivo;
import com.unpsjb.poo.model.PagoTarjeta;
import com.unpsjb.poo.model.Venta;
import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaVentaUtil;
import com.unpsjb.poo.util.Exporter_pdf.PDFExportar;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FacturaVistaControlador extends BaseControlador implements Initializable {

    private boolean vistaDatosFacturaInicializada = false;

    // INYECCIÓN DE VISTAS MAESTRAS
    @FXML private Node FacturaAgregarProductos; 
    @FXML private Node FacturaDatosVenta;    
    @FXML private Node FacturaConfirmarVenta; 

    // INYECCIÓN DE ELEMENTOS DEL PASO 1 - Agregar Productos
    @FXML private TextField txtCodigoProducto; 
    @FXML private TextField txtCantidad; 
    @FXML private TableView<ItemCarrito> carritoTable; 
    @FXML private Label lblTotalParcial; 
    @FXML private Label lblStockDisponible; 
    
    // INYECCIÓN DE ELEMENTOS DEL PASO 2 - Datos de Venta
    @FXML private ComboBox <String> cbTipoFactura;
    @FXML private VBox panelDatosCliente; 
    @FXML private VBox vboxClienteInfo;
    @FXML private Label lblClienteSeleccionado;
    @FXML private Label lblCuitSeleccionado;
    @FXML private Button btnCargarCliente;
    @FXML private Button btnAgregarCliente;
    @FXML private HBox hboxCambiarCliente;
    @FXML private Label lblEstadoCliente;
    @FXML private Label lblTotalVenta; 
    
    // INYECCIÓN DE ELEMENTOS DEL PASO 3 - Confirmación de Venta
    @FXML private Label lblTipoFacturaResumen;
    @FXML private Label lblClienteResumen;
    @FXML private Label lblMontoSinIVAResumen;
    @FXML private Label lblIVAResumen;
    @FXML private Label lblComisionPago; 
    @FXML private ScrollPane scrollPaneItems; 
    @FXML private ComboBox<EstrategiaPago> cbMetodoPago; 
    @FXML private VBox vboxItemsLista;
    @FXML private Label lblPrecioTotalCarrito; 
    @FXML private Button btnExportarPDF; 
    
    // BOTONES DE NAVEGACIÓN
    @FXML private Button btnVolver; // Botón "Atrás" para navegar estados
    @FXML private Button btnSiguiente; // Botón "Siguiente" para navegar estados

    private boolean vistaConfirmacionPagoInicializada = false;
    private final AuditoriaVentaUtil auditoriaVentaUtil = new AuditoriaVentaUtil();
    // MODELO DE DATOS Y ESTADO
    private Venta miVenta;
    private Map<String, Node> vistaMap;

    // INICIALIZACIÓN DEL MODELO
    @Override 
    public void initialize(URL url, ResourceBundle rb) {
    miVenta = new Venta(); // Crear nueva venta al iniciar la vista
    vistaMap = new HashMap<>(); // Inicializar el mapa de vistas

    // Los nombres de las claves deben coincidir con lo que devuelve estado.getVistaID()
    vistaMap.put("FacturaAgregarProductos", FacturaAgregarProductos); 
    vistaMap.put("FacturaDatosVenta", FacturaDatosVenta); 
    vistaMap.put("FacturaConfirmarVenta", FacturaConfirmarVenta); 
    
    // CONFIGURACIÓN DE LA UI INICIAL
    // a. Configurar la tabla: enlazar la TableView con la lista observable del Carrito
    carritoTable.setItems(FXCollections.observableArrayList(miVenta.getCarrito().getItems()));
    // b. Mostrar la primera vista: Usar el ID del estado inicial (EstadoAgregarProductos)
    actualizarVisibilidadVistas(miVenta.getEstadoActual().getVistaID());
    // c. Actualizar el total parcial inicial (que es cero al comenzar)
    actualizarTotalParcial();
    // d. Deshabilitar el botón de exportar PDF inicialmente
    if (btnExportarPDF != null) {
        btnExportarPDF.setDisable(true);
    }
    // e. Escuchar cambios en el código para mostrar stock disponible
    if (txtCodigoProducto != null) {
        txtCodigoProducto.textProperty().addListener((obs, oldV, newV) -> actualizarStockSegunCodigo(newV));
    }
    manejarSolicitudesUIDelEstado();
}
//Recalcula el total del Carrito y actualiza el Label de la interfaz.
private void actualizarTotalParcial() {
    lblTotalParcial.setText("$ " + miVenta.getCarrito().getTotal());
}
    // =============================================================================================================================================
    // MANEJO DE EVENTOS DE LA VISTA 1: CARRITO
    // =============================================================================================================================================
    @FXML public void handleAnadirItem() {
        String codigo = txtCodigoProducto.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();
        // Validación inline directa
        if (codigo.isEmpty() || cantidadStr.isEmpty()) {
            mostrarAlerta("Error", "Complete todos los campos", Alert.AlertType.WARNING);
            return;
        }
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                mostrarAlerta("Error", "La cantidad debe ser mayor a 0", Alert.AlertType.WARNING);
                return;
            }
            Producto producto = buscarProductoPorCodigo(codigo);
            if (producto != null) {
                // Validar stock disponible antes de agregar al carrito
                if (!producto.tieneStockSuficiente(cantidad)) {
                    mostrarAlerta("Error", 
                        "Stock insuficiente para el producto: " + producto.getNombreProducto() + 
                        "\nDisponible: " + producto.getStockProducto() + 
                        "\nSolicitado: " + cantidad, 
                        Alert.AlertType.WARNING);
                    return;
                }
                miVenta.getCarrito().agregarItemAlCarrito(producto, cantidad);
                carritoTable.setItems(FXCollections.observableArrayList(miVenta.getCarrito().getItems()));
                actualizarTotalParcial();
                txtCodigoProducto.clear();
                txtCantidad.clear();
                // Limpiar indicador de stock
                if (lblStockDisponible != null) {
                    lblStockDisponible.setText("");
                    lblStockDisponible.setStyle("");
                }
            } else {
                mostrarAlerta("Error", "Producto no encontrado: " + codigo, Alert.AlertType.WARNING);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Cantidad inválida", Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    // Busca un producto por su código exacto (solo activos). Retorna null si no se encuentra.
    private Producto buscarProductoPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return null;
        // Pasar directamente al DAO especializado para evitar coincidencias parciales
        try {
            return new com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl()
                    .findByCodigo(codigo.trim())
                    .orElse(null);
        } catch (Exception e) {
            return null; // Silencioso: se maneja en quien llama.
        }
    }
    // Maneja la acción de quitar un item del carrito
    @FXML public void handleQuitarItem() {
        ItemCarrito seleccionado = carritoTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            miVenta.getCarrito().eliminarItemDelCarrito(seleccionado); 
            carritoTable.setItems(FXCollections.observableArrayList(miVenta.getCarrito().getItems()));
            lblTotalParcial.setText("$ " + miVenta.getCarrito().getTotal());
        }
    }
    @FXML public void handleListarCodigos() {
        try {
            List<Producto> listaProductos = Producto.obtenerTodos(); // Buscar todos
            abrirVentanaCodigos(listaProductos);
        } catch (Exception e) {
            mostrarAlerta("Error de DB", "No se pudo cargar la lista de productos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void abrirVentanaCodigos(List<Producto> productos) {
        VentanaVistaControlador.ResultadoVentana resultado = crearVentanaModal(
            "/view/CodigosListaVista.fxml", 
            "Lista de Códigos de Productos", 
            500, 450
        );
        if (resultado != null && resultado.getControlador() != null) {
            try {
                CodigosListaControlador controlador = (CodigosListaControlador) resultado.getControlador();
                controlador.setProductos(productos);
                controlador.setControladorPadre(this);
                resultado.getVentana().setVisible(true);
            } catch (ClassCastException e) {
                mostrarAlerta("Error de Configuración", "El archivo FXML no corresponde al controlador esperado.", Alert.AlertType.ERROR);
            }
        }
    }
    // Metodo reutilizable para crear ventanas modales
    private VentanaVistaControlador.ResultadoVentana crearVentanaModal(String fxml, String titulo, int ancho, int alto) {
        try {
            return crearVentana(fxml, titulo, ancho, alto);
        } catch (Exception e) {
            mostrarAlerta("Error de Vista", "No se pudo cargar la ventana: " + e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
    }
    // Método llamado por CodigosListaControlador cuando se selecciona un producto
    public void setCodigoProductoSeleccionado(int codigo) {
        txtCodigoProducto.setText(String.valueOf(codigo));
        actualizarStockSegunCodigo(String.valueOf(codigo));
    }
    // =============================================================================================================================================
    // MANEJO DE EVENTOS DE LA VISTA 2: FACTURA/TICKET
    // =============================================================================================================================================
    @FXML
    public void handleTipoFacturaSelected() {
        String tipoSeleccionado = cbTipoFactura.getSelectionModel().getSelectedItem();
        
        if ("Factura".equals(tipoSeleccionado)) {
            panelDatosCliente.setVisible(true); // Mostrar el formulario
            miVenta.setTipoFactura("FACTURA");
        } else if ("Ticket".equals(tipoSeleccionado)) {
            panelDatosCliente.setVisible(false); // Ocultar el formulario
            miVenta.setTipoFactura("TICKET");
            miVenta.setClienteFactura(null); // Limpiar cliente si elige Ticket
        } else {
            // Si deselecciona o selecciona null
            panelDatosCliente.setVisible(false);
            miVenta.setTipoFactura(null);
            miVenta.setClienteFactura(null);
        }
        actualizarEstadoCliente("", false, false); // Ocultar estado
    }
    // Recibe los datos del cliente recién registrado desde el formulario
    public void setClienteTemporal(Cliente cliente) {
        try {
            // Usar el cliente ya creado y guardado desde el formulario
            miVenta.setClienteFactura(cliente); 
            // Actualizar la interfaz
            actualizarInfoCliente(cliente);
            actualizarEstadoCliente("Cliente cargado exitosamente.", true, false);
            
        } catch (IllegalArgumentException e) {
            actualizarEstadoCliente("ERROR de Datos: " + e.getMessage(), true, true);
        }
    }
    // Actualiza la interfaz para mostrar la información del cliente seleccionado
    private void actualizarInfoCliente(Cliente cliente) {
        if (cliente != null) {
            lblClienteSeleccionado.setText("Cliente: " + cliente.getNombre());
            lblCuitSeleccionado.setText("CUIT: " + cliente.getCuit());
            vboxClienteInfo.setVisible(true);
            btnCargarCliente.setVisible(false);
            btnAgregarCliente.setVisible(false);
            hboxCambiarCliente.setVisible(true);
        }
    }
    // Limpia la información del cliente de la interfaz
    private void limpiarInfoCliente() {
        vboxClienteInfo.setVisible(false);
        btnCargarCliente.setVisible(true);
        btnAgregarCliente.setVisible(true);
        hboxCambiarCliente.setVisible(false);
        actualizarEstadoCliente("", false, false);
    }
    // Actualiza el estado del cliente en la interfaz
    @FXML
    public void handleCargarClienteExistente() {
        abrirVentanaClientes("/view/ClientesView.fxml", "Seleccionar Cliente Existente", 800, 600, true);
    }
    // Abre la ventana para registrar un nuevo cliente  
    @FXML
    public void handleRegistrarCliente() {
        abrirVentanaClientes("/view/ClienteForm.fxml", "Registrar Nuevo Cliente", 400, 500, false);
    }
    // Método unificado para abrir ventanas de cliente
    private void abrirVentanaClientes(String fxml, String titulo, int ancho, int alto, boolean modoSeleccion) {
        VentanaVistaControlador.ResultadoVentana resultado = crearVentanaModal(fxml, titulo, ancho, alto);
        
        if (resultado != null && resultado.getControlador() != null) {
            try {
                if (modoSeleccion) {
                    // Ventana de selección de cliente existente
                    ClientesVistaControlador controlador = (ClientesVistaControlador) resultado.getControlador();
                    controlador.setModoSeleccion(true);
                    controlador.setFacturaControlador(this);
                } else {
                    // Ventana de registro de cliente nuevo
                    ClienteFormularioVistaControlador controlador = (ClienteFormularioVistaControlador) resultado.getControlador();
                    controlador.setFacturaControlador(this);
                }
                resultado.getVentana().setVisible(true);
            } catch (ClassCastException e) {
                mostrarAlerta("Error de Configuración", "El archivo FXML no corresponde al controlador esperado.", Alert.AlertType.ERROR);
            }
        }
    }
    // Método llamado desde ClientesVistaControlador cuando se selecciona un cliente
    public void setClienteSeleccionado(Cliente cliente) {
        if (cliente != null) {
            miVenta.setClienteFactura(cliente);
            actualizarInfoCliente(cliente);
            actualizarEstadoCliente("Cliente seleccionado: " + cliente.getNombre(), true, false);
        }
    }
    // Maneja el cambio de cliente (limpia la selección actual)
    @FXML
    public void handleCambiarCliente() {
        miVenta.setClienteFactura(null);
        limpiarInfoCliente();
        actualizarEstadoCliente("Seleccione un cliente para la factura.", true, false);
    }
// =============================================================================================================================================
// MANEJO DE EVENTOS DE LA VISTA 3: CONFIRMACIÓN Y PAGO (Strategy)
// =============================================================================================================================================
// Configura el ComboBox con las estrategias de pago disponibles
private void configurarComboBoxMetodosPago() {
    cbMetodoPago.setItems(FXCollections.observableArrayList(new PagoEfectivo(), new PagoTarjeta()));
    // Configurar visualización del ComboBox
    cbMetodoPago.setCellFactory(param -> new javafx.scene.control.ListCell<EstrategiaPago>() {
        @Override
        protected void updateItem(EstrategiaPago item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.getNombreMetodoPago());
        }
    });
    cbMetodoPago.setButtonCell(new javafx.scene.control.ListCell<EstrategiaPago>() {
        @Override
        protected void updateItem(EstrategiaPago item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.getNombreMetodoPago());
        }
    });
}
// Actualiza el resumen de la venta y selecciona el método de pago por defecto
private void actualizarResumenYSeleccionarPago() {
    if (lblPrecioTotalCarrito == null || lblComisionPago == null) {
        System.err.println("Error: Faltan inyecciones críticas de Label en Vista 3.");
        return;
    }
    mostrarResumenVenta(); 
    cbMetodoPago.getSelectionModel().selectFirst();
    handleMetodoPagoSelected(); 
}
// Muestra los detalles finales (IVA y cliente) en la vista de resumen.
private void mostrarResumenVenta() {
    // 1. OBTENER DATOS CALCULADOS DEL MODELO
    BigDecimal subtotalSinIVA = miVenta.calcularSubtotalSinIVA();
    BigDecimal montoIVA = miVenta.calcularMontoIVA();
    BigDecimal totalCarrito = miVenta.getCarrito().getTotal();
    // 2. OBTENER DATOS DE CLIENTE Y TIPO DE FACTURA DEL MODELO
    String nombreCliente = miVenta.obtenerDescripcionCliente();
    String tipoFactura = miVenta.obtenerDescripcionTipoFactura();
    // 3. ACTUALIZAR COMPONENTES DE LA UI
    actualizarLabelsResumen(tipoFactura, nombreCliente, subtotalSinIVA, montoIVA, totalCarrito);
    actualizarListaProductos();
}
// Actualiza los labels del resumen con los datos proporcionados
private void actualizarLabelsResumen(String tipoFactura, String nombreCliente, BigDecimal subtotalSinIVA, BigDecimal montoIVA, BigDecimal totalCarrito) {
    if (lblTipoFacturaResumen != null) lblTipoFacturaResumen.setText(tipoFactura);
    if (lblClienteResumen != null) lblClienteResumen.setText(nombreCliente);
    if (lblMontoSinIVAResumen != null) lblMontoSinIVAResumen.setText("Subtotal sin IVA: $ " + subtotalSinIVA);
    if (lblIVAResumen != null) lblIVAResumen.setText("Monto IVA (21%): $ " + montoIVA);
    if (lblPrecioTotalCarrito != null) lblPrecioTotalCarrito.setText("$ " + totalCarrito);
}
// Actualiza la lista de productos en el resumen de la venta
private void actualizarListaProductos() {
    if (vboxItemsLista != null) {
        vboxItemsLista.getChildren().clear();
        for (ItemCarrito item: miVenta.getCarrito().getItems()) {
            String itemTexto = String.format("%s - Cantidad: %d - Subtotal: $%s",
                item.getProducto().getNombreProducto(), item.getCantidad(), item.getSubtotal());
            Label lblItem = new Label(itemTexto);
            lblItem.setStyle("-fx-padding: 5; -fx-font-size: 12px;");
            vboxItemsLista.getChildren().add(lblItem);
        }
        if (scrollPaneItems != null) {
            scrollPaneItems.setContent(vboxItemsLista);
        }
    }
}
// Llamado por el ComboBox. Aplica la Estrategia de Pago seleccionada.
@FXML public void handleMetodoPagoSelected() {
    EstrategiaPago estrategia = cbMetodoPago.getSelectionModel().getSelectedItem();
    if (estrategia != null) {
        // Establecer la estrategia de pago en el modelo Venta
        miVenta.setEstrategiaPago(estrategia);
        if (lblComisionPago == null || lblPrecioTotalCarrito == null) {
            System.err.println("Error: Faltan inyecciones críticas de Label en Vista 3.");
            return; 
        }
        BigDecimal totalConComision = miVenta.calcularTotalConComision();
        double comision = estrategia.getComision();
        // Actualizar UI
        lblComisionPago.setText("Comision/Descuento: " + String.format("%.2f", comision * 100) + "%");
        lblPrecioTotalCarrito.setText("$ " + totalConComision);
    }
}
// Boton "Registrar Venta" en la Vista 3
@FXML public void handleRegistrarVenta() {
    if (miVenta.getEstrategiaPago() == null) {
        mostrarAlerta("Error", "Debe seleccionar un método de pago", Alert.AlertType.WARNING);
        return;
    }
    try {
        // Procesar el pago usando la estrategia seleccionada
        double montoTotal = miVenta.getCarrito().getTotal().doubleValue();
        double comision = miVenta.getEstrategiaPago().getComision();
        double montoConComision = montoTotal * (1 + comision);
        boolean pagoExitoso = miVenta.getEstrategiaPago().pagar(montoConComision);
        if (pagoExitoso) {
            miVenta.guardarVentaBD(); // Guardar la venta en la base de datos
            // Habilitar el botón de exportar PDF después de completar la venta
            if (btnExportarPDF != null) {
                btnExportarPDF.setDisable(false);
            }
            // ---------------- auditoria ----------------// 
            auditoriaVentaUtil.registrarCreacion(miVenta);
            mostrarAlerta("Éxito", "Venta registrada correctamente", Alert.AlertType.INFORMATION);
        } else {
            throw new RuntimeException("ERROR: El pago no pudo ser procesado.");
        }
    } catch (Exception e) {
        System.err.println("Error al registrar venta: " + e.getMessage());
        mostrarAlerta("Error", "No se pudo completar la venta: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}
// ===================================
// Llamado por el botón "Exportar PDF".
@FXML public void handleExportarPDF() {
    // Generar nombre automático y ruta 
    String tipoDoc = "FACTURA".equalsIgnoreCase(miVenta.getTipoFactura()) ? "Factura" : "Ticket";
    String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
    String fileName = tipoDoc + "_" + timestamp + ".pdf";
    String filePath = System.getProperty("user.home") + "/" + fileName;
    
    try {
        // Crear el generador de PDF según tipo de factura
        PDFExportar pdfGenerator;
        if ("FACTURA".equalsIgnoreCase(miVenta.getTipoFactura())) {
            pdfGenerator = new com.unpsjb.poo.util.Exporter_pdf.PDFFactura(miVenta);
        } else {
            pdfGenerator = new com.unpsjb.poo.util.Exporter_pdf.PDFTicket(miVenta);
        }

        // Crear hilo para exportar el PDF (no bloquea la interfaz)
        Thread hiloExportar = new Thread(() -> {
            boolean ok = pdfGenerator.export(filePath);

            // Volvemos al hilo principal para mostrar el mensaje
            javafx.application.Platform.runLater(() -> {
                if (ok) {
                    mostrarAlerta("Éxito", tipoDoc + " generado correctamente en:\n" + filePath,
                            Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "No se pudo generar el " + tipoDoc + ".", Alert.AlertType.ERROR);
                }
            });
        });

        // Hilo secundario (no bloquea cierre del programa)
        hiloExportar.setDaemon(true);
        hiloExportar.start();
        
    } catch (Exception e) {
        mostrarAlerta("Error", "Error al inicializar exportación: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}
// -------------------------------------------------------------------------
// LÓGICA DE NAVEGACIÓN (PATRÓN STATE)
// ------------------------------------------------------------------------- 
// Avanza la Venta al siguiente estado secuencial (Paso 1 -> Paso 2 -> Paso 3).
@FXML public void handleSiguientePaso() {
    try {
        miVenta.validarTransicion(); // Validar antes de avanzar
        miVenta.siguientePaso(); 
        actualizarVistaSegunEstado();
    } catch (IllegalStateException e) {
        mostrarAlerta("Error de Validación", e.getMessage(), Alert.AlertType.WARNING);
    } catch (Exception e) {
        mostrarAlerta("Error", "Error al avanzar: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}
// Retrocede la Venta al estado anterior.
@FXML public void handleVolverPaso() {
    try {
        miVenta.volverPaso();
        actualizarVistaSegunEstado();
        // Actualización específica para vista de datos
        if ("FacturaDatosVenta".equals(miVenta.getEstadoActual().getVistaID())) {
            lblTotalVenta.setText("Total de Venta: $ " + miVenta.getCarrito().getTotal());
        }
    } catch (Exception e) {
        mostrarAlerta("Error", "Error al retroceder: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}
// Reinicia la Venta y la regresa al estado inicial para comenzar una nueva venta.
@FXML public void handleCancelarVenta() {
    try {
        miVenta.cancelar(); 
        resetearEstadoVistas();
        actualizarVistaSegunEstado();
        // Deshabilitar el botón de exportar PDF al reiniciar la venta
        if (btnExportarPDF != null) {
            btnExportarPDF.setDisable(true);
        }
    } catch (Exception e) {
        mostrarAlerta("Error", "Error al reiniciar: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}
// Actualiza la vista mostrada según el estado actual de la venta.
private void actualizarVistaSegunEstado() {
    String vistaID = miVenta.getEstadoActual().getVistaID();
    actualizarVisibilidadVistas(vistaID);
    miVenta.inicializarVistaActual(vistaMap);
    // Manejar solicitudes específicas del estado para la UI
    manejarSolicitudesUIDelEstado();
}
// Maneja las solicitudes específicas de UI que envían los estados.
private void manejarSolicitudesUIDelEstado() {
    String estadoActual = miVenta.getEstadoActual().getNombreEstado();
    configurarBotonVolver(estadoActual);
    configurarBotonSiguiente(estadoActual);
}
private void configurarBotonVolver(String estadoActual) {
    if (btnVolver == null) return;
    boolean mostrar = switch (estadoActual) {
        case "Agregar Productos" -> false; // No hay paso anterior
        case "Datos de Factura", "Confirmación de Pago" -> true;
        default -> true;
    };
    configurarVisibilidadBoton(btnVolver, mostrar);
}
private void configurarBotonSiguiente(String estadoActual) {
    if (btnSiguiente == null) return;
    
    boolean mostrar = switch (estadoActual) {
        case "Confirmación de Pago" -> false; // Último paso
        case "Agregar Productos", "Datos de Factura" -> true;
        default -> true;
    };
    configurarVisibilidadBoton(btnSiguiente, mostrar);
}
private void configurarVisibilidadBoton(Button boton, boolean visible) {
    boton.setVisible(visible);
    boton.setManaged(visible);
}
private void resetearEstadoVistas() {
    vistaDatosFacturaInicializada = false;
    vistaConfirmacionPagoInicializada = false;
    
    // Resetear las 3 vistas porque cancelar siempre vuelve al campo 1
    inicializarVistaAgregarProductos();
    limpiarInfoCliente();
    if (cbTipoFactura != null) cbTipoFactura.getSelectionModel().clearSelection();
    if (panelDatosCliente != null) panelDatosCliente.setVisible(false);
}
    
    // Actualiza el label de stock disponible en función del código ingresado
    private void actualizarStockSegunCodigo(String codigoIngresado) {
        if (lblStockDisponible == null) return;
        if (codigoIngresado == null || codigoIngresado.trim().isEmpty()) {
            lblStockDisponible.setText("");
            lblStockDisponible.setStyle("");
            return;
        }
        try {
            Producto p = buscarProductoPorCodigo(codigoIngresado.trim());
            if (p != null) {
                lblStockDisponible.setText("Stock: " + p.getStockProducto());
                String color = p.getStockProducto() > 0 ? "-fx-text-fill: #41d16d;" : "-fx-text-fill: #ff6b6b;";
                lblStockDisponible.setStyle(color);
            } else {
                lblStockDisponible.setText("Producto no encontrado");
                lblStockDisponible.setStyle("-fx-text-fill: #ff6b6b;");
            }
        } catch (Exception ex) {
            lblStockDisponible.setText("Error al buscar producto");
            lblStockDisponible.setStyle("-fx-text-fill: #ff6b6b;");
        }
    }
    // -------------------------------------------------------------------------
    // MÉTODOS DE UTILIDAD
    // -------------------------------------------------------------------------
    private void actualizarVisibilidadVistas(String nuevaVistaID) {
        // Ocultar todas las vistas
        vistaMap.values().forEach(node -> node.setVisible(false));
        Node vistaAMostrar = vistaMap.get(nuevaVistaID);
        
        if (vistaAMostrar != null) {
            miVenta.inicializarVistaActual(vistaMap);
            // Inicializaciones específicas que requieren acceso directo a componentes JavaFX
            if (nuevaVistaID.equals("FacturaDatosVenta")) {
                inicializarComponentesVistaDatos();
            } else if (nuevaVistaID.equals("FacturaConfirmarVenta")) {
                inicializarComponentesVistaConfirmacion();
            }
            vistaAMostrar.setVisible(true);
            System.out.println("Vista mostrada: " + nuevaVistaID);
        } else {
            System.err.println("Error FATAL: La vista " + nuevaVistaID + " no está mapeada.");
        }
    }
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    // Método utilitario para actualizar el estado del cliente de forma consistente
    private void actualizarEstadoCliente(String mensaje, boolean visible, boolean esError) {
        lblEstadoCliente.setText(mensaje);
        lblEstadoCliente.setStyle(esError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        lblEstadoCliente.setVisible(visible);
    }
    // Métodos auxiliares para inicialización específica de JavaFX
    private void inicializarComponentesVistaDatos() {
        if (!vistaDatosFacturaInicializada) {
            cbTipoFactura.setItems(FXCollections.observableArrayList("Factura", "Ticket"));
            cbTipoFactura.getSelectionModel().select("Factura");
            vistaDatosFacturaInicializada = true;
        }
        lblTotalVenta.setText("Total de Venta: $ " + miVenta.getCarrito().getTotal());
    }
    private void inicializarComponentesVistaConfirmacion() {
        if (!vistaConfirmacionPagoInicializada) {
            configurarComboBoxMetodosPago();
            vistaConfirmacionPagoInicializada = true;
        }
        actualizarResumenYSeleccionarPago();
    }
    private void inicializarVistaAgregarProductos() {
    if (carritoTable != null) { // 1. Limpiar visualmente la tabla
        carritoTable.getItems().clear();
    }
    if (lblTotalParcial != null) { // 2. Resetear el Label del Total Parcial a 0.00
        lblTotalParcial.setText("$ 0.00"); 
    }
    if (txtCodigoProducto != null) { // 3. Limpiar campos de entrada
        txtCodigoProducto.clear();
    }
    if (txtCantidad != null) {
        txtCantidad.clear();
    }
}
}