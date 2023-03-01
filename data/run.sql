/*1. select ingredientID where name is 'burgerPatty'*/
SELECT ingredientID FROM inventory WHERE name = 'burgerPatty';
/*2. select ingredients where name contains 'Patty'*/
SELECT * FROM inventory WHERE (name like '%Patty%');
/*3. select menu items where name contains 'double' or 'combo'*/
SELECT * FROM menuitems WHERE (name like '%double%' OR name like '%combo%');
/*4. select menu items where name contains 'double' and 'black bean'*/
SELECT * FROM menuitems WHERE (name like '%double%' AND name like '%black bean%');
/*5. select all menu items that contains potato as an ingredient*/
SELECT menuitems.name FROM menuitems 
    JOIN itemingredients ON itemingredients.menuitemid=menuitems.menuitemid 
    JOIN inventory ON inventory.ingredientid=itemingredients.ingredientid
    WHERE inventory.name='potato';
/*6. select all menu itmes where it contains potato or american cheese*/
SELECT DISTINCT menuitems.name FROM menuitems 
    JOIN itemingredients ON itemingredients.menuitemid=menuitems.menuitemid 
    JOIN inventory ON inventory.ingredientid=itemingredients.ingredientid
    WHERE inventory.name='potato' OR inventory.name='americanCheese';
/*7. select all combos with drink*/
SELECT DISTINCT menuitems.name FROM menuitems 
    JOIN itemingredients ON itemingredients.menuitemid=menuitems.menuitemid 
    JOIN inventory ON inventory.ingredientid=itemingredients.ingredientid
    WHERE inventory.name='drinkCup' AND menuitems.name like '%combo%';
/*8. select all combos with chicken*/
SELECT DISTINCT menuitems.name FROM menuitems 
    JOIN itemingredients ON itemingredients.menuitemid=menuitems.menuitemid 
    JOIN inventory ON inventory.ingredientid=itemingredients.ingredientid
    WHERE inventory.name like '%chicken%' AND menuitems.name like '%combo%';
/*9. select all ice cream*/
SELECT name FROM menuitems WHERE name like '%ice cream%';
/*10. select menu items with vanila ice cream as an ingredients*/
SELECT DISTINCT menuitems.name FROM menuitems 
    JOIN itemingredients ON itemingredients.menuitemid=menuitems.menuitemid 
    JOIN inventory ON inventory.ingredientid=itemingredients.ingredientid
    WHERE inventory.name='vanillaIceCream';
/*11. select menu items with milk as an ingredients*/
SELECT DISTINCT menuitems.name FROM menuitems 
    JOIN itemingredients ON itemingredients.menuitemid=menuitems.menuitemid 
    JOIN inventory ON inventory.ingredientid=itemingredients.ingredientid
    WHERE inventory.name='milk';
/*12. select anything that does not contain american chesse*/
SELECT DISTINCT menuitems.name FROM menuitems 
    WHERE NOT EXISTS (SELECT * FROM itemingredients
    JOIN inventory ON inventory.ingredientid=itemingredients.ingredientid
    WHERE itemingredients.menuitemid=menuitems.menuitemid AND inventory.name='americanCheese');
/*13. select anything that does not contain american chesse or milk but is a combo */
SELECT DISTINCT menuitems.name FROM menuitems 
    WHERE menuitems.name like '%combo%' 
    AND NOT EXISTS (SELECT * FROM itemingredients
    JOIN inventory ON inventory.ingredientid=itemingredients.ingredientid
    WHERE itemingredients.menuitemid=menuitems.menuitemid AND (inventory.name='americanCheese' OR inventory.name='milk'));
/*14. select items in inventory that need to be restocked*/
SELECT name FROM inventory WHERE curramount < minamount;
/*15. select the top 10 most expensive items in inventory*/
SELECT name FROM inventory ORDER BY curramount * cost DESC LIMIT 10;