=
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
VALUES ('12', 'Marcos David', '1', '1', 'ADMINISTRADOR', true);

-- =============================================================
-- CONSULTAS DE PRUEBA
-- =============================================================

SELECT * FROM clientes;
SELECT * FROM productos;
SELECT * FROM auditoria ORDER BY fecha_hora DESC;
SELECT * FROM sesion333es ORDER BY fecha_inicio DESC;
-- =============================================================
-- TABLA DE PROVEEDORES (integrada)
-- =============================================================

CREATE TABLE IF NOT EXISTS proveedores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(40),
    email VARCHAR(100),
    direccion VARCHAR(200),
    cuit VARCHAR(20),
    tipo VARCHAR(50) DEFAULT 'FISICO' CHECK (tipo IN ('DIGITAL','FISICO')),
    activo BOOLEAN DEFAULT TRUE
);

-- =============================================================
-- TABLA DE PROVEEDORES DIGITALES 
-- =============================================================

CREATE TABLE IF NOT EXISTS proveedores_digitales (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Migrar proveedores digitales desde la tabla unificada (si existen)
INSERT INTO proveedores_digitales (id, nombre)
SELECT id, nombre FROM proveedores WHERE tipo = 'DIGITAL'
    AND NOT EXISTS (SELECT 1 FROM proveedores_digitales pd WHERE pd.id = proveedores.id);

-- Actualizar secuencia para evitar conflictos en futuros INSERTs
SELECT setval(pg_get_serial_sequence('proveedores_digitales','id'), COALESCE((SELECT MAX(id) FROM proveedores_digitales), 1), true);

-- =============================================================
-- TABLA DE FABRICANTES 
-- =============================================================

CREATE TABLE IF NOT EXISTS fabricantes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Insertar fabricantes de ejemplo si no existen
INSERT INTO fabricantes (nombre)
SELECT v.name FROM (VALUES ('Dell'), ('HP'), ('Lenovo'), ('Asus'), ('Acer')) AS v(name)
WHERE NOT EXISTS (SELECT 1 FROM fabricantes f WHERE f.nombre = v.name);

-- Actualizar secuencia de fabricantes
SELECT setval(pg_get_serial_sequence('fabricantes','id'), COALESCE((SELECT MAX(id) FROM fabricantes), 1), true);

-- 