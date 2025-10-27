package com.unpsjb.poo.model.productos;

import java.util.List;
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
    
    // getters y setters

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

    // Métodos de acceso a persistencia
    public static List<ProductoDigital> obtenerTodosDigitales() {
        return productoDigitalDAO.findAll();
    }
    
    public static ProductoDigital obtenerPorId(int id) {
        return productoDigitalDAO.read(id).orElse(null);
    }
    
    public boolean guardarDigital() {
        if (this.idProducto == 0) {
            return productoDigitalDAO.create(this);
        } else {
            return productoDigitalDAO.update(this);
        }
    }
}
