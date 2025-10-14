package com.unpsjb.poo.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase encargada de gestionar la conexión a la base de datos PostgreSQL.
 * Usa el patrón Singleton para tener una única instancia en toda la aplicación.
 * 
 * Carga los datos de conexión desde el archivo config.properties.
 */
public class GestorDeConexion {

    // Instancia única de la clase
    private static GestorDeConexion instancia;

    // Datos de conexión
    private String URL;
    private String user;
    private String password;

    // Constructor privado para el patrón Singleton
    private GestorDeConexion() {
        Properties props = new Properties();

        try (InputStream fis = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (fis == null) {
                throw new IOException("No se encontró el archivo config.properties en resources/");
            }

            // Cargar las propiedades desde el archivo
            props.load(fis);

            // Asignar valores
            URL = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");

            // Cargar el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ Configuración de PostgreSQL cargada correctamente.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("⚠️ Error cargando configuración: " + e.getMessage());
        }
    }

    // Retorna la única instancia de GestorDeConexion
    public static synchronized GestorDeConexion getInstancia() {
        if (instancia == null) {
            instancia = new GestorDeConexion();
        }
        return instancia;
    }

    // Devuelve una nueva conexión a la base de datos
    public Connection getConexion() {
        try {
            Connection conn = DriverManager.getConnection(URL, user, password);
            System.out.println("🔗 Conexión abierta con PostgreSQL.");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Error al abrir la conexión: " + e.getMessage());
            return null;
        }
    }
}
