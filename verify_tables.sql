-- Verificar estructura de la tabla planesfinanzas
DESCRIBE planesfinanzas;

-- Verificar que la tabla está vacía (como debe ser)
SELECT COUNT(*) as total_planes FROM planesfinanzas;

-- Verificar usuarios existentes
SELECT id, nombre, email FROM usuario;

-- Verificar estructura de otras tablas relacionadas
DESCRIBE gastos;
DESCRIBE ingresos;
