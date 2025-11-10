-- Tạo bảng Category
CREATE TABLE Category
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Tạo bảng Product
CREATE TABLE Product
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    price       DECIMAL(15, 2) NOT NULL,
    in_stock    BOOLEAN        NOT NULL,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES Category (id)
);

-- Tạo bảng Comment
CREATE TABLE Comment
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    text       TEXT NOT NULL,
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES Product (id)
);

-- Tạo bảng Role
CREATE TABLE Role
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Tạo bảng Customer
CREATE TABLE Customer
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL,
    customer_since DATE         NOT NULL
);

-- Tạo bảng Customer_Role (Nhiều nhiều giữa Customer và Role)
CREATE TABLE Customer_Role
(
    customer_id INT,
    role_id     INT,
    PRIMARY KEY (customer_id, role_id),
    FOREIGN KEY (customer_id) REFERENCES Customer (id),
    FOREIGN KEY (role_id) REFERENCES Role (id)
);

-- Tạo bảng Order
CREATE TABLE `Order`
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    date        DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer (id)
);

-- Tạo bảng OrderLine
CREATE TABLE OrderLine
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    order_id       INT,
    product_id     INT,
    amount         INT            NOT NULL,
    purchase_price DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `Order` (id),
    FOREIGN KEY (product_id) REFERENCES Product (id)
);

-- Chèn dữ liệu vào bảng Category
INSERT INTO Category (name)
VALUES ('Điện tử'),
       ('Thời trang'),
       ('Sách');

-- Chèn dữ liệu vào bảng Product
INSERT INTO Product (name, price, in_stock, category_id)
VALUES ('iPhone 15 Pro Max', 29990000, TRUE, (SELECT id FROM Category WHERE name = 'Điện tử')),
       ('MacBook Pro M3', 45990000, TRUE, (SELECT id FROM Category WHERE name = 'Điện tử')),
       ('Áo sơ mi nam', 299000, TRUE, (SELECT id FROM Category WHERE name = 'Thời trang')),
       ('Đắc nhân tâm', 89000, TRUE, (SELECT id FROM Category WHERE name = 'Sách'));

-- Chèn dữ liệu vào bảng Comment
INSERT INTO Comment (text, product_id)
VALUES ('Sản phẩm rất tốt, chất lượng cao!', (SELECT id FROM Product WHERE name = 'iPhone 15 Pro Max')),
       ('Giao hàng nhanh, đóng gói cẩn thận.', (SELECT id FROM Product WHERE name = 'iPhone 15 Pro Max'));

-- Chèn dữ liệu vào bảng Role
INSERT INTO Role (name)
VALUES ('ROLE_CUSTOMER'),
       ('ROLE_ADMIN');

-- Chèn dữ liệu vào bảng Customer
INSERT INTO Customer (name, password, customer_since)
VALUES ('customer', '123', '2023-01-15'),
       ('admin', '123', '2023-03-20'),
       ('Lê Văn C', '123', '2024-06-10');

-- Chèn dữ liệu vào bảng Customer_Role
INSERT INTO Customer_Role (customer_id, role_id)
VALUES ((SELECT id FROM Customer WHERE name = 'customer'), (SELECT id FROM Role WHERE name = 'ROLE_CUSTOMER')),
       ((SELECT id FROM Customer WHERE name = 'admin'), (SELECT id FROM Role WHERE name = 'ROLE_ADMIN')),
       ((SELECT id FROM Customer WHERE name = 'Lê Văn C'), (SELECT id FROM Role WHERE name = 'ROLE_CUSTOMER'));

-- Chèn dữ liệu vào bảng Order
INSERT INTO `Order` (customer_id, date)
VALUES ((SELECT id FROM Customer WHERE name = 'customer'), '2024-10-01'),
       ((SELECT id FROM Customer WHERE name = 'admin'), '2024-10-05');

-- Chèn dữ liệu vào bảng OrderLine
INSERT INTO OrderLine (order_id, product_id, amount, purchase_price)
VALUES ((SELECT id
         FROM `Order`
         WHERE customer_id = (SELECT id FROM Customer WHERE name = 'customer') AND date = '2024-10-01'),
        (SELECT id FROM Product WHERE name = 'iPhone 15 Pro Max'), 1,
        (SELECT price FROM Product WHERE name = 'iPhone 15 Pro Max')),
       ((SELECT id
         FROM `Order`
         WHERE customer_id = (SELECT id FROM Customer WHERE name = 'customer') AND date = '2024-10-01'),
        (SELECT id FROM Product WHERE name = 'Đắc nhân tâm'), 2,
        (SELECT price FROM Product WHERE name = 'Đắc nhân tâm')),
       ((SELECT id
         FROM `Order`
         WHERE customer_id = (SELECT id FROM Customer WHERE name = 'admin') AND date = '2024-10-05'),
        (SELECT id FROM Product WHERE name = 'MacBook Pro M3'), 1,
        (SELECT price FROM Product WHERE name = 'MacBook Pro M3')),
       ((SELECT id
         FROM `Order`
         WHERE customer_id = (SELECT id FROM Customer WHERE name = 'admin') AND date = '2024-10-05'),
        (SELECT id FROM Product WHERE name = 'Áo sơ mi nam'), 3,
        (SELECT price FROM Product WHERE name = 'Áo sơ mi nam'));
