-- Añadir columna CUIT a proveedores
ALTER TABLE proveedores ADD COLUMN IF NOT EXISTS cuit VARCHAR(11);

-- Actualizar secuencia si es necesario
SELECT setval(pg_get_serial_sequence('proveedores','id'),
              COALESCE((SELECT MAX(id) FROM proveedores), 1), true);