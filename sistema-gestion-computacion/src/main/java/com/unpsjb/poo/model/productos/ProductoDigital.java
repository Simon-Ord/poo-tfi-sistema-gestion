package com.unpsjb.poo.model.productos;

import java.util.List;

import com.unpsjb.poo.controller.ProductoFormularioVistaControlador;
import com.unpsjb.poo.persistence.dao.impl.ProductoDigitalDAOImpl;

public class ProductoDigital extends Producto{

    private ProveedorDigital proveedorDigital;
    private TipoLicencia tipoLicencia;
    private Integer activacionesMax;
    private Integer duracionLicenciaDias; // Null si es perpetua
    // enum
    public enum TipoLicencia {PERPETUA, SUSCRIPCION, TRIAL}
    
    private static final ProductoDigitalDAOImpl productoDigitalDAO = new ProductoDigitalDAOImpl();

    // Constructor basico
    public ProductoDigital() {
        super();
    }
    // ========================================
    // GETTERS Y SETTERS
    // ========================================

    public ProveedorDigital getProveedorDigital() {
        return proveedorDigital;
    }
    public void setProveedorDigital(ProveedorDigital proveedorDigital) {
        this.proveedorDigital = proveedorDigital;
    }
    public TipoLicencia getTipoLicencia() {
        return tipoLicencia;
    }
    public void setTipoLicencia(TipoLicencia tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }
    public Integer getActivacionesMax() {
        return activacionesMax;
    }
    public void setActivacionesMax(Integer activacionesMax) {
        this.activacionesMax = activacionesMax;
    }
    public Integer getDuracionLicenciaDias() {
        return duracionLicenciaDias;
    }
    public void setDuracionLicenciaDias(Integer duracionLicenciaDias) {
        this.duracionLicenciaDias = duracionLicenciaDias;
    }
    @Override
    public String toString() {
        return "Producto Digital: " + nombreProducto + 
               " - Licencia: " + (tipoLicencia != null ? tipoLicencia : "N/A") +
               " ($" + precioProducto + ")";
    }
    // ========================================
    // Sobrescribe para cargar los datos específicos del producto digital
    @Override
    public Producto obtenerInstanciaCompleta() {
        return ProductoDigital.obtenerPorId(this.idProducto);
    }    
    // ========================
    // Acceso a Persistencia
    // ========================
    public static List<ProductoDigital> obtenerTodosDigitales() {
        return productoDigitalDAO.findAll();
    }
    
    public static ProductoDigital obtenerPorId(int id) {
        return productoDigitalDAO.read(id).orElse(null);
    }    
    // Override del método guardar para usar el DAO específico
    @Override
    public boolean guardar() {
        if (this.idProducto == 0) {
            return productoDigitalDAO.create(this);
        } else {
            return productoDigitalDAO.update(this);
        }
    }
    
    // Override del método actualizar para usar el DAO específico
    @Override
    public boolean actualizar() {
        return productoDigitalDAO.update(this);
    }
    
    // Override del método crear para usar el DAO específico
    @Override
    public boolean crear() {
        return productoDigitalDAO.create(this);
    }
    
    // Override del método eliminar para usar el DAO específico
    @Override
    public boolean eliminar() {
        this.desactivar();
        return productoDigitalDAO.update(this);
    }
    // ========================================
    // Detalle del producto para la UI
    @Override
    public String detallesProductoUI() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(super.detallesProductoUI());
        
        ProductoDigital datosCompletos = ProductoDigital.obtenerPorId(this.idProducto);
        if (datosCompletos != null) {
            sb.append("\n═══════════════════════════════════════════════════\n");
            sb.append("         CARACTERÍSTICAS DIGITALES\n");
            sb.append("═══════════════════════════════════════════════════\n\n");
            
            sb.append("Proveedor Digital: ").append(datosCompletos.getProveedorDigital() != null ? 
                datosCompletos.getProveedorDigital().getNombre() : "No especificado").append("\n");
            sb.append("Tipo de Licencia: ").append(datosCompletos.getTipoLicencia() != null ? 
                datosCompletos.getTipoLicencia() : "No especificado").append("\n");
            
            if (datosCompletos.getActivacionesMax() != null) {
                sb.append("Activaciones Máximas: ").append(datosCompletos.getActivacionesMax()).append("\n");
            }
            
            if (datosCompletos.getDuracionLicenciaDias() != null) {
                sb.append("Duración de Licencia: ").append(datosCompletos.getDuracionLicenciaDias()).append(" días\n");
            }
        }
        return sb.toString();
    }
    // ========================================
    // Override para delegar al controlador para que maneje los datos específicos de producto digital
    @Override
    public void procesarDatosEspecificos(ProductoFormularioVistaControlador controlador) {
        controlador.guardarDatosDigitales(this);
    }
    
    // ========================================
    // Genera cambios específicos de producto digital para auditoría
    @Override
    public String generarCambiosEspecificos(Producto original) {
        ProductoDigital orig = (ProductoDigital) original;
        StringBuilder sb = new StringBuilder();
        if (this.proveedorDigital != null && orig.getProveedorDigital() != null &&
            !this.proveedorDigital.getNombre().equals(orig.getProveedorDigital().getNombre())) {
            sb.append("\n• Proveedor: ").append(orig.getProveedorDigital().getNombre())
            .append(" → ").append(this.proveedorDigital.getNombre());
        }
        if (this.tipoLicencia != null && orig.getTipoLicencia() != null &&
            !this.tipoLicencia.equals(orig.getTipoLicencia())) {
            sb.append("\n• Tipo de Licencia: ").append(orig.getTipoLicencia())
            .append(" → ").append(this.tipoLicencia);
        }
        return sb.toString();
    }
    
    // ========================================
    // Clona producto digital con todos sus atributos
    @Override
    public Producto clonar() {
        ProductoDigital copia = new ProductoDigital();
        // Reutilizar método padre para copiar atributos base
        copiarAtributosBase(copia);
        // Atributos específicos de ProductoDigital
        copia.setProveedorDigital(this.proveedorDigital);
        copia.setTipoLicencia(this.tipoLicencia);
        copia.setActivacionesMax(this.activacionesMax);
        copia.setDuracionLicenciaDias(this.duracionLicenciaDias);
        return copia;
    }
    // ========================================


}
