import Database.DBHelper;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("Starting new server...");
        System.out.println("Ctrl+C to exit...");

        DBHelper.createDB();
    }
}
