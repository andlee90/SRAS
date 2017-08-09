package Database;

import CommModels.*;

import java.sql.*;
import java.util.ArrayList;

public class DBHelper {
    static String dbUrl = "jdbc:sqlite:C:\\Users\\Tim\\Documents\\SRAS\\SRAS-DesktopClient\\databases\\servers.db";

    /**
     * Connects to the existing resources.db or creates it from scratch using default settings.
     */
    public static void connectToOrCreateNewDB() {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            if (conn != null) {
                DatabaseMetaData dbm = conn.getMetaData();
                ResultSet tables = dbm.getTables(null, null, "Users", null);

                if (tables.next()) {
                    System.out.println("Now connected to servers.db");
                }

            }
        } catch (SQLException e) {
            System.out.println("Error connecting");
        }
    }

    public static void createNewServerTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:\\Users\\Tim\\Documents\\SRAS\\SRAS-DesktopClient\\databases\\servers.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS servers (\n"
                + "	server_id INTEGER NOT NULL PRIMARY KEY UNIQUE,\n"
                + "	server_name TEXT UNIQUE NOT NULL,\n"
                + "	server_address TEXT NOT NULL,\n"
                + "	server_port INTEGER NOT NULL,\n"
                + "	server_username TEXT NOT NULL,\n"
                + "	server_password TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement())
        {
            // create a new table
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public static void insert(String serverName, String ip, int port, String username, String password) {
        String sql = "INSERT INTO servers(server_name, server_address, server_port, server_username, server_password) VALUES(?,?,?,?,?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, serverName);
            pstmt.setString(2, ip);
            pstmt.setInt(3, port);
            pstmt.setString(4, username);
            pstmt.setString(5, password);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a connection to resources.db.
     */
    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}