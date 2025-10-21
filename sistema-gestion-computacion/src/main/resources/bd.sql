-- =============================================================
--  🧱 TABLAS PRINCIPALES DEL SISTEMA
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
    id_producto SERIAL PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion_producto TEXT,
    stock_producto INT NOT NULL DEFAULT 0,
    precio_producto NUMERIC(10,2) NOT NULL,
    categoria_producto VARCHAR(50),
    fabricante_producto VARCHAR(100),
    codigo_producto INT UNIQUE NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    activo BOOLEAN DEFAULT TRUE
);
-- Insertar productos de ejemplo
INSERT INTO productos (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto, fabricante_producto, codigo_producto, estado, activo)
VALUES
('Laptop XYZ', 'Laptop de alta gama con 16GB RAM y 512GB SSD', 10, 1200.00, 'Electrónica', 'TechCorp', 1001, TRUE, TRUE),
('Smartphone ABC', 'Smartphone con cámara de 48MP y pantalla OLED', 25, 800.00, 'Electrónica', 'PhoneMakers', 1002, TRUE, TRUE),
('Auriculares Inalámbricos', 'Auriculares con cancelación de ruido y Bluetooth 5.0', 50, 150.00, 'Accesorios', 'SoundWave', 1003, TRUE, TRUE),
('Monitor 4K Ultra HD', 'Monitor de 27 pulgadas con resolución 4K y HDR', 15, 400.00, 'Electrónica', 'DisplayTech', 1004, TRUE, TRUE),
('Teclado Mecánico RGB', 'Teclado mecánico con retroiluminación RGB y switches táctiles', 30, 100.00, 'Accesorios', 'KeyMasters', 1005, TRUE, TRUE);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM productos;
-- =============================================================
-- 💰  TABLA DE FACTURAS (simplificada)
-- =============================================================
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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
<<<<<<< HEAD
    producto_id INT,
=======
    id_producto INT,
    producto_id INT, -- Referencia a la clave primaria de productos
>>>>>>> 96994ef3a83f7c6aad22d560e4f2fadd36b1ae55
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id_producto)
);

-- =============================================================
-- 🕵️‍♂️ TABLA DE AUDITORÍA
-- =============================================================
CREATE TABLE auditoria (
    id SERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(100) NOT NULL,
    accion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    entidad_afectada VARCHAR(50),
    id_referencia VARCHAR(50)
);

-- =============================================================
-- 🕓 TABLA DE SESIONES
-- =============================================================
<<<<<<< HEAD
CREATE TABLE sesiones (
    id SERIAL PRIMARY KEY,
    usuario VARCHAR(100) NOT NULL,
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA', 'CERRADA'))
);
=======
INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado)
VALUES
('12345678', 'Admin User', 'admin', 'admin', 'ADMINISTRADOR', TRUE),
('87654321', 'John Doe', 'johndoe', 'password123', 'USER', TRUE),
('11223344', 'Jane Smith', 'janesmith', 'mypassword', 'USER', FALSE);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM usuarios;





-- Insertar productos de ejemplo
INSERT INTO productos (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto, fabricante_producto, codigo_producto, estado, activo)
VALUES
('Laptop XYZ', 'Laptop de alto rendimiento', 10, 1500.00, 'Computadoras', 'TechCorp', 1001, TRUE, TRUE),
('Mouse Inalámbrico', 'Mouse ergonómico inalámbrico', 50, 25.99, 'Periféricos', 'GadgetPro', 1002, TRUE, TRUE),
('Teclado Mecánico', 'Teclado mecánico retroiluminado', 30, 75.50, 'Periféricos', 'KeyMasters', 1003, TRUE, TRUE);
>>>>>>> 96994ef3a83f7c6aad22d560e4f2fadd36b1ae55

-- =============================================================
-- ⚙️ FUNCIONES Y TRIGGERS DE AUDITORÍA (solo productos y sesiones)
-- =============================================================

-- ✅ Productos
CREATE OR REPLACE FUNCTION log_producto_insert() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (CURRENT_USER, 'CREAR PRODUCTO', CONCAT('Se creó el producto: ', NEW.nombre_producto), 'producto', NEW.id_producto::TEXT);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_insert
AFTER INSERT ON productos
FOR EACH ROW
EXECUTE FUNCTION log_producto_insert();

CREATE OR REPLACE FUNCTION log_producto_update() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (CURRENT_USER, 'MODIFICAR PRODUCTO', CONCAT('Se modificó el producto: ', NEW.nombre_producto), 'producto', NEW.id_producto::TEXT);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_update
AFTER UPDATE ON productos
FOR EACH ROW
WHEN (OLD.* IS DISTINCT FROM NEW.*)
EXECUTE FUNCTION log_producto_update();

CREATE OR REPLACE FUNCTION log_producto_delete() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (CURRENT_USER, 'ELIMINAR PRODUCTO', CONCAT('Se eliminó el producto: ', OLD.nombre_producto), 'producto', OLD.id_producto::TEXT);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_delete
AFTER DELETE ON productos
FOR EACH ROW
EXECUTE FUNCTION log_producto_delete();

-- ✅ Sesiones (solo PostgreSQL registra estos por consistencia)
CREATE OR REPLACE FUNCTION log_sesion_insert() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (NEW.usuario, 'INICIO DE SESIÓN', 'Inicio de sesión registrado por la app', 'sesion', NEW.id::TEXT);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_sesion_insert
AFTER INSERT ON sesiones
FOR EACH ROW
EXECUTE FUNCTION log_sesion_insert();

CREATE OR REPLACE FUNCTION log_sesion_update() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.estado = 'CERRADA' THEN
        INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
        VALUES (NEW.usuario, 'CIERRE DE SESIÓN', 'Cierre de sesión registrado por la app', 'sesion', NEW.id::TEXT);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_sesion_update
AFTER UPDATE ON sesiones
FOR EACH ROW
WHEN (NEW.estado = 'CERRADA')
EXECUTE FUNCTION log_sesion_update();

-- =============================================================
-- 🔍 CONSULTAS DE PRUEBA
-- =============================================================

SELECT * FROM auditoria ORDER BY fecha_hora DESC;
SELECT * FROM sesiones ORDER BY fecha_inicio DESC;

-- Crear usuario inicial
 INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado) VALUES
 ('12345678', 'alexis coronel', '1', '1', 'ADMINISTRADOR', TRUE),
 ('87654321', 'Usuario Normal', 'user1', 'userpass', 'USER', TRUE);


-- =============================================================
--  ELIMINAR LOS TRIGGERS Y FUNCIONES DE PRODUCTO
-- =============================================================

DROP TRIGGER IF EXISTS trg_producto_insert ON productos;
DROP TRIGGER IF EXISTS trg_producto_update ON productos;
DROP TRIGGER IF EXISTS trg_producto_delete ON productos;

DROP FUNCTION IF EXISTS log_producto_insert();
DROP FUNCTION IF EXISTS log_producto_update();
DROP FUNCTION IF EXISTS log_producto_delete();

