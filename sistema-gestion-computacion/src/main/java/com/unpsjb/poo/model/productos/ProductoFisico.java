package com.unpsjb.poo.model.productos;

import java.util.List;

import com.unpsjb.poo.controller.ProductoFormularioVistaControlador;
import com.unpsjb.poo.persistence.dao.impl.ProductoFisicoDAOImpl;

public class ProductoFisico extends Producto {
    
    private Fabricante fabricante;
    private Integer garantiaMeses; // Null si no tiene garantia
    private TipoGarantia tipoGarantia; // Null si no tiene garantia
    private EstadoFisico estadoFisico;
    // enums públicos para permitir su uso desde DAO y otros paquetes
    public static enum TipoGarantia {FABRICANTE, TIENDA}
    public static enum EstadoFisico { NUEVO, USADO, REACONDICIONADO} 
    
    private static final ProductoFisicoDAOImpl productoFisicoDAO = new ProductoFisicoDAOImpl();

    // Constructor basico
    public ProductoFisico() {
        super();
    }
    // ========================================
    // GETTERS Y SETTERS
    // ========================================

    public Fabricante getFabricante() {
        return fabricante;
    }
    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }
    public Integer getGarantiaMeses() {
        return garantiaMeses;
    }
    public void setGarantiaMeses(Integer garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }
    public TipoGarantia getTipoGarantia() {
        return tipoGarantia;
    }
    public void setTipoGarantia(TipoGarantia tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }
    public EstadoFisico getEstadoFisico() {
        return estadoFisico;
    }
    public void setEstadoFisico(EstadoFisico estadoFisico) {
        this.estadoFisico = estadoFisico;
    }

    @Override
    public String toString() {
        return "Producto Físico: " + nombreProducto + 
               " - Fabricante: " + (fabricante != null ? fabricante.getNombre() : "N/A") +
               " ($" + precioProducto + ")";
    }
    // ========================================
    // Sobrescribe para cargar los datos específicos del producto físico
    @Override
    public Producto obtenerInstanciaCompleta() {
        return ProductoFisico.obtenerPorId(this.idProducto);
    }
    // ========================
    // Acceso a Persistencia
    // ========================
    public static List<ProductoFisico> obtenerTodosFisicos() {
        return productoFisicoDAO.findAll();
    }
    public static ProductoFisico obtenerPorId(int id) {
        return productoFisicoDAO.read(id).orElse(null);
    }
    // Override del método guardar para usar el DAO específico
    @Override
    public boolean guardar() {
        if (this.idProducto == 0) {
            return productoFisicoDAO.create(this);
        } else {
            return productoFisicoDAO.update(this);
        }
    }
    // Override del método actualizar para usar el DAO específico
    @Override
    public boolean actualizar() {
        return productoFisicoDAO.update(this);
    }
    // Override del método crear para usar el DAO específico
    @Override
    public boolean crear() {
        return productoFisicoDAO.create(this);
    }
    // Override del método eliminar para usar el DAO específico
    @Override
    public boolean eliminar() {
        this.desactivar();
        return productoFisicoDAO.update(this);
    }
    
    // ========================================
    // Detalle del producto para la UI
    @Override
    public String detallesProductoUI() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(super.detallesProductoUI());
        ProductoFisico datosCompletos = ProductoFisico.obtenerPorId(this.idProducto);
        if (datosCompletos != null) {
            sb.append("\n═══════════════════════════════════════════════════\n");
            sb.append("           CARACTERÍSTICAS FÍSICAS\n");
            sb.append("═══════════════════════════════════════════════════\n\n");
            
            sb.append("Fabricante: ").append(datosCompletos.getFabricante() != null ? 
                datosCompletos.getFabricante().getNombre() : "No especificado").append("\n");
            sb.append("Estado Físico: ").append(datosCompletos.getEstadoFisico() != null ? 
                datosCompletos.getEstadoFisico() : "No especificado").append("\n");
            
            if (datosCompletos.getGarantiaMeses() != null && datosCompletos.getGarantiaMeses() > 0) {
                sb.append("Garantía: ").append(datosCompletos.getGarantiaMeses()).append(" meses\n");
                sb.append("Tipo de Garantía: ").append(datosCompletos.getTipoGarantia() != null ? 
                    datosCompletos.getTipoGarantia() : "No especificado").append("\n");
            }
        }
        return sb.toString();
    }
    // ========================================
    // Override para delegar al controlador para que maneje los datos específicos de producto físico
    @Override
    public void procesarDatosEspecificos(ProductoFormularioVistaControlador controlador) {
        // Delego al controlador para que maneje los datos específicos de producto físico
        controlador.guardarDatosFisicos(this);
    }
    
    // ========================================
    // Genera cambios específicos de producto físico para auditoría
    @Override
    public String generarCambiosEspecificos(Producto original) {
        ProductoFisico orig = (ProductoFisico) original;
        StringBuilder sb = new StringBuilder();
        if (this.fabricante != null && orig.getFabricante() != null &&
            !this.fabricante.getNombre().equals(orig.getFabricante().getNombre())) {
            sb.append("\n• Fabricante: ").append(orig.getFabricante().getNombre())
              .append(" → ").append(this.fabricante.getNombre());
        }
        if (this.estadoFisico != null && orig.getEstadoFisico() != null &&
            !this.estadoFisico.equals(orig.getEstadoFisico())) {
            sb.append("\n• Estado: ").append(orig.getEstadoFisico())
              .append(" → ").append(this.estadoFisico);
        }
        return sb.toString();
    }
    
    // ========================================
    // Clona producto físico con todos sus atributos
    @Override
    public Producto clonar() {
        ProductoFisico copia = new ProductoFisico();
        // Atributos base
        copia.setIdProducto(this.idProducto);
        copia.setNombreProducto(this.nombreProducto);
        copia.setDescripcionProducto(this.descripcionProducto);
        copia.setPrecioProducto(this.precioProducto);
        copia.setStockProducto(this.stockProducto);
        copia.setCodigoProducto(this.codigoProducto);
        copia.setCategoria(this.categoria);
        copia.setActivo(this.activo);
        copia.setFechaCreacion(this.fechaCreacion);
        // Atributos específicos
        copia.setFabricante(this.fabricante);
        copia.setEstadoFisico(this.estadoFisico);
        copia.setGarantiaMeses(this.garantiaMeses);
        copia.setTipoGarantia(this.tipoGarantia);
        return copia;
    }
    // ========================================
}
