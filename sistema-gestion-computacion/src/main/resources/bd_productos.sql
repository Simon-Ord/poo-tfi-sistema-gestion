-- ======================
-- TABLA DE CATEGORÍA
-- ======================
CREATE TABLE categorias (
    id_categoria SERIAL PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL UNIQUE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM categorias;
-- ======================
-- TABLA DE PRODUCTOS 
-- ======================
CREATE TABLE productos (
    id_producto SERIAL PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion_producto TEXT,
    stock_producto INT NOT NULL DEFAULT 0,
    precio_producto NUMERIC(10,2) NOT NULL,
    categoria_id INTEGER REFERENCES categorias(id_categoria),
    codigo_producto INT UNIQUE NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- <--- CAMPO AÑADIDO
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM productos;
-- =================================
-- TABLA DE PROVEEDORES DIGITALES
-- =================================
CREATE TABLE proveedores_digitales (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM proveedores_digitales;
-- ======================
-- TABLA DE FABRICANTE
-- ======================
CREATE TABLE fabricantes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM fabricantes;

-- ===========================
-- TABLA PRODUCTOS FÍSICOS
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
-- TABLA PRODUCTOS DIGITALES
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


-- Insertar categorías de ejemplo para la tienda de computacion
INSERT INTO categorias (nombre_categoria) VALUES
('Computadoras'), ('Notebooks'), ('Periféricos'), ('Componentes'), ('Accesorios'), ('Software'),
('Monitores'), ('Almacenamiento');






