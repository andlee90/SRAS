package Database;

import CommModels.Device;
import CommModels.Devices;
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

                    insertDevice(1, "LED1", "LED", "AVAILABLE");
                    System.out.println("> [" + Main.getDate() + "] Default device added to devices table");

                    insertDevice(2, "LED2", "LED", "AVAILABLE");
                    System.out.println("> [" + Main.getDate() + "] Default device added to devices table");
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
    public static void insertUser(String user, String pass, String email, String fn, String ln, int r_id)
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
    public static void insertRole(String name)
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
     * Insert a new row into the devices table
     * @param pin device pin of the device to be inserted.
     * @param name device name of the device to be inserted.
     * @param type device type of the device to be inserted.
     * @param status device status of the device to be inserted.
     */
    public static void insertDevice(int pin, String name, String type, String status)
    {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(DBQueries.getInsertDeviceQuery()))
        {
            pstmt.setInt(1, pin);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setString(4, status);
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
        int userId;
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
            return 0;
        }

        return userId;
    }

    public static Devices selectAllDevices()
    {
        String sql = "SELECT device_pin,device_name,device_type,device_status FROM devices";
        Devices devices = new Devices();

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                Device device = new Device(rs.getInt("device_pin"),
                        rs.getString("device_name"),
                        getDeviceTypeFromString(rs.getString("device_type")),
                        rs.getString("device_status"));
                devices.addDevice(device);
            }
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }

        return devices;
    }

    /**
     * Convert the device type string stored in the db to a DeviceType
     * @param s the string from the db
     * @return the corresponding DeviceType
     */
    private static Device.DeviceType getDeviceTypeFromString(String s)
    {
        Device.DeviceType dt = null;

        if (s.equals("LED"))
        {
            dt = Device.DeviceType.LED;
        }
        else if (s.equals("ARM"))
        {
            dt = Device.DeviceType.ARM;
        }
        return dt;
    }
}
