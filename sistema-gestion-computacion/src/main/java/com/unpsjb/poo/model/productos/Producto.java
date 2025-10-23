package com.unpsjb.poo.model.productos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;

public class Producto {
    protected int idProducto;
    protected String nombreProducto;
    protected String descripcionProducto;
    protected int stockProducto;
    protected BigDecimal precioProducto;
    private Categoria categoria;
    protected int codigoProducto;
    protected boolean activo;
    protected Timestamp fechaCreacion;
    
    // DAO estático compartido por todos los productos
    private static final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    
    public Producto() {
        // Constructor por defecto, necesario para el método Read en el DAO
        this.activo = true; // Por defecto los nuevos productos están activos
    }


    public int getIdProducto() {return idProducto;}
    public void setIdProducto(int idProducto) {this.idProducto = idProducto;}

    public String getNombreProducto () {return nombreProducto;}
    public void setNombreProducto (String nombreProducto) {this.nombreProducto = nombreProducto;}

    public String getDescripcionProducto () {return descripcionProducto;}
    public void setDescripcionProducto(String descripcionProducto) {this.descripcionProducto = descripcionProducto;}

    public int getStockProducto() {return stockProducto;}
    public void setStockProducto(int stockProducto) {this.stockProducto = stockProducto;}

    public BigDecimal getPrecioProducto() {return precioProducto;}
    public void setPrecioProducto(BigDecimal precioProducto) {this.precioProducto = precioProducto;}

    public Categoria getCategoria() {return categoria;}
    public void setCategoria(Categoria categoria) {this.categoria = categoria;}

    public String getCategoriaProducto() {return categoria != null ? categoria.getNombre() : null;}
    public void setCategoriaProducto(String categoriaProducto) {
        if (this.categoria == null) {
            this.categoria = new Categoria();
        }
        this.categoria.setNombre(categoriaProducto);
    }

    public int getCodigoProducto() {return codigoProducto;}
    public void setCodigoProducto (int codigoProducto) {this.codigoProducto = codigoProducto;}

    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Timestamp getFechaCreacion() {return fechaCreacion;}
    public void setFechaCreacion(Timestamp fechaCreacion) {this.fechaCreacion = fechaCreacion;}
    
    // ========================================
    // ===== MÉTODOS DE LÓGICA DE NEGOCIO =====
    // ========================================

    /**
     * Cambia el estado del producto (activo/inactivo).
     * Esta es lógica de negocio que pertenece al modelo.
     */
    public void cambiarEstado() {
        this.activo = !this.activo;
    }
    /**
     * Activa el producto
     */
    public void activar() {
        this.activo = true;
    }
    /**
     * Desactiva el producto (eliminación lógica)
     */
    public void desactivar() {
        this.activo = false;
    }
    /**
     * Verifica si hay stock suficiente
     * @param cantidadRequerida cantidad que se necesita
     * @return true si hay stock suficiente
     */
    public boolean tieneStockSuficiente(int cantidadRequerida) {
        return this.stockProducto >= cantidadRequerida && cantidadRequerida > 0;
    }
    /**
     * Reduce el stock del producto
     * @param cantidad cantidad a reducir
     * @return true si se pudo reducir, false si no hay stock suficiente
     */
    public boolean reducirStock(int cantidad) {
        if (!tieneStockSuficiente(cantidad)) {
            return false;
        }
        this.stockProducto -= cantidad;
        return true;
    }
    /**
     * Aumenta el stock del producto
     * @param cantidad cantidad a aumentar
     */
    public void aumentarStock(int cantidad) {
        if (cantidad > 0) {
            this.stockProducto += cantidad;
        }
    }
    
    // ===== MÉTODOS ESTÁTICOS PARA ACCESO A PERSISTENCIA =====
    // El modelo encapsula el acceso al DAO
    // El controlador NUNCA debe conocer el DAO directamente
    
    /**
     * Obtiene todos los productos activos desde la base de datos
     * @return Lista de productos activos
     */
    public static List<Producto> obtenerTodos() {
        return productoDAO.findAll();
    }
    
    /**
     * Obtiene todos los productos (activos e inactivos) desde la base de datos
     * @return Lista completa de productos
     */
    public static List<Producto> obtenerTodosCompleto() {
        return productoDAO.findAllCompleto();
    }
    
    /**
     * Busca un producto por su ID
     * @param id ID del producto
     * @return Producto encontrado o null
     */
    public static Producto obtenerPorId(int id) {
        return productoDAO.read(id).orElse(null);
    }
    
    /**
     * Guarda este producto en la base de datos (persistencia de la instancia)
     * @return true si se guardó correctamente
     */
    public boolean guardar() {
        if (this.idProducto == 0) {
            // Es un producto nuevo, usar create
            return productoDAO.create(this);
        } else {
            // Es un producto existente, usar update
            return productoDAO.update(this);
        }
    }
    
    /**
     * Actualiza este producto en la base de datos
     * @return true si se actualizó correctamente
     */
    public boolean actualizar() {
        return productoDAO.update(this);
    }
    
    /**
     * Crea un nuevo producto en la base de datos
     * @return true si se creó correctamente
     */
    public boolean crear() {
        return productoDAO.create(this);
    }
    
    /**
     * Elimina este producto (eliminación lógica - lo marca como inactivo)
     * @return true si se eliminó correctamente
     */
    public boolean eliminar() {
        this.desactivar();
        return productoDAO.update(this);
    }
    
    // ========================================
}