package com.unpsjb.poo.persistence;

import java.sql.Connection;// importa la clase Connection que representa una conexión a la base de datos
import java.sql.DriverManager;// importa la clase DriverManager que maneja las conexiones a la base de datos
import java.sql.SQLException;// importa la clase SQLException que maneja las excepciones SQL

public class GestorDeConexion {
    private static GestorDeConexion instance;// instancia única (singleton)
    private Connection conn;// conexión a la base de datos

    // Cambiá estos datos según tu instalación local
    private final String URL = "jdbc:postgresql://localhost:5432/tienda";// URL de conexión
    private final String USER = "postgres";// usuario de la base de datos
    private final String PASS = "tu_contraseña";// contraseña de la base de datos

    // Constructor privado para evitar instanciación externa
    private GestorDeConexion() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASS);// establece la conexión
    }
// Método para obtener la instancia única (singleton)
    public static synchronized GestorDeConexion getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new GestorDeConexion();
        }
        return instance;
    }
// Método para obtener la conexión
    public Connection getConnection() {
        return conn;
    }
}




//Por qué Singleton : la conexión es un recurso costoso y no conviene crearla repetidamente; 
//El patrón Singleton asegura una única instancia en toda la aplicación. (ver apuntes de clase).

