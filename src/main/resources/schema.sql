CREATE TABLE category
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    symbol      VARCHAR(10),
    description TEXT
);

CREATE TABLE place (
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