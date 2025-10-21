package com.unpsjb.poo.model.productos;


public class ProductoFisico extends Producto {
    
    private Fabricante fabricante;
    private Integer garantiaMeses; // Null si no tiene garantia
    private TipoGarantia tipoGarantia; // Null si no tiene garantia
    private EstadoFisico estadoFisico;
    // enums públicos para permitir su uso desde DAO y otros paquetes
    public static enum TipoGarantia {FABRICANTE, TIENDA}
    public static enum EstadoFisico { NUEVO, USADO, REACONDICIONADO} 

    // Constructor basico
    public ProductoFisico() {
        super();
    }

    // getters y setters
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



}
