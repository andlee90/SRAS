package Main;

import Networking.ServerManager;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        System.out.println("Starting new server...");
        System.out.println("Ctrl+C to exit...");

        new ServerManager();
    }
}
