-- Zlagoda Supermarket Database Schema for H2
-- Creates tables only if they don't exist

-- Employee table

DROP TABLE IF EXISTS Sale;
DROP TABLE IF EXISTS Check_Table;
DROP TABLE IF EXISTS Customer_Card;
DROP TABLE IF EXISTS Store_Product;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Employee;

CREATE TABLE IF NOT EXISTS Employee (
                                        id_employee VARCHAR(50) PRIMARY KEY,
                                        password_hash VARCHAR(255) NOT NULL,
                                        empl_surname VARCHAR(100) NOT NULL,
                                        empl_name VARCHAR(100) NOT NULL,
                                        empl_patronymic VARCHAR(100),
                                        empl_role VARCHAR(20) NOT NULL CHECK (empl_role IN ('CASHIER', 'MANAGER')),
                                        salary DECIMAL(10,2) NOT NULL CHECK (salary >= 0),
                                        date_of_birth DATE NOT NULL,
                                        date_of_start DATE NOT NULL,
                                        phone_number VARCHAR(20) NOT NULL,
                                        city VARCHAR(100) NOT NULL,
                                        street VARCHAR(200) NOT NULL,
                                        zip_code VARCHAR(20) NOT NULL
);

-- Category table
CREATE TABLE IF NOT EXISTS Category (
                                        category_number INT PRIMARY KEY AUTO_INCREMENT,
                                        category_name VARCHAR(100) NOT NULL
);

-- Product table
CREATE TABLE IF NOT EXISTS Product (
                                       id_product INT PRIMARY KEY AUTO_INCREMENT,
                                       category_number INT NOT NULL,
                                       product_name VARCHAR(200) NOT NULL,
                                       characteristics TEXT NOT NULL,
                                       FOREIGN KEY (category_number) REFERENCES Category(category_number)
                                           ON UPDATE CASCADE
                                           ON DELETE RESTRICT
);

-- Store_Product table
CREATE TABLE IF NOT EXISTS Store_Product (
                                             UPC VARCHAR(50) PRIMARY KEY,
                                             UPC_prom VARCHAR(50),
                                             id_product INT NOT NULL,
                                             selling_price DECIMAL(10,2) NOT NULL CHECK (selling_price >= 0),
                                             products_number INT NOT NULL CHECK (products_number >= 0),
                                             promotional_product BOOLEAN NOT NULL,
                                             FOREIGN KEY (id_product) REFERENCES Product(id_product)
                                                 ON UPDATE CASCADE
                                                 ON DELETE RESTRICT,
                                             FOREIGN KEY (UPC_prom) REFERENCES Store_Product(UPC)
                                                 ON UPDATE CASCADE
                                                 ON DELETE SET NULL
);

-- Customer_Card table
CREATE TABLE IF NOT EXISTS Customer_Card (
                                             card_number VARCHAR(50) PRIMARY KEY,
                                             cust_surname VARCHAR(100) NOT NULL,
                                             cust_name VARCHAR(100) NOT NULL,
                                             cust_patronymic VARCHAR(100),
                                             phone_number VARCHAR(20) NOT NULL,
                                             city VARCHAR(100),
                                             street VARCHAR(200),
                                             zip_code VARCHAR(20),
                                             percent INT NOT NULL CHECK (percent >= 0 AND percent <= 100)
);

-- Check_Table (renamed to avoid SQL keyword conflict)
CREATE TABLE IF NOT EXISTS Check_Table (
                                           check_number VARCHAR(50) PRIMARY KEY,
                                           id_employee VARCHAR(50) NOT NULL,
                                           card_number VARCHAR(50),
                                           print_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           sum_total DECIMAL(10,2) NOT NULL CHECK (sum_total >= 0),
                                           vat DECIMAL(10,2) NOT NULL CHECK (vat >= 0),
                                           FOREIGN KEY (id_employee) REFERENCES Employee(id_employee)
                                               ON UPDATE CASCADE
                                               ON DELETE RESTRICT,
                                           FOREIGN KEY (card_number) REFERENCES Customer_Card(card_number)
                                               ON UPDATE CASCADE
                                               ON DELETE RESTRICT
);

-- Sale table
CREATE TABLE IF NOT EXISTS Sale (
                                    UPC VARCHAR(50),
                                    check_number VARCHAR(50),
                                    product_number INT NOT NULL CHECK (product_number > 0),
                                    selling_price DECIMAL(10,2) NOT NULL CHECK (selling_price >= 0),
                                    PRIMARY KEY (UPC, check_number),
                                    FOREIGN KEY (UPC) REFERENCES Store_Product(UPC)
                                        ON UPDATE CASCADE
                                        ON DELETE RESTRICT,
                                    FOREIGN KEY (check_number) REFERENCES Check_Table(check_number)
                                        ON UPDATE CASCADE
                                        ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_employee_surname ON Employee(empl_surname);
CREATE INDEX IF NOT EXISTS idx_employee_role ON Employee(empl_role);
CREATE INDEX IF NOT EXISTS idx_product_name ON Product(product_name);
CREATE INDEX IF NOT EXISTS idx_product_category ON Product(category_number);
CREATE INDEX IF NOT EXISTS idx_store_product_promotional ON Store_Product(promotional_product);
CREATE INDEX IF NOT EXISTS idx_store_product_product ON Store_Product(id_product);
CREATE INDEX IF NOT EXISTS idx_check_date ON Check_Table(print_date);
CREATE INDEX IF NOT EXISTS idx_check_employee ON Check_Table(id_employee);
CREATE INDEX IF NOT EXISTS idx_check_card ON Check_Table(card_number);
CREATE INDEX IF NOT EXISTS idx_customer_surname ON Customer_Card(cust_surname);
CREATE INDEX IF NOT EXISTS idx_sale_check ON Sale(check_number);