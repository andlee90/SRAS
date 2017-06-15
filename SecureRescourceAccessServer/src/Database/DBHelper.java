package Database;

import Main.Main;

import java.sql.*;

public class DBHelper
{
    private static String dbUrl = "jdbc:sqlite:sqlite/db/resources.db";

    public static void createDB()
    {
        try (Connection conn = DriverManager.getConnection(dbUrl))
        {
            if (conn != null)
            {
                DatabaseMetaData dbm = conn.getMetaData();
                ResultSet tables = dbm.getTables(null, null, "Users", null);
                if (tables.next())
                {
                    System.out.println("> [" + Main.getDate() + "] Connected to resources.db");
                }
                else
                {
                    System.out.println("> [" + Main.getDate() + "] Created resources.db");

                    executeStatement(DBQueries.getRolesTableCreationQuery());
                    System.out.println("> [" + Main.getDate() + "] Roles table created");

                    executeStatement(DBQueries.getDevicesTableCreationQuery());
                    System.out.println("> [" + Main.getDate() + "] Devices table created");

                    executeStatement(DBQueries.getPermissionsTableCreationQuery());
                    System.out.println("> [" + Main.getDate() + "] Permissions table created");

                    executeStatement(DBQueries.getUsersTableCreationQuery());
                    System.out.println("> [" + Main.getDate() + "] Users table created");

                    executeStatement(DBQueries.getRolePermissionsTableCreationQuery());
                    System.out.println("> [" + Main.getDate() + "] Role-permissions linking table created");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }
    }

    /**
     * Default SQL execution method.
     */
    private static void executeStatement(String sql)
    {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }
    }
}
