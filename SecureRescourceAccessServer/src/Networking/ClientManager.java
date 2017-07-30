package Networking;

import CommModels.*;
import Database.DBHelper;
import Main.Main;
import Resources.DeviceController;
import Resources.DeviceControllerFactory;

import java.io.EOFException;
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
    private volatile DeviceController dc = null;

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
            Message connectionMessage;

            if (authenticateUser(user))
            {
                serverOutputStream.writeObject(user);

                connectionMessage = new Message("Logged in as " + user.getUserName());
                serverOutputStream.writeObject(connectionMessage);

                Message userAddress = (Message) serverInputStream.readObject();
                System.out.println("> [" + Main.getDate() + "] " + user.getUserName() + "@" + userAddress.getMessage() + " connected");

                while(!interrupted())
                {
                    try
                    {
                        Object object = serverInputStream.readObject();

                        if(object instanceof Message)
                        {
                            Message message = (Message) object;
                            System.out.println("> [" + Main.getDate() + "] " + message.getMessage());
                        }

                        else if (object instanceof Devices)
                        {
                            Devices devices = getDevices();
                            serverOutputStream.writeObject(devices);
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
                                Enum deviceState = dc.issueCommand(command.getCommandType());
                                device.setDeviceState(deviceState);
                                serverOutputStream.reset(); // disregard the state of any Device already written to the stream
                                serverOutputStream.writeObject(device);
                            }
                        }
                    }
                    catch (EOFException e)
                    {
                        System.out.println("> [" + Main.getDate() + "] " + user.getUserName() + "@" + userAddress.getMessage() + " disconnected");
                        close();
                        break;
                    }
                }
            }
            else
            {
                serverOutputStream.writeObject(user);

                connectionMessage = new Message("Incorrect username or password");
                connectionMessage.setState(false);
                serverOutputStream.writeObject(connectionMessage);

                close();
            }
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
