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
    private User authenticatedUser;
    private String userName;
    private String userRole;
    private String userAddress;

    private Socket socket;
    private ClientManager[] clientConnections;

    private volatile Device device = null;
    private volatile DeviceController dc = null;

    ClientManager(Socket p, int id, ServerManager sm)
    {
        this.socket = p;
        this.threadId = id;
        this.clientConnections = sm.getClientConnections();
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
                userName = authenticatedUser.getUserName();
                userRole = authenticatedUser.getRole();

                serverOutputStream.writeObject(authenticatedUser);

                connectionMessage = new Message("Logged in as " + userName);
                serverOutputStream.writeObject(connectionMessage);

                Message userAddressMessage = (Message) serverInputStream.readObject();
                userAddress = userAddressMessage.getMessage();
                System.out.println("> [" + Main.getDate() + "] " + userName + "@" + userAddress + " connected");

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
                        System.out.println("> [" + Main.getDate() + "] " + user.getUserName() + "@" + userAddress + " disconnected");
                        clientConnections[threadId] = null;
                        close();
                        this.interrupt();
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
        User user = DBHelper.selectUserByUsernameAndPassword(u.getUserName(), u.getPassword());

        if (user != null && user.getUserId() != 0)
        {
            authenticatedUser = user;
            authenticatedUser.setValidity(true);
        }

        return authenticatedUser.getValidity();
    }

    private Devices getDevices()
    {
        return DBHelper.selectAllDevices();
    }

    public int getThreadId()
    {
        return threadId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserRole()
    {
        return userRole;
    }

    public String getUserAddress()
    {
        return userAddress;
    }
}
