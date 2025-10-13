package com.unpsjb.poo.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unpsjb.poo.model.Producto;
import com.unpsjb.poo.persistence.dao.DAO;

public class ProductoDAOMock implements DAO<Producto> {
    private final List<Producto> productos = new ArrayList<>();

    @Override
    public void create(Producto producto) {
        producto.setId(productos.size() + 1);
        productos.add(producto);
        System.out.println("💾 [Simulación] Producto registrado: " + producto.getNombreProducto());
    }

    @Override
    public Optional<Producto> read(int id) {
        return productos.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public void update(Producto producto) {
        productos.replaceAll(p -> p.getId() == producto.getId() ? producto : p);
    }

    @Override
    public void delete(int id) {
        productos.removeIf(p -> p.getId() == id);
    }

    @Override
    public List<Producto> findAll() {
        return new ArrayList<>(productos);
    }
}
