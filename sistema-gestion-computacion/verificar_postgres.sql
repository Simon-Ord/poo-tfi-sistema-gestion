-- Verificar conectividad PostgreSQL
-- Ejecutar estos comandos en psql o pgAdmin:

-- 1. Verificar que el usuario postgres existe
SELECT usename FROM pg_user WHERE usename = 'postgres';

-- 2. Verificar que la base de datos existe
SELECT datname FROM pg_database WHERE datname = 'tienda_computacion';

-- 3. Crear base de datos si no existe
-- CREATE DATABASE tienda_computacion;

-- 4. Cambiar contraseña del usuario postgres (si es necesario)
-- ALTER USER postgres PASSWORD 'nueva_contraseña';

-- Contraseñas comunes para probar:
-- postgres
-- admin  
-- 123456
-- root
-- password