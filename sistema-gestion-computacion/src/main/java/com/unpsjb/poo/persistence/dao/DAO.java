package com.unpsjb.poo.persistence.dao;


import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones de Acceso a Datos (DAO).
 * Define las operaciones CRUD estándar que cualquier DAO concreto debe implementar.
 * @param <T> El tipo de la entidad (modelo) que este DAO manejará.
 */
public interface DAO<T> {
    /**
     * Inserta una nueva entidad en la base de datos.
     * @param t La entidad a crear.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean create(T t);

    /**
     * Busca y devuelve una entidad por su identificador único.
     * @param id El ID de la entidad a buscar.
     * @return Un Optional que contiene la entidad si se encuentra, o un Optional vacío si no.
     */
    Optional<T> read(int id);
    /**
     * Actualiza una entidad existente en la base de datos.
     * @param t La entidad con los datos actualizados.
     */
    boolean update(T t);
    /**
     * Elimina una entidad de la base de datos por su identificador único.
     * @param id El ID de la entidad a eliminar.
    * @return true si la eliminación (o desactivación) fue exitosa, false en caso contrario.
    */
    boolean delete(int id);
    /**
     * Devuelve una lista con todas las entidades de la tabla.
     * @return Una lista de todas las entidades.
     */
    List<T> findAll();
}





