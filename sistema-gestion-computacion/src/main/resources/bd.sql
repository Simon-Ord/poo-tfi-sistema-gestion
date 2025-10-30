-- Active: 1760230244890@@127.0.0.1@5432@tienda_computacion
-- =============================================================
-- TABLAS PRINCIPALES DEL SISTEMA
-- =============================================================

CREATE TABLE usuarios (
    dni VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    rol VARCHAR(50) NOT NULL CHECK (rol IN ('ADMINISTRADOR', 'EMPLEADO')),
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ====================
-- TABLA DE CLIENTES
-- ====================


CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cuit VARCHAR(11),
    telefono VARCHAR(40),
    direccion VARCHAR(200),
    email VARCHAR(100),
    tipo VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE

);


DROP TABLE IF EXISTS clientes CASCADE;

-- =============================================================
-- TABLA DE AUDITORÍA 
-- =============================================================

CREATE TABLE auditoria (
    id SERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(100) NOT NULL,     -- Usuario Java (no PostgreSQL)
    accion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    entidad_afectada VARCHAR(50),
    id_referencia VARCHAR(50)
);

-- =============================================================
-- TABLA DE SESIONES 
-- =============================================================

CREATE TABLE sesiones (
    id SERIAL PRIMARY KEY,
    usuario VARCHAR(100) NOT NULL,  -- nombre o usuario Java
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA', 'CERRADA'))
);

-- =============================================================
-- USUARIO ADMINISTRADOR INICIAL
-- =============================================================

INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado)
VALUES ('1234', 'ALEXIS', '1', '1', 'ADMINISTRADOR', true);

-- =============================================================
-- CONSULTAS DE PRUEBA
-- =============================================================

SELECT * FROM clientes;
SELECT * FROM productos;
SELECT * FROM auditoria ORDER BY fecha_hora DESC;
SELECT * FROM sesion333es ORDER BY fecha_inicio DESC;

-- Crear usuario inicial

INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado)
VALUES ('1234', 'ALEXIS', '1', '1', 'ADMINISTRADOR', true);

INSERT INTO clientes(nombre,direccion,telefono,email) VALUES
('ALEXIS','enpapa','29345543','ac991623438.com');
