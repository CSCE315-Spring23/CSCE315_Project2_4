
// Java Backend postgress
import java.sql.*;
import java.time.*;
import java.util.*;

//import javax.swing.JOptionPane;

// Command to run on Mac:
// cd src/main
// javac *.java
// java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL
public class jdbcpostgreSQL {
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

  public static void updateOrdersTable(Connection conn, Vector<Integer> itemID, int employeeID) {
    try {
      Statement stmt = conn.createStatement();
      // Get new order id
      int newOrderId = getNewOrderId(conn) + 1;

      // Get the time
      LocalDateTime orderTime = LocalDateTime.now();

      // Calculate the price
      float totalPrice = 0;
      for (int i = 0; i < itemID.size(); i++) {
        int item = itemID.elementAt(i);
        String sqlStatement = "select name, menuPrice from MenuItems where menuItemID=" + Integer.toString(item);
        ResultSet result = stmt.executeQuery(sqlStatement);
        result.next();
        totalPrice += result.getFloat(2);
        System.out.println("Item " + result.getString("name") + " has price of " + Float.toString(result.getFloat(2)));
      }

      System.out.println("Order summary:");
      System.out.println("Order id: " + Integer.toString(newOrderId));
      System.out.println("Total price: " + Float.toString(totalPrice));
      System.out.println("Date time: " + orderTime.toString());

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
    Vector<Integer> vector = new Vector<Integer>();
    vector.add(1);
    vector.add(5);
    vector.add(6);
    vector.add(9);
    updateOrdersTable(conn, vector, 5);
    updateOrderLineItemsTable(conn, vector);

    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch (Exception e) {
      System.out.println("Connection NOT Closed.");
    } // end try catch
  }// end main
}// end Class
