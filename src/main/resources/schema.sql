USE interest_db;
CREATE TABLE IF NOT EXISTS category
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    symbol      VARCHAR(10),
    description TEXT
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
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

SELECT *
FROM place
WHERE user_id NOT IN (SELECT id FROM users);

DELETE FROM place
WHERE user_id NOT IN (SELECT id FROM users);

ALTER TABLE place
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

SELECT *
FROM information_schema.table_constraints
WHERE table_name = 'place'
  AND constraint_type = 'FOREIGN KEY';

DELETE FROM users WHERE username = 'test_userX';
SELECT * FROM place WHERE name =  'Test PlaceX';

SELECT *
FROM place
WHERE user_id NOT IN (SELECT id FROM users);


UPDATE users
SET password = '$2a$10$ByhcU7MNRigZMOYjSJGfnuKPk63oATRwmNxypCogEy02YfsW7EbGS'
WHERE username = 'user';

SELECT * FROM category WHERE id = 1;

SELECT * FROM users WHERE username = 'user';

DESCRIBE place;

CREATE SPATIAL INDEX idx_coordinates ON place (coordinates);

SELECT id, ST_IsValid(coordinates) FROM place;

SELECT id, ST_AsText(coordinates) FROM place;

SELECT id, ST_SRID(coordinates) FROM place;

UPDATE place
SET coordinates = ST_GeomFromText(ST_AsText(coordinates), 4326)
WHERE ST_SRID(coordinates) = 0;

SELECT id, ST_SRID(coordinates) FROM place;

SELECT id, name, ST_AsText(coordinates), ST_SRID(coordinates)
FROM place;

SELECT *
FROM place
WHERE ST_Distance(coordinates, ST_GeomFromText('POINT(10.0 30.0)', 4326)) <= 5000;

UPDATE place
SET coordinates = ST_GeomFromText(ST_AsText(coordinates), 4326)
WHERE ST_SRID(coordinates) = 0;

SELECT id, ST_IsValid(coordinates) AS is_valid
FROM place;

SELECT *
FROM place
WHERE ST_Distance(coordinates, ST_GeomFromText('POINT(10.0 30.0)', 4326)) <= 5000;

SHOW VARIABLES LIKE 'have_geometry';

SHOW COLUMNS FROM place;

SELECT id, ST_SRID(coordinates), ST_AsText(coordinates) FROM place;


