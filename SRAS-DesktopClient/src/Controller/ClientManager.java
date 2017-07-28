package Controller;

import CommModels.*;
import DesktopGUI.AuthenticationPanel;
import DesktopGUI.DeviceControlPanel;
import DesktopGUI.MainErrorMessageFrame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Tim on 7/23/2017.
 */

public class ClientManager {
    static int rowConst;
    public static Socket socket;
    public static ObjectOutputStream clientOutputStream;
    public static ObjectInputStream clientInputStream;
    public static void createSocket(int row)throws IOException{
        rowConst = row;
        String ip = DesktopClientController.data[row][0].toString();
        int port = Integer.parseInt(DesktopClientController.data[row][1].toString());
        // System.out.println("You are connecting to: "+ip+ " on port "+ port+"...");
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(5000);
            DesktopClientController.replacePanel(new AuthenticationPanel().getAuthenticationPanel(), "SRAS - Authentication");
        }catch (IOException e1)
        {
            new MainErrorMessageFrame("This Server is currently offline.");
        }
    }

    public static void connectToServer() throws IOException {
        try {

            String ip = DesktopClientController.data[rowConst][0].toString();
            int port = Integer.parseInt(DesktopClientController.data[rowConst][1].toString());
            socket = new Socket(ip, port);
            socket.setSoTimeout(5000);

            Message message = new Message(DesktopClientController.userIn.getUserName()+" @ " + InetAddress.getLocalHost()+" has connected to the server.");
            clientOutputStream = new ObjectOutputStream(socket.getOutputStream());

            clientOutputStream.writeObject(DesktopClientController.userIn);
            clientInputStream = new ObjectInputStream(socket.getInputStream());
            DesktopClientController.userIn = (User) clientInputStream.readObject();
            if (DesktopClientController.userIn.getValidity()) {
                clientOutputStream.writeObject(message);
                DesktopClientController.devices = (Devices) clientInputStream.readObject();
                DesktopClientController.replacePanel(new DeviceControlPanel().getPanel(),"SRAS - Device Controller");
            }
        }
        catch(ClassNotFoundException e2){
            e2.printStackTrace();
            new MainErrorMessageFrame(e2.getLocalizedMessage());
        }

    }

    public static void sendCommand(Device device, Command command) throws IOException, ClassNotFoundException {
        //send current device here**
        clientOutputStream.writeObject((Led)device);
        clientOutputStream.writeObject(command);
    }
}
