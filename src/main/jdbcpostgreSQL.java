
// Java Backend postgress
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

//import javax.swing.JOptionPane;

// Command to run on Mac:
// cd src/main
// javac *.java
// java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL
public class jdbcpostgreSQL {
  public static float round(float d, int decimalPlace) {
    BigDecimal bd = new BigDecimal(Float.toString(d));
    bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
    return bd.floatValue();
  }

  public static int getNewOrderId(Connection conn) {
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

  public static int getNewLineItemId(Connection conn) {
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

  public static void updateOrdersAndOrderLineItemsTable(Connection conn, Vector<Integer> itemIDs, int employeeID,
      int newOrderId) {
    try {
      Statement stmt = conn.createStatement();
      // Get the time
      LocalDateTime orderTime = LocalDateTime.now();

      // Calculate the price & update orderlineitems table
      int newLineItemId = getNewLineItemId(conn);
      float totalPrice = 0;
      for (int i = 0; i < itemIDs.size(); i++) {
        // Get menu item information
        int itemID = itemIDs.elementAt(i);
        String sqlStatement = "select name, menuPrice from MenuItems where menuItemID=" + Integer.toString(itemID);
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
        sqlStatement = String.format("insert into orderlineitems values (%s, %s, %s, %s)",
            Integer.toString(newLineItemId), Integer.toString(itemID), Float.toString(itemPrice),

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
          Integer.toString(newOrderId), formattedDateTime, Float.toString(totalPrice), Integer.toString(employeeID));
      System.out.println(sqlStatement);
      System.out.println();
      stmt.executeUpdate(sqlStatement);
      System.out.println("Insert into order table successfully");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void updateInventoryTransactionsAndInventoryTable(Connection conn, int newOrderId) {
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
        String ingredientID = ingredients.elementAt(i);
        sqlStatement = "update inventory set curramount = curramount-" + qty.elementAt(i) + " where ingredientid="
            + ingredientID + ";";
        System.out.println(sqlStatement);

        stmt.executeUpdate(sqlStatement);
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String args[]) {
    // dbSetup hides my username and password
    dbSetup my = new dbSetup();
    // Building the connection
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(
          "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_4", my.user, my.pswd);
      conn.setAutoCommit(false);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    } // end try catch
    System.out.println("Opened database successfully");

    // Create a vector if menu item id
    Vector<Integer> vector = new Vector<Integer>();
    vector.add(1);
    vector.add(5);
    vector.add(6);
    vector.add(9);
    // Get the new order id
    int newOrderId = getNewOrderId(conn);
    // Insert into order and orderlineitems table
    updateOrdersAndOrderLineItemsTable(conn, vector, 5, newOrderId);
    // Update inventorytransactions and subtract from the inventory
    updateInventoryTransactionsAndInventoryTable(conn, newOrderId);

    // remember to do conn.commit() in the end to update the actual table
    try {
      // conn.commit();
      conn.close();
      System.out.println("Connection Closed.");
    } catch (Exception e) {
      System.out.println("Connection NOT Closed.");
    } // end try catch
  }// end main
}// end Class
