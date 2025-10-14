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
    estado BOOLEAN DEFAULT TRUE
);

SELECT * FROM usuarios;
