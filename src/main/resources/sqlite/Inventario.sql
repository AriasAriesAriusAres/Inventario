-- Script SQL corregido para evitar errores de 'incomplete input' en SQLite
-- VERSIÓN AJUSTADA PARA COMPILAR CORRECTAMENTE

-- =============================================
-- Tabla de Inventarios
-- =============================================
DROP TABLE IF EXISTS inventarios;
CREATE TABLE inventarios (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             nombre_producto TEXT NOT NULL,
                             cantidad INTEGER NOT NULL
);

-- =============================================
-- Tabla de Productos
-- =============================================
DROP TABLE IF EXISTS productos;
CREATE TABLE productos (
                           id_producto INTEGER PRIMARY KEY AUTOINCREMENT,
                           nombre TEXT NOT NULL,
                           descripcion TEXT,
                           precio REAL NOT NULL DEFAULT 0.0,
                           stock INTEGER NOT NULL DEFAULT 0,
                           id_inventario INTEGER NOT NULL,
                           FOREIGN KEY (id_inventario) REFERENCES inventarios(id)
);

-- =============================================
-- Tabla de Usuarios
-- =============================================
DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios (
                          id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                          username TEXT NOT NULL UNIQUE,
                          password TEXT NOT NULL /* Pendiente Hashing */
);

-- =============================================
-- Tabla de Movimientos
-- =============================================
DROP TABLE IF EXISTS movimientos;
CREATE TABLE movimientos (
                             id_movimiento INTEGER PRIMARY KEY AUTOINCREMENT,
                             id_usuario INTEGER NOT NULL,
                             fecha_hora TEXT NOT NULL /* Formato ISO: YYYY-MM-DDTHH:MM:SS */,
                             tabla_afectada TEXT NOT NULL,
                             id_registro_afectado INTEGER NOT NULL,
                             accion TEXT NOT NULL,
                             estado TEXT NOT NULL /* PENDIENTE, APROBADO, RECHAZADO, EJECUTADO, INFO */,
                             detalles_cambio TEXT,
                             FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);



-- =============================================
-- DATOS DE EJEMPLO
-- =============================================

-- Inventario de ejemplo
INSERT INTO inventarios (nombre_producto, cantidad) VALUES
    ('Almacén Principal', 100);

-- Productos de ejemplo
INSERT INTO productos (nombre, descripcion, precio, stock, id_inventario) VALUES
                                                                              ('Manzana Fuji', 'Kilo de Manzana Fuji Roja', 1.99, 50, 1),
                                                                              ('Plátano de Canarias', 'Kilo de Plátanos', 1.50, 80, 1),
                                                                              ('Naranja de Zumo', 'Malla de 2Kg Naranjas para Zumo', 2.50, 60, 1),
                                                                              ('Pera Conferencia', 'Kilo de Pera Conferencia', 2.20, 40, 1);

-- Usuarios de ejemplo
INSERT INTO usuarios (username, password) VALUES
                                              ('admin', '1234'), /* Cambiar contraseña ASAP y usar Hash */
                                              ('frutero', 'fruta'); /* Cambiar contraseña ASAP y usar Hash */

-- Movimiento dummy
INSERT INTO movimientos (id_usuario, fecha_hora, tabla_afectada, id_registro_afectado, accion, estado, detalles_cambio)
VALUES (1, '2024-01-01T12:00:00', 'productos', 1, 'INSERT', 'APROBADO', 'Carga inicial');

