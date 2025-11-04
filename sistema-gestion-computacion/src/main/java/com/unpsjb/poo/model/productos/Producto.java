package com.unpsjb.poo.model.productos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.unpsjb.poo.controller.ProductoFormularioVistaControlador;
import com.unpsjb.poo.persistence.dao.impl.ProductoDAOImpl;

public class Producto {

    protected int idProducto;
    protected String nombreProducto;
    protected String descripcionProducto;
    protected int stockProducto;
    protected BigDecimal precioProducto;
    protected Categoria categoria;
    protected int codigoProducto;
    protected boolean activo;
    protected Timestamp fechaCreacion;
    
    // DAO estatico compartido por todos los productos
    private static final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    
    public Producto() {
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
    public void setActivo(boolean activo) {this.activo = activo;}
    
    public Timestamp getFechaCreacion() {return fechaCreacion;}
    public void setFechaCreacion(Timestamp fechaCreacion) {this.fechaCreacion = fechaCreacion;}

    @Override
    public String toString() {
        return "Producto: " + nombreProducto + " ($" + precioProducto + ")";
    }
    // ========================
    // Lógica de Negocio
    // ========================

    // Generacion automatica de codigo para un nuevo producto
    public static int generarCodigoAutomatico() {
        try {
            int maxCodigo = productoDAO.obtenerCodigoMaximo();
            return maxCodigo == 0 ? 1000 : maxCodigo + 1;
        } catch (Exception e) {
            return 90000 + (int)(Math.random() * 9999); // Fallback 90000-99999
        }
    }
    // Cambia el estado de activo a inactivo o viceversa
    public void cambiarEstado() {
        this.activo = !this.activo;
    }
    // Desactiva el producto (eliminación lógica)
    public void desactivar() {
        this.activo = false;
    }
    // Verifica si hay stock suficiente para una cantidad requerida
    public boolean tieneStockSuficiente(int cantidadRequerida) {
        return this.stockProducto >= cantidadRequerida && cantidadRequerida > 0;
    }
    // Reduce el stock del producto
    public boolean reducirStock(int cantidad) {
        if (!tieneStockSuficiente(cantidad)) {
            return false;
        }
        this.stockProducto -= cantidad;
        return true;
    }
    // Aumenta el stock del producto
    public void aumentarStock(int cantidad) {
        if (cantidad > 0) {
            this.stockProducto += cantidad;
        }
    }
    // ========================
    // Acceso a Persistencia
    // ========================
    
    // Obtiene todos los productos activos desde la base de datos
    public static List<Producto> obtenerTodos() {
        return productoDAO.findAll();
    }
    // Obtiene todos los productos activos e INACTIVOS desde la base de datos
    public static List<Producto> obtenerTodosCompleto() {
        return productoDAO.findAllCompleto();
    }
    // Busca productos por cualquier campo (solo activos)
    public static List<Producto> buscarProductos(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return obtenerTodos(); // Si no hay término, devolver todos
        }
        return productoDAO.buscarProductos(termino.trim());
    }
    // Busca productos por cualquier campo incluyendo inactivos
    public static List<Producto> buscarProductosCompleto(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return obtenerTodosCompleto(); // Si no hay término, devolver todos
        }
        return productoDAO.buscarProductosCompleto(termino.trim());
    }
    // Obtiene un producto por su ID
    public static Producto obtenerPorId(int id) {
        return productoDAO.read(id).orElse(null);
    }
    // Guarda este producto en la base de datos
    public boolean guardar() {
        if (this.idProducto == 0) {
            // Es un producto nuevo, usar create
            return productoDAO.create(this);
        } else {
            // Es un producto existente, usar update
            return productoDAO.update(this);
        }
    }
    // Actualiza un producto existente en la base de datos
    public boolean actualizar() {
        return productoDAO.update(this);
    }
    // Crea un producto nuevo en la base de datos
    public boolean crear() {
        return productoDAO.create(this);
    }
    // Pone el estado de activo en false (eliminación lógica)
    public boolean eliminar() {
        this.desactivar();
        return productoDAO.update(this);
    }
    
    // Método para determinar el tipo de producto
    public String obtenerTipoProducto() {
        return this.idProducto == 0 ? "GENERICO" : productoDAO.obtenerTipoProducto(this.idProducto);
    }
    // ========================================
    // Sobrescribe para cargar los datos específicos del producto
    public Producto obtenerInstanciaCompleta() {
        return this;
    }
    // ========================================
    // Método base para procesar datos específicos del formulario
    public void procesarDatosEspecificos(ProductoFormularioVistaControlador controlador) {}
    
    // ========================================
    // Las subclases sobrescriben para agregar sus atributos específicos
    public String generarCambiosEspecificos(Producto original) {
        return ""; // Por defecto, producto genérico no tiene cambios específicos
    }
    
    // ========================================
    // Las subclases sobrescriben para copiar sus atributos específicos para Auditoria
    public Producto clonar() {
        Producto copia = new Producto();
        copiarAtributosBase(copia);
        return copia;
    }
    
    // Método auxiliar para copiar atributos base (reutilizable por subclases)
    protected void copiarAtributosBase(Producto copia) {
        copia.setIdProducto(this.idProducto);
        copia.setNombreProducto(this.nombreProducto);
        copia.setDescripcionProducto(this.descripcionProducto);
        copia.setPrecioProducto(this.precioProducto);
        copia.setStockProducto(this.stockProducto);
        copia.setCodigoProducto(this.codigoProducto);
        copia.setCategoria(this.categoria);
        copia.setActivo(this.activo);
        copia.setFechaCreacion(this.fechaCreacion);
    }
    
    // ========================================
    // Detalle del producto para la UI
    public String detallesProductoUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append("                INFORMACIÓN BÁSICA\n");
        sb.append("═══════════════════════════════════════════════════\n\n");
        
        sb.append("Código: ").append(this.codigoProducto).append("\n");
        sb.append("Nombre: ").append(this.nombreProducto).append("\n");
        sb.append("Descripción: ").append(this.descripcionProducto).append("\n");
        sb.append("Categoría: ").append(this.categoria != null ? this.categoria.getNombre() : "Sin categoría").append("\n");
        sb.append("Precio: $").append(this.precioProducto).append("\n");
        sb.append("Stock: ").append(this.stockProducto).append(" unidades\n");
        sb.append("Estado: ").append(this.activo ? "Activo" : "Inactivo").append("\n");
        
        return sb.toString();
    }
    // ========================================

    public String compararDatosEspecificos(Producto otro) {
    // Por defecto, no hay campos específicos que comparar.
    return "";
}


}