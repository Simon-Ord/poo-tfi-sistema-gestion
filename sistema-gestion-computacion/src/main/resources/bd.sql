
-- =============================================================
-- 🧍‍♂️  TABLA DE USUARIOS
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

-- =============================================================
-- 🧾  TABLA DE CLIENTES (si la usás)
-- =============================================================
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    telefono VARCHAR(30),
    email VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================
-- 📦  TABLA DE PRODUCTOS
-- =============================================================
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================
-- 💰  TABLA DE FACTURAS (simplificada)
-- =============================================================
CREATE TABLE facturas (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cliente_id INT,
    total DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- =============================================================
-- 🧩  TABLA DE DETALLES DE FACTURA (opcional)
-- =============================================================
CREATE TABLE detalle_factura (
    id SERIAL PRIMARY KEY,
    factura_id INT,
    producto_id INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- =============================================================
-- 🕵️‍♂️  TABLA DE AUDITORÍA (REPORTES)
-- =============================================================
CREATE TABLE auditoria (
    id SERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(50) NOT NULL,
    accion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    entidad_afectada VARCHAR(50),        -- por ejemplo: 'usuario', 'producto'
    id_referencia VARCHAR(50),           -- ej: DNI o ID del elemento modificado
    ip_origen VARCHAR(50),               -- opcional: IP del cliente
    exito BOOLEAN DEFAULT TRUE           -- si la acción fue exitosa
);

-- =============================================================
-- 🔐  USUARIO DE PRUEBA (ADMIN)
-- =============================================================
INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado)
VALUES ('12345678', 'Admin User', 'admin', 'admin', 'ADMINISTRADOR', TRUE);

-- =============================================================
-- ✅  TRIGGERS DE AUDITORÍA OPCIONALES (para automatizar)
-- =============================================================

-- Trigger para registrar cada vez que se agrega un producto nuevo
CREATE OR REPLACE FUNCTION log_producto_insert() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (CURRENT_USER, 'CREAR PRODUCTO', CONCAT('Se creó el producto: ', NEW.nombre), 'producto', NEW.id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_insert
AFTER INSERT ON productos
FOR EACH ROW
EXECUTE FUNCTION log_producto_insert();


-- Trigger para registrar modificaciones
CREATE OR REPLACE FUNCTION log_producto_update() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (CURRENT_USER, 'MODIFICAR PRODUCTO', CONCAT('Se modificó el producto: ', NEW.nombre), 'producto', NEW.id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_update
AFTER UPDATE ON productos
FOR EACH ROW
EXECUTE FUNCTION log_producto_update();


-- Trigger para registrar "borrados lógicos" (desactivación)
CREATE OR REPLACE FUNCTION log_producto_delete() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (CURRENT_USER, 'DESACTIVAR PRODUCTO', CONCAT('Se desactivó el producto: ', OLD.nombre), 'producto', OLD.id);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_delete
AFTER DELETE ON productos
FOR EACH ROW
EXECUTE FUNCTION log_producto_delete();

-- =============================================================
-- 📊 CONSULTAS DE EJEMPLO PARA REPORTES
-- =============================================================
-- Últimas acciones realizadas
SELECT * FROM auditoria ORDER BY fecha_hora DESC LIMIT 20;

-- Acciones por tipo
SELECT accion, COUNT(*) AS cantidad
FROM auditoria
GROUP BY accion
ORDER BY cantidad DESC;

-- Acciones por usuario
SELECT usuario, COUNT(*) AS cantidad
FROM auditoria
GROUP BY usuario
ORDER BY cantidad DESC;
