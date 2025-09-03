-- Seleccionar la base de datos
USE linktic_inventory_db;

-- =========================================
-- TABLA USERS
-- =========================================
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    nickname VARCHAR(60) NOT NULL,
    passwd VARCHAR(60) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_products_nickname (nickname),
    INDEX idx_products_passwd (passwd)
) ENGINE=InnoDB;

-- =========================================
-- DATOS DE EJEMPLO
-- =========================================

-- Insertar usuarios de ejemplo con UUID específicos para referencia

SET @user1_uuid = UUID();
SET @user2_uuid = UUID();

INSERT INTO users (id, name, nickname, passwd) VALUES 
(@user1_uuid, "Root LinkTick", "Toor", "T00rL1nkT1ck"),
(@user2_uuid, "User LinkTick", "NormalUser", "U53rL1nkT1ck")
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname);

-- =========================================
-- TRIGGER PARA AUDITORÍA
-- =========================================

DELIMITER $$

CREATE TRIGGER IF NOT EXISTS users_audit_update
    BEFORE UPDATE ON users
    FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END$$

DELIMITER ;
