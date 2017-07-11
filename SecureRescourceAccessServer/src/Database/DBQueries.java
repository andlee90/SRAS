package Database;

class DBQueries
{
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
                + "role_name TEXT NOT NULL UNIQUE\n"
                + ");";
    }

    static String getRolePermissionsTableCreationQuery()
    {
        return "CREATE TABLE IF NOT EXISTS role_permissions (\n"
                + "role_permission_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "role_id INTEGER NOT NULL,\n"
                + "permission_id INTEGER NOT NULL,\n"
                + "FOREIGN KEY(role_id) REFERENCES roles(role_id),\n"
                + "FOREIGN KEY(permission_id) REFERENCES permissions(permission_id)\n"
                + ");";
    }

    static String getPermissionsTableCreationQuery()
    {
        return "CREATE TABLE IF NOT EXISTS permissions (\n"
                + "permission_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "permission_value TEXT NOT NULL UNIQUE,\n"
                + "device_id INTEGER NOT NULL,\n"
                + "FOREIGN KEY(device_id) REFERENCES devices(device_id)\n"
                + ");";
    }

    static String getDevicesTableCreationQuery()
    {
        return "CREATE TABLE IF NOT EXISTS devices (\n"
                + "device_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "device_pin INTEGER NOT NULL UNIQUE,\n"
                + "device_name TEXT NOT NULL UNIQUE,\n"
                + "device_type TEXT NOT NULL,\n"
                + "device_status TEXT NOT NULL\n"
                + ");";
    }

    static String getInsertUserQuery()
    {
        return "INSERT INTO users(user_username,user_password,user_email," +
                "user_first_name,user_last_name,role_id) VALUES(?,?,?,?,?,?)";
    }

    static String getInsertRoleQuery()
    {
        return "INSERT INTO roles(role_name) VALUES(?)";
    }

    static String getInsertDeviceQuery()
    {
        return "INSERT INTO devices(device_pin,device_name,device_type,device_status) VALUES(?,?,?,?)";
    }

    static String getSelectUserByUsernameAndPassword()
    {
        return "SELECT user_id FROM users WHERE user_username = ? AND user_password = ?";
    }
}
