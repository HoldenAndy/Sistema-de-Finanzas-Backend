-- Script de migración para agregar campos del wizard avanzado a la tabla planesfinanzas
-- Ejecutar en MySQL antes de usar las nuevas funcionalidades

-- Verificar estructura actual de la tabla
DESCRIBE planesfinanzas;

-- Agregar las nuevas columnas (usar SET sql_notes = 0 para ignorar advertencias)
SET sql_notes = 0;

-- Agregar columna nombre
ALTER TABLE planesfinanzas ADD COLUMN nombre VARCHAR(100) COMMENT 'Nombre del plan financiero';

-- Agregar columna otros_ingresos
ALTER TABLE planesfinanzas ADD COLUMN otros_ingresos DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Otros ingresos mensuales';

-- Agregar columna meta_principal
ALTER TABLE planesfinanzas ADD COLUMN meta_principal VARCHAR(255) COMMENT 'Meta principal del plan';

-- Agregar columna monto_objetivo
ALTER TABLE planesfinanzas ADD COLUMN monto_objetivo DECIMAL(12,2) COMMENT 'Monto objetivo a alcanzar';

-- Agregar columna distribucion_necesidades
ALTER TABLE planesfinanzas ADD COLUMN distribucion_necesidades DECIMAL(5,2) DEFAULT 50.00 COMMENT 'Porcentaje para necesidades';

-- Agregar columna distribucion_deseos
ALTER TABLE planesfinanzas ADD COLUMN distribucion_deseos DECIMAL(5,2) DEFAULT 30.00 COMMENT 'Porcentaje para deseos';

-- Agregar columna distribucion_ahorros
ALTER TABLE planesfinanzas ADD COLUMN distribucion_ahorros DECIMAL(5,2) DEFAULT 20.00 COMMENT 'Porcentaje para ahorros';

-- Agregar columna tipo_distribucion
ALTER TABLE planesfinanzas ADD COLUMN tipo_distribucion VARCHAR(50) DEFAULT 'BALANCED' COMMENT 'Tipo de distribución';

-- Agregar columna prioridad
ALTER TABLE planesfinanzas ADD COLUMN prioridad VARCHAR(50) DEFAULT 'MEDIA' COMMENT 'Prioridad del plan';

-- Restaurar sql_notes
SET sql_notes = 1;

-- Actualizar registros existentes con valores por defecto
UPDATE planesfinanzas SET 
    nombre = CONCAT('Plan Financiero ', id),
    otros_ingresos = 0.00,
    meta_principal = 'Establecer hábitos de ahorro',
    monto_objetivo = sueldo_base * 6, -- Meta de 6 meses de sueldo
    distribucion_necesidades = 50.00,
    distribucion_deseos = 30.00,
    distribucion_ahorros = 20.00,
    tipo_distribucion = 'BALANCED',
    prioridad = 'MEDIA'
WHERE nombre IS NULL;

-- Verificar la estructura actualizada
DESCRIBE planesfinanzas;

-- Mostrar algunos registros de ejemplo
SELECT id, nombre, sueldo_base, otros_ingresos, meta_principal, 
       distribucion_necesidades, distribucion_deseos, distribucion_ahorros,
       tipo_distribucion, prioridad
FROM planesfinanzas 
LIMIT 5;

-- Verificar que la tabla tiene el campo correcto para el ID
SHOW COLUMNS FROM planesfinanzas LIKE 'id%';
