USE interest_db;

-- Skapa tabeller
CREATE TABLE IF NOT EXISTS category (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL UNIQUE,
                                        symbol VARCHAR(10),
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
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     FOREIGN KEY (category_id) REFERENCES category(id),
                                     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Se till att alla koordinater har SRID = 4326
UPDATE place
SET coordinates = ST_SRID(coordinates, 4326)
WHERE ST_SRID(coordinates) = 0;

-- Rensa platser vars användare inte finns
DELETE FROM place
WHERE user_id NOT IN (SELECT id FROM users);

-- Kontroll av constraint
SELECT * FROM information_schema.table_constraints
WHERE table_name = 'place'
  AND constraint_type = 'FOREIGN KEY';

-- Skapa spatialt index för snabbare geografiska frågor
CREATE SPATIAL INDEX idx_coordinates ON place (coordinates);

-- Kontrollera validitet och SRID
SELECT id, ST_IsValid(coordinates) AS is_valid FROM place;
SELECT id, ST_SRID(coordinates) AS srid FROM place;

-- Sätt rätt SRID om nödvändigt
UPDATE place
SET coordinates = ST_GeomFromText(ST_AsText(coordinates), 4326)
WHERE ST_SRID(coordinates) != 4326;

-- Exempel: Hämta platser inom en polygon
SELECT id, ST_AsText(coordinates) AS coordinates,
       ST_Within(coordinates, ST_GeomFromText('POLYGON((-73.99 40.78, -73.99 40.79, -73.96 40.79, -73.96 40.78, -73.99 40.78))', 4326)) AS within_area
FROM place;

-- Exempel: Hämta platser inom viss distans till en punkt (Obs! Enheter i grader, ej meter)
SELECT id, ST_AsText(coordinates) AS coordinates,
       ST_Distance(coordinates, ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)) AS distance
FROM place
WHERE ST_Distance(coordinates, ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)) <= 500;

-- Övriga kontroller och information
DESCRIBE place;
SHOW COLUMNS FROM place;
SHOW INDEX FROM place;
SELECT * FROM place WHERE coordinates IS NULL;

-- Kolla användare och kategori
SELECT * FROM category WHERE id = 1;
SELECT * FROM users WHERE username = 'user';

-- Exempeluppdatering av en plats
UPDATE place
SET name = 'Valid Name', category_id = 1, user_id = 1, is_private = 0,
    description = 'A proper description.',
    coordinates = ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)
WHERE id = 24;

-- Exempeluppdatering av en användare
UPDATE users
SET password = '$2a$10$ByhcU7MNRigZMOYjSJGfnuKPk63oATRwmNxypCogEy02YfsW7EbGS'
WHERE username = 'user';

-- Kontroll av punktkoordinater
SELECT id, ST_X(coordinates) AS lon, ST_Y(coordinates) AS lat FROM place;
