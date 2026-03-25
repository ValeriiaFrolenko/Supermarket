-- Zlagoda Supermarket Test Data for SQLite

-- Enable foreign key constraints (SQLite requires this)
PRAGMA foreign_keys = ON;

-- Insert Employees (edge cases: NULL patronymic, minimum age 18, minimum salary 0)
INSERT OR IGNORE INTO Employee VALUES
                         ('EMP0000001','$2a$10$VavcbDs5wojYs8TPs32dRe.dE60UR8.odOQv4ZNSwGKVBhhWUCIwC', 'Smith', 'John', NULL, 'manager', 25000.0000, '2000-01-01', '2020-01-15', '+380501234567', 'Kyiv', 'Shevchenka St', '01001'),
                         ('EMP0000002', '$2a$10$UXxaBrMUMCh3OWp0KB7Yvu7xKxGrtzeSVoTZ4QO4BP6snGXyRya6u','Johnson', 'Mary', 'Ann', 'cashier', 12000.5000, '1995-06-15', '2021-03-20', '+380672345678', 'Lviv', 'Franka St', '79000'),
                         ('EMP0000003', '$2a$10$3gXhiStA8keP6rVpZQCI1.H5wQFH4Y0hJgiM39KuuEa2nVqlNFvoa','Williams', 'Robert', NULL, 'cashier', 0.0000, '2006-03-25', '2024-03-26', '+380933456789', 'Odesa', 'Deribasivska St', '65000'),
                         ('EMP0000004', '$2a$10$Dy0V/tpsgMTTfY/qx3JIVOlwHn.mOa0eys/8NRjkTDn8LSW/jO2BS','Brown', 'Patricia', 'Lee', 'manager', 50000.9999, '1985-12-31', '2015-07-01', '+380994567890', 'Dnipro', 'Gagarina Ave', '49000'),
                         ('EMP0000005', '$2a$10$a3IRg52fwkkewN7ZBQEWVeZ.H4yInDNtnZBYTpi/13Nmgl1346Oo6','Jones', 'Michael', NULL, 'cashier', 13500.0000, '1998-08-20', '2022-11-10', '+380635678901', 'Kharkiv', 'Sumska St', '61000'),
                         ('EMP0000006', '$2a$10$nBvEzv.gJgdiAxUGXp5X.u/unkbYv62ZyGBCiIQJG7ZEq1sT8I836','Garcia', 'Linda', 'Marie', 'cashier', 11000.0000, '2003-02-14', '2023-02-15', '+380666789012', 'Zaporizhzhia', 'Sobornyi Ave', '69000'),
                         ('EMP0000007', '$2a$10$R9B2FVqrE6FzYIbvtw2XPuWiyt1kyL852JeAVOosOA4Pg7fAb5N.q','Miller', 'David', NULL, 'manager', 30000.0000, '1990-05-05', '2018-09-01', '+380677890123', 'Kyiv', 'Khreshchatyk St', '01001'),
                         ('EMP0000008', '$2a$10$gAHwoBP8hX1GvJizhFwuweZbYmkp8tWSUUpG2tJ5ovNz.n5fZxO6q','Davis', 'Barbara', 'Jean', 'cashier', 12500.0000, '1999-11-30', '2022-06-15', '+380508901234', 'Lviv', 'Horodotska St', '79000');

-- Insert Categories
INSERT OR IGNORE INTO Category VALUES
                         (1, 'Dairy Products'),
                         (2, 'Meat & Poultry'),
                         (3, 'Bakery'),
                         (4, 'Beverages'),
                         (5, 'Fruits & Vegetables'),
                         (6, 'Frozen Foods'),
                         (7, 'Snacks'),
                         (8, 'Household Items');

-- Insert Products
INSERT OR IGNORE INTO Product VALUES
                        (1, 1, 'Milk 2.5%', 'UHT milk, 1 liter pack'),
                        (2, 1, 'Butter', 'Unsalted butter, 200g pack'),
                        (3, 2, 'Chicken Breast', 'Fresh chicken breast, per kg'),
                        (4, 3, 'White Bread', 'Sliced white bread, 500g loaf'),
                        (5, 4, 'Orange Juice', 'Freshly squeezed, 1 liter'),
                        (6, 5, 'Apples', 'Red apples, per kg'),
                        (7, 6, 'Ice Cream Vanilla', 'Vanilla ice cream, 500ml tub'),
                        (8, 7, 'Potato Chips', 'Salted chips, 150g pack'),
                        (9, 8, 'Dish Soap', 'Liquid dish soap, 500ml bottle'),
                        (10, 1, 'Yogurt Natural', 'Natural yogurt, 250g cup'),
                        (11, 2, 'Pork Chops', 'Fresh pork chops, per kg'),
                        (12, 3, 'Croissant', 'Butter croissant, single piece'),
                        (13, 4, 'Mineral Water', 'Still mineral water, 1.5 liter'),
                        (14, 5, 'Tomatoes', 'Fresh tomatoes, per kg'),
                        (15, 7, 'Chocolate Bar', 'Milk chocolate, 100g bar');

-- Insert Store_Products (edge cases: 0 quantity, minimum price, promotional items)
-- Regular products first (promotional_product = 0, UPC_prom = NULL)
INSERT OR IGNORE INTO Store_Product VALUES
                              ('000000000001', NULL, 1, 45.5000, 100, 0),
                              ('000000000002', NULL, 2, 89.9900, 50, 0),
                              ('000000000003', NULL, 3, 125.0000, 0, 0),  -- Edge case: 0 quantity
                              ('000000000004', NULL, 4, 30.0000, 200, 0),
                              ('000000000005', NULL, 5, 55.7500, 75, 0),
                              ('000000000006', NULL, 6, 42.0000, 150, 0),
                              ('000000000007', NULL, 7, 78.0000, 30, 0),
                              ('000000000008', NULL, 8, 35.5000, 500, 0),
                              ('000000000009', NULL, 9, 0.0100, 10, 0),  -- Edge case: minimum price
                              ('000000000010', NULL, 10, 28.5000, 80, 0),
                              ('000000000011', NULL, 11, 145.0000, 40, 0),
                              ('000000000012', NULL, 12, 22.0000, 120, 0),
                              ('000000000013', NULL, 13, 18.0000, 300, 0),
                              ('000000000014', NULL, 14, 38.5000, 100, 0),
                              ('000000000015', NULL, 15, 45.0000, 250, 0);

-- Promotional products (promotional_product = 1, UPC_prom points to regular product)
-- Price is 80% of regular price
INSERT OR IGNORE INTO Store_Product VALUES
                              ('100000000001', '000000000001', 1, 36.4000, 50, 1),   -- Milk promo: 45.50 * 0.8 = 36.40
                              ('100000000002', '000000000002', 2, 71.9920, 25, 1),   -- Butter promo: 89.99 * 0.8 = 71.992
                              ('100000000007', '000000000007', 7, 62.4000, 15, 1),   -- Ice Cream promo: 78.00 * 0.8 = 62.40
                              ('100000000015', '000000000015', 15, 36.0000, 100, 1); -- Chocolate promo: 45.00 * 0.8 = 36.00

-- Insert Customer Cards (edge cases: NULL patronymic, NULL address, 0% discount, 100% discount)
INSERT OR IGNORE INTO Customer_Card VALUES
                              ('C0000000001', 'Anderson', 'Emma', NULL, '+380501111111', 'Kyiv', 'Peremohy Ave', '03115', 10),
                              ('C0000000002', 'Taylor', 'James', 'Thomas', '+380672222222', NULL, NULL, NULL, 0),  -- Edge case: 0% discount, no address
                              ('C0000000003', 'Martinez', 'Sophia', NULL, '+380933333333', 'Lviv', 'Svobody Ave', '79000', 50),
                              ('C0000000004', 'Robinson', 'Oliver', 'William', '+380994444444', 'Odesa', 'Pushkinska St', '65000', 5),
                              ('C0000000005', 'Clark', 'Isabella', NULL, '+380635555555', NULL, NULL, NULL, 100),  -- Edge case: 100% discount, no address
                              ('C0000000006', 'Rodriguez', 'Liam', 'Alexander', '+380666666666', 'Dnipro', 'Naberezhna St', '49000', 15),
                              ('C0000000007', 'Lewis', 'Mia', NULL, '+380677777777', 'Kharkiv', 'Pushkinska St', '61000', 20),
                              ('C0000000008', 'Walker', 'Noah', 'James', '+380508888888', NULL, NULL, NULL, 3);  -- Edge case: no address

-- Insert Checks (edge cases: NULL card_number, minimum sum, various dates)
-- VAT is always 20% of sum_total
INSERT OR IGNORE INTO Check_Table VALUES
                            ('0000000001', 'EMP0000002', 'C0000000001', '2024-01-15 10:30:00', 91.0000, 18.2000),
                            ('0000000002', 'EMP0000003', NULL, '2024-01-15 14:45:00', 125.0000, 25.0000),  -- No customer card
                            ('0000000003', 'EMP0000005', 'C0000000003', '2024-01-16 09:15:00', 0.0100, 0.0020),  -- Edge case: minimum sum
                            ('0000000004', 'EMP0000006', 'C0000000002', '2024-01-16 16:20:00', 200.0000, 40.0000),
                            ('0000000005', 'EMP0000002', NULL, '2024-01-17 11:00:00', 89.9900, 17.9980),  -- No customer card
                            ('0000000006', 'EMP0000008', 'C0000000005', '2024-01-17 13:30:00', 78.0000, 15.6000),
                            ('0000000007', 'EMP0000005', 'C0000000006', '2024-01-18 10:00:00', 455.0000, 91.0000),
                            ('0000000008', 'EMP0000003', NULL, '2024-01-18 15:45:00', 30.0000, 6.0000),  -- No customer card
                            ('0000000009', 'EMP0000006', 'C0000000004', '2024-01-19 12:15:00', 180.0000, 36.0000),
                            ('0000000010', 'EMP0000002', 'C0000000007', '2024-01-20 09:30:00', 350.0000, 70.0000),
                            ('0000000011', 'EMP0000008', NULL, '2024-02-01 10:00:00', 150.0000, 30.0000),  -- No customer card
                            ('0000000012', 'EMP0000005', 'C0000000008', '2024-02-05 14:00:00', 240.0000, 48.0000),
                            ('0000000013', 'EMP0000003', NULL, '2024-02-10 11:30:00', 95.0000, 19.0000),  -- No customer card
                            ('0000000014', 'EMP0000006', 'C0000000001', '2024-02-15 16:45:00', 420.0000, 84.0000),
                            ('0000000015', 'EMP0000002', NULL, '2024-03-01 10:15:00', 75.0000, 15.0000);  -- No customer card

-- Insert Sales (various quantities and prices)
-- Check 0000000001: 2 items
INSERT OR IGNORE INTO Sale VALUES
                     ('000000000001', '0000000001', 2, 45.5000),  -- 2x Milk
                     ('000000000004', '0000000001', 1, 30.0000);  -- 1x White Bread

-- Check 0000000002: 1 item
INSERT OR IGNORE INTO Sale VALUES
    ('000000000003', '0000000002', 1, 125.0000);  -- 1x Chicken Breast

-- Check 0000000003: 1 item (edge case: minimum price)
INSERT OR IGNORE INTO Sale VALUES
    ('000000000009', '0000000003', 1, 0.0100);  -- 1x Dish Soap

-- Check 0000000004: 3 items
INSERT OR IGNORE INTO Sale VALUES
                     ('000000000005', '0000000004', 2, 55.7500),  -- 2x Orange Juice
                     ('000000000006', '0000000004', 1, 42.0000),  -- 1x Apples
                     ('000000000012', '0000000004', 2, 22.0000);  -- 2x Croissants

-- Check 0000000005: 1 item
INSERT OR IGNORE INTO Sale VALUES
    ('000000000002', '0000000005', 1, 89.9900);  -- 1x Butter

-- Check 0000000006: 1 promotional item
INSERT OR IGNORE INTO Sale VALUES
    ('100000000007', '0000000006', 1, 62.4000);  -- 1x Ice Cream (promo)

-- Check 0000000007: 5 items (large quantity)
INSERT OR IGNORE INTO Sale VALUES
    ('000000000001', '0000000007', 10, 45.5000);  -- 10x Milk

-- Check 0000000008: 1 item
INSERT OR IGNORE INTO Sale VALUES
    ('000000000004', '0000000008', 1, 30.0000);  -- 1x White Bread

-- Check 0000000009: 2 promotional items
INSERT OR IGNORE INTO Sale VALUES
                     ('100000000001', '0000000009', 3, 36.4000),  -- 3x Milk (promo)
                     ('100000000015', '0000000009', 2, 36.0000);  -- 2x Chocolate (promo)

-- Check 0000000010: 4 items
INSERT OR IGNORE INTO Sale VALUES
    ('000000000008', '0000000010', 10, 35.5000);  -- 10x Potato Chips

-- Check 0000000011: 2 items
INSERT OR IGNORE INTO Sale VALUES
                     ('000000000013', '0000000011', 5, 18.0000),  -- 5x Mineral Water
                     ('000000000010', '0000000011', 2, 28.5000);  -- 2x Yogurt

-- Check 0000000012: 3 items
INSERT OR IGNORE INTO Sale VALUES
                     ('000000000014', '0000000012', 3, 38.5000),  -- 3x Tomatoes
                     ('000000000015', '0000000012', 2, 45.0000),  -- 2x Chocolate
                     ('000000000012', '0000000012', 1, 22.0000);  -- 1x Croissant

-- Check 0000000013: 2 items
INSERT OR IGNORE INTO Sale VALUES
                     ('000000000006', '0000000013', 1, 42.0000),  -- 1x Apples
                     ('000000000007', '0000000013', 1, 78.0000);  -- 1x Ice Cream

-- Check 0000000014: 4 promotional items
INSERT OR IGNORE INTO Sale VALUES
                     ('100000000002', '0000000014', 3, 71.9920),  -- 3x Butter (promo)
                     ('100000000001', '0000000014', 5, 36.4000);  -- 5x Milk (promo)

-- Check 0000000015: 2 items
INSERT OR IGNORE INTO Sale VALUES
                     ('000000000011', '0000000015', 1, 145.0000),  -- 1x Pork Chops
                     ('000000000004', '0000000015', 1, 30.0000);  -- 1x White Bread

