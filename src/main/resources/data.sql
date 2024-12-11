INSERT INTO category (name, symbol, description) VALUES
                                                     ('Parks', '🌳', 'Public parks and green spaces'),
                                                     ('Restaurants', '🍴', 'Places to eat'),
                                                     ('Landmarks', '📍', 'Tourist landmarks and famous spots');

INSERT INTO place (name, category_id, user_id, is_private, description, coordinates)
VALUES
    ('Central Park', 1, 1, FALSE, 'A large public park in New York City', ST_GeomFromText('POINT(-73.968285 40.785091)', 4326));

INSERT INTO users (username, password)
VALUES ('user', '$2a$10$hashedpassword')



-- Lägg till publika platser (is_private = false)
INSERT INTO place (name, category_id, user_id, is_private, description, coordinates)
VALUES
    ('Central Park', 1, 1, FALSE, 'A large public park in New York City', ST_GeomFromText('POINT(-73.968285 40.785091)', 4326)),
    ('Eiffel Tower', 3, 1, FALSE, 'Famous landmark in Paris', ST_GeomFromText('POINT(2.2945 48.8584)', 4326));

-- Lägg även till en privat plats (för att se skillnad)
INSERT INTO place (name, category_id, user_id, is_private, description, coordinates)
VALUES
    ('Local Cafe', 2, 2, TRUE, 'A small private cafe', ST_GeomFromText('POINT(-0.1276 51.5074)', 4326));