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

    /**
     * Guarda esta categoría en la base de datos (persistencia de la instancia)
     * @return true si se guardó correctamente
     */
    public boolean guardar() {
        if (this.idCategoria == 0) {
            // Es una categoría nueva, usar create
            return categoriaDAO.create(this);
        } else {
            // Es una categoría existente, usar update
            return categoriaDAO.update(this);
        }
    }
    
    /**
     * Actualiza esta categoría en la base de datos
     * @return true si se actualizó correctamente
     */
    public boolean actualizar() {
        return categoriaDAO.update(this);
    }
    
    /**
     * Crea una nueva categoría en la base de datos
     * @return true si se creó correctamente
     */
    public boolean crear() {
        return categoriaDAO.create(this);
    }
    
    /**
     * Elimina esta categoría (eliminación lógica - la marca como inactiva)
     * @return true si se eliminó correctamente
     */
    public boolean eliminar() {
        this.desactivar();
        return categoriaDAO.update(this);
    }
    
    /**
     * Activa esta categoría
     */
    public void activar() {
        this.activo = true;
    }
    
    /**
     * Desactiva esta categoría
     */
    public void desactivar() {
        this.activo = false;
    }
    
    /**
     * Cambia el estado de esta categoría
     * @param activo nuevo estado
     * @return true si se cambió correctamente
     */
    public boolean cambiarEstado(boolean activo) {
        this.activo = activo;
        return categoriaDAO.update(this);
    }
    
    /**
     * Verifica si esta categoría está activa
     * @return true si está activa
     */
    public boolean estaActiva() {
        return this.activo;
    }
    
    /**
     * Obtiene una representación amigable del estado
     * @return "Activa" o "Inactiva"
     */
    public String getEstadoTexto() {
        return this.activo ? "Activa" : "Inactiva";
    }

    // ===== MÉTODOS ESTÁTICOS PARA ACCESO A PERSISTENCIA =====
    // El modelo encapsula el acceso al DAO
    // El controlador NUNCA debe conocer el DAO directamente
    
    /**
     * Obtiene todas las categorías activas desde la base de datos
     * @return Lista de categorías activas
     */
    public static List<Categoria> obtenerTodas() {
        return categoriaDAO.findAll();
    }
    
    /**
     * Obtiene todas las categorías (activas e inactivas) desde la base de datos
     * @return Lista completa de categorías
     */
    public static List<Categoria> obtenerTodasCompleto() {
        List<Categoria> activas = categoriaDAO.findAll();
        // Nota: Necesitarías implementar findAllInactivas en el DAO si quieres incluir inactivas
        return activas;
    }
    
    /**
     * Busca una categoría por su ID
     * @param id ID de la categoría
     * @return Categoría encontrada o null
     */
    public static Categoria obtenerPorId(int id) {
        return categoriaDAO.read(id).orElse(null);
    }
    
    /**
     * Busca categorías por nombre (búsqueda parcial)
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de categorías que coinciden
     */
    public static List<Categoria> buscarPorNombre(String nombre) {
        return categoriaDAO.findAll().stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }
    
    /**
     * Verifica si una categoría está siendo utilizada por algún producto
     * @param id ID de la categoría
     * @return true si está en uso
     */
    public static boolean estaEnUso(int id) {
        // Implementar verificación en el DAO
        // Por ahora retorna false
        return false;
    }

    // ========================================


}
