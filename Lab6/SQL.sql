
USE OrderManagement;
GO
CREATE TABLE customers (
    customer_id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) UNIQUE NOT NULL,
    phone NVARCHAR(15),
    address NVARCHAR(255)
);
CREATE TABLE products (
    product_id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL
);
CREATE TABLE orders (
    order_id INT IDENTITY(1,1) PRIMARY KEY,
    customer_id INT NOT NULL,
    order_date DATETIME DEFAULT GETDATE(),
    total DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);
CREATE TABLE order_items (
    order_item_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
-- Chèn dữ liệu vào bảng customers
INSERT INTO customers (name, email, phone, address) VALUES
('Nguyễn Văn A', 'nguyenvana@example.com', '0123456789', 'Hà Nội'),
('Trần Thị B', 'tranthib@example.com', '0987654321', 'Hồ Chí Minh'),
('Lê Văn C', 'levanc@example.com', '0345678901', 'Đà Nẵng');

-- Chèn dữ liệu vào bảng products
INSERT INTO products (name, price, stock_quantity) VALUES
('Bánh Pizza Hải Sản', 250000, 50),
('Bánh Pizza Phô Mai', 220000, 30),
('Bánh Pizza Gà Nướng', 200000, 40);

-- Chèn đơn hàng
INSERT INTO orders (customer_id) VALUES (1);
INSERT INTO orders (customer_id) VALUES (2);

-- Chèn chi tiết đơn hàng
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (1, 1, 2, 500000);
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (1, 3, 1, 200000);
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (2, 2, 3, 660000);
