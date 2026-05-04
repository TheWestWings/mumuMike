INSERT INTO user (id, username, password, email, phone, status, role, avatar) VALUES
(1, 'admin_auto', '$2a$10$qxBq09.WFq57LlQuM6KctumGJE6ymW/vWtlZSvXU3cGTjb4g7278G', 'admin@example.com', '13800000001', '1', 0, NULL),
(2, 'merchant_auto', '$2a$10$qxBq09.WFq57LlQuM6KctumGJE6ymW/vWtlZSvXU3cGTjb4g7278G', 'merchant@example.com', '13800000002', '1', 1, NULL),
(3, 'customer_auto', '$2a$10$qxBq09.WFq57LlQuM6KctumGJE6ymW/vWtlZSvXU3cGTjb4g7278G', 'customer@example.com', '13800000003', '1', 2, NULL),
(4, 'disabled_auto', '$2a$10$qxBq09.WFq57LlQuM6KctumGJE6ymW/vWtlZSvXU3cGTjb4g7278G', 'disabled@example.com', '13800000004', '0', 2, NULL);

INSERT INTO product_type (id, title, description, status) VALUES
(1, '自动化测试启用分类', 'API regression enabled type', 1),
(2, '自动化测试停用分类', 'API regression disabled type', 0);

INSERT INTO product (id, product_type_id, name, description, price, picture_url, status) VALUES
(1, 1, '自动化上架奶茶', 'Visible in menu regression', 1200, '/uploads/images/test-visible.png', 1),
(2, 1, '自动化下架奶茶', 'Hidden from menu regression', 1500, '/uploads/images/test-hidden.png', 0),
(3, 2, '停用分类商品', 'Hidden because type disabled', 1800, '/uploads/images/test-type-hidden.png', 1);

ALTER TABLE user AUTO_INCREMENT = 100;
ALTER TABLE product_type AUTO_INCREMENT = 100;
ALTER TABLE product AUTO_INCREMENT = 100;
ALTER TABLE orders AUTO_INCREMENT = 100;
ALTER TABLE orders_product AUTO_INCREMENT = 100;
ALTER TABLE message AUTO_INCREMENT = 100;
ALTER TABLE information AUTO_INCREMENT = 100;
