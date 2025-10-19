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
    codigo_producto INT UNIQUE NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    activo BOOLEAN DEFAULT TRUE
);
-- Insertar productos de ejemplo
INSERT INTO productos (nombre_producto, descripcion_producto, stock_producto, precio_producto, categoria_producto, fabricante_producto, codigo_producto, estado, activo)
VALUES
('Laptop XYZ', 'Laptop de alto rendimiento', 10, 1500.00, 'Computadoras', 'TechCorp', 1001, TRUE, TRUE),
('Mouse Inalámbrico', 'Mouse ergonómico inalámbrico', 50, 25.99, 'Periféricos', 'GadgetPro', 1002, TRUE, TRUE),
('Teclado Mecánico', 'Teclado mecánico retroiluminado', 30, 75.50, 'Periféricos', 'KeyMasters', 1003, TRUE, TRUE);

-- VERIFICAR CONTENIDO DE LA TABLA
SELECT * FROM productos;
