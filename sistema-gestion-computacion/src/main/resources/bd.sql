-- Crear la base de datos (solo si no existe)
CREATE DATABASE tienda_computacion;

 \c tienda_computacion; -- conectar a la base de datos 
SELECT current_database(); -- Verificar que estamos en la base de datos correcta

-----------------------
-- TABLA DE USUARIOS --
-----------------------
CREATE TABLE  usuarios (
    dni VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);

-- Insertar usuarios de ejemplo
INSERT INTO usuarios (dni, nombre, usuario, contraseña, rol, estado)
VALUES
('12345678', 'Admin User', 'admin', 'adminpass', 'ADMIN', TRUE),
('87654321', 'John Doe', 'johndoe', 'password123', 'USER', TRUE),
('11223344', 'Jane Smith', 'janesmith', 'mypassword', 'USER', FALSE);
-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM usuarios;


------------------------
-- TABLA DE PRODUCTOS --
------------------------
CREATE TABLE productos (
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
SELECT * FROM productos;
