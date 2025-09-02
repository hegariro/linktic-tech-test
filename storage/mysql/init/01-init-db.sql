-- =========================================
-- Script de inicialización para linktic_inventory_db
-- Products e Inventory
-- =========================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS linktic_inventory_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE linktic_inventory_db;

-- =========================================
-- TABLA PRODUCTS
-- =========================================
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_products_name (name),
    INDEX idx_products_created_at (created_at)
) ENGINE=InnoDB;

-- =========================================
-- TABLA INVENTORY
-- =========================================
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity_available INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_inventory_product_id (product_id)
) ENGINE=InnoDB;

-- =========================================
-- DATOS DE EJEMPLO
-- =========================================

-- Insertar productos de ejemplo
INSERT INTO products (name, description, price) VALUES
('Laptop Dell Inspiron 15', 'High-performance laptop for business and personal use', 4899900.00),
('Cotton T-Shirt Blue', 'Comfortable cotton t-shirt in blue color', 79900.00),
('Java Programming Book', 'Complete guide to Java programming', 89000.00),
('Garden Hose 25ft', 'Durable garden hose for outdoor use', 119900.00)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insertar inventario de ejemplo
INSERT INTO inventory (product_id, quantity_available) VALUES
(1, 50),
(1, 30),
(2, 200),
(3, 75),
(4, 25)
ON DUPLICATE KEY UPDATE quantity_available = VALUES(quantity_available);

-- =========================================
-- TRIGGERS PARA AUDITORÍA
-- =========================================

DELIMITER $$

CREATE TRIGGER IF NOT EXISTS products_audit_update
    BEFORE UPDATE ON products
    FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END$$

CREATE TRIGGER IF NOT EXISTS inventory_audit_update
    BEFORE UPDATE ON inventory
    FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END$$

DELIMITER ;
