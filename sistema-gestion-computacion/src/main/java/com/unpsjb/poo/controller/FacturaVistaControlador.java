package com.unpsjb.poo.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.unpsjb.poo.model.Cliente; // Importa todas tus clases del Modelo
import com.unpsjb.poo.model.EstrategiaPago;
import com.unpsjb.poo.model.ItemCarrito; // Asume la ubicación de tu DAO // Asume que moviste la interfaz EstadoVenta a su propio paquete
import com.unpsjb.poo.model.PagoEfectivo;
import com.unpsjb.poo.model.PagoTarjeta;
import com.unpsjb.poo.model.Venta; // Importar FXMLLoader
import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaVentaUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML; // Necesario para la ventana emergente
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FacturaVistaControlador extends BaseControlador implements Initializable {

    private boolean vistaDatosFacturaInicializada = false;

    // 1. INYECCIÓN DE VISTAS MAESTRAS
    @FXML private StackPane contentStackPane;
    @FXML private Node FacturaAgregarProductos; 
    @FXML private Node FacturaDatosVenta;    
    @FXML private Node FacturaConfirmarVenta; 

    // 2. INYECCIÓN DE ELEMENTOS DEL PASO 1 (FacturaAgregarProductos.fxml)
    @FXML private TextField txtCodigoProducto; 
    @FXML private TextField txtCantidad; 
    @FXML private TableView<ItemCarrito> carritoTable; 
    @FXML private Label lblTotalParcial; 
    
    // 3. INYECCIÓN DE ELEMENTOS DEL PASO 2 (FacturaDatosVenta.fxml)
    @FXML private ComboBox <String> cbTipoFactura;
    @FXML private VBox panelDatosCliente; 
    @FXML private TextField txtCuitDni;
    @FXML private TextField txtRazonSocial;
    @FXML private Label lblEstadoCliente;
    @FXML private Label lblTotalVenta; 
    @FXML private Button btnCargarCliente;
    

    // 4. INYECCIÓN DE ELEMENTOS DEL PASO 3 (FacturaConfirmarVenta.fxml)
    @FXML private Label lblTipoFacturaResumen;
    @FXML private Label lblClienteResumen;
    @FXML private Label lblMontoSinIVAResumen;
    @FXML private Label lblIVAResumen;
    @FXML private Label lblTotalFinal;
    @FXML private Label lblComisionPago; // Para mostrar la comisión del pago
    @FXML private ScrollPane scrollPaneItems; // El contenedor para la lista de productos
    @FXML private ComboBox<EstrategiaPago> cbMetodoPago; // El ComboBox usará objetos EstrategiaPago
    @FXML private VBox vboxItemsLista;

    private EstrategiaPago estrategiaPagoSeleccionada; // Campo auxiliar para guardar la estrategia
    private boolean vistaConfirmacionPagoInicializada = false;
    private final AuditoriaVentaUtil auditoriaVentaUtil = new AuditoriaVentaUtil();
    
    
    
    // 5. MODELO DE DATOS Y ESTADO
    private Venta miVenta;
    private Map<String, Node> vistaMap;

    
   @Override
   public void initialize(URL url, ResourceBundle rb) {
    // 1. INICIALIZACIÓN DEL MODELO (Lógica de Negocio)
    
    // El constructor de Venta no recibe Empleado, según el código que me pasaste.
    miVenta = new Venta(); 
    
    // --------------------------------------------------------------------------
    
    // 2. MAPEO DE VISTAS (Debe hacerse después de que JavaFX inyectó los Nodos)
    
    // NOTA CLAVE: Las variables de inyección @FXML ya contienen sus valores aquí.
    vistaMap = new HashMap<>();
    
    // CORRECCIÓN: Los nombres de las claves deben coincidir con lo que devuelve estado.getVistaID()
    // Los valores deben usar las variables @FXML inyectadas.
    vistaMap.put("FacturaAgregarProductos", FacturaAgregarProductos); 
    vistaMap.put("FacturaDatosVenta", FacturaDatosVenta); 
    vistaMap.put("FacturaConfirmarVenta", FacturaConfirmarVenta); 

    // --------------------------------------------------------------------------

    // 3. CONFIGURACIÓN DE LA UI INICIAL
    
    // a. Configurar la tabla: enlazar la TableView con la lista observable del Carrito
    carritoTable.setItems(FXCollections.observableArrayList(miVenta.getCarrito().getItems()));
    
    // b. Mostrar la primera vista: Usar el ID del estado inicial (EstadoAgregarProductos)
    actualizarVisibilidadVistas(miVenta.getEstadoActual().getVistaID());
    
    // c. Actualizar el total parcial inicial (que es cero al comenzar)
    actualizarTotalParcial();

   }
   


/**
 * Recalcula el total del Carrito y actualiza el Label de la interfaz.
 */
private void actualizarTotalParcial() {
    // El total se obtiene directamente del Carrito, sin necesidad de guardarlo
    // Asume que tu getTotal() devuelve un tipo que se puede convertir a String.
    // Si devuelve BigDecimal, usa .toString() o .toPlainString().
    
    lblTotalParcial.setText("$ " + miVenta.getCarrito().getTotal());
}



    
    // -------------------------------------------------------------------------
    // MANEJO DE EVENTOS DE LA VISTA 1: CARRITO
    // -------------------------------------------------------------------------
@FXML
public void handleAnadirItem() {
    String codigo = txtCodigoProducto.getText();
    String cantidadStr = txtCantidad.getText();

    // 1. VALIDACIÓN BÁSICA
    if (codigo.isEmpty() || cantidadStr.isEmpty()) {
        mostrarAlerta("Datos Faltantes", "Debe ingresar el código y la cantidad del producto.", Alert.AlertType.WARNING);
        return;
    }

    try {
        int cantidad = Integer.parseInt(cantidadStr);
        
        if (cantidad <= 0) {
            mostrarAlerta("Cantidad Inválida", "La cantidad debe ser un número positivo.", Alert.AlertType.WARNING);
            return;
        }
        // 2. BÚSQUEDA SIMPLIFICADA - Usar el mismo método que el buscador
        List<Producto> resultados = Producto.buscarProductos(codigo);
        
        if (!resultados.isEmpty()) {
            Producto producto = resultados.get(0); // Tomar el primer resultado
            // 3. AGREGAR AL CARRITO
            miVenta.getCarrito().agregarItemAlCarrito(producto, cantidad);
            // 4. ACTUALIZAR INTERFAZ
            carritoTable.setItems(FXCollections.observableArrayList(miVenta.getCarrito().getItems()));
            actualizarTotalParcial();
            // Limpiar campos
            txtCodigoProducto.clear();
            txtCantidad.clear();
        } else {
            mostrarAlerta("Producto No Encontrado", "No existe un producto con el código ingresado.", Alert.AlertType.ERROR);
        }
        
    } catch (NumberFormatException e) {
        mostrarAlerta("Error de Formato", "La cantidad debe ser un número entero válido.", Alert.AlertType.WARNING);
    } catch (Exception e) {
        mostrarAlerta("Error de Sistema", "Ocurrió un error al procesar la solicitud.", Alert.AlertType.ERROR);
        e.printStackTrace();
    }
}
    
    @FXML
    public void handleQuitarItem() {
        ItemCarrito selectedItem = carritoTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            miVenta.getCarrito().eliminarItemDelCarrito(selectedItem); 
            carritoTable.setItems(FXCollections.observableArrayList(miVenta.getCarrito().getItems()));
            lblTotalParcial.setText("$ " + miVenta.getCarrito().getTotal());
        }
    }

    @FXML
    public void handleListarCodigos() {
        try {
            // Usar el mismo método optimizado de búsqueda que otros controladores
            List<Producto> listaProductos = Producto.buscarProductos(""); // Buscar todos
            mostrarVentanaCodigos(listaProductos);
        } catch (Exception e) {
            mostrarAlerta("Error de DB", "No se pudo cargar la lista de productos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    // --- LÓGICA DE VENTANA MODAL (Listado de Códigos) ---


    private void mostrarVentanaCodigos(List<Producto> productos) {
        try {
            // Usar el patrón crearVentana heredado de BaseControlador
            VentanaVistaControlador.ResultadoVentana resultado = crearVentana(
                "/view/CodigosListaVista.fxml", 
                "Lista de Códigos de Productos", 
                500, 
                450
            );

            if (resultado != null && resultado.getControlador() instanceof CodigosListaControlador) {
                CodigosListaControlador controlador = (CodigosListaControlador) resultado.getControlador();
                
                // Pasar la lista de productos al controlador
                controlador.setProductos(productos);
                
                // Configurar el controlador padre para recibir la selección
                controlador.setControladorPadre(this);

                // Mostrar la ventana
                resultado.getVentana().setVisible(true);
            }
        } catch (Exception e) {
            mostrarAlerta("Error de Vista", "No se pudo cargar la ventana de códigos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Método llamado por CodigosListaControlador cuando se selecciona un producto
    public void setCodigoProductoSeleccionado(int codigo) {
        txtCodigoProducto.setText(String.valueOf(codigo));
    }


    // -------------------------------------------------------------------------
    // MANEJO DE EVENTOS DE LA VISTA 2: FACTURA/TICKET
    // -------------------------------------------------------------------------
    
  
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
        lblEstadoCliente.setVisible(false);
    }


    @FXML
public void handleCargarCliente() {
    String cuit = txtCuitDni.getText();
    String nombre = txtRazonSocial.getText();
    
    // 1. Validación de campos obligatorios para Factura (CUIT y Nombre)
    if (cuit.isBlank() || nombre.isBlank()) {
        lblEstadoCliente.setText("ERROR: CUIT y Nombre/Razón Social son obligatorios.");
        lblEstadoCliente.setStyle("-fx-text-fill: red;"); // Mostrar el error en rojo
        lblEstadoCliente.setVisible(true);
        return;
    }

    try {
        // 2. Crear el objeto Cliente utilizando el constructor por defecto y setters
        // NOTA: Usar setters es más seguro porque tienen validación.
        Cliente cliente = new Cliente(); 
        
        // Asignar valores
        cliente.setNombre(nombre); 
        cliente.setCuit(cuit); // El setter de CUIT valida la longitud (11)
        cliente.setTipo("Responsable Inscripto"); // Definir el tipo para Factura

        // 3. Guardar el objeto Cliente en el Modelo (Venta)
        miVenta.setClienteFactura(cliente); 

        // 4. Actualizar UI
        lblEstadoCliente.setText("Cliente fiscal cargado exitosamente.");
        lblEstadoCliente.setStyle("-fx-text-fill: green;");
        lblEstadoCliente.setVisible(true);
        
        
    } catch (IllegalArgumentException e) {
        // Captura errores de validación, como CUIT inválido
        lblEstadoCliente.setText("ERROR de Datos: " + e.getMessage());
        lblEstadoCliente.setStyle("-fx-text-fill: red;");
        lblEstadoCliente.setVisible(true);
    }
}

// -------------------------------------------------------------------------
// MANEJO DE EVENTOS DE LA VISTA 3: CONFIRMACIÓN Y PAGO (Strategy)
// -------------------------------------------------------------------------

/**
 * Lógica de inicialización demorada para la VISTA 3.
 * Aquí se llena el ComboBox y se muestran los datos de resumen.
 */
private void inicializarVistaConfirmacionPago() {
    System.out.println("Inicializando Vista de Confirmación de Pago...");
    // 1. Chequeo CRÍTICO: Si el ComboBox es null, asumimos que toda la vista falló la inyección.
    if (cbMetodoPago == null) {
        System.err.println("Error: El ComboBox/Vista 3 no está inyectado. Revise el FXML.");
        return; 
    }
    
    if (!vistaConfirmacionPagoInicializada) {
        // Llenar el ComboBox con las estrategias de pago disponibles
        cbMetodoPago.setItems(FXCollections.observableArrayList(
            new PagoEfectivo(),
            new PagoTarjeta()
        ));
        
        // Configurar cómo se muestra cada estrategia en el ComboBox
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
        
        vistaConfirmacionPagoInicializada = true;
    }
    
    // 2. ACTUALIZACIÓN DE LABELS: Proteger la llamada a mostrarResumenVenta
    if (lblTotalFinal == null || lblComisionPago == null) {
        // Si fallan los labels, forzamos el fin para evitar el NullPointerException en handleMetodoPagoSelected()
        System.err.println("Error: Faltan inyecciones críticas de Label en Vista 3.");
        //return; 
    }
    
    // Si no es nulo, ejecutamos la lógica que actualiza los labels
    mostrarResumenVenta(); 
    cbMetodoPago.getSelectionModel().selectFirst();
    handleMetodoPagoSelected(); 
}
/**
 * Muestra los detalles finales (IVA y cliente) en la vista de resumen.
 */
private void mostrarResumenVenta() {
    // 1. OBTENER DATOS CALCULADOS DEL MODELO
    double subtotalConIVA = miVenta.getCarrito().getTotal().doubleValue();
    
    // Asumo que el 21% de IVA ya está incluido en el totalConIVA
    double iva = subtotalConIVA * 0.21; 
    double baseImponible = subtotalConIVA - iva; 
    
    // 2. OBTENER DATOS DE CLIENTE Y TIPO DE FACTURA
    Cliente cliente = miVenta.getClienteFactura();
    String nombreCliente = (cliente != null && cliente.getNombre() != null) 
                            ? cliente.getNombre() 
                            : "[Consumidor final o Razón social]";
    
    String tipoFactura = miVenta.getTipoFactura() != null 
                         ? miVenta.getTipoFactura() 
                         : "[Tipo no Seleccionado]";

    // 3. ACTUALIZAR LABELS DE RESUMEN (Vista 3)
    
    // A. Tipo de Factura y Cliente
    if (lblTipoFacturaResumen != null) {
        lblTipoFacturaResumen.setText(tipoFactura);
    }
    if (lblClienteResumen != null) {
        lblClienteResumen.setText(nombreCliente);
    }
    
    // B. Montos (Formateando a 2 decimales)
    if (lblMontoSinIVAResumen != null) {
        lblMontoSinIVAResumen.setText("Subtotal sin IVA: $ " + String.format("%.2f", baseImponible));
    }
    if (lblIVAResumen != null) {
        lblIVAResumen.setText("Monto IVA (21%): $ " + String.format("%.2f", iva));
    }
    
    // C. El Total Final se inicializa con el subtotal (handleMetodoPagoSelected lo ajustará)
    if (lblTotalFinal != null) {
        lblTotalFinal.setText("$ " + String.format("%.2f", subtotalConIVA));
    }

      if (vboxItemsLista != null) {
        vboxItemsLista.getChildren().clear();
        for (ItemCarrito item: miVenta.getCarrito().getItems()) {
            String itemTexto = String.format("%s - Cantidad: %d - Subtotal: $%.2f",
                item.getProducto().getNombreProducto(),
                item.getCantidad(),
                item.getSubtotal().doubleValue()
            );
            
            Label lblItem = new Label(itemTexto);
            lblItem.setStyle("-fx-padding: 5; -fx-font-size: 12px;");
            vboxItemsLista.getChildren().add(lblItem);
            if (scrollPaneItems != null) {
                System.out.println("Productos cargados. ScrollPaneItems no es nulo.");
                scrollPaneItems.setContent(vboxItemsLista);
        }
        }
    } 
        
    }

/**
 * Llamado por el ComboBox. Aplica la Estrategia de Pago seleccionada.
 */
@FXML
public void handleMetodoPagoSelected() {
    EstrategiaPago estrategia = cbMetodoPago.getSelectionModel().getSelectedItem();
    if (estrategia != null) {
        this.estrategiaPagoSeleccionada = estrategia;
        // Establecer la estrategia de pago en el modelo Venta
        miVenta.setEstrategiaPago(estrategia);

        if (lblComisionPago == null || lblTotalFinal == null) {
            System.err.println("Error: Faltan inyecciones críticas de Label en Vista 3.");
            return; 
        }
        
        double totalBase = miVenta.getCarrito().getTotal().doubleValue();
        
        // Simulación: La lógica real de Strategy aplicaría la comisión
        double comision = estrategia.getComision();
        double totalFinal = totalBase * (1 + comision);

        // Actualizar UI
        lblComisionPago.setText("Comision/Descuento: " + String.format("%.2f", comision * 100) + "%");
        lblTotalFinal.setText("$ " + String.format("%.2f", totalFinal));
    }
}


@FXML
public void handleRegistrarVenta() {
    if (estrategiaPagoSeleccionada == null) {
        mostrarAlerta("Error", "Debe seleccionar un método de pago.", Alert.AlertType.WARNING);
        return;
    }

    try {
     
        miVenta.siguientePaso();
        actualizarVisibilidadVistas(miVenta.getEstadoActual().getVistaID());
        inicializarVistaAgregarProductos();
        vistaDatosFacturaInicializada = false;
        vistaConfirmacionPagoInicializada = false;
        //-------------------
        // auditoriaaaaaaaaaaa
        //------------------------
auditoriaVentaUtil.registrarCreacion(miVenta);

        mostrarAlerta("Éxito", "Venta registrada y auditada correctamente.", Alert.AlertType.INFORMATION);

    } catch (Exception e) {
        System.err.println("Error al registrar venta: " + e.getMessage());
        e.printStackTrace();
        mostrarAlerta("Error", "No se pudo completar la venta: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}

/**
 * Llamado por el botón "Exportar PDF".
 */
@FXML
public void handleExportarPDF() {
    // Validar que haya datos para exportar
    if (miVenta.getCarrito() == null || miVenta.getCarrito().getItems().isEmpty()) {
        mostrarAlerta("Error", "No hay productos en el carrito para exportar.", Alert.AlertType.WARNING);
        return;
    }
    
    if (miVenta.getTipoFactura() == null) {
        mostrarAlerta("Error", "Debe seleccionar el tipo de factura antes de exportar.", Alert.AlertType.WARNING);
        return;
    }
    
    if (miVenta.getEstrategiaPago() == null) {
        mostrarAlerta("Error", "Debe seleccionar un método de pago antes de exportar.", Alert.AlertType.WARNING);
        return;
    }
    
    try {

        // Crear el generador de PDF
        com.unpsjb.poo.util.Exporter_pdf.PDFFactura pdfGenerator = new com.unpsjb.poo.util.Exporter_pdf.PDFFactura(miVenta);

        // Generar nombre de archivo
        String tipoDoc = "FACTURA".equals(miVenta.getTipoFactura()) ? "Factura" : "Ticket";
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String fileName = tipoDoc + "_" + timestamp + ".pdf";
        String filePath = System.getProperty("user.home") + "/" + fileName;
        
        // Exportar el PDF
        boolean success = pdfGenerator.export(filePath);
        
        if (success) {
            mostrarAlerta("Éxito", 
                "PDF generado exitosamente en:\n" + filePath, 
                Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", 
                "No se pudo generar el PDF. Verifique los datos de la venta.", 
                Alert.AlertType.ERROR);
        }
        
    } catch (Exception e) {
        System.err.println("Error al exportar PDF: " + e.getMessage());
        e.printStackTrace();
        mostrarAlerta("Error", 
            "Error al generar el PDF: " + e.getMessage(), 
            Alert.AlertType.ERROR);
    }
}   
    // -------------------------------------------------------------------------
    // LÓGICA DE NAVEGACIÓN (PATRÓN STATE)
    // -------------------------------------------------------------------------
    
    /**
 * Llamado por el botón "Siguiente" en el BorderPane.
 * Avanza la Venta al siguiente estado secuencial (Paso 1 -> Paso 2 -> Paso 3).
 */
@FXML
public void handleSiguientePaso() {
    // 1. Ejecutar el avance del Modelo. El estado CAMBIA aquí (Paso 1 -> Paso 2).
    // NOTA: Si comentaste la validación en EstadoAgregarProductos, esto siempre avanza.
    miVenta.siguientePaso(); 
    
    // 2. Obtener el ID de la NUEVA vista (Ahora es FacturaDatosVenta)
    String nuevaVistaID = miVenta.getEstadoActual().getVistaID();
    
    
    // 4. El Controlador actualiza la UI para mostrar el Paso 2
    actualizarVisibilidadVistas(nuevaVistaID);
}

/**
 * Llamado por el botón "Atrás" en el BorderPane.
 * Retrocede la Venta al estado anterior.
 */
@FXML
public void handleVolverPaso() {
    // 1. El Modelo retrocede al estado anterior (el Estado actual contiene el método volverPaso)
    miVenta.getEstadoActual().volverPaso(miVenta);
    
    // 2. El Controlador actualiza la UI
    String nuevaVistaID = miVenta.getEstadoActual().getVistaID();
    actualizarVisibilidadVistas(nuevaVistaID);

    if ("VistaDatosFactura".equals(nuevaVistaID)) {
    
        lblTotalVenta.setText("Total de Venta: $ " + miVenta.getCarrito().getTotal());
    }
}

/**
 * Llamado por el botón "Cancelar Venta" en el BorderPane.
 * Cancela la Venta y la regresa al estado inicial (Agregar Productos).
 */
@FXML
public void handleCancelarVenta() {
    // 1. Limpieza de datos en el Modelo
    // Esto vacía el carrito y resetea el estado a EstadoAgregarProductos
    miVenta.cancelar(); 
    
    // 2. El Controlador actualiza la UI al Paso 1
    String nuevaVistaID = miVenta.getEstadoActual().getVistaID();
    actualizarVisibilidadVistas(nuevaVistaID); 
    
    // 3. LIMPIEZA DE LA VISTA 1: Llamar al nuevo método auxiliar
    // Esto garantiza que la tabla se vacíe y el Label del Total se ponga en $0.00
    if ("VistaAgregarProductos".equals(nuevaVistaID)) {
        inicializarVistaAgregarProductos(); 
    }

       actualizarVisibilidadVistas(nuevaVistaID); 

}
    
    // -------------------------------------------------------------------------
    // MÉTODOS DE UTILIDAD
    // -------------------------------------------------------------------------

private void inicializarVistaDatosFactura() {
        if (!vistaDatosFacturaInicializada) {
            // Lógica para llenar el ComboBox (SOLO se ejecuta la primera vez)
            cbTipoFactura.setItems(FXCollections.observableArrayList("Factura", "Ticket"));
            cbTipoFactura.getSelectionModel().select("Factura");
            
            // Lógica para limpiar el total inicial y otros elementos
            lblTotalVenta.setText("Total de Venta: $ " + miVenta.getCarrito().getTotal());
            
            vistaDatosFacturaInicializada = true; // MARCAR como hecho
        } else {
            lblTotalVenta.setText("Total de Venta: $ " + miVenta.getCarrito().getTotal());
        }
    }

 private void actualizarVisibilidadVistas(String nuevaVistaID) {
    // Ocultar todas las vistas
    vistaMap.values().forEach(node -> node.setVisible(false));
    
    Node vistaAMostrar = vistaMap.get(nuevaVistaID);
    
    if (vistaAMostrar != null) {

        if (nuevaVistaID.equals("FacturaDatosVenta")) {
            inicializarVistaDatosFactura();

        } else if (nuevaVistaID.equals("FacturaConfirmarVenta")) {
            inicializarVistaConfirmacionPago();
        }

        // **********************************************
        
        vistaAMostrar.setVisible(true);
        // ... (resto de impresiones y lógica)
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

    private void inicializarVistaAgregarProductos() {
    // 1. Limpiar visualmente la tabla (aunque el modelo esté limpio, la tabla debe reflejarlo)
    if (carritoTable != null) {
        carritoTable.getItems().clear();
    }
    
    // 2. Resetear el Label del Total Parcial a 0.00
    if (lblTotalParcial != null) { 
        lblTotalParcial.setText("$ 0.00"); 
    }
    
    // 3. Limpiar campos de entrada
    if (txtCodigoProducto != null) {
        txtCodigoProducto.clear();
    }
    if (txtCantidad != null) {
        txtCantidad.clear();
    }
}

}