
// Java Backend postgress
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Command to run on Mac:
// cd src/main
// javac *.java
// java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL
public class jdbcpostgreSQL {
    Connection conn;

    public jdbcpostgreSQL() {
        conn = new dbSetup().getConnection();
    }

    public void close() {
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch (Exception e) {
            System.out.println("Connection NOT Closed.");
        } // end try catch
    }

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
     *
     * @throws exception if return of newOrderId fails
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
     *
     * @throws exception if return of newLineItemId fails
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
     * @param conn is the session instance of java to database
     *
     * @param itemIDs is a vector of all itemID's
     *
     * @param employeeID is the current employee accessing the system
     *
     * @param newOrderId is the order id to be pushed/updated
     *
     * @returns void
     *
     * @throws exception if function fails to create the sql statement to be
     * inserted into order
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

    public ResultSet getMenu() {
        ResultSet r = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * from menuitems";
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return r;
    }

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
     * Given a time window, display the sales by item from the order history.
     * 
     * @param startTime The start time of the time window
     * 
     * @param endTime The end time of the time window
     * 
     * @return A ResultSet containing the sales report
     *
     * @brief SQL Table Setup:
     * Table orderlineitems: lineItemID int, itemID int, price float, orderID int
     * Table order: orderID int, timestamp datetime, employeeID int
     */
    public ResultSet getSalesReport(Date startTime, Date endTime) {
        ResultSet r = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT menuitems.name, SUM(orderlineitems.price) FROM orderlineitems JOIN menuitems ON orderlineitems.itemID = menuitems.menuItemID JOIN orders ON orderlineitems.orderID = orders.orderID WHERE orders.timestamp BETWEEN '"
                    + startTime + "' AND '" + endTime + "' GROUP BY menuitems.name";
            System.out.println(sqlStatement);
            r = stmt.executeQuery(sqlStatement);
            // print table
            while (r.next()) {
                System.out.println(r.getString(1) /*
                                                   * + " " + r.getString(2) + " " + r.getString(3) + " " +
                                                   * r.getString(4)
                                                   */);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    public static void main(String[] args) {
    }
} // end Class
