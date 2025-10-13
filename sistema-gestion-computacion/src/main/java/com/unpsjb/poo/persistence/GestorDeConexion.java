package com.unpsjb.poo.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 * Gestor de conexión a la base de datos (patrón Singleton).
 * Se encarga de leer las credenciales desde config.properties y mantener
 * una única conexión abierta durante la ejecución de la aplicación.
 */
public class GestorDeConexion {

    private static GestorDeConexion instancia;
    private Connection conexion;

    // =====================================================
    // 🔹 Constructor privado (solo se ejecuta una vez)
    // =====================================================
    private GestorDeConexion() {
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/config.properties")) {

            if (input == null) {
                throw new IOException("No se encontró el archivo config.properties dentro de resources.");
            }

            props.load(input);

            String URL = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Class.forName("org.postgresql.Driver");
            this.conexion = DriverManager.getConnection(URL, user, password);

            System.out.println("✅ Conexión establecida correctamente con la base de datos.");

        } catch (SQLException e) {
            System.err.println("❌ Error SQL al conectar con la base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ Error al leer el archivo de configuración: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("❌ No se encontró el driver de PostgreSQL: " + e.getMessage());
        }
    }

    // =====================================================
    // 🔹 Método estático para obtener la instancia única
    // =====================================================
    public static synchronized GestorDeConexion getInstancia() {
        if (instancia == null) {
            instancia = new GestorDeConexion();
        }
        return instancia;
    }

    // =====================================================
    // 🔹 Devuelve la conexión activa
    // =====================================================
    public Connection getConexion() {
        return this.conexion;
    }

    // =====================================================
    // 🔹 Cierra la conexión (si está abierta)
    // =====================================================
    public void closeConexion() {
        if (this.conexion != null) {
            try {
                this.conexion.close();
                System.out.println("🔒 Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    // =====================================================
    // 🔹 Método de acceso rápido (atajo estático)
    // =====================================================
    public static Connection getConnection() {
        return getInstancia().getConexion();
    }
}
