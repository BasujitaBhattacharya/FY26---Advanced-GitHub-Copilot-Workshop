-- H2 Database Seed Data for Petstore API
-- Auto-executed on startup with spring.jpa.hibernate.ddl-auto=create-drop

-- Insert 10 sample pets with varied species, prices, and inventory
INSERT INTO pets (name, species, price, inventory_count, created_at, updated_at) VALUES
('Buddy', 'DOG', 299.99, 5, NOW(), NOW()),
('Whiskers', 'CAT', 199.99, 8, NOW(), NOW()),
('Tweety', 'BIRD', 79.99, 12, NOW(), NOW()),
('Fluffy', 'RABBIT', 89.99, 6, NOW(), NOW()),
('Nibbles', 'HAMSTER', 29.99, 20, NOW(), NOW()),
('Nemo', 'FISH', 39.99, 15, NOW(), NOW()),
('Rex', 'DOG', 349.99, 3, NOW(), NOW()),
('Mittens', 'CAT', 249.99, 4, NOW(), NOW()),
('Polly', 'BIRD', 149.99, 7, NOW(), NOW()),
('Hoppy', 'RABBIT', 129.99, 5, NOW(), NOW());

-- Insert sample cart items for testing
INSERT INTO cart_items (pet_id, quantity, created_at) VALUES
(1, 2, NOW()),
(3, 1, NOW());
