create table Products
(
    id           varchar(100)   not null
        primary key,
    product_name varchar(255)   not null,
    category     varchar(100)   not null,
    price        decimal(10, 2) not null,
    quantity     int            not null
);

create table Users
(
    id        int auto_increment
        primary key,
    username  varchar(255) not null,
    password  varchar(255) not null,
    full_name varchar(255) null
);

create table Orders
(
    order_id     int auto_increment
        primary key,
    order_date   date                      not null,
    type         enum ('Import', 'Export') not null,
    total_amount decimal(15, 2)            null,
    user_id      int                       not null,
    note         varchar(255)              null,
    constraint orders_ibfk_1
        foreign key (user_id) references Users (id)
);

create table OrderDetails
(
    id         int auto_increment
        primary key,
    order_id   int            not null,
    product_id varchar(100)   null,
    quantity   int            not null,
    price      decimal(10, 2) not null,
    constraint orderdetails_ibfk_1
        foreign key (order_id) references Orders (order_id)
);

create index order_id
    on OrderDetails (order_id);

create index product_id
    on OrderDetails (product_id);

create definer = root@`%` trigger trg_after_orderdetails_insert
    after insert
    on OrderDetails
    for each row
BEGIN
    DECLARE orderType ENUM('Import', 'Export');

SELECT type INTO orderType
FROM Orders
WHERE order_id = NEW.order_id;

IF orderType = 'Export' THEN
UPDATE Products
SET quantity = quantity - NEW.quantity
WHERE id = NEW.product_id;
END IF;

END;

create definer = root@`%` trigger trg_after_orderdetails_insert_import
    after insert
    on OrderDetails
    for each row
BEGIN
    DECLARE orderType ENUM('Import', 'Export');

SELECT type INTO orderType
FROM Orders
WHERE order_id = NEW.order_id;

IF orderType = 'Import' THEN
UPDATE Products
SET quantity = quantity + NEW.quantity
WHERE id = NEW.product_id;
END IF;

END;

create definer = root@`%` trigger update_total_amount_after_insert
    after insert
    on OrderDetails
    for each row
BEGIN
    DECLARE total DECIMAL(15, 2);

SELECT SUM(quantity * price)
INTO total
FROM OrderDetails
WHERE order_id = NEW.order_id;

UPDATE Orders
SET total_amount = total
WHERE order_id = NEW.order_id;
END;

create index user_id
    on Orders (user_id);

