package com.unpsjb.poo.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Clase modelo que representa un producto dentro del sistema de gestión.
 * 
 * 📘 Conceptos de POO aplicados:
 * - Encapsulamiento: los atributos son privados y se acceden por getters/setters.
 * - Abstracción: solo define los datos relevantes del producto, no la lógica de negocio.
 * - Reutilización: se puede usar en DAOs, controladores y vistas.
 * - Responsabilidad única (SRP): esta clase solo modela un producto, no lo gestiona.
 */
public class Producto {

    // =============================================================
    // 🧱 Atributos (campos)
    // =============================================================
    private int idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private int stockProducto;
    private BigDecimal precioProducto;
    private String categoriaProducto;
    private String fabricanteProducto;
    private int codigoProducto;
    private boolean activo;
    private Timestamp fechaCreacion;

    // =============================================================
    // 🏗️ Constructores
    // =============================================================

    /**
     * Constructor por defecto.
     * Se usa en los DAOs para crear objetos vacíos.
     */
    public Producto() {
        this.activo = true; // Por defecto, los productos nuevos están activos
    }

    /**
     * Constructor completo (todos los campos)
     */
    public Producto(int idProducto, String nombreProducto, String descripcionProducto,
                    int stockProducto, BigDecimal precioProducto, String categoriaProducto,
                    String fabricanteProducto, int codigoProducto, boolean activo) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.stockProducto = stockProducto;
        this.precioProducto = precioProducto;
        this.categoriaProducto = categoriaProducto;
        this.fabricanteProducto = fabricanteProducto;
        this.codigoProducto = codigoProducto;
        this.activo = activo;
    }

    /**
     * Constructor simplificado (por compatibilidad con versiones anteriores)
     * Por defecto, los productos creados con este constructor estarán activos.
     */
    public Producto(int idProducto, String nombreProducto, String descripcionProducto,
                    int stockProducto, BigDecimal precioProducto, String categoriaProducto,
                    String fabricanteProducto, int codigoProducto) {
        this(idProducto, nombreProducto, descripcionProducto, stockProducto,
             precioProducto, categoriaProducto, fabricanteProducto, codigoProducto, true);
    }

    // =============================================================
    // 🧩 Getters y Setters (Encapsulamiento)
    // =============================================================

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public int getStockProducto() {
        return stockProducto;
    }

    public void setStockProducto(int stockProducto) {
        this.stockProducto = stockProducto;
    }

    public BigDecimal getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto( BigDecimal precioProducto) {
        this.precioProducto = precioProducto;
    }

    public String getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(String categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }

    public String getFabricanteProducto() {
        return fabricanteProducto;
    }

    public void setFabricanteProducto(String fabricanteProducto) {
        this.fabricanteProducto = fabricanteProducto;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // =============================================================
    // 🧠 Método de clonación (opcional, útil para auditoría)
    // =============================================================

    /**
     * Crea una copia exacta de este producto.
     * Sirve para comparar los cambios antes y después de una modificación.
     *
     * 📘 Conceptos POO aplicados:
     * - Reutilización: permite replicar un producto sin modificar el original.
     * - Abstracción: oculta los detalles de cómo se hace la copia.
     */
    public Producto clonar() {
        Producto copia = new Producto();
        copia.setIdProducto(this.idProducto);
        copia.setNombreProducto(this.nombreProducto);
        copia.setDescripcionProducto(this.descripcionProducto);
        copia.setStockProducto(this.stockProducto);
        copia.setPrecioProducto(this.precioProducto);
        copia.setCategoriaProducto(this.categoriaProducto);
        copia.setFabricanteProducto(this.fabricanteProducto);
        copia.setCodigoProducto(this.codigoProducto);
        copia.setActivo(this.activo);
        copia.setFechaCreacion(this.fechaCreacion);
        return copia;
    }

    // =============================================================
    // 🔍 Método toString() (opcional para depuración)
    // =============================================================

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + idProducto +
                ", nombre='" + nombreProducto + '\'' +
                ", descripcion='" + descripcionProducto + '\'' +
                ", categoria='" + categoriaProducto + '\'' +
                ", fabricante='" + fabricanteProducto + '\'' +
                ", precio=" + precioProducto +
                ", stock=" + stockProducto +
                ", activo=" + activo +
                '}';
    }
}
