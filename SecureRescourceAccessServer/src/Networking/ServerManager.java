package Networking;

import Database.DBHelper;
import Main.Main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Creates a server on a new thread and listens for incoming client connections.
 * For each new client up to MAX_CLIENTS, a new thread is created for management
 * of that client's requests.
 */
public class ServerManager extends Thread
{
    final private int MAX_CLIENTS = 100;
    final private ServerSocket serverSocket;

    private static ClientManager[] clientConnections;

    public ServerManager() throws IOException
    {
        clientConnections = new ClientManager[MAX_CLIENTS];
        this.serverSocket = new ServerSocket(50873);

        start(); // Start manager on a new thread
    }

    @Override
    public void run()
    {
        DBHelper.createDB(); // Create a new resource database if one does not already exist
        System.out.println("> [" + Main.getDate() + "] Listening on: " + serverSocket.getLocalPort());
        System.out.println("> [" + Main.getDate() + "] Total connected clients: 0/" + this.MAX_CLIENTS);

        Socket socket = null;

        while (!interrupted())
        {
            try
            {
                socket = this.serverSocket.accept();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            assignClientToThread(socket);
        }
        try
        {
            for(ClientManager st:clientConnections)
            {
                st.close(); // Close all child connections if this server is interrupted
                st.interrupt(); // Interrupt all child threads if this thread is interrupted
            }
            // TODO: Address already in use (Bind failed) when restarting server on same port.
            serverSocket.setReuseAddress(true);
            serverSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Assigns each new client connection to it's own manager until MAX_CLIENTS is reached
     */
    private void assignClientToThread(Socket socket)
    {
        for (int i = 0; i <= this.MAX_CLIENTS; i++)
        {
            if(i == this.MAX_CLIENTS)
            {
                System.out.println("> [" + Main.getDate() + "] WARNING: Maximum client connections reached");
                break;
            }
            else if(clientConnections[i] == null)
            {
                clientConnections[i] = new ClientManager(socket, i);
                break;
            }
        }
    }
}
