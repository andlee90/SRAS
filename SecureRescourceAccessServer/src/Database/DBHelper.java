package Database;

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
                    System.out.println("> [DATE] Connected to resources.db");
                }
                else
                {
                    System.out.println("> [DATE] Created resources.db");

                    executeStatement(DBQueries.getRolesTableCreationQuery());
                    System.out.println("> [DATE] Roles table created");

                    executeStatement(DBQueries.getDevicesTableCreationQuery());
                    System.out.println("> [DATE] Devices table created");

                    executeStatement(DBQueries.getPermissionsTableCreationQuery());
                    System.out.println("> [DATE] Permissions table created");

                    executeStatement(DBQueries.getUsersTableCreationQuery());
                    System.out.println("> [DATE] Users table created");

                    executeStatement(DBQueries.getRolePermissionsTableCreationQuery());
                    System.out.println("> [DATE] Role-permissions linking table created");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("> [DATE] " + e.getMessage());
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
            System.out.println("> [DATE] " + e.getMessage());
        }
    }
}
