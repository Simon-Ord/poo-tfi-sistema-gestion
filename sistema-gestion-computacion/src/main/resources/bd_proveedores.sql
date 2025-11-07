-- =============================================================
-- TABLA DE PROVEEDORES
-- =============================================================

CREATE TABLE IF NOT EXISTS proveedores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(40),
    email VARCHAR(100),
    direccion VARCHAR(200),
    activo BOOLEAN DEFAULT TRUE
);