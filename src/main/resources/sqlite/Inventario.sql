-- Script SQL para crear las tablas esperadas por la aplicación web
-- Adaptado para SQLite y con datos de ejemplo de Frutería

-- Tabla de Inventarios
DROP TABLE IF EXISTS inventarios;
CREATE TABLE inventarios (
                             id_inventario INTEGER PRIMARY KEY AUTOINCREMENT,
                             nombre TEXT NOT NULL,
                             descripcion TEXT NULL
);

-- Tabla de Productos
DROP TABLE IF EXISTS productos;
CREATE TABLE productos (
                           id_producto INTEGER PRIMARY KEY AUTOINCREMENT,
                           nombre TEXT NOT NULL,
                           descripcion TEXT NULL,
                           precio REAL NOT NULL DEFAULT 0.0,             -- REAL para números decimales
                           stock INTEGER NOT NULL DEFAULT 0,              -- INTEGER para enteros
                           id_inventario INTEGER NOT NULL,
                           FOREIGN KEY (id_inventario) REFERENCES inventarios(id_inventario)
);

-- --- DATOS DE EJEMPLO: FRUTERÍA ---

-- Primero un inventario (Almacén Principal)
-- El ID generado será 1 si es el primero
INSERT INTO inventarios (nombre, descripcion) VALUES ('Almacén Principal', 'Stock general de la frutería');

-- Luego productos asociados a ese inventario (ID=1)
INSERT INTO productos (nombre, descripcion, precio, stock, id_inventario) VALUES
                                                                              ('Manzana Fuji', 'Kilo de Manzana Fuji Roja', 1.99, 50, 1),
                                                                              ('Plátano de Canarias', 'Kilo de Plátanos', 1.50, 80, 1),
                                                                              ('Naranja de Zumo', 'Malla de 2Kg Naranjas para Zumo', 2.50, 60, 1),
                                                                              ('Pera Conferencia', 'Kilo de Pera Conferencia', 2.20, 40, 1),
                                                                              ('Uva Blanca sin Pepita', 'Bandeja de 500g Uva Blanca', 3.10, 30, 1),
                                                                              ('Fresa', 'Tarrina de Fresas 250g', 2.80, 25, 1),
                                                                              ('Kiwi Zespri Gold', 'Kilo de Kiwi Zespri Gold', 4.50, 35, 1),
                                                                              ('Patata Nueva', 'Saco de 5Kg Patata Nueva Lavada', 3.00, 100, 1),
                                                                              ('Cebolla', 'Malla de 1Kg Cebolla Seca', 1.20, 70, 1),
                                                                              ('Tomate Rama', 'Kilo de Tomate en Rama Maduro', 2.10, 55, 1);