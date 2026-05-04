-- 1. Додаємо кореневі категорії
INSERT INTO categories (id, name, description, parent_id)
VALUES (1, 'Побутова техніка', 'Електронна побутова техніка', NULL),
       (2, 'Комп''ютери та ноутбуки', 'Персональні комп''ютери, ноутбуки та аксесуари', NULL),
       (3, 'Смартфони та планшети', 'Мобільні пристрої та аксесуари', NULL);

-- 2. Додаємо підкатегорії (рівень 1)
INSERT INTO categories (id, name, description, parent_id)
VALUES (4, 'Велика побутова техніка', 'Холодильники, пральні машини, плити', 1),
       (5, 'Дрібна побутова техніка', 'Кавоварки, блендери, праски', 1),
       (6, 'Ноутбуки', 'Портативні комп''ютери', 2),
       (7, 'Настільні ПК', 'Системні блоки та моноблоки', 2),
       (8, 'Android-смартфони', 'Смартфони на базі Android', 3),
       (9, 'Планшети', 'Android та iOS планшети', 3);

-- 3. Додаємо підкатегорії (рівень 2)
INSERT INTO categories (id, name, description, parent_id)
VALUES (10, 'Холодильники', 'Дво- та однокамерні холодильники', 4),
       (11, 'Пральні машини', 'Фронтальне та вертикальне завантаження', 4);

-- 4. Додаємо товари
INSERT INTO products (id, name, description, price, image_url, category_id, available)
VALUES
-- Холодильники (category_id = 10)
(1, 'BOSCH KGN39VI306', 'Двокамерний холодильник з NoFrost, 366 л, нержавіюча сталь', 23999.0,
 'https://content1.rozetka.com.ua/goods/images/big/18122527.jpg', 10, TRUE),
(2, 'Samsung RB5000A', 'Двокамерний холодильник, 367 л, срібний', 18499.0,
 'https://images.samsung.com/is/image/samsung/kz-ru-rb37a5200sawt-rb37a5200sa-wt-rperspactivesilver-308486889?$Q90_1248_936_F_PNG$',
 10, TRUE),

-- Пральні машини (category_id = 11)
(3, 'LG F2V5HS0W', 'Пральна машина з паровою функцією, 9 кг, біла', 15999.0,
 'https://content.rozetka.com.ua/goods/images/big/248080752.jpg', 11, TRUE),

-- Ноутбуки (category_id = 6)
(4, 'Apple MacBook Air M2 13"', 'Ноутбук з чипом Apple M2, 8 ГБ RAM, 256 ГБ SSD, 13.6"', 54999.0,
 'https://content2.rozetka.com.ua/goods/images/big/269256826.jpg', 6, TRUE),
(5, 'ASUS VivoBook 15 X1500', 'Ноутбук Intel Core i5-1235U, 16 ГБ RAM, 512 ГБ SSD, 15.6" FHD', 22499.0, '', 6, TRUE),
(6, 'Lenovo IdeaPad 5 Pro', 'Ноутбук AMD Ryzen 7 5800H, 16 ГБ RAM, 512 ГБ SSD, 16" 2.5K', 31999.0, '', 6, FALSE),

-- Настільні ПК (category_id = 7)
(7, 'Apple iMac 24" M3', 'Моноблок з чипом Apple M3, 8 ГБ RAM, 256 ГБ SSD, 24" Retina 4.5K', 79999.0,
 'https://content2.rozetka.com.ua/goods/images/big/485318740.jpg', 7, TRUE),

-- Смартфони (category_id = 8)
(8, 'Samsung Galaxy S24 Ultra', '6.8" QHD+, Snapdragon 8 Gen 3, 12 ГБ RAM, 256 ГБ, 200 МП', 55999.0,
 'https://content1.rozetka.com.ua/goods/images/big/429440267.jpg', 8, TRUE),
(9, 'Google Pixel 8 Pro', '6.7" LTPO OLED, Google Tensor G3, 12 ГБ RAM, 128 ГБ', 42999.0,
 'https://content.rozetka.com.ua/goods/images/big/381556378.png', 8, TRUE),

-- Планшети (category_id = 9)
(10, 'Apple iPad Air 11" M2', '11" Liquid Retina, Apple M2, 8 ГБ RAM, 128 ГБ, Wi-Fi', 29999.0,
 'https://content1.rozetka.com.ua/goods/images/big/433639273.jpg', 9, TRUE),

-- Дрібна техніка (category_id = 5)
(11, 'DeLonghi Dinamica ECAM350.55.B', 'Автоматична кавоварка, 15 бар, 1,8 л, чорна', 21999.0,
 'https://content2.rozetka.com.ua/goods/images/big/163132518.jpg', 5, TRUE);

SELECT setval(pg_get_serial_sequence('categories', 'id'), MAX(id)) FROM categories;
SELECT setval(pg_get_serial_sequence('products', 'id'), MAX(id)) FROM products;