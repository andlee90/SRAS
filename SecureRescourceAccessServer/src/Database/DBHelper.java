package Database;

import Main.Main;

import java.sql.*;

import static Database.DBQueries.getSelectUserByUsernameAndPassword;

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

                    insertRole("admin");
                    System.out.println("> [" + Main.getDate() + "] Default admin role added to roles table");

                    insertUser("admin", "drowssap", "admin@admin.com", "admin", "admin", 1);
                    System.out.println("> [" + Main.getDate() + "] Default admin user added to users table");
                }

            }
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }
    }

    /**
     * Creates a connection to resources.db.
     */
    private static Connection connect()
    {
        Connection conn = null;
        try
        {
            conn = DriverManager.getConnection(dbUrl);
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }
        return conn;
    }

    /**
     * Default SQL execution method.
     */
    private static void executeStatement(String sql)
    {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }
    }

    /**
     * Insert a new row into the users table
     *
     * @param user username of the user to be inserted.
     * @param pass password of the user to be inserted.
     * @param email email of the user to be inserted.
     * @param fn first name of the user to be inserted.
     * @param ln last name of the user to be inserted.
     * @param r_id role id of the user to be inserted.
     */
    //TODO: r_id should be role name and should perform a query for the name's id
    static void insertUser(String user, String pass, String email, String fn, String ln, int r_id)
    {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(DBQueries.getInsertUserQuery()))
        {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setString(3, email);
            pstmt.setString(4, fn);
            pstmt.setString(5, ln);
            pstmt.setInt(6, r_id);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }
    }

    /**
     * Insert a new row into the roles table
     * @param name role name of the role to be inserted.
     */
    static void insertRole(String name)
    {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(DBQueries.getInsertRoleQuery()))
        {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }
    }

    /**
     * Returns an user id for the corresponding username and password from the users table.
     *
     * @param username the username of the user to select.
     * @param password the password of the user to select.
     */
    public static int selectUserIdByUsernameAndPassword(String username, String password)
    {
        int userId = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(getSelectUserByUsernameAndPassword()))
        {
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            ResultSet rs    = pstmt.executeQuery();

            userId = rs.getInt("user_id");
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] DB: No such user found: " + e.getMessage());
        }

        return userId;
    }
}
