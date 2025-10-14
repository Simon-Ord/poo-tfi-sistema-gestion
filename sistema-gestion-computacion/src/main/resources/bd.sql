-- Crear la base de datos (solo si no existe)
CREATE DATABASE tienda_computacion;

\c tienda_computacion; -- conectar a la base de datos

-- Crear tabla usuarios
CREATE TABLE  usuarios (
    id SERIAL PRIMARY KEY,
    legajo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
  
);

INSERT INTO usuarios (legajo, nombre, usuario, contraseña, rol) VALUES
('1001', 'Admin User', 'admin', 'admin123', 'ADMIN'),
('1002', 'John Doe', 'jdoe', 'password1', 'USER'),
('1003', 'Jane Smith', 'jsmith', 'password2', 'USER');