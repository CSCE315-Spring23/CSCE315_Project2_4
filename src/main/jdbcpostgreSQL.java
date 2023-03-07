
// Java Backend postgress
import java.sql.*;
import java.time.*;
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

  public static void updateOrdersAndOrderLineItemsTable(Connection conn, Vector<Integer> itemIDs, int employeeID) {
    try {
      Statement stmt = conn.createStatement();
      // Get new order id
      int newOrderId = getNewOrderId(conn);

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

        // Insert into orderlineitem
        sqlStatement = String.format("insert into orderlineitems values (%o, %o, %f, %o)",
            newLineItemId, itemID, itemPrice, newOrderId);
        System.out.println("OrderLineItems summary:");
        System.out.println("Line item id: " + Integer.toString(newLineItemId));
        System.out.println("Item id: " + Integer.toString(itemID));
        System.out.println("Item price: " + Float.toString(itemPrice));
        System.out.println("Order id: " + Integer.toString(newOrderId));
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
      System.out.println();

      // Generate sql statement to insert into order
      String sqlStatement = String.format(
          "insert into orders values (%o, %t, %f, %o)",
          newOrderId, orderTime, totalPrice, employeeID);
      stmt.executeUpdate(sqlStatement);
      System.out.println("Insert into order table successfully");
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
    try {
      // create a statement object
      Statement stmt = conn.createStatement();
      // create an SQL statement
      String sqlStatement = "SELECT name FROM employees";
      // send statement to DBMS
      ResultSet result = stmt.executeQuery(sqlStatement);

      // OUTPUT
      System.out.println("Employee names from the Database.");
      System.out.println("______________________________________");
      while (result.next()) {
        System.out.println(result.getString("name"));
      }
    } catch (Exception e) {
      System.out.println("Error accessing Database.");
    }

    // MAIN
    // remember to do conn.commit() in the end to update the actual table
    Vector<Integer> vector = new Vector<Integer>();
    vector.add(1);
    vector.add(5);
    vector.add(6);
    vector.add(9);
    updateOrdersAndOrderLineItemsTable(conn, vector, 5);

    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch (Exception e) {
      System.out.println("Connection NOT Closed.");
    } // end try catch
  }// end main
}// end Class
