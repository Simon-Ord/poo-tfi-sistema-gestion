-- ================================================================
-- Script para agregar columna email a tabla usuarios existente
-- Ejecutar SOLAMENTE si la tabla usuarios ya existe sin email
-- ================================================================

-- Agregar columna email si no existe
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name='usuarios' AND column_name='email'
    ) THEN
        ALTER TABLE usuarios ADD COLUMN email VARCHAR(120);
        RAISE NOTICE 'Columna email agregada exitosamente';
    ELSE
        RAISE NOTICE 'La columna email ya existe';
    END IF;
END $$;

-- Verificar
SELECT column_name, data_type, character_maximum_length 
FROM information_schema.columns 
WHERE table_name = 'usuarios' 
ORDER BY ordinal_position;
