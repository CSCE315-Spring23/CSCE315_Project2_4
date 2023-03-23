import java.sql.*;

/**
 * The dbSetup class provides a method for establishing a connection to a
 * PostgreSQL database.
 */
public final class dbSetup {
    /**
     * The PostgreSQL user to use when connecting to the database.
     */
    public static final String user = "csce315331_team_4_master";
    /**
     * The password for the PostgreSQL user to use when connecting to the database.
     */
    public static final String pswd = "@12345";

    /**
     * Returns a connection to the PostgreSQL database.
     *
     * @return A connection to the PostgreSQL database.
     */
    public Connection getConnection() {
        // Building the connection
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_4", user,
                    pswd);
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } // end try catch
        System.out.println("Opened database successfully");

        return conn;
    }
} // end class