-- =====================================================
-- SCRIPT RÁPIDO - SOLO ACTUALIZAR CAMPOS DEL WIZARD
-- Para usar cuando las tablas ya existen
-- =====================================================

-- Usar la base de datos
USE finanzas_db;

-- Silenciar advertencias
SET sql_notes = 0;

-- Agregar campos del wizard a planesfinanzas (ignora si ya existen)
ALTER TABLE planesfinanzas ADD COLUMN nombre VARCHAR(100);
ALTER TABLE planesfinanzas ADD COLUMN otros_ingresos DECIMAL(10,2) DEFAULT 0.00;
ALTER TABLE planesfinanzas ADD COLUMN meta_principal VARCHAR(255);
ALTER TABLE planesfinanzas ADD COLUMN monto_objetivo DECIMAL(12,2);
ALTER TABLE planesfinanzas ADD COLUMN distribucion_necesidades DECIMAL(5,2) DEFAULT 50.00;
ALTER TABLE planesfinanzas ADD COLUMN distribucion_deseos DECIMAL(5,2) DEFAULT 30.00;
ALTER TABLE planesfinanzas ADD COLUMN distribucion_ahorros DECIMAL(5,2) DEFAULT 20.00;
ALTER TABLE planesfinanzas ADD COLUMN tipo_distribucion VARCHAR(50) DEFAULT 'BALANCED';
ALTER TABLE planesfinanzas ADD COLUMN prioridad VARCHAR(50) DEFAULT 'MEDIA';

-- Actualizar registros existentes sin nombre
UPDATE planesfinanzas SET 
    nombre = CONCAT('Plan Financiero ', id),
    otros_ingresos = COALESCE(otros_ingresos, 0.00),
    meta_principal = COALESCE(meta_principal, 'Establecer hábitos de ahorro'),
    monto_objetivo = COALESCE(monto_objetivo, sueldo_base * 6),
    distribucion_necesidades = COALESCE(distribucion_necesidades, 50.00),
    distribucion_deseos = COALESCE(distribucion_deseos, 30.00),
    distribucion_ahorros = COALESCE(distribucion_ahorros, 20.00),
    tipo_distribucion = COALESCE(tipo_distribucion, 'BALANCED'),
    prioridad = COALESCE(prioridad, 'MEDIA')
WHERE nombre IS NULL OR nombre = '';

-- Restaurar advertencias
SET sql_notes = 1;

-- Verificar
SELECT COUNT(*) as total_planes FROM planesfinanzas;
SELECT COUNT(*) as planes_con_nombre FROM planesfinanzas WHERE nombre IS NOT NULL;

SELECT 'Actualización completada' as estado;
