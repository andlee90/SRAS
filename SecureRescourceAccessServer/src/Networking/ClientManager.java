package Networking;

import CommModels.Devices;
import CommModels.Message;
import CommModels.User;
import Database.DBHelper;
import Main.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * Manages a single client connection by first comparing the incoming
 * user object attributes against those in the database. The socket is
 * closed if authentication fails. Otherwise, communication with the
 * client continues.
 */
public class ClientManager extends Thread
{
    private int threadId;
    private Socket socket;

    ClientManager(Socket p, int id)
    {
        this.socket = p;
        this.threadId = id;
        start(); // Start on a new thread
    }

    @Override
    public void run()
    {
        try
        {
            ObjectInputStream serverInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream serverOutputStream = new ObjectOutputStream(socket.getOutputStream());

            User user = (User)serverInputStream.readObject();

            if (authenticateUser(user))
            {
                serverOutputStream.writeObject(user);
                Message message = (Message)serverInputStream.readObject(); // Get test message from client
                System.out.println("> [" + Main.getDate() + "] " + message.getMessage());

                message.setMessage("And a hello to you too");
                serverOutputStream.writeObject(message);

                serverOutputStream.writeObject(getDevices());
            }
            serverOutputStream.writeObject(user);
            close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Closes current socket
     */
    void close() throws IOException
    {
        this.socket.close();
    }

    private boolean authenticateUser(User u)
    {
        int userId = DBHelper.selectUserIdByUsernameAndPassword(u.getUserName(), u.getPassword());

        if (userId != 0)
        {
            u.setValidity(true);
        }

        return u.getValidity();
    }

    private Devices getDevices()
    {
        return DBHelper.selectAllDevices();
    }
}
