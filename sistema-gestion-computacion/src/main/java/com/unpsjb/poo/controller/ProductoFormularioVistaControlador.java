package com.unpsjb.poo.controller;

import java.math.BigDecimal;
import java.util.List;

import com.unpsjb.poo.model.productos.Categoria;
import com.unpsjb.poo.model.productos.Fabricante;
import com.unpsjb.poo.model.productos.Producto;
import com.unpsjb.poo.model.productos.ProductoDigital;
import com.unpsjb.poo.model.productos.ProductoFisico;
import com.unpsjb.poo.model.productos.ProveedorDigital;
import com.unpsjb.poo.util.Sesion;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaProductoUtil;
import com.unpsjb.poo.util.cap_auditoria.AuditoriaUtil;
import com.unpsjb.poo.util.copias.CopiarProductoUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ProductoFormularioVistaControlador extends BaseControlador {

    @FXML private javafx.scene.layout.VBox vboxPrincipal;
    @FXML private ChoiceBox<String> cbTipoProducto;
    @FXML private TextField txtNombre, txtDescripcion, txtCodigo, txtPrecio, txtStock;
    @FXML private ChoiceBox<Categoria> cbCategoria;
    
    // Campos específicos para Producto Físico
    @FXML private Label lblFabricante, lblEstadoFisico, lblGarantia, lblTipoGarantia;
    @FXML private HBox hboxFabricante;
    @FXML private ChoiceBox<Fabricante> cbFabricante;
    @FXML private ChoiceBox<String> cbEstadoFisico, cbTipoGarantia;
    @FXML private TextField txtGarantiaMeses;
    
    // Campos específicos para Producto Digital
    @FXML private Label lblProveedor, lblTipoLicencia, lblActivaciones, lblDuracion;
    @FXML private HBox hboxProveedor;
    @FXML private ChoiceBox<ProveedorDigital> cbProveedorDigital;
    @FXML private ChoiceBox<String> cbTipoLicencia;
    @FXML private TextField txtActivacionesMax, txtDuracionDias;

    private Producto productoAEditar;
    private Producto productoOriginal;
    private ProductosVistaControlador productosVista;

    @FXML
    private void initialize() {
        try {
            cargarCategorias();
            configurarTipoProducto();
            cargarFabricantes();
            cargarProveedoresDigitales();
            configurarEnums();
        } catch (Exception e) {
            System.err.println("Error al inicializar el formulario de productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTipoProducto() {
        cbTipoProducto.getItems().addAll("GENÉRICO", "FÍSICO", "DIGITAL");
        cbTipoProducto.setValue("GENÉRICO");
        
        cbTipoProducto.valueProperty().addListener((obs, oldVal, newVal) -> {
            mostrarCamposSegunTipo(newVal);
        });
    }
    
    private void configurarEnums() {
        // Estados físicos
        cbEstadoFisico.getItems().addAll("NUEVO", "USADO", "REACONDICIONADO");
        // Tipos de garantía
        cbTipoGarantia.getItems().addAll("FABRICANTE", "TIENDA");
        // Tipos de licencia
        cbTipoLicencia.getItems().addAll("PERPETUA", "SUSCRIPCION", "TRIAL");
    }
    // Con el java.util.List.of() ahorro setters repetidos
    private void mostrarCamposSegunTipo(String tipo) {
        // Ocultar todos los campos específicos primero
        java.util.List.of(lblFabricante, hboxFabricante, lblEstadoFisico, cbEstadoFisico,
                lblGarantia, txtGarantiaMeses, lblTipoGarantia, cbTipoGarantia,
                lblProveedor, hboxProveedor, lblTipoLicencia, cbTipoLicencia,
                lblActivaciones, txtActivacionesMax, lblDuracion, txtDuracionDias)
                .forEach(node -> { node.setVisible(false); node.setManaged(false); });
        
        // Mostrar campos según el tipo
        if ("FÍSICO".equals(tipo)) {
            java.util.List.of(lblFabricante, hboxFabricante, lblEstadoFisico, cbEstadoFisico, 
                    lblGarantia, txtGarantiaMeses, lblTipoGarantia, cbTipoGarantia)
                    .forEach(node -> { node.setVisible(true); node.setManaged(true); });
            ajustarTamañoVentana(600.0);
        } else if ("DIGITAL".equals(tipo)) {
            java.util.List.of(lblProveedor, hboxProveedor, lblTipoLicencia, cbTipoLicencia,
                    lblActivaciones, txtActivacionesMax, lblDuracion, txtDuracionDias)
                    .forEach(node -> { node.setVisible(true); node.setManaged(true); });
            ajustarTamañoVentana(600.0);
        } else {
            // Producto genérico - ventana más pequeña
            ajustarTamañoVentana(300.0);
        }
    }
    
    private void ajustarTamañoVentana(double altura) {
        if (vboxPrincipal != null) {
            vboxPrincipal.setPrefHeight(altura);
        }
    }

    public void cargarCategorias(){
        try {
            List<Categoria> categorias = Categoria.obtenerTodas();
            if (categorias != null && !categorias.isEmpty()) {
                cbCategoria.getItems().clear();
                cbCategoria.getItems().addAll(categorias);
            } else {
                System.out.println("No se encontraron categorías en la base de datos");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar categorías: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void cargarFabricantes() {
        try {
            List<Fabricante> fabricantes = Fabricante.obtenerTodos();
            if (fabricantes != null && !fabricantes.isEmpty()) {
                cbFabricante.getItems().clear();
                cbFabricante.getItems().addAll(fabricantes);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar fabricantes: " + e.getMessage());
        }
    }
    
    public void cargarProveedoresDigitales() {
        try {
            List<ProveedorDigital> proveedores = ProveedorDigital.obtenerTodos();
            if (proveedores != null && !proveedores.isEmpty()) {
                cbProveedorDigital.getItems().clear();
                cbProveedorDigital.getItems().addAll(proveedores);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar proveedores digitales: " + e.getMessage());
        }
    }

    public void setProducto(Producto p) {
        this.productoAEditar = p;
        if (p != null) {
            this.productoOriginal = CopiarProductoUtil.copiarProducto(p);
            cargarDatosEnCampos(p);
        }
    }

    public void setProductoAEditar(Producto p) {setProducto(p);}

    public void setControladorPadre(ProductosVistaControlador productosVista) {
        this.productosVista = productosVista;
    }

    private void cargarDatosEnCampos(Producto producto) {
        if (producto != null) {
            txtCodigo.setText(String.valueOf(producto.getCodigoProducto()));
            txtNombre.setText(producto.getNombreProducto());
            txtDescripcion.setText(producto.getDescripcionProducto());
            cbCategoria.setValue(producto.getCategoria());
            txtPrecio.setText(producto.getPrecioProducto() != null ? producto.getPrecioProducto().toPlainString() : "");
            txtStock.setText(String.valueOf(producto.getStockProducto()));
            
            String tipo = producto.obtenerTipoProducto();
            if ("FISICO".equals(tipo)) {
                cbTipoProducto.setValue("FÍSICO");
                ProductoFisico pf = ProductoFisico.obtenerPorId(producto.getIdProducto());
                if (pf != null) {
                    cbFabricante.setValue(pf.getFabricante());
                    if (pf.getEstadoFisico() != null) {
                        cbEstadoFisico.setValue(pf.getEstadoFisico().name());
                    }
                    if (pf.getGarantiaMeses() != null) {
                        txtGarantiaMeses.setText(String.valueOf(pf.getGarantiaMeses()));
                    }
                    if (pf.getTipoGarantia() != null) {
                        cbTipoGarantia.setValue(pf.getTipoGarantia().name());
                    }
                }
            } else if ("DIGITAL".equals(tipo)) {
                cbTipoProducto.setValue("DIGITAL");
                ProductoDigital pd = ProductoDigital.obtenerPorId(producto.getIdProducto());
                if (pd != null) {
                    cbProveedorDigital.setValue(pd.getProveedorDigital());
                    if (pd.getTipoLicencia() != null) {
                        cbTipoLicencia.setValue(pd.getTipoLicencia().name());
                    }
                    if (pd.getActivacionesMax() != null) {
                        txtActivacionesMax.setText(String.valueOf(pd.getActivacionesMax()));
                    }
                    if (pd.getDuracionLicenciaDias() != null) {
                        txtDuracionDias.setText(String.valueOf(pd.getDuracionLicenciaDias()));
                    }
                }
            } else {
                cbTipoProducto.setValue("GENÉRICO");
            }
        }
    }

//------------------------
@FXML
private void guardarProducto() {
    try {
        // Validaciones básicas
        if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() ||
            txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty() ||
            cbCategoria.getValue() == null) {
            mostrarAlerta("Todos los campos básicos son obligatorios.");
            return;
        }

        String usuario = (Sesion.getUsuarioActual() != null)
                ? Sesion.getUsuarioActual().getNombre()
                : "Desconocido";

        String tipoSeleccionado = cbTipoProducto.getValue();
        boolean ok = false;
        String mensajeUsuario = "";

        // ========================================================
        // CREACIÓN DE NUEVO PRODUCTO
        // ========================================================
        if (productoAEditar == null) {
            Producto nuevo = switch (tipoSeleccionado) {
                case "FÍSICO" -> new ProductoFisico();
                case "DIGITAL" -> new ProductoDigital();
                default -> new Producto();
            };

            guardarDatosEnProducto(nuevo);

            if (nuevo instanceof ProductoFisico pf) {
                guardarDatosFisicos(pf);
                ok = pf.guardarFisico();
            } else if (nuevo instanceof ProductoDigital pd) {
                guardarDatosDigitales(pd);
                ok = pd.guardarDigital();
            } else {
                ok = nuevo.guardar();
            }
      // auditoria
            if (ok) {
                String tipo = tipoSeleccionado.toUpperCase();
                AuditoriaUtil.registrarAccion(
                        "CREAR PRODUCTO", "producto",
                        " creó un producto " + tipo + ": '" + nuevo.getNombreProducto() + "'.");
                mensajeUsuario = "Se creó un nuevo producto " + tipo + ":\n" + nuevo.getNombreProducto();
            }

        // ========================================================
        //  MODIFICACIÓN DE PRODUCTO EXISTENTE
        // ========================================================
        } else {
            // Clonar estado original antes de modificar
            Producto original = CopiarProductoUtil.copiarProducto(productoOriginal);

            Producto actualizado = switch (productoAEditar.obtenerTipoProducto()) {
                case "FISICO" -> ProductoFisico.obtenerPorId(productoAEditar.getIdProducto());
                case "DIGITAL" -> ProductoDigital.obtenerPorId(productoAEditar.getIdProducto());
                default -> productoAEditar;
            };

            guardarDatosEnProducto(actualizado);

            if (actualizado instanceof ProductoFisico pf) {
                guardarDatosFisicos(pf);
                ok = pf.guardarFisico();
            } else if (actualizado instanceof ProductoDigital pd) {
                guardarDatosDigitales(pd);
                ok = pd.guardarDigital();
            } else {
                ok = actualizado.actualizar();
            }

            if (ok) {
                String resumen = AuditoriaProductoUtil.generarResumenCambios(original, actualizado);
                if (!resumen.isEmpty()) {
                    AuditoriaProductoUtil.registrarCambioProducto(original, actualizado);
                    mensajeUsuario = "Producto modificado:\n" + resumen;
                } else {
                    mensajeUsuario = "No hubo cambios relevantes en el producto.";
                }
            }
        }

        if (ok) {
            mostrarAlerta(mensajeUsuario);
            if (productosVista != null) {
                productosVista.cargarProductos();
            }
            cerrarVentana();
        } else {
            mostrarAlerta("Error al guardar el producto. Ver consola.");
        }

    } catch (NumberFormatException nfe) {
        mostrarAlerta("Formato numérico incorrecto (precio, stock, etc).");
    } catch (Exception e) {
        mostrarAlerta("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
}


    private void guardarDatosEnProducto(Producto producto) {
        if (producto != null) {
            try {
                producto.setCodigoProducto(Integer.parseInt(txtCodigo.getText().trim()));
                producto.setNombreProducto(txtNombre.getText().trim());
                producto.setDescripcionProducto(txtDescripcion.getText() == null ? "" : txtDescripcion.getText().trim());
                producto.setStockProducto(Integer.parseInt(txtStock.getText().trim()));
                producto.setPrecioProducto(new BigDecimal(txtPrecio.getText().trim().replace(',', '.')));
                producto.setCategoria(cbCategoria.getValue());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Error en el formato de los campos numéricos");
            }
        }
    }
    
    private void guardarDatosFisicos(ProductoFisico producto) {
        producto.setFabricante(cbFabricante.getValue());
        
        if (cbEstadoFisico.getValue() != null) {
            producto.setEstadoFisico(ProductoFisico.EstadoFisico.valueOf(cbEstadoFisico.getValue()));
        }
        if (txtGarantiaMeses.getText() != null && !txtGarantiaMeses.getText().trim().isEmpty()) {
            producto.setGarantiaMeses(Integer.parseInt(txtGarantiaMeses.getText().trim()));
        }
        if (cbTipoGarantia.getValue() != null) {
            producto.setTipoGarantia(ProductoFisico.TipoGarantia.valueOf(cbTipoGarantia.getValue()));
        }
    }
    
    private void guardarDatosDigitales(ProductoDigital producto) {
        producto.setProveedorDigital(cbProveedorDigital.getValue());
        
        if (cbTipoLicencia.getValue() != null) {
            producto.setTipoLicencia(ProductoDigital.TipoLicencia.valueOf(cbTipoLicencia.getValue()));
        }
        if (txtActivacionesMax.getText() != null && !txtActivacionesMax.getText().trim().isEmpty()) {
            producto.setActivacionesMax(Integer.parseInt(txtActivacionesMax.getText().trim()));
        }
        if (txtDuracionDias.getText() != null && !txtDuracionDias.getText().trim().isEmpty()) {
            producto.setDuracionLicenciaDias(Integer.parseInt(txtDuracionDias.getText().trim()));
        }
    }

    @FXML
    public void agregarCategoria() {
        try {
            crearVentanaPequena("/view/formularios/CategoriaForm.fxml", "Agregar Nueva Categoría");
            cargarCategorias();
        } catch (Exception e) {
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }
    
    @FXML
    private void agregarFabricante() {
        try {
            VentanaVistaControlador.ResultadoVentana resultado = crearVentanaPequena("/view/formularios/FabricanteForm.fxml", "Agregar Nuevo Fabricante");
            if (resultado != null && resultado.getControlador() != null) {
                FabricanteFormularioVistaControlador controlador = (FabricanteFormularioVistaControlador) resultado.getControlador();
                controlador.setControladorPadre(this);
            }
        } catch (Exception e) {
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }
    
    @FXML
    private void modificarFabricante() {
        Fabricante seleccionado = cbFabricante.getValue();
        if (seleccionado == null) {
            mostrarAlerta("Debe seleccionar un fabricante para modificar.");
            return;
        }

        try {
            VentanaVistaControlador.ResultadoVentana resultado = crearVentanaPequena("/view/formularios/FabricanteForm.fxml", "Modificar Fabricante");
            if (resultado != null && resultado.getControlador() != null) {
                FabricanteFormularioVistaControlador controlador = (FabricanteFormularioVistaControlador) resultado.getControlador();
                controlador.setFabricanteAEditar(seleccionado);
                controlador.setControladorPadre(this);
            }
        } catch (Exception e) {
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }
    
    @FXML
    private void eliminarFabricante() {
        Fabricante seleccionado = cbFabricante.getValue();
        if (seleccionado == null) {
            mostrarAlerta("Debe seleccionar un fabricante para eliminar.");
            return;
        }

        if (seleccionado.eliminar()) {
            mostrarAlerta("Fabricante eliminado correctamente.");
            cargarFabricantes();
            cbFabricante.setValue(null);
        } else {
            mostrarAlerta("Error al eliminar el fabricante. Puede estar en uso por productos.");
        }
    }
    
    @FXML
    private void agregarProveedorDigital() {
        try {
            VentanaVistaControlador.ResultadoVentana resultado = crearVentanaPequena("/view/formularios/ProveedorDigitalForm.fxml", "Agregar Nuevo Proveedor Digital");
            if (resultado != null && resultado.getControlador() != null) {
                ProveedorDigitalFormularioVistaControlador controlador = (ProveedorDigitalFormularioVistaControlador) resultado.getControlador();
                controlador.setControladorPadre(this);
            }
        } catch (Exception e) {
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }
    
    @FXML
    private void modificarProveedorDigital() {
        ProveedorDigital seleccionado = cbProveedorDigital.getValue();
        if (seleccionado == null) {
            mostrarAlerta("Debe seleccionar un proveedor digital para modificar.");
            return;
        }

        try {
            VentanaVistaControlador.ResultadoVentana resultado = crearVentanaPequena("/view/formularios/ProveedorDigitalForm.fxml", "Modificar Proveedor Digital");
            if (resultado != null && resultado.getControlador() != null) {
                ProveedorDigitalFormularioVistaControlador controlador = (ProveedorDigitalFormularioVistaControlador) resultado.getControlador();
                controlador.setProveedorDigitalAEditar(seleccionado);
                controlador.setControladorPadre(this);
            }
        } catch (Exception e) {
            mostrarAlerta("Error al abrir el formulario: " + e.getMessage());
        }
    }
    
    @FXML
    private void eliminarProveedorDigital() {
        ProveedorDigital seleccionado = cbProveedorDigital.getValue();
        if (seleccionado == null) {
            mostrarAlerta("Debe seleccionar un proveedor digital para eliminar.");
            return;
        }

        if (seleccionado.eliminar()) {
            mostrarAlerta("Proveedor digital eliminado correctamente.");
            cargarProveedoresDigitales();
            cbProveedorDigital.setValue(null);
        } else {
            mostrarAlerta("Error al eliminar el proveedor digital. Puede estar en uso por productos.");
        }
    }

    @FXML private void cancelar() {cerrarVentana();}

    private void cerrarVentana() {
        if (productosVista != null) {
            productosVista.cargarProductos();
        }
        BaseControlador.cerrarVentanaInterna(txtCodigo);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
