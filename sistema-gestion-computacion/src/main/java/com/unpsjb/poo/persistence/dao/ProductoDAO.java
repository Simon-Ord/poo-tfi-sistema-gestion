package com.unpsjb.poo.persistence.dao;//package com.unpsjb.poo.persistence.dao;
import java.util.List;// importa la clase SQLException que maneja las excepciones SQL

import com.unpsjb.poo.model.Producto;// importa la clase List que representa una lista de objetos


public interface ProductoDAO {
    void agregarProducto(Producto p);
    void actualizarProducto(Producto p);
    void eliminarProducto(int id);
    Producto obtenerPorId(int id);
    List<Producto> listarTodos();
}


// ejemplos de lo que podria contener la interfaz

// lo que hace esta interfaz es definir los métodos que cualquier clase que implemente esta interfaz debe tener
// en este caso, la interfaz define los métodos para manejar objetos de tipo Producto en una base
// de datos, como agregar, actualizar, eliminar y obtener productos.
// esto es parte del patrón DAO (Data Access Object), que es una forma de separar la lógica de acceso a datos
// de la lógica de negocio en una aplicación.



