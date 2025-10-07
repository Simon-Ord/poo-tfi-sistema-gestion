package com.unpsjb.poo.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestorDeConexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/tienda_computacion";
    private static final String USER = "postgres";
    private static final String PASSWORD = "alexis1213"; // tu contraseña real

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
