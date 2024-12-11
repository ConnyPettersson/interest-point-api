-- Lägg in kategorier
INSERT INTO category (name, symbol, description) VALUES
                                                     ('Parks', '🌳', 'Public parks and green spaces'),
                                                     ('Restaurants', '🍴', 'Places to eat'),
                                                     ('Landmarks', '📍', 'Tourist landmarks and famous spots');

-- Lägg in användare
INSERT INTO users (username, password)
VALUES ('user', '$2a$10$hashedpassword');

INSERT INTO users (username, password)
VALUES ('test_userX', 'test_password');

-- Lägg in platser knutna till befintliga användare
INSERT INTO place (name, category_id, user_id, is_private, description, coordinates)
VALUES
    ('Central Park', 1, 1, FALSE, 'A large public park in New York City', ST_GeomFromText('POINT(-73.968285 40.785091)', 4326)),
    ('New Place', 1, 1, FALSE, 'Description', ST_GeomFromText('POINT(10.0 30.0)', 4326)),
    ('Test Place', 1, 1, FALSE, 'Inside the polygon', ST_GeomFromText('POINT(-73.965 40.785)', 4326)),
    ('Test Place', 1, 1, FALSE, 'Inside polygon', ST_GeomFromText('POINT(-73.975 40.785)', 4326));

-- Lägg in plats för test_userX
INSERT INTO place (name, category_id, user_id, is_private, description, coordinates)
VALUES ('Test PlaceX', 1, (SELECT id FROM users WHERE username = 'test_userX'), FALSE, 'Test descriptionX', ST_GeomFromText('POINT(10.0 20.0)', 4326));

INSERT INTO place (name, category_id, user_id, is_private, description, coordinates)
VALUES ('Invalid Place', 1, 9999, FALSE, 'Invalid user', ST_GeomFromText('POINT(30.0 50.0)', 4326));
