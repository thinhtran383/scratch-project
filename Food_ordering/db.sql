drop database food_ordering;

create database food_ordering;

use food_ordering;

CREATE TABLE
    Customers (
        CustomerID INT AUTO_INCREMENT PRIMARY KEY,
        Phone VARCHAR(20) NOT NULL,
        Name VARCHAR(100) NOT NULL
    );

CREATE TABLE
    MenuItems (
        MenuItemID INT AUTO_INCREMENT PRIMARY KEY,
        Name VARCHAR(100) NOT NULL,
        Price DECIMAL(10, 2) NOT NULL,
        ImageUrl varchar(255) default null
    );

Create table
    Tables(
        TableID int auto_increment primary key,
        Status enum(
            'Available',
            'NotAvailable',
            'Booked'
        ) not null
    );

CREATE TABLE
    Reservations (
        ReservationID INT AUTO_INCREMENT PRIMARY KEY,
        CustomerID INT,
        TableID INT,
        ReservationTime DATETIME NOT NULL,
        Status ENUM(
            'Accepted',
            'Rejected',
            'Pending'
        ) NOT NULL,
        FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),
        Foreign key (TableID) References Tables(TableID)
    );

CREATE TABLE
    Orders (
        OrderID INT AUTO_INCREMENT PRIMARY KEY,
        ReservationID INT,
        MenuItemID INT,
        Quantity INT NOT NULL,
        FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID),
        FOREIGN KEY (MenuItemID) REFERENCES MenuItems(MenuItemID)
    );

CREATE TABLE
    Payments (
        PaymentID INT AUTO_INCREMENT PRIMARY KEY,
        ReservationID INT,
        Amount DECIMAL(10, 2) NOT NULL,
        FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID)
    );

CREATE TABLE
    Employees (
        EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
        Username VARCHAR(50) NOT NULL,
        Password VARCHAR(100) NOT NULL,
        Role ENUM('Employee', 'Admin') NOT NULL
    );

CREATE TABLE
    Revenues (
        RevenueID INT AUTO_INCREMENT PRIMARY KEY,
        Date DATE NOT NULL,
        Amount DECIMAL(10, 2) NOT NULL
    );
    


    

    
    