package com.unpsjb.poo.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GestorDeConexion {
	
	private static GestorDeConexion instancia;
	private Connection conexion;
	
	private GestorDeConexion() {
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream("src/config.properties")){  //Con estas lineas lo que hace es leer
			props.load(fis);                                                       //Los datos del archivo properties
			
			String URL = props.getProperty("db.url");
			String user = props.getProperty("db.user");
			String password = props.getProperty("db.password");
			
			
			Class.forName("org.postgresql.Driver");
			this.conexion = DriverManager.getConnection(URL,user,password);   //Esta linea se encarga de establecer la conexion
			//inicializarBaseDeDatos();                                       //Entre Java y Postgresql, es la mas importante  
		} catch (SQLException  | IOException e) {
			System.err.println("Error al conectar con la base de datos: " + e.getMessage());
			e.printStackTrace();
		}
		 catch (ClassNotFoundException e) {
			System.err.println("No se encontro el driver de Postgresql: " + e.getMessage());
			e.printStackTrace();
		 }
	}
		
	
	
	public static synchronized GestorDeConexion getInstancia() {   //Este es el metodo de acceso global
		if (instancia == null) {
			instancia = new GestorDeConexion();
		}
		return instancia;
	}
	
	public Connection getConexion() {
		return this.conexion;
	}

	public void closeConexion() {                    //Para cerrar la conexion cuando
		if (this.conexion != null) {                 //No se use
			try {
				this.conexion.close();
			} catch (SQLException e) {
				System.err.println("Error al cerrar la conexión: " + e.getMessage());
			}
		}
	}



	public static Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}



}
