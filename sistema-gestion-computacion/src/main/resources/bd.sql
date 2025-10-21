-- =============================================================
-- 🧱 TABLAS PRINCIPALES DEL SISTEMA
-- =============================================================

CREATE TABLE usuarios (
    dni VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    rol VARCHAR(50) NOT NULL CHECK (rol IN ('ADMINISTRADOR', 'USER')),
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    telefono VARCHAR(30),
    email VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE productos (
    id_producto SERIAL PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion_producto TEXT,
    stock_producto INT NOT NULL DEFAULT 0,
    precio_producto NUMERIC(10,2) NOT NULL,
    categoria_producto VARCHAR(50),
    fabricante_producto VARCHAR(100),
    codigo_producto INT UNIQUE NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ✅ Insertar productos de ejemplo
INSERT INTO productos (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto, fabricante_producto, codigo_producto, activo, fecha_creacion)
VALUES 
('Laptop XYZ', 'Laptop de alto rendimiento', 10, 1500.00, 'Computadoras', 'TechCorp', 1001, TRUE, CURRENT_TIMESTAMP),
('Mouse Inalámbrico', 'Mouse ergonómico inalámbrico', 50, 25.99, 'Periféricos', 'GadgetPro', 1002, TRUE, CURRENT_TIMESTAMP),
('Teclado Mecánico', 'Teclado mecánico retroiluminado', 30, 75.50, 'Periféricos', 'KeyMasters', 1003, TRUE, CURRENT_TIMESTAMP);


-- =============================================================
-- 💰 TABLA DE FACTURAS (simplificada)
-- =============================================================

CREATE TABLE facturas (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cliente_id INT,
    total DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE detalle_factura (
    id SERIAL PRIMARY KEY,
    factura_id INT,
    producto_id INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id_producto)
);


-- =============================================================
-- 🕵️‍♂️ TABLA DE AUDITORÍA (registrada por Java)
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
-- 🕓 TABLA DE SESIONES (controladas desde Java)
-- =============================================================

CREATE TABLE sesiones (
    id SERIAL PRIMARY KEY,
    usuario VARCHAR(100) NOT NULL,  -- nombre o usuario Java
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA', 'CERRADA'))
);


-- =============================================================
-- 🔐 USUARIO ADMINISTRADOR INICIAL
-- =============================================================

INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado)
VALUES ('1234', 'ALEXIS', '1', '1', 'ADMINISTRADOR', true);

-- =============================================================
-- 🔍 CONSULTAS DE PRUEBA
-- =============================================================

SELECT * FROM usuarios;
SELECT * FROM productos;
SELECT * FROM auditoria ORDER BY fecha_hora DESC;
SELECT * FROM sesiones ORDER BY fecha_inicio DESC;