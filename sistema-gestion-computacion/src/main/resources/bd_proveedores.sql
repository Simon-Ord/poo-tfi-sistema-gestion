-- =============================================================
-- TABLA DE PROVEEDORES
-- =============================================================

CREATE TABLE IF NOT EXISTS proveedores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(40),
    email VARCHAR(100),
    direccion VARCHAR(200),
    tipo VARCHAR(50) DEFAULT 'FISICO' CHECK (tipo IN ('DIGITAL','FISICO')),
    activo BOOLEAN DEFAULT TRUE
);

-- Nota: Este archivo es legado. La definición centralizada se encuentra en `bd.sql`.
-- Mantener consistencia: preferir ejecutar `bd.sql` como script de inicialización principal.