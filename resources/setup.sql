DROP DATABASE IF EXISTS shop;

CREATE DATABASE shop;
USE shop;

CREATE TABLE employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employeeId INT NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    wholesalerPrice DOUBLE NOT NULL,
    available BOOLEAN NOT NULL DEFAULT 1,
    stock INT NOT NULL
);

CREATE TABLE historical_inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_product INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    wholesalerPrice DOUBLE NOT NULL,
    available BOOLEAN NOT NULL,
    stock INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO employee (employeeId, name, password) VALUES 
(123, 'Santi', 'test1'),
(456, 'Jose', 'test2'),
(789, 'Vicente', 'test3');

INSERT INTO inventory (name, wholesalerPrice, available, stock) VALUES
('Manzana', 10.00, 1, 10),
('Fresa',    5.00,  1, 20);