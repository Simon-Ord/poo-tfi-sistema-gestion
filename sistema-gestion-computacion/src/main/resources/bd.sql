-- Crear la base de datos (solo si no existe)
CREATE DATABASE tienda_computacion;

 \c tienda_computacion; -- conectar a la base de datos 
SELECT current_database(); -- Verificar que estamos en la base de datos correcta

-----------------------
-- TABLA DE USUARIOS --
-----------------------
CREATE TABLE  usuarios (
    id SERIAL PRIMARY KEY,
    legajo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM usuarios;
-- Insertar usuarios de ejemplo
INSERT INTO usuarios (legajo, nombre, usuario, contraseña, rol, estado)
VALUES 
('A001', 'Administrador', 'admin', 'admin123', 'ADMIN', TRUE),
('V001', 'María López', 'mlopez', 'venta2024', 'VENDEDOR', TRUE),
('V002', 'Juan Pérez', 'jperez', 'venta2024', 'VENDEDOR', TRUE),
('T001', 'Sofía Ramírez', 'sramirez', 'tecnico2024', 'TECNICO', TRUE);

------------------------
-- TABLA DE PRODUCTOS --
------------------------
<<<<<<< HEAD
CREATE TABLE productos (
=======
CREATE TABLE IF NOT EXISTS productos (
>>>>>>> b0f1eeaff4775a466d15ab0af2055eba7b65ee49
    id_producto SERIAL PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion_producto TEXT,
    stock_producto INT NOT NULL DEFAULT 0,
    precio_producto NUMERIC(10,2) NOT NULL,
    categoria_producto VARCHAR(50),
    fabricante_producto VARCHAR(100),
    codigo_producto INT UNIQUE NOT NULL
);
-- Insertar productos de ejemplo
INSERT INTO productos (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto,
fabricante_producto, codigo_producto)
VALUES
('Laptop Dell XPS 13', 'Laptop ultradelgada con pantalla de 13 pulgadas', 10, 999.99, 'Laptops', 'Dell', 1001),
('Monitor Samsung 24"', 'Monitor LED de 24 pulgadas con resolución Full HD', 15, 149.99, 'Monitores', 'Samsung', 1002),
('Teclado Mecánico Logitech', 'Teclado mecánico retroiluminado para gaming', 20, 89.99, 'Periféricos', 'Logitech', 1003),
('Mouse Inalámbrico HP', 'Mouse inalámbrico ergonómico con alta precisión', 25, 29.99, 'Periféricos', 'HP', 1004),
('Impresora Canon Pixma', 'Impresora multifuncional con conectividad Wi-Fi', 8, 199.99, 'Impresoras', 'Canon', 1005);
-- VERIFICAR CONTENIDO DE LA TABLA
<<<<<<< HEAD
SELECT * FROM productos;
=======
SELECT * FROM productos;
>>>>>>> b0f1eeaff4775a466d15ab0af2055eba7b65ee49
