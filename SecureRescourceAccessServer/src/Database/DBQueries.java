package Database;

/**
 * Returns various SQL query strings.
 */
class DBQueries
{
    /**
     * The following query strings are used for table creation.
     */
    static String getUsersTableCreationQuery()
    {
        return "CREATE TABLE IF NOT EXISTS users (\n"
                + "user_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "user_username TEXT NOT NULL UNIQUE,\n"
                + "user_password TEXT NOT NULL,\n"
                + "user_email TEXT NOT NULL,\n"
                + "user_first_name TEXT NOT NULL,\n"
                + "user_last_name TEXT NOT NULL,\n"
                + "role_id INTEGER NOT NULL,\n"
                + "FOREIGN KEY(role_id) REFERENCES roles(role_id)\n"
                + ");";
    }

    static String getRolesTableCreationQuery()
    {
        return "CREATE TABLE IF NOT EXISTS roles (\n"
                + "role_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "role_name TEXT NOT NULL UNIQUE,\n"
                + "role_priority INT NOT NULL\n"
                + ");";
    }

    static String getRulesTableCreationQuery()
    {
        return "CREATE TABLE IF NOT EXISTS rules (\n"
                + "rule_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "role_id INTEGER NOT NULL,\n"
                + "permission_id INTEGER NOT NULL,\n"
                + "device_id INTEGER NOT NULL,\n"
                + "FOREIGN KEY(role_id) REFERENCES roles(role_id),\n"
                + "FOREIGN KEY(permission_id) REFERENCES permissions(permission_id),\n"
                + "FOREIGN KEY(device_id) REFERENCES devices(device_id)\n"
                + ");";
    }

    static String getPermissionsTableCreationQuery()
    {
        return "CREATE TABLE IF NOT EXISTS permissions (\n"
                + "permission_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "permission_value TEXT NOT NULL UNIQUE\n"
                + ");";
    }

    static String getDevicesTableCreationQuery()
    {
        return "CREATE TABLE IF NOT EXISTS devices (\n"
                + "device_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "device_pin INTEGER NOT NULL UNIQUE,\n"
                + "device_name TEXT NOT NULL UNIQUE,\n"
                + "device_type TEXT NOT NULL,\n"
                + "device_status TEXT NOT NULL,\n"
                + "device_state TEXT NOT NULL\n"
                + ");";
    }

    /**
     * The following query strings are used for row insertion.
     */
    static String getInsertUserQuery()
    {
        return "INSERT INTO users(user_username,user_password,user_email," +
                "user_first_name,user_last_name,role_id) VALUES(?,?,?,?,?,?)";
    }

    static String getInsertRoleQuery()
    {
        return "INSERT INTO roles(role_name,role_priority) VALUES(?,?)";
    }

    static String getInsertPermissionQuery()
    {
        return "INSERT INTO permissions(permission_value) VALUES(?)";
    }

    static String getInsertDeviceQuery()
    {
        return "INSERT INTO devices(device_pin,device_name,device_type,device_status,device_state) VALUES(?,?,?,?,?)";
    }

    static String getInsertRuleQuery()
    {
        return "INSERT INTO rules(role_id,permission_id,device_id) VALUES(?,?,?)";
    }

    /**
     * The following query strings are used for row selections.
     */
    static String getSelectUserByUsernameAndPassword()
    {
        return "SELECT user_id,user_email,user_first_name,user_last_name,role_id " +
                "FROM users WHERE user_username = ? AND user_password = ?";
    }

    static String getSelectAllDevices()
    {
        return "SELECT device_id,device_pin,device_name,device_type,device_status,device_state FROM devices";
    }

    static String getSelectRoleById()
    {
        return "SELECT role_name FROM roles WHERE role_id = ?";
    }

    static String getSelectRolePriorityById()
    {
        return "SELECT role_priority FROM roles WHERE role_id = ?";
    }

    static String getSelectRoleIdByName()
    {
        return "SELECT role_id FROM roles WHERE role_name = ?";
    }


    static String getSelectRulesByRoleId()
    {
        return "SELECT permission_id,device_id FROM rules WHERE role_id = ?";
    }

    static String getSelectDeviceIdByName()
    {
        return "SELECT device_id FROM devices WHERE device_name = ?";
    }

    static String getSelectDeviceNameById()
    {
        return "SELECT device_name FROM devices WHERE device_id = ?";
    }

    static String getSelectPermissionValueById()
    {
        return "SELECT permission_value FROM permissions WHERE permission_id = ?";
    }

    static String getSelectPermissionIdByValue()
    {
        return "SELECT permission_id FROM permissions WHERE permission_value = ?";
    }

    /**
     * The following query strings are used for row updates.
     */
    static String getUpdateDeviceQuery()
    {
        return "UPDATE devices SET device_pin = ? , "
                + "device_name = ? , "
                + "device_type = ? , "
                + "device_status = ? , "
                + "device_state = ? "
                + "WHERE device_id = ?";
    }
}
