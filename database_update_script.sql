-- =====================================================
-- SCRIPT DE ACTUALIZACIÓN DE BASE DE DATOS
-- Sistema de Finanzas Personales
-- Fecha: 2025-01-16
-- =====================================================

-- Usar la base de datos
USE finanzas_db;

-- Configurar para ignorar advertencias
SET sql_notes = 0;

-- =====================================================
-- 1. VERIFICAR Y CREAR TABLAS PRINCIPALES
-- =====================================================

-- Crear tabla usuarios si no existe
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    edad INT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla planesfinanzas si no existe (versión actualizada)
CREATE TABLE IF NOT EXISTS planesfinanzas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    sueldo_base DECIMAL(10,2) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    estado VARCHAR(50) NOT NULL DEFAULT 'activo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Crear tabla ingresos si no existe
CREATE TABLE IF NOT EXISTS ingresos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_plan_finanzas INT,
    monto DECIMAL(10,2) NOT NULL,
    descripcion VARCHAR(255),
    fecha DATE NOT NULL,
    categoria VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (id_plan_finanzas) REFERENCES planesfinanzas(id) ON DELETE SET NULL
);

-- Crear tabla gastos si no existe
CREATE TABLE IF NOT EXISTS gastos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_plan_finanzas INT,
    monto DECIMAL(10,2) NOT NULL,
    descripcion VARCHAR(255),
    fecha DATE NOT NULL,
    categoria VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (id_plan_finanzas) REFERENCES planesfinanzas(id) ON DELETE SET NULL
);

-- Crear tabla ahorros si no existe
CREATE TABLE IF NOT EXISTS ahorros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    descripcion VARCHAR(255),
    fecha DATE NOT NULL,
    meta VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Crear tabla planes_ahorro si no existe (tabla legacy)
CREATE TABLE IF NOT EXISTS planes_ahorro (
    id_plan_ahorro INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    monto_periodico DECIMAL(10,2) NOT NULL,
    frecuencia VARCHAR(50) NOT NULL,
    dia_ejecucion INT,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    estado VARCHAR(50) DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- =====================================================
-- 2. ACTUALIZAR TABLA PLANESFINANZAS CON NUEVOS CAMPOS
-- =====================================================

-- Nota: Si una columna ya existe, MySQL mostrará una advertencia pero continuará
-- Agregar campos del wizard avanzado a planesfinanzas (ignorar si ya existen)
ALTER TABLE planesfinanzas ADD COLUMN IF NOT EXISTS nombre VARCHAR(100) COMMENT 'Nombre del plan financiero';
ALTER TABLE planesfinanzas ADD COLUMN IF NOT EXISTS otros_ingresos DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Otros ingresos mensuales';
ALTER TABLE planesfinanzas ADD COLUMN IF NOT EXISTS meta_principal VARCHAR(255) COMMENT 'Meta principal del plan';
ALTER TABLE planesfinanzas ADD COLUMN IF NOT EXISTS monto_objetivo DECIMAL(12,2) COMMENT 'Monto objetivo a alcanzar';
ALTER TABLE planesfinanzas ADD COLUMN IF NOT EXISTS distribucion_necesidades DECIMAL(5,2) DEFAULT 50.00 COMMENT 'Porcentaje para necesidades';
ALTER TABLE planesfinanzas ADD COLUMN IF NOT EXISTS distribucion_deseos DECIMAL(5,2) DEFAULT 30.00 COMMENT 'Porcentaje para deseos';
ALTER TABLE planesfinanzas ADD COLUMN IF NOT EXISTS distribucion_ahorros DECIMAL(5,2) DEFAULT 20.00 COMMENT 'Porcentaje para ahorros';
ALTER TABLE planesfinanzas ADD COLUMN IF NOT EXISTS tipo_distribucion VARCHAR(50) DEFAULT 'BALANCED' COMMENT 'Tipo de distribución';
ALTER TABLE planesfinanzas ADD COLUMN IF NOT EXISTS prioridad VARCHAR(50) DEFAULT 'MEDIA' COMMENT 'Prioridad del plan';

-- =====================================================
-- 3. ACTUALIZAR TABLA PLANES_AHORRO CON NUEVOS CAMPOS
-- =====================================================

-- Agregar campos del wizard avanzado a planes_ahorro (para compatibilidad)
ALTER TABLE planes_ahorro ADD COLUMN IF NOT EXISTS sueldo_base DECIMAL(10,2) COMMENT 'Sueldo base mensual';
ALTER TABLE planes_ahorro ADD COLUMN IF NOT EXISTS otros_ingresos DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Otros ingresos mensuales';
ALTER TABLE planes_ahorro ADD COLUMN IF NOT EXISTS meta_principal VARCHAR(255) COMMENT 'Meta principal del plan';
ALTER TABLE planes_ahorro ADD COLUMN IF NOT EXISTS monto_objetivo DECIMAL(12,2) COMMENT 'Monto objetivo a alcanzar';
ALTER TABLE planes_ahorro ADD COLUMN IF NOT EXISTS distribucion_necesidades DECIMAL(5,2) DEFAULT 50.00 COMMENT 'Porcentaje para necesidades';
ALTER TABLE planes_ahorro ADD COLUMN IF NOT EXISTS distribucion_deseos DECIMAL(5,2) DEFAULT 30.00 COMMENT 'Porcentaje para deseos';
ALTER TABLE planes_ahorro ADD COLUMN IF NOT EXISTS distribucion_ahorros DECIMAL(5,2) DEFAULT 20.00 COMMENT 'Porcentaje para ahorros';
ALTER TABLE planes_ahorro ADD COLUMN IF NOT EXISTS tipo_distribucion VARCHAR(50) DEFAULT 'BALANCED' COMMENT 'Tipo de distribución';
ALTER TABLE planes_ahorro ADD COLUMN IF NOT EXISTS prioridad VARCHAR(50) DEFAULT 'MEDIA' COMMENT 'Prioridad del plan';

-- =====================================================
-- 4. ACTUALIZAR DATOS EXISTENTES
-- =====================================================

-- Actualizar registros existentes en planesfinanzas con valores por defecto
UPDATE planesfinanzas SET 
    nombre = CONCAT('Plan Financiero ', id),
    otros_ingresos = 0.00,
    meta_principal = 'Establecer hábitos de ahorro',
    monto_objetivo = COALESCE(sueldo_base * 6, 18000.00),
    distribucion_necesidades = 50.00,
    distribucion_deseos = 30.00,
    distribucion_ahorros = 20.00,
    tipo_distribucion = 'BALANCED',
    prioridad = 'MEDIA'
WHERE nombre IS NULL;

-- Actualizar registros existentes en planes_ahorro con valores por defecto
UPDATE planes_ahorro SET 
    sueldo_base = COALESCE(monto_periodico * 5, 3000.00),
    otros_ingresos = 0.00,
    meta_principal = 'Meta de ahorro',
    monto_objetivo = COALESCE(monto_periodico * 12, 36000.00),
    distribucion_necesidades = 50.00,
    distribucion_deseos = 30.00,
    distribucion_ahorros = 20.00,
    tipo_distribucion = 'BALANCED',
    prioridad = 'MEDIA'
WHERE sueldo_base IS NULL;

-- =====================================================
-- 5. CREAR ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================

-- Índices para mejorar rendimiento
CREATE INDEX IF NOT EXISTS idx_planesfinanzas_usuario ON planesfinanzas(id_usuario);
CREATE INDEX IF NOT EXISTS idx_planesfinanzas_estado ON planesfinanzas(estado);
CREATE INDEX IF NOT EXISTS idx_ingresos_usuario ON ingresos(id_usuario);
CREATE INDEX IF NOT EXISTS idx_ingresos_plan ON ingresos(id_plan_finanzas);
CREATE INDEX IF NOT EXISTS idx_gastos_usuario ON gastos(id_usuario);
CREATE INDEX IF NOT EXISTS idx_gastos_plan ON gastos(id_plan_finanzas);
CREATE INDEX IF NOT EXISTS idx_ahorros_usuario ON ahorros(id_usuario);
CREATE INDEX IF NOT EXISTS idx_planes_ahorro_usuario ON planes_ahorro(id_usuario);

-- =====================================================
-- 6. VERIFICACIONES FINALES
-- =====================================================

-- Restaurar sql_notes
SET sql_notes = 1;

-- Mostrar estructura actualizada de planesfinanzas
DESCRIBE planesfinanzas;

-- Mostrar estructura actualizada de planes_ahorro
DESCRIBE planes_ahorro;

-- Contar registros en cada tabla
SELECT 'usuarios' as tabla, COUNT(*) as registros FROM usuarios
UNION ALL
SELECT 'planesfinanzas' as tabla, COUNT(*) as registros FROM planesfinanzas
UNION ALL
SELECT 'planes_ahorro' as tabla, COUNT(*) as registros FROM planes_ahorro
UNION ALL
SELECT 'ingresos' as tabla, COUNT(*) as registros FROM ingresos
UNION ALL
SELECT 'gastos' as tabla, COUNT(*) as registros FROM gastos
UNION ALL
SELECT 'ahorros' as tabla, COUNT(*) as registros FROM ahorros;

-- Verificar algunos registros de ejemplo
SELECT 
    id, 
    nombre, 
    sueldo_base, 
    otros_ingresos, 
    meta_principal,
    distribucion_necesidades,
    distribucion_deseos,
    distribucion_ahorros,
    tipo_distribucion,
    prioridad,
    estado
FROM planesfinanzas 
LIMIT 5;

SELECT 'Base de datos actualizada correctamente.' as mensaje;
SELECT 'Tablas creadas/actualizadas: usuarios, planesfinanzas, planes_ahorro, ingresos, gastos, ahorros' as detalle;
SELECT 'Campos del wizard agregados a planesfinanzas y planes_ahorro' as wizard_campos;
SELECT 'Índices creados para optimización' as indices;
