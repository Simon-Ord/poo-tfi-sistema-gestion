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
-- ====================
-- 🧑‍🤝‍🧑 TABLA DE CLIENTES
-- ====================
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    telefono VARCHAR(30),
    email VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- ======================
-- 🛒 TABLA DE PRODUCTOS 
--  =====================
CREATE TABLE productos (
    id_producto SERIAL PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion_producto TEXT,
    stock_producto INT NOT NULL DEFAULT 0,
    precio_producto NUMERIC(10,2) NOT NULL,
    categoria_id INTEGER REFERENCES categorias(id),
    codigo_producto INT UNIQUE NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- <--- CAMPO AÑADIDO
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM productos;
-- =================================
-- 🏢 TABLA DE PROVEEDORES DIGITALES
-- =================================
CREATE TABLE proveedores_digitales (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM proveedores_digitales;
-- ======================
-- 🏭 TABLA DE FABRICANTE
-- ======================
CREATE TABLE fabricantes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM fabricantes;
-- ======================
-- 🗂️ TABLA DE CATEGORÍA
-- ======================
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM categorias;
-- ===========================
-- 📦 TABLA PRODUCTOS FÍSICOS
-- ===========================
CREATE TABLE productos_fisicos (
    id_producto INT PRIMARY KEY,
    id_fabricante INT,
    garantia_meses INT, -- Null si no tiene garantía
    tipo_garantia VARCHAR(20), -- 'FABRICANTE' o 'TIENDA', null si no tiene
    estado_fisico VARCHAR(20) NOT NULL, -- 'NUEVO', 'USADO', 'REACONDICIONADO'
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE,
    FOREIGN KEY (id_fabricante) REFERENCES fabricantes(id)
);
-- ============================
-- 💾 TABLA PRODUCTOS DIGITALES
-- ============================
CREATE TABLE productos_digitales (
    id_producto INT PRIMARY KEY,
    id_proveedor_digital INT,
    tipo_licencia VARCHAR(20) NOT NULL, -- 'PERPETUA', 'SUSCRIPCION', 'TRIAL'
    activaciones_max INT,
    duracion_licencia_dias INT, -- Null si es perpetua
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE,
    FOREIGN KEY (id_proveedor_digital) REFERENCES proveedores_digitales(id)
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
-- ==============================
-- 📄 TABLA DE DETALLE DE FACTURA
-- ==============================
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
-- 🕵️‍♂️ TABLA DE AUDITORÍA
-- =============================================================
CREATE TABLE auditoria (
    id SERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(100) NOT NULL,         -- usuario Java, no postgres
    accion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    entidad_afectada VARCHAR(50),
    id_referencia VARCHAR(50)
);

-- =============================================================
-- 🕓 TABLA DE SESIONES
-- =============================================================
CREATE TABLE sesiones (
    id SERIAL PRIMARY KEY,
    usuario VARCHAR(100) NOT NULL,         -- nombre o usuario Java
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA', 'CERRADA'))
);

-- =============================================================
-- ⚙️ FUNCIONES Y TRIGGERS DE AUDITORÍA (solo productos y sesiones)
-- =============================================================

-- ✅ Productos (usa NEW.usuario_producto que debe venir desde Java)
ALTER TABLE productos ADD COLUMN usuario_producto VARCHAR(100);

CREATE OR REPLACE FUNCTION log_producto_insert() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (NEW.usuario_producto, 'CREAR PRODUCTO', CONCAT('Se creó el producto: ', NEW.nombre_producto), 'producto', NEW.id_producto::TEXT);
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
    VALUES (NEW.usuario_producto, 'MODIFICAR PRODUCTO', CONCAT('Se modificó el producto: ', NEW.nombre_producto), 'producto', NEW.id_producto::TEXT);
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
    VALUES (OLD.usuario_producto, 'ELIMINAR PRODUCTO', CONCAT('Se eliminó el producto: ', OLD.nombre_producto), 'producto', OLD.id_producto::TEXT);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_delete
AFTER DELETE ON productos
FOR EACH ROW
EXECUTE FUNCTION log_producto_delete();

-- ✅ Sesiones (solo usa usuario Java)
CREATE OR REPLACE FUNCTION log_sesion_insert() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (NEW.usuario, 'INICIO DE SESIÓN', 'Inicio de sesión desde la app Java', 'sesion', NEW.id::TEXT);
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
        VALUES (NEW.usuario, 'CIERRE DE SESIÓN', 'Cierre de sesión desde la app Java', 'sesion', NEW.id::TEXT);
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

INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado)
VALUES ('1234', 'ALEXIS', '1', '1', 'ADMINISTRADOR', true);









-- ============================================================
-- Insertar datos de prueba generales (Nombres reales)
-- =============================================================

-- Insertar proveedores digitales de ejemplo
INSERT INTO proveedores_digitales (nombre) VALUES
('Steam'), ('Epic Games'), ('GOG'), ('Origin'), ('Uplay');
-- Insertar fabricantes de ejemplo
INSERT INTO fabricantes (nombre) VALUES
('Dell'), ('HP'), ('Lenovo'), ('Asus'), ('Acer');

-- Insertar categorías de ejemplo
INSERT INTO categorias (nombre) VALUES
('Computadoras'), ('Periféricos'), ('Componentes'), ('Accesorios'), ('Software');
-- Insertar productos de ejemplo
INSERT INTO productos (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto, codigo_producto, activo, fecha_creacion)
VALUES 
('Mouse Logitech MX Master 3', 'Mouse inalámbrico de alta precisión', 50, 99.99, 'Periféricos', 1001, true, CURRENT_TIMESTAMP),
('Teclado Mecánico Redragon K552', 'Teclado mecánico compacto con retroiluminación', 30, 79.99, 'Periféricos', 1002, true, CURRENT_TIMESTAMP),
('Disco Duro Externo Seagate 2TB', 'Disco duro portátil USB 3.0', 20, 89.99, 'Almacenamiento', 1003, true, CURRENT_TIMESTAMP),
('Monitor Samsung 24" FHD', 'Monitor LED Full HD con tecnología Eye Saver', 15, 149.99, 'Monitores', 1004, true, CURRENT_TIMESTAMP),
('Tarjeta Gráfica NVIDIA GeForce RTX 3060', 'Tarjeta gráfica para gaming y diseño', 10, 399.99, 'Componentes', 1005, true, CURRENT_TIMESTAMP);
