-- ======================
-- 🗂️ TABLA DE CATEGORÍA
-- ======================
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM categorias;
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
    ('Monitores'), ('Mouses'), ('Teclados'), ('Periféricos'), ('Componentes'),
    ('Accesorios'), ('Sin Categoría'), ('Software');
-- Insertar productos de ejemplo
INSERT INTO productos (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_id, codigo_producto, activo)
VALUES 
    ('Monitor LG 24''', 'Monitor Full HD de 24 pulgadas', 15, 199.99, 1, 1001, TRUE),
    ('Mouse Logitech MX Master 3', 'Mouse inalámbrico de alta precisión', 30, 99.99, 2, 1002, TRUE),
    ('Teclado Mecánico Corsair K70', 'Teclado mecánico retroiluminado', 20, 129.99, 3, 1003, TRUE),
    ('Disco Duro SSD Samsung 1TB', 'Unidad de estado sólido de 1TB', 10, 149.99, 5, 1004, TRUE),
    ('Software Antivirus Norton', 'Protección completa contra virus y malware', 50, 39.99, 8, 1005, TRUE);







