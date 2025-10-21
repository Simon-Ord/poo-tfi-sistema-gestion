-- =============================================================
-- 🎯 CREACIÓN DE ESTRUCTURA DE TABLAS
-- =============================================================

-- 🧍‍♂️ TABLA DE USUARIOS
CREATE TABLE usuarios (
    dni VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    rol VARCHAR(50) NOT NULL CHECK (rol IN ('ADMINISTRADOR', 'USER')),
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 🧾 TABLA DE CLIENTES
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    telefono VARCHAR(30),
    email VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 📦 TABLA DE PRODUCTOS (ACTUALIZADA con la estructura del modelo Java)
-- 📦 TABLA DE PRODUCTOS (Recomendación con fecha_creacion)
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
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- <--- CAMPO AÑADIDO
);
-- Insertar productos de ejemplo
INSERT INTO productos (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto, fabricante_producto, codigo_producto, activo, fecha_creacion)
VALUES 
('Laptop XYZ', 'Laptop de alto rendimiento', 10, 1500.00, 'Computadoras', 'TechCorp', 1001, TRUE, CURRENT_TIMESTAMP),
('Mouse Inalámbrico', 'Mouse ergonómico inalámbrico', 50, 25.99, 'Periféricos', 'GadgetPro', 1002, TRUE, CURRENT_TIMESTAMP),
('Teclado Mecánico', 'Teclado mecánico retroiluminado', 30, 75.50, 'Periféricos', 'KeyMasters', 1003, TRUE, CURRENT_TIMESTAMP);


-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM productos;
-- =============================================================
-- 💰  TABLA DE FACTURAS (simplificada)
-- =============================================================

-- 💰 TABLA DE FACTURAS (simplificada)
CREATE TABLE facturas (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cliente_id INT,
    total DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- 🧩 TABLA DE DETALLES DE FACTURA
CREATE TABLE detalle_factura (
    id SERIAL PRIMARY KEY,
    factura_id INT,
    id_producto INT,
    producto_id INT, -- Referencia a la clave primaria de productos
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id),
    -- CORREGIDO: Ahora referencia id_producto de la tabla productos
    FOREIGN KEY (producto_id) REFERENCES productos(id_producto) 
);

-- 🕵️‍♂️ TABLA DE AUDITORÍA
CREATE TABLE auditoria (
    id SERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(50) NOT NULL,
    accion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    entidad_afectada VARCHAR(50),
    id_referencia VARCHAR(50),
    ip_origen VARCHAR(50),
    exito BOOLEAN DEFAULT TRUE
);

-- =============================================================
-- 🔐 INSERCIÓN DE DATOS INICIALES
-- =============================================================
INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado)
VALUES
('12345678', 'Admin User', 'admin', 'admin', 'ADMINISTRADOR', TRUE),
('87654321', 'John Doe', 'johndoe', 'password123', 'USER', TRUE),
('11223344', 'Jane Smith', 'janesmith', 'mypassword', 'USER', FALSE);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM usuarios;


-- =============================================================
-- ✅ FUNCIONES Y TRIGGERS DE AUDITORÍA PARA PRODUCTOS (CORREGIDOS)
-- =============================================================

-- Función para registrar INSERT de productos (CORREGIDA)
CREATE OR REPLACE FUNCTION log_producto_insert() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (
        CURRENT_USER, 
        'CREAR PRODUCTO', 
        CONCAT('Se creó el producto: ', NEW.nombre_producto), 
        'producto', 
        NEW.id_producto
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_insert
AFTER INSERT ON productos
FOR EACH ROW
EXECUTE FUNCTION log_producto_insert();

-- Función para registrar UPDATE de productos (CORREGIDA)
CREATE OR REPLACE FUNCTION log_producto_update() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (
        CURRENT_USER, 
        'MODIFICAR PRODUCTO', 
        CONCAT('Se modificó el producto: ', NEW.nombre_producto), 
        'producto', 
        NEW.id_producto
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_update
AFTER UPDATE ON productos
FOR EACH ROW
WHEN (OLD.* IS DISTINCT FROM NEW.*) -- Optimización: solo dispara si hay algún cambio real
EXECUTE FUNCTION log_producto_update();

-- Función para registrar DELETE de productos (CORREGIDA)
CREATE OR REPLACE FUNCTION log_producto_delete() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (
        CURRENT_USER, 
        'ELIMINAR PRODUCTO', 
        CONCAT('Se eliminó el producto: ', OLD.nombre_producto), 
        'producto', 
        OLD.id_producto
    );
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_delete
AFTER DELETE ON productos
FOR EACH ROW
EXECUTE FUNCTION log_producto_delete();


-- =============================================================
-- ✅ FUNCIONES Y TRIGGERS DE AUDITORÍA PARA USUARIOS (SIN CAMBIOS)
-- =============================================================

-- Función para registrar INSERT de usuarios
CREATE OR REPLACE FUNCTION log_usuario_insert() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
    VALUES (CURRENT_USER, 'CREAR USUARIO', CONCAT('Se creó el usuario: ', NEW.usuario, ' con rol: ', NEW.rol), 'usuario', NEW.dni);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_usuario_insert
AFTER INSERT ON usuarios
FOR EACH ROW
EXECUTE FUNCTION log_usuario_insert();


-- Función para registrar UPDATE de usuarios
CREATE OR REPLACE FUNCTION log_usuario_update() RETURNS TRIGGER AS $$
DECLARE
    cambios TEXT;
BEGIN
    cambios := '';
    
    -- Detectar cambios en campos importantes:
    IF OLD.nombre IS DISTINCT FROM NEW.nombre THEN
        cambios := cambios || CONCAT('Nombre (Antiguo: ', OLD.nombre, ', Nuevo: ', NEW.nombre, '); ');
    END IF;
    
    IF OLD.contraseña IS DISTINCT FROM NEW.contraseña THEN
        cambios := cambios || 'Contraseña (MODIFICADA); ';
    END IF;
    
    IF OLD.rol IS DISTINCT FROM NEW.rol THEN
        cambios := cambios || CONCAT('Rol (Antiguo: ', OLD.rol, ', Nuevo: ', NEW.rol, '); ');
    END IF;
    
    IF OLD.estado IS DISTINCT FROM NEW.estado THEN
        cambios := cambios || CONCAT('Estado (Antiguo: ', OLD.estado, ', Nuevo: ', NEW.estado, '); ');
    END IF;

    -- Solo inserta el registro si se detectó algún cambio
    IF cambios <> '' THEN
        INSERT INTO auditoria (usuario, accion, descripcion, entidad_afectada, id_referencia)
        VALUES (
            CURRENT_USER,
            'MODIFICAR USUARIO', 
            CONCAT('Se modificó el usuario ', OLD.usuario, '. Cambios: ', cambios), 
            'usuario', 
            NEW.dni
        );
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_usuario_update
AFTER UPDATE ON usuarios
FOR EACH ROW
WHEN (OLD.* IS DISTINCT FROM NEW.*) 
EXECUTE FUNCTION log_usuario_update();