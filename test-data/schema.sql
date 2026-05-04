DROP TABLE IF EXISTS information;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS orders_product;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS product_type;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    email VARCHAR(128),
    phone VARCHAR(32),
    status VARCHAR(1) DEFAULT '1',
    role INT NOT NULL,
    avatar VARCHAR(255)
);

CREATE TABLE product_type (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(64) NOT NULL,
    description VARCHAR(255),
    status INT DEFAULT 1
);

CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_type_id INT NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(255),
    price INT NOT NULL,
    picture_url VARCHAR(255),
    status INT DEFAULT 1,
    INDEX idx_product_type_id (product_type_id)
);

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    status INT DEFAULT 0,
    reviewed INT DEFAULT 0,
    createTime DATETIME NULL,
    updateTime DATETIME NULL,
    INDEX idx_orders_user_id (user_id)
);

CREATE TABLE orders_product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    order_id INT NOT NULL,
    count INT NOT NULL,
    status INT DEFAULT 0,
    comment VARCHAR(255),
    INDEX idx_orders_product_order_id (order_id),
    INDEX idx_orders_product_product_id (product_id)
);

CREATE TABLE message (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(128) NOT NULL,
    content VARCHAR(500),
    create_time DATETIME NULL,
    update_time DATETIME NULL,
    status INT DEFAULT 0,
    INDEX idx_message_user_id (user_id)
);

CREATE TABLE information (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(128) NOT NULL,
    content VARCHAR(500),
    reply VARCHAR(500),
    create_time DATETIME NULL,
    update_time DATETIME NULL,
    status INT DEFAULT 0,
    INDEX idx_information_user_id (user_id)
);
