import java.sql.*;

public final class dbSetup {
    public static final String user = "csce315331_team_4_master";
    public static final String pswd = "@12345";

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