package Networking;

import CommModels.*;
import Database.DBHelper;
import Main.Main;
import Resources.DeviceController;
import Resources.DeviceControllerFactory;

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
    private volatile Device device = null;

    ClientManager(Socket p, int id)
    {
        this.socket = p;
        this.threadId = id;
        start();
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
                serverOutputStream.writeObject(getDevices());

                DeviceController dc = null;

                while(!interrupted())
                {
                    Object object = serverInputStream.readObject();

                    if(object instanceof Message)
                    {
                        Message message = (Message) object;
                        System.out.println("> [" + Main.getDate() + "] " + message.getMessage());
                    }

                    else if (object instanceof Device)
                    {
                        device = (Device) object;
                        dc = DeviceControllerFactory.getDeviceController(device);
                    }

                    else if (object instanceof Command)
                    {
                        Command command = (Command) object;
                        if (dc != null)
                        {
                            device.setDeviceState(dc.issueCommand(command.getCommandType()));
                            serverOutputStream.reset(); // disregard the state of any Device already written to the stream
                            serverOutputStream.writeObject(device);
                        }
                    }
                }
            }
            serverOutputStream.writeObject(user);
            close();
        }
        catch (IOException | ClassNotFoundException | InterruptedException e)
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

    /**
     * Authenticates the received user credentials by querying for them in the db.
     * @param u the User containing the credentials to be checked.
     */
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
