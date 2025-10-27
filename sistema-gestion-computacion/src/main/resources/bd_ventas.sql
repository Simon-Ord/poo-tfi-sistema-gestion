-- =============================================================
-- 💰 TABLA DE VENTAS/FACTURAS (Completa para el sistema)
-- =============================================================

-- Crear la tabla de ventas mejorada
CREATE TABLE ventas (
    id SERIAL PRIMARY KEY,
    codigo_venta VARCHAR(50) UNIQUE NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cliente_id INT,
    tipo_factura VARCHAR(20) NOT NULL CHECK (tipo_factura IN ('FACTURA', 'TICKET')),
    metodo_pago VARCHAR(50) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    iva DECIMAL(12,2) NOT NULL DEFAULT 0,
    total DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- ==============================
-- 📄 TABLA DE DETALLE DE VENTA
-- ==============================
CREATE TABLE detalle_venta (
    id SERIAL PRIMARY KEY,
    venta_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id_producto)
);
