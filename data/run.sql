/*CREATE TABLE Inventory (
    name text PRIMARY KEY,
    currAmount float,
    unit text,
    minAmount float,
    cost float
);

CREATE TABLE ItemIngredients (
    itemID int PRIMARY KEY,
    name text, 
    ingredients text[]
);

CREATE TABLE MenuItems (
    itemID int PRIMARY KEY,
    name text, 
    ingredientsList int,
    menuPrice float,
    classID int
);

CREATE TABLE Employees (
    employeeID int PRIMARY KEY,
    name text, 
    title int
);*/

/*INSERT INTO itemingredients (itemid, name, ingredients) values (0, 'hamburger', ARRAY ['bun', 'burgerPatty']);
INSERT INTO itemingredients (itemid, name, ingredients) values (1, 'cheeseburger', ARRAY ['bun', 'burgerPatty', 'americanCheese']);
INSERT INTO itemingredients (itemid, name, ingredients) values (2, 'black bean hamburger', ARRAY ['bun', 'beanBurgerPatty']);
INSERT INTO itemingredients (itemid, name, ingredients) values (3, 'black bean cheeseburger', ARRAY ['bun', 'beanBurgerPatty', 'americanCheese']);
INSERT INTO itemingredients (itemid, name, ingredients) values (4, 'hamburger w/ bacon', ARRAY ['bun', 'burgerPatty', 'bacon', 'bacon']);
INSERT INTO itemingredients (itemid, name, ingredients) values (5, 'cheeseburger w/ bacon', ARRAY ['bun', 'burgerPatty', 'americanCheese', 'bacon', 'bacon']);
INSERT INTO itemingredients (itemid, name, ingredients) values (6, 'black bean hamburger w/ bacon', ARRAY ['bun', 'beanBurgerPatty', 'bacon', 'bacon']);
INSERT INTO itemingredients (itemid, name, ingredients) values (7, 'black bean cheeseburger w/ bacon', ARRAY ['bun', 'beanBurgerPatty', 'americanCheese', 'bacon', 'bacon']);
INSERT INTO itemingredients (itemid, name, ingredients) values (8, 'double hamburger', ARRAY ['bun', 'burgerPatty', 'burgerPatty']);
INSERT INTO itemingredients (itemid, name, ingredients) values (9, 'double cheeseburger', ARRAY ['bun', 'burgerPatty', 'burgerPatty', 'americanCheese', 'americanCheese']);
INSERT INTO itemingredients (itemid, name, ingredients) values (10, 'double black bean hamburger', ARRAY ['bun', 'beanBurgerPatty', 'beanBurgerPatty']);
INSERT INTO itemingredients (itemid, name, ingredients) values (11, 'double black bean cheeseburger', ARRAY ['bun', 'beanBurgerPatty', 'beanBurgerPatty', 'americanCheese', 'americanCheese']);
INSERT INTO itemingredients (itemid, name, ingredients) values (12, 'double hamburger w/ bacon', ARRAY ['bun', 'burgerPatty', 'burgerPatty', 'bacon', 'bacon']);
INSERT INTO itemingredients (itemid, name, ingredients) values (13, 'double cheeseburger w/ bacon', ARRAY ['bun', 'burgerPatty', 'burgerPatty', 'americanCheese', 'americanCheese', 'bacon', 'bacon']);
INSERT INTO itemingredients (itemid, name, ingredients) values (14, 'double black bean hamburger w/ bacon', ARRAY ['bun', 'beanBurgerPatty', 'beanBurgerPatty', 'bacon', 'bacon']);
INSERT INTO itemingredients (itemid, name, ingredients) values (15, 'double black bean cheeseburger w/ bacon', ARRAY ['bun', 'beanBurgerPatty', 'beanBurgerPatty', 'americanCheese', 'americanCheese', 'bacon', 'bacon']);
INSERT INTO itemingredients (itemid, name, ingredients) values (16, 'gig em patty melt', ARRAY ['breadSlice', 'breadSlice', 'burgerPatty', 'americanCheese', 'choppedOnion']);
INSERT INTO itemingredients (itemid, name, ingredients) values (17, 'grilled chicken sandwich', ARRAY ['bun', 'chickenPatty']);
INSERT INTO itemingredients (itemid, name, ingredients) values (18, 'spicy grilled chicken sandwich', ARRAY ['bun', 'spicyChickenPatty']);
INSERT INTO itemingredients (itemid, name, ingredients) values (19, 'aggie chicken club', ARRAY ['bun', 'chickenPatty', 'americanCheese', 'avocado', 'bacon', 'bacon']);
INSERT INTO itemingredients (itemid, name, ingredients) values (20, 'chicken philly cheese steak', ARRAY ['subRoll', 'choppedChicken', 'greenPepper', 'redPeper', 'choppedOnion', 'provoloneCheese']);
INSERT INTO itemingredients (itemid, name, ingredients) values (21, 'chicken caesar salad', ARRAY ['salad']);
INSERT INTO itemingredients (itemid, name, ingredients) values (22, 'fries', ARRAY ['potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (23, 'fountain drink', ARRAY ['drinkCup']);
INSERT INTO itemingredients (itemid, name, ingredients) values (24, 'bottled water', ARRAY ['bottledWater']);
INSERT INTO itemingredients (itemid, name, ingredients) values (25, 'vanilla ice cream', ARRAY ['iceCreamCup', 'vanillaIceCream', 'vanillaIceCream']);
INSERT INTO itemingredients (itemid, name, ingredients) values (26, 'chocolate ice cream', ARRAY ['iceCreamCup', 'chocolateIceCream', 'chocolateIceCream']);
INSERT INTO itemingredients (itemid, name, ingredients) values (27, 'strawberry ice cream', ARRAY ['iceCreamCup', 'strawberryIceCream', 'strawberryIceCream']);
INSERT INTO itemingredients (itemid, name, ingredients) values (28, 'oreo cookie ice cream', ARRAY ['iceCreamCup', 'oreoIceCream', 'oreoIceCream']);
INSERT INTO itemingredients (itemid, name, ingredients) values (29, 'vanilla shake', ARRAY ['shakeCup', 'shakeLid', 'vanillaIceCream', 'vanillaIceCream', 'milk']);
INSERT INTO itemingredients (itemid, name, ingredients) values (30, 'chocolate shake', ARRAY ['shakeCup', 'shakeLid', 'chocolateIceCream', 'chocolateIceCream', 'milk']);
INSERT INTO itemingredients (itemid, name, ingredients) values (31, 'strawberry shake', ARRAY ['shakeCup', 'shakeLid', 'strawberryIceCream', 'strawberryIceCream', 'milk']);
INSERT INTO itemingredients (itemid, name, ingredients) values (32, 'oreo cookie shake', ARRAY ['shakeCup', 'shakeLid', 'oreoIceCream', 'oreoIceCream', 'milk']);
INSERT INTO itemingredients (itemid, name, ingredients) values (33, 'rootbeer float', ARRAY ['shakeCup', 'shakeLid', 'rootBeer', 'vanillaIceCream']);
INSERT INTO itemingredients (itemid, name, ingredients) values (34, 'chocolate chip cookie sunday', ARRAY ['chocolateChipCookie', 'chocolateChipCookie', 'vanillaIceCream']);
INSERT INTO itemingredients (itemid, name, ingredients) values (35, 'sugar cookie sunday', ARRAY ['sugarCookie', 'sugarCookie', 'vanillaIceCream']);
INSERT INTO itemingredients (itemid, name, ingredients) values (36, 'three tender basket', ARRAY ['chickenFinger', 'chickenFinger', 'chickenFinger', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (37, 'hamburger combo', ARRAY ['bun', 'burgerPatty', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (38, 'cheeseburger combo', ARRAY ['bun', 'burgerPatty', 'americanCheese', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (39, 'black bean hamburger combo', ARRAY ['bun', 'beanBurgerPatty', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (40, 'black bean cheeseburger combo', ARRAY ['bun', 'beanBurgerPatty', 'americanCheese', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (41, 'hamburger w/ bacon combo', ARRAY ['bun', 'burgerPatty', 'bacon', 'bacon', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (42, 'cheeseburger w/ bacon combo', ARRAY ['bun', 'burgerPatty', 'americanCheese', 'bacon', 'bacon', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (43, 'black bean hamburger w/ bacon combo', ARRAY ['bun', 'beanBurgerPatty', 'bacon', 'bacon', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (44, 'black bean cheeseburger w/ bacon combo', ARRAY ['bun', 'beanBurgerPatty', 'americanCheese', 'bacon', 'bacon', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (45, 'double hamburger combo', ARRAY ['bun', 'burgerPatty', 'burgerPatty', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (46, 'double cheeseburger combo', ARRAY ['bun', 'burgerPatty', 'burgerPatty', 'americanCheese', 'americanCheese', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (47, 'double black bean hamburger combo', ARRAY ['bun', 'beanBurgerPatty', 'beanBurgerPatty', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (48, 'double black bean cheeseburger combo', ARRAY ['bun', 'beanBurgerPatty', 'beanBurgerPatty', 'americanCheese', 'americanCheese', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (49, 'double hamburger w/ bacon combo', ARRAY ['bun', 'burgerPatty', 'burgerPatty', 'bacon', 'bacon', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (50, 'double cheeseburger w/ bacon combo', ARRAY ['bun', 'burgerPatty', 'burgerPatty', 'americanCheese', 'americanCheese', 'bacon', 'bacon', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (51, 'double black bean hamburger w/ bacon combo', ARRAY ['bun', 'beanBurgerPatty', 'beanBurgerPatty', 'bacon', 'bacon', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (52, 'double black bean cheeseburger w/ bacon combo', ARRAY ['bun', 'beanBurgerPatty', 'beanBurgerPatty', 'americanCheese', 'americanCheese', 'bacon', 'bacon', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (53, 'gig em patty melt combo', ARRAY ['breadSlice', 'breadSlice', 'burgerPatty', 'americanCheese', 'choppedOnion', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (54, 'grilled chicken sandwich combo', ARRAY ['bun', 'chickenPatty', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (55, 'spicy grilled chicken sandwich combo', ARRAY ['bun', 'spicyChickenPatty', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (56, 'aggie chicken club combo', ARRAY ['bun', 'chickenPatty', 'americanCheese', 'avocado', 'bacon', 'bacon', 'drinkCup', 'potato', 'potato', 'potato']);
INSERT INTO itemingredients (itemid, name, ingredients) values (57, 'chicken philly cheese steak combo', ARRAY ['subRoll', 'choppedChicken', 'greenPepper', 'redPeper', 'choppedOnion', 'provoloneCheese', 'drinkCup', 'potato', 'potato', 'potato']);*/

/*1. select ingredients where name is hamburger*/
SELECT ingredients FROM itemingredients WHERE name = 'hamburger';
/*2. select ingredients where name contains hamburger*/
SELECT * FROM itemingredients WHERE (name like '%hamburger%');
/*3. select ingredients where name contains double or combo*/
SELECT * FROM itemingredients WHERE (name like '%double%' OR name like '%combo%');
/*4. select ingredients where name contains double and black bean*/
SELECT * FROM itemingredients WHERE (name like '%double%' AND name like '%black bean%');
/*5. select all item ingredients where it contains potato*/
SELECT name FROM itemingredients WHERE 'potato'=ANY(ingredients);
/*6. select all item ingredients where it contains potato and american cheese*/
SELECT name FROM itemingredients WHERE 'potato'=ANY(ingredients) AND 'americanCheese'=ANY(ingredients);
/*7. select all combos with drink*/
SELECT name FROM itemingredients WHERE (name like '%combo%') AND 'drinkCup'=ANY(ingredients);
/*8. select all combos with chicken*/
SELECT name FROM itemingredients WHERE (name like '%combo%') AND 'chickenPatty'=ANY(ingredients);
/*9. select all ice cream*/
SELECT name FROM itemingredients WHERE (name like '%ice cream%');
/*10. select everything with vanila ice cream as an ingredients*/
SELECT name FROM itemingredients WHERE 'vanillaIceCream'=ANY(ingredients);
/*11. select everything with milk as an ingredients*/
SELECT name FROM itemingredients WHERE 'milk'=ANY(ingredients);
/*12. select anything that not contain american chesse*/
SELECT name FROM itemingredients WHERE NOT 'americanCheese'=ANY(ingredients);
/*13. select anything that not contain american chesse and milk but is a combo */
SELECT name FROM itemingredients WHERE NOT 'americanCheese'=ANY(ingredients) AND NOT 'milk'=ANY(ingredients) AND (name like '%combo%');
/*14. select item in inventory that need to be stocked*/
SELECT name FROM inventory WHERE curramount < minamount;
/*15. select the top 10 expensive item in inventory*/
SELECT name FROM inventory ORDER BY curramount * cost DESC LIMIT 10;