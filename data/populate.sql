\copy inventory from 'data/Inventory.csv' CSV HEADER

\copy itemingredients from 'data/ItemIngredients.csv' CSV HEADER

\copy menuitems from 'data/MenuItems.csv' CSV HEADER

\copy orderlineitems from 'data/OrderLineItems.csv' CSV HEADER

\copy orders from 'data/Orders.csv' CSV HEADER

\copy employees from 'data/Employees.csv' CSV HEADER

-- update the orderlineitems table with the correct menuprice values
update orderlineitems oli
set menuprice=mi.menuprice
from menuitems mi
where oli.menuitemid=mi.menuitemid;

-- update the orders table with the correct totalprice values
with ordersummary as (select oli.orderid orderid, round(sum(oli.menuprice)::numeric,2) totalprice
from orderlineitems oli
group by oli.orderid)
update orders o
set totalprice=os.totalprice
from ordersummary os
where os.orderid=o.orderid;

-- Generate respective inventory transaactions for each order
insert into inventorytransactions (orderid,ordertime,ingredientid,qty)
select o.orderid, o.ordertime, ii.ingredientid, sum(ii.qty)
from orders o join orderlineitems oli on (oli.orderid=o.orderid)
join itemingredients ii on (ii.menuitemid=oli.menuitemid)
group by 1,2,3;