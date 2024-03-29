import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 
 * The jdbcpostgreSQL class represents a connection to a PostgreSQL database
 * using JDBC.
 * This class provides an instance variable conn that holds the database
 * connection object.
 * Once you have an instance of this class, you can use the various methods of
 * the JDBC API
 */
public class jdbcpostgreSQL {
    /**
     * The conn instance variable holds the database connection object.
     */
    Connection conn;

    /**
     * Constructs a new jdbcpostgreSQL object and sets the connection to the
     * PostgreSQL database.
     */
    public jdbcpostgreSQL() {
        conn = new dbSetup().getConnection();
    }

    /**
     * Closes the connection to the database.
     */
    public void close() {
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch (Exception e) {
            System.out.println("Connection NOT Closed.");
        } // end try catch
    }

    /**
     * Rounds a given float number to the specified number of decimal places.
     * 
     * @param d            the float number to be rounded
     * @param decimalPlace the number of decimal places to round to
     * @return the rounded float number
     */
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    /*
     * Returns (current maximum Order ID + 1) as the
     * the new Order ID.
     *
     * @param conn is the session instance of java to database
     *
     * @returns newOrderID
     */
    public int getNewOrderId() {
        // Get the maximum order_id + 1 to be the new order it
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "select max(orderid) from orders;";
            ResultSet result = stmt.executeQuery(sqlStatement);
            result.next();
            int newOrderId = result.getInt(1) + 1;
            return newOrderId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /*
     * Returns (current maximum Line Item ID + 1) as the
     * the new Line Item ID.
     *
     * @param conn is the session instance of java to database
     *
     * @returns newLineItemId
     */
    public int getNewLineItemId() {
        // Get the maximum order_id + 1 to be the new order it
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "select max(lineitemid) from orderlineitems;";
            ResultSet result = stmt.executeQuery(sqlStatement);
            result.next();
            int newOrderId = result.getInt(1) + 1;
            return newOrderId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /*
     * Updates both orders tables by grabbing information from database,
     * calculating the price, inserting into orderLineItem, executing the
     * sqlStatement and then insert into orderTable.
     *
     * @param itemIDs is a vector of all itemID's
     *
     * @param employeeID is the current employee accessing the system
     *
     * @param newOrderId is the order id to be pushed/updated
     *
     * @returns void
     */
    public void updateOrdersAndOrderLineItemsTable(Vector<Integer> itemIDs, int employeeID, int newOrderId) {
        try {
            Statement stmt = conn.createStatement();
            // Get the time
            LocalDateTime orderTime = LocalDateTime.now();

            // Calculate the price & update orderlineitems table
            int newLineItemId = getNewLineItemId();
            float totalPrice = 0;
            for (int i = 0; i < itemIDs.size(); i++) {
                // Get menu item information
                int itemID = itemIDs.elementAt(i);
                String sqlStatement = "select name, menuPrice from MenuItems where menuItemID="
                        + Integer.toString(itemID);
                ResultSet result = stmt.executeQuery(sqlStatement);
                result.next();
                String name = result.getString("name");
                Float itemPrice = round(result.getFloat(2), 2);

                // Calculate the price
                totalPrice += result.getFloat(2);
                System.out.println("Item " + name + " has price of " + Float.toString(itemPrice));

                System.out.println("OrderLineItems summary:");
                System.out.println("Line item id: " + Integer.toString(newLineItemId));
                System.out.println("Item id: " + Integer.toString(itemID));
                System.out.println("Item price: " + Float.toString(itemPrice));
                System.out.println("Order id: " + Integer.toString(newOrderId));

                // Insert into orderlineitem
                sqlStatement = String.format(
                        "insert into orderlineitems values (%s, %s, %s, %s)",
                        Integer.toString(newLineItemId),
                        Integer.toString(itemID),
                        Float.toString(itemPrice),
                        Integer.toString(newOrderId));
                System.out.println(sqlStatement);
                System.out.println();

                // Execute and update accordingly
                stmt.executeUpdate(sqlStatement);
                newLineItemId += 1;
            }

            // Round total price to 2 decimal places;
            totalPrice = round(totalPrice, 2);

            System.out.println("Order summary:");
            System.out.println("Order id: " + Integer.toString(newOrderId));
            System.out.println("Total price: " + Float.toString(totalPrice));
            System.out.println("Date time: " + orderTime.toString());

            // Create DateTimeFormatter instance with specified format
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            // Format LocalDateTime to String
            String formattedDateTime = orderTime.format(dateTimeFormatter);
            // Generate sql statement to insert into order
            String sqlStatement = String.format(
                    "insert into orders values (%s, '%s', %s, %s)",
                    Integer.toString(newOrderId),
                    formattedDateTime,
                    Float.toString(totalPrice),
                    Integer.toString(employeeID));
            System.out.println(sqlStatement);
            System.out.println();
            stmt.executeUpdate(sqlStatement);
            System.out.println("Insert into order table successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Updates both inventorytransactions and inventory tables by grabbing
     * information from database, and executing the sqlStatement and then
     * inserting entries into inventorytransactions table and updating the
     * quantities of reac respective ingredient for each transaction.
     *
     * @param newOrderId is the orderid of the newly created order
     *
     * @returns void
     *
     * @throws exception if function fails to create the sql statement to be
     * inserted into order
     */
    public void updateInventoryTransactionsAndInventoryTable(int newOrderId) {
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "insert into inventorytransactions (orderid,ordertime,ingredientid,qty) select o.orderid, o.ordertime, ii.ingredientid, sum(ii.qty) from orders o join orderlineitems oli on (oli.orderid=o.orderid) and (o.orderid="
                    + Integer.toString(newOrderId)
                    + ") join itemingredients ii on (ii.menuitemid=oli.menuitemid) group by 1,2,3;";
            System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);

            sqlStatement = "select * from inventorytransactions where orderid=" + Integer.toString(newOrderId) + ";";
            System.out.println(sqlStatement);
            Vector<String> ingredients = new Vector<String>();
            Vector<String> qty = new Vector<String>();
            ResultSet r = stmt.executeQuery(sqlStatement);

            while (r.next()) {
                ingredients.add(r.getString(4));
                qty.add(r.getString(5));
                System.out.println("Ingredient ID: " + r.getString(4) + ", qty: " + r.getString(5));
            }

            for (int i = 0; i < ingredients.size(); i++) {
                int ingredientID = Integer.valueOf(ingredients.elementAt(i));
                int ingredientQty = Integer.valueOf(qty.elementAt(i));
                subtractInventory(ingredientID, ingredientQty);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Updates inventory table; subtracts qty from currAmt of the ingredient
     * associated with the given ingredientid
     *
     * @param ingredientID is the ingredientid of the ingreditent to be reduced
     *
     * @param qty is the amount to reduce the ingredient currAmt by
     *
     * @returns void
     */
    public void subtractInventory(int ingredientID, int qty) {
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "update inventory set curramount = curramount-" + Integer.toString(qty)
                    + " where ingredientid=" + Integer.toString(ingredientID) + ";";
            System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Updates inventory table; restocks qty from currAmt of the ingredient
     * associated with the given ingredientid
     *
     * @param ingredientID is the ingredientid of the ingreditent to be reduced
     *
     * @param qty is the amount to increase the ingredient currAmt by
     *
     * @returns void
     */
    public void addInventory(int ingredientID, int qty) {
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "update inventory set curramount = curramount+" + Integer.toString(qty)
                    + " where ingredientid=" + Integer.toString(ingredientID) + ";";
            System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the minimum amount of a given ingredient in the inventory.
     * 
     * @param ingredientID the ID of the ingredient to be updated
     * @param minAmt       the new minimum amount for the ingredient
     */
    public void updateIngredientMinAmt(int ingredientID, int minAmt) {
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "update inventory set minamount = " + Integer.toString(minAmt)
                    + " where ingredientid=" + Integer.toString(ingredientID) + ";";
            System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Checks to see if the given menuitemid is already used in the
     *
     * @param id is the menuitemid of the to be validated
     *
     * @returns boolean; true if the id is unused in teh menuitems table, flase if
     * it is used in already
     */
    public boolean isMenuIdValid(int id) {
        try {
            Statement stmt = conn.createStatement();

            String sqlStatement = "select name from MenuItems where menuItemID=" + Integer.toString(id);
            ResultSet result = stmt.executeQuery(sqlStatement);
            if (result.next()) {
                System.out.println(result.getString(1) + " already has an id of " + id);
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("MenuValid -- " + e.getStackTrace() + e.getMessage());
        }
        return false;
    }

    /*
     * Updates menuitem table; adds menu item into menuitems table with passed in
     * values
     *
     * @param id is the new item's menuitemid
     *
     * @param name is the name of the new item
     *
     * @param price is the menuprice of the new item
     *
     * @param classId is the classid of the new item; determines which tab in the
     * server view the item will be displayed
     *
     * @returns void
     */
    public void addMenuItem(int id, String name, float price, int classId) {
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = String.format(
                    "insert into menuitems values (%s, '%s', %s, %s)",
                    Integer.toString(id),
                    name,
                    Float.toString(price),
                    Integer.toString(classId));
            System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("MenuAdd -- " + e.getMessage());
        }
    }

    /**
     * Updates the price of a given menu item.
     * 
     * @param itemID the ID of the menu item to be updated
     * @param price  the new price for the menu item
     */
    public void updateMenuItemPrice(int itemID, float price) {
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "update menuitems set menuprice = " + Float.toString(price)
                    + " where menuitemid=" + Integer.toString(itemID) + ";";
            System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Calculates the total price of a given order based on the menu items selected.
     * 
     * @param itemIDs a vector of menu item IDs included in the order
     * 
     * @return the total price of the order
     */
    public float getOrderTotal(Vector<Integer> itemIDs) {
        try {
            Statement stmt = conn.createStatement();
            float totalPrice = 0;
            for (int i = 0; i < itemIDs.size(); i++) {
                // Get menu item information
                int itemID = itemIDs.elementAt(i);
                String sqlStatement = "select name, menuPrice from MenuItems where menuItemID="
                        + Integer.toString(itemID);
                ResultSet result = stmt.executeQuery(sqlStatement);
                result.next();
                Float itemPrice = round(result.getFloat(2), 2);

                // Calculate the price
                totalPrice += itemPrice;
            }
            // Round total price to 2 decimal places;
            return round(totalPrice, 2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /*
     * Accesses the menuitems table to get the given menuitemid's name
     *
     * @param itemID is the menuitemid of the to name to be returned
     *
     * @returns String of the name of the menuitem
     */
    public String getItemName(int itemID) {
        try {
            Statement stmt = conn.createStatement();

            // Get menu item name
            String sqlStatement = "select name from MenuItems where menuItemID=" + Integer.toString(itemID);
            ResultSet result = stmt.executeQuery(sqlStatement);
            result.next();
            String name = result.getString(1);

            return name;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    /*
     * Retrieves the data in the inventory table as a ResultSet
     *
     * @returns ResultSet of the sql query that retreives the inventory table data
     */
    public ResultSet getInventory() {
        ResultSet r = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "select * from inventory order by ingredientid";
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    /*
     * Retrieves the data in of which ingredients have a currAmt not less than its
     * minAmt as a ResultSet
     *
     * @returns ResultSet of the sql query that retreives the which ingredients need
     * to be restocked
     */
    public ResultSet getRestockReport() {
        ResultSet r = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * from inventory WHERE CURRAMOUNT <= MINAMOUNT";
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return r;
    }

    /**
     * Retrieves a result set containing the excess report for a specified date.
     * 
     * @param date the date to retrieve the excess report for
     * @return a result set containing the excess report for the specified date
     */
    public ResultSet getExcessReport(String date) {
        ResultSet r = null;
        try {
            // Get the time
            LocalDateTime dateTimeRightNow = LocalDateTime.now();
            // Create DateTimeFormatter instance with specified format
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            // Format LocalDateTime to String
            String formattedDateTime = dateTimeRightNow.format(dateTimeFormatter);

            Statement stmt = conn.createStatement();
            String sqlStatement = "select distinct inventory.ingredientID, inventory.name, inventory.currAmount, inventory.unit, inventory.minAmount, inventory.cost from inventory join inventorytransactions on inventory.ingredientid = inventorytransactions.ingredientid where inventorytransactions.ordertime between '"
                    + date + "' AND '" + formattedDateTime + "' and inventory.curramount > inventory.minamount * 0.9;";
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return r;
    }

    /**
     * Retrieves a result set containing all menu items.
     * 
     * @return a result set containing all menu items
     */
    public ResultSet getMenu() {
        ResultSet r = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * from menuitems order by menuitemid";
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return r;
    }

    /**
     * Retrieves a result set containing all menu items belonging to a specific
     * class ID.
     * 
     * @param classID the ID of the class to retrieve menu items for
     * @return a result set containing all menu items belonging to the specified
     *         class ID
     */
    public ResultSet getClassItems(int classID) {
        ResultSet r = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * from menuitems WHERE classid=" + classID;
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    /**
     * Deletes a menu item with a specified ID.
     * 
     * @param id the ID of the menu item to delete
     */
    public void deleteMenuItem(int id) {
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "DELETE from menuitems WHERE menuitemid=" + id;
            System.out.println(sqlStatement);
            stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Given a time window, display the sales and quantities orderered by item from
     * the order history.
     *
     * @param startTime The start time of the time window
     * 
     * @param endTime The end time of the time window
     *
     * @return A ResultSet containing the sales report
     */
    public ResultSet getSalesReport(Date startTime, Date endTime) {
        ResultSet r = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT menuitems.name as \"Menu Item\", COUNT(1) as \"Quantity Sold\" , ROUND(CAST(SUM(orderlineitems.menuprice) AS numeric), 2) as \"Sales\"\n"
                    + "FROM orderlineitems \n"
                    + "JOIN menuitems ON orderlineitems.menuitemID = menuitems.menuItemID JOIN orders ON orderlineitems.orderID = orders.orderID \n"
                    + "WHERE orders.ordertime BETWEEN '" + startTime + "' AND '" + endTime
                    + "' GROUP BY menuitems.name";
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    /*
     * Given a time window, display a list of pairs of menu
     * items that sell together often, popular or not, sorted by most frequent.
     *
     * @param startTime The start time of the time window
     * 
     * @param endTime The end time of the time window
     *
     * @return A ResultSet containing the sales report
     */
    public ResultSet generateFrequentSalesReport(String startTime, String endTime) {
        ResultSet r = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "select t1.firstitem, t1.seconditem, t1.concat as menuitemstogether, COUNT(t1.concat) from (SELECT a.menuitemid as firstitem, b.menuitemid as seconditem, a.orderid, CONCAT(a.menuitemid, ' ', b.menuitemid) FROM orderlineitems a JOIN orderlineitems b  ON a.orderid = b.orderid and a.menuitemid < b.menuitemid) t1 INNER JOIN (select orders.ordertime, orders.orderid from orders where orders.ordertime between '"
                    + startTime + "' AND '" + endTime
                    + "' group by orders.orderid) t2 ON t1.orderid = t2.orderid GROUP BY t1.firstitem, t1.seconditem, t1.concat ORDER BY COUNT(t1.concat) DESC;";
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    /**
     * Retrieves a result set containing the X report for the most recent day.
     * 
     * @return a result set containing the X report for the most recent day
     */
    public ResultSet getXReport() {
        ResultSet r = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * FROM orders WHERE date_trunc('day', ordertime) = (SELECT max(date_trunc('day', ordertime)) FROM orders)";
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }
} // end Class
