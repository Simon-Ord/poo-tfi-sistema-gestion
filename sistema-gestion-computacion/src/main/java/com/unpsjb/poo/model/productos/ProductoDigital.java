package com.unpsjb.poo.model.productos;


public class ProductoDigital extends Producto{

    private ProveedorDigital proveedorDigital;
    private TipoLicencia tipoLicencia;
    private Integer activacionesMax;
    private Integer duracionLicenciaDias; // Null si es perpetua
    // enum
    public enum TipoLicencia {PERPETUA, SUSCRIPCION, TRIAL}
    

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

}
