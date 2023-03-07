
// Java Backend postgress
import java.sql.*;

//import javax.swing.JOptionPane;

/*
CSCE 315
9-25-2019 Original
2/7/2020 Update for AWS
 */
public class jdbcpostgreSQL {
  public static int getNewOrderId() {
    dbSetup my = new dbSetup();
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(
          "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_4", my.user, my.pswd);
      Statement stmt = conn.createStatement();
      String sqlStatement = "select max(orderid) from orders;";
      ResultSet result = stmt.executeQuery(sqlStatement);
      // while (result.next()) {
      // int newOrderId = result.getInt(0) + 1;
      // return newOrderId;
      // }
      while (result.next()) {
        System.out.println(result.getString("name"));
      }
      return 1;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return -1;
  }

  public static void main(String args[]) {
    // dbSetup hides my username and password
    dbSetup my = new dbSetup();
    // Building the connection
    Connection conn = null;
    try {
      // Class.forName("org.postgresql.Driver");
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
    int newOrderId = getNewOrderId();
    System.out.println("New order id:" + Integer.toString(newOrderId));
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch (Exception e) {
      System.out.println("Connection NOT Closed.");
    } // end try catch
  }// end main
}// end Class
