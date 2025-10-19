package com.unpsjb.poo.persistence.dao;

import java.util.List;

import com.unpsjb.poo.model.Cliente;

public interface ClienteDAO {

    // 🔹 Devuelve todos los clientes
    List<Cliente> obtenerTodos();

    // 🔹 Inserta un nuevo cliente
    boolean insertar(Cliente cliente);

    // 🔹 Modifica un cliente existente
    boolean modificar(Cliente cliente);

    // 🔹 Elimina un cliente (por ID)
    boolean eliminar(int id);

    // 🔹 Busca un cliente por ID
    Cliente obtenerPorId(int id);

    // 🔹 Busca clientes por nombre (parcial o completo)
    List<Cliente> buscarPorNombre(String nombre);
}
