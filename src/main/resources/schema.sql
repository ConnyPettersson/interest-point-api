USE interest_db;
CREATE TABLE IF NOT EXISTS category
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    symbol      VARCHAR(10),
    description TEXT
);

CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(100) NOT NULL UNIQUE,
                                     password VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS place (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id INT NOT NULL,
    user_id INT NOT NULL,
    is_private BOOLEAN DEFAULT FALSE,
    description TEXT,
    coordinates GEOMETRY NOT NULL,
    created_at TIMESTAMP DEFAULT  CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT  CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

SHOW DATABASES;
SHOW TABLES;


UPDATE users
SET password = '$2a$10$ByhcU7MNRigZMOYjSJGfnuKPk63oATRwmNxypCogEy02YfsW7EbGS'
WHERE username = 'user';

SELECT * FROM category WHERE id = 1;

SELECT * FROM users WHERE username = 'user';