package Main;

import Networking.ServerManager;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        System.out.println("> [" + Main.getDate() + "] Starting new server... Ctrl+C to exit.");
        new ServerManager();
    }

    /**
     * Gets the current date and time.
     */
    public static String getDate()
    {
        java.util.Date date = new java.util.Date();
        String dateString = date.toString();

        return dateString.substring(4,19);
    }
}
