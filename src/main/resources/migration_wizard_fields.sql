-- Migración para agregar campos del wizard avanzado a la tabla planes_ahorro
-- Ejecutar en la base de datos después de actualizar el backend

ALTER TABLE planes_ahorro 
ADD COLUMN sueldo_base DECIMAL(12,2) AFTER created_at,
ADD COLUMN otros_ingresos DECIMAL(12,2) DEFAULT 0.00 AFTER sueldo_base,
ADD COLUMN meta_principal VARCHAR(255) AFTER otros_ingresos,
ADD COLUMN monto_objetivo DECIMAL(12,2) AFTER meta_principal,
ADD COLUMN distribucion_necesidades DECIMAL(5,2) DEFAULT 50.00 AFTER monto_objetivo,
ADD COLUMN distribucion_deseos DECIMAL(5,2) DEFAULT 30.00 AFTER distribucion_necesidades,
ADD COLUMN distribucion_ahorros DECIMAL(5,2) DEFAULT 20.00 AFTER distribucion_deseos,
ADD COLUMN tipo_distribucion VARCHAR(20) DEFAULT 'BALANCED' AFTER distribucion_ahorros,
ADD COLUMN prioridad VARCHAR(10) DEFAULT 'MEDIA' AFTER tipo_distribucion;

-- Opcional: Actualizar registros existentes con valores calculados
UPDATE planes_ahorro 
SET sueldo_base = monto_periodico * 5,
    distribucion_necesidades = 50.00,
    distribucion_deseos = 30.00,
    distribucion_ahorros = 20.00,
    tipo_distribucion = 'BALANCED',
    prioridad = 'MEDIA'
WHERE sueldo_base IS NULL;

-- Verificar la migración
SELECT 
    nombre,
    sueldo_base,
    otros_ingresos,
    meta_principal,
    monto_objetivo,
    distribucion_necesidades,
    distribucion_deseos,
    distribucion_ahorros,
    tipo_distribucion,
    prioridad
FROM planes_ahorro 
LIMIT 5;
