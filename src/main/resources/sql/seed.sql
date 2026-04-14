-- Zlagoda Supermarket Test Data
-- Dates: 2025-2026
-- Password is bcrypt hash of "12345" for all employees (for testing purposes only)
-- ============================================================
-- CLEANUP (safe re-run)
-- ============================================================
DELETE FROM Sale;
DELETE FROM Check_Table;
DELETE FROM Customer_Card;
DELETE FROM Store_Product;
DELETE FROM Product;
DELETE FROM Category;
DELETE FROM Employee;

-- ============================================================
-- EMPLOYEES
-- 3 managers, 8 cashiers
-- Edge cases:
--   EMP0000003 — no patronymic (NULL)
--   EMP0000008 — minimum salary 0.00 (trainee)
--   EMP0000010 — exactly 18 years old (born 2007-04-14)
--   EMP0000011 — NOT linked to any check (can be deleted safely)
-- ============================================================
INSERT INTO Employee VALUES
                         ('EMP0000001', '$2a$10$VavcbDs5wojYs8TPs32dRe.dE60UR8.odOQv4ZNSwGKVBhhWUCIwC',
                          'Kovalenko', 'Ivan', 'Petrovych', 'MANAGER', 45000.0000, '1985-03-12', '2019-06-01',
                          '+380501234567', 'Kyiv', 'Khreshchatyk St', '01001'),

                         ('EMP0000002', '$2a$10$UXxaBrMUMCh3OWp0KB7Yvu7xKxGrtzeSVoTZ4QO4BP6snGXyRya6u',
                          'Melnyk', 'Olena', 'Vasylivna', 'MANAGER', 47500.0000, '1990-07-22', '2020-03-15',
                          '+380672345678', 'Lviv', 'Franka St', '79000'),

                         ('EMP0000003', '$2a$10$3gXhiStA8keP6rVpZQCI1.H5wQFH4Y0hJgiM39KuuEa2nVqlNFvoa',
                          'Bondarenko', 'Serhiy', NULL, 'MANAGER', 50000.9999, '1982-11-05', '2015-01-10',
                          '+380933456789', 'Dnipro', 'Gagarin Ave', '49000'),
                         -- ^ Edge case: NULL patronymic

                         ('EMP0000004', '$2a$10$Dy0V/tpsgMTTfY/qx3JIVOlwHn.mOa0eys/8NRjkTDn8LSW/jO2BS',
                          'Tkachenko', 'Maria', 'Ivanivna', 'CASHIER', 18000.0000, '1998-04-30', '2022-09-01',
                          '+380994567890', 'Kyiv', 'Saksahanskoho St', '01033'),

                         ('EMP0000005', '$2a$10$a3IRg52fwkkewN7ZBQEWVeZ.H4yInDNtnZBYTpi/13Nmgl1346Oo6',
                          'Shevchenko', 'Oleksiy', 'Mykolayovych', 'CASHIER', 17500.0000, '1999-08-14', '2023-02-20',
                          '+380635678901', 'Kharkiv', 'Sumska St', '61000'),

                         ('EMP0000006', '$2a$10$nBvEzv.gJgdiAxUGXp5X.u/unkbYv62ZyGBCiIQJG7ZEq1sT8I836',
                          'Lysenko', 'Natalia', 'Olehivna', 'CASHIER', 19000.0000, '2000-12-01', '2023-07-10',
                          '+380666789012', 'Zaporizhzhia', 'Sobornyi Ave', '69000'),

                         ('EMP0000007', '$2a$10$R9B2FVqrE6FzYIbvtw2XPuWiyt1kyL852JeAVOosOA4Pg7fAb5N.q',
                          'Hrytsenko', 'Dmytro', 'Andriyovych', 'CASHIER', 18500.0000, '1997-06-17', '2022-04-05',
                          '+380677890123', 'Odesa', 'Derybasivska St', '65000'),

                         ('EMP0000008', '$2a$10$gAHwoBP8hX1GvJizhFwuweZbYmkp8tWSUUpG2tJ5ovNz.n5fZxO6q',
                          'Romanenko', 'Yulia', 'Serhiivna', 'CASHIER', 0.0000, '2001-02-28', '2025-01-15',
                          '+380508901234', 'Kyiv', 'Horkoho St', '03150'),
                         -- ^ Edge case: salary = 0.00 (trainee)

                         ('EMP0000009', '$2a$10$VavcbDs5wojYs8TPs32dRe.dE60UR8.odOQv4ZNSwGKVBhhWUCIwC',
                          'Pavlenko', 'Andriy', 'Borysovych', 'CASHIER', 20000.0000, '1995-09-09', '2021-11-01',
                          '+380509012345', 'Lviv', 'Horodotska St', '79000'),

                         ('EMP0000010', '$2a$10$UXxaBrMUMCh3OWp0KB7Yvu7xKxGrtzeSVoTZ4QO4BP6snGXyRya6u',
                          'Semenchenko', 'Tetiana', 'Yuriivna', 'CASHIER', 16000.0000, '2007-04-14', '2025-04-14',
                          '+380670123456', 'Kyiv', 'Lesi Ukrainky Blvd', '01133'),
                         -- ^ Edge case: exactly 18 years old (born 2007-04-14)

                         ('EMP0000011', '$2a$10$3gXhiStA8keP6rVpZQCI1.H5wQFH4Y0hJgiM39KuuEa2nVqlNFvoa',
                          'Zinchenko', 'Vasyl', 'Pavlovych', 'CASHIER', 17000.0000, '1993-05-20', '2024-03-01',
                          '+380631234567', 'Sumy', 'Pryvokzalna St', '40000');
-- ^ Edge case: NOT linked to any check — can be safely deleted

-- ============================================================
-- CATEGORIES
-- Category 10 has no products — can be safely deleted
-- ============================================================
INSERT INTO Category VALUES
                         (1,  'Dairy Products'),
                         (2,  'Meat & Poultry'),
                         (3,  'Bakery'),
                         (4,  'Beverages'),
                         (5,  'Fruits & Vegetables'),
                         (6,  'Frozen Foods'),
                         (7,  'Snacks & Sweets'),
                         (8,  'Household Chemicals'),
                         (9,  'Canned Goods'),
                         (10, 'Empty Category - safe to delete');
-- ^ Edge case: no products linked — can be deleted safely

-- ============================================================
-- PRODUCTS
-- Products 27 and 28 are NOT linked to any Store_Product — can be deleted
-- ============================================================
INSERT INTO Product VALUES
                        (1,  1, 'Milk 2.5%',              'Molokyia',       'UHT milk, 1 liter pack'),
                        (2,  1, 'Butter Unsalted',        'Yagotynske',     'Unsalted butter, 200g pack'),
                        (3,  1, 'Natural Yogurt',         'Activia',        'Plain yogurt, 250g cup'),
                        (4,  1, 'Cottage Cheese 9%',      'President',      'Grain cottage cheese, 400g pack'),
                        (5,  2, 'Chicken Breast',         'Nasha Ryaba',    'Fresh chicken breast fillet, per kg'),
                        (6,  2, 'Pork Chops',             'Globino',        'Fresh pork chops, per kg'),
                        (7,  2, 'Beef Ribeye Steak',      'Ecovita',        'Ribeye steak, per kg'),
                        (8,  3, 'White Sliced Bread',     'Kyivkhlib',      'Wheat bread loaf, 500g'),
                        (9,  3, 'Butter Croissant',       'Boulangerie',    'Butter croissant, single piece'),
                        (10, 3, 'Milk Baguette',          'Kyivkhlib',      'Milk baguette, 400g'),
                        (11, 4, 'Orange Juice',           'Sandora',        'Freshly squeezed, 1 liter'),
                        (12, 4, 'Mineral Water Still',    'Morshynska',     'Still mineral water, 1.5 liter bottle'),
                        (13, 4, 'Cola',                   'Coca-Cola',      'Carbonated drink, 0.33L can'),
                        (14, 4, 'Energy Drink',           'Monster Energy', 'Energy drink, 0.5L can'),
                        (15, 5, 'Golden Apples',          'Farm Fresh',     'Yellow apples, per kg'),
                        (16, 5, 'Cherry Tomatoes',        'Green Valley',   'Fresh cherry tomatoes, per kg'),
                        (17, 5, 'Bananas',                'Chiquita',       'Ripe bananas, per kg'),
                        (18, 6, 'Vanilla Ice Cream',      'Rud',            'Vanilla ice cream tub, 500ml'),
                        (19, 6, 'Frozen Pizza Margherita','Dr. Oetker',     'Margherita pizza, 350g'),
                        (20, 7, 'Potato Chips Salted',    'Lay''s',         'Salted chips, 150g bag'),
                        (21, 7, 'Milk Chocolate Bar',     'Roshen',         'Milk chocolate, 100g bar'),
                        (22, 7, 'Oreo Cookies',           'Oreo',           'Chocolate sandwich cookies, 176g'),
                        (23, 8, 'Dish Soap',              'Fairy',          'Liquid dish soap, 500ml bottle'),
                        (24, 8, 'Laundry Powder',         'Ariel',          'Automatic wash powder, 1.5kg box'),
                        (25, 9, 'Green Peas',             'Bonduelle',      'Canned green peas, 400g'),
                        (26, 9, 'Sweet Corn',             'Bonduelle',      'Canned sweet corn, 400g'),
                        -- Products NOT linked to any Store_Product (can be deleted safely):
                        (27, 5, 'Watermelon - no stock',  'Local Farm',     'Seasonal, per kg'),
                        (28, 3, 'Napoleon Cake - no stock','Home Bakery',   'Napoleon pastry, single piece');

-- ============================================================
-- STORE_PRODUCT
-- Regular: promotional_product = FALSE, UPC_prom = NULL
-- Promo:   promotional_product = TRUE,  UPC_prom = parent UPC (price = 80% of regular)
-- Edge cases:
--   UPC 000000000003 — quantity = 0 (out of stock)
--   UPC 000000000023 — minimum price 0.01
--   UPC 100000000020 — promo NOT linked to any sale (can be deleted)
-- ============================================================
INSERT INTO Store_Product VALUES
                              ('000000000001', NULL, 1,  52.9900,  120, FALSE),  -- Milk
                              ('000000000002', NULL, 2,  98.5000,   45, FALSE),  -- Butter
                              ('000000000003', NULL, 3,  35.9900,    0, FALSE),  -- Yogurt (out of stock)
                              ('000000000004', NULL, 4,  62.0000,   60, FALSE),  -- Cottage Cheese
                              ('000000000005', NULL, 5, 135.0000,   80, FALSE),  -- Chicken Breast
                              ('000000000006', NULL, 6, 155.0000,   35, FALSE),  -- Pork Chops
                              ('000000000007', NULL, 7, 320.0000,   15, FALSE),  -- Beef Ribeye
                              ('000000000008', NULL, 8,  32.5000,  300, FALSE),  -- White Bread
                              ('000000000009', NULL, 9,  28.0000,  150, FALSE),  -- Croissant
                              ('000000000010', NULL, 10, 29.9900,  200, FALSE),  -- Milk Baguette
                              ('000000000011', NULL, 11, 59.9900,   90, FALSE),  -- Orange Juice
                              ('000000000012', NULL, 12, 22.0000,  400, FALSE),  -- Mineral Water
                              ('000000000013', NULL, 13, 28.5000,  500, FALSE),  -- Cola
                              ('000000000014', NULL, 14, 45.0000,  200, FALSE),  -- Energy Drink
                              ('000000000015', NULL, 15, 48.0000,  180, FALSE),  -- Golden Apples
                              ('000000000016', NULL, 16, 72.0000,  100, FALSE),  -- Cherry Tomatoes
                              ('000000000017', NULL, 17, 38.5000,  250, FALSE),  -- Bananas
                              ('000000000018', NULL, 18, 85.0000,   40, FALSE),  -- Ice Cream
                              ('000000000019', NULL, 19, 95.0000,   55, FALSE),  -- Frozen Pizza
                              ('000000000020', NULL, 20, 42.0000,  600, FALSE),  -- Chips
                              ('000000000021', NULL, 21, 49.9900,  350, FALSE),  -- Chocolate Bar
                              ('000000000022', NULL, 22, 55.0000,  180, FALSE),  -- Oreo Cookies
                              ('000000000023', NULL, 23,  0.0100,   10, FALSE),  -- Dish Soap (min price edge case)
                              ('000000000024', NULL, 24, 189.0000,  70, FALSE),  -- Laundry Powder
                              ('000000000025', NULL, 25, 34.9900,  220, FALSE),  -- Green Peas
                              ('000000000026', NULL, 26, 34.9900,  220, FALSE);  -- Sweet Corn

-- Promotional products (80% of regular price)
INSERT INTO Store_Product VALUES
                              ('100000000001', '000000000001',  1,  42.3900,  60, TRUE),  -- Milk -20%
                              ('100000000002', '000000000002',  2,  78.8000,  20, TRUE),  -- Butter -20%
                              ('100000000005', '000000000005',  5, 108.0000,  30, TRUE),  -- Chicken Breast -20%
                              ('100000000011', '000000000011', 11,  47.9900,  40, TRUE),  -- Orange Juice -20%
                              ('100000000018', '000000000018', 18,  68.0000,  15, TRUE),  -- Ice Cream -20%
                              ('100000000021', '000000000021', 21,  39.9900, 100, TRUE),  -- Chocolate -20%
                              ('100000000020', '000000000020', 20,  33.6000,  50, TRUE);  -- Chips -20% (no sales — can be deleted)

-- ============================================================
-- CUSTOMER CARDS
-- Edge cases:
--   C0000000002 — 0% discount
--   C0000000005 — 100% discount
--   C0000000008, C0000000009 — no address (NULL city/street/zip)
--   C0000000010 — NOT linked to any check (can be deleted safely)
-- ============================================================
INSERT INTO Customer_Card VALUES
                              ('C0000000001', 'Anderson',    'Emma',     'Victoria',      '+380501111111', 'Kyiv',         'Peremohy Ave',  '03115', 10),
                              ('C0000000002', 'Peterson',    'Mykola',   'Ivanovych',     '+380672222222', 'Kharkiv',      'Pushkinska St', '61000',  0),  -- 0% discount
                              ('C0000000003', 'Sydorenko',   'Sofia',    NULL,            '+380933333333', 'Lviv',         'Svobody Ave',   '79000', 50),  -- 50% discount, no patronymic
                              ('C0000000004', 'Kravchenko',  'Oleksiy',  'Pavlovych',     '+380994444444', 'Odesa',        'Pushkinska St', '65000',  5),
                              ('C0000000005', 'Nechyporenko','Halyna',   NULL,            '+380635555555', NULL,            NULL,            NULL,   100), -- 100% discount, no address
                              ('C0000000006', 'Diachenko',   'Roman',    'Oleksandrovych','+380666666666', 'Dnipro',       'Naberezhna St', '49000', 15),
                              ('C0000000007', 'Lytvynenko',  'Alina',    NULL,            '+380677777777', 'Zaporizhzhia', 'Sobornyi Ave',  '69000', 20),
                              ('C0000000008', 'Oliynyk',     'Ihor',     'Stepanovych',   '+380508888888', NULL,            NULL,            NULL,    3),  -- no address
                              ('C0000000009', 'Vasylenko',   'Taras',    'Hryhorovych',   '+380509999999', NULL,            NULL,            NULL,    7),  -- no address
                              ('C0000000010', 'Ostapenko',   'Liudmyla', 'Viktorivna',    '+380630000001', 'Sumy',         'Soborna St',    '40000', 12); -- NOT linked to any check — can be deleted

-- ============================================================
-- CHECKS (2025-2026)
-- VAT = 20% of sum_total
-- Edge cases:
--   CH00000003 — minimum sum 0.01
--   CH00000006 — no customer card
--   CH00000020 — large check, 10 line items
-- ============================================================
INSERT INTO Check_Table VALUES
                            ('CH00000001', 'EMP0000004', 'C0000000001', '2025-01-10 09:15:00', 214.4800, 42.8960),
                            ('CH00000002', 'EMP0000005', NULL,           '2025-01-12 11:30:00', 135.0000, 27.0000),
                            ('CH00000003', 'EMP0000006', 'C0000000002',  '2025-01-15 14:00:00',   0.0100,  0.0020), -- min sum
                            ('CH00000004', 'EMP0000007', 'C0000000003',  '2025-01-20 16:45:00', 320.0000, 64.0000),
                            ('CH00000005', 'EMP0000004', NULL,            '2025-02-03 10:00:00', 190.4800, 38.0960),
                            ('CH00000006', 'EMP0000005', NULL,            '2025-02-14 13:20:00',  98.5000, 19.7000), -- no card
                            ('CH00000007', 'EMP0000006', 'C0000000004',  '2025-02-20 09:50:00', 227.0000, 45.4000),
                            ('CH00000008', 'EMP0000009', 'C0000000006',  '2025-03-05 15:10:00', 246.9700, 49.3940),
                            ('CH00000009', 'EMP0000004', NULL,            '2025-03-12 11:00:00',  85.0000, 17.0000),
                            ('CH00000010', 'EMP0000007', 'C0000000007',  '2025-03-25 17:30:00', 399.9200, 79.9840),
                            ('CH00000011', 'EMP0000005', 'C0000000001',  '2025-04-02 10:15:00', 133.5000, 26.7000),
                            ('CH00000012', 'EMP0000006', NULL,            '2025-04-18 14:00:00', 129.9700, 25.9940),
                            ('CH00000013', 'EMP0000009', 'C0000000008',  '2025-05-07 09:30:00', 317.0000, 63.4000),
                            ('CH00000014', 'EMP0000004', 'C0000000004',  '2025-05-22 12:45:00', 208.5000, 41.7000),
                            ('CH00000015', 'EMP0000007', NULL,            '2025-06-10 16:00:00',  76.5000, 15.3000),
                            ('CH00000016', 'EMP0000005', 'C0000000003',  '2025-07-04 10:30:00', 548.9800, 109.7960),
                            ('CH00000017', 'EMP0000006', 'C0000000006',  '2025-08-15 13:15:00', 153.9700, 30.7940),
                            ('CH00000018', 'EMP0000009', NULL,            '2025-09-01 09:00:00', 247.0000, 49.4000),
                            ('CH00000019', 'EMP0000004', 'C0000000007',  '2025-10-10 11:45:00', 261.5700, 52.3140),
                            ('CH00000020', 'EMP0000007', 'C0000000001',  '2025-11-28 15:00:00', 894.4300, 178.8860), -- large check
                            ('CH00000021', 'EMP0000005', NULL,            '2025-12-05 10:20:00', 142.0000, 28.4000),
                            ('CH00000022', 'EMP0000006', 'C0000000002',  '2025-12-24 18:00:00', 437.9800, 87.5960), -- Christmas Eve
                            ('CH00000023', 'EMP0000009', 'C0000000009',  '2026-01-07 12:00:00', 251.0000, 50.2000),
                            ('CH00000024', 'EMP0000004', 'C0000000009',   '2026-01-15 09:30:00', 123.5000, 24.7000),
                            ('CH00000025', 'EMP0000010', 'C0000000004',  '2026-02-01 14:00:00',  82.9800, 16.5960),
                            ('CH00000026', 'EMP0000007', 'C0000000008',  '2026-02-14 16:30:00', 134.9800, 26.9960), -- Valentine''s Day
                            ('CH00000027', 'EMP0000005', NULL,            '2026-03-08 11:00:00', 241.5000, 48.3000),
                            ('CH00000028', 'EMP0000009', 'C0000000006',  '2026-03-20 13:45:00', 255.0100, 51.0020),
                            ('CH00000029', 'EMP0000004', 'C0000000007',  '2026-04-01 10:00:00', 130.9700, 26.1940),
                            ('CH00000030', 'EMP0000010', 'C0000000001', '2026-04-10 15:30:00',  71.0000, 14.2000);
-- ============================================================
-- SALES
-- ============================================================

-- CH00000001: Milk x2, Butter x1, White Bread x1
INSERT INTO Sale VALUES
                     ('000000000001', 'CH00000001', 2,  52.9900),
                     ('000000000002', 'CH00000001', 1,  98.5000),
                     ('000000000008', 'CH00000001', 1,  32.5000);

-- CH00000002: Chicken Breast x1
INSERT INTO Sale VALUES
    ('000000000005', 'CH00000002', 1, 135.0000);

-- CH00000003: Dish Soap x1 (min price 0.01)
INSERT INTO Sale VALUES
    ('000000000023', 'CH00000003', 1,   0.0100);

-- CH00000004: Beef Ribeye x1
INSERT INTO Sale VALUES
    ('000000000007', 'CH00000004', 1, 320.0000);

-- CH00000005: Orange Juice x2, White Bread x1, Croissant x1
INSERT INTO Sale VALUES
                     ('000000000011', 'CH00000005', 2,  59.9900),
                     ('000000000008', 'CH00000005', 1,  32.5000),
                     ('000000000009', 'CH00000005', 1,  28.0000);

-- CH00000006: Butter x1 (no customer card)
INSERT INTO Sale VALUES
    ('000000000002', 'CH00000006', 1,  98.5000);

-- CH00000007: Pork Chops x1, Cherry Tomatoes x1
INSERT INTO Sale VALUES
                     ('000000000006', 'CH00000007', 1, 155.0000),
                     ('000000000016', 'CH00000007', 1,  72.0000);

-- CH00000008: Milk promo x3, Orange Juice x2
INSERT INTO Sale VALUES
                     ('100000000001', 'CH00000008', 3,  42.3900),
                     ('000000000011', 'CH00000008', 2,  59.9900);

-- CH00000009: Ice Cream x1
INSERT INTO Sale VALUES
    ('000000000018', 'CH00000009', 1,  85.0000);

-- CH00000010: Chocolate x4, Chips x2, Oreo x2
INSERT INTO Sale VALUES
                     ('000000000021', 'CH00000010', 4,  49.9900),
                     ('000000000020', 'CH00000010', 2,  42.0000),
                     ('000000000022', 'CH00000010', 2,  55.0000);

-- CH00000011: Frozen Pizza x1, Bananas x1
INSERT INTO Sale VALUES
                     ('000000000019', 'CH00000011', 1,  95.0000),
                     ('000000000017', 'CH00000011', 1,  38.5000);

-- CH00000012: Milk Baguette x2, Green Peas x1, Sweet Corn x1
INSERT INTO Sale VALUES
                     ('000000000010', 'CH00000012', 2,  29.9900),
                     ('000000000025', 'CH00000012', 1,  34.9900),
                     ('000000000026', 'CH00000012', 1,  34.9900);

-- CH00000013: Pork Chops x1, Golden Apples x2, Mineral Water x3
INSERT INTO Sale VALUES
                     ('000000000006', 'CH00000013', 1, 155.0000),
                     ('000000000015', 'CH00000013', 2,  48.0000),
                     ('000000000012', 'CH00000013', 3,  22.0000);

-- CH00000014: Chicken Breast promo x1, Cottage Cheese x1, Bananas x1
INSERT INTO Sale VALUES
                     ('100000000005', 'CH00000014', 1, 108.0000),
                     ('000000000004', 'CH00000014', 1,  62.0000),
                     ('000000000017', 'CH00000014', 1,  38.5000);

-- CH00000015: Cola x1, Golden Apples x1
INSERT INTO Sale VALUES
                     ('000000000013', 'CH00000015', 1,  28.5000),
                     ('000000000015', 'CH00000015', 1,  48.0000);

-- CH00000016: Beef Ribeye x1, Laundry Powder x1, Chocolate promo x2
INSERT INTO Sale VALUES
                     ('000000000007', 'CH00000016', 1, 320.0000),
                     ('000000000024', 'CH00000016', 1, 189.0000),
                     ('100000000021', 'CH00000016', 2,  39.9900);

-- CH00000017: Milk x2, White Bread x1, Milk Baguette x1
INSERT INTO Sale VALUES
                     ('000000000001', 'CH00000017', 2,  52.9900),
                     ('000000000008', 'CH00000017', 1,  32.5000),
                     ('000000000010', 'CH00000017', 1,  29.9900);

-- CH00000018: Frozen Pizza x2, Cola x2, Energy Drink x1
INSERT INTO Sale VALUES
                     ('000000000019', 'CH00000018', 2,  95.0000),
                     ('000000000013', 'CH00000018', 2,  28.5000),
                     ('000000000014', 'CH00000018', 1,  45.0000);

-- CH00000019: Butter promo x2, Orange Juice promo x2, Green Peas x1
INSERT INTO Sale VALUES
                     ('100000000002', 'CH00000019', 2,  78.8000),
                     ('100000000011', 'CH00000019', 2,  47.9900),
                     ('000000000025', 'CH00000019', 1,  34.9900);

-- CH00000020: Large check — 10 line items
INSERT INTO Sale VALUES
                     ('000000000001', 'CH00000020', 3,  52.9900),  -- Milk x3
                     ('000000000005', 'CH00000020', 2, 135.0000),  -- Chicken Breast x2
                     ('000000000008', 'CH00000020', 2,  32.5000),  -- White Bread x2
                     ('000000000011', 'CH00000020', 2,  59.9900),  -- Orange Juice x2
                     ('000000000015', 'CH00000020', 2,  48.0000),  -- Golden Apples x2
                     ('000000000021', 'CH00000020', 3,  49.9900),  -- Chocolate x3
                     ('000000000024', 'CH00000020', 1, 189.0000),  -- Laundry Powder x1
                     ('000000000012', 'CH00000020', 4,  22.0000),  -- Mineral Water x4
                     ('000000000022', 'CH00000020', 1,  55.0000),  -- Oreo Cookies x1
                     ('000000000016', 'CH00000020', 1,  72.0000);  -- Cherry Tomatoes x1

-- CH00000021: Energy Drink x2, Chips x1
INSERT INTO Sale VALUES
                     ('000000000014', 'CH00000021', 2,  45.0000),
                     ('000000000020', 'CH00000021', 1,  42.0000);

-- CH00000022: Beef Ribeye x1, Chocolate x2, Ice Cream promo x1 (Christmas Eve)
INSERT INTO Sale VALUES
                     ('000000000007', 'CH00000022', 1, 320.0000),
                     ('000000000021', 'CH00000022', 2,  49.9900),
                     ('100000000018', 'CH00000022', 1,  68.0000);

-- CH00000023: Chicken Breast x1, Cherry Tomatoes x1, Mineral Water x2
INSERT INTO Sale VALUES
                     ('000000000005', 'CH00000023', 1, 135.0000),
                     ('000000000016', 'CH00000023', 1,  72.0000),
                     ('000000000012', 'CH00000023', 2,  22.0000);

-- CH00000024: Frozen Pizza x1, Cola x1, Croissant x1
INSERT INTO Sale VALUES
                     ('000000000019', 'CH00000024', 1,  95.0000),
                     ('000000000013', 'CH00000024', 1,  28.5000),
                     ('000000000009', 'CH00000024', 1,  28.0000);

-- CH00000025: Milk x1, Milk Baguette x1
INSERT INTO Sale VALUES
                     ('000000000001', 'CH00000025', 1,  52.9900),
                     ('000000000010', 'CH00000025', 1,  29.9900);

-- CH00000026: Chocolate promo x2, Oreo Cookies x1 (Valentine's Day)
INSERT INTO Sale VALUES
                     ('100000000021', 'CH00000026', 2,  39.9900),
                     ('000000000022', 'CH00000026', 1,  55.0000);

-- CH00000027: Pork Chops x1, Golden Apples x1, Bananas x1
INSERT INTO Sale VALUES
                     ('000000000006', 'CH00000027', 1, 155.0000),
                     ('000000000015', 'CH00000027', 1,  48.0000),
                     ('000000000017', 'CH00000027', 1,  38.5000);

-- CH00000028: Laundry Powder x1, Dish Soap x1, Mineral Water x3
INSERT INTO Sale VALUES
                     ('000000000024', 'CH00000028', 1, 189.0000),
                     ('000000000023', 'CH00000028', 1,   0.0100),
                     ('000000000012', 'CH00000028', 3,  22.0000);

-- CH00000029: Orange Juice promo x2, Green Peas x1
INSERT INTO Sale VALUES
                     ('100000000011', 'CH00000029', 2,  47.9900),
                     ('000000000025', 'CH00000029', 1,  34.9900);

-- CH00000030: White Bread x1, Bananas x1
INSERT INTO Sale VALUES
                     ('000000000008', 'CH00000030', 1,  32.5000),
                     ('000000000017', 'CH00000030', 1,  38.5000);