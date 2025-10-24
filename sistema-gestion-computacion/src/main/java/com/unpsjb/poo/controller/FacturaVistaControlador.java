package com.unpsjb.poo.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Optional; // Necesario para buscar productos

import com.unpsjb.poo.model.*; // Importa todas tus clases del Modelo
import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl; // Asume la ubicación de tu DAO // Asume que moviste la interfaz EstadoVenta a su propio paquete

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Importar FXMLLoader
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene; // Necesario para la ventana emergente
import javafx.scene.control.*; // Importar todos los controles
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality; // Necesario para la ventana modal
import javafx.stage.Stage; // Necesario para la ventana emergente

public class FacturaVistaControlador implements Initializable {

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
   // @FXML private CheckBox cbFacturaA; // Antes estaba mal nombrado como cbFactura
    //@FXML private CheckBox cbFacturaB; // Antes estaba mal nombrado como cbTicket
    @FXML private ComboBox <String> cbTipoFactura;
    @FXML private VBox panelDatosCliente; 
    @FXML private TextField txtCuitDni;
    @FXML private TextField txtRazonSocial;
    @FXML private Label lblEstadoCliente;
    @FXML private Label lblTotalVenta; 

    // 4. INYECCIÓN DE ELEMENTOS DEL PASO 3 (FacturaConfirmarVenta.fxml)
    @FXML private Label lblTipoFacturaResumen;
    @FXML private Label lblClienteResumen;
    @FXML private Label lblMontoSinIVAResumen;
    @FXML private Label lblIVAResumen;
    @FXML private Label lblTotalFinal;
    @FXML private Label lblComision; // Para mostrar la comisión del pago
    @FXML private ScrollPane scrollPaneItems; // El contenedor para la lista de productos
    @FXML private ComboBox<EstrategiaPago> cbMetodoPago; // El ComboBox usará objetos EstrategiaPago

    private EstrategiaPago estrategiaPagoSeleccionada; // Campo auxiliar para guardar la estrategia
    
    // 5. MODELO DE DATOS Y ESTADO
    private Venta miVenta;
    private Map<String, Node> vistaMap; 
    private ProductoDAOImpl productoDAO; // Instancia del DAO para buscar productos

    
   @Override
   public void initialize(URL url, ResourceBundle rb) {
    // 1. INICIALIZACIÓN DEL MODELO Y DAO (Lógica de Negocio)
    
    // El constructor de Venta no recibe Empleado, según el código que me pasaste.
    miVenta = new Venta(); 
    
    // Inicializar el DAO
    productoDAO = new ProductoDAOImpl(); 
    
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
        try {
            String codigo = txtCodigoProducto.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());

            // Buscar producto por código (asumiendo que DAO tiene este método)
            Optional<Producto> productoOpt = productoDAO.findByCodigo(codigo); 

            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                // CORRECCIÓN 3: Asumiendo que CarritoDeCompra ya maneja el agregar item.
                miVenta.getCarrito().agregarItemAlCarrito(producto, cantidad); 
                
                // Actualizar la UI
                carritoTable.setItems(FXCollections.observableArrayList(miVenta.getCarrito().getItems()));
                // Actualizar Total
                lblTotalParcial.setText("$ " + miVenta.getCarrito().getTotal()); 
                
                txtCodigoProducto.clear();
                txtCantidad.clear();
            } else {
                mostrarAlerta("Producto no encontrado", "No existe un producto con el código ingresado.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de entrada", "La cantidad debe ser un número entero.", Alert.AlertType.WARNING);
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
            List<Producto> listaProductos = productoDAO.findAll();
            mostrarVentanaCodigos(listaProductos);
        } catch (Exception e) {
            mostrarAlerta("Error de DB", "No se pudo cargar la lista de productos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    // --- LÓGICA DE VENTANA MODAL (Listado de Códigos) ---

    private void mostrarVentanaCodigos(List<Producto> productos) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CodigosListaVista.fxml"));
            Parent root = loader.load();

            CodigosListaControlador controlador = loader.getController();
            controlador.setProductos(productos); // Pasa la lista

            Stage stage = new Stage();
            stage.setTitle("Lista de Códigos de Productos");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Recibe el código seleccionado
            if (controlador.getCodigoSeleccionado() != null) {
                txtCodigoProducto.setText(controlador.getCodigoSeleccionado().toString());
            }
        } catch (Exception e) {
            mostrarAlerta("Error de Vista", "No se pudo cargar la ventana de códigos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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


    /* 
    @FXML
    public void handleCargarCliente() {
        if (txtCuitDni.getText().isEmpty() || txtRazonSocial.getText().isEmpty()) {
            lblEstadoCliente.setText("ERROR: CUIT/Nombre no pueden estar vacíos.");
            lblEstadoCliente.setVisible(true);
            return;
        }

        // Asumiendo que Cliente tiene un constructor para CUIT y Nombre
        Cliente cliente = new Cliente(txtCuitDni.getText(), txtRazonSocial.getText());
        miVenta.setClienteFactura(cliente); 

        lblEstadoCliente.setText("Cliente cargado exitosamente.");
        lblEstadoCliente.setVisible(true);
    }
    */

// -------------------------------------------------------------------------
// MANEJO DE EVENTOS DE LA VISTA 3: CONFIRMACIÓN Y PAGO (Strategy)
// -------------------------------------------------------------------------

/**
 * Lógica de inicialización demorada para la VISTA 3.
 * Aquí se llena el ComboBox y se muestran los datos de resumen.
 */
private void inicializarVistaConfirmacionPago() {
    // 1. Cargar las Estrategias de Pago en el ComboBox
    List<EstrategiaPago> estrategias = new ArrayList<>();
    estrategias.add(new PagoEfectivo());
    estrategias.add(new PagoTarjeta()); 
    
    cbMetodoPago.setItems(FXCollections.observableArrayList(estrategias));

    // 2. Mostrar resumen (IVA, Cliente, Totales)
    mostrarResumenVenta();

    // 3. Seleccionar la primera opción por defecto
    cbMetodoPago.getSelectionModel().selectFirst();
    handleMetodoPagoSelected(); // Llamar a la lógica de pago para calcular el total inicial
}

/**
 * Muestra los detalles finales (IVA y cliente) en la vista de resumen.
 */
private void mostrarResumenVenta() {
    double subtotal = miVenta.getCarrito().getTotal().doubleValue();
    double iva = subtotal * 0.21; // 21% de IVA asumido
    double baseImponible = subtotal - iva; // Cálculo simple
    
    // Actualizar Labels de Resumen
    lblTipoFacturaResumen.setText(miVenta.getTipoFactura());
    lblMontoSinIVAResumen.setText("$ " + String.format("%.2f", baseImponible));
    lblIVAResumen.setText("$ " + String.format("%.2f", iva));
    //lblClienteResumen.setText(miVenta.getClienteFactura() != null ? miVenta.getClienteFactura().getRazonSocial() : "Consumidor Final");
    
    // (Opcional) Llenar el ScrollPane con Labels para cada ItemCarrito
    // ...
}

/**
 * Llamado por el ComboBox. Aplica la Estrategia de Pago seleccionada.
 */
@FXML
public void handleMetodoPagoSelected() {
    EstrategiaPago estrategia = cbMetodoPago.getSelectionModel().getSelectedItem();
    if (estrategia != null) {
        this.estrategiaPagoSeleccionada = estrategia;
        
        double totalBase = miVenta.getCarrito().getTotal().doubleValue();
        double comision = estrategia.getComision();
        double totalFinal = totalBase * (1 + comision);

        // Actualizar UI
        lblComision.setText("Comision/Descuento: " + String.format("%.2f", comision * 100) + "%");
        lblTotalFinal.setText("$ " + String.format("%.2f", totalFinal));
    }
}

/**
 * Llamado por el botón "Registrar venta". FINALIZA el State y la transacción.
 */
@FXML
public void handleRegistrarVenta() {
    if (estrategiaPagoSeleccionada == null) {
        mostrarAlerta("Error", "Debe seleccionar un método de pago.", Alert.AlertType.WARNING);
        return;
    }
    
    // Establecer la estrategia de pago en la venta
    miVenta.setEstrategiaPago(estrategiaPagoSeleccionada);
    
    // El controlador invoca la lógica de PAGO y PERSISTENCIA en el Modelo (EstadoConfirmacionPago)
    miVenta.siguientePaso(); 

    // El Patrón State mueve la Venta al estado inicial (Agregar Productos)
    // El Controlador actualiza la UI para volver al inicio
    actualizarVisibilidadVistas(miVenta.getEstadoActual().getVistaID());
    
    // Actualizar la tabla del carrito
    carritoTable.setItems(FXCollections.observableArrayList(miVenta.getCarrito().getItems()));
    actualizarTotalParcial();
    
    mostrarAlerta("Éxito", "Venta registrada exitosamente.", Alert.AlertType.INFORMATION);
}

/**
 * Llamado por el botón "Exportar PDF".
 */
@FXML
public void handleExportarPDF() {
    // Lógica para exportar el PDF (asumiendo que la clase PDFExporter existe)
    // PDFExporter.exportar(miVenta);
    mostrarAlerta("Exportar", "Generando PDF de la factura...", Alert.AlertType.INFORMATION);
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
}

/**
 * Llamado por el botón "Cancelar Venta" en el BorderPane.
 * Cancela la Venta y la regresa al estado inicial (Agregar Productos).
 */
@FXML
public void handleCancelarVenta() {
    // La clase Venta asume que tiene un método cancelar() que hace la limpieza
    miVenta.cancelar(); 
    
    // Reinicia el proceso y la UI
    actualizarVisibilidadVistas(miVenta.getEstadoActual().getVistaID()); 
    
    // Opcional: limpiar los campos de texto
    txtCodigoProducto.clear();
    txtCantidad.clear();
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
            lblTotalVenta.setText("Total de Venta: $ 0.00");
            
            vistaDatosFacturaInicializada = true; // MARCAR como hecho
        }
    }

 private void actualizarVisibilidadVistas(String nuevaVistaID) {
    // Ocultar todas las vistas
    vistaMap.values().forEach(node -> node.setVisible(false));
    
    Node vistaAMostrar = vistaMap.get(nuevaVistaID);
    
    if (vistaAMostrar != null) {
        
        // **********************************************
        // LÓGICA DE INICIALIZACIÓN DEMORADA (AQUÍ DENTRO)
        // **********************************************
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
}