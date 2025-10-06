//package com.unpsjb.poo.persistence.dao.impl;

//import java.sql.Connection;


//👉¿Qué es?
//Es la implementación concreta de la interfaz ProductoDAO.
//Aquí sí está el código SQL que interactúa con la base de datos.

//👉 Qué hace:

//para abrir la conexión.

//Ejecuta sentencias SQL ( INSERT, UPDATE, DELETE, SELECT).

//Convertir resultados ( ResultSet) en objetos Producto.
/* 
public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public void agregarProducto(Producto p) {
        String sql = "INSERT INTO productos(nombre, precio, stock) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecio());
            stmt.setInt(3, p.getStock());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // demás métodos...
} */


    // implementar actualizar, eliminar, buscarPorId de forma similar
