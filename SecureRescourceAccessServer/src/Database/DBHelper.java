package Database;

import CommModels.*;
import Main.Main;

import java.sql.*;
import java.util.ArrayList;

import static Database.DBQueries.*;

public class DBHelper
{
    private static String dbUrl = "jdbc:sqlite:sqlite/db/resources.db";

    /**
     * Connects to the existing resources.db or creates it from scratch using default settings.
     */
    public static void connectToOrCreateNewDB()
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
                    buildDefaultDB();
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
     * Insert a new row into the users table.
     * @param user  username of the user to be inserted.
     * @param pass  password of the user to be inserted.
     * @param email email of the user to be inserted.
     * @param fn    first name of the user to be inserted.
     * @param ln    last name of the user to be inserted.
     * @param r_id  role id of the user to be inserted.
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
     * Insert a new row into the roles table.
     * @param name role name of the role to be inserted.
     * @param priority priority of the role to be inserted (0 - n, 0 being highest).
     */
    public static void insertRole(String name, int priority)
    {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(DBQueries.getInsertRoleQuery()))
        {
            pstmt.setString(1, name);
            pstmt.setInt(2, priority);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }
    }

    /**
     * Insert a new row into the devices table.
     * @param pin    device pin of the device to be inserted.
     * @param name   device name of the device to be inserted.
     * @param status device status of the device to be inserted.
     * @param state  device state of the device to be inserted.
     */
    public static void insertDevice(int pin, String name, String type, String status, String state)
    {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(DBQueries.getInsertDeviceQuery()))
        {
            pstmt.setInt(1, pin);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setString(4, status);
            pstmt.setString(5, state);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }
    }

    /**
     * Returns a User object for the corresponding username and password from the users table.
     * @param username the username of the user to select.
     * @param password the password of the user to select.
     */
    public static User selectUserByUsernameAndPassword(String username, String password)
    {
        User user;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(getSelectUserByUsernameAndPassword()))
        {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            int userId = rs.getInt("user_id");
            String email = rs.getString("user_email");
            String firstName = rs.getString("user_first_name");
            String lastName = rs.getString("user_last_name");
            int roleId = rs.getInt("role_id");
            String roleName = selectRoleById(roleId);

            user = new User(userId, username, password, email, firstName, lastName, roleName);
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] DB: No such user found: " + e.getMessage());
            return null;
        }

        return user;
    }

    /**
     * Returns the name of the role for a given id.
     * @param id the id of the role to select.
     * @return the name of the selected role.
     */
    public static String selectRoleById(int id)
    {
        String roleName = "";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(getSelectRoleById()))
        {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            roleName = rs.getString("role_name");
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }

        return roleName;
    }

    /**
     * Return all devices currently stored in the db.
     * @return A Devices object containing a List of of Devices in the db.
     */
    public static Devices selectAllDevices()
    {
        Devices devices = new Devices();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(getSelectAllDevices()))
        {
            while (rs.next())
            {
                if (rs.getString("device_type").equals("LED"))
                {
                    Led led = new Led(rs.getInt("device_id"),
                            rs.getInt("device_pin"),
                            rs.getString("device_name"),
                            getDeviceStatusFromString(rs.getString("device_status")),
                            getLedStateFromString(rs.getString("device_state")));
                    devices.addDevice(led);
                }
                else if (rs.getString("device_type").equals("ARM"))
                {
                    Arm arm = new Arm(rs.getInt("device_id"),
                            new ArrayList<Integer>(rs.getInt("device_pin")),
                            rs.getString("device_name"),
                            getDeviceStatusFromString(rs.getString("device_status")),
                            getArmStateFromString(rs.getString("device_state")));
                    devices.addDevice(arm);
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("> [" + Main.getDate() + "] " + e.getMessage());
        }

        return devices;
    }

    /**
     * Updates the selected Device's information.
     * @param id the id of the Device to update.
     * @param pin the updated Device's pin.
     * @param name the updated Device's name.
     * @param type the updated Device's type.
     * @param status the updated Device's status.
     * @param state the updated Device's state.
     */
    public static void updateDevice(int id, int pin, String name, String type, String status, String state)
    {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(DBQueries.getUpdateDeviceQuery()))
        {
            pstmt.setInt(1, pin);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setString(4, status);
            pstmt.setString(5, state);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds default characteristics to a blank database.
     */
    private static void buildDefaultDB()
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

        insertRole("admin", 0);
        System.out.println("> [" + Main.getDate() + "] Default admin role added to roles table");

        insertUser("admin", "drowssap", "admin@admin.com", "admin", "admin", 1);
        System.out.println("> [" + Main.getDate() + "] Default admin user added to users table");

        insertDevice(1, "LED1", "LED", "AVAILABLE", "OFF");
        System.out.println("> [" + Main.getDate() + "] Default device added to devices table");

        insertDevice(2, "LED2", "LED", "AVAILABLE", "OFF");
        System.out.println("> [" + Main.getDate() + "] Default device added to devices table");

        insertDevice(3, "LED3", "LED", "AVAILABLE", "OFF");
        System.out.println("> [" + Main.getDate() + "] Default device added to devices table");

        insertDevice(4, "LED4", "LED", "AVAILABLE", "OFF");
        System.out.println("> [" + Main.getDate() + "] Default device added to devices table");

        insertDevice(5, "LED5", "LED", "AVAILABLE", "OFF");
        System.out.println("> [" + Main.getDate() + "] Default device added to devices table");

        insertDevice(9, "LED6", "LED", "AVAILABLE", "OFF");
        System.out.println("> [" + Main.getDate() + "] Default device added to devices table");

        insertDevice(7, "LED7", "LED", "AVAILABLE", "OFF");
        System.out.println("> [" + Main.getDate() + "] Default device added to devices table");

        insertDevice(8, "LED8", "LED", "AVAILABLE", "OFF");
        System.out.println("> [" + Main.getDate() + "] Default device added to devices table");
    }

    /**
     * Convert the device type string stored in the db to a DeviceType.
     * @param s the string from the db.
     * @return the corresponding DeviceType.
     */
    private static DeviceStatus getDeviceStatusFromString(String s)
    {
        DeviceStatus ds = null;

        if (s.equals("AVAILABLE"))
        {
            ds = DeviceStatus.AVAILABLE;
        }
        else if (s.equals("IN_USE"))
        {
            ds = DeviceStatus.IN_USE;
        }
        return ds;
    }

    /**
     * Convert the device type string stored in the db to a DeviceType.
     * @param s the string from the db.
     * @return the corresponding DeviceType.
     */
    private static LedState getLedStateFromString(String s)
    {
        LedState ls = null;

        if (s.equals("ON"))
        {
            ls = LedState.ON;
        }
        else if (s.equals("OFF"))
        {
            ls = LedState.OFF;
        }
        else if (s.equals("BLINKING"))
        {
            ls = LedState.BLINKING;
        }
        return ls;
    }

    /**
     * Convert the device type string stored in the db to a DeviceType.
     * @param s the string from the db.
     * @return the corresponding DeviceType.
     */
    private static ArmState getArmStateFromString(String s)
    {
        ArmState as = null;

        if (s.equals("ON"))
        {
            as = ArmState.ON;
        }
        else if (s.equals("OFF"))
        {
            as = ArmState.OFF;
        }
        return as;
    }
}