package Database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerListDatabase
{
    public static void createNewDatabase(String fileName)
    {

        String url = "jdbc:sqlite:C:/Users/Tim/Documents/SRAS/SRAS-DesktopClient/src/Database/" + fileName;

        try (Connection conn = DriverManager.getConnection(url))
        {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}
