-- Reparación de proveedores: añade columna `tipo`, agrega la constraint CHECK,
-- crea tabla `proveedores_digitales` y migra proveedores digitales si aplica.
-- Idempotente: puede ejecutarse varias veces sin causar errores.

BEGIN;

-- 1) Añadir columna 'tipo' con valor por defecto si no existe
ALTER TABLE proveedores ADD COLUMN IF NOT EXISTS tipo VARCHAR(50) DEFAULT 'FISICO';

-- 2) Normalizar valores nulos a 'FISICO'
UPDATE proveedores SET tipo = 'FISICO' WHERE tipo IS NULL;

-- 3) Agregar constraint CHECK si no existe (nombre proveedores_tipo_chk)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint c
        JOIN pg_class t ON c.conrelid = t.oid
        WHERE t.relname = 'proveedores' AND c.conname = 'proveedores_tipo_chk'
    ) THEN
       ALTER TABLE proveedores ADD CONSTRAINT proveedores_tipo_chk CHECK (tipo IN ('DIGITAL','FISICO'));
    END IF;
END$$;

-- 4) Crear tabla proveedores_digitales (compatibilidad con productos)
CREATE TABLE IF NOT EXISTS proveedores_digitales (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- 5) Migrar proveedores digitales desde proveedores (si no existen en proveedores_digitales)
INSERT INTO proveedores_digitales (id, nombre)
SELECT id, nombre FROM proveedores
 WHERE tipo = 'DIGITAL'
   AND NOT EXISTS (SELECT 1 FROM proveedores_digitales pd WHERE pd.id = proveedores.id);

-- 6) Ajustar secuencia para evitar colisiones
SELECT setval(pg_get_serial_sequence('proveedores_digitales','id'),
              COALESCE((SELECT MAX(id) FROM proveedores_digitales), 1), true);

COMMIT;
