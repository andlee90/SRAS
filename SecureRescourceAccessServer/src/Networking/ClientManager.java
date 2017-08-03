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
 * Manages a single client connection by first comparing the incoming user object attributes against those in the
 * database. The socket is closed if authentication fails. Otherwise, communication with the client continues.
 */
public class ClientManager extends Thread
{
    private int threadId;
    private ClientManager[] clientConnections;

    private User authenticatedUser;
    private String authenticatedUserName;
    private String authenticatedUserRole;
    private String authenticatedUserAddress;

    private Socket socket;
    private ObjectInputStream serverInputStream;
    private ObjectOutputStream serverOutputStream;

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
            serverInputStream = new ObjectInputStream(socket.getInputStream());
            serverOutputStream = new ObjectOutputStream(socket.getOutputStream());

            Object initialObject = serverInputStream.readObject(); // Check initial object for a possible connection test.

            if (initialObject instanceof Message)
            {
                // TODO: This may be the cause of a Connection Reset Exception being thrown. Needs fixing.
                clientConnections[threadId] = null; // Clear index
                close(); // Terminate socket
                this.interrupt();
            }
            else if (initialObject instanceof User)
            {
                User user = (User) initialObject;

                Message connectionMessage;

                if (authenticateUser(user))
                {
                    authenticatedUserName = authenticatedUser.getUserName();
                    authenticatedUserRole = authenticatedUser.getRole();

                    connectionMessage = new Message("Logged in as " + authenticatedUserName);
                    serverOutputStream.writeObject(connectionMessage);

                    authenticatedUserAddress = socket.getRemoteSocketAddress().toString();
                    int end = authenticatedUserAddress.indexOf(':');
                    authenticatedUserAddress = authenticatedUserAddress.substring(1, end);

                    System.out.println("> [" + Main.getDate() + "] " + authenticatedUserName + "@"
                            + authenticatedUserAddress + " connected");

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

                                    // disregard the state of any Device already written to the stream
                                    serverOutputStream.reset();
                                    serverOutputStream.writeObject(device);
                                }
                            }
                        }
                        catch (EOFException e)
                        {
                            System.out.println("> [" + Main.getDate() + "] " + user.getUserName() + "@"
                                    + authenticatedUserAddress + " disconnected");
                            clientConnections[threadId] = null; // Clear index
                            close(); // Terminate socket
                            this.interrupt(); // Terminate thread
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

                    clientConnections[threadId] = null; // Clear index
                    close(); // Terminate socket
                    this.interrupt(); // Terminate thread
                }
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
        this.serverInputStream.close();
        this.serverOutputStream.close();
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

    /**
     * @return A Devices object containing a List of all Devices stored in the db.
     */
    private Devices getDevices()
    {
        return DBHelper.selectAllDevices();
    }

    /**
     * @return This thread's index in the ClientConnections[].
     */
    public int getThreadId()
    {
        return threadId;
    }

    /**
     * @return This thread's User.
     */
    public String getAuthenticatedUserName()
    {
        return authenticatedUserName;
    }

    /**
     * @return This thread's User's role.
     */
    public String getAuthenticatedUserRole()
    {
        return authenticatedUserRole;
    }

    /**
     * @return This thread's User's address.
     */
    public String getAuthenticatedUserAddress()
    {
        return authenticatedUserAddress;
    }
}
