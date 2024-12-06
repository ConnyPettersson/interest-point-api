INSERT INTO category (name, symbol, description) VALUES
                                                     ('Parks', '🌳', 'Public parks and green spaces'),
                                                     ('Restaurants', '🍴', 'Places to eat'),
                                                     ('Landmarks', '📍', 'Tourist landmarks and famous spots');

INSERT INTO place (name, category_id, user_id, is_private, description, coordinates)
VALUES
    ('Central Park', 1, 1, FALSE, 'A large public park in New York City', ST_GeomFromText('POINT(-73.968285 40.785091)', 4326));

INSERT INTO users (username, password)
VALUES ('user', '$2a$10$hashedpassword')