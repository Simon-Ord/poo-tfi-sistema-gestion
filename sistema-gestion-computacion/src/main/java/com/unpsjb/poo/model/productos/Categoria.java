package com.unpsjb.poo.model.productos;

import java.time.LocalDateTime;
import java.util.List;

import com.unpsjb.poo.persistence.dao.impl.CategoriaDAOImpl;

public class Categoria {
    private int idCategoria;
    private String nombreCategoria;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    // DAO estático compartido por todas las categorías
    private static final CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();

    // Constructores
    public Categoria() {
        this.activo = true; // Por defecto activa
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Categoria(String nombreCategoria) {
        this();
        this.nombreCategoria = nombreCategoria;
    }

    // Getters y setters
    public int getId() {
        return idCategoria;
    }
    
    public void setId(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    
    public String getNombre() {
        return nombreCategoria;
    }
    
    public void setNombre(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return nombreCategoria;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categoria categoria = (Categoria) obj;
        return idCategoria == categoria.idCategoria;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idCategoria);
    }

    // ========================================
    // ===== MÉTODOS DE LÓGICA DE NEGOCIO =====
    // ========================================
    // Activa esta categoría
    public void activar() {
        this.activo = true;
    }
    // Desactiva esta categoría
    public void desactivar() {
        this.activo = false;
    }

    // Metodo para verificar si la categoria esta activa
    public boolean estaActiva() {
        return this.activo;
    }
    // Metodo para obtener el estado en texto
    public String getEstadoTexto() {
        return this.activo ? "Activa" : "Inactiva";
    }
    // ========================
    // Acceso a Persistencia
    // ========================
    // ========================================
    // Metodo para guardar la categoria (crear o actualizar)
    public boolean guardar() {
        if (this.idCategoria == 0) {
            // Es una categoría nueva, usar create
            return categoriaDAO.create(this);
        } else {
            // Es una categoría existente, usar update
            return categoriaDAO.update(this);
        }
    }
    // Override del método actualizar para usar el DAO específico
    public boolean actualizar() {
        return categoriaDAO.update(this);
    }
    // Override del método crear para usar el DAO específico
    public boolean crear() {
        return categoriaDAO.create(this);
    }
    // Override del método eliminar para usar el DAO específico
    public boolean eliminar() {
        this.desactivar();
        return categoriaDAO.update(this);
    }
    // Obtiene todas las categorías activas
    public static List<Categoria> obtenerTodas() {
        return categoriaDAO.findAll();
    }
    // Obtiene todas las categorías, activas e inactivas
    public static List<Categoria> obtenerTodasCompleto() {
        List<Categoria> activas = categoriaDAO.findAll();
        // Nota: Necesitarías implementar findAllInactivas en el DAO si quieres incluir inactivas
        return activas;
    }
    // Obtiene una categoría por su ID
    public static Categoria obtenerPorId(int id) {
        return categoriaDAO.read(id).orElse(null);
    }
        // Metodo para cambiar el estado de la categoria
    public boolean cambiarEstado(boolean activo) {
        this.activo = activo;
        return categoriaDAO.update(this);
    }
    // Busca categorías por nombre (búsqueda parcial)
    public static List<Categoria> buscarPorNombre(String nombre) {
        return categoriaDAO.findAll().stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }
    // 
    public static boolean estaEnUso(int id) {
        // Implementar verificación en el DAO
        // Por ahora retorna false
        return false;
    }

    // ========================================


}
