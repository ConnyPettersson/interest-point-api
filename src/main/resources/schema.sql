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
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

SHOW DATABASES;
SHOW TABLES;


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
DESCRIBE place;
SELECT * FROM place WHERE id = 24;

UPDATE place
SET name = 'Valid Name', category_id = 1, user_id = 1, is_private = 0, description = 'A proper description.',
    coordinates = ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)
WHERE id = 24;

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





SELECT *
FROM place
WHERE ST_Distance(coordinates, ST_GeomFromText('POINT(10.0 30.0)', 4326)) <= 5000;

SHOW VARIABLES LIKE 'have_geometry';

SHOW COLUMNS FROM place;

SELECT id, ST_SRID(coordinates), ST_AsText(coordinates) FROM place;


SELECT * FROM users WHERE username = 'user';
ALTER TABLE place
    MODIFY COLUMN coordinates GEOMETRY(Point, 4326);

SELECT id, ST_SRID(coordinates) AS srid FROM place;
UPDATE place
SET coordinates = ST_GeomFromText(ST_AsText(coordinates), 4326)
WHERE ST_SRID(coordinates) = 0;

SELECT id, ST_IsValid(coordinates) AS is_valid
FROM place;


SELECT id, ST_SRID(coordinates) AS srid FROM place;

SELECT id, ST_AsText(coordinates) AS coordinates, ST_SRID(coordinates) AS srid
FROM place;

UPDATE place
SET coordinates = ST_SetSRID(coordinates, 4326)
WHERE ST_SRID(coordinates) != 4326;

SELECT id, ST_SRID(coordinates), ST_AsText(coordinates) FROM place;

UPDATE place
SET coordinates = ST_GeometryFromText(ST_AsText(coordinates), 4326)
WHERE ST_SRID(coordinates) != 4326;

SELECT id, ST_Distance(coordinates, ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)) AS distance
FROM place
WHERE ST_SRID(coordinates) = 4326;


SELECT id
FROM place
WHERE ST_Within(
              coordinates,
              ST_GeomFromText('POLYGON((-73.99 40.75, -73.99 40.76, -73.98 40.76, -73.98 40.75, -73.99 40.75))', 4326)
      );



SELECT id, ST_AsText(coordinates) AS coordinates, ST_Within(coordinates, ST_GeomFromText('POLYGON((-73.99 40.75, -73.99 40.76, -73.98 40.76, -73.98 40.75, -73.99 40.75))', 4326)) AS within_area
FROM place;

SELECT id, ST_AsText(coordinates) AS coordinates, ST_Distance(ST_Transform(coordinates, 4326), ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)) AS distance
FROM place
WHERE ST_Distance(ST_Transform(coordinates, 4326), ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)) <= 500;

UPDATE place
SET coordinates = ST_SetSRID(coordinates, 4326)
WHERE ST_SRID(coordinates) != 4326;

SELECT id, ST_AsText(coordinates) AS coordinates, ST_Distance(coordinates, ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)) AS distance
FROM place
WHERE ST_Distance(coordinates, ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)) <= 500;

UPDATE place
SET coordinates = ST_GeomFromText(ST_AsText(coordinates), 4326)
WHERE ST_SRID(coordinates) != 4326;

SELECT ST_SRID(coordinates) FROM place;
DESCRIBE place;
ALTER TABLE place MODIFY coordinates GEOMETRY SRID 4326;
SHOW INDEX FROM place;
ALTER TABLE place DROP INDEX idx_coordinates;
ALTER TABLE place MODIFY coordinates GEOMETRY SRID 4326;
ALTER TABLE place ADD SPATIAL INDEX (coordinates);
SELECT * FROM place WHERE coordinates IS NULL;

ALTER TABLE place MODIFY coordinates GEOMETRY NOT NULL SRID 4326;
ALTER TABLE place ADD SPATIAL INDEX (coordinates);

SELECT id, ST_AsText(coordinates) AS coordinates,
       ST_Within(coordinates, ST_GeomFromText('POLYGON((-73.99 40.75, -73.99 40.76, -73.98 40.76, -73.98 40.75, -73.99 40.75))', 4326)) AS within_area
FROM place;
SELECT id, ST_AsText(coordinates) AS coordinates,
       ST_Distance(coordinates, ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)) AS distance
FROM place
WHERE ST_Distance(coordinates, ST_GeomFromText('POINT(-73.983233 40.755966)', 4326)) <= 500;
