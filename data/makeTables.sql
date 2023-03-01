
CREATE TABLE Inventory (
    ingredientID int PRIMARY KEY,
    name text,
    currAmount float,
    unit text,
    minAmount float,
    cost float
);

CREATE TABLE ItemIngredients (
    id int PRIMARY KEY,
    menuItemID int,
    ingredientID int,
    qty int
);

CREATE TABLE MenuItems (
    menuItemID int PRIMARY KEY,
    name text, 
    menuPrice float,
    classID int
);

CREATE TABLE OrderLineItems (
    lineItemID int PRIMARY KEY,
    menuItemID int,
    menuPrice float,
    orderID int
);

CREATE TABLE Orders (
    orderID int PRIMARY KEY,
    orderTime timestamp, 
    totalPrice float,
    employeeID int
);*/

CREATE TABLE InventoryTransactions (
    transactionID serial PRIMARY KEY,
    orderID int, 
    orderTime timestamp,
    ingredientID int,
    qty int
);

CREATE TABLE Employees (
    employeeID int PRIMARY KEY,
    name text, 
    title int
);